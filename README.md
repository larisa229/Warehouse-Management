# Warehouse Management System

This project is an order and inventory management system implemented in Java, designed to manage clients, products, orders, and billing. It demonstrates concepts of database CRUD operations, business logic validation, and real-time interaction through a GUI built with JavaFX.

## Features
- Manage clients: add, update, delete, and view client information.
- Manage products: add, update, delete, and view products with price and stock.
- Place orders with automatic stock validation and deduction.
- Generate bills automatically for each order with client and product details.
- View orders along with associated clients and products in a dynamic TableView.
- Transaction handling to ensure conssistency during order placement.
- Real-time GUI built with JavaFX for easy management of clients, products, and orders.

## Technologies Used
- **Language:** Java
- **Frameworks & Tools:** JavaFX, PostgreSQL, IntelliJ IDEA, DataGrip
- **Concepts Used:** Object-Oriented Programming, DAO Pattern, Reflection, Business Logic Layer, JDBC, Transaction Management, GUI Development

## How It Works
Users interact with the GUI to manage clients and products. When placing an order, the system checks the stock of the product, decrements it, saves the order, and generates the corresponding bill. The TableView dynamically displays current data, and all operations are handled through the layered architecture.

## Future Improvements
- Enhance the GUI with charts and reports for sales and stock trends.
- Add user authentication and role-based access.
- Implement search and filter functionalities for clients, products, and orders.
