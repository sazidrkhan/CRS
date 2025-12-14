/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.recovery.model;

/**
 *
 * @author Sazid R Khan
 * 
 * Concrete recovery item for a real course component (assignment, exam).
 */

import crs.shared.CourseComponent;
import java.time.LocalDate;


public class ComponentRecoveryItem extends RecoveryItem {
    
    
    private CourseComponent component;
    private int attemptNumber; // 1, 2, or 3

    public ComponentRecoveryItem() {
        super();
    }

    public ComponentRecoveryItem(String itemId,
                                 String title,
                                 String description,
                                 LocalDate dueDate,
                                 boolean completed,
                                 CourseComponent component,
                                 int attemptNumber) {
        super(itemId, title, description, dueDate, completed); // super usage
        this.component = component;
        this.attemptNumber = attemptNumber;
    }

    public CourseComponent getComponent() {
        return component;
    }

    public void setComponent(CourseComponent component) {
        this.component = component;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    @Override
    public String getItemType() {
        return "COMPONENT";
    }
}
