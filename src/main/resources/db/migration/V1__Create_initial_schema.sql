CREATE TABLE users
(
    id                UUID PRIMARY KEY,
    email             VARCHAR(255)             NOT NULL UNIQUE,
    password_hash     VARCHAR(255),
    creation_date     TIMESTAMP WITH TIME ZONE NOT NULL,
    modification_date TIMESTAMP WITH TIME ZONE NOT NULL,
    version           BIGINT                   NOT NULL DEFAULT 0
);

CREATE TABLE user_roles
(
    user_id UUID        NOT NULL,
    role    VARCHAR(50) NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user_roles_to_users
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

CREATE TABLE customers
(
    user_id           UUID PRIMARY KEY,
    display_name      VARCHAR(255)             NOT NULL,
    phone_number      VARCHAR(50),
    creation_date     TIMESTAMP WITH TIME ZONE NOT NULL,
    modification_date TIMESTAMP WITH TIME ZONE NOT NULL,
    version           BIGINT                   NOT NULL DEFAULT 0,
    CONSTRAINT fk_customer_to_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE addresses
(
    id            UUID PRIMARY KEY,
    customer_id   UUID         NOT NULL,
    street        VARCHAR(255) NOT NULL,
    street_number VARCHAR(20)  NOT NULL,
    flat_number   VARCHAR(20),
    city          VARCHAR(100) NOT NULL,
    postal_code   VARCHAR(20)  NOT NULL,
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers (user_id)
            ON DELETE CASCADE

);

CREATE TABLE service_requests
(
    id                   UUID PRIMARY KEY,
    customer_id          UUID                     NOT NULL,
    address_id           UUID                     NOT NULL,
    title                VARCHAR(255)             NOT NULL,
    description_text     TEXT,
    status               VARCHAR(50)              NOT NULL,
    offer_estimated_cost DECIMAL(10, 2),
    chosen_slot_start    TIMESTAMP WITH TIME ZONE,
    chosen_slot_end      TIMESTAMP WITH TIME ZONE,
    final_revenue        DECIMAL(10, 2),
    costs_of_parts       DECIMAL(10, 2),
    internal_note        TEXT,
    creation_date        TIMESTAMP WITH TIME ZONE NOT NULL,
    modification_date    TIMESTAMP WITH TIME ZONE NOT NULL,
    version              BIGINT                   NOT NULL DEFAULT 0,
    CONSTRAINT fk_sr_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers (user_id),
    CONSTRAINT fk_sr_address
        FOREIGN KEY (address_id)
            REFERENCES addresses (id)
);

CREATE TABLE offer_available_time_slots
(
    service_request_id UUID                     NOT NULL,
    start_date_time    TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date_time      TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_oats_service_request
        FOREIGN KEY (service_request_id)
            REFERENCES service_requests (id)
            ON DELETE CASCADE
);

CREATE TABLE attachments
(
    id                 UUID PRIMARY KEY,
    status             VARCHAR(50)              NOT NULL,
    original_filename  VARCHAR(255)             NOT NULL,
    content_type       VARCHAR(100)             NOT NULL,
    file_size          BIGINT                   NOT NULL,
    uploader_id        UUID                     NOT NULL,
    creation_date      TIMESTAMP WITH TIME ZONE NOT NULL,
    service_request_id UUID,

    CONSTRAINT fk_attachment_to_service_request
        FOREIGN KEY (service_request_id)
            REFERENCES service_requests (id)
);