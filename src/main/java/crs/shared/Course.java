package crs.shared;

/**
 * Represents a course in the Course Recovery System.
 * <p>
 * This class encapsulates basic course information, primarily
 * the unique course code that identifies the course.
 * </p>
 *
 * @author Sazid R Khan
 */
public class Course {
    
    private String courseCode;

    /**
     * Default constructor.
     */
    public Course() {
    }

    /**
     * Constructs a Course with the specified course code.
     *
     * @param courseCode the unique course code
     */
    public Course(String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * Gets the course code.
     *
     * @return the course code
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Sets the course code.
     *
     * @param courseCode the course code to set
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}