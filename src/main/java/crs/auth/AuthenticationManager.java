/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.auth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import crs.management.PasswordUtils;
import crs.management.User;

/**
 *
 * @author Sazid R Khan
 */
public class AuthenticationManager {
    private final File file;

    public AuthenticationManager(String filePath) {
        this.file = new File(filePath);
    }

    // CHANGED: Returns 'User' instead of 'boolean'
    public User verifyUser(String inputRole, String inputUsername, String inputPassword) {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                // Use the User class to parse the line easily
                try {
                    User tempUser = new User(line); 
                    
                    String hashedInput = PasswordUtils.hashPassword(inputPassword);
                    
                    // Check credentials
                    if (tempUser.getRole().equalsIgnoreCase(inputRole) &&
                        tempUser.getUsername().equalsIgnoreCase(inputUsername) &&
                        tempUser.getPassword().equals(hashedInput)) {
                        
                        return tempUser; // SUCCESS: Return the whole user object!
                    }
                } catch (Exception e) {
                    // Ignore lines that don't match the format (e.g. empty lines)
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: User file not found.");
        }
        return null; // FAILURE: User not found
    }
    
}
