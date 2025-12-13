package crs.EligibilityAndEnrollmentFrames;

import crs.EligibilityAndEnrollment.EligibilityChecker;
import crs.EligibilityAndEnrollment.Student;
import crs.ui.AcademicOfficerDashboard;

import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;


public class EligibilityCheckerForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EligibilityCheckerForm.class.getName());

    public EligibilityCheckerForm() {
        initComponents();
        loadStudentData();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        tableAllStudents = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        lblSearch = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        lblTotal = new javax.swing.JLabel();
        lblEligible = new javax.swing.JLabel();
        lblIneligible = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblTitle.setText("All Students Overview");

        tableAllStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "StudenID", "First Name", "Last Name", "Major", "Courses", "Year", "CGPA", "Failed", "Status"
            }
        ));
        scrollPane.setViewportView(tableAllStudents);

        btnRefresh.setText("Refresh Data");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnBack.setText("Back to Menu");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        lblSearch.setText("Search:");

        txtSearch.setText("Search by name or ID: ");

        lblTotal.setText("Total Students: ");

        lblEligible.setText("Eligable");

        lblIneligible.setText("Ineligible: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(311, 311, 311)
                        .addComponent(lblTitle))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(505, 505, 505)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblSearch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTotal)
                                .addGap(71, 71, 71)
                                .addComponent(lblEligible)
                                .addGap(64, 64, 64)
                                .addComponent(lblIneligible))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSearch)
                    .addComponent(lblSearch)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotal)
                    .addComponent(lblEligible)
                    .addComponent(lblIneligible))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
private void loadStudentData() {
    DefaultTableModel model = (DefaultTableModel) tableAllStudents.getModel();
    model.setRowCount(0);
    
    int totalStudents = 0;
    int eligibleCount = 0;
    int ineligibleCount = 0;
    
    try {
        // Use EligibilityChecker to get student data with calculated values
            EligibilityChecker checker = new EligibilityChecker();
        
        for (Student student : checker.getAllStudents()) {
            String studentID = student.getStudentID();
            String firstName = student.getFirstName();
            String lastName = student.getLastName();
            String major = student.getMajor();
            String courses = student.getCourses();
            String year = student.getYear();
            
            // Calculate CGPA and failed courses
            double cgpa = checker.calculateCGPA(studentID);
            int failedCourses = checker.countFailedCourses(studentID);
            boolean eligible = checker.isEligible(studentID);
            String status = eligible ? "Eligible" : "Not Eligible";
            
            

            
            model.addRow(new Object[]{
                studentID, 
                firstName, 
                lastName, 
                major, 
                courses,
                year, 
                String.format("%.2f", cgpa), 
                failedCourses, 
                status
            });
            
            totalStudents++;
            if (eligible) {
                eligibleCount++;
            } else {
                ineligibleCount++;
            }
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error loading student data: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        logger.log(java.util.logging.Level.SEVERE, "Error loading student data", e);
    }
    
    lblTotal.setText("Total Students: " + totalStudents);
    lblEligible.setText("Eligible: " + eligibleCount);
    lblIneligible.setText("Ineligible: " + ineligibleCount);
}
    
private void searchStudents(String searchText) {
    DefaultTableModel model = (DefaultTableModel) tableAllStudents.getModel();
    model.setRowCount(0);
    
    int totalStudents = 0;
    int eligibleCount = 0;
    int ineligibleCount = 0;
    
    try {
        EligibilityChecker checker = new EligibilityChecker();
        
        for (Student student : checker.getAllStudents()) {
            String studentID = student.getStudentID();
            String firstName = student.getFirstName();
            String lastName = student.getLastName();
            
            // Check if matches search
            if (studentID.toLowerCase().contains(searchText.toLowerCase()) ||
                firstName.toLowerCase().contains(searchText.toLowerCase()) ||
                lastName.toLowerCase().contains(searchText.toLowerCase())) {
                
                String major = student.getMajor();
                String year = student.getYear();
                double cgpa = checker.calculateCGPA(studentID);
                int failedCourses = checker.countFailedCourses(studentID);
                boolean eligible = checker.isEligible(studentID);
                String status = eligible ? "Eligible" : "Not Eligible";
                
                model.addRow(new Object[]{
                    studentID, 
                    firstName, 
                    lastName, 
                    major, 
                    year, 
                    String.format("%.2f", cgpa), 
                    failedCourses, 
                    status
                });
                
                totalStudents++;
                if (eligible) {
                    eligibleCount++;
                } else {
                    ineligibleCount++;
                }
            }
        }
        
        if (totalStudents == 0) {
            JOptionPane.showMessageDialog(this,
                "No students found matching: \"" + searchText + "\"",
                "Search Result",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error searching: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        logger.log(java.util.logging.Level.SEVERE, "Error searching student data", e);
    }
    
    lblTotal.setText("Total Students: " + totalStudents);
    lblEligible.setText("Eligible: " + eligibleCount);
    lblIneligible.setText("Ineligible: " + ineligibleCount);
}
    
    
    
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        txtSearch.setText("");
        loadStudentData();
        JOptionPane.showMessageDialog(this, "Data refreshed successfully!");

    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
      this.dispose();
      AcademicOfficerDashboard dashboard = new AcademicOfficerDashboard();
      dashboard.setVisible(true);
      
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
    String searchText = txtSearch.getText().trim();
    
    if (searchText.isEmpty() || searchText.equals("Search by name or ID: ")) {
        JOptionPane.showMessageDialog(this,
            "Please enter a search term!",
            "Search",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    searchStudents(searchText);    }//GEN-LAST:event_btnSearchActionPerformed

 
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new EligibilityCheckerForm().setVisible(true));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel lblEligible;
    private javax.swing.JLabel lblIneligible;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable tableAllStudents;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}


