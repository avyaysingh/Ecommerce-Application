# Ecommerce-Application

## Table of Contents
1. [Introduction](#introduction)
2. [Problem Statement](#problem-statement)
3. [Features](#features)
4. [Technologies Used](#technologies-used)
5. [Setup Instructions](#setup-instructions)
6. [Project Structure](#project-structure)
7. [Services Breakdown](#services-breakdown)
8. [Future Enhancements](#future-enhancements)

## Introduction
This project is a capstone e-commerce application built with Spring Boot (version 3.2.*) following a microservices architecture. The system is divided into three core services: **User Service**, **E-commerce Service**, and **discovery-server Service**. Communication between microservices is handled using **FeignClient**.

## Problem Statement
The primary objective of this project was to design and develop an e-commerce platform utilizing a microservices architecture. The key services implemented include a **User Service** for authentication and role-based access, an **E-commerce Service** for product and order management, and a **Eureka Service** for service discovery.

## Features
### User Service
- **Registration and Authentication**: Users can register, log in, and manage their profiles.
- **Role-Based Access Control**: Users have different roles and permissions based on their privileges (e.g., Admin, Customer).
- **Profile Management**: Users can update personal information.
  
### E-commerce Service
- **Product Management**: Handle product catalogs and categories.
- **Cart Functionality**: Users can add/remove items to/from their cart.
- **Order Processing**: Facilitates order placement and fulfillment i.e checkout, cancel.

### Eureka Service
- **Service Discovery**: Automates microservices registration and discovery, ensuring seamless interaction between different services.


## Technologies Used
- **Java**: 17
- **Spring Boot**: 3.2.*
- **Spring Security**: Role-based access control and user authentication
- **Spring Data JPA**: ORM for data persistence
- **MySQL**: Relational database management
- **Eureka Server**: Service registry for microservices
- **Spring Devtools**: Development-time support
- **Spring Validation**: Input validation
- **FeignClient**: For inter-service communication

## Setup Instructions
### Prerequisites
- JDK 17+
- Maven
- MySQL Database

### Steps to Set Up
1. Clone the repository: `git clone https://github.com/avyaysingh/Ecommerce-Application.git`
2. Set up your MySQL database and configure the `application.yml` files for each service.
3. Build the project using Maven: `mvn clean install`.
4. Run the services in the following order:
   - Eureka Service: `java -jar discovery-server.jar`
   - User Service: `java -jar user-service.jar`
   - E-commerce Service: `java -jar e-commerce.jar`


## Project Structure
```plaintext
├── eureka-service
│   └── src
├── user-service
│   └── src
├── ecommerce-service
    └── src
```

## Services Breakdown
### 1. User Service
Handles user registration, authentication, and role-based access control.

### 2. E-commerce Service
Manages product catalog, cart functionality, and order processing.

### 3. Eureka Service
Provides microservices registration and discovery.


## Future Enhancements
- Add real-time order tracking.
- Enhance product search capabilities.
- Implement payment gateway integration.
