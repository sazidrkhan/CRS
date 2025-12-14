/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.reporting.service;

/**
 *
 * @author Sazid R Khan
 */

import crs.reporting.model.AcademicReport;
import crs.reporting.model.ReportRow;
import crs.reporting.model.StudentInfo;

import java.util.List;

public class AcademicReportService {

    private final StudentInfoProvider infoProvider;
    private final StudentResultsProvider resultsProvider;

    public AcademicReportService(StudentInfoProvider infoProvider,
                                 StudentResultsProvider resultsProvider) {
        this.infoProvider = infoProvider;
        this.resultsProvider = resultsProvider;
    }

    /**
     * Generate an academic report for a student.
     *
     * @param studentId  student ID
     * @param yearly     true = yearly report, false = semester report
     * @param semester   semester number (1–3) if yearly == false, otherwise can be null
     */
    public AcademicReport generateReport(String studentId,
                                         boolean yearly,
                                         Integer semester) {

        StudentInfo info = infoProvider.getStudentInfo(studentId);
        if (info == null) {
            throw new IllegalArgumentException("Student not found for ID: " + studentId);
        }

        List<ReportRow> rows;
        if (yearly) {
            rows = resultsProvider.getResultsForYear(studentId, 0);
        } else {
            if (semester == null) {
                throw new IllegalArgumentException("Semester is required for a semester report.");
            }
            rows = resultsProvider.getResults(studentId, semester, 0);
        }

        double gpa = calculateGpa(rows);

        return new AcademicReport(info, rows, gpa, semester, yearly);
    }

    private double calculateGpa(List<ReportRow> rows) {
        double totalPoints = 0;
        int totalCredits = 0;

        for (ReportRow r : rows) {
            totalPoints += r.getGradePoint() * r.getCredits();
            totalCredits += r.getCredits();
        }

        if (totalCredits == 0) return 0.0;
        return totalPoints / totalCredits;
    }
}
