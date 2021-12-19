Julian Davies
101020508

This is my final project for COMP3005. This project was competed entirely by me.

Instruction to run code:
1. Download project_bookstore
2. Open the file project_bookstore.java in project_bookstore/src
3. Run the main function in the file

Owner Capabilities:
1. Add a new book to the database. If the author and/or publisher is not already created in the database, it will first ask for that information before creating the book
2. Delete book from the database based on the ISBN of the book.
3. View reports on sales with options being total_sales, sales_by_genre, and sales_by_author

Customer Capabilities:
1. Register if they current are not in the database
2. Search books via ISBN, Author, Title, or Genre. After viewing a specific book, all the relevant information is displayed and the user can add it to their cart.
3. View any orders done by this customer. Customers can only view their own orders. The customer will recieve where the current order is (Ex. 'Warehouse').
4. Checkout any items in the cart. The customer will input billing and shipping information and it will place the order for them. 


SQL Functions:
Files can be found in the SQL directory and have 4 files.

DDL.sql:
Statements to create the tables

Queries.sql:
Example statements used in the application

Triggers.sql:
Created trigger for the database

Materialized Views.sql
3 created views for the owner reports