CREATE TABLE users_locations
(
    user_id     BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    CONSTRAINT pk_userslocations PRIMARY KEY (user_id, location_id)
);