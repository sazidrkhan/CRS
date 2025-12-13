package crs.EligibilityAndEnrollment;

// Assuming Person class handles ID, First Name, Last Name, Full Name getters
public class Student extends Person {
    
    // NEW FIELD ADDED
    private String courses;
    
    private String major;
    private String year;
    private String email;
    
    // UPDATED CONSTRUCTOR: Now accepts the 'courses' parameter (7 total fields)
    public Student(String studentId, String firstName, String lastName, String major, String courses, String year, String email) {
        super(studentId, firstName, lastName);
        this.major = major;
        this.courses = courses; // <--- NEW
        this.year = year;
        this.email = email;
    }
    
   
    public String getStudentID() {
        return super.getId();
    }
    
    // --- NEW GETTER/SETTER FOR COURSES ---
    public String getCourses() {
        return courses;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }
    // -------------------------------------
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public String getYear() {
        return year;
    }
    
    public void setYear(String year) {
        this.year = year;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String getRole() {
        return "Student";
    }
    
    @Override
    public void displayInfo() {
        System.out.println("=== Student Information ===");
        System.out.println("ID: " + getStudentID());
        System.out.println("Name: " + getFullName());
        System.out.println("Major: " + major);
        System.out.println("Courses: " + courses); // <--- NEW
        System.out.println("Year: " + year);
        System.out.println("Email: " + email);
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + getStudentID() + '\'' +
                ", name='" + getFirstName() + " " + getLastName() + '\'' +
                ", major='" + major + '\'' +
                ", courses='" + courses + '\'' + // <--- NEW
                ", year='" + year + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}