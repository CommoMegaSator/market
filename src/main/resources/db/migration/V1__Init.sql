CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(30) NOT NULL UNIQUE,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS document (
    id SERIAL PRIMARY KEY,
    original_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id INT NULL REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT now(),
    version INT NOT NULL,
    status VARCHAR(20) CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE IF NOT EXISTS document_attributes (
    id SERIAL PRIMARY KEY,
    document_id INT REFERENCES document(id) ON DELETE CASCADE,
    attribute_key VARCHAR(255) NOT NULL,
    attribute_value TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS document_owners (
    document_id INT REFERENCES document(id) ON DELETE CASCADE,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (document_id, user_id)
);
