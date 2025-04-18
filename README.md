# BTO Management System (SC2002 Assignment)

Welcome to our OO-based **Built-To-Order (BTO) Management System** — a console-based application designed to streamline interactions between HDB applicants and staff. Built as part of our SC2002 (Object-Oriented Design & Programming) assignment, this project models a centralised platform for all BTO operations.

---
## Team & Contribution

This project was built collaboratively as part of NTU's SC2002 course. Special focus was given to OO modeling, clarity of user roles, and thoughtful requirement interpretation.

## Team Members

| Name | Matric No. | Course |
|------|------------|---------|
| Kong Fook Wah | U2421655E | Data Science and Artificial Intelligence |
| Kris Khor Hai Xiang | U2421377C | Computer Science |
| Lin Zeshen | U2421421J | Computer Science |
| Mau Ze Ming | U2421176G | Data Science and Artificial Intelligence |
| Zheng Nan | U2422815K | Computer Science |

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
- **Use of MVC structure** – with Controller classes handling core logic (`ProjectController`, `EnquiryController`, etc.)
- **Clean UI layering** – separate UI classes for Applicant, Officer, and Manager
- **Password logic encapsulation** – managed via a shared `ChangePasswordUI` used across all actors

---

## UML & System Design

We modeled our system with clear **aggregation** where reusable components (like `ChangePasswordUI`) are shared across interfaces. Our class design promotes modularity, maintainability, and scalability.

Key Concepts:
- Object-Oriented Programming
- MVC-inspired layering
- Aggregation vs Inheritance
- Encapsulation of sensitive data (passwords)

---

## Project Structure

```bash

/2002-Group-4
│
|     ├──docs                              → Final Project Documentation (documents, diagrams, etc.)
|     ├──src/                              → Java source code
|     |   ├── main/
|     |   |    ├── java/
|     |   |    |    ├── boundary/
|     |   |    |    ├── controller/
|     |   |    |    ├── entity/
|     |   |    |    ├── enums/
|     |   |    |    ├── interfaces/
|     |   |    |    ├── utils/
|     |   |    |    └── Main.java
|     |   |    └── resources/
|     |   ├── test/
└──   └── README.md                        → This file

```

---

How to Run:

```bash
#clone this repository
cd src/main/java
javac Main.java
java Main
```

---

Feel free to fork, explore or take inspiration from our structure ✨
