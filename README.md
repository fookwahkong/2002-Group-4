# BTO Management System (SC2002 Assignment)

Welcome to our OO-based **Built-To-Order (BTO) Management System** — a console-based application designed to streamline interactions between HDB applicants and staff. Built as part of our SC2002 (Object-Oriented Design & Programming) assignment, this project models a centralised platform for all BTO operations.

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

## 🧠 Design Approach

We started with a deep dive into the assignment brief, identifying key actors, their roles, and use-case actions. Our process involved:
- **Iterative requirement analysis** – multiple readings of the brief, highlighting roles/actions
- **Keyword & functionality clustering** – grouped actions to clarify scope
- **Use of MVC structure** – with Controller classes handling core logic (`ProjectController`, `EnquiryController`, etc.)
- **Clean UI layering** – separate UI classes for Applicant, Officer, and Manager
- **Password logic encapsulation** – managed via a shared `ChangePasswordUI` used across all actors

---

## 🧩 UML & System Design

We modeled our system with clear **aggregation** where reusable components (like `ChangePasswordUI`) are shared across interfaces. Our class design promotes modularity, maintainability, and scalability.

Key Concepts:
- Object-Oriented Programming
- MVC-inspired layering
- Aggregation vs Inheritance
- Encapsulation of sensitive data (passwords)

---

## 📁 Project Structure

```bash
src/
│
├── ui/
│   ├── ApplicantUI.java
│   ├── OfficerUI.java
│   ├── ManagerUI.java
│   └── ChangePasswordUI.java
│
├── controller/
│   ├── ProjectController.java
│   ├── EnquiryController.java
│   └── RegistrationController.java
│
├── model/
│   ├── Applicant.java
│   ├── HDBOfficer.java
│   ├── HDBManager.java
│   ├── Project.java
│   └── Flat.java
│
└── Main.java
```

---

How to Run:

```bash
#clone this repository
javac Main.java
java Main
```

---

## Team & Contribution

This project was built collaboratively as part of NTU's SC2002 course. Special focus was given to OO modeling, clarity of user roles, and thoughtful requirement interpretation.

---

Feel free to fork, explore or take inspiration from our structure ✨
