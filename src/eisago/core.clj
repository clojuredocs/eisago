(ns eisago.core
  (:require [eisago.http :as http])
  (:gen-class))

(defn -main [& _]
  (println "Starting Eisago API Server...")
  (http/start-api-server))
