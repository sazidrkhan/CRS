/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.recovery.model;

import crs.shared.Course;
import crs.shared.Student;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yal
 */
public class RecoveryPlan {
    
        private String planId;
    private Student student;
    private Course course;
    private RecoveryStatus status;
    private LocalDate createdDate;
    private LocalDate lastUpdatedDate;

    private List<RecoveryItem> items = new ArrayList<>();
    private List<RecoveryMilestone> milestones = new ArrayList<>();
    private List<ProgressEntry> progressEntries = new ArrayList<>();
    private List<GradeEntry> gradeEntries = new ArrayList<>();
    

    public RecoveryPlan() {
    }

    public RecoveryPlan(String planId, Student student, Course course) {
        this.planId = planId;
        this.student = student;
        this.course = course;
        this.status = RecoveryStatus.PLANNED;
        this.createdDate = LocalDate.now();
        this.lastUpdatedDate = LocalDate.now();
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public RecoveryStatus getStatus() {
        return status;
    }

    public void setStatus(RecoveryStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDate lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public List<RecoveryItem> getItems() {
        return items;
    }

    public void setItems(List<RecoveryItem> items) {
        this.items = items;
    }

    public List<RecoveryMilestone> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<RecoveryMilestone> milestones) {
        this.milestones = milestones;
    }

    public List<ProgressEntry> getProgressEntries() {
        return progressEntries;
    }

    public void setProgressEntries(List<ProgressEntry> progressEntries) {
        this.progressEntries = progressEntries;
    }

    public List<GradeEntry> getGradeEntries() {
        return gradeEntries;
    }

    public void setGradeEntries(List<GradeEntry> gradeEntries) {
        this.gradeEntries = gradeEntries;
    }

    

    public void addItem(RecoveryItem item) {
        items.add(item);
        touch();
    }

    public void addMilestone(RecoveryMilestone milestone) {
        milestones.add(milestone);
        touch();
    }

    public void addProgressEntry(ProgressEntry progressEntry) {
        progressEntries.add(progressEntry);
        touch();
    }

    public void addGradeEntry(GradeEntry gradeEntry) {
        gradeEntries.add(gradeEntry);
        touch();
    }

    

    public double calculateCompletionPercentage() {
        if (items.isEmpty()) {
            return 0.0;
        }
        long completedCount = items.stream()
                                   .filter(RecoveryItem::isCompleted)
                                   .count();
        return (completedCount * 100.0) / items.size();
    }

    private void touch() {
        this.lastUpdatedDate = LocalDate.now();
    }
}
