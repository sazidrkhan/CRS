/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package crs.reporting.service;

/**
 *
 * @author Acer
 */
import crs.reporting.model.ReportRow;
import java.util.List;

public interface StudentResultsProvider {

    List<ReportRow> getResults(String studentId, int semester, int ignoredYear);

    List<ReportRow> getResultsForYear(String studentId, int ignoredYear);

    boolean hasCompletedYear(String studentId, int ignoredYear);
}
