/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.recovery.model;

import java.time.LocalDate;
/**
 *
 * @author yal
 * Recovery item for consultation-style tasks (meet lecturer, etc.).
 */


public class ConsultationRecoveryItem extends RecoveryItem {
    
    private String lecturerName;
    private String location;

    public ConsultationRecoveryItem() {
        super();
    }

    public ConsultationRecoveryItem(String itemId,
                                    String title,
                                    String description,
                                    LocalDate dueDate,
                                    boolean completed,
                                    String lecturerName,
                                    String location) {
        super(itemId, title, description, dueDate, completed); // super usage
        this.lecturerName = lecturerName;
        this.location = location;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getItemType() {
        return "CONSULTATION";
    }
}

