/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.management;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Sazid R Khan
 */
public class User {
    // Attributes (private for Encapsulation)
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

    // ✅ CONSTRUCTOR 1: The one you were missing! 
    // This allows creating a User object from individual fields (Used in Add Button)
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

    // ✅ CONSTRUCTOR 2: The CSV Parser
    // This allows creating a User object from a line of text (Used in Search/Login)
    public User(String csvLine) throws ParseException {
        String[] parts = csvLine.split(",");
        if (parts.length != 10) {
            throw new IllegalArgumentException("Invalid CSV line format.");
        }
        
        this.role = parts[0];
        this.userId = parts[1];
        this.firstName = parts[2];
        this.lastName = parts[3];
        this.username = parts[4];
        this.password = parts[5];
        this.major = parts[6];
        this.year = parts[7];
        this.email = parts[8];
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.dob = sdf.parse(parts[9]);
    }

    // --- Getters ---
    public String getRole() { return role; }
    public String getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getMajor() { return major; }
    public String getYear() { return year; }
    public String getEmail() { return email; }
    public Date getDob() { return dob; }
    public void setPassword(String newPassword) {
    this.password = newPassword;
    }

    // --- Helper to convert back to CSV format ---
    public String toCsvLine() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dobStr = sdf.format(dob);
        
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
