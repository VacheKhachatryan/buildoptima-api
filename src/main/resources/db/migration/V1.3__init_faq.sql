CREATE TABLE IF NOT EXISTS faq_category
(
    id            UUID primary key NOT NULL,
    name          VARCHAR          NOT NULL,
    created_by_id UUID             NOT NULL REFERENCES users (id),
    updated_by_id UUID             NOT NULL REFERENCES users (id),
    created_at    TIMESTAMP        NOT NULL,
    updated_at    TIMESTAMP        NOT NULL
);

CREATE TABLE IF NOT EXISTS faq_question
(
    id            UUID primary key NOT NULL,
    question      TEXT             NOT NULL,
    answer        TEXT             NOT NULL,
    status        VARCHAR          NOT NULL,
    category_id   UUID             NOT NULL REFERENCES faq_category (id),
    created_by_id UUID             NOT NULL REFERENCES users (id),
    updated_by_id UUID             NOT NULL REFERENCES users (id),
    created_at    TIMESTAMP        NOT NULL,
    updated_at    TIMESTAMP        NOT NULL
);