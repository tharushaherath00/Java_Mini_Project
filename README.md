# Learning Management System (LMS)

## Overview

This is a **Learning Management System (LMS)** developed using **Swing** and **MySQL**. The system is designed to be a fully functional LMS for managing courses, users, and educational resources. It provides an intuitive GUI and connects to a MySQL database to manage backend data. Lecture materials are securely stored and retrieved using a dedicated **File Server** to ensure safe and efficient file management.

## Features

- Connects to a MySQL database using JDBC for data management.
- Securely stores and retrieves lecture materials using a [File Server](https://github.com/ktauchathuranga/file-server).
- Displays a list of courses, materials, and other educational data.
- User management (students, teachers, admins).
- Course management (create, edit, delete courses).
- Enrollment and attendance tracking.
- Simple, clean, and extendable GUI built using Swing.
- Modular and extendable architecture for easy feature addition.

## Prerequisites

Before running the project, ensure you have the following installed:

- **Java 22+** (JDK 22 or higher)
- **MySQL Server** (or any compatible database)
- **MySQL Workbench** (optional, for managing your database visually)
- **Maven** (for managing dependencies)
- **File Server** (https://github.com/ktauchathuranga/file-server) for secure file storage and retrieval
- **Git** (for cloning repositories)

## Setup and Installation

1. **Clone the LMS Repository:**
   Clone the LMS repository to your local machine:
   ```bash
   git clone https://github.com/tharushaherath00/Java_Mini_Project.git
   cd Java_Mini_Project
   ```

2. **Set Up the File Server:**
   The LMS uses a File Server for securely storing and retrieving lecture materials. Follow these steps to set it up:
   - Clone the File Server repository:
     ```bash
     git clone https://github.com/ktauchathuranga/file-server.git
     cd file-server
     ```
   - Follow the setup instructions in the File Server's [README](https://github.com/ktauchathuranga/file-server/blob/main/README.md) to configure and run the server.
   - Ensure the File Server is running and accessible (e.g., via its API endpoint).
   - Note the File Server credentials (e.g., `test_backend`, `your_random_jwt_secret_32_chars_minimum`) for use in the LMS.

3. **Import Database:**
   In the `db` folder, you will find a script file named `import.bat`. This script imports the database schema into your MySQL database. Follow these steps:
   - Ensure MySQL is installed and running.
   - Open the `import.bat` file in a text editor and update the `MYSQL_PATH` variable if needed:
     ```batch
     set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin"
     ```
     Update `MYSQL_PATH` to point to your MySQL `bin` folder if using a different version.
   - Run the `import.bat` file:
     ```bash
     db\import.bat
     ```

4. **Configure Database and File Server Connections:**
   - Update the database connection details in `src/main/java/org/example/Database.java` to point to your MySQL database (e.g., URL, username, password).
   - Configure the File Server credentials in the LMS application (e.g., in `UndergraduateViewCourseDetails.java` or a configuration file). Replace hardcoded credentials (`test_backend`, `your_random_jwt_secret_32_chars_minimum`) with secure values, preferably stored in environment variables or a configuration file:
     ```java
     ClientCredentials credentials = new ClientCredentials(
         System.getenv("FILESERVE_USERNAME"),
         System.getenv("FILESERVE_SECRET")
     );
     ```

5. **Build and Run the Project:**
   - If using Maven, build the project:
     ```bash
     mvn clean install
     ```
   - Run the main application (e.g., `Main.java`):
     ```bash
     mvn exec:java -Dexec.mainClass="org.example.Main"
     ```
   - Alternatively, run the project in your IDE by setting the main class to `org.example.Main`.

## Using the File Server in the LMS

The LMS integrates with the File Server to securely manage lecture materials:
- **Uploading Materials**: Teachers/admins can upload lecture materials (e.g., PDFs, slides) to the File Server, which assigns a unique `file_id` stored in the MySQL `files` table.
- **Downloading Materials**: Students can download materials via the `UndergraduateViewCourseDetails` interface. The system authenticates with the File Server using `ClientCredentials`, retrieves a download link, and saves the file to the project root directory.
- **Security**: The File Server uses JWT-based authentication to ensure only authorized users access materials.

To download materials:
1. Log in as a student.
2. Navigate to the "View Course Details" section.
3. Select a course and material from the dropdown menus.
4. Click "Download Materials" to authenticate, retrieve, and save the file to the project root (`./`).

## Contributing

We welcome contributions to the LMS and File Server integration! To contribute, follow these guidelines:

1. **Fork the Repository:**
   - Fork the LMS repository: https://github.com/tharushaherath00/Java_Mini_Project
   - Optionally, fork the File Server repository: https://github.com/ktauchathuranga/file-server

2. **Clone Your Fork:**
   ```bash
   git clone https://github.com/YOUR_USERNAME/Java_Mini_Project.git
   cd Java_Mini_Project
   ```

3. **Create a New Branch:**
   - Create a branch for your feature or bug fix. **Do not work directly on the `main` branch**.
   ```bash
   git switch -c new-branch
   ```

4. **Make Changes:**
   - Implement your feature or bug fix in the LMS or File Server code.
   - Ensure compatibility with the File Server API for file operations.

5. **Commit Changes:**
   ```bash
   git add .
   git commit -m "Description of changes"
   ```

6. **Push Changes:**
   ```bash
   git push origin new-branch
   ```

7. **Create a Pull Request:**
   - Open a pull request from your branch to the `develop` branch of the LMS repository. **Do not target the `main` branch**.
   - If contributing to the File Server, submit a pull request to its repository as per its guidelines.

8. **Follow Code Review Process:**
   - Address any feedback during the review process to ensure your changes are merged.

## Database Hosting Guidelines

- **Database Configuration**:
   - Configure your MySQL database connection in `src/main/java/org/example/Database.java`.
   - Use a local MySQL server or a remote host (e.g., AWS RDS, Google Cloud SQL).
   - Ensure the database schema includes:
      - `Course` table: `Course_ID`, `Course_Name`.
      - `files` table: `course_id`, `filename`, `file_id` (linked to File Server).

- **Importing the Database**:
   1. Run `db/import.bat` to create the schema.
   2. Verify the `files` table contains valid `file_id` values corresponding to File Server files.

## File Server Guidelines

- **Setup**: Follow the File Server repository's [README](https://github.com/ktauchathuranga/file-server/blob/main/README.md) to deploy the server.
- **Integration**: The LMS uses the File Server's API for secure file operations. Ensure the server is running and accessible.
- **Credentials**: Securely store File Server credentials (e.g., in environment variables or a configuration file).
- **File Management**: The LMS stores `file_id` in the `files` table after uploads and uses it to request download links.

## License

This project is open-source and available under the [MIT License](LICENSE).
