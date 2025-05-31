CREATE TABLE companies (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(150) NOT NULL,
                           description TEXT,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       company_id INTEGER REFERENCES companies(id) ON DELETE SET NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE projects (
                          id SERIAL PRIMARY KEY,
                          user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          company_id INTEGER REFERENCES companies(id) ON DELETE SET NULL,
                          name VARCHAR(150) NOT NULL,
                          description TEXT,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE applications (
                              id SERIAL PRIMARY KEY,
                              user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                              project_id INTEGER REFERENCES projects(id) ON DELETE SET NULL,
                              company_id INTEGER REFERENCES companies(id) ON DELETE SET NULL,
                              status VARCHAR(50) NOT NULL,
                              applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
