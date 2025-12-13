/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.reporting.service.impl;

/**
 *
 * @author Acer
 */
import crs.EligibilityAndEnrollment.EligibilityChecker;
import crs.EligibilityAndEnrollment.EligibilityChecker.StudentCourseRecord;
import crs.reporting.model.ReportRow;
import crs.reporting.service.StudentResultsProvider;

import java.util.ArrayList;
import java.util.List;

public class StudentResultsProviderImpl implements StudentResultsProvider {

    private final EligibilityChecker checker;
    private final CoursesTxtIndex courseIndex;

    public StudentResultsProviderImpl(EligibilityChecker checker) {
        this.checker = checker;
        this.courseIndex = new CoursesTxtIndex("data/courses.txt"); // IMPORTANT
    }

    @Override
    public List<ReportRow> getResults(String studentId, int semester, int ignoredYear) {
        List<StudentCourseRecord> records = checker.getStudentRecords(studentId);
        List<ReportRow> rows = new ArrayList<>();

        for (StudentCourseRecord rec : records) {
            CoursesTxtIndex.CourseInfo c = courseIndex.getById(rec.getCourseId());
            if (c == null) continue;

            int semNum = mapSemester(c.getIntake());
            if (semNum != semester) continue;

            double finalScore = calculateFinalScore(rec, c);

            rows.add(new ReportRow(
                    c.getCourseId(),
                    c.getCourseName(),
                    c.getCredits(),
                    scoreToLetter(finalScore),
                    scoreToPoint(finalScore)
            ));
        }

        return rows;
    }

    @Override
    public List<ReportRow> getResultsForYear(String studentId, int ignoredYear) {
        List<StudentCourseRecord> records = checker.getStudentRecords(studentId);
        List<ReportRow> rows = new ArrayList<>();

        for (StudentCourseRecord rec : records) {
            CoursesTxtIndex.CourseInfo c = courseIndex.getById(rec.getCourseId());
            if (c == null) continue;

            double finalScore = calculateFinalScore(rec, c);

            rows.add(new ReportRow(
                    c.getCourseId(),
                    c.getCourseName(),
                    c.getCredits(),
                    scoreToLetter(finalScore),
                    scoreToPoint(finalScore)
            ));
        }

        return rows;
    }

    @Override
    public boolean hasCompletedYear(String studentId, int ignoredYear) {
        boolean hasSpring = false;
        boolean hasFall = false;

        for (StudentCourseRecord rec : checker.getStudentRecords(studentId)) {
            CoursesTxtIndex.CourseInfo c = courseIndex.getById(rec.getCourseId());
            if (c == null) continue;

            int sem = mapSemester(c.getIntake());
            if (sem == 1) hasSpring = true;
            if (sem == 2) hasFall = true;
        }

        return hasSpring && hasFall;
    }

    private double calculateFinalScore(StudentCourseRecord rec, CoursesTxtIndex.CourseInfo c) {
        return (rec.getExamScore() * c.getExamWeight() / 100.0)
             + (rec.getAssignmentScore() * c.getAssignmentWeight() / 100.0);
    }

    private int mapSemester(String s) {
        if (s == null) return 0;
        String t = s.trim().toLowerCase();
        if (t.contains("spring")) return 1;
        if (t.contains("fall") || t.contains("autumn")) return 2;
        if (t.contains("summer")) return 3;
        return 0;
    }

    private double scoreToPoint(double s) {
        if (s >= 90) return 4.0;
        else if (s >= 85) return 3.7;
        else if (s >= 80) return 3.3;
        else if (s >= 75) return 3.0;
        else if (s >= 70) return 2.7;
        else if (s >= 65) return 2.3;
        else if (s >= 60) return 2.0;
        else if (s >= 55) return 1.7;
        else if (s >= 50) return 1.0;
        else return 0.0;
    }

    private String scoreToLetter(double s) {
        if (s >= 90) return "A";
        else if (s >= 85) return "A-";
        else if (s >= 80) return "B+";
        else if (s >= 75) return "B";
        else if (s >= 70) return "B-";
        else if (s >= 65) return "C+";
        else if (s >= 60) return "C";
        else if (s >= 55) return "C-";
        else if (s >= 50) return "D";
        else return "F";
    }
}
