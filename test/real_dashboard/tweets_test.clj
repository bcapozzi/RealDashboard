(ns real-dashboard.tweets-test
  (:require [clojure.test :refer :all]
            [real-dashboard.tweets :refer :all]))


(def complete_tweet "{\"text\":\"This is a complete tweet.\"}\r\n")
(def part1 "{\"text\":\"This is")
(def part2 " NOW a complete tweet.\"}\r\n")

(deftest message-processing
  (testing "Complete Tweet"
    (is (= {:msg "" :count 1} (add_message_part {:msg "" :count 0} complete_tweet))))
  
  (testing "First Part of Tweet Should Not Increment Count"
    (is (= {:msg part1 :count 0} (add_message_part {:msg "" :count 0} part1))))
  
  (testing "Given First Part of Tweet, Adding Second Part Should Increment Count"
    (is (= {:msg "" :count 1} (add_message_part {:msg part1 :count 0} part2))))
  
  )

