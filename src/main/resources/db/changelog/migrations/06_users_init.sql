CREATE TABLE users (
    user_id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    first_name varchar,
    last_name varchar,
    username varchar NOT NULL UNIQUE,
    email varchar NOT NULL UNIQUE,
    password varchar,
    user_role varchar,
    is_enabled BOOLEAN
)