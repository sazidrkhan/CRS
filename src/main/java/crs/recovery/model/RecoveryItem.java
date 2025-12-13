/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.recovery.model;

import java.time.LocalDate;

/**
 *
 * @author yal
 * 
 * Abstract base for different kinds of recovery tasks.
 * Demonstrates abstraction + inheritance + polymorphism.
 */
public abstract class RecoveryItem {    

    private String itemId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;

    public RecoveryItem() {
    }

    public RecoveryItem(String itemId,
                        String title,
                        String description,
                        LocalDate dueDate,
                        boolean completed) {
        this.itemId = itemId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void markCompleted() {
        this.completed = true;
    }

    public abstract String getItemType();
}


