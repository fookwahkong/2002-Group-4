# SC2002-FDAB-Group4

# BTO Management System

Built as part of our SC2002 (Object-Oriented Design & Programming) assignment, this project is an OO-based **Built-To-Order (BTO) Management System**, designed to be a console-based centralised platform for all BTO operations.

---

## Team & Contribution

This project was built collaboratively as part of NTU's SC2002 course. Special focus was given to OO modeling, clarity of user roles, and thoughtful requirement interpretation.

## Team Members

| Name | Matric No. | Course | Roles |
|------|------------|---------|---------|
| Kong Fook Wah | U2421655E | Data Science and Artificial Intelligence | Project Manager |
| Kris Khor Hai Xiang | U2421377C | Computer Science | Documentation |
| Lin Zeshen | U2421421J | Computer Science | Lead Tester |
| Mau Ze Ming | U2421176G | Data Science and Artificial Intelligence | Documentation |
| Zheng Nan | U2422815K | Computer Science | Lead Developer |

---

## 🚀 Features

### 👤 Applicant
- View open BTO projects
- Apply for a project
- Book or withdraw a flat
- Make and manage enquiries

### 🧑‍💼 HDB Officer
- Register interest to manage projects
- Approve/reject flat bookings
- View/respond to enquiries
- Generate booking receipts

### 👩‍💼 HDB Manager
- Create, edit, delete BTO projects
- Approve/reject officer registrations
- Handle applicant withdrawals
- Generate and filter reports

### 🔐 All Users
- Secure login with NRIC + password
- Change password functionality

---

## Design Approach

We started with a deep dive into the assignment brief, identifying key actors, their roles, and use-case actions. Our process involved:
- **Iterative requirement analysis** – multiple readings of the brief, highlighting roles/actions
- **Keyword & functionality clustering** – grouped actions to clarify scope
- **Use of MVC structure** – structure the system into Models for data handling, Views for user interface, and Controllers for business logic
- **Clean UI layering** – separate UI classes for Applicant, Officer, and Manager
- **Password logic encapsulation** – managed via a shared `ChangePasswordUI` used across all actors' UI

---

## UML & System Design

We modeled our system with a strong emphasis on modularity, maintainability, and scalability to ensure clean architecture and long-term adaptability

Key Concepts:
- Object-Oriented Programming
- MVC-inspired layering
- Association vs Dependency
- Generalisation vs Realisation
- Encapsulation of sensitive data (passwords)

---

## Project Structure

```bash

/2002-Group-4
│
|     ├──docs                              → Final Project Documentation (javadocs, diagrams, etc.)
|     ├──src/                              → Java source code
|     |   ├── main/java
|     |   |    ├── bto/
|     |   |    |    ├── boundary/          → UI views (e.g. OfficerUI, LoginUI etc.)
|     |   |    |    ├── controller/        → Handles backend logic and links models with views
|     |   |    |    ├── entity/            → Core data models representing system objecets
|     |   |    |    ├── enums/             → Defines constant values across the system
|     |   |    |    ├── interfaces/        → Defines shared contract for types and structure
|     |   |    |    ├── utils/             → Contain utility functions and helper 
|     |   |    |    └── Main.java          → Program entry point
|     |   |    └── resources/              → csv files containing samples for testing
|     |   └── test/                        → unit testing
├──   └── README.md                        → This file

```
---

Feel free to fork, explore or take inspiration from our structure ✨
