package crs.EligibilityAndEnrollment;

public class Enrolment {
    private Student student;
    private Course course;
    private double gradePoint;
    private boolean isPassed;
    
    public Enrolment(Student student, Course course, double gradePoint) {
        this.student = student;
        this.course = course;
        this.gradePoint = gradePoint;
        this.isPassed = gradePoint >= 2.0;
    }
    
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public double getGradePoint() { return gradePoint; }
    public boolean isPassed() { return isPassed; }
    
    @Override
    public String toString() {
        return student.getStudentID() + " -> " + course.getCourseID() + 
               " | Grade: " + String.format("%.1f", gradePoint) + 
               " | " + (isPassed ? "PASS" : "FAIL");
    }
}