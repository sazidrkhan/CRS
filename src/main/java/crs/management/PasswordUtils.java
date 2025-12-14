package crs.management;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for password hashing operations.
 * <p>
 * This class provides secure password hashing using SHA-256 algorithm.
 * All passwords in the system should be hashed using this utility before
 * storage or comparison.
 * </p>
 *
 * @author Sazid R Khan
 */
public class PasswordUtils {
    
    /**
     * Hashes a plain text password using SHA-256 algorithm.
     * <p>
     * The password is converted to bytes using UTF-8 encoding and then
     * hashed using SHA-256. The resulting hash is converted to a hexadecimal
     * string representation.
     * </p>
     *
     * @param plainPassword the plain text password to hash
     * @return the hashed password as a hexadecimal string, or null if hashing fails
     * @throws IllegalArgumentException if plainPassword is null or empty
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Add password bytes to digest using UTF-8 encoding
            md.update(plainPassword.getBytes(StandardCharsets.UTF_8));
            
            // Get the hash's bytes
            byte[] bytes = md.digest();
            
            // Convert bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            
            // Get complete hashed password in hex format
            return sb.toString();
            
        } catch (NoSuchAlgorithmException e) {
            // This should never happen as SHA-256 is a standard algorithm
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
