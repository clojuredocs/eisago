(ns eisago.api.interface
  (:require [eisago.config :refer [config]]
            [eisago.es :as es]))

(defn missing
  "Response for non-matching API requests."
  [encoder req]
  {:status 404 :body (encoder {:status 404 :message "Invalid request"})})

(defn error
  "Response for API 500 errors"
  [encoder req ex]
  {:status 500 :body (encoder {:status 500
                              :message "There was an error"
                              :error (.getMessage ex)})})

(defn search
  "API implementation of searching, lib and namespace can be optionally
  specified as paths, with name and query optionally specified in
  json query-string."
  ([encoder {:keys [query-string] :as req}]
     (if query-string
       (let [results (es/search query-string)]
         {:status 200 :body (encoder results)})
       {:status 404 :body "Must specify a query!"}))
  ([encoder req lib]
     (search (update-in req [:query-string] assoc :lib lib) encoder))
  ([encoder req lib namespace]
     (search (-> req
                 (update-in [:query-string] assoc :lib lib)
                 (update-in [:query-string] assoc :ns namespace))
             encoder)))

(defn children-for
  "API implementation of returning examples and comments for a given method/id."
  ([encoder request id]
     {:status 200 :body (-> (es/meta-for id)
                            :children
                            encoder)})
  ([encoder request lib version namespace varname]
     {:status 200 :body (-> (es/meta-for lib namespace varname)
                            :children
                            encoder)}))

(defn doc-for
  "API implementation of returning all information for a given method/id."
  ([encoder request id]
     {:status 200 :body (encoder (es/meta-for id))})
  ([encoder request lib version namespace varname]
     {:status 200 :body (encoder (es/meta-for lib version namespace varname))}))

(defn stats
  "API implementation for statistics"
  [encoder request]
  {:status 200
   :body (encoder {:total (or (es/es-count) 0)
                   :projects (or (es/es-count :project) 0)
                   :vars (or (es/es-count :var) 0)
                   :examples (or (es/es-count :example) 0)
                   :comments (or (es/es-count :comment) 0)})})

(defn all-projects
  "API implementation for returning all projects"
  [encoder request]
  {:status 200 :body (encoder (es/all-projects))})
