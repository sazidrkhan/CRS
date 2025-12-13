package crs.management;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a user in the Course Recovery System.
 * <p>
 * This class encapsulates user information including authentication credentials,
 * personal details, and academic information. Users can have different roles
 * such as student, lecturer, course administrator, or academic officer.
 * </p>
 * <p>
 * The class supports two modes of construction:
 * <ul>
 *   <li>From individual fields for creating new users</li>
 *   <li>From CSV line for loading existing users from storage</li>
 * </ul>
 * </p>
 *
 * @author Sazid R Khan
 */
public class User {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    private final String role;
    private final String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String major;
    private String year;
    private String email;
    private Date dob;

    /**
     * Constructs a User with individual field values.
     * <p>
     * This constructor is typically used when creating new users through
     * the user interface or administrative functions.
     * </p>
     *
     * @param role the user's role (e.g., "student", "lecturer", "admin")
     * @param userId the unique identifier for the user
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param username the username for authentication
     * @param password the hashed password for authentication
     * @param major the user's academic major or department
     * @param year the academic year or level
     * @param email the user's email address
     * @param dob the user's date of birth
     */
    public User(String role, String userId, String firstName, String lastName, String username, 
                String password, String major, String year, String email, Date dob) {
        this.role = role;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.major = major;
        this.year = year;
        this.email = email;
        this.dob = dob;
    }

    /**
     * Constructs a User from a CSV line.
     * <p>
     * This constructor parses a comma-separated line containing user data.
     * The expected format is: role,userId,firstName,lastName,username,password,
     * major,year,email,dob (dd/MM/yyyy)
     * </p>
     *
     * @param csvLine the CSV line containing user data
     * @throws ParseException if the date format is invalid
     * @throws IllegalArgumentException if the CSV line format is invalid
     */
    public User(String csvLine) throws ParseException {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV line cannot be null or empty");
        }
        
        String[] parts = csvLine.split(",");
        if (parts.length != 10) {
            throw new IllegalArgumentException("Invalid CSV line format. Expected 10 fields, got " + parts.length);
        }
        
        this.role = parts[0].trim();
        this.userId = parts[1].trim();
        this.firstName = parts[2].trim();
        this.lastName = parts[3].trim();
        this.username = parts[4].trim();
        this.password = parts[5].trim();
        this.major = parts[6].trim();
        this.year = parts[7].trim();
        this.email = parts[8].trim();
        
        this.dob = DATE_FORMAT.parse(parts[9].trim());
    }

    // Getters
    
    /**
     * Gets the user's role.
     * @return the role
     */
    public String getRole() { 
        return role; 
    }
    
    /**
     * Gets the user's unique identifier.
     * @return the user ID
     */
    public String getUserId() { 
        return userId; 
    }
    
    /**
     * Gets the user's first name.
     * @return the first name
     */
    public String getFirstName() { 
        return firstName; 
    }
    
    /**
     * Gets the user's last name.
     * @return the last name
     */
    public String getLastName() { 
        return lastName; 
    }
    
    /**
     * Gets the username.
     * @return the username
     */
    public String getUsername() { 
        return username; 
    }
    
    /**
     * Gets the hashed password.
     * @return the password
     */
    public String getPassword() { 
        return password; 
    }
    
    /**
     * Gets the user's major or department.
     * @return the major
     */
    public String getMajor() { 
        return major; 
    }
    
    /**
     * Gets the academic year or level.
     * @return the year
     */
    public String getYear() { 
        return year; 
    }
    
    /**
     * Gets the user's email address.
     * @return the email
     */
    public String getEmail() { 
        return email; 
    }
    
    /**
     * Gets the user's date of birth.
     * @return the date of birth
     */
    public Date getDob() { 
        return dob != null ? new Date(dob.getTime()) : null; 
    }
    
    /**
     * Sets a new password for the user.
     *
     * @param newPassword the new hashed password
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Converts the user object to CSV format.
     * <p>
     * The output format is: role,userId,firstName,lastName,username,password,
     * major,year,email,dob (dd/MM/yyyy)
     * </p>
     *
     * @return the user data as a CSV line
     */
    public String toCsvLine() {
        String dobStr = dob != null ? DATE_FORMAT.format(dob) : "";
        
        return role + "," +
               userId + "," +
               firstName + "," +
               lastName + "," +
               username + "," +
               password + "," +
               major + "," +
               year + "," +
               email + "," +
               dobStr;
    }
}
