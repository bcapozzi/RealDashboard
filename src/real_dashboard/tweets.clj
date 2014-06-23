(ns real-dashboard.tweets
  (:use
    [twitter.oauth]
    [twitter.callbacks]
    [twitter.callbacks.handlers]
    [twitter.api.streaming])
  (:require
    [clojure.data.json :as json]
    [http.async.client :as ac])
  (:import
    (twitter.callbacks.protocols AsyncStreamingCallback))
  )

(def my-creds make-oauth-creds *app-consumer-key*
  *app-consumer-secret*
  *user-access-token*
  *user-access-token-secret*)

(defstruct msgbuff :msg :count)

; single instance of msg handler for now
(def msg_handler (agent (struct msgbuff "" 0)))

(defn is_complete? [msg]
  "Check for the trailing indicator for a tweet, \r\n"
  (def filtered_msg (clojure.string/trim-newline msg))
  (cond
    (clojure.string/blank? msg) false
    (> (count msg) (count filtered_msg)) true
    :else false))

(defn process_complete_tweet [previous_buffer tweet_body_json]
  (let [n (inc (:count previous_buffer))]
    (println (:text (json/read-json tweet_body_json)))
    {:msg "" :count n}))

; append message, increment count if complete
(defn add_message_part[previous content] 
  (let [updated (str (:msg previous) content)]
    (cond
      (is_complete? updated) (process_complete_tweet previous updated)
      :else {:msg updated :count (:count previous)})))


(defn bodypart_func2 [response baos]
  ;(println "response: ", (str response), "baos:", (str baos))
  (cond 
    (clojure.string/blank? (str baos)) "EMPTY..."
    :else (send msg_handler add_message_part (str baos))    
  ))

(def ^:dynamic 
*custom-streaming-callback* 
  (AsyncStreamingCallback. bodypart_func2 
                           (comp println response-return-everything)
                           exception-print))

(statuses-filter :params {:track "miamiheat, miami heat"}
                 :oauth-creds my-creds
                 :callbacks *custom-streaming-callback*)

;; some test data
;(def complete_tweet "{\"text\":\"This is a complete tweet.\"}\r\n")
;(def part1 "{\"text\":\"This is")
;(def part2 " NOW a complete tweet.\"}\r\n")

; need an accumulator of tweets since last reset
;(defn incr [c]
;  (inc c))

;(defn reset [c]
;  0
;  )

