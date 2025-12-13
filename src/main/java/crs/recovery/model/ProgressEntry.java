/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.recovery.model;

import java.time.LocalDate;
/**
 *
 * @author Sazid R Khan
 */
public class ProgressEntry {
    
        private String progressId;
    private LocalDate date;
    private int completionPercent; // 0–100
    private String notes;

    public ProgressEntry() {
    }

    public ProgressEntry(String progressId, LocalDate date,
                         int completionPercent, String notes) {
        this.progressId = progressId;
        this.date = date;
        this.completionPercent = completionPercent;
        this.notes = notes;
    }

    public String getProgressId() {
        return progressId;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCompletionPercent() {
        return completionPercent;
    }

    public void setCompletionPercent(int completionPercent) {
        this.completionPercent = completionPercent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
