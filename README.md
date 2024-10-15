# 🛍️ POS System Backend (Spring Framework)

This project is the backend for a comprehensive Point of Sale (POS) system, developed using the Spring Framework. It exposes REST APIs that manage business-critical functions, including customers, items, and orders, all powered by a clean, layered architecture.

## 📑 Table of Contents

* **Project Overview**
* **Architecture**
* **Tech Stack**
* **Getting Started**
* **API Documentation**
* **License**



## 🎯 Project Overview

The backend handles core POS functionalities, exposing RESTful APIs that communicate seamlessly with the front-end via AJAX or Fetch. Its design makes it flexible, scalable, and easy to integrate with various frontend interfaces.

**Key features include:**

* Customer Management: Create, retrieve, update, and delete customer data.
* Item Management: Manage inventory with item-related APIs.
* Order Processing: Handle order creation and management efficiently.

## 🏗️ Architecture

The application follows a structured, layered approach to ensure separation of concerns and clean code organization:

* Controller Layer: Manages HTTP requests and directs them to appropriate service methods.
* Service Layer: Contains business logic and validations.
* Data Access Layer (DAO): Communicates with the database using Spring Data JPA.
* Entity Layer: JPA entities representing database tables.
* DTO Layer: For data transfer between different layers.
* Global Exception Handling: Provides centralized error and exception handling to ensure a smooth experience.

## 💻 Tech Stack

The following technologies have been used to build this project:

* Spring Web MVC: For building RESTful services.
* Spring Data JPA: To handle database interactions via JPA and Hibernate ORM.
* Hibernate: ORM framework used for managing persistence.
* MySQL: Relational database management system.
* Lombok: Reduces boilerplate code.
* Jackson: For serializing and deserializing JSON data.
* SLF4J & Logback: For logging purposes.
* JUnit 5: Testing framework for unit and integration tests.
* ModelMapper: To streamline DTO-to-entity conversions.


## 🚀 Getting Started

### Prerequisites

Ensure that you have the following installed:

* Java 17: The project requires Java 17.
* Maven: Dependency management and project build tool.
* MySQL: Ensure your database is set up and running.
* Postman (Optional): To test and explore the APIs.
<br>
<br>
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)

### Setup Instructions

Clone the Repository: git clone https://github.com/nadeesamaraweera/Spring-POS-System.git

## 📜 API Documentation

To test or explore the API, you can use tools like Postman. Detailed documentation is available at:
<br>
[API Documentation](https://documenter.getpostman.com/view/35385577/2sAXxS8X5H)

## 📄 License

This project is licensed under the MIT License.See the [LICENSE](./LICENSE) file for details.
<hr/>
Copyright © 2024 Nadeesha Samaraweera