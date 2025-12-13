/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.recovery.model;

import java.util.List;

/**
 *
 * @author yal
 * 
 * Represents the course retrieval policy:
 * - Max 3 attempts
 * - 2nd attempt: recover failed components only
 * - 3rd attempt: must refer to all components (Exam + Assignment)
 *
 * Also includes plan-level checks for when a plan can:
 * - move to IN_PROGRESS
 * - move to COMPLETED
 */

public class CourseRecoveryPolicy {
    

    private int maxAttempts = 3;
    private boolean allowComponentResubmission = true;
    private boolean requireAllComponentsOn3rdAttempt = true;

    public CourseRecoveryPolicy() {
    }

    public CourseRecoveryPolicy(int maxAttempts,
                                boolean allowComponentResubmission,
                                boolean requireAllComponentsOn3rdAttempt) {
        this.maxAttempts = maxAttempts;
        this.allowComponentResubmission = allowComponentResubmission;
        this.requireAllComponentsOn3rdAttempt = requireAllComponentsOn3rdAttempt;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public boolean isAllowComponentResubmission() {
        return allowComponentResubmission;
    }

    public void setAllowComponentResubmission(boolean allowComponentResubmission) {
        this.allowComponentResubmission = allowComponentResubmission;
    }

    public boolean isRequireAllComponentsOn3rdAttempt() {
        return requireAllComponentsOn3rdAttempt;
    }

    public void setRequireAllComponentsOn3rdAttempt(boolean requireAllComponentsOn3rdAttempt) {
        this.requireAllComponentsOn3rdAttempt = requireAllComponentsOn3rdAttempt;
    }

    //Is this attempt number allowed at all?
    public boolean canAttempt(int currentAttempt) {
        return currentAttempt <= maxAttempts && currentAttempt >= 1;
    }

    //Do we require "all components" for this attempt?
    //2nd attempt = only failed components need to be recovered
    //3rd attempt = must refer to all components (Exam + Assignment)
    public boolean mustRecoverAllComponents(int currentAttempt) {
        return requireAllComponentsOn3rdAttempt
                && currentAttempt == maxAttempts;
    }
    
    
    //======= Plan-State rules =======
    //Can this plan move to IN_PROGRESS?
    public boolean canStart(RecoveryPlan plan) {
        if (plan == null) return false;

        return plan.getItems() != null && !plan.getItems().isEmpty()
            && plan.getMilestones() != null && !plan.getMilestones().isEmpty();
    }
    
    //Can this plan move to COMPLETED?

    public boolean canComplete(RecoveryPlan plan) {
        if (plan == null) return false;

        return plan.getProgressEntries() != null && !plan.getProgressEntries().isEmpty()
            && plan.getGradeEntries() != null && !plan.getGradeEntries().isEmpty();
    }
    
    
}


