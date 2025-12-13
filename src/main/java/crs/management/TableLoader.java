/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class TableLoader {
    
    public static void loadDataIntoTable(javax.swing.JTable table, String filePath) {
        File file = new File(filePath);
        String[] columns = {"Role", "User ID", "First Name", "Last Name", "Username", "Major", "Year", "Email"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 9) { 
                    Object[] rowData = {
                        parts[0], parts[1], parts[2], parts[3], parts[4], parts[6], parts[7], parts[8]
                    };
                    model.addRow(rowData);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading table: " + e.getMessage());
        }
        table.setModel(model);
    }
    
    
    public static void loadStudentsOnly(javax.swing.JTable table, String filePath) {
        java.io.File file = new java.io.File(filePath);
    
    // 1. Define Columns specifically for the "Assign Course" task
        String[] columns = {"User ID", "First Name", "Last Name", "Major", "Year"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columns, 0);
    
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
            
            // Check if we have enough data to avoid crashes
                if (parts.length >= 8) {
                    String role = parts[0].trim();
                
                // 2. FILTER: Only add if the role is "STUDENT"
                    if (role.equalsIgnoreCase("STUDENT")) {
                        Object[] rowData = {
                            parts[1], // User ID
                            parts[2], // First Name
                            parts[3], // Last Name
                            parts[6], // Major
                            parts[7]  // Year
                        };
                        model.addRow(rowData);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading student table: " + e.getMessage());
        }
    
        table.setModel(model);
}
}
