/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.reporting.service.impl;
/**
 *
 * @author Sazid R Khan
 */

import crs.EligibilityAndEnrollment.EligibilityChecker;
import crs.reporting.model.StudentInfo;
import crs.reporting.service.StudentInfoProvider;

public class StudentInfoProviderImpl implements StudentInfoProvider {

    private final EligibilityChecker checker;

    public StudentInfoProviderImpl(EligibilityChecker checker) {
        this.checker = checker;
    }

    @Override
    public StudentInfo getStudentInfo(String studentId) {
        var s = checker.getStudentById(studentId);
        if (s == null) return null;

        return new StudentInfo(
        s.getStudentID(),
        s.getFullName(),
        s.getMajor(),     
        s.getYear() 
        );
    }
}

