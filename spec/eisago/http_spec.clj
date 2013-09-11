(ns eisago.http-spec
  (:require [speclj.core :refer :all]
            [eisago.http :refer :all]
            [eisago.api.json :as api]))

(defn match-one? [regexes strings]
  (not (every? false?
        (for [string strings
              re     regexes]
          (re-matches re string)))))

(describe "API routes"
          
          (it "matches valid documentation routes to the doc handler"
              (let [valid-routes ["/v1/doc/4398e3902623b427a2727acd389c52f3"
                                  "/v1/doc/clojure/clojure.core/map"
                                  "/v1/doc/clojure/1.5.0/clojure.core/map"]]
              (should (match-one? doc-routes valid-routes))))

          (it "matches valid search routes to the search handler"
              (let [valid-routes ["/v1/clojure/clojure.core/_search/?q=map"
                                  "/v1/clojure/_search/?q=map"
                                  "/v1/_search/?q=map"]]
                (should (match-one? search-routes valid-routes))))

          (it "matches the all projects route to the all-projects handler"
              (should (match-one? [all-projects-route]
                                  ["/v1/_projects/"])))

          (it "matches the stats route to the stats handler"
              (should (match-one? [stats-route]
                                  ["/v1/stats/"]))))






