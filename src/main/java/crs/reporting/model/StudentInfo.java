/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.reporting.model;

/**
 *
 * @author Sazid R Khan
 */

public class StudentInfo {

    private final String studentId;
    private final String studentName;
    private final String programme;
    private final String year;   

    public StudentInfo(String studentId, String studentName, String programme, String year) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.programme = programme;
        this.year = year;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getProgramme() {
        return programme;
    }

    public String getYear() {    
        return year;
    }
}

