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

## 📦 Installation & Setup (Dev)

> The backend depends on infrastructure (PostgreSQL + MinIO).  
> **Start Docker services first**, then build and run the app with the `dev` profile.

### Prerequisites
- Docker & Docker Compose
- Java 21+
- Gradle (or the included Gradle Wrapper)

### 1) 📝 Clone the repository
    git clone https://github.com/Hikinokouji/CoffeeFlash.git
    cd coffeeflash
### 2) ⬇️ Start infrastructure (DB + Object Storage)
Runs services from ``docker-compose.dev.yml``:

```bash
    docker compose -f docker-compose.dev.yml up -d
```
Check containers:
```bash 
    docker compose -f docker-compose.dev.yml ps
```

### 3) 🔨 Build the backend:

- **Linux/maсOS** ```./gradlew clean build```
- **Windows(PowerShell)** ```./gradlew.bat clean build```
- **Windows(CMD)** ```gradlew.bat clean build```

### The executable JAR will be created in: 
``` build/libs/BackendServerCF-<version>.jar ```

#### Latest version: 0.0.2 (28.09.2025)

### 4) 🚚 Run the backend (connects to Docker service)
- **Linux/macOS** 
```bash 
    SPRING_PROFILES_ACTIVE=dev java -jar build/libs/BackendServerCF-<version>.jar
```   
Example: ``` SPRING_PROFILES_ACTIVE=dev java -jar build/libs/BackendServerCF-0.0.2.jar```

- **Windows(PowerShell)**
```bash
   $env:SPRING_PROFILES_ACTIVE="dev"; java -jar build/libs/BackendServerCF-<version>.jar
```

- **Windows(CMD)**
```bash
  set SPRING_PROFILES_ACTIVE=dev
  java -jar build/libs/BackendServerCF-<version>.jar 
```

### 5) 🚩 Verify 
- **API** ```http://localhost:8080```
- **Swagger** ```http://localhost:8080/swagger-ui/index.html``` - (limited info)
- **(Optional) Health** ```http://localhost:8080/actuator/health```

### ❄️ Project status
The project is currently frozen and unfinished, but you can use this code for personal purposes.


### 📬 Contact
For contact, you can reach me on 

Telegram ✈️: [@Hikouji](https://t.me/https://t.me/Hikouji)
![img.png](img.png)