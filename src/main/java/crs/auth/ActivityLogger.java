/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.auth;

/**
 *
 * @author HP
 */
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

public class ActivityLogger {
    private final File file;

    public ActivityLogger(String filePath) {
        this.file = new File(filePath);
    }

    /**
     * Appends a log entry to the binary file.
     * * @param userId The ID of the user.
     * @param role The role of the user.
     * @param activityType "LOGIN" or "LOGOUT"
     */
    public void logActivity(String userId, String role, String activityType) {
        // use "true" in FileOutputStream constructor to APPEND to the file
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true))) {
            
            // 1. Write User ID (UTF String)
            dos.writeUTF(userId);
            
            // 2. Write Role (UTF String)
            dos.writeUTF(role);
            
            // 3. Write Activity Type (UTF String: "LOGIN" or "LOGOUT")
            dos.writeUTF(activityType);
            
            // 4. Write Timestamp (Long - Binary form of date)
            long timestamp = new Date().getTime();
            dos.writeLong(timestamp);
            
            // Optional: Print to console just for verify it's working
            System.out.println("Binary Log written: " + userId + " - " + activityType);

        } catch (IOException e) {
            System.err.println("Error writing binary log: " + e.getMessage());
        }
    }
}
