HR Management Application (JavaFX + MySQL)
Overview
This is a JavaFX-based Human Resource (HR) Management System developed as part of Lab 3. It includes GUI-based pages, MySQL database integration, DAO usage with ORM practices, and JUnit testing.

Features
✅ Login Module
Email and Password input

Secure login authentication

✅ Dashboard Module
Buttons: Admin, Employee, Logout, Exit

Displays welcome message and current date

✅ Admin Module
Create, View, Update, Delete Admin Records

Navigation: Back, Logout

(⚠ Currently, data is not displayed despite successful database connection)

✅ Employee Module
Create, View, Update, Delete Employee Records

Navigation: Back, Logout

(⚠ Same issue as Admin module: data not displaying)

✅ Database Structure
Tables:

Admin

Employee Detail

Salary

MySQL database is used to store and retrieve data

✅ DAO and ORM
Data access through:

AdminDAO.java

EmployeeDAO.java

LoginDAO.java

Uses Java Models mapped to database

✅ Database Connection
JDBC (MySQL Connector/J)

Includes connection setup with URL, username, and password

✅ JUnit Testing
Test cases written to calculate yearly salaries

Covers various employee roles and salary structures

Technologies Used
JavaFX

MySQL

JDBC

Object-Oriented Programming (OOP)

JUnit (Testing)

GitHub (Version Control)

Setup Instructions
Clone the repository

Import project into your Java IDE (e.g., IntelliJ, Eclipse)

Add JDBC Driver to the classpath (mysql-connector-j-x.x.x.jar)

Configure MySQL database

Create the required tables: Admin, Employee Detail, Salary

Run the application

Login using valid credentials

Known Issues
Admin and Employee tables connect but do not display data in TableView (UI)

JUnit code is written but not fully integrated/tested yet

Author
Niraj Bhandari
Id: 23093760
