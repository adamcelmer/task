CREATE TABLE user_account
(
    id          VARCHAR(36) PRIMARY KEY,
    email       VARCHAR(256) NOT NULL
        CONSTRAINT uk_user_email UNIQUE,
    password    VARCHAR(256) NOT NULL,
    salt        VARCHAR(256) NOT NULL,
    created_at  TIMESTAMP    NOT NULL,
    modified_at TIMESTAMP    NOT NULL
);