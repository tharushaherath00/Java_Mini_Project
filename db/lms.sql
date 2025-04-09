-- Create the database
CREATE DATABASE lms;
USE lms;

-- Table: Users
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL COMMENT 'Login ID: registration_no for students, staff_id for others',
    password VARCHAR(255) NOT NULL COMMENT 'Hashed password for security',
    role ENUM('admin', 'dean', 'lecturer', 'to', 'student') NOT NULL COMMENT 'User role within the system'
);

-- Table: Students
CREATE TABLE Students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    registration_no VARCHAR(20) UNIQUE NOT NULL COMMENT 'Unique student registration number, e.g., ICT2023001',
    name VARCHAR(100) NOT NULL,
    status ENUM('proper', 'repeat', 'suspended') NOT NULL COMMENT 'Enrollment status',
    email VARCHAR(100),
    phone VARCHAR(15),
    department ENUM('ICT', 'BST', 'ET') NOT NULL COMMENT 'Department: ICT, BST, or ET',
    user_id INT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Table: Lecturers
CREATE TABLE Lecturers (
    lecturer_id INT AUTO_INCREMENT PRIMARY KEY,
    staff_id VARCHAR(20) UNIQUE NOT NULL COMMENT 'Unique staff ID, e.g., LEC001',
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    department ENUM('ICT', 'BST', 'ET') NOT NULL COMMENT 'Department: ICT, BST, or ET',
    office VARCHAR(50),
    user_id INT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Table: TechnicalOfficers
CREATE TABLE TechnicalOfficers (
    to_id INT AUTO_INCREMENT PRIMARY KEY,
    staff_id VARCHAR(20) UNIQUE NOT NULL COMMENT 'Unique staff ID, e.g., TO001',
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(15),
    department ENUM('ICT', 'BST', 'ET') NOT NULL COMMENT 'Department: ICT, BST, or ET',
    user_id INT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Table: Courses
CREATE TABLE Courses (
    course_code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    semester VARCHAR(10) NOT NULL CHECK (semester = 'L01S02') COMMENT 'Semester code, fixed as L01S02 for this example',
    department ENUM('ICT', 'BST', 'ET') NOT NULL COMMENT 'Department offering the course'
);

-- Table: CourseComponents
CREATE TABLE CourseComponents (
    component_id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(10),
    type ENUM('theory', 'practical') NOT NULL,
    lecturer_id INT,
    FOREIGN KEY (course_code) REFERENCES Courses(course_code),
    FOREIGN KEY (lecturer_id) REFERENCES Lecturers(lecturer_id)
);

-- Table: Enrollments
CREATE TABLE Enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    course_code VARCHAR(10),
    semester VARCHAR(10) NOT NULL CHECK (semester = 'L01S02'),
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (course_code) REFERENCES Courses(course_code)
);

-- Table: AttendanceSessions
CREATE TABLE AttendanceSessions (
    session_id INT AUTO_INCREMENT PRIMARY KEY,
    component_id INT,
    session_date DATE NOT NULL,
    FOREIGN KEY (component_id) REFERENCES CourseComponents(component_id)
);

-- Table: Attendance
CREATE TABLE Attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    session_id INT,
    student_id INT,
    present BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (session_id) REFERENCES AttendanceSessions(session_id),
    FOREIGN KEY (student_id) REFERENCES Students(student_id)
);

-- Table: MedicalSubmissions
CREATE TABLE MedicalSubmissions (
    medical_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    submission_date DATE NOT NULL,
    approved BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (student_id) REFERENCES Students(student_id)
);

-- Table: AssessmentTypes
CREATE TABLE AssessmentTypes (
    assessment_type_id INT AUTO_INCREMENT PRIMARY KEY,
    component_id INT,
    assessment_name VARCHAR(50) NOT NULL,
    is_ca BOOLEAN NOT NULL COMMENT 'Is continuous assessment?',
    weight DECIMAL(5,2) NOT NULL CHECK (weight BETWEEN 0 AND 100),
    FOREIGN KEY (component_id) REFERENCES CourseComponents(component_id)
);

-- Table: Marks
CREATE TABLE Marks (
    mark_id INT AUTO_INCREMENT PRIMARY KEY,
    assessment_type_id INT,
    student_id INT,
    marks_obtained DECIMAL(5,2) CHECK (marks_obtained BETWEEN 0 AND 100),
    FOREIGN KEY (assessment_type_id) REFERENCES AssessmentTypes(assessment_type_id),
    FOREIGN KEY (student_id) REFERENCES Students(student_id)
);

-- Table: AssessmentMedicals
CREATE TABLE AssessmentMedicals (
    medical_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    assessment_type_id INT,
    approved BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (assessment_type_id) REFERENCES AssessmentTypes(assessment_type_id)
);

