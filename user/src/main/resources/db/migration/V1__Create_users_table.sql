CREATE TABLE users
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    full_name VARCHAR(255),
    email     VARCHAR(255),
    password  VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);