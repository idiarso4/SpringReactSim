-- Insert roles
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_TEACHER');
INSERT INTO roles (name) VALUES ('ROLE_STUDENT');
INSERT INTO roles (name) VALUES ('ROLE_BK');
INSERT INTO roles (name) VALUES ('ROLE_HOMEROOM_TEACHER');

-- Insert admin user (password: admin123)
INSERT INTO users (username, email, password, full_name, active) 
VALUES ('admin', 'admin@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Admin User', true);

-- Assign admin role to admin user
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

-- Insert test employee
INSERT INTO employees (first_name, last_name, email_address) 
VALUES ('John', 'Doe', 'john.doe@example.com');