-- Table: Notices
CREATE TABLE Notices (
    notice_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    posted_date DATETIME NOT NULL,
    posted_by INT,
    target_role ENUM('all', 'student', 'lecturer', 'to', 'admin', 'dean') NOT NULL DEFAULT 'all',
    FOREIGN KEY (posted_by) REFERENCES Users(user_id)
);

-- Table: Timetable
CREATE TABLE Timetable (
    timetable_id INT AUTO_INCREMENT PRIMARY KEY,
    component_id INT,
    day_of_week ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday') NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    location VARCHAR(100) NOT NULL,
    FOREIGN KEY (component_id) REFERENCES CourseComponents(component_id)
);

-- Insert sample data into Users
INSERT INTO Users (username, password, role) VALUES
('ADMIN001', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'admin'),
('DEAN001', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'dean'),
('LEC001', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'lecturer'),
('LEC002', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'lecturer'),
('LEC003', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'lecturer'),
('LEC004', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'lecturer'),
('LEC005', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'lecturer'),
('TO001', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'to'),
('TO002', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'to'),
('TO003', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'to'),
('TO004', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'to'),
('TO005', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'to'),
('ICT2023001', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2023002', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2023003', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2023004', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2023005', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2023006', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2023007', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2023008', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2023009', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2023010', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2022011', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2022012', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2022013', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2022014', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student'),
('ICT2022015', '4813494d137e1631bba301d5acab6e7bb7aa74ce1185d456565ef51d737677b2', 'student');

-- Insert sample data into Lecturers
INSERT INTO Lecturers (staff_id, name, email, phone, department, office, user_id) VALUES
('LEC001', 'Dr. Silva', 'silva@fot.com', '0711234561', 'ICT', 'A101', (SELECT user_id FROM Users WHERE username='LEC001')),
('LEC002', 'Prof. Perera', 'perera@fot.com', '0711234562', 'ICT', 'A102', (SELECT user_id FROM Users WHERE username='LEC002')),
('LEC003', 'Ms. Fernando', 'fernando@fot.com', '0711234563', 'ICT', 'A103', (SELECT user_id FROM Users WHERE username='LEC003')),
('LEC004', 'Mr. Jayasinghe', 'jaya@fot.com', '0711234564', 'ICT', 'A104', (SELECT user_id FROM Users WHERE username='LEC004')),
('LEC005', 'Dr. Wijesinghe', 'wije@fot.com', '0711234565', 'ICT', 'A105', (SELECT user_id FROM Users WHERE username='LEC005'));

-- Insert sample data into TechnicalOfficers
INSERT INTO TechnicalOfficers (staff_id, name, email, phone, department, user_id) VALUES
('TO001', 'Nimal', 'nimal@fot.com', '0711234571', 'ICT', (SELECT user_id FROM Users WHERE username='TO001')),
('TO002', 'Kamal', 'kamal@fot.com', '0711234572', 'ICT', (SELECT user_id FROM Users WHERE username='TO002')),
('TO003', 'Sunil', 'sunil@fot.com', '0711234573', 'ICT', (SELECT user_id FROM Users WHERE username='TO003')),
('TO004', 'Ranil', 'ranil@fot.com', '0711234574', 'ICT', (SELECT user_id FROM Users WHERE username='TO004')),
('TO005', 'Saman', 'saman@fot.com', '0711234575', 'ICT', (SELECT user_id FROM Users WHERE username='TO005'));

