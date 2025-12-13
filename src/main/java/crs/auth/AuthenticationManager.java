package crs.auth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

import crs.management.PasswordUtils;
import crs.management.User;

/**
 * Manages user authentication for the Course Recovery System.
 * <p>
 * This class handles user verification by checking credentials against
 * a user database file. It supports role-based authentication and uses
 * secure password hashing for credential verification.
 * </p>
 *
 * @author Sazid R Khan
 */
public class AuthenticationManager {
    
    private static final Logger LOGGER = Logger.getLogger(AuthenticationManager.class.getName());
    private final File file;

    /**
     * Constructs an AuthenticationManager with the specified user data file.
     *
     * @param filePath the path to the user database file
     * @throws IllegalArgumentException if filePath is null or empty
     */
    public AuthenticationManager(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        this.file = new File(filePath);
    }

    /**
     * Verifies user credentials and returns the authenticated user if successful.
     * <p>
     * This method checks the provided credentials against the user database.
     * The password is hashed before comparison to ensure secure authentication.
     * </p>
     *
     * @param inputRole the expected role of the user (e.g., "admin", "student", "lecturer")
     * @param inputUsername the username to verify
     * @param inputPassword the plain text password to verify
     * @return the authenticated User object if credentials are valid, null otherwise
     */
    public User verifyUser(String inputRole, String inputUsername, String inputPassword) {
        if (inputRole == null || inputUsername == null || inputPassword == null) {
            LOGGER.warning("Null credentials provided to verifyUser");
            return null;
        }
        
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                
                // Use the User class to parse the line
                try {
                    User tempUser = new User(line); 
                    
                    String hashedInput = PasswordUtils.hashPassword(inputPassword);
                    
                    // Check credentials (case-insensitive for role and username)
                    if (tempUser.getRole().equalsIgnoreCase(inputRole) &&
                        tempUser.getUsername().equalsIgnoreCase(inputUsername) &&
                        tempUser.getPassword().equals(hashedInput)) {
                        
                        LOGGER.info("User authenticated successfully: " + inputUsername);
                        return tempUser;
                    }
                } catch (Exception e) {
                    // Ignore lines that don't match the expected format
                    LOGGER.log(Level.FINE, "Skipping invalid line in user file", e);
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "User file not found: " + file.getAbsolutePath(), e);
        }
        
        LOGGER.warning("Authentication failed for user: " + inputUsername);
        return null;
    }
}
