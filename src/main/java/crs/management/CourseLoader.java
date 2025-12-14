/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sazid R Khan
 */
public class CourseLoader {

    // Returns a list of course names that match the given major
    public static List<String> getCoursesByMajor(String targetMajor) {
        List<String> matchingCourses = new ArrayList<>();
        File file = new File("data/Courses.txt");
        
        // DEBUG PRINT 1: Check what major we are looking for
        System.out.println("DEBUG: Searching for Major -> [" + targetMajor + "]");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                
                if (parts.length >= 3) {
                    String fileMajor = parts[0].trim();
                    String courseId = parts[1].trim();
                    String courseName = parts[2].trim();
                    
                    // DEBUG PRINT 2: Check what the file has
                    // System.out.println("DEBUG: Checking line -> File Major: [" + fileMajor + "]");

                    if (fileMajor.equalsIgnoreCase(targetMajor.trim())) { // Added .trim() to targetMajor too!
                        matchingCourses.add(courseId + " - " + courseName);
                        // System.out.println("DEBUG: *** MATCH FOUND! ***");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
        
        // DEBUG PRINT 3: Final count
        System.out.println("DEBUG: Total courses found: " + matchingCourses.size());
        
        return matchingCourses;
    }
    
    
    public static java.util.List<String> getCourseIDsOnly(String targetMajor) {
        java.util.List<String> ids = new java.util.ArrayList<>();
        java.io.File file = new java.io.File("data/COURSES.txt"); // Ensure correct path

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
            
            // File Format: Major, CourseID, CourseName, ...
                if (parts.length >= 2) {
                    String major = parts[0].trim();
                    String courseId = parts[1].trim();

                    if (major.equalsIgnoreCase(targetMajor.trim())) {
                        ids.add(courseId);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading course IDs: " + e.getMessage());
        }
        return ids;
    }
    
    
    public static java.util.List<String> getCourseNamesOnly(String targetMajor) {
    java.util.List<String> names = new java.util.ArrayList<>();
    java.io.File file = new java.io.File("data/COURSES.txt");

    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",");
            
            // Format: Major (0), ID (1), Name (2)
            if (parts.length >= 3) {
                String major = parts[0].trim();
                String courseName = parts[2].trim(); // Index 2 is the Name

                if (major.equalsIgnoreCase(targetMajor.trim())) {
                    names.add(courseName);
                }
            }
        }
    } catch (Exception e) {
        System.out.println("Error loading course names: " + e.getMessage());
    }
    return names;
}
    
}
