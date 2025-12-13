/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.recovery.io;


import crs.recovery.model.ComponentRecoveryItem;
import crs.recovery.model.ConsultationRecoveryItem;
import crs.recovery.model.GradeEntry;
import crs.recovery.model.ProgressEntry;
import crs.recovery.model.RecoveryItem;
import crs.recovery.model.RecoveryMilestone;
import crs.recovery.model.RecoveryPlan;
import crs.recovery.model.RecoveryStatus;
import crs.shared.Course;
import crs.shared.CourseComponent;
import crs.shared.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yal
 */
public class FileRecoveryPlanRepository implements RecoveryPlanRepository {

    private final String filePath;

    public FileRecoveryPlanRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<RecoveryPlan> loadAll() throws IOException {
        List<RecoveryPlan> plans = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            RecoveryPlan currentPlan = null;
            List<RecoveryItem> currentItems = new ArrayList<>();
            List<RecoveryMilestone> currentMilestones = new ArrayList<>();
            List<ProgressEntry> currentProgressEntries = new ArrayList<>();
            List<GradeEntry> currentGrades = new ArrayList<>();

            String section = "NONE";

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // end of one plan
                if ("---".equals(line)) {
                    if (currentPlan != null) {
                        currentPlan.setItems(currentItems);
                        currentPlan.setMilestones(currentMilestones);
                        currentPlan.setProgressEntries(currentProgressEntries);
                        currentPlan.setGradeEntries(currentGrades);
                        plans.add(currentPlan);
                    }
                    currentPlan = null;
                    currentItems = new ArrayList<>();
                    currentMilestones = new ArrayList<>();
                    currentProgressEntries = new ArrayList<>();
                    currentGrades = new ArrayList<>();
                    section = "NONE";
                    continue;
                }

                // header lines
                if (line.startsWith("PLAN:")) {
                    String planId = line.substring("PLAN:".length()).trim();
                    currentPlan = new RecoveryPlan();
                    currentPlan.setPlanId(planId);
                    currentPlan.setStatus(RecoveryStatus.PLANNED);
                    continue;
                }
                if (line.startsWith("STUDENT:") && currentPlan != null) {
                    String studentId = line.substring("STUDENT:".length()).trim();
                    currentPlan.setStudent(new Student(studentId, ""));
                    continue;
                }
                if (line.startsWith("COURSE:") && currentPlan != null) {
                    String courseCode = line.substring("COURSE:".length()).trim();
                    currentPlan.setCourse(new Course(courseCode));
                    continue;
                }
                if (line.startsWith("STATUS:") && currentPlan != null) {
                    String statusStr = line.substring("STATUS:".length()).trim();
                    currentPlan.setStatus(RecoveryStatus.valueOf(statusStr));
                    continue;
                }

                // section markers
                if ("ITEMS:".equals(line)) {
                    section = "ITEMS";
                    continue;
                }
                if ("MILESTONES:".equals(line)) {
                    section = "MILESTONES";
                    continue;
                }
                if ("PROGRESS:".equals(line)) {
                    section = "PROGRESS";
                    continue;
                }
                if ("GRADES:".equals(line)) {
                    section = "GRADES";
                    continue;
                }

                // detail lines
                switch (section) {
                    case "ITEMS":
                        if (line.startsWith("ITEM|")) {
                            // Option A format
                            // COMPONENT:
                            // ITEM|itemId|COMPONENT|title|dueDate|completed|attemptNumber|componentName
                            //
                            // CONSULTATION:
                            // ITEM|itemId|CONSULTATION|title|dueDate|completed|lecturerName|location
                            String[] parts = line.split("\\|");

                            if (parts.length < 6) {
                                // invalid, skip
                                break;
                            }

                            String itemId = parts[1];
                            String type = parts[2];
                            String title = parts[3];
                            LocalDate due = LocalDate.parse(parts[4]);
                            boolean completed = Boolean.parseBoolean(parts[5]);

                            if ("COMPONENT".equalsIgnoreCase(type)) {
                                int attempt = 1;
                                String componentName = title;

                                if (parts.length > 6 && !parts[6].isEmpty()) {
                                    try {
                                        attempt = Integer.parseInt(parts[6]);
                                    } catch (NumberFormatException ex) {
                                        attempt = 1;
                                    }
                                }
                                if (parts.length > 7 && !parts[7].isEmpty()) {
                                    componentName = parts[7];
                                }

                                CourseComponent cc = new CourseComponent("", componentName);
                                ComponentRecoveryItem cri =
                                        new ComponentRecoveryItem(itemId, title,
                                                "", due, completed, cc, attempt);
                                currentItems.add(cri);

                            } else if ("CONSULTATION".equalsIgnoreCase(type)) {
                                String lecturerName = "";
                                String location = "";

                                if (parts.length > 6) {
                                    lecturerName = parts[6];
                                }
                                if (parts.length > 7) {
                                    location = parts[7];
                                }

                                ConsultationRecoveryItem ci =
                                        new ConsultationRecoveryItem(itemId, title,
                                                "", due, completed, lecturerName, location);
                                currentItems.add(ci);
                            }
                        }
                        break;

                    case "MILESTONES":
                        if (line.startsWith("MS|")) {
                            // MS|milestoneId|studyWeek|task
                            String[] parts = line.split("\\|");
                            if (parts.length >= 4) {
                                String msId = parts[1];
                                String week = parts[2];
                                String task = parts[3];
                                RecoveryMilestone ms = new RecoveryMilestone(msId, week, task);
                                currentMilestones.add(ms);
                            }
                        }
                        break;

                    case "PROGRESS":
                        if (line.startsWith("PE|")) {
                        // PE|progressId|date|completionPercent|notes (notes optional)
                        String[] parts = line.split("\\|");
                        if (parts.length >= 4) {
                            String pId = parts[1];
                            LocalDate date = LocalDate.parse(parts[2]);
                            int percent = Integer.parseInt(parts[3]);
                            String notes = "";
                            if (parts.length >= 5) {
                                notes = parts[4];
                            }
                            ProgressEntry pe = new ProgressEntry(pId, date, percent, notes);
                            currentProgressEntries.add(pe);
                        }
                    }
                    break;

                    case "GRADES":
                        if (line.startsWith("GR|")) {
                            // GR|gradeId|componentName|marks|gradeLetter|gradedDate
                            String[] parts = line.split("\\|");
                            if (parts.length >= 6) {
                                String gId = parts[1];
                                String componentName = parts[2];
                                double marks = Double.parseDouble(parts[3]);
                                String letter = parts[4];
                                LocalDate gradedDate = LocalDate.parse(parts[5]);
                                GradeEntry ge =
                                        new GradeEntry(gId, componentName, marks, letter, gradedDate);
                                currentGrades.add(ge);
                            } else if (parts.length == 5) {
                                // backward compatibility: no date -> use today
                                String gId = parts[1];
                                String componentName = parts[2];
                                double marks = Double.parseDouble(parts[3]);
                                String letter = parts[4];
                                GradeEntry ge =
                                        new GradeEntry(gId, componentName, marks, letter, LocalDate.now());
                                currentGrades.add(ge);
                            }
                        }
                        break;

                    default:
                        break;
                }
            }

