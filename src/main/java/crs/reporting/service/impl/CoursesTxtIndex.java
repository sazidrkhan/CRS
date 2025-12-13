/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.reporting.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class CoursesTxtIndex {

    public static class CourseInfo {
        private final String courseId;
        private final String courseName;
        private final int credits;
        private final String intake;          // Spring/Fall/Summer
        private final int assignmentWeight;   // %
        private final int examWeight;         // %

        public CourseInfo(String courseId, String courseName, int credits,
                          String intake, int assignmentWeight, int examWeight) {
            this.courseId = courseId;
            this.courseName = courseName;
            this.credits = credits;
            this.intake = intake;
            this.assignmentWeight = assignmentWeight;
            this.examWeight = examWeight;
        }

        public String getCourseId() { return courseId; }
        public String getCourseName() { return courseName; }
        public int getCredits() { return credits; }
        public String getIntake() { return intake; }
        public int getAssignmentWeight() { return assignmentWeight; }
        public int getExamWeight() { return examWeight; }
    }

    private final Map<String, CourseInfo> byId = new HashMap<>();

    public CoursesTxtIndex(String path) {
        load(path);
    }

    public CourseInfo getById(String courseId) {
        return byId.get(courseId);
    }

    private void load(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean first = true;

            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                line = line.trim();
                if (line.isEmpty()) continue;

                // Format:
                // Major,CourseID,CourseName,CreditHour,Intake,Lecturer,Assignment,Exam
                String[] d = line.split(",", -1);
                if (d.length < 8) continue;

                String courseId = d[1].trim();
                String courseName = d[2].trim();
                int credits = Integer.parseInt(d[3].trim());
                String intake = d[4].trim();
                int assignmentWeight = Integer.parseInt(d[6].trim());
                int examWeight = Integer.parseInt(d[7].trim());

                byId.put(courseId, new CourseInfo(courseId, courseName, credits, intake,
                        assignmentWeight, examWeight));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load courses.txt: " + e.getMessage(), e);
        }
    }
}
