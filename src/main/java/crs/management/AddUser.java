/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.management;

/**
 *
 * @author Sazid R Khan
 */
import java.io.*;
import java.util.*;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddUser {

    private final File file;

    public AddUser(String filePath) {
        this.file = new File(filePath);
    }

    // FIXED METHOD: Now accepts a single User object
    public boolean addUser(User user) throws IOException {
        // Use the User object's internal method to format the CSV line
        // This is Abstraction in action!
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(user.toCsvLine());
            bw.newLine();
        }
        return true;
    }

    // ID GENERATOR: Keeps your auto-ID feature working
    public String generateNewUserId() {
        int maxId = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Ensure valid line and ID is at index 1
                if (parts.length >= 2) {
                    String userId = parts[1].trim();
                    if (userId.startsWith("TP")) {
                        try {
                            String numberPart = userId.substring(2); 
                            int currentId = Integer.parseInt(numberPart);
                            if (currentId > maxId) {
                                maxId = currentId;
                            }
                        } catch (NumberFormatException e) {
                            // Ignore invalid formats
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error generating ID: " + e.getMessage());
        }

        int newId = maxId + 1;
        return String.format("TP%03d", newId);
    }
    
    public boolean isUserUnique(String username, String email) {
    
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

            // Ensure the line is long enough (Username is at index 4, Email is at index 8)
                if (parts.length >= 9) {
                    String existingUsername = parts[4].trim();
                    String existingEmail = parts[8].trim();

                // Check for duplicate Username OR duplicate Email
                    if (existingUsername.equalsIgnoreCase(username) || existingEmail.equalsIgnoreCase(email)) {
                      return false; // Not unique! Duplicate found.
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking user uniqueness: " + e.getMessage());
        // For safety, assume unique if file cannot be read, but log the error
            return true; 
        }
    
        return true; // Unique! No matches found.
    }
    
    public void assignDefaultCourses(String studentId, String major) {
    // 1. Get the 5 course IDs for this major
    java.util.List<String> courseIds = CourseLoader.getCourseIDsOnly(major);
    
    if (courseIds.isEmpty()) {
        System.out.println("Warning: No courses found for major " + major);
        return;
    }

    // 2. Open the NEW file in APPEND mode
    java.io.File file = new java.io.File("data/notGradedStudentsRecords.txt");
    
    // Ensure the file exists (optional safety check)
    if (!file.exists()) {
        try {
            file.createNewFile();
        } catch (java.io.IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }
    
    try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(file, true))) {
        for (String courseId : courseIds) {
            // 3. Write ONLY StudentID and CourseID
            String recordLine = studentId + "," + courseId;
            
            bw.write(recordLine);
            bw.newLine();
        }
        System.out.println("Auto-assigned " + courseIds.size() + " courses to " + studentId);
        
    } catch (java.io.IOException e) {
        System.out.println("Error auto-assigning courses: " + e.getMessage());
    }
}
    
    
    public void saveTostudentFile(User student) {
    // 1. Define the file path
    java.io.File file = new java.io.File("data/students-abood.txt");
    
    // 2. Create file if it doesn't exist
    if (!file.exists()) {
        try {
            file.createNewFile();
        } catch (java.io.IOException e) {
            System.out.println("Error creating students-abood.txt: " + e.getMessage());
        }
    }

    // 3. Get the Course NAMES Only
    java.util.List<String> courseList = CourseLoader.getCourseNamesOnly(student.getMajor());
    
    // Join them with semicolons: "Ethical Hacking;Digital Forensics;..."
    String coursesString = String.join(";", courseList);
    
    if (coursesString.isEmpty()) {
        coursesString = "None";
    }

    // 4. Write to file
    try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(file, true))) {
        
        String line = student.getUserId() + "," + 
                      student.getFirstName() + "," + 
                      student.getLastName() + "," + 
                      student.getMajor() + "," + 
                      coursesString + "," +    // <--- Now contains Names separated by ';'
                      student.getYear() + "," +
                      student.getEmail();
        
        bw.write(line);
        bw.newLine();
        System.out.println("Saved to students-abood.txt with course names.");
        
    } catch (java.io.IOException e) {
        System.out.println("Error writing to students-abood.txt: " + e.getMessage());
    }
}

}