            // last plan if file does not end with ---
            if (currentPlan != null) {
                currentPlan.setItems(currentItems);
                currentPlan.setMilestones(currentMilestones);
                currentPlan.setProgressEntries(currentProgressEntries);
                currentPlan.setGradeEntries(currentGrades);
                plans.add(currentPlan);
            }
        }

        return plans;
    }

    @Override
    public void saveAll(List<RecoveryPlan> plans) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (RecoveryPlan plan : plans) {
                // header
                writer.write("PLAN:" + plan.getPlanId());
                writer.newLine();
                if (plan.getStudent() != null) {
                    writer.write("STUDENT:" + plan.getStudent().getStudentId());
                    writer.newLine();
                }
                if (plan.getCourse() != null) {
                    writer.write("COURSE:" + plan.getCourse().getCourseCode());
                    writer.newLine();
                }
                writer.write("STATUS:" + plan.getStatus().name());
                writer.newLine();
                writer.newLine();

                // ITEMS
                writer.write("ITEMS:");
                writer.newLine();
                for (RecoveryItem item : plan.getItems()) {
                    if (item instanceof ComponentRecoveryItem) {
                        ComponentRecoveryItem cri = (ComponentRecoveryItem) item;
                        String componentName = "";
                        if (cri.getComponent() != null) {
                            componentName = cri.getComponent().getComponentName();
                        }
                        // ITEM|itemId|COMPONENT|title|dueDate|completed|attemptNumber|componentName
                        writer.write(" ITEM|"
                                + cri.getItemId() + "|"
                                + cri.getItemType() + "|"
                                + cri.getTitle() + "|"
                                + cri.getDueDate() + "|"
                                + cri.isCompleted() + "|"
                                + cri.getAttemptNumber() + "|"
                                + componentName);
                        writer.newLine();

                    } else if (item instanceof ConsultationRecoveryItem) {
                        ConsultationRecoveryItem ci = (ConsultationRecoveryItem) item;
                        String lecturerName = ci.getLecturerName() != null ? ci.getLecturerName() : "";
                        String location = ci.getLocation() != null ? ci.getLocation() : "";
                        // ITEM|itemId|CONSULTATION|title|dueDate|completed|lecturerName|location
                        writer.write(" ITEM|"
                                + ci.getItemId() + "|"
                                + ci.getItemType() + "|"
                                + ci.getTitle() + "|"
                                + ci.getDueDate() + "|"
                                + ci.isCompleted() + "|"
                                + lecturerName + "|"
                                + location);
                        writer.newLine();

                    } else {
                        // fallback for any other RecoveryItem implementation
                        writer.write(" ITEM|"
                                + item.getItemId() + "|"
                                + item.getItemType() + "|"
                                + item.getTitle() + "|"
                                + item.getDueDate() + "|"
                                + item.isCompleted());
                        writer.newLine();
                    }
                }
                writer.newLine();

                // MILESTONES
                writer.write("MILESTONES:");
                writer.newLine();
                for (RecoveryMilestone ms : plan.getMilestones()) {
                    // MS|milestoneId|studyWeek|task
                    writer.write(" MS|"
                            + ms.getMilestoneId() + "|"
                            + ms.getStudyWeek() + "|"
                            + ms.getTask());
                    writer.newLine();
                }
                writer.newLine();

                // PROGRESS
                writer.write("PROGRESS:");
                writer.newLine();
                for (ProgressEntry pe : plan.getProgressEntries()) {
                    // PE|progressId|date|completionPercent|notes
                    String notes = pe.getNotes();
                    if (notes == null) {
                        notes = "";
                    }
                    // (Optional) very light sanitising: avoid '|' breaking the format
                    notes = notes.replace("|", "/");

                    writer.write(" PE|"
                            + pe.getProgressId() + "|"
                            + pe.getDate() + "|"
                            + pe.getCompletionPercent() + "|"
                            + notes);
                    writer.newLine();
                }
                writer.newLine();

                // GRADES
                writer.write("GRADES:");
                writer.newLine();
                for (GradeEntry ge : plan.getGradeEntries()) {
                    // GR|gradeId|componentName|marks|gradeLetter|gradedDate
                    writer.write(" GR|"
                            + ge.getGradeId() + "|"
                            + ge.getComponentName() + "|"
                            + ge.getMarksObtained() + "|"
                            + ge.getGradeLetter() + "|"
                            + ge.getGradedDate());
                    writer.newLine();
                }
                writer.newLine();

                // end of plan
                writer.write("---");
                writer.newLine();
            }
        }
    }
}
