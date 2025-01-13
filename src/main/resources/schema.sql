CREATE DATABASE IF NOT EXISTS task_management_app

CREATE TABLE IF NOT EXISTS  users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username varchar(30) UNIQUE NOT null
   );

CREATE TABLE IF NOT EXISTS  tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description TEXT,
    status VARCHAR(15) DEFAULT 'PENDING',
    creator_id INT REFERENCES users(id),
    approval_count int4 DEFAULT 0 NULL,
	project_key varchar(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS  approvals (
    id SERIAL PRIMARY KEY,
    task_id INT REFERENCES tasks(id),
    approver_id INT REFERENCES users(id),
    status BOOLEAN DEFAULT FALSE,
    approved_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS  comments (
    id SERIAL PRIMARY KEY,
    task_id INT REFERENCES tasks(id),
    commenter_id INT REFERENCES users(id),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);