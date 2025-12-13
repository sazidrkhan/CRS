/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.recovery.model;

import java.time.LocalDate;

/**
 *
 * @author yal
 */
public class GradeEntry {
        private String gradeId;
    private String componentName;
    private double marksObtained;
    private String gradeLetter;
    private LocalDate gradedDate;

    public GradeEntry() {
    }

    public GradeEntry(String gradeId,
                      String componentName,
                      double marksObtained,
                      String gradeLetter,
                      LocalDate gradedDate) {
        this.gradeId = gradeId;
        this.componentName = componentName;
        this.marksObtained = marksObtained;
        this.gradeLetter = gradeLetter;
        this.gradedDate = gradedDate;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public double getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
    }

    public String getGradeLetter() {
        return gradeLetter;
    }

    public void setGradeLetter(String gradeLetter) {
        this.gradeLetter = gradeLetter;
    }

    public LocalDate getGradedDate() {
        return gradedDate;
    }

    public void setGradedDate(LocalDate gradedDate) {
        this.gradedDate = gradedDate;
    }
}
