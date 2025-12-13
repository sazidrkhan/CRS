/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.shared;

/**
 *
 * @author yal
 */
public class CourseComponent {


    private String componentId;    // e.g. "A1", "EXAM"
    private String componentName;  // e.g. "Assignment 1", "Final Exam";

    public CourseComponent() {
    }

    public CourseComponent(String componentId, String componentName) {
        this.componentId = componentId;
        this.componentName = componentName;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}


