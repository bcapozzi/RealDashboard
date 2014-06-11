(ns real-dashboard.channels
  (require [clojure.core.async :refer :all]))

(def MOVES [:rock :paper :scissors])
(def BEATS {:rock :scissors, :paper :rock, :scissors :paper})

(defn winner
  "Based on two moves, return the name of the winner."
  [[name1 move1] [name2 move2]]
  (cond
   (= move1 move2) "no one"
   (= move2 (BEATS move1)) name1
   :else name2))

(defn random-player
  "Create a named player and return a channel to report moves."
  [name]
  (let [out (chan)]
    (go (while true (>! out [name (rand-nth MOVES)])))
    out))

(defn always-player
  "Create a named player that always returns the same move via its output channel"
  [name move]
  (let [out (chan)]
    (go (while true (>! out [name move])))
    out))

(defn judge
  "Given two channels on which players report moves, create and return an
   output channel to report the results of each match as [move1 move2 winner]."
  [player1 player2]
  (let [out (chan)]
    (go
     (while true
       (let [move1 (<! player1)
             move2 (<! player2)]
         (>! out [move1 move2 (winner move1 move2)]))))
    out))

(defn init
  "Create 2 players (by default Alice and Bob) and return an output channel of match results."
  ([] (init "Alice" "Bob"))
  ([n1 n2] (judge (random-player n1) (random-player n2)))
  ([player1 player2] (judge player1 player2)))

(defn init-game
  "Create game given 2 players return an output channel of match results."
  ([player1 player2] (judge player1 player2)))

(defn report
  "Report results of a match to the console."
  [[name1 move1] [name2 move2] winner]
  (println)
  (println name1 "throws" move1)
  (println name2 "throws" move2)
  (println winner "wins!"))

(defn play
  "Play by taking a match reporting channel and reporting the results of the latest match."
  [out-chan]
  (apply report (<!! out-chan)))

(defn play-many
  "Play n matches from out-chan and report a summary of the results."
  [out-chan n]
  (loop [remaining n
         results {}]
    (if (zero? remaining)
      results
      (let [[m1 m2 winner] (<!! out-chan)]
        (recur (dec remaining)
               (merge-with + results {winner 1}))))))



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


    
    
  
  

