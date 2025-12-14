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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sazid R Khan
 */
public class PasswordResetter {
    private final File file;

    public PasswordResetter(String filePath) {
        this.file = new File(filePath);
    }

    /**
     * Resets the password if the username and email match.
     */
    public boolean resetPassword(String username, String email, String newEncryptedPassword) {
        List<String> lines = new ArrayList<>();
        boolean foundAndUpdated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    // 1. Parse the user using your User class
                    User user = new User(line);

                    // 2. Verification Check: Match Username AND Email
                    if (user.getUsername().equalsIgnoreCase(username) && 
                        user.getEmail().equalsIgnoreCase(email)) {
                        
                        // 3. Update the password
                        user.setPassword(newEncryptedPassword);
                        
                        // 4. Add the UPDATED line to our list
                        lines.add(user.toCsvLine());
                        foundAndUpdated = true;
                    } else {
                        // Keep the original line
                        lines.add(line);
                    }
                } catch (Exception e) {
                    // If a line is corrupted/empty, just keep it as is
                    lines.add(line); 
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return false;
        }

        // 5. If we updated a user, rewrite the file
        if (foundAndUpdated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing file: " + e.getMessage());
                return false;
            }
        }

        return foundAndUpdated;
    }
}
