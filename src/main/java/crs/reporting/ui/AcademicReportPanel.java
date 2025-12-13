/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.reporting.ui;

// Sazid R Khan's code starts here.
import crs.notification.NotificationManager;
// Sazid R Khan's code ends here.

import crs.reporting.model.AcademicReport;
import crs.reporting.pdf.AcademicReportPdfExporter;
import crs.reporting.service.AcademicReportService;
import crs.reporting.service.RecoveryFeedbackProvider;
import crs.ui.AcademicOfficerDashboard;

import java.io.File;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class AcademicReportPanel extends JPanel {

    private final AcademicReportService service;
    private final RecoveryFeedbackProvider feedbackProvider;
    private final Runnable backAction;

    private JTextField txtStudentId;
    private JRadioButton rbSemester;
    private JRadioButton rbYear;
    private JComboBox<String> cmbSemester;
    private JTable tblCourses;
    private JTextArea txtFeedback;
    private JLabel lblNameValue;
    private JLabel lblProgrammeValue;
    private JLabel lblYearValue;
    private JLabel lblGpaValue;


    private AcademicReport currentReport;

    public AcademicReportPanel(AcademicReportService service,
                               RecoveryFeedbackProvider feedbackProvider,
                               Runnable backAction) {
        this.service = service;
        this.feedbackProvider = feedbackProvider;
        this.backAction = backAction;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(8, 8, 8, 8)); // nice padding around the whole panel

        // ===== Top controls =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5)); // left aligned, spacing
        top.setBorder(BorderFactory.createTitledBorder("Academic Reporting"));
        
        JLabel lblStudentId = new JLabel("Student ID:");
        lblStudentId.setFont(lblStudentId.getFont().deriveFont(Font.BOLD));
        top.add(lblStudentId);

        txtStudentId = new JTextField(8);
        top.add(txtStudentId);
        
        JLabel lblName = new JLabel("Name:");
        lblName.setFont(lblName.getFont().deriveFont(Font.BOLD));
        top.add(lblName);

        lblNameValue = new JLabel("-");
        top.add(lblNameValue);

        JLabel lblProgramme = new JLabel("Programme:");
        lblProgramme.setFont(lblProgramme.getFont().deriveFont(Font.BOLD));
        top.add(lblProgramme);

        lblProgrammeValue = new JLabel("-");
        top.add(lblProgrammeValue);

        JLabel lblYear = new JLabel("Year:");
        lblYear.setFont(lblYear.getFont().deriveFont(Font.BOLD));
        top.add(lblYear);

        lblYearValue = new JLabel("-");
        top.add(lblYearValue);

        JLabel lblCgpa = new JLabel("CGPA:");
        lblCgpa.setFont(lblCgpa.getFont().deriveFont(Font.BOLD));
        top.add(lblCgpa);

        lblGpaValue = new JLabel("-");
        top.add(lblGpaValue);


        rbSemester = new JRadioButton("Semester Report");
        rbYear = new JRadioButton("Year Report");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbSemester);
        bg.add(rbYear);
        rbSemester.setSelected(true);

        top.add(rbSemester);
        top.add(rbYear);

        JLabel lblSem = new JLabel("Semester:");
        lblSem.setFont(lblSem.getFont().deriveFont(Font.BOLD));
        top.add(lblSem);

        cmbSemester = new JComboBox<>(new String[]{"1", "2", "3"});
        top.add(cmbSemester);
        
        JButton btnGenerate = new JButton("Generate");
        JButton btnBack = new JButton("Back");
        
            
        top.add(btnGenerate);
        top.add(btnBack);

        add(top, BorderLayout.NORTH);

        // ===== Courses table =====
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Code", "Course", "Credits", "Grade", "Points"}, 0);
        tblCourses = new JTable(model);
        add(new JScrollPane(tblCourses), BorderLayout.CENTER);

        // ===== Feedback area =====
        txtFeedback = new JTextArea(6, 40);
        txtFeedback.setEditable(false);

        JPanel fbPanel = new JPanel(new BorderLayout());
        fbPanel.add(new JLabel("Recovery Plan Milestones:"), BorderLayout.NORTH);
        fbPanel.add(new JScrollPane(txtFeedback), BorderLayout.CENTER);
        add(fbPanel, BorderLayout.SOUTH);

        // ===== Bottom (PDF button) =====
        JPanel bottom = new JPanel();
        JButton btnExportPdf = new JButton("Export to PDF");
        bottom.add(btnExportPdf);
        add(bottom, BorderLayout.PAGE_END);

        // ===== Listeners =====
        rbYear.addActionListener(e -> cmbSemester.setEnabled(false));
        rbSemester.addActionListener(e -> cmbSemester.setEnabled(true));

        btnBack.addActionListener(e -> {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
        
 
        AcademicOfficerDashboard dashboard = new AcademicOfficerDashboard();
        dashboard.setVisible(true);
        });

        btnGenerate.addActionListener(e -> onGenerate());
        btnExportPdf.addActionListener(e -> onExportPdf());
    }

    private void onGenerate() {
        try {
            String studentId = txtStudentId.getText().trim();
            if (studentId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Student ID.");
                return;
            }

            boolean yearly = rbYear.isSelected();
            Integer semester = null;
            if (!yearly) {
                semester = Integer.parseInt((String) cmbSemester.getSelectedItem());
            }

            currentReport = service.generateReport(studentId, yearly, semester);
            
            var info = currentReport.getStudentInfo();
            lblNameValue.setText(info.getStudentName());
            lblProgrammeValue.setText(info.getProgramme());
            lblYearValue.setText(info.getYear());

            updateTable();
            updateFeedback(studentId);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage());
        }
    }

    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) tblCourses.getModel();
        model.setRowCount(0);

        if (currentReport == null) return;

        currentReport.getRows().forEach(r -> {
            model.addRow(new Object[]{
                    r.getCode(),          // ReportRow.getCode()
                    r.getName(),          // ReportRow.getName()
                    r.getCredits(),
                    r.getGradeLetter(),
                    r.getGradePoint()
            });
        });
    }

    private void updateFeedback(String studentId) {
        List<String> fb = feedbackProvider.getFeedbackForStudent(studentId);

        if (fb == null || fb.isEmpty()) {
            txtFeedback.setText("No recovery milestones recorded.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String line : fb) {
                sb.append(line).append("\n");
            }
            txtFeedback.setText(sb.toString());
        }
    }

    private void onExportPdf() {
    if (currentReport == null) {
        JOptionPane.showMessageDialog(this, "Generate a report before exporting.");
        return;
    }

    try {
        // 1. Get student ID from the current report
        String studentId = currentReport.getStudentInfo().getStudentId();

        // 2. Project root folder (CRS project directory)
        String baseDir = System.getProperty("user.dir");

        // 3. Create /reports folder inside the project if it doesn't exist
        File reportsDir = new File(baseDir, "reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        // 4. Build filename based on type of report
        String suffix;
        if (currentReport.isYearlyReport()) {
            suffix = "_YEAR";
        } else {
            suffix = "_SEM" + currentReport.getSemester();
        }

        File outFile = new File(
                reportsDir,
                "AcademicReport_" + studentId + suffix + ".pdf"
        );

        // 5. Get feedback lines to include in the PDF
        List<String> fb = feedbackProvider.getFeedbackForStudent(studentId);

        // 6. Export the PDF
        new AcademicReportPdfExporter().export(
                currentReport,
                fb,
                outFile.getAbsolutePath()
        );

        // Sazid R Khan's code starts here.
        new NotificationManager().sendAcademicReportNotification(studentId, outFile.getAbsolutePath());
        // Sazid R Khan's code ends here.

        // 7. Notify user where the file was saved
        JOptionPane.showMessageDialog(
                this,
                "PDF exported to:\n" + outFile.getAbsolutePath()
        );

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                "Failed to export PDF: " + ex.getMessage()
        );
         }
    }
    
}
