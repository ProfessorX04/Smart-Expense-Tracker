💰 Smart Expense Tracker - Full-Stack Web Application
A modern, secure, and feature-rich web application designed to help users manage their personal finances with ease. This project provides a complete solution for tracking income and expenses, offering insightful summaries and intelligent features to give users a clear overview of their financial health.

Built with a robust Java and Spring Boot backend and an interactive Thymeleaf frontend, this application serves as a comprehensive example of a full-stack web development project.

✨ Core Features
This application is packed with features that make personal finance management intuitive and powerful:

🔐 Secure User Authentication:

User Registration & Login: Users can create secure, individual accounts.

Password Hashing: Passwords are never stored as plain text. We use the industry-standard BCrypt hashing algorithm to ensure user credentials are safe.

Session Management: The application securely manages user sessions, ensuring that each user can only access their own financial data.

💸 Income and Expense Tracking:

Separate Forms: Dedicated and intuitive forms for adding both income and expenses.

Categorization: Users can categorize each transaction (e.g., Salary, Food, Transport, Rent) for better organization and filtering.

Descriptions: An optional description field allows for detailed notes on each transaction.

📊 Dynamic Dashboard:

At-a-Glance Summaries: The main dashboard displays three key financial metrics in real-time: Total Income, Total Expenses, and the current Balance.

Personalized Welcome: Greets the logged-in user by their name for a personalized experience.

📜 Interactive Transaction History:

Show/Hide Toggles: Both the income and expense history tables are hidden by default to maintain a clean UI and can be revealed with a single click.

Advanced Filtering: Users can filter their transaction history by various timeframes: All, Today, This Week, and Last Month.

📄 Data Export:

Export to Excel: Users can download a complete report of their expenses in a native Excel (.xlsx) format for offline analysis or record-keeping.

🤖 AI-Powered Summary:

Intelligent Insights: With a single click on the "✦ AI Summary ✦" button, the application sends the user's financial data to the Google Gemini API.

Personalized Paragraph: The AI generates a concise, encouraging, and easy-to-read paragraph summarizing the user's income sources and spending habits, addressed directly to them.

🛠️ Tech Stack
This project utilizes a modern and robust set of technologies:

Backend:

Java 17

Spring Boot 3: For rapid, secure, and powerful web application development.

Spring Security: For handling all authentication and authorization.

Spring Data JPA (Hibernate): For elegant and efficient database communication.

Maven: For project build and dependency management.

Apache POI: For generating Excel files.

Frontend:

Thymeleaf: A server-side Java template engine for creating dynamic HTML.

HTML5 & CSS3: For structuring and styling the user interface.

JavaScript: For all interactive features, including filtering, show/hide, and API calls.

Bootstrap 5: For a responsive and clean grid layout system.

Database:

MySQL (for local development)

PostgreSQL (for deployment)

External Services:

Google Gemini API: For the AI-powered summary feature.
