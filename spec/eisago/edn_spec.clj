(ns eisago.edn-spec
  (:require [speclj.core :refer :all]
            [eisago.api.edn :refer :all]
            [clojure.edn :refer [read-string]]
            [eisago.es :as es]))

(defn encode [encoder data]
  (encoder data))

(describe "EDN encoder" 
          (it "serializes Clojure data to strings"
              (should= "{:a 1, :c 3, :b \"2\"}"
                       ((as-edn encode) {:a 1 :b "2" :c 0x3}))))

(describe "Errors"
          (it "returns a 404 with an error message"
              (let [not-found (missing :req)
                    body (read-string (not-found :body))]
                (should= 404 (not-found :status))
                (should= 404 (body :status))
                (should= "Invalid request" (body :message))))

          (it "returns a 500 with an error message"
              (let [message   "Keyboard not found. Press any key to continue."
                    exception (Exception. message)
                    error (error :req exception)
                    body (read-string (error :body))]
                (should= 500 (error :status))
                (should= 500 (body :status))
                (should= "There was an error" (body :message))
                (should= message (body :error)))))

(defn meta-for-stub
  ([id] {:doc "project"})
  ([lib ns version var] {:doc "project"}))

(describe "Documentation"
          (it "returns a project's documentation information when found"
              (with-redefs [es/meta-for meta-for-stub]
                (let [id-docs   (doc-for :req 1)
                      id-body   (read-string (id-docs :body))
                      docs      (doc-for :req "my" "0.0.1" "great" "project")
                      docs-body (read-string (docs :body))]
                  (should= 200 (id-docs :status))
                  (should= 200 (docs :status))
                  (should= {:doc "project"} id-body)
                  (should= {:doc "project"} docs-body)))))
