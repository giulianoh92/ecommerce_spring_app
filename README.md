# 🛍 eCommerce Spring Boot API  

## 📌 Description  

The **eCommerce Spring Boot API** is a backend service for a comprehensive **web eCommerce system**. It currently supports a **Swing desktop application** for system management and is connected to a **PostgreSQL database**. The API provides endpoints for **product management, user authentication (via JWT), cart and order management**, and more.  

🚀 **Future releases** will include a **frontend web client** to interact with the backend API.  

---

## 🛠 Technologies  

🔹 **Java** (Spring Boot)  
🔹 **PostgreSQL** (Relational Database)  
🔹 **JWT** (JSON Web Tokens for authentication)  
🔹 **Hibernate ORM** (Object-Relational Mapping)  
🔹 **Jakarta Validation** (Input validation)  
🔹 **Swing** (for the desktop application)  
🔹 **Maven** (Dependency management)  
🔹 **Docker & Docker Compose** (For containerization)  

---

## 🏢 Architecture  

The project follows the **Model-View-Controller (MVC)** architecture, ensuring a clean separation of concerns.  

✔️ **MVC Conventions**: Separates **data, UI, and logic** for better maintainability.  
✔️ **Jakarta Validation**: Ensures incoming data meets business rules.  
✔️ **Hibernate ORM**: Simplifies database interactions.  
✔️ **Integration with Swing**: The backend API currently works with a **Swing desktop app**, which will be **refactored into a separate application**.  
✔️ **Future Plans**: A **frontend web client** will be developed to interact with the API via HTTP endpoints.  

---

## 🐋 Running with Docker  

To simplify deployment, we provide a **setup script (`setup.sh`)** that automates the installation of **Maven, Docker, and Docker Compose**, then builds and runs the application in containers.  

### 🚀 Quick Start  

1️⃣ Clone the repository:  
```sh
git clone https://github.com/giulianoh92/ecommerce_spring_app.git
cd ecommerce-spring-app
```  

2️⃣ Make the `setup.sh` script executable:  
```sh
chmod +x setup.sh
```  

3️⃣ Run the script:  
```sh
./setup.sh
```  

This script will:  
👉 **Check and install** Maven, Docker, and Docker Compose if they are missing.  
👉 **Build the Spring Boot application** using Maven.  
👉 **Create and start** the Docker containers using `docker-compose up --build`.  

Once completed, the API will be running inside Docker containers. 🎉  

---

## 🛠 Manual Installation  

### 📌 Prerequisites  

- **Java 17** ☕  
- **Maven** 🛠  
- **PostgreSQL** 🐘

### 🔧 Setup  

1️⃣ **Clone the repository**  
```sh
git clone https://github.com/giulianoh92/ecommerce_spring_app.git
cd ecommerce-spring-app
```  

2️⃣ **Set up the PostgreSQL database**  
```sh
psql -U postgres
CREATE DATABASE ecommerce;
\q
```  

3️⃣ **Configure database connection**  
Edit the `src/main/resources/application.properties` file:  
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=your_username
spring.datasource.password=your_password
```  

4️⃣ **Build the project**  
```sh
mvn clean install
```  

5️⃣ **Run the application**  
```sh
mvn spring-boot:run
```  

---

## 🚀 Usage  

🔹 Once the application is running, access the **Swagger UI** for API documentation:  
📌 `http://localhost:8080/swagger-ui.html`  

---

## 🗓 Future Releases  

✔️ **Separate Swing Desktop App** 🎨  
✔️ **Develop a Web Frontend Client** 🌐  

---

## 🤝 Contributing  

We welcome contributions! Follow these steps:  

1️⃣ **Fork** the repository.  
2️⃣ **Create a new branch** (`git checkout -b feature-branch`).  
3️⃣ **Make your changes** 🛠  
4️⃣ **Commit your changes** (`git commit -m "Add new feature"`).  
5️⃣ **Push to the branch** (`git push origin feature-branch`).  
6️⃣ **Create a Pull Request**.  

---

## 📝 License  

This project is licensed under the **MIT License**. 📝  

---