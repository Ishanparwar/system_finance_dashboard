# Finance Data Processing and Access Control Backend

> Backend system with RBAC and financial analytics for dashboard use cases.

---

## 📌 Overview
This project implements a backend system for managing financial records with role-based access control (RBAC) and dashboard analytics.

The system allows different types of users to interact with a shared financial dataset based on their roles. It supports creation and management of financial records, user management, and provides aggregated insights for a dashboard.

---

## 🏗️ Tech Stack
*   **Java**
*   **Spring Boot**
*   **Spring Data JPA**
*   **MySQL**
*   **Lombok**

---

## 🏗️ High-Level Design
The system follows a layered monolithic architecture:
*   **Controller layer**: Handles API requests.
*   **Service layer**: Contains business logic and RBAC enforcement.
*   **Repository layer**: Interacts with the database.
*   **MySQL**: Used for persistent storage.

*All financial data is shared across users, and access is controlled via roles.*

---

## 🔧 Low-Level Design

### Entities
*   **User**: `id`, `name`, `email`, `role`, `status`
*   **FinancialRecord**: `id`, `amount`, `type`, `category`, `date`, `createdBy`, `isDeleted`

### Relationships
*   **FinancialRecord → Many-to-One → User**: Linked via `createdBy` for audit purposes.

### Key Logic
*   **RBAC**: Enforced in the service layer.
*   **Soft Delete**: Implemented using the `isDeleted` flag.
*   **Filtering**: Handled using dynamic JPQL queries.
*   **Aggregations**: Implemented using native SQL queries.

---

## 👤 User Roles & Access Control


| Role    | Permissions                    |
| :------ | :----------------------------- |
| Viewer  | View records and dashboard     |
| Analyst | Create and update records      |
| Admin   | Full access (users + records)  |

**Key Rules:**
*   Only Admin can create users and change user status.
*   Viewer has read-only access.
*   Inactive users cannot perform actions.

---

## 🔌 API Endpoints

### 🔐 Authentication Note
Authentication is simulated using a `userId` query parameter.
*Example:* `POST /records?userId=1`

### User Management

| Endpoint | Method | Description | Access |
| :--- | :--- | :--- | :--- |
| `/users` | `POST` | Create user | Admin (First user bootstrap allowed) |
| `/users/{id}` | `GET` | Get user details | Admin |
| `/users/{id}/status` | `PATCH` | Activate/Deactivate user | Admin |

### Financial Records

| Endpoint | Method | Description | Access |
| :--- | :--- | :--- | :--- |
| `/records` | `POST` | Create record | Analyst, Admin |
| `/records` | `GET` | Get records (with filters) | All roles |
| `/records/{id}` | `PUT` | Update record | Analyst, Admin |
| `/records/{id}` | `DELETE`| Soft delete record | Admin |

### Dashboard

| Endpoint                      | Method | Description | Access |
|:------------------------------| :--- | :--- | :--- |
| `/dashboard/summary`          | `GET` | Get financial summary | All roles |
| `/dashboard/category-summary` | `GET` | Category-wise totals | All roles |
| `/dashboard/monthly-trends`   | `GET` | Monthly trends (supports optional month & year filtering) | All roles |
| `/dashboard/recent-activity`  | `GET` | Latest financial records | All roles |

---

## 💰 Financial Records
Each financial record represents a system-level financial transaction (not individual user data).

**Fields include:**
*   Amount
*   Type (INCOME / EXPENSE)
*   Category
*   Date
*   Notes
*   CreatedBy (for audit tracking)

---

## 🔎 Filtering Support
The system supports dynamic filtering. Multiple filters can be combined:
*   `GET /records?type=INCOME`
*   `GET /records?category=FOOD`
*   `GET /records?startDate=2026-04-01&endDate=2026-04-10`

---

## 📊 Dashboard APIs

### Summary
*   **Returns:** Total Income, Total Expense, Net Balance.

### Category Summary
*   **Returns:** Total spending grouped by category.

### Recent Activity 
*   **Returns:** The latest financial records ordered by date and insertion order to ensure deterministic results.

### Monthly Trends
*   **Monthly Trends Behavior:** The `/dashboard/monthly-trends` endpoint supports both aggregated and filtered views:

- **Without parameters**:
    - Returns month-wise aggregated income, expense, and net amount across all available data.
    - Example:
      ```
      GET /dashboard/monthly-trends
      ```

- **With `month` and `year` parameters**:
    - Returns data for a specific month.
    - Example:
      ```
      GET /dashboard/monthly-trends?month=4&year=2026
      ```

This design allows the endpoint to serve both dashboard visualization and targeted data queries.

---

## ⚠️ Validation & Error Handling
*   Global exception handling implemented.
*   Proper HTTP status codes used.
*   Input validation for: Dates, Roles, and Status values.

---

## 💾 Data Persistence
*   MySQL used for storage.
*   JPA/Hibernate for ORM.
*   Soft delete implemented using `isDeleted`.

---

## 🚀 How to Run
1.  **Clone** the repository.
2.  **Configure** MySQL in `application.properties`.
3.  **Create Database**:
    ```sql
    CREATE DATABASE finance_dashboard;
    ```
4.  **Run** the Spring Boot application.
5.  **Test** APIs using Postman.

---

## 🧪 Sample Flow
1.  Create Admin user (bootstrap).
2.  Create Analyst/Viewer via the Admin account.
3.  Add financial records.
4.  Fetch dashboard insights.

---

## 🧠 Design Decisions
*   **Service-layer RBAC**: For simplicity and clarity.
*   **Soft Delete**: To preserve data integrity and audit history.
*   **Dynamic Filtering**: Scalable query generation.
*   **DTO Separation**: For clean API contracts and security.

---

## ⚖️ Assumptions
*   Authentication is mocked using `userId`.
*   No JWT implemented to keep focus on core backend logic.
*   Financial data is shared across all users (system-level data).
*   **Analyst** users are allowed to **create** and **update** financial records, as they are typically responsible for managing and analyzing financial data. **Admin** users retain full control including **user management** and **deletion operations**.


---

## 🧪 Testing

The system has been tested using a combination of manual and automated approaches:

- **Black-box testing**: All APIs were tested via Postman to validate request/response behavior, edge cases, and access control rules.
- **Unit testing**: Core business logic such as role-based access control, validation, and edge cases are covered using unit tests.

This ensures correctness, reliability, and expected behavior under different scenarios.
---

## ✨ Future Improvements
*   JWT-based authentication.
*   Role hierarchy (Super Admin).

---

## ✅ Conclusion
This project demonstrates backend design with clean architecture, role-based access control, data handling, and aggregation logic suitable for a financial dashboard system.
