/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.reporting.service.impl;

/**
 *
 * @author Sazid R Khan
 */
import crs.reporting.service.RecoveryFeedbackProvider;
import crs.recovery.io.RecoveryPlanRepository;
import crs.recovery.model.RecoveryMilestone;
import crs.recovery.model.RecoveryPlan;

import java.util.ArrayList;
import java.util.List;

public class RecoveryFeedbackProviderImpl implements RecoveryFeedbackProvider {

    private final RecoveryPlanRepository repo;

    public RecoveryFeedbackProviderImpl(RecoveryPlanRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<String> getFeedbackForStudent(String studentId) {
        List<String> out = new ArrayList<>();

        try {
            List<RecoveryPlan> plans = repo.loadAll();

            for (RecoveryPlan plan : plans) {

                if (plan.getStudent() == null
                        || plan.getStudent().getStudentId() == null
                        || !plan.getStudent().getStudentId().equals(studentId)) {
                    continue;
                }

                String courseCode = (plan.getCourse() != null)
                        ? plan.getCourse().getCourseCode()
                        : "(Unknown Course)";

                for (RecoveryMilestone ms : plan.getMilestones()) {
                    String week = ms.getStudyWeek() != null ? ms.getStudyWeek() : "(No week)";
                    String task = ms.getTask() != null ? ms.getTask() : "(No task)";

                    String line = String.format(
                            "%s | %s | %s",
                            courseCode,
                            week,
                            task
                    );

                    out.add(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }
}

