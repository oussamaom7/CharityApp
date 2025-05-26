-- Insert admin user with encrypted password (password: 12345678)
INSERT IGNORE INTO users (username, email, password, enabled, name, created_at)
VALUES ('admin', 'admin@charityapp.com', '$2b$12$VLUAO/qdtouxnn9lAaAwge/D5L7ERnYVYIIWIb27QnfadH7A/mJLG', true, 'Admin User', CURRENT_TIMESTAMP);

-- Insert admin role for the user
INSERT IGNORE INTO user_roles (user_id, role)
SELECT id, 'ROLE_ADMIN' FROM users WHERE username = 'admin'; 