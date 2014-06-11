(ns real-dashboard.core
  (:use [compojure.core :only (defroutes GET)]
  ring.util.response
  ring.middleware.cors
  org.httpkit.server)
  (:require [compojure.route :as route] 
            [compojure.handler :as handler]
            [ring.middleware.reload :as reload]
            [cheshire.core :refer :all]
            [real-dashboard.tweets :refer :all]))

(def clients (atom {}))

(defn ws [req]
  (with-channel req con 
    (swap! clients assoc con true)
    (println con " connected")
    (on-close con (fn [status]
                    (swap! clients dissoc con)
                    (println con " disconnected. status: " status)))))

;; TODO:  weird reference to msg_handler "global" here ...
;; must be a better way to somehow PASS in something to the twitter side of things

(future (loop []
          (doseq [client @clients]
            (println "sending message to client...")
            ;(send msg_handler send_message "happiness")
            (send! (key client) (generate-string
                                  {:happiness (:count @msg_handler)})
                   false))
          (Thread/sleep 5000)
          (recur)))

(defroutes routes 
  (GET "/happiness" [] ws))

(def application (-> (handler/site routes)
                   reload/wrap-reload
                   (wrap-cors
                     :access-control-allow-origin #".+")))

(defn -main [& args]
  (let [port (Integer/parseInt
               (or (System/getenv "PORT") "8080"))]
    (run-server application {:port port :join? false})))

;(statuses-filter :params {:track "miamiheat, miami heat"}
;                 :oauth-creds my-creds
;                 :callbacks *custom-streaming-callback*)

; TODO:  need to have a custom callback
; TIMER - core/async?
; CHANNEL - for tweets?
; how to connect the twitter stream callback handler (message complete) to the websocket??





