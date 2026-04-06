# Finance Data Processing and Access Control Backend

## 📌 Overview
This project implements a backend system for managing financial records with role-based access control (RBAC) and dashboard analytics.

The system allows different types of users to interact with a shared financial dataset based on their roles. It supports creation and management of financial records, user management, and provides aggregated insights for a dashboard.

---

## 🏗️ Tech Stack
*   Java
*   Spring Boot
*   Spring Data JPA
*   MySQL
*   Lombok

---

## 🧠 System Design
The application follows a layered monolithic architecture:
**Controller → Service → Repository → Database**

*   **Controller Layer:** Handles API requests and responses
*   **Service Layer:** Contains business logic and access control
*   **Repository Layer:** Manages database interactions
*   **Database:** Stores users and financial records

---

## 👤 User Roles & Access Control
The system implements Role-Based Access Control (RBAC):


| Role     | Permissions                     |
| :------- | :------------------------------ |
| Viewer   | View records and dashboard      |
| Analyst  | Create and update records       |
| Admin    | Full access (users + records)   |

**Key Rules:**
*   Only Admin can create users and change user status
*   Viewer has read-only access
*   Inactive users cannot perform actions

---

## 🔐 User Management
*   Create users (Admin only)
*   Assign roles (Viewer / Analyst / Admin)
*   Activate / deactivate users (soft control via status)

**Bootstrap Logic:**
*   The first user must be an Admin
*   After that, only Admins can create users

---

## 💰 Financial Records
Each financial record contains:
*   Amount
*   Type (INCOME / EXPENSE)
*   Category
*   Date
*   Notes
*   CreatedBy (audit purpose)

**Features:**
*   Create record
*   Update record
*   Soft delete (isDeleted flag)
*   Filter records by: Type, Category, and Date range

---

## 🔎 Filtering Support
The system supports dynamic filtering. Multiple filters can be combined:

*   `GET /records?type=INCOME`
*   `GET /records?category=FOOD`
*   `GET /records?startDate=2026-04-01&endDate=2026-04-10`

---

## 📊 Dashboard APIs
The system provides aggregated insights:

### Summary
`GET /dashboard/summary`
*   **Returns:** Total Income, Total Expense, Net Balance

### Category Summary
`GET /dashboard/category-summary`
*   **Returns:** Total spending per category

### Monthly Trends
`GET /dashboard/monthly-trends`
*   **Returns:** Month-wise financial trends

---

## ⚠️ Validation & Error Handling
*   Global exception handling implemented
*   Proper HTTP status codes
*   Input validation for: Dates, User roles, and Status values

---

## 💾 Data Persistence
*   MySQL used for storage
*   JPA/Hibernate for ORM
*   Soft delete implemented using `isDeleted`

---

## 🚀 How to Run
1.  Clone the repository
2.  Configure MySQL in `application.properties`
3.  Create database:
    ```sql
    CREATE DATABASE finance_dashboard;
    ```
4.  Run the Spring Boot application
5.  Test APIs using Postman

### 🧪 Sample API Flow
1.  Create Admin user
2.  Create Analyst/Viewer
3.  Add financial records
4.  Fetch dashboard insights

---

## 🧠 Design Decisions
*   Used service-layer RBAC instead of interceptors for simplicity and clarity
*   Implemented soft delete to preserve audit history
*   Used dynamic query filtering for scalability
*   Maintained DTO separation for clean API contracts

---

## ⚖️ Assumptions
*   Authentication is mocked using `userId` parameter
*   No JWT implemented to keep focus on core backend logic
*   Financial data is shared across users (system-level data)

---

## ✨ Improvements (Future Scope)
*   JWT-based authentication
*   Pagination support
*   Unit & integration tests
*   Role hierarchy (Super Admin)

---

## ✅ Conclusion
This project demonstrates backend design with clean architecture, role-based access control, data handling, and aggregation logic suitable for a financial dashboard system.