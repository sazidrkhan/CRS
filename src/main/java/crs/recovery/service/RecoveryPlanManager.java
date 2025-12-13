/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.recovery.service;

import crs.recovery.io.RecoveryPlanRepository;
import crs.recovery.model.*;
import crs.shared.Course;
import crs.shared.Student;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yal
 */
public class RecoveryPlanManager {

    private final RecoveryEligibilityChecker eligibilityChecker;
    private final RecoveryPlanRepository repository;
    private final CourseRecoveryPolicy policy;
    private List<RecoveryPlan> plans = new ArrayList<>();

    public RecoveryPlanManager(RecoveryEligibilityChecker eligibilityChecker,
                               RecoveryPlanRepository repository,
                               CourseRecoveryPolicy policy) {
        this.eligibilityChecker = eligibilityChecker;
        this.repository = repository;
        this.policy = policy;
        loadExistingPlans();
    }

    private void loadExistingPlans() {
        try {
            List<RecoveryPlan> loaded = repository.loadAll();
            if (loaded != null) {
                this.plans = loaded;
            }
        } catch (IOException e) {
            System.err.println("Could not load recovery plans: " + e.getMessage());
            this.plans = new ArrayList<>();
        }
    }

    public List<RecoveryPlan> getAllPlans() {
        return plans;
    }

    public RecoveryPlan getPlanById(String planId) {
        for (RecoveryPlan plan : plans) {
            if (plan.getPlanId().equals(planId)) {
                return plan;
            }
        }
        return null;
    }

    /**
     * Only AcademicOfficer can create a plan.
     */
    public void createPlan(String planId,
                           Student student,
                           Course course) {
        
        //Check if there is already an active (non-COMPLETED) plan
        for (RecoveryPlan existing : plans) {
            if (existing.getStudent() != null
                    && existing.getCourse() != null
                    && existing.getStudent().getStudentId().equalsIgnoreCase(student.getStudentId())
                    && existing.getCourse().getCourseCode().equalsIgnoreCase(course.getCourseCode())
                    && existing.getStatus() != RecoveryStatus.CANCELLED
                    && existing.getStatus() != RecoveryStatus.COMPLETED ) {

                throw new IllegalStateException(
                        """
                        There is already an active recovery plan for this student and course.
                        Please complete or cancel the existing plan before creating a new attempt.""");
            }
        }

        //only failed courses can have recovery
        if (!eligibilityChecker.canRecoverCourse(student.getStudentId(),
                                         course.getCourseCode())) {
            throw new IllegalStateException(
                "Student has not failed this course in the records – no recovery plan needed.");
        }

        
        //Create the new plan (this represents the *next* attempt)
        RecoveryPlan plan = new RecoveryPlan(planId, student, course);
        plan.setStatus(RecoveryStatus.PLANNED);
        
        int attempt = determineAttemptForPlan(plan);
        if (attempt > policy.getMaxAttempts()) {
            throw new IllegalStateException(
                "Maximum attempts reached (" + policy.getMaxAttempts() + "). "
              + "Cannot create another recovery plan for this student and course."
            );
        }

        
        
        plans.add(plan);
        saveAll();
    }

    public void changeStatus(String planId,
                         RecoveryStatus newStatus) {

        RecoveryPlan plan = getPlanById(planId);
        if (plan == null) {
            throw new IllegalStateException("Plan not found.");
        }

        // Rule 1: IN_PROGRESS requires items + milestones
        if (newStatus == RecoveryStatus.IN_PROGRESS && !policy.canStart(plan)) {
            throw new IllegalStateException(
                    "Cannot set status to IN_PROGRESS. "
                  + "At least one Item and one Milestone are required.");
        }

        // Rule 2: COMPLETED requires progress + grades
        if (newStatus == RecoveryStatus.COMPLETED && !policy.canComplete(plan)) {
            throw new IllegalStateException(
                    "Cannot set status to COMPLETED. "
                  + "At least one Progress entry and one Grade entry are required.");
        }
        
        
        // Rule 3: 
        if (newStatus == RecoveryStatus.IN_PROGRESS
            || newStatus == RecoveryStatus.COMPLETED) {

            int attempt = determineAttemptForPlan(plan);

            if (policy.mustRecoverAllComponents(attempt)
                    && !hasAllCoreComponents(plan)) {

                throw new IllegalStateException(
                    "3rd attempt requires BOTH components (Assignment and Final Exam) "
                  + "to be added as recovery items in this plan.");
            }
        }


        // If passed policy, apply
        plan.setStatus(newStatus);
        
        saveAll();
    }

