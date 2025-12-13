/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package crs.reporting.service;
import java.util.List;
/**
 *
 * @author Acer
 */
public interface RecoveryFeedbackProvider {
    List<String> getFeedbackForStudent(String studentId);
}