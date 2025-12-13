/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package crs.EligibilityAndEnrollmentFrames;

import crs.EligibilityAndEnrollment.Student;
import crs.ui.LOGIN_FRAME;
import crs.EligibilityAndEnrollment.Instructor;
import crs.EligibilityAndEnrollment.Course;
import crs.EligibilityAndEnrollment.EligibilityChecker;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author abdullah
 */
public class GradeAssignmentsAndExamsForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GradeAssignmentsAndExamsForm.class.getName());
    
    // Add these instance variables
    private EligibilityChecker checker;
    private Instructor currentInstructor;
    
    /**
     * Creates new form GradeAssignmentsAndExams
     */
    public GradeAssignmentsAndExamsForm() {
     initComponents();
    
    // Initialize the checker and instructor
    checker = new EligibilityChecker();
    currentInstructor = new Instructor("INS001", "Dr. John", "Smith", "Computer Science", "AI");
    
    // Load data after components are initialized
    loadCourses();
    // 🛑 CHANGE THIS LINE:
    // loadPendingAssignments(); 
    loadAllAssignmentRecords(); 
    setupPlaceholders();
}
    
    /**
     * Setup placeholder text for text fields
     */
    private void setupPlaceholders() {
        // Clear default text and set proper placeholders
        jTextField1.setText("");
        txtExamScore.setText("");
        txtAssignmentScore.setText("");
        
        jTextField1.setForeground(Color.GRAY);
        txtExamScore.setForeground(Color.GRAY);
        txtAssignmentScore.setForeground(Color.GRAY);
    }
    
    /**
     * Load all courses into the combo box
     */
    private void loadCourses() {
        SelectCourses.removeAllItems();
        SelectCourses.addItem("-- Select Course --");
        
        for (Course course : checker.getAllCourses()) {
            SelectCourses.addItem(course.getCourseID() + " - " + course.getCourseName());
        }
    }
    
    /**
     * Load existing grades into the table
     */
private void loadPendingAssignments() { 
    DefaultTableModel model = (DefaultTableModel) ShowStudentDetails.getModel();
    ArrayList<Instructor.StudentCourseAssignment> pendingRecords = currentInstructor.getPendingGrades();
    
    for (Instructor.StudentCourseAssignment record : pendingRecords) {
        Student student = checker.getStudentById(record.getStudentId());
        Course course = getCourse(record.getCourseId());
        
        if (student != null && course != null) {
            model.addRow(new Object[]{
                student.getStudentID(),
                student.getFullName(),
                student.getMajor(),
                course.getCourseID(), 
                course.getCourseName(), 
                "PENDING", 
                "PENDING", 
                "N/A",
                "N/A"
            });
        }
    }
}
    
private void loadGradedRecords(DefaultTableModel model) {
    // Iterate through all students to find their graded records
    for (Student student : checker.getAllStudents()) {
        
        // Get records from the EligibilityChecker (which reads GradedStudentRecords.txt)
        ArrayList<EligibilityChecker.StudentCourseRecord> records = checker.getStudentRecords(student.getStudentID());
        
        for (EligibilityChecker.StudentCourseRecord record : records) {
            Course course = getCourse(record.getCourseId());
            if (course != null) { 
                
                // Calculate final score and grade for display
                double finalScore = record.calculateFinalScore(course);
                String grade = convertScoreToGrade(finalScore);
                
                model.addRow(new Object[]{
                    student.getStudentID(),
                    student.getFullName(),
                    student.getMajor(),
                    course.getCourseID(),
                    course.getCourseName(),
                    String.format("%.1f", record.getExamScore()), // Actual Exam Score
                    String.format("%.1f", record.getAssignmentScore()), // Actual Assignment Score
                    String.format("%.1f", finalScore),
                    grade
                });
            }
        }
    }
}