    public void addItemToPlan(String planId,
                              RecoveryItem item) {
        RecoveryPlan plan = getPlanById(planId);
        if (plan == null) return;

        // Course Retrieval Policy enforcement for Component items
        if (item instanceof ComponentRecoveryItem) {
            ComponentRecoveryItem cri = (ComponentRecoveryItem) item;

            // Let policy/manager decide the attempt number automatically
            int attempt = determineAttemptForPlan(plan);
            cri.setAttemptNumber(attempt);   // override any GUI value

            // Check attempt validity
            if (!policy.canAttempt(attempt)) {
                throw new IllegalArgumentException(
                        "Attempt " + attempt + " exceeds maximum allowed attempts ("
                                + policy.getMaxAttempts() + ").");
            }
  
        }

        plan.addItem(item);
        
        saveAll();
    }

    public void addMilestoneToPlan(String planId,
                                   RecoveryMilestone milestone) {
        RecoveryPlan plan = getPlanById(planId);
        if (plan == null) return;

        plan.addMilestone(milestone);
        
        saveAll();
    }

    public void recordProgress(String planId,
                               ProgressEntry progressEntry) {
        RecoveryPlan plan = getPlanById(planId);
        if (plan == null) return;

        plan.addProgressEntry(progressEntry);
        
        saveAll();
    }

    public void recordGrade(String planId,
                            GradeEntry gradeEntry) {
        RecoveryPlan plan = getPlanById(planId);
        if (plan == null) return;

        plan.addGradeEntry(gradeEntry);
        
        saveAll();
    }

    public double getCompletionPercentage(String planId) {
        RecoveryPlan plan = getPlanById(planId);
        if (plan == null) return 0.0;
        return plan.calculateCompletionPercentage();
    }

    //helper to compute attempt for the plan
    /**
     * Determines the correct attempt number for this recovery plan
     * based on existing recovery plans for the SAME student and course.
     *
     * Assumption:
     * - Attempt 1 already happened and is stored in studentRecords.txt.
     * - Each recovery plan for this (student, course) represents one
     *   additional attempt (2nd, then 3rd).
     */
    private int determineAttemptForPlan(RecoveryPlan plan) {
        if (plan == null || plan.getStudent() == null || plan.getCourse() == null) {
            // Safe default: first recovery = 2nd attempt
            return 2;
        }

        String studentId = plan.getStudent().getStudentId();
        String courseCode = plan.getCourse().getCourseCode();

        int completedRecoveryCount = 0;

        for (RecoveryPlan p : plans) {
            if (p == plan) continue; // don't count itself

            if (p.getStudent() != null
                    && p.getCourse() != null
                    && p.getStudent().getStudentId().equalsIgnoreCase(studentId)
                    && p.getCourse().getCourseCode().equalsIgnoreCase(courseCode)
                    && p.getStatus() == RecoveryStatus.COMPLETED) {
                completedRecoveryCount++;
            }
        }

        // First recovery plan → attempt 2
        // Second recovery plan → attempt 3
        // Third recovery plan → attempt 4 (should be blocked by createPlan)
        int computed = 2 + completedRecoveryCount;
        return computed;
    }
    
    
    //Helper to Delete Plan
    public void deletePlan(String planId) {
        RecoveryPlan plan = getPlanById(planId);
        if (plan == null) {
            throw new IllegalStateException("Plan not found.");
        }

        

        // Remove from list
        plans.remove(plan);

        // Save to file
        saveAll();
    }

    
    // ================== Helper methods ==================
    
    /**
    * Checks whether the plan contains BOTH required course components:
    *
    *   1) "Assignment"
    *   2) "Final Exam"
    *
    * These are the ONLY two components allowed in our system.
    */
    public boolean hasAllCoreComponents(RecoveryPlan plan) {
        if (plan == null) return false;

        List<RecoveryItem> items = plan.getItems();
        if (items == null || items.isEmpty()) {
            return false;
        }

        boolean hasFinalExam = false;
        boolean hasAssignment = false;

        for (RecoveryItem item : items) {
            if (item instanceof ComponentRecoveryItem) {
                ComponentRecoveryItem cri = (ComponentRecoveryItem) item;
                if (cri.getComponent() == null) continue;

                String name = cri.getComponent().getComponentName();
                if (name == null) continue;

                if (name.equalsIgnoreCase("Final Exam")) {
                    hasFinalExam = true;
                } else if (name.equalsIgnoreCase("Assignment")) {
                    hasAssignment = true;
                }
            }
        }

        return hasFinalExam && hasAssignment;
    }
    
    
    
    

    private void saveAll() {
        try {
            repository.saveAll(plans);
        } catch (IOException e) {
            System.err.println("Could not save recovery plans: " + e.getMessage());
        }
    } 
    
    public void persistUpdates() {
    saveAll();
    }
}
