# Course Recovery System (CRS)

A comprehensive Course Recovery System designed to help students manage and track their academic recovery plans. This system provides a complete solution for course eligibility checking, enrollment management, recovery plan tracking, and academic reporting.

## Features

- **User Authentication & Authorization**: Secure login system with role-based access control for students, lecturers, course administrators, and academic officers
- **Eligibility Management**: Check student eligibility for course recovery based on academic performance
- **Enrollment System**: Manage student enrollments in recovery courses
- **Recovery Plan Management**: Create, track, and update personalized recovery plans for students
- **Progress Tracking**: Monitor student progress through recovery milestones and grade entries
- **Academic Reporting**: Generate comprehensive academic reports with PDF export functionality
- **Notification System**: Email notification system to keep students and staff informed
- **User Management**: Complete user administration including add, update, delete, and password reset

## Technology Stack

- **Language**: Java 17
- **Build Tool**: Maven
- **GUI Framework**: Java Swing
- **PDF Generation**: iText PDF
- **Email**: Jakarta Mail API
- **Architecture**: Layered architecture with separation of concerns (Model, Service, UI, Data Access)

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6+

### Building the Project

```bash
mvn clean compile
```

### Running the Application

```bash
mvn exec:java
```

## Project Structure

- `crs.auth` - Authentication and activity logging
- `crs.management` - User and course management
- `crs.recovery` - Recovery plan models, services, and GUI
- `crs.reporting` - Academic report generation and services
- `crs.notification` - Email notification system
- `crs.ui` - User interface components for different user roles
- `crs.shared` - Shared domain models (Student, Course, etc.)

## Author

Sazid R Khan

## License

This project is licensed under the MIT License - see the LICENSE file for details.
