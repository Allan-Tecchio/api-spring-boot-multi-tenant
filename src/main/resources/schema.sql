CREATE TABLE IF NOT EXISTS tenant_db (
     id SERIAL PRIMARY KEY,
     tenant_id VARCHAR(100) UNIQUE NOT NULL,
     db_url TEXT NOT NULL,
     db_user TEXT NOT NULL,
     db_pass_enc TEXT NOT NULL,
     ativo BOOLEAN NOT NULL DEFAULT TRUE
);
