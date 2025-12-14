/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.recovery.model;

/**
 *
 * @author Sazid R Khan
 */
public class RecoveryMilestone {
    
    private String milestoneId;
    private String studyWeek;  // e.g. "Week 1-2"
    private String task;       // e.g. "Review all lecture topic"

    public RecoveryMilestone() {
    }

    public RecoveryMilestone(String milestoneId, String studyWeek, String task) {
        this.milestoneId = milestoneId;
        this.studyWeek = studyWeek;
        this.task = task;
    }

    public String getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(String milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getStudyWeek() {
        return studyWeek;
    }

    public void setStudyWeek(String studyWeek) {
        this.studyWeek = studyWeek;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
