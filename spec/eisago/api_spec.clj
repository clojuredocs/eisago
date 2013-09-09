(ns eisago.api-spec
  (:require [speclj.core :refer :all]
            [eisago.api.json :refer :all]
            [eisago.es :as es]
            [cheshire.core :as json]))

(describe "Non-matching API requests"
          (let [no-match (missing :req)]
            (it "should return a 404 status code"
                (should= 404 (:status no-match)))
            (it "should return an invalid request message"
                (should-contain "Invalid request" (:body no-match)))))

(describe "API 500 errors"
          (let [message   "A terrible horrible error occured."
                exception (Exception. message)
                error     (error :req exception)]
            (it "should return a 500 status code"
                (should= 500 (:status error)))
            (it "should return an error message"
                (should-contain message (:body error)))))

(defn es-count-default [& _])

(defn es-count-found
  ([]  5)
  ([_] 2))

(describe "Statistics"
          (let [default     (with-redefs [es/es-count es-count-default]
                              (stats :req))
                found-stats (with-redefs [es/es-count es-count-found]
                              (stats :req))]
            (it "should return a 200 status code"
                (should= 200 (:status default) (:status found-stats)))
            (it "should return zeroes by default"
                (let [body (json/parse-string (:body default))]
                  (should= 0 (body "total"))
                  (should= 0 (body "projects"))
                  (should= 0 (body "vars"))
                  (should= 0 (body "examples"))
                  (should= 0 (body "comments"))))
            (it "should return statistics when found"
                (let [body (json/parse-string (:body found-stats))]
                  (should= 5 (body "total"))
                  (should= 2 (body "projects"))
                  (should= 2 (body "vars"))
                  (should= 2 (body "examples"))
                  (should= 2 (body "comments"))))))

(defn all-projects-mock []
  [{:description "Mock project"}])

(describe "All projects"
          (let [projects (with-redefs [es/all-projects all-projects-mock]
                           (all-projects :req))]
            (it "should return a 200 status code"
                (should= 200 (:status projects)))
            (it "should return all projects found"
                (should-contain "Mock project" (:body projects))
                (should= 1 (count (json/parse-string (:body projects)))))))
