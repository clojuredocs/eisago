(ns eisago.es-spec
  (:require [speclj.core :refer :all]
            [eisago.es :refer :all]
            [clj-http.client :as http]
            [cheshire.core :as json]
            [eisago.config :refer [config]]))

(describe "MD5 hashing"
          (it "should return the correct hex for MD5 sums of strings"
              (should= "15d7f6357f08bf4d7fb1c2665e7ebc19"
                       (md5 "eisago"))))

(defn return-args [& args] args)

(describe "Creating an index"
          (let [result (with-redefs [http/post return-args
                                     mapping   (fn [& args] "mapping")
                                     config    (fn [& args] {})]
                         (create-index "mock-index"))]

            (it "should POST to elasticsearch to create an index"
                (should= "http://localhost:9200/mock-index" (first result)))

            (it "should include the default mapping and settings in the POST body"
                (let [body (json/parse-string (:body (second result)))]
                  (should= "mapping" (body "mappings"))
                  (should= {"number_of_replicas" 0 "number_of_shards" 10}
                           (body "settings"))))))

(describe "Deleting an index"
          (let [result (with-redefs [http/delete return-args
                                     config      (fn [& args] {:es-http-opts "options"})]
                         (delete-index "mock-index"))]

            (it "should DELETE the index from elasticsearch"
                (should= "http://localhost:9200/mock-index" (first result)))
            (it "should include the default HTTP options"
                (should= {:es-http-opts "options"} (second result)))))

(describe "Checking for an index"
          (let [index-exists (with-redefs [http/head (fn [& args] {:status 200})]
                               (index-exists?))
                not-found    (with-redefs [http/head (fn [& args] {:status 404})]
                               (index-exists?))]

            (it "returns true if the index is found"
                (should index-exists))

            (it "returns false if the index is not found"
                (should-not not-found))))


