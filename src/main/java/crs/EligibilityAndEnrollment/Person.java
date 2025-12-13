package crs.EligibilityAndEnrollment;

// Abstraction - Abstract class
public abstract class Person {
    // Encapsulation - private fields
    private String id;
    private String firstName;
    private String lastName;
    
    public Person(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Encapsulation - public getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    // Abstraction - Abstract method (must be implemented by subclasses)
    public abstract String getRole();
    
    // Abstraction - Abstract method
    public abstract void displayInfo();
    
    // Polymorphism - Method to be overridden
    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}