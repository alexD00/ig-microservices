CREATE TABLE IF NOT EXISTS followers
(
	user_id INTEGER NOT NULL,
	follower_id INTEGER NOT NULL,
	started_following_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(user_id, follower_id)
);