(defproject eisago "0.1.0-SNAPSHOT"
  :description "Next-gen clojuredocs importer, API, and website."
  :url "http://clojuredocs.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :resource-paths ["etc"]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [clj-http "0.5.7"]
                 [cheshire "4.0.4"]
                 [commons-codec "1.6"]
                 [laeggen "0.3"]]
  :main eisago.core)
