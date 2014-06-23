(ns real-dashboard.channels
  (require [clojure.core.async :refer :all]))

(defn process_tweet [text timer_ch]
  (println text)
  timer_ch)

(defn dummy_monitor [n num_calls_to_make tweet_chan]
  (go 
	  (loop [t (timeout n) num_calls 0]
     (println num_calls)
     (if (= num_calls_to_make num_calls)
       nil
       (let [[v c] (alts! [t tweet_chan])]
         (condp = c
           t (recur (timeout n) (inc num_calls))
           tweet_chan (recur (process_tweet v t) num_calls)))))))

(defn periodic_timer_chan [n num_calls_to_make]
  (go 
	  (loop [t (timeout n) num_calls 0]
     (println num_calls)
     (if (= num_calls_to_make num_calls)
       nil
       (let [[v c] (alts! [t])]
         (condp = c
           t (recur (timeout n) (inc num_calls))))))))


    
    
  
  

