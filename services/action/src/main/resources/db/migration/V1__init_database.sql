CREATE TABLE IF NOT EXISTS followers
(
	user_id INTEGER NOT NULL,
	follower_id INTEGER NOT NULL,
	started_following_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(user_id, follower_id)
);

CREATE TABLE IF NOT EXISTS follower_requests
(
  user_id INTEGER NOT NULL,
  follower_requester_id INTEGER NOT NULL,
  follower_request_sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(user_id, follower_requester_id)
);