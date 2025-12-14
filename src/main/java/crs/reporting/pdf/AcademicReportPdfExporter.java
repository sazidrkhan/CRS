/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.reporting.pdf;

/**
 *
 * @author Sazid R Khan
 */

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import crs.reporting.model.AcademicReport;
import crs.reporting.model.ReportRow;

import java.io.FileOutputStream;
import java.util.List;

public class AcademicReportPdfExporter {

    public void export(AcademicReport report,
                       List<String> milestoneLines,
                       String filePath) throws Exception {

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(filePath));
        doc.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);

        // Header
        doc.add(new Paragraph("ACADEMIC REPORT", titleFont));

        doc.add(new Paragraph("Name: " + report.getStudentInfo().getStudentName()));
        doc.add(new Paragraph("Student ID: " + report.getStudentInfo().getStudentId()));
        doc.add(new Paragraph("Programme: " + report.getStudentInfo().getProgramme()));
        doc.add(new Paragraph("Year: " + report.getStudentInfo().getYear()));
        doc.add(new Paragraph(" "));


        if (report.isYearlyReport()) {
            doc.add(new Paragraph("Report Type: Yearly Academic Summary"));
        } else {
            doc.add(new Paragraph("Report Type: Semester " + report.getSemester()));
        }
        doc.add(new Paragraph(" "));

        // Courses table
        PdfPTable table = new PdfPTable(5);
        table.addCell("Code");
        table.addCell("Course");
        table.addCell("Credits");
        table.addCell("Grade");
        table.addCell("Points");

        for (ReportRow r : report.getRows()) {
            table.addCell(r.getCode());                        // uses ReportRow.getCode()
            table.addCell(r.getName());                        // uses ReportRow.getName()
            table.addCell(String.valueOf(r.getCredits()));
            table.addCell(r.getGradeLetter());
            table.addCell(String.valueOf(r.getGradePoint()));
        }

        doc.add(table);
        doc.add(new Paragraph("Overall GPA: " + String.format("%.2f", report.getGpa())));

        // Milestones
        doc.add(new Paragraph("Recovery Plan Milestones:", titleFont));
        if (milestoneLines == null || milestoneLines.isEmpty()) {
            doc.add(new Paragraph("No recovery milestones recorded."));
        } else {
            for (String line : milestoneLines) {
                doc.add(new Paragraph("- " + line));
            }
        }

        doc.close();
    }
}
