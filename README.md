# 💰 Smart Expense Tracker  
*A Full-Stack Web Application for Smarter Personal Finance Management*  
---

## 📖 Overview  
**Smart Expense Tracker** is a modern, secure, and feature-rich web application designed to help users manage their personal finances with ease.  
It provides a complete solution for **tracking income and expenses**, offering **insightful summaries** and **AI-powered insights** to give users a clear picture of their financial health.  

This project demonstrates a complete **Java + Spring Boot backend** with a dynamic **Thymeleaf frontend** — making it a great example of a **full-stack web development project**.  

---

## ✨ Features  

### 🔐 Secure User Authentication  
- User Registration & Login with Spring Security  
- Password hashing with **BCrypt** (no plain-text storage)  
- Session management ensuring data isolation  

### 💸 Income & Expense Tracking  
- Separate forms for adding **income** and **expenses**  
- Categorization (e.g., Salary, Food, Rent, Transport)  
- Optional description field for detailed notes  

### 📊 Dynamic Dashboard  
- Real-time financial overview: **Total Income, Expenses, Balance**  
- Personalized welcome message for logged-in user  

### 📜 Interactive Transaction History  
- Clean UI with **show/hide toggles**  
- **Timeframe filters**: All, Today, This Week, Last Month  

### 📄 Data Export  
- Export transactions to **Excel (.xlsx)** using Apache POI  

### 🤖 AI-Powered Summary  
- One-click “✦ AI Summary ✦” button  
- Sends financial data to **Google Gemini API**  
- Returns a personalized, easy-to-read spending analysis  

---

## 🛠️ Tech Stack  

**Backend**  
- Java 17  
- Spring Boot 3 (Web, Data JPA, Security)  
- Hibernate ORM  
- Maven (build & dependency management)  
- Apache POI (Excel export)  

**Frontend**  
- Thymeleaf (server-side templates)  
- HTML5, CSS3, Bootstrap 5 (responsive UI)  
- JavaScript (filters, toggles, API calls)  

**Database**  
- MySQL (local development)  
- PostgreSQL (deployment-ready)  

**External Services**  
- Google Gemini API (AI-powered summaries)  

