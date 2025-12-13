/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Sazid R Khan
 */
public class SearchUser {
    
    private final File file;

    // Use Composition to manage the file path
    public SearchUser(String filePath) {
        this.file = new File(filePath);
    }

    /**
     * Searches for a user by their ID and returns a fully constructed User object.
     * * @param userId The ID of the user to find.
     * @return The User object if found, or null otherwise.
     * @throws Exception if a file operation or date parsing fails.
     */
    public User findUserById(String userId) throws Exception {
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                // We split the line only for the quick check
                String[] data = line.split(",");
                
                // Check format validity and match on User ID (index 1)
                if (data.length == 10 && data[1].equalsIgnoreCase(userId)) {
                    // Use the User class constructor (Abstraction/Encapsulation) 
                    // to handle the parsing of the entire line (including DOB).
                    return new User(line); 
                }
            }

        } catch (IOException e) {
            // Re-throw as a checked exception to be handled by the UI layer
            throw new IOException("Error reading file during search: " + e.getMessage(), e);
        }
        
        return null; // User not found
    }
}
