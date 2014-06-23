(defproject real-dashboard "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-json "0.2.0"]
                 [http-kit "2.0.0"]
                 [ring/ring-devel "1.1.8"]
                 [compojure "1.1.5"]
                 [ring-cors "0.1.0"]
                 [org.clojure/core.async "0.1.256.0-1bf8cf-alpha"]
                 [http.async.client "0.5.2"]
                 [twitter-streaming-client "0.3.1"]
                 [twitter-api "0.7.5"]]

  :jvm-opts ["-Dline.separator=\"\n\""]
  :main real-dashboard.core )
