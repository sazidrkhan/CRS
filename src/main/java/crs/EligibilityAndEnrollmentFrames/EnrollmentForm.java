package crs.EligibilityAndEnrollmentFrames;

import crs.EligibilityAndEnrollment.Student;
import crs.EligibilityAndEnrollment.Course;
import crs.EligibilityAndEnrollment.EnrolmentManager;
import crs.EligibilityAndEnrollment.EligibilityChecker;
import crs.EligibilityAndEnrollment.EligibilityChecker.StudentCourseRecord;
import crs.ui.AcademicOfficerDashboard;

// Sazid R Khan's import starts here
import crs.notification.NotificationManager;
// Sazid R Khan's import ends here

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class EnrollmentForm extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(EnrollmentForm.class.getName());

    private final EligibilityChecker eligibilityChecker;
    private final EnrolmentManager enrolmentManager;
    private Student currentStudent;

    public EnrollmentForm() {
        // Initialize OOP objects
        eligibilityChecker = new EligibilityChecker();
        enrolmentManager = new EnrolmentManager();

        initComponents();

        btnCheckAndEnrol.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCGPA = new javax.swing.JLabel();
        lblFailedCourses = new javax.swing.JLabel();
        lblEligibilityStatus = new javax.swing.JLabel();
        lblResult = new javax.swing.JLabel();
        scrollDetails = new javax.swing.JScrollPane();
        txtStudentDetails = new javax.swing.JTextArea();
        btnCheckAndEnrol = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        lblSelectStudent = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        lblStudentInfo = new javax.swing.JLabel();
        txtSearchStudent = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblCGPA.setText("CGPA: ");

        lblFailedCourses.setText("Failed Courses: ");

        lblEligibilityStatus.setText("Eligibility Status : ");

        lblResult.setText("(Empty,  for showing success /error message");

        txtStudentDetails.setColumns(20);
        txtStudentDetails.setRows(5);
        scrollDetails.setViewportView(txtStudentDetails);

        btnCheckAndEnrol.setText("Check Eligibility & Enrol");
        btnCheckAndEnrol.setActionCommand("btnCheckAndEnrol");
        btnCheckAndEnrol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckAndEnrolActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblTitle.setText("Student Enrolment");

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        lblSelectStudent.setText("Select Student");

        btnBack.setText("Back to Menu");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblStudentInfo.setText("Student Information & Course Performance");

        txtSearchStudent.setText("Search by name or ID: ");
        txtSearchStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchStudentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                .createSequentialGroup()
                                                .addComponent(btnCheckAndEnrol, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(36, 36, 36)
                                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 124,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87,
                                                        Short.MAX_VALUE)
                                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 248,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(86, 86, 86))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblStudentInfo)
                                                        .addComponent(lblResult)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(lblSelectStudent)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(txtSearchStudent,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 355,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(scrollDetails,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 709,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        false)
                                                                .addComponent(lblEligibilityStatus,
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 141,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(lblFailedCourses,
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE))
                                                        .addComponent(lblCGPA, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        Short.MAX_VALUE))))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(280, 280, 280)
                                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 271,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 30,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblSelectStudent)
                                        .addComponent(txtSearchStudent, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
                                .addComponent(lblStudentInfo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrollDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 381,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblCGPA)
                                .addGap(12, 12, 12)
                                .addComponent(lblFailedCourses)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblEligibilityStatus)
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCheckAndEnrol, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnBack))
                                .addGap(18, 18, 18)
                                .addComponent(lblResult, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public ArrayList<Student> getAllStudents() {

        ArrayList<Student> dummyStudents = new ArrayList<>();

        return dummyStudents;
    }

    private void displayStudentDetails(Student student) {
        String studentID = student.getStudentID();

        StringBuilder details = new StringBuilder();
        details.append("=== STUDENT INFORMATION ===\n");
        details.append("Student ID: ").append(studentID).append("\n");
        details.append("Name: ").append(student.getFullName()).append("\n");
        details.append("Major: ").append(student.getMajor()).append("\n");
        details.append("Year: ").append(student.getYear()).append("\n");
        details.append("Email: ").append(student.getEmail()).append("\n\n");

        details.append("=== COURSE PERFORMANCE ===\n");

        ArrayList<StudentCourseRecord> records = eligibilityChecker.getStudentRecords(studentID);
        ArrayList<Course> allCourses = eligibilityChecker.getAllCourses();

        double totalGradePoints = 0;
        int totalCredits = 0;
        int failedCourses = 0;

        for (StudentCourseRecord record : records) {
            Course course = getCourse(allCourses, record.getCourseId());
            if (course != null) {
                double finalScore = record.calculateFinalScore(course);
                double gradePoint = convertScoreToGradePoint(finalScore);
                String letterGrade = getLetterGrade(finalScore);

                details.append(String.format("Course: %s - %s\n",
                        course.getCourseID(), course.getCourseName()));
                details.append(String.format("  Credits: %d | Final Score: %.2f | Grade: %s (%.2f)\n",
                        course.getCredits(), finalScore, letterGrade, gradePoint));

                totalGradePoints += gradePoint * course.getCredits();
                totalCredits += course.getCredits();

                if (finalScore < 50) {
                    failedCourses++;
                }
            }
        }

        double cgpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;

        details.append("\n=== OVERALL PERFORMANCE ===\n");
        details.append(String.format("Total Credits: %d\n", totalCredits));
        details.append(String.format("CGPA: %.2f\n", cgpa));
        details.append(String.format("Failed Courses: %d\n", failedCourses));

        txtStudentDetails.setText(details.toString());

        // --- START OF NEW/MODIFIED COLOR LOGIC ---
        lblCGPA.setText(String.format("CGPA: %.2f", cgpa));

        if (cgpa < 2.0) {
            // RED: Ineligible (Below 2.0)
            lblCGPA.setForeground(new Color(204, 0, 0));
        } else if (cgpa >= 2.0 && cgpa <= 2.5) {
            // YELLOW/ORANGE: Barely Passing/Warning (2.0 to 2.5)
            lblCGPA.setForeground(new Color(255, 153, 0));
        } else {
            // GREEN: Good Standing (Above 2.5)
            lblCGPA.setForeground(new Color(0, 153, 0));
        }
        // --- END OF NEW/MODIFIED COLOR LOGIC ---

        lblFailedCourses.setText("Failed Courses: " + failedCourses);
        lblFailedCourses.setForeground(failedCourses <= 3 ? new Color(0, 153, 0) : new Color(204, 0, 0));

        boolean isEligible = eligibilityChecker.isEligible(studentID);
        String status = isEligible ? "Eligible" : "Ineligible";
        lblEligibilityStatus.setText("Eligibility Status: " + status);
        lblEligibilityStatus.setForeground(isEligible ? new Color(0, 153, 0) : new Color(204, 0, 0));

        lblResult.setText("");
    }

    private Course getCourse(ArrayList<Course> courses, String courseId) {
        for (Course course : courses) {
            if (course.getCourseID().equals(courseId)) {
                return course;
            }
        }
        return null;
    }

    private double convertScoreToGradePoint(double score) {
        if (score >= 90)
            return 4.0;
        else if (score >= 85)
            return 3.7;
        else if (score >= 80)
            return 3.3;
        else if (score >= 75)
            return 3.0;
        else if (score >= 70)
            return 2.7;
        else if (score >= 65)
            return 2.3;
        else if (score >= 60)
            return 2.0;
        else if (score >= 55)
            return 1.7;
        else if (score >= 50)
            return 1.0;
        else
            return 0.0;
    }

    private String getLetterGrade(double score) {
        if (score >= 90)
            return "A";
        else if (score >= 80)
            return "B";
        else if (score >= 70)
            return "C";
        else if (score >= 60)
            return "D";
        else if (score >= 50)
            return "E";
        else
            return "F";
    }

    private boolean enrollStudent(Student student, double cgpa, int failedCourses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("enrollments.txt", true))) {
            String enrollmentRecord = student.getStudentID() + "," +
                    student.getFirstName() + "," +
                    student.getLastName() + "," +
                    student.getMajor() + "," +
                    student.getYear() + "," +
                    String.format("%.2f", cgpa) + "," +
                    failedCourses + "," +
                    "Enrolled\n";
            writer.write(enrollmentRecord);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving enrollment: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, "Error enrolling student", e);
            return false;
        }
    }

    private void searchAndDisplayStudent() {
        String searchText = txtSearchStudent.getText().trim();
    
    // Clear placeholder text - BETTER CHECK
    if (searchText.equals("Search by name or ID: ") || searchText.startsWith("Search by name or ID:")) {
        searchText = searchText.replace("Search by name or ID:", "").trim();
    }
    
    if (searchText.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Please enter a Student ID or name to search!",
            "Search",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    Student foundStudent = null;
    int foundIndex = -1;
    
    // Search through all students
    ArrayList<Student> students = eligibilityChecker.getAllStudents();
    for (int i = 0; i < students.size(); i++) {
        Student student = students.get(i);
        
        // Search by ID or Name (case-insensitive)
        if (student.getStudentID().equalsIgnoreCase(searchText) ||
            student.getFirstName().toLowerCase().contains(searchText.toLowerCase()) ||
            student.getLastName().toLowerCase().contains(searchText.toLowerCase()) ||
            student.getFullName().toLowerCase().contains(searchText.toLowerCase())) {
            
            foundStudent = student;
            foundIndex = i + 1;
            break;
        }
    }
    
    if (foundStudent == null) {
        JOptionPane.showMessageDialog(this,
            "No student found matching: \"" + searchText + "\"",
            "Search Result",
            JOptionPane.INFORMATION_MESSAGE);
        btnClearActionPerformed(null);
        currentStudent = null;
        return;
    }
    currentStudent = foundStudent; 
    
    displayStudentDetails(foundStudent);
    btnCheckAndEnrol.setEnabled(true); 

    displayStudentDetails(foundStudent);
    btnCheckAndEnrol.setEnabled(true);

    lblResult.setText("✓ Found: " + foundStudent.getFullName());
    lblResult.setForeground(new Color(0, 153, 0));
    }

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnClearActionPerformed
        txtSearchStudent.setText("Search by name or ID: ");
        txtSearchStudent.setForeground(Color.GRAY);

        // CLEAR DETAILS
        txtStudentDetails.setText("");
        lblCGPA.setText("CGPA: --");
        lblCGPA.setForeground(Color.BLACK);
        lblFailedCourses.setText("Failed Courses: --");
        lblFailedCourses.setForeground(Color.BLACK);
        lblEligibilityStatus.setText("Eligibility Status: --");
        lblEligibilityStatus.setForeground(Color.BLACK);
        lblResult.setText("");

        // DISABLE BUTTON UNTIL SEARCH IS DONE
        btnCheckAndEnrol.setEnabled(false);
    }// GEN-LAST:event_btnClearActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnBackActionPerformed
        this.dispose();
        AcademicOfficerDashboard dashboard = new AcademicOfficerDashboard();
        dashboard.setVisible(true);
    }// GEN-LAST:event_btnBackActionPerformed

        private void btnCheckAndEnrolActionPerformed(java.awt.event.ActionEvent evt) {
        
            
        Student student = currentStudent; 
        if (student == null) {
            lblResult.setText("Please search for a student first!");
            lblResult.setForeground(new Color(204, 0, 0));
            return;
        }

        
        String studentID = student.getStudentID();
        double cgpa = eligibilityChecker.calculateCGPA(studentID);
        int failedCourses = eligibilityChecker.countFailedCourses(studentID);
        boolean isEligible = eligibilityChecker.isEligible(studentID);
        
        if (isEligible) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Student is ELIGIBLE for enrollment.\n\n" +
                "Student: " + student.getFullName() + "\n" +   
                "CGPA: " + String.format("%.2f", cgpa) + "\n" +
                "Failed Courses: " + failedCourses + "\n\n" +
                "Do you want to enroll this student?", 
                "Confirm Enrollment", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = enrollStudent(student, cgpa, failedCourses);
                if (success) {

                    // Sazid R Khan's code starts here
                    new NotificationManager().sendEnrollmentNotification(studentID, "Enrollment completed");
                    // Sazid R Khan's code ends here

                    lblResult.setText("✓ Student successfully enrolled!");
                    lblResult.setForeground(new Color(0, 153, 0));
                    JOptionPane.showMessageDialog(this, 
                        student.getFullName() + " has been enrolled successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    lblResult.setText("✗ Error enrolling student. Please try again.");
                    lblResult.setForeground(new Color(204, 0, 0));
                }
            }
        } else {
            String reason = "";
            if (cgpa < 2.0 && failedCourses > 3) {
                reason = "CGPA is below 2.0 AND has more than 3 failed courses";
            } else if (cgpa < 2.0) {
                reason = "CGPA is below 2.0";
            } else if (failedCourses > 3) {
                reason = "Has more than 3 failed courses";
            }
            
            lblResult.setText("✗ Student is NOT eligible for enrollment!");
            lblResult.setForeground(new Color(204, 0, 0));
            
            JOptionPane.showMessageDialog(this, 
                "Student is INELIGIBLE for enrollment.\n\n" +
                "Student: " + student.getFullName() + "\n" +
                "CGPA: " + String.format("%.2f", cgpa) + " (Required: ≥ 2.0)\n" +
                "Failed Courses: " + failedCourses + " (Maximum: 2)\n\n" +
                "Reason: " + reason, 
                "Ineligible", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void txtSearchStudentActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtSearchStudentActionPerformed
        searchAndDisplayStudent();
    }// GEN-LAST:event_txtSearchStudentActionPerformed

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new EnrollmentForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCheckAndEnrol;
    private javax.swing.JButton btnClear;
    private javax.swing.JLabel lblCGPA;
    private javax.swing.JLabel lblEligibilityStatus;
    private javax.swing.JLabel lblFailedCourses;
    private javax.swing.JLabel lblResult;
    private javax.swing.JLabel lblSelectStudent;
    private javax.swing.JLabel lblStudentInfo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane scrollDetails;
    private javax.swing.JTextField txtSearchStudent;
    private javax.swing.JTextArea txtStudentDetails;
    // End of variables declaration//GEN-END:variables
}