-- Insert sample data into Students
INSERT INTO Students (registration_no, name, status, email, phone, department, user_id) VALUES
('ICT2023001', 'A. Kumara', 'proper', 'a.kumara@fot.com', '0711234581', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2023001')),
('ICT2023002', 'B. Nuwan', 'proper', 'b.nuwan@fot.com', '0711234582', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2023002')),
('ICT2023003', 'C. Dilshan', 'proper', 'c.dilshan@fot.com', '0711234583', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2023003')),
('ICT2023004', 'D. Suresh', 'proper', 'd.suresh@fot.com', '0711234584', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2023004')),
('ICT2023005', 'E. Tharindu', 'proper', 'e.tharindu@fot.com', '0711234585', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2023005')),
('ICT2023006', 'F. Kasun', 'proper', 'f.kasun@fot.com', '0711234586', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2023006')),
('ICT2023007', 'G. Malith', 'proper', 'g.malith@fot.com', '0711234587', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2023007')),
('ICT2023008', 'H. Pradeep', 'proper', 'h.pradeep@fot.com', '0711234588', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2023008')),
('ICT2023009', 'I. Chamara', 'proper', 'i.chamara@fot.com', '0711234589', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2023009')),
('ICT2023010', 'J. Lahiru', 'proper', 'j.lahiru@fot.com', '0711234590', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2023010')),
('ICT2022011', 'K. Ruwan', 'repeat', 'k.ruwan@fot.com', '0711234591', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2022011')),
('ICT2022012', 'L. Sandun', 'repeat', 'l.sandun@fot.com', '0711234592', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2022012')),
('ICT2022013', 'M. Danushka', 'repeat', 'm.danushka@fot.com', '0711234593', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2022013')),
('ICT2022014', 'N. Amila', 'repeat', 'n.amila@fot.com', '0711234594', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2022014')),
('ICT2022015', 'O. Supun', 'repeat', 'o.supun@fot.com', '0711234595', 'ICT', (SELECT user_id FROM Users WHERE username='ICT2022015'));

-- Insert sample data into Courses
INSERT INTO Courses (course_code, name, credits, semester, department) VALUES
('ICT1222', 'Database Management Systems', 3, 'L01S02', 'ICT'),
('ICT1223', 'Programming Fundamentals', 4, 'L01S02', 'ICT'),
('ICT1224', 'Computer Networks', 3, 'L01S02', 'ICT');

-- Insert sample data into CourseComponents
INSERT INTO CourseComponents (course_code, type, lecturer_id) VALUES
('ICT1222', 'theory', 1),
('ICT1222', 'practical', 2),
('ICT1223', 'theory', 3),
('ICT1223', 'practical', 4),
('ICT1224', 'theory', 5);

-- Insert sample data into Enrollments
INSERT INTO Enrollments (student_id, course_code, semester)
SELECT student_id, 'ICT1222', 'L01S02' FROM Students
UNION ALL
SELECT student_id, 'ICT1223', 'L01S02' FROM Students
UNION ALL
SELECT student_id, 'ICT1224', 'L01S02' FROM Students;

-- Insert sample data into AttendanceSessions for ICT1222 theory and practical
INSERT INTO AttendanceSessions (component_id, session_date)
SELECT 1, DATE_ADD('2023-08-01', INTERVAL (n-1) WEEK)
FROM (SELECT a.N + b.N * 10 + 1 AS n
      FROM (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) a
      CROSS JOIN (SELECT 0 AS N UNION ALL SELECT 1) b) numbers
WHERE n <= 15;

INSERT INTO AttendanceSessions (component_id, session_date)
SELECT 2, DATE_ADD('2023-08-02', INTERVAL (n-1) WEEK)
FROM (SELECT a.N + b.N * 10 + 1 AS n
      FROM (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4) a
      CROSS JOIN (SELECT 0 AS N UNION ALL SELECT 1) b) numbers
WHERE n <= 15;

-- Insert sample data into Attendance for ICT1222 theory
INSERT INTO Attendance (session_id, student_id, present)
SELECT session_id, student_id, 
       CASE WHEN RAND() < 0.85 THEN TRUE ELSE FALSE END
FROM AttendanceSessions, Students
WHERE component_id = 1;

-- Insert sample data into MedicalSubmissions
INSERT INTO MedicalSubmissions (student_id, submission_date, approved) VALUES
(1, '2023-08-08', TRUE),
(11, '2023-08-15', TRUE);

-- Insert sample data into AssessmentTypes
INSERT INTO AssessmentTypes (component_id, assessment_name, is_ca, weight) VALUES
(1, 'Quiz 1', TRUE, 10),
(1, 'Mid Theory', TRUE, 30),
(1, 'Final Theory', FALSE, 60),
(2, 'Practical Assessment', TRUE, 40),
(2, 'Final Practical', FALSE, 60);

-- Insert sample data into Marks for Quiz 1
INSERT INTO Marks (assessment_type_id, student_id, marks_obtained)
SELECT 1, student_id, ROUND(RAND() * 100, 2) FROM Students;

-- Insert sample data into AssessmentMedicals
INSERT INTO AssessmentMedicals (student_id, assessment_type_id, approved) VALUES
(1, 3, TRUE); -- Medical for Final Theory

-- Insert sample data into Notices
INSERT INTO Notices (title, content, posted_date, posted_by, target_role) VALUES
('Welcome to Semester 2', 'Welcome all students to Level 01 Semester 02.', NOW(), 1, 'student'),
('Staff Meeting', 'There will be a staff meeting on Friday.', NOW(), 1, 'lecturer');

-- Insert sample data into Timetable
INSERT INTO Timetable (component_id, day_of_week, start_time, end_time, location) VALUES
(1, 'Monday', '09:00', '11:00', 'Hall A'),
(2, 'Tuesday', '14:00', '16:00', 'Lab B'),
(3, 'Wednesday', '10:00', '12:00', 'Hall C');