package crs.EligibilityAndEnrollment;

import java.io.*;
import java.util.*;

// Inheritance - Instructor extends Person
public class Instructor extends Person {
    // Encapsulation - private fields
    private String department;
    private String specialization;
    
    // Inheritance - uses super
    public Instructor(String instructorId, String firstName, String lastName, String department, String specialization) {
        super(instructorId, firstName, lastName);
        this.department = department;
        this.specialization = specialization;
    }
    
    // Encapsulation - getters and setters
    public String getInstructorId() {
        return super.getId();
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    // NEW METHOD: Assign grades to a student for a course
public boolean assignGrades(String studentId, String courseId, double examScore, double assignmentScore, Course course) {
        try {
            // Calculate final score and grade
            double finalScore = (examScore * course.getExamWeight() / 100.0) + 
                               (assignmentScore * course.getAssignmentWeight() / 100.0);
            double gpa = convertScoreToGPA(finalScore);
            String letterGrade = convertScoreToLetterGrade(finalScore);
            
            // Read existing records
            ArrayList<String> lines = new ArrayList<>();
            boolean recordExists = false;
            
            try (BufferedReader reader = new BufferedReader(new FileReader("data/GradedStudentRecords.txt"))) {
                String line;
                boolean isFirstLine = true;
                
                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        lines.add(line); // Keep header
                        isFirstLine = false;
                        continue;
                    }
                    
                    String[] data = line.split(",");
                    if (data.length >= 2 && data[0].equals(studentId) && data[1].equals(courseId)) {
                        // Update existing record
                        lines.add(studentId + "," + courseId + "," + examScore + "," + 
                                 assignmentScore + "," + String.format("%.2f", gpa) + "," + letterGrade);
                        recordExists = true;
                    } else {
                        lines.add(line);
                    }
                }
            }
            
            // If record doesn't exist, add new one
            if (!recordExists) {
                lines.add(studentId + "," + courseId + "," + examScore + "," + 
                         assignmentScore + "," + String.format("%.2f", gpa) + "," + letterGrade);
            }
            
            // Write back to file (saves the grade to GradedStudentRecords.txt)
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/GradedStudentRecords.txt"))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            
            // 🛑 CRITICAL FIX: Remove the entry from the 'not graded' file after success
            removePendingGrade(studentId, courseId);
            
            return true;
            
        } catch (IOException e) {
            System.out.println("Error assigning grades: " + e.getMessage());
            return false;
        }
    }
    
    private void removePendingGrade(String studentId, String courseId) {
    final String PENDING_FILE_PATH = "data/notGradedStudentsRecords.txt";
    List<String> remainingLines = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(PENDING_FILE_PATH))) {
        String line;
        boolean isHeader = true;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            if (isHeader) {
                remainingLines.add(line); // Keep the header
                isHeader = false;
                continue;
            }

            String[] parts = line.split(",", -1);
            if (parts.length >= 2) {
                String fileStudentId = parts[0].trim();
                String fileCourseId = parts[1].trim();

                // Keep line IF it is NOT the one we just graded
                if (!fileStudentId.equals(studentId) || !fileCourseId.equals(courseId)) {
                    remainingLines.add(line); 
                }
            } else {
                remainingLines.add(line); // Keep malformed lines
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading pending records file for deletion: " + e.getMessage());
        return;
    }

    // Rewrite the file with the remaining (still pending) records
    try (PrintWriter pw = new PrintWriter(new FileWriter(PENDING_FILE_PATH))) {
        for (String line : remainingLines) {
            pw.println(line);
        }
    } catch (IOException e) {
        System.err.println("Error writing pending records file after deletion: " + e.getMessage());
    }
}
    
    
    
    // Helper method to convert score to GPA
    private double convertScoreToGPA(double score) {
        if (score >= 90) return 4.0;
        else if (score >= 85) return 3.7;
        else if (score >= 80) return 3.3;
        else if (score >= 75) return 3.0;
        else if (score >= 70) return 2.7;
        else if (score >= 65) return 2.3;
        else if (score >= 60) return 2.0;
        else if (score >= 55) return 1.7;
        else if (score >= 50) return 1.0;
        else return 0.0;
    }
    
    // Helper method to convert score to letter grade
    private String convertScoreToLetterGrade(double score) {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else if (score >= 50) return "E";
        else return "F";
    }
    
    // NEW METHOD: Get students enrolled in instructor's courses
public ArrayList<StudentCourseAssignment> getPendingGrades() {
    ArrayList<StudentCourseAssignment> pending = new ArrayList<>();
    // 🌟 CORRECT FILE PATH: Change to the file containing only pending assignments
    final String PENDING_FILE = "data/notGradedStudentsRecords.txt"; 
    
    try (BufferedReader reader = new BufferedReader(new FileReader(PENDING_FILE))) {
        String line;
        boolean isFirstLine = true;
        
        while ((line = reader.readLine()) != null) {
            
            // 🌟 NEW: Add check for empty lines
            line = line.trim();
            if (line.isEmpty()) {
                continue; 
            }
            
            if (isFirstLine) {
                // If the header is "StudentID,CourseID" or similar, skip it.
                isFirstLine = false;
                continue; 
            }
            
            String[] data = line.split(",");
            
            // 🌟 CHECK FOR MINIMUM LENGTH (StudentID and CourseID)
            if (data.length >= 2) { 
                String studentId = data[0].trim();
                String courseId = data[1].trim();
                
                // Set examScore and assignmentScore to -1 to signify they are pending/not graded
                // The form can display "N/A" if these values are -1.
                StudentCourseAssignment assignment = new StudentCourseAssignment(studentId, courseId, -1, -1);
                pending.add(assignment);
            } else {
                System.out.println("Skipping malformed pending record: " + line);
            }
        }
    } catch (IOException e) {
        System.out.println("Error loading pending grades: " + e.getMessage());
    }
    
    return pending;
}
    
    // Abstraction - Implementing abstract method from Person
    @Override
    public String getRole() {
        return "Instructor";
    }
    
    // Abstraction - Implementing abstract method from Person
    @Override
    public void displayInfo() {
        System.out.println("=== Instructor Information ===");
        System.out.println("ID: " + getInstructorId());
        System.out.println("Name: " + getFullName());
        System.out.println("Department: " + department);
        System.out.println("Specialization: " + specialization);
    }
    
    // Polymorphism - Overriding toString
    @Override
    public String toString() {
        return "Instructor{" +
                "instructorId='" + getInstructorId() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", department='" + department + '\'' +
                ", specialization='" + specialization + '\'' +
                '}';
    }
    
    // Inner class for student-course assignments
    public static class StudentCourseAssignment {
        private String studentId;
        private String courseId;
        private double examScore;
        private double assignmentScore;
        
        public StudentCourseAssignment(String studentId, String courseId, double examScore, double assignmentScore) {
            this.studentId = studentId;
            this.courseId = courseId;
            this.examScore = examScore;
            this.assignmentScore = assignmentScore;
        }
        
        public String getStudentId() { return studentId; }
        public String getCourseId() { return courseId; }
        public double getExamScore() { return examScore; }
        public double getAssignmentScore() { return assignmentScore; }
        public boolean hasGrades() { return examScore >= 0 && assignmentScore >= 0; }
    }
}