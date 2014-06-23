# real-dashboard

A Clojure library designed to examine the time-density of tweets associated with a particular topic.  At the moment, it uses a clojure backend, hitting the twitter streaming api, keeps track of complete tweets that contain the target keywords received within a given time interval, and periodically sends the current tweet count to connected clients via websocket for visualization.  Simple javascript front end uses chartkick for visualizing the time-density (number per 5-second window) on a live timeline in the browser. 

## Usage

Mostly via the repl at the moment.  First kickoff the "back-end" twitter processing by invoking real-dashboard.tweets/statuses-filter.  Then start the web-server / websocket using run-server.  Then, open the file src/real_dashboard/frontend.html in a browser to see the live timeline.  Current topic is hard-coded to Miami Heat 

