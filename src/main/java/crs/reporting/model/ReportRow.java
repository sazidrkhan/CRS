/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.reporting.model;

public class ReportRow {

    private String code;
    private String name;
    private int credits;
    private String gradeLetter;
    private double gradePoint;

    public ReportRow(String code, String name, int credits,
                     String gradeLetter, double gradePoint) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.gradeLetter = gradeLetter;
        this.gradePoint = gradePoint;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public String getGradeLetter() {
        return gradeLetter;
    }

    public double getGradePoint() {
        return gradePoint;
    }
}
