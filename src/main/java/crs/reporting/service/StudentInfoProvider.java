/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package crs.reporting.service;

/**
 *
 * @author Acer
 */
import crs.reporting.model.StudentInfo;

public interface StudentInfoProvider {
    StudentInfo getStudentInfo(String studentId);
}

