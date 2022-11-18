DROP
ALL OBJECTS;
CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512) UNIQUE                     NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(255)                            NOT NULL,
    description  VARCHAR                                 NOT NULL,
    is_available BOOLEAN                                 NOT NULL,
    owner_id     BIGINT                                  NOT NULL,
    request_id   BIGINT,
    CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TYPE booking_status AS ENUM ('WAITING','APPROVED','REJECTED','CANCELED');

CREATE TABLE IF NOT EXISTS bookings
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_booking  TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    end_of_booking TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    item_id        BIGINT                                  NOT NULL,
    booker_id      BIGINT                                  NOT NULL,
    status         booking_status,
    CONSTRAINT pk_bookings PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      VARCHAR                                 NOT NULL,
    item_id   BIGINT                                  NOT NULL,
    author_id BIGINT                                  NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR                                 NOT NULL,
    requestor_id BIGINT                                  NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id)
);

ALTER TABLE items
    ADD CONSTRAINT "fk_item_user_id" FOREIGN KEY (owner_id)
        REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE bookings
    ADD CONSTRAINT "fk_booking_item_id" FOREIGN KEY (item_id)
        REFERENCES items (id) ON DELETE CASCADE;

ALTER TABLE bookings
    ADD CONSTRAINT "fk_booking_user_id" FOREIGN KEY (booker_id)
        REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT "fk_comments_item_id" FOREIGN KEY (item_id)
        REFERENCES items (id) ON DELETE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT "fk_comments_author_id" FOREIGN KEY (author_id)
        REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE requests
    ADD CONSTRAINT "fk_requests_requestor_id" FOREIGN KEY (requestor_id)
        REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE items
    ADD CONSTRAINT "fk_items_request_id" FOREIGN KEY (request_id)
        REFERENCES requests (id) ON DELETE CASCADE;