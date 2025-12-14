/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Sazid R Khan
 */
public class UpdateUser {

    private final File file;

    public UpdateUser(String filePath) {
        this.file = new File(filePath);
    }

    public boolean updateUser(
            String userId,
            String firstName,
            String lastName,
            String username,
   
            String major,
            String year,
            String email,
            Date dob
    ) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dobStr = sdf.format(dob);

        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                // Ensure line has enough parts (Role + ID = 2 minimum)
                if (parts.length < 2) {
                    lines.add(line);
                    continue;
                }

                // Fix 2: Match on userId (index 1) - The Role is at index 0.
                if (parts[1].equalsIgnoreCase(userId)) {
                    
                    // Fix 3: Read the original role (parts[0]) and prepend it.
                    // This preserves the file structure (10 fields) as required by the LOGIN.txt format.
                    String originalRole = parts[0]; 
                    
                    String updatedLine = 
                            originalRole + "," + // Must be included
                            userId + "," +
                            firstName + "," +
                            lastName + "," +
                            username + "," +
                            major + "," +
                            year + "," +
                            email + "," +
                            dobStr;
                            
                    lines.add(updatedLine); // Replace the original line
                    updated = true;
                } else {
                    lines.add(line); // Keep original line
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return false;
        }

        // Only rewrite if an update actually occurred
        if (updated) {
            // Rewrite updated list into CSV file (overwrites the entire file)
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) { 
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error writing file: " + e.getMessage());
                return false;
            }
        }

        return updated;
    }
    
    
    
}
