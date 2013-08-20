(ns eisago.import-spec
  (:require [speclj.core :refer :all]
            [eisago.import :refer :all]
            [cheshire.core :as json]))

(describe "Reading gzipped json files"
          (it "should correctly decode the data in a gzipped file"
              (should= {:mock "json"}
                       (read-json-file "spec/eisago/mock.json.gz"))))
