(ns eisago.api.edn
  (require [eisago.api.interface :as api]))

(defn as-edn [func]
  (partial func str))

(def missing (as-edn api/missing))
(def error (as-edn api/error))
(def search (as-edn api/search))
(def children-for (as-edn api/children-for))
(def doc-for (as-edn api/doc-for))
(def stats (as-edn api/stats))
(def all-projects (as-edn api/all-projects))
