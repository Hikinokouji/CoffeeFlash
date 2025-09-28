# CoffeeFlash ☕

CoffeeFlash is the **server-side backend** of a business management application, originally designed with a coffee shop in mind.  
It provides tools for handling daily operations, customer orders, inventory, staff management, and analytics.

---

## 🚀 Features
- User account and company management (sign up / log in / log out)
- Product creation and management in the database
- POS terminal – a module responsible for processing sales of products stored in the company’s database

---

## 🛠️ Tech Stack
- **Backend Framework**: Spring Boot (Java)
- **Database**: PostgreSQL (multi-database architecture)
- **API**: RESTful endpoints (JSON)
- **Containerization**: Docker & Docker Compose
- **Database Migrations**: Liquibase
- **Security**: Spring Security (JWT)

---

## 📦 Installation & Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/Hikinokouji/CoffeeFlash.git
   cd coffeeflash


2. Run with Docker Compose (development environment):
   
   ```bash
   docker compose -f docker-compose.dev.yml up -d

- **⚠️ Warning**
Make sure to use the docker-compose.dev.yml file.
This file is specifically designed for local development.

- **Information Description**
The development Compose file runs on your local machine and provides two containers:

   PostgreSQL (database)

   MinIO (object storage)

   These services are connected via a Docker network for seamless communication.