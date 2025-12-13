/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.recovery.service;

/**
 * * Adapter class that allows the Course Recovery module
 * to use the student score data loaded by the Part 3 EligibilityChecker.
 *
 * This class answers ONE question:
 *
 *      "Has this student FAILED this specific course?"
 *
 * Because recovery is only needed for failed courses.
 *
 * The adapter reads from:
 *  - students-abood.txt
 *  - courses.txt
 *  - studentRecords.txt
 *
 * through the Part 3 EligibilityChecker (delegate).
 */

import crs.EligibilityAndEnrollment.EligibilityChecker;

import java.util.ArrayList;


public class Part3RecoveryEligibilityAdapter implements RecoveryEligibilityChecker {

    
    private final EligibilityChecker delegate;

    /**
     * Constructor.
     * When this object is created, the delegate automatically loads
     * all text files needed for determining failed courses.
     */
    
    public Part3RecoveryEligibilityAdapter() {
        this.delegate = new EligibilityChecker();
    }

    /**
     * Determines whether the student FAILED the given course.
     *
     * Recovery plans should ONLY be created for courses the student failed.
     *
     * @param studentId  The student ID
     * @param courseCode The course the recovery plan is being created for
     * @return true if recovery is allowed, i.e., if the student failed this course
     */
    
    @Override
    public boolean canRecoverCourse(String studentId, String courseCode) {

        // Retrieve all course records for this student
        // Each record contains: courseId, examScore, assignmentScore, grade
        ArrayList<EligibilityChecker.StudentCourseRecord> records =
                delegate.getStudentRecords(studentId);

        // Loop through all courses the student has taken
        for (EligibilityChecker.StudentCourseRecord rec : records) {

            // Does this record belong to the course we are checking?
            if (rec.getCourseId().equalsIgnoreCase(courseCode)) {

                String grade = rec.getGrade();   // you must add getGrade() in StudentCourseRecord

                if (grade == null) return false;

                grade = grade.trim().toUpperCase();

                // Define what counts as FAIL in our system:
                //F is fail, and E is also treated as fail (since GPA 0.00 / 1.00 )
                return grade.equals("F") || grade.equals("E");
            }
        }

        // No record found for that course so not eligible for recover
        return false;
    }
}
