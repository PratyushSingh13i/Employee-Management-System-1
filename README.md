# Employee Management System

A Java Swing desktop application with MySQL database integration built as a Capstone Project.

## Features
- Start page with background image
- Secure login system (admin/1234)
- View all employees in a styled dashboard
- Add new employees with full details
- Edit existing employee information
- Delete employees with confirmation
- Search employees by name
- Filter by department, salary range and status
- Employee details popup with contact and role info

## Technologies Used
- Java (Swing GUI)
- MySQL Database
- JDBC for database connection
- Eclipse IDE

## Database Setup
1. Open MySQL
2. Run the provided SQL script to create the database and tables
3. Sample data is included in the script

## How to Run
1. Clone or download this repository
2. Open Eclipse and import the project
3. Set up MySQL and run the SQL script
4. Update DBConnection.java with your MySQL password
5. Run StartPage.java

## Project Structure
- StartPage.java - Entry screen
- LoginPage.java - Login screen
- EmployeeGUI.java - Main dashboard
- AddEditEmployeePage.java - Add and edit form
- EmployeeDAO.java - Database operations
- Employee.java - Employee model
- EmployeeDetails.java - Employee details model
- DBConnection.java - Database connection
- ImageLoader.java - Image loading utility