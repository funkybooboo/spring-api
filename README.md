# spring-api

A lightweight e-commerce platform powered by Spring Boot, featuring product browsing, shopping-cart management, secure checkout (Stripe), and an admin console for inventory & order tracking.

---

## Features

- **Product Catalog**: Browse, search & filter products  
- **Shopping Cart**: Add/remove items, adjust quantities  
- **Checkout**: Address form, payment integration (Stripe)  
- **Admin Console**: CRUD products, view orders & sales reports  

---

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.x, Spring MVC, Spring Data JPA  
- **Database**: H2 (dev) / MySQL (prod)  
- **View**: Thymeleaf (server-rendered) or REST endpoints for SPA  
- **Security**: Spring Security (form login + roles)  
- **Payments**: Stripe Java SDK  

---

## Getting Started

### Prerequisites

- JDK 17+  
- Maven 3.6+  
- (Optional) MySQL 8.x or higher  

### Installation & Run

1. **Clone the repo**  
   ```bash
   git clone git@github.com:funkybooboo/spring-api .git
   cd spring-api
    ```

2. **Configure**
   Copy `.env.example` -> `.env`, then set your keys:

3. **Build & Start**

   ```bash
   mvn clean package
   java -jar target/spring-api-1.0.0.jar
   ```

---

## Usage

See bruno folder
