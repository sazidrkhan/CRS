<<<<<<< HEAD
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

To compile the source code:

```bash
mvn clean compile
```

To create a distributable JAR package:

```bash
mvn clean package
```

This will create `CRS-1.0-SNAPSHOT.jar` in the `target` directory along with all dependencies in `target/lib`.

### Running the Application

#### Using Maven:

```bash
mvn exec:java
```

#### Using the JAR file:

```bash
java -jar target/CRS-1.0-SNAPSHOT.jar
```

#### Using the convenience script (Unix/Linux/Mac):

```bash
./run.sh
```

This script will automatically build the project and run it.

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
=======
# CRS - Course Registration System

A comprehensive Java-based Course Registration System developed as a university group project. This system provides a complete solution for managing course eligibility, enrollment, academic recovery plans, and reporting for educational institutions.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Modules](#modules)
- [Data Files](#data-files)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

The Course Registration System (CRS) is a desktop application designed to streamline academic administration processes. It provides three main modules to handle different aspects of course management:

1. **Eligibility & Enrollment** - Manage student eligibility and course enrollment
2. **Course Recovery Plan** - Track and manage student academic recovery plans
3. **Academic Reporting** - Generate and export academic reports in PDF format

## ✨ Features

### Eligibility & Enrollment Module
- Check student eligibility for course enrollment
- Enroll students in courses based on prerequisites and requirements
- View all enrolled students
- Grade students' assignments and exams
- Track student course records

### Course Recovery Plan Module
- Create and manage academic recovery plans for struggling students
- Define recovery milestones and track progress
- Set component-based recovery items (assignments, exams)
- Schedule consultations for academic support
- Monitor recovery status and completion
- Send notifications for recovery plan updates

### Academic Reporting Module
- Generate comprehensive academic reports
- Export reports to PDF format
- View student performance analytics
- Track semester and yearly progress
- Store reports for historical reference

### Additional Features
- User authentication and authorization
- Role-based access control (Admin, Academic Officer, Lecturer, Course Admin)
- Email notification system
- Activity logging
- Password reset functionality
- User management

## 🛠️ Technology Stack

- **Language**: Java (JDK 24)
- **Build Tool**: Apache Maven
- **GUI Framework**: Java Swing
- **PDF Generation**: iTextPDF 5.5.13.3
- **Email**: Jakarta Mail 1.6.7
- **IDE**: NetBeans (recommended)

### Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>com.sun.mail</groupId>
        <artifactId>jakarta.mail</artifactId>
        <version>1.6.7</version>
    </dependency>
    <dependency>
        <groupId>com.sun.activation</groupId>
        <artifactId>jakarta.activation</artifactId>
        <version>1.2.2</version>
    </dependency>
    <dependency>
        <groupId>com.itextpdf</groupId>
        <artifactId>itextpdf</artifactId>
        <version>5.5.13.3</version>
    </dependency>
</dependencies>
```

## 📥 Installation

### Prerequisites

- Java Development Kit (JDK) 24 or higher
- Apache Maven 3.6 or higher
- NetBeans IDE (optional but recommended)

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/sazidrkhan/CRS.git
   cd CRS
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="crs.Crs"
   ```

   Or if using NetBeans, simply open the project and click Run.

## 🚀 Usage

### Running the Application

1. Launch the CRS Launcher application
2. Select one of the three available modules:
   - **Eligibility & Enrollment**: Manage student enrollment and grades
   - **Course Recovery Plan**: Create and track recovery plans
   - **Academic Reporting**: Generate and view academic reports

### Login Credentials

User credentials are stored in `data/LOGIN.txt`. The system supports multiple user roles:
- Admin
- Academic Officer
- Lecturer
- Course Admin

### Data Management

The system uses text files for data persistence located in the `/data` directory:
- `students-abood.txt` - Student information
- `courses.txt` - Course catalog
- `enrollments.txt` - Course enrollment records
- `recovery_plans.txt` - Recovery plan data
- `GradedStudentRecords.txt` - Graded student records
- `notGradedStudentsRecords.txt` - Pending grade records
- `LOGIN.txt` - User authentication data

Reports are saved in the `/reports` directory in PDF format.

## 📁 Project Structure

```
CRS/
├── src/
│   └── main/
│       └── java/
│           └── crs/
│               ├── Crs.java                          # Main launcher
│               ├── auth/                             # Authentication
│               │   ├── AuthenticationManager.java
│               │   └── ActivityLogger.java
│               ├── EligibilityAndEnrollment/         # Eligibility logic
│               │   └── EligibilityChecker.java
│               ├── EligibilityAndEnrollmentFrames/   # Eligibility UI
│               │   ├── MainForm.java
│               │   ├── EligibilityCheckerForm.java
│               │   ├── EnrollmentForm.java
│               │   └── GradeAssignmentsAndExamsForm.java
│               ├── recovery/                         # Recovery module
│               │   ├── gui/                          # Recovery UI
│               │   ├── io/                           # Data persistence
│               │   ├── model/                        # Domain models
│               │   └── service/                      # Business logic
│               ├── reporting/                        # Reporting module
│               │   ├── model/                        # Report models
│               │   ├── pdf/                          # PDF export
│               │   ├── service/                      # Report services
│               │   └── ui/                           # Reporting UI
│               ├── notification/                     # Notifications
│               │   ├── EmailService.java
│               │   ├── NotificationManager.java
│               │   └── EmailGUI.java
│               ├── management/                       # Course management
│               ├── shared/                           # Shared models
│               │   ├── Student.java
│               │   ├── Course.java
│               │   └── CourseComponent.java
│               └── ui/                               # Common UI components
│                   ├── LOGIN_FRAME.java
│                   ├── AdminFrame.java
│                   ├── AcademicOfficerDashboard.java
│                   ├── CourseAdminDashboard.java
│                   └── lecturerFrame.java
├── data/                                             # Data files
├── reports/                                          # Generated reports
├── pom.xml                                           # Maven configuration
├── nbactions.xml                                     # NetBeans actions
├── LICENSE                                           # MIT License
└── README.md                                         # This file
```

## 🔧 Modules

### 1. Eligibility & Enrollment
- **Package**: `crs.EligibilityAndEnrollment`, `crs.EligibilityAndEnrollmentFrames`
- **Purpose**: Manage student course eligibility and enrollment processes
- **Key Classes**:
  - `MainForm` - Main dashboard for the module
  - `EligibilityChecker` - Core eligibility checking logic
  - `EnrollmentForm` - Student enrollment interface
  - `GradeAssignmentsAndExamsForm` - Grading interface

### 2. Course Recovery Plan
- **Package**: `crs.recovery.*`
- **Purpose**: Support students who need academic recovery
- **Key Classes**:
  - `FrmCourseRecoveryPlan` - Recovery plan management UI
  - `RecoveryPlanManager` - Recovery plan business logic
  - `RecoveryEligibilityChecker` - Check recovery eligibility
  - `FileRecoveryPlanRepository` - Data persistence

### 3. Academic Reporting
- **Package**: `crs.reporting.*`
- **Purpose**: Generate comprehensive academic reports
- **Key Classes**:
  - `FrmAcademicReport` - Report generation UI
  - `AcademicReportPdfExporter` - PDF export functionality
  - `RecoveryFeedbackProvider` - Generate feedback

## 📊 Data Files

The system uses text-based data files for persistence:

- **courses.txt**: Course catalog with format `Major,CourseID,CourseName,CreditHour,Intake,Lecturer,Assignment,Exam`
- **students-abood.txt**: Student records
- **enrollments.txt**: Course enrollment data
- **recovery_plans.txt**: Academic recovery plans
- **LOGIN.txt**: User authentication credentials
- **GradedStudentRecords.txt**: Completed grade entries
- **notGradedStudentsRecords.txt**: Pending grade entries

## 👥 Contributing

This project was developed as a university group project. Contributions, issues, and feature requests are welcome!

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

Copyright (c) 2025 Sazid R Khan

## 🙏 Acknowledgments

- Developed as a university group project
- Built using Java Swing for desktop GUI
- PDF generation powered by iTextPDF
- Email notifications using Jakarta Mail

---

**Note**: This is an educational project developed for academic purposes. For production use, consider implementing a proper database backend, enhanced security measures, and comprehensive error handling.
>>>>>>> copilot/create-readme-file
