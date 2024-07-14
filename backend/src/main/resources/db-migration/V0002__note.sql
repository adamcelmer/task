CREATE TABLE note
(
    id              VARCHAR(36) PRIMARY KEY,
    title           VARCHAR(128) NOT NULL UNIQUE,
    latest_version  BIGINT       NOT NULL,
    current_version VARCHAR(36)  NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    modified_at     TIMESTAMP    NOT NULL
);

CREATE TABLE note_version
(
    id          VARCHAR(36) PRIMARY KEY,
    note_id     VARCHAR(36)   NOT NULL
        CONSTRAINT fk_note_id REFERENCES note (id),
    version_no  BIGINT        NOT NULL,
    content     VARCHAR(5000) NOT NULL,
    created_at  TIMESTAMP     NOT NULL,
    modified_at TIMESTAMP     NOT NULL
);