private void loadAllAssignmentRecords() {
    DefaultTableModel model = (DefaultTableModel) ShowStudentDetails.getModel();
    model.setRowCount(0); // Clear existing rows

    // 1. Load Pending Assignments (from notGradedStudentsRecords.txt)
    loadPendingAssignments();
    
    // 2. Load Graded Assignments (from GradedStudentRecords.txt)
    loadGradedRecords(model);
}

    /**
     * Get course by course ID
     */
    private Course getCourse(String courseId) {
        for (Course course : checker.getAllCourses()) {
            if (course.getCourseID().equals(courseId)) {
                return course;
            }
        }
        return null;
    }
    
    /**
     * Get student by student ID or name
     */
    private Student getStudent(String searchText) {
        searchText = searchText.trim();
        
        for (Student student : checker.getAllStudents()) {
            // Check if matches ID or name
            if (student.getStudentID().equalsIgnoreCase(searchText) ||
                student.getFullName().toLowerCase().contains(searchText.toLowerCase())) {
                return student;
            }
        }
        return null;
    }
    
    /**
     * Convert numeric score to letter grade
     */
    private String convertScoreToGrade(double score) {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else if (score >= 50) return "E";
        else return "F";
    }
    
    /**
     * Clear the form
     */
    private void clearForm() {
        jTextField1.setText("");
        SelectCourses.setSelectedIndex(0);
        txtExamScore.setText("");
        txtAssignmentScore.setText("");
        jTextField1.setForeground(Color.BLACK);
        txtExamScore.setForeground(Color.BLACK);
        txtAssignmentScore.setForeground(Color.BLACK);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnAssignGrades = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnback = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ShowStudentDetails = new javax.swing.JTable();
        SelectCourses = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        txtExamScore = new javax.swing.JTextField();
        txtAssignmentScore = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Assign Grades");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel2.setText("Grade Assignment System");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Select Course: ");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Assignment Score: ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Select Student: ");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Exam Score: ");

        btnAssignGrades.setText("Assign Grades");
        btnAssignGrades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignGradesActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnback.setText("LOG OUT");
        btnback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbackActionPerformed(evt);
            }
        });

        ShowStudentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "StudentID", "Student Name", "Major", "CourseID", "Course Name", "Exam", "Assignment", "Final", "Grade"
            }
        ));
        jScrollPane1.setViewportView(ShowStudentDetails);

        SelectCourses.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        SelectCourses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectCoursesActionPerformed(evt);
            }
        });

        jTextField1.setText("Student name or ID: ");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        txtExamScore.setText("Enter exam score");
        txtExamScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtExamScoreActionPerformed(evt);
            }
        });

        txtAssignmentScore.setText("Enter assignment score ");
        txtAssignmentScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAssignmentScoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(283, 283, 283))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 997, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(221, 221, 221)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5)))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(368, 368, 368)
                            .addComponent(btnAssignGrades)
                            .addGap(18, 18, 18)
                            .addComponent(btnClear)
                            .addGap(18, 18, 18)
                            .addComponent(btnback, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(401, 401, 401)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(SelectCourses, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtExamScore)
                                .addComponent(txtAssignmentScore, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(479, 479, 479)
                        .addComponent(jLabel1)))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSeparator1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSeparator2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SelectCourses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtExamScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtAssignmentScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAssignGrades)
                    .addComponent(btnClear)
                    .addComponent(btnback))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAssignGradesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssignGradesActionPerformed
        String studentInput = jTextField1.getText().trim();
        if (studentInput.isEmpty() || studentInput.equalsIgnoreCase("Student name or ID:")) {
            JOptionPane.showMessageDialog(this, "Please enter a student ID or name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Find student
        Student student = getStudent(studentInput);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found! Please check the ID or name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate course selection
        if (SelectCourses.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a course!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get selected course
        // The index logic is correct: 0 is the placeholder, so actual courses start at index 1.
        Course course = checker.getAllCourses().get(SelectCourses.getSelectedIndex() - 1);

        // Validate scores
        try {
            String examText = txtExamScore.getText().trim();
            String assignmentText = txtAssignmentScore.getText().trim();

            if (examText.isEmpty() || assignmentText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both exam and assignment scores!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double examScore = Double.parseDouble(examText);
            double assignmentScore = Double.parseDouble(assignmentText);

            // Fetch course weights from the Course object
            int examWeight = course.getExamWeight();
            int assignmentWeight = course.getAssignmentWeight();


            if (examScore < 0 || examScore > examWeight || assignmentScore < 0 || assignmentScore > assignmentWeight) {

                if (examScore < 0 || examScore > 100 || assignmentScore < 0 || assignmentScore > 100) {
                    JOptionPane.showMessageDialog(this, "Scores must be between 0 and 100!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }


            // Assign grades
            // This method call suggests that `assignGrades` handles saving the data.
            boolean success = currentInstructor.assignGrades(
                student.getStudentID(), 
                course.getCourseID(), 
                examScore, 
                assignmentScore, 
                course
            );

            if (success) {

                double finalScore = (examScore * course.getExamWeight() / 100.0) + 
                                    (assignmentScore * course.getAssignmentWeight() / 100.0);
                String grade = convertScoreToGrade(finalScore);

                JOptionPane.showMessageDialog(this,
                    "Grades assigned successfully!\n\n" +
                    "Student: " + student.getFullName() + " (" + student.getStudentID() + ")\n" +
                    "Course: " + course.getCourseName() + " (" + course.getCourseID() + ")\n" +
                    "Exam Score (Raw): " + examScore + "\n" +
                    "Assignment Score (Raw): " + assignmentScore + "\n" +
                    "Final Score: " + String.format("%.1f", finalScore) + "\n" +
                    "Grade: " + grade,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

                // Reload data and clear form
                checker = new EligibilityChecker(); // Reload data from files
                loadAllAssignmentRecords();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error assigning grades! This usually happens if the student is not registered for the selected course.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric scores!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAssignGradesActionPerformed

    private void txtExamScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtExamScoreActionPerformed
        txtAssignmentScore.requestFocusInWindow();
    }//GEN-LAST:event_txtExamScoreActionPerformed

    private void txtAssignmentScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAssignmentScoreActionPerformed
        String studentInput = jTextField1.getText().trim();
        if (studentInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student ID or name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Find student
        Student student = getStudent(studentInput);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found! Please check the ID or name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate course selection
        if (SelectCourses.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a course!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get selected course
        Course course = checker.getAllCourses().get(SelectCourses.getSelectedIndex() - 1);
        
        // Validate scores
        try {
            String examText = txtExamScore.getText().trim();
            String assignmentText = txtAssignmentScore.getText().trim();
            
            if (examText.isEmpty() || assignmentText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both exam and assignment scores!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double examScore = Double.parseDouble(examText);
            double assignmentScore = Double.parseDouble(assignmentText);
            
            if (examScore < 0 || examScore > 100 || assignmentScore < 0 || assignmentScore > 100) {
                JOptionPane.showMessageDialog(this, "Scores must be between 0 and 100!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Assign grades
            boolean success = currentInstructor.assignGrades(
                student.getStudentID(), 
                course.getCourseID(), 
                examScore, 
                assignmentScore, 
                course
            );
            
            if (success) {
                // Calculate final score for display
                double finalScore = (examScore * course.getExamWeight() / 100.0) + 
                                  (assignmentScore * course.getAssignmentWeight() / 100.0);
                String grade = convertScoreToGrade(finalScore);
                
                JOptionPane.showMessageDialog(this,
                    "Grades assigned successfully!\n\n" +
                    "Student: " + student.getFullName() + " (" + student.getStudentID() + ")\n" +
                    "Course: " + course.getCourseName() + " (" + course.getCourseID() + ")\n" +
                    "Exam: " + examScore + "\n" +
                    "Assignment: " + assignmentScore + "\n" +
                    "Final Score: " + String.format("%.1f", finalScore) + "\n" +
                    "Grade: " + grade,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Reload data and clear form
                checker = new EligibilityChecker(); // Reload data from files
                loadPendingAssignments();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error assigning grades! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric scores!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txtAssignmentScoreActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbackActionPerformed
        dispose();  
        LOGIN_FRAME login = new LOGIN_FRAME();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }//GEN-LAST:event_btnbackActionPerformed

    private void SelectCoursesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectCoursesActionPerformed
        int index = SelectCourses.getSelectedIndex();
        if (index > 0) {
            Course course = checker.getAllCourses().get(index - 1);
            // Optional: Display course weights or other info
            System.out.println("Selected: " + course.getCourseName() + 
                             " (Exam: " + course.getExamWeight() + "%, Assignment: " + course.getAssignmentWeight() + "%)");
        }
    }//GEN-LAST:event_SelectCoursesActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    /**
     * @param args the command line arguments
     */
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
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new GradeAssignmentsAndExamsForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> SelectCourses;
    private javax.swing.JTable ShowStudentDetails;
    private javax.swing.JButton btnAssignGrades;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnback;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField txtAssignmentScore;
    private javax.swing.JTextField txtExamScore;
    // End of variables declaration//GEN-END:variables

}
