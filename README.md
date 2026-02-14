# 🛒 Online Shop - Backend (Spring Boot)

This is the server-side component of the Online Shop project. It handles data persistence, business logic, and security.

 Key Features

* **Robust User Management:** Secure registration and login.
* **Cascading Deletion Logic:** A "Danger Zone" feature that ensures when a user is deleted, all their orders, items, and favorites are removed to maintain DB integrity.
* **Smart Inventory Management:** * Automatically deducts stock when an order is closed.
    * **Stock Rollback:** If an order is cancelled or deleted, the items are automatically returned to the inventory.
* **Database:** Built using Spring JDBC Template with H2/MySQL support.

 Tech Stack

* **Language:** Java 17+
* **Framework:** Spring Boot
* **Data Access:** Spring JDBC Template
* **Security:** JWT / Session-based Authentication
* **Build Tool:** Maven

 Setup & Installation

1.  Clone the repository.
2.  Open the project in IntelliJ IDEA.
3.  Update `src/main/resources/application.properties` with your DB credentials.
4.  Run `ShopOnlineProjectApplication.java`.
5.  The server will be available at `http://localhost:8080`.

---