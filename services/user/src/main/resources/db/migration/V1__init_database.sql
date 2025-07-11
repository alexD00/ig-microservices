CREATE TABLE IF NOT EXISTS users
(
	id SERIAL PRIMARY KEY,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
	username VARCHAR(255) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	num_followers INTEGER DEFAULT 0 NOT NULL,
	num_followings INTEGER DEFAULT 0 NOT NULL,
	num_of_posts INTEGER DEFAULT 0 NOT NULL,
	is_account_public BOOLEAN DEFAULT TRUE NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	role VARCHAR (10) NOT NULL DEFAULT 'User'
	CHECK (length(password) >= 8)
);
