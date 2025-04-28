@echo off
echo Starting MySQL database import...

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin"
set USER=root
set PASSWORD=root
set SQL_FILE="lms-v3.sql"

if not exist %SQL_FILE% (
    echo Error: SQL file not found at %SQL_FILE%
    pause
    exit /b 1
)

set PATH=%MYSQL_PATH%;%PATH%

echo Importing %SQL_FILE%...
mysql -u %USER% -p%PASSWORD% < %SQL_FILE%

if %ERRORLEVEL% equ 0 (
    echo Database import completed successfully!
    echo You can now open MySQL Workbench to view the database 'lms'.
) else (
    echo Error: Failed to import the database. Check your credentials, file, or MySQL server.
)

pause