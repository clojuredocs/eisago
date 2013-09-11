(ns eisago.http
  (:require [eisago.api.json :as api]
            [eisago.config :refer [config]]
            [laeggen.core :as laeggen]
            [laeggen.dispatch :as dispatch]))

(def doc-routes
  [#"^/doc/([^/]+)/?$"
   #"^/doc/([^/]+)/([0-9\.]+)/([^/]+)/([^/]+)/?$"])

(def children-routes
   [#"^/meta/([^/]+)/?$"
    #"^/meta/([^/]+)/([^/]+)/([^/]+)/?$"])

(def search-routes
   [#"^/([^/]+)/([^/]+)/_search/?$"
    #"^/([^/]+)/_search/?$"
    #"^/_search/?$"])

(def all-projects-route #"^/v1/_projects/?")

(def stats-route #"^/v1/_stats/?$")

(def api-urls
  (dispatch/urls

   doc-routes
   #'api/doc-for

   children-routes
   #'api/children-for

   search-routes
   #'api/search

   all-projects-route
   #'api/all-projects

   stats-route
   #'api/stats

   :404 #'api/missing
   :500 #'api/error))

(defn start-api-server []
  (laeggen/start {:port (config :api-port)
                  :urls api-urls}))

;; (do (swap! laeggen/routes assoc (config :api-port) (dispatch/merge-urls laeggen.views/default-urls api-urls)) (swap! laeggen/routes assoc (config :web-port) (dispatch/merge-urls laeggen.views/default-urls web-urls)))
