	⏰ RemindMe
	Smart Reminder & Notification Platform
	
	A full-stack reminder management web application built 
	using Spring Boot, MySQL, Thymeleaf, and JavaScript, 
	featuring secure authentication, real-time notifications, 
	and an interactive dashboard.
	
	📖 Project Overview
	
	RemindMe is a full-stack web application that helps users create, organize,
	and manage reminders through a modern and intuitive interface. 
	The platform provides secure user authentication, reminder scheduling,
	real-time browser notifications, and custom in-app toast notifications to 
	ensure that important tasks are never missed.
	
	The application also includes a dashboard with reminder statistics, an 
	interactive calendar, upcoming reminder tracking, search and filtering
	capabilities, and a customizable dark/light theme for an improved user experience.

> Dashboard Screenshot

<img width="1900" height="988" alt="Screenshot 2026-07-19 132408" src="https://github.com/user-attachments/assets/dcac8ffe-f857-4efe-8993-1ce496faf32d" />


✨ Features

	User Authentication
	- Secure user registration with encrypted passwords
	- User login and logout using session-based authentication
	- Account deletion with confirmation prompt
	- Personalized dashboard for each authenticated user
	
	Reminder Management
	- Create, edit, and delete reminders
	- Mark reminders as completed
	- Set reminder title, description, date & time, and priority
	- Search reminders by title
	- Filter reminders by priority and status
	- Sort reminders by date, title, and priority
	
	Interactive Dashboard
	- Dashboard displaying total, pending, and completed reminders
	- Monthly calendar view
	- Upcoming reminders section
	- Monthly reminder summary cards
	
	Smart Notification System
	- Browser notifications for upcoming reminders
	- Custom in-app toast notifications
	- Priority-based notification badges
	- Notification sound alerts
	- 60-second progress bar with automatic dismissal
	- Multiple notification stacking
	- Quick navigation to the reminders page from notifications
	
	Modern User Interface
	- Responsive dashboard layout
	- Collapsible navigation sidebar
	- Dark and light theme support
	- Settings panel
	- Smooth animations and transitions
	- Clean and intuitive user experience
	
🛠️ Technology Stack

	   Category                                      Technologies 
	Programming Language                             Java 21 
	Framework                                      Spring Boot 
	Frontend                              Thymeleaf, HTML, CSS, JavaScript 
	Database                                          MySQL 
	Data Access                             Spring Data JPA, Hibernate 
	Authentication               Session-Based Authentication, BCrypt Password Hashing 
	Build Tool                                        Maven 
	Development Environment                        Eclipse IDE 
	Version Control                                Git, GitHub 
	
🏗️ Project Architecture

	RemindMe follows the **Model-View-Controller (MVC)** architectural pattern
	provided by Spring Boot. This architecture separates the application into 
	different layers, making the code easier to maintain, test, and extend.

                           ┌──────────────────────┐
                           │       Browser        │
                           │  (User Interface)    │
                           └──────────┬───────────┘
                                      │
                           HTTP Requests / Responses
                                      │
                                      ▼
                     ┌────────────────────────────────┐
                     │        Spring Boot MVC         │
                     └────────────────┬───────────────┘
                                      │
          ┌───────────────────────────┼───────────────────────────┐
          ▼                           ▼                           ▼
	┌─────────────────┐         ┌─────────────────┐         ┌─────────────────┐
	│   Controllers   │ ─────▶  │    Services     │ ─────▶  │  Repositories   │
	└─────────────────┘         └─────────────────┘         └─────────────────┘
	                                      │
	                                      ▼
	                             ┌─────────────────┐
	                             │    Entities     │
	                             └─────────────────┘
	                                      │
	                                      ▼
	                             ┌─────────────────┐
	                             │     MySQL DB    │
	                             └─────────────────┘


	Application Components
	
	- **Config Package**     contains application configuration classes such as `SecurityConfig`.
	- **Controller Package** manages HTTP requests and coordinates user interactions.
	- **Service Package**    implements the application's business logic and communicates 
	                         between controllers and repositories.
	- **Repository Package** performs database operations using Spring Data JPA.
	- **Model Package**      contains the application's entities and enumerations used throughout the system.
	- **View Layer**         consists of Thymeleaf templates, HTML, CSS, and JavaScript that provide the user interface.
	
🗄️ Database Design

	RemindMe uses a MySQL relational database to securely store user accounts
	and reminder information. The database is designed with a one-to-many 
	relationship, allowing each user to manage multiple reminders while ensuring
	that every reminder belongs to a single user.
	
	Entity Relationship Diagram (ERD)

														+---------------------+
														|        User         |
														+---------------------+
														| user_id (PK)        |
														| name                |
														| email               |
														| password            |
														+----------+----------+
														           |
														           | 1
														           |
														           | owns
														           |
														           | *
														+----------v----------+
														|      Reminder       |
														+---------------------+
														| reminder_id (PK)    |
														| title               |
														| description         |
														| reminder_time       |
														| priority            |
														| status              |
														| created_at          |
														| user_id (FK)        |
														+---------------------+

	
	🗄️ Database Tables
	
	User
	- Stores user account information, including the user's name, email address,
	  and encrypted password. Each user can create and manage multiple reminders.
	
	Reminder
	- Stores reminder details such as the title, description, reminder date and time,
	  priority level, completion status, and creation timestamp. Every reminder is 
	  linked to a single user through a foreign key relationship.
	
📁 Project Structure

	ReminderAlertSystem
	├── src
	│   ├── main
	│   │   ├── java
	│   │   │   └── com.example.reminderalertsystem
	│   │   │       ├── config
	│   │   │       │   └── SecurityConfig.java
	│   │   │       ├── controller
	│   │   │       │   ├── AuthController.java
	│   │   │       │   ├── DashboardController.java
	│   │   │       │   └── ReminderController.java
	│   │   │       ├── model
	│   │   │       │   ├── Priority.java
	│   │   │       │   ├── Reminder.java
	│   │   │       │   ├── ReminderStatus.java
	│   │   │       │   └── User.java
	│   │   │       ├── repository
	│   │   │       │   ├── ReminderRepository.java
	│   │   │       │   └── UserRepository.java
	│   │   │       ├── service
	│   │   │       │   ├── ReminderService.java
	│   │   │       │   └── UserService.java
	│   │   │       └── ReminderAlertSystemApplication.java
	│   │   └── resources
	│   │       ├── static
	│   │       │   ├── css
	│   │       │   └── js
	│   │       ├── templates
	│   │       └── application.properties
	│   └── test
	├── .mvn
	├── .gitignore
	├── mvnw
	├── mvnw.cmd
	├── pom.xml
	└── README.md
	
🚀 Getting Started

	☁️ Deployment

	RemindMe is deployed on **Amazon Web Services (AWS EC2)**, allowing users 
	to access the application through a web browser without installing or 
	configuring the project locally.

	Live Demo

> **Application URL:** *(Will be updated after deployment)*

	Demo Account

	Email: demo@remindme.com
	Password: Demo123!
	
	The demo account includes sample reminders that demonstrate the 
	application's reminder management, dashboard, notification system,
	search, filtering, sorting, and theme customization features.

🔮 Future Enhancements

	The following features are planned for future versions of RemindMe to further improve functionality and user experience:
	
	- Email notifications for upcoming reminders.
	- Push notifications for mobile devices.
	- Recurring reminders for daily, weekly, monthly, and yearly tasks.
	- Reminder categories, labels, and tags for better organization.
	- File attachments and notes for reminders.
	- Analytics dashboard with reminder completion trends and productivity insights.
	
	
	
