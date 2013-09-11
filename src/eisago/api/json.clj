(ns eisago.api.json
  (require [eisago.api.interface :as api]
           [cheshire.core :as json]))

(defn as-json [func]
  (partial func json/encode ))

(def missing (as-json api/missing))
(def error (as-json api/error))
(def search (as-json api/search))
(def children-for (as-json api/children-for))
(def doc-for (as-json api/doc-for))
(def stats (as-json api/stats))
(def all-projects (as-json api/all-projects))
