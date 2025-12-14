/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.reporting.model;
import java.util.List;
/**
 *
 * @author Sazid R Khan
 */


import java.util.List;

public class AcademicReport {

    private StudentInfo studentInfo;
    private List<ReportRow> rows;
    private double gpa;
    private Integer semester;    // null if yearly report
    private boolean yearlyReport;

    public AcademicReport(StudentInfo studentInfo,
                          List<ReportRow> rows,
                          double gpa,
                          Integer semester,
                          boolean yearlyReport) {
        this.studentInfo = studentInfo;
        this.rows = rows;
        this.gpa = gpa;
        this.semester = semester;
        this.yearlyReport = yearlyReport;
    }

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public List<ReportRow> getRows() {
        return rows;
    }

    public double getGpa() {
        return gpa;
    }

    public Integer getSemester() {
        return semester;
    }

    public boolean isYearlyReport() {
        return yearlyReport;
    }
}

