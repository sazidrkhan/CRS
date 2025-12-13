/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package crs.recovery.service;

/**
 *
 * @author yal
 * Recovery-specific eligibility abstraction.
 * "Is this student allowed to create a recovery plan for this course?".
 * Different from the Part 3 enrolment eligibility.
 */
public interface RecoveryEligibilityChecker {

    /**
     * Returns true only if the student FAILED this specific course.
     */
    boolean canRecoverCourse(String studentId, String courseCode);
}