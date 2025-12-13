package crs.shared;

/**
 * Represents a component of a course (e.g., assignment, exam, project).
 * <p>
 * Course components are individual assessable items within a course
 * that students need to complete. Each component has a unique identifier
 * and a descriptive name.
 * </p>
 *
 * @author Sazid R Khan
 */
public class CourseComponent {

    private String componentId;    // e.g. "A1", "EXAM"
    private String componentName;  // e.g. "Assignment 1", "Final Exam"

    /**
     * Default constructor.
     */
    public CourseComponent() {
    }

    /**
     * Constructs a CourseComponent with the specified ID and name.
     *
     * @param componentId the unique identifier for the component (e.g., "A1", "EXAM")
     * @param componentName the descriptive name of the component (e.g., "Assignment 1", "Final Exam")
     */
    public CourseComponent(String componentId, String componentName) {
        this.componentId = componentId;
        this.componentName = componentName;
    }

    /**
     * Gets the component's unique identifier.
     *
     * @return the component ID
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * Sets the component's unique identifier.
     *
     * @param componentId the component ID to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * Gets the component's descriptive name.
     *
     * @return the component name
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Sets the component's descriptive name.
     *
     * @param componentName the component name to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}