/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.notification.data;

/**
 *
 * @author Sazid R Khan
 */

import java.io.Serializable;

// Represents a student/user record pulled from flat files with varied schemas.
public class StudentRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    // Core identity and profile fields; kept mutable to allow merge/patch
    // operations when
    // combining data from multiple sources.
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String program;
    private String year;
    private String role;
    private String username;

    // Minimal constructor used by simple text sources where only id, name, email,
    // and program are present.
    public StudentRecord(String id, String name, String email, String program) {
        this(id, null, null, email, program, null, null, null);
        this.firstName = name;
    }

    // Full constructor used when more attributes are available from richer data
    // sources (CSV or LOGIN.txt).
    public StudentRecord(String id, String firstName, String lastName, String email, String program, String year,
            String role, String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.program = program;
        this.year = year;
        this.role = role;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Returns the most complete available display name.
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return null;
        }
        if (firstName == null) {
            return lastName;
        }
        if (lastName == null) {
            return firstName;
        }
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        // Debug-friendly snapshot of the record state.
        return "StudentRecord{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", program='" + program + '\'' +
                ", year='" + year + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
