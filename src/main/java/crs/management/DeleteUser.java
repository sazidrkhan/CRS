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

/**
 *
 * @author HP
 */
public class DeleteUser {
    
    private final File file;
    private final String tempFilePath;

    // Constructor ensures the file path is abstracted and managed by the object
    public DeleteUser(String filePath, String tempFilePath) {
        this.file = new File(filePath);
        this.tempFilePath = tempFilePath;
    }

    /**
     * Finds and removes a user record based on their ID.
     * * @param userId The ID of the user to delete.
     * @return true if a user was found and deleted, false otherwise.
     * @throws IOException if a file operation fails.
     */
    public boolean deleteUser(String userId) throws IOException {
        
        File tempFile = new File(tempFilePath);
        boolean deleted = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Check if the line is long enough to contain the User ID
                String[] parts = line.split(",");

                // The User ID is at index 1 (after the Role, index 0)
                if (parts.length >= 2) {
                    String currentUserId = parts[1];

                    if (currentUserId.equalsIgnoreCase(userId)) {
                        deleted = true;
                        continue; // Skip writing this line to the temp file
                    }
                }
                
                // Write the line only if it was not the one to be deleted
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            // Re-throw the exception so the UI button handler can catch and display it
            throw new IOException("Failed to process user file during deletion: " + e.getMessage(), e);
        }

        // Replace original file with the temp file only if a user was actually deleted
        if (deleted) {
            if (!file.delete()) {
                // Handle case where original file could not be deleted
                tempFile.delete(); // Clean up temp file
                throw new IOException("Could not delete the original file.");
            }
            if (!tempFile.renameTo(file)) {
                // Handle case where temp file could not be renamed
                throw new IOException("Could not rename temporary file to original file name.");
            }
        } else {
            // If nothing was deleted, delete the temp file and leave the original alone
            tempFile.delete(); 
        }

        return deleted;
    }
}
