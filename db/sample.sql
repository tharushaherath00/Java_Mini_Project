CREATE DATABASE lms;
USE lms;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'STUDENT', 'TEACHER') NOT NULL
);

INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('student1', 'student123', 'STUDENT');
