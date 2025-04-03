# Learning Management System (LMS)

## Overview

This is a **Learning Management System (LMS)** developed using **Swing** and **MySQL**. The system is designed to be a fully functional LMS for managing courses, users, and other educational resources. It provides an intuitive GUI and connects to a MySQL database to manage the backend data.

## Features

- Connects to a MySQL database using JDBC.
- Displays the list of tables from the database and provides the foundation for managing educational data.
- User management (students, teachers, admins).
- Course management (create, edit, delete courses).
- Enrollment and attendance tracking.
- Simple, clean, and extendable GUI built using Swing.
- Modular and extendable architecture for easy feature addition.

## Prerequisites

Before running the project, make sure you have the following installed:

- **Java 22+** (JDK 22 or higher)
- **MySQL Server** (or any compatible database)
- **MySQL Workbench** (optional, for managing your database visually)
- **Maven** (optional, for managing dependencies)

## Setup and Installation

1. **Clone the Repository:**
   Clone the repository to your local machine:
   ```bash
   git clone https://github.com/your-repo/lms.git
   cd lms
   ```

2. **Import Database:**
   In the `db` folder, you will find a script file named `import.bat`. This script will import the database schema into your MySQL database. Follow these steps:

    - Ensure MySQL is installed and running on your machine.
    - Open the `import.bat` file in a text editor and update the `MYSQL_PATH` variable if needed.

   **For example:**
   ```batch
   set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin"
   ```
    - If you're using a different version of MySQL, update the `MYSQL_PATH` to point to the correct MySQL `bin` folder.

    - After setting the correct path, run the `import.bat` file:
      ```bash
      db\import.bat
      ```

## Contributing

We welcome contributions to the project! If you'd like to contribute, please follow these guidelines:

1. **Fork the Repository:**
    - Click the **Fork** button on the repository page to create a copy of the repository under your GitHub account.

2. **Clone Your Fork:**
    - Clone your fork to your local machine:
   ```bash
   git clone https://github.com/your-username/lms.git
   cd lms
   ```

3. **Create a New Branch:**
    - Create a new branch to work on your specific feature or bug fix. **Do not work directly on the `main` branch**.
   ```bash
   git switch -c new-branch
   ```

4. **Make Changes:**
    - Implement your feature or bug fix in the project.

5. **Commit Changes:**
    - Stage and commit your changes:
   ```bash
   git add .
   git commit -m "Description of changes"
   ```

6. **Push Changes:**
    - Push your changes to your fork:
   ```bash
   git push origin new-branch
   ```

7. **Create a Pull Request:**
    - Open a pull request from your branch to the `develop` branch of the main repository. **Do not create pull requests for the `main` branch**.

8. **Follow Code Review Process:**
    - Once the pull request is submitted, it will be reviewed. Any necessary changes will be requested before it can be merged.

By following this process, you ensure that the codebase remains clean and maintainable.

## Database Hosting Guidelines

- **Database Configuration**: You need to set up your MySQL database connection in the application. You can either use a local MySQL server or host your database remotely.

- **Importing the Database**:
    1. Run the `import.bat` script from the `db` folder to create the necessary tables in your database.
    2. Update the database connection details in your project (e.g., `DatabaseConnector.java`) to point to your database host.

## License

This project is open-source and available under the [MIT License](LICENSE).
