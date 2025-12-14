package crs.shared;

/**
 * Represents a student in the Course Recovery System.
 * <p>
 * This class encapsulates basic student information including
 * a unique student identifier and the student's name.
 * </p>
 *
 * @author Sazid R Khan
 */
public class Student {

    private String studentId;
    private String name;

    /**
     * Default constructor.
     */
    public Student() {
    }

    /**
     * Constructs a Student with the specified ID and name.
     *
     * @param studentId the unique identifier for the student
     * @param name the student's name
     */
    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    /**
     * Gets the student's unique identifier.
     *
     * @return the student ID
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Sets the student's unique identifier.
     *
     * @param studentId the student ID to set
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Gets the student's name.
     *
     * @return the student's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the student's name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}