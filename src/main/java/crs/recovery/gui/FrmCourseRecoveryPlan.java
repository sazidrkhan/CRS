/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package crs.recovery.gui;

// Sazid R Khan's code starts here
import crs.notification.NotificationManager;
// Sazid R Khan's code ends here

import crs.recovery.io.FileRecoveryPlanRepository;
import crs.recovery.io.RecoveryPlanRepository;
import crs.recovery.model.*;
import crs.recovery.service.*;
import crs.shared.Course;
import crs.shared.CourseComponent;
import crs.shared.Student;
import crs.ui.AcademicOfficerDashboard;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrmCourseRecoveryPlan extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(FrmCourseRecoveryPlan.class.getName());

    private RecoveryPlanManager manager;
    private CourseRecoveryPolicy policy;

    private final crs.EligibilityAndEnrollment.EligibilityChecker part3Checker = new crs.EligibilityAndEnrollment.EligibilityChecker();

    private String currentPlanId; // which plan is loaded now
    // actual object for the loaded plan
    private RecoveryPlan currentPlan;

    /**
     * Creates new form FrmCourseRecoveryPlan
     */
    public FrmCourseRecoveryPlan() {
        initComponents();
        initRecoveryModule();
        tblItems.getColumnModel().getColumn(6).setPreferredWidth(130);
        tblItems.getColumnModel().getColumn(7).setPreferredWidth(100);
        tblItems.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblItems.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblItems.getColumnModel().getColumn(1).setPreferredWidth(120);
        // Attempt is auto-managed by backend; user should not change it.
        spnAttemptNumber.setEnabled(false);
    }

    private void initRecoveryModule() {
        // Policy
        policy = new CourseRecoveryPolicy();

        // Repository using your text file
        RecoveryPlanRepository repo = new FileRecoveryPlanRepository("data/recovery_plans.txt");

        // Recovery Eligibility Checker
        RecoveryEligibilityChecker checker = new Part3RecoveryEligibilityAdapter();

        // Create manager (service layer)
        manager = new RecoveryPlanManager(checker, repo, policy);

        refreshPlansTable();

        // when user clicks a milestone row, load to form
        tblMilestones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedMilestoneToForm();
            }
        });

        // progress row
        tblProgress.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedProgressToForm();
            }
        });

        // grade row
        tblGrades.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedGradeToForm();
            }
        });

        // item row
        tblItems.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedItemToForm();
            }
        });
    }

    private void loadPlanIntoForm(String planId) {
        RecoveryPlan plan = manager.getPlanById(planId);
        if (plan == null) {
            JOptionPane.showMessageDialog(this,
                    "Plan not found: " + planId,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentPlan = plan;
        currentPlanId = plan.getPlanId();

        txtPlanID.setText(plan.getPlanId());

        if (plan.getStudent() != null) {
            String sid = plan.getStudent().getStudentId();
            txtStudentID.setText(sid);

            // Use Part-3 student data (students-abood.txt)
            crs.EligibilityAndEnrollment.Student s = part3Checker.getStudentById(sid);
            if (s != null) {
                txtStudentName.setText(s.getFirstName() + " " + s.getLastName());
            } else {
                txtStudentName.setText("");
            }
        }

        if (plan.getCourse() != null) {
            txtCourseCode.setText(plan.getCourse().getCourseCode());
        }

        cmbStatus.setSelectedItem(plan.getStatus().name());

        refreshItemsTable(plan);
        refreshMilestonesTable(plan);
        refreshProgressTable(plan);
        refreshGradesTable(plan);
        refreshOverallCompletion(plan);
    }

    private void refreshItemsTable(RecoveryPlan plan) {
        DefaultTableModel model = (DefaultTableModel) tblItems.getModel();
        model.setRowCount(0);

        if (plan == null) {
            return;
        }

        for (RecoveryItem item : plan.getItems()) {
            // 8 columns: ID, Type, Title, Due, Completed, Attempt, Comp/Lecturer, Location
            Object[] row = new Object[8];

            row[0] = item.getItemId();
            row[1] = item.getItemType();
            row[2] = item.getTitle();
            row[3] = item.getDueDate();
            row[4] = item.isCompleted();

            if (item instanceof ComponentRecoveryItem) {
                ComponentRecoveryItem cri = (ComponentRecoveryItem) item;
                row[5] = cri.getAttemptNumber(); // Attempt
                row[6] = cri.getComponent().getComponentName(); // Component name
                row[7] = ""; // no location

            } else if (item instanceof ConsultationRecoveryItem) {
                ConsultationRecoveryItem ci = (ConsultationRecoveryItem) item;
                row[5] = ""; // no attempt
                row[6] = ci.getLecturerName(); // Lecturer
                row[7] = ci.getLocation(); // Location

            } else {
                row[5] = "";
                row[6] = "";
                row[7] = "";
            }

            model.addRow(row);
        }
    }

    private void refreshMilestonesTable(RecoveryPlan plan) {
        DefaultTableModel model = (DefaultTableModel) tblMilestones.getModel();
        model.setRowCount(0);

        for (RecoveryMilestone ms : plan.getMilestones()) {
            model.addRow(new Object[] {
                    ms.getMilestoneId(),
                    ms.getStudyWeek(),
                    ms.getTask()
            });
        }
    }

    private void refreshProgressTable(RecoveryPlan plan) {
        DefaultTableModel model = (DefaultTableModel) tblProgress.getModel();
        model.setRowCount(0);

        for (ProgressEntry pe : plan.getProgressEntries()) {
            model.addRow(new Object[] {
                    pe.getProgressId(),
                    pe.getDate(),
                    pe.getCompletionPercent(),
                    pe.getNotes()
            });
        }
    }

    private void refreshGradesTable(RecoveryPlan plan) {
        DefaultTableModel model = (DefaultTableModel) tblGrades.getModel();
        model.setRowCount(0);

        for (GradeEntry ge : plan.getGradeEntries()) {
            model.addRow(new Object[] {
                    ge.getGradeId(),
                    ge.getComponentName(),
                    ge.getMarksObtained(),
                    ge.getGradeLetter(),
                    ge.getGradedDate()
            });
        }
    }

    private void refreshPlansTable() {

        DefaultTableModel model = (DefaultTableModel) tblPlans.getModel();
        model.setRowCount(0); // clear existing rows

        // Get all plans from the manager
        List<RecoveryPlan> allPlans = manager.getAllPlans();
        if (allPlans == null)
            return;

        for (RecoveryPlan plan : allPlans) {
            String planId = plan.getPlanId();

            String studentId = "";
            if (plan.getStudent() != null) {
                studentId = plan.getStudent().getStudentId();

            }

            String courseCode = "";
            if (plan.getCourse() != null) {
                courseCode = plan.getCourse().getCourseCode();
            }

            String status = plan.getStatus() != null
                    ? plan.getStatus().name()
                    : "";

            // Add one row to the table
            model.addRow(new Object[] {
                    planId,
                    studentId,
                    courseCode,
                    status
            });
        }
    }

    private void clearForm() {

        // Header fields
        txtPlanID.setText("");
        txtStudentID.setText("");
        txtStudentName.setText("");
        txtCourseCode.setText("");
        cmbStatus.setSelectedItem("PLANNED");

        // Clear tab input fields
        txtItemID.setText("");
        txtItemTitle.setText("");
        txtDueDate.setText("");
        chkCompleted.setSelected(false);
        txtComponentName.setText("");
        txtLecturerName.setText("");
        txtLocation.setText("");

        // Clear tables
        ((DefaultTableModel) tblItems.getModel()).setRowCount(0);
        ((DefaultTableModel) tblMilestones.getModel()).setRowCount(0);
        ((DefaultTableModel) tblProgress.getModel()).setRowCount(0);
        ((DefaultTableModel) tblGrades.getModel()).setRowCount(0);

        // Clear plan in memory
        currentPlan = null;
        currentPlanId = null;
    }

    private void refreshOverallCompletion(RecoveryPlan plan) {
        double percent = plan.calculateCompletionPercentage();
        lblOverallCompletion.setText("Overall Completion: " + String.format("%.1f", percent) + "%");
    }

    // Helper methods to load selected row into form
    // Milestones
    private void loadSelectedMilestoneToForm() {
        if (currentPlan == null)
            return;

        int row = tblMilestones.getSelectedRow();
        if (row < 0)
            return;

        RecoveryMilestone ms = currentPlan.getMilestones().get(row);
        txtMilestoneID.setText(ms.getMilestoneId());
        txtStudyWeek.setText(ms.getStudyWeek());
        txtMilestoneTask.setText(ms.getTask());
    }

    private void clearMilestoneForm() {
        txtMilestoneID.setText("");
        txtStudyWeek.setText("");
        txtMilestoneTask.setText("");
        tblMilestones.clearSelection();
    }

    // Progress
    private void loadSelectedProgressToForm() {
        if (currentPlan == null)
            return;

        int row = tblProgress.getSelectedRow();
        if (row < 0)
            return;

        ProgressEntry pe = currentPlan.getProgressEntries().get(row);
        txtProgressID.setText(pe.getProgressId());
        txtProgressDate.setText(pe.getDate().toString());
        spnCompletionPercent.setValue(pe.getCompletionPercent());
        txtNotes.setText(pe.getNotes());
    }

    private void clearProgressForm() {
        txtProgressID.setText("");
        txtProgressDate.setText("");
        spnCompletionPercent.setValue(0);
        txtNotes.setText("");
        tblProgress.clearSelection();
    }

    // Grades
    private void loadSelectedGradeToForm() {
        if (currentPlan == null)
            return;

        int row = tblGrades.getSelectedRow();
        if (row < 0)
            return;

        GradeEntry ge = currentPlan.getGradeEntries().get(row);
        txtGradeID.setText(ge.getGradeId());
        txtCompName.setText(ge.getComponentName());
        txtMarks.setText(String.valueOf(ge.getMarksObtained()));
        txtGradeLetter.setText(ge.getGradeLetter());
        txtGradeDate.setText(ge.getGradedDate().toString());
    }

    private void clearGradeForm() {
        txtGradeID.setText("");
        txtCompName.setText("");
        txtMarks.setText("");
        txtGradeLetter.setText("");
        txtGradeDate.setText("");
        tblGrades.clearSelection();
    }

    // Items
    private void loadSelectedItemToForm() {
        if (currentPlan == null)
            return;

        int row = tblItems.getSelectedRow();
        if (row < 0)
            return;

        RecoveryItem item = currentPlan.getItems().get(row);

        // Common fields
        txtItemID.setText(item.getItemId());
        txtItemTitle.setText(item.getTitle());
        txtDueDate.setText(item.getDueDate() != null ? item.getDueDate().toString() : "");
        chkCompleted.setSelected(item.isCompleted());

        // Reset text + enable everything first
        txtComponentName.setText("");
        txtLecturerName.setText("");
        txtLocation.setText("");
        spnAttemptNumber.setValue(1);

        txtComponentName.setEnabled(true);
        txtLecturerName.setEnabled(true);
        txtLocation.setEnabled(true);
        spnAttemptNumber.setEnabled(true);

        if (item instanceof ComponentRecoveryItem) {
            ComponentRecoveryItem cri = (ComponentRecoveryItem) item;

            // Match what is in the combo box ("Component")
            cmbItemType.setSelectedItem("Component");

            if (cri.getComponent() != null) {
                txtComponentName.setText(cri.getComponent().getComponentName());
            }
            spnAttemptNumber.setValue(cri.getAttemptNumber());

            // Consultation-only fields not used here
            txtLecturerName.setEnabled(false);
            txtLocation.setEnabled(false);

        } else if (item instanceof ConsultationRecoveryItem) {
            ConsultationRecoveryItem ci = (ConsultationRecoveryItem) item;

            // Match combo value ("Consultation")
            cmbItemType.setSelectedItem("Consultation");

            txtLecturerName.setText(ci.getLecturerName());
            txtLocation.setText(ci.getLocation());

            // Component/attempt not used here
            txtComponentName.setEnabled(false);
            spnAttemptNumber.setEnabled(false);
        }
    }

    private void clearItemForm() {
        txtItemID.setText("");
        txtItemTitle.setText("");
        txtDueDate.setText("");
        chkCompleted.setSelected(false);

        txtComponentName.setText("");
        txtLecturerName.setText("");
        txtLocation.setText("");

        // Reset combo & spinner
        cmbItemType.setSelectedItem("Component");
        spnAttemptNumber.setValue(1);

        // Re-enable all fields for a fresh entry
        txtComponentName.setEnabled(true);
        txtLecturerName.setEnabled(true);
        txtLocation.setEnabled(true);
        spnAttemptNumber.setEnabled(true);

        tblItems.clearSelection();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        panelHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPlanID = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtStudentID = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtStudentName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCourseCode = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        btnNewPlan = new javax.swing.JButton();
        btnClearForm = new javax.swing.JButton();
        panelCentre = new javax.swing.JTabbedPane();
        panelMilestones = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtMilestoneID = new javax.swing.JTextField();
        txtStudyWeek = new javax.swing.JTextField();
        txtMilestoneTask = new javax.swing.JTextField();
        btnAddMilestone = new javax.swing.JButton();
        btnUpdateMilestone = new javax.swing.JButton();
        btnRemoveMilestone = new javax.swing.JButton();
        btnClearMilestoneForm = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMilestones = new javax.swing.JTable();
        panelProgressGrades = new javax.swing.JPanel();
        pnlProgress = new javax.swing.JPanel();
        lblProgressID = new javax.swing.JLabel();
        lblProgressDate = new javax.swing.JLabel();
        lblCompletePercent = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        btnAddProgress = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        txtProgressID = new javax.swing.JTextField();
        txtProgressDate = new javax.swing.JTextField();
        txtNotes = new javax.swing.JTextField();
        spnCompletionPercent = new javax.swing.JSpinner();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblProgress = new javax.swing.JTable();
        btnClearProgress = new javax.swing.JButton();
        pnlGrade = new javax.swing.JPanel();
        lblGradeID = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        btnAddGrade = new javax.swing.JButton();
        btnClearGrade = new javax.swing.JButton();
        txtGradeID = new javax.swing.JTextField();
        txtCompName = new javax.swing.JTextField();
        txtMarks = new javax.swing.JTextField();
        txtGradeLetter = new javax.swing.JTextField();
        txtGradeDate = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblGrades = new javax.swing.JTable();
        panelItems = new javax.swing.JPanel();
        lblItemType = new javax.swing.JLabel();
        cmbItemType = new javax.swing.JComboBox<>();
        lblItemID = new javax.swing.JLabel();
        txtItemID = new javax.swing.JTextField();
        lblTitle = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtDueDate = new javax.swing.JTextField();
        chkCompleted = new javax.swing.JCheckBox();
        lblComponentName = new javax.swing.JLabel();
        txtComponentName = new javax.swing.JTextField();
        txtItemTitle = new javax.swing.JTextField();
        lblAttempt = new javax.swing.JLabel();
        spnAttemptNumber = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        txtLecturerName = new javax.swing.JTextField();
        lblLocation = new javax.swing.JLabel();
        txtLocation = new javax.swing.JTextField();
        btnAddItem = new javax.swing.JButton();
        btnRemoveItem = new javax.swing.JButton();
        btnUpdateItem = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblItems = new javax.swing.JTable();
        lblOverallCompletion = new javax.swing.JLabel();
        panelFooter = new javax.swing.JPanel();
        btnSaveAll = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblPlans = new javax.swing.JTable();
        btnRefreshPlans = new javax.swing.JButton();
        btnLoadSelectedPlans = new javax.swing.JButton();
        btnDeletePlan = new javax.swing.JButton();

        jToolBar1.setRollover(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelHeader.setPreferredSize(new java.awt.Dimension(725, 190));
        panelHeader.setLayout(null);

        jLabel1.setText("Plan ID:");
        panelHeader.add(jLabel1);
        jLabel1.setBounds(40, 40, 50, 17);

        txtPlanID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlanIDActionPerformed(evt);
            }
        });
        panelHeader.add(txtPlanID);
        txtPlanID.setBounds(130, 40, 64, 23);

        jButton1.setText("Load Plan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panelHeader.add(jButton1);
        jButton1.setBounds(270, 40, 100, 23);

        jLabel2.setText("Student ID:");
        panelHeader.add(jLabel2);
        jLabel2.setBounds(40, 70, 70, 17);
        panelHeader.add(txtStudentID);
        txtStudentID.setBounds(130, 70, 100, 23);

        jButton2.setText("Load Student");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        panelHeader.add(jButton2);
        jButton2.setBounds(270, 70, 110, 23);

        jLabel3.setText("Student Name:");
        panelHeader.add(jLabel3);
        jLabel3.setBounds(410, 70, 100, 17);
        panelHeader.add(txtStudentName);
        txtStudentName.setBounds(520, 70, 130, 23);

        jLabel4.setText("Course Code:");
        panelHeader.add(jLabel4);
        jLabel4.setBounds(40, 100, 80, 17);

        txtCourseCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCourseCodeActionPerformed(evt);
            }
        });
        panelHeader.add(txtCourseCode);
        txtCourseCode.setBounds(130, 100, 120, 23);

        lblStatus.setText("Status:");
        panelHeader.add(lblStatus);
        lblStatus.setBounds(40, 140, 50, 17);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "PLANNED", "IN_PROGRESS", "COMPLETED", "CANCELLED" }));
        panelHeader.add(cmbStatus);
        cmbStatus.setBounds(130, 140, 120, 20);

        jButton3.setText("Change Status");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        panelHeader.add(jButton3);
        jButton3.setBounds(280, 140, 115, 23);

        btnNewPlan.setText("New Plan");
        btnNewPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewPlanActionPerformed(evt);
            }
        });
        panelHeader.add(btnNewPlan);
        btnNewPlan.setBounds(420, 140, 90, 23);

        btnClearForm.setText("Clear Form");
        btnClearForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFormActionPerformed(evt);
            }
        });
        panelHeader.add(btnClearForm);
        btnClearForm.setBounds(570, 120, 150, 50);

        panelCentre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                panelCentreFocusGained(evt);
            }
        });

        panelMilestones.setPreferredSize(new java.awt.Dimension(733, 300));

        jLabel9.setText("Milestone ID:");

        jLabel10.setText("Study Week:");

        jLabel11.setText("Task:");

        btnAddMilestone.setText("Add Milestone");
        btnAddMilestone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMilestoneActionPerformed(evt);
            }
        });

        btnUpdateMilestone.setText("Update");
        btnUpdateMilestone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateMilestoneActionPerformed(evt);
            }
        });

        btnRemoveMilestone.setText("Remove");
        btnRemoveMilestone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveMilestoneActionPerformed(evt);
            }
        });

        btnClearMilestoneForm.setText("Clear");
        btnClearMilestoneForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearMilestoneFormActionPerformed(evt);
            }
        });

        tblMilestones.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null },
                        { null, null, null },
                        { null, null, null },
                        { null, null, null }
                },
                new String[] {
                        "Milestone ID", "Study Week", "Task"
                }));
        jScrollPane2.setViewportView(tblMilestones);

        javax.swing.GroupLayout panelMilestonesLayout = new javax.swing.GroupLayout(panelMilestones);
        panelMilestones.setLayout(panelMilestonesLayout);
        panelMilestonesLayout.setHorizontalGroup(
                panelMilestonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelMilestonesLayout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addGroup(panelMilestonesLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 648,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panelMilestonesLayout.createSequentialGroup()
                                                .addGroup(panelMilestonesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel9)
                                                        .addComponent(jLabel10)
                                                        .addComponent(jLabel11))
                                                .addGap(69, 69, 69)
                                                .addGroup(panelMilestonesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtMilestoneTask,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 300,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(panelMilestonesLayout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        false)
                                                                .addComponent(txtMilestoneID,
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 172,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(txtStudyWeek,
                                                                        javax.swing.GroupLayout.Alignment.LEADING))))
                                        .addGroup(panelMilestonesLayout.createSequentialGroup()
                                                .addGap(52, 52, 52)
                                                .addComponent(btnAddMilestone)
                                                .addGap(20, 20, 20)
                                                .addComponent(btnUpdateMilestone)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnRemoveMilestone)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnClearMilestoneForm)))
                                .addContainerGap(275, Short.MAX_VALUE)));
        panelMilestonesLayout.setVerticalGroup(
                panelMilestonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelMilestonesLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(panelMilestonesLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(txtMilestoneID, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelMilestonesLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10)
                                        .addComponent(txtStudyWeek, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(24, 24, 24)
                                .addGroup(panelMilestonesLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(txtMilestoneTask, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelMilestonesLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddMilestone)
                                        .addComponent(btnUpdateMilestone)
                                        .addComponent(btnRemoveMilestone)
                                        .addComponent(btnClearMilestoneForm))
                                .addGap(28, 28, 28)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 289,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(14, Short.MAX_VALUE)));

        panelCentre.addTab("Milestones ( Study Weeks )", panelMilestones);

        pnlProgress.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblProgressID.setText("Progress ID:");

        lblProgressDate.setText("Date:");

        lblCompletePercent.setText("% Complete:");

        jLabel15.setText("Notes:");

        btnAddProgress.setText("Add Progress");
        btnAddProgress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProgressActionPerformed(evt);
            }
        });

        jButton6.setText("Clear");

        spnCompletionPercent.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));

        tblProgress.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Progress ID", "Date", "% Complete", "Notes"
                }));
        jScrollPane3.setViewportView(tblProgress);

        btnClearProgress.setText("Clear");
        btnClearProgress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearProgressActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlProgressLayout = new javax.swing.GroupLayout(pnlProgress);
        pnlProgress.setLayout(pnlProgressLayout);
        pnlProgressLayout.setHorizontalGroup(
                pnlProgressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlProgressLayout.createSequentialGroup()
                                .addGroup(pnlProgressLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlProgressLayout.createSequentialGroup()
                                                .addGap(31, 31, 31)
                                                .addGroup(pnlProgressLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(pnlProgressLayout.createSequentialGroup()
                                                                .addGap(39, 39, 39)
                                                                .addComponent(btnAddProgress)
                                                                .addGap(60, 60, 60)
                                                                .addComponent(btnClearProgress)
                                                                .addGap(596, 596, 596)
                                                                .addComponent(jButton6))
                                                        .addGroup(pnlProgressLayout.createSequentialGroup()
                                                                .addGroup(pnlProgressLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(pnlProgressLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(
                                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                        pnlProgressLayout
                                                                                                .createParallelGroup(
                                                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addGroup(
                                                                                                        pnlProgressLayout
                                                                                                                .createSequentialGroup()
                                                                                                                .addComponent(
                                                                                                                        lblProgressDate)
                                                                                                                .addGap(74,
                                                                                                                        74,
                                                                                                                        74))
                                                                                                .addGroup(
                                                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                        pnlProgressLayout
                                                                                                                .createSequentialGroup()
                                                                                                                .addComponent(
                                                                                                                        lblCompletePercent)
                                                                                                                .addGap(28,
                                                                                                                        28,
                                                                                                                        28)))
                                                                                .addGroup(pnlProgressLayout
                                                                                        .createSequentialGroup()
                                                                                        .addComponent(jLabel15)
                                                                                        .addGap(67, 67, 67)))
                                                                        .addGroup(pnlProgressLayout
                                                                                .createSequentialGroup()
                                                                                .addComponent(lblProgressID)
                                                                                .addGap(34, 34, 34)))
                                                                .addGroup(pnlProgressLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(txtProgressID,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                104, Short.MAX_VALUE)
                                                                        .addGroup(pnlProgressLayout
                                                                                .createSequentialGroup()
                                                                                .addComponent(spnCompletionPercent,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                        .addComponent(txtNotes)
                                                                        .addComponent(txtProgressDate)))))
                                        .addGroup(pnlProgressLayout.createSequentialGroup()
                                                .addGap(15, 15, 15)
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 382,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pnlProgressLayout.setVerticalGroup(
                pnlProgressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlProgressLayout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(pnlProgressLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblProgressID, javax.swing.GroupLayout.PREFERRED_SIZE, 17,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtProgressID, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlProgressLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblProgressDate)
                                        .addComponent(txtProgressDate, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlProgressLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblCompletePercent)
                                        .addComponent(spnCompletionPercent, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlProgressLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15)
                                        .addComponent(txtNotes, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(pnlProgressLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddProgress)
                                        .addComponent(jButton6)
                                        .addComponent(btnClearProgress))
                                .addGap(26, 26, 26)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 217,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(37, Short.MAX_VALUE)));

        pnlGrade.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblGradeID.setText("Grade ID:");

        jLabel17.setText("Component Name:");

        jLabel18.setText("Marks:");

        jLabel19.setText("Grade Letter:");

        jLabel20.setText("Date:");

        btnAddGrade.setText("Add Grade");
        btnAddGrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddGradeActionPerformed(evt);
            }
        });

        btnClearGrade.setText("Clear");
        btnClearGrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearGradeActionPerformed(evt);
            }
        });

        txtCompName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCompNameActionPerformed(evt);
            }
        });

        tblGrades.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null }
                },
                new String[] {
                        "Grade ID", "Component", "Marks", "Letter", "Date"
                }));
        jScrollPane4.setViewportView(tblGrades);

        javax.swing.GroupLayout pnlGradeLayout = new javax.swing.GroupLayout(pnlGrade);
        pnlGrade.setLayout(pnlGradeLayout);
        pnlGradeLayout.setHorizontalGroup(
                pnlGradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlGradeLayout.createSequentialGroup()
                                .addGroup(pnlGradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlGradeLayout.createSequentialGroup()
                                                .addGap(37, 37, 37)
                                                .addGroup(pnlGradeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel17)
                                                        .addComponent(jLabel18)
                                                        .addComponent(jLabel19)
                                                        .addComponent(jLabel20)
                                                        .addComponent(lblGradeID))
                                                .addGap(91, 91, 91)
                                                .addGroup(pnlGradeLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtCompName,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 139,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtGradeDate,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 118,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtGradeID,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(pnlGradeLayout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        false)
                                                                .addComponent(txtGradeLetter,
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 84,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(txtMarks,
                                                                        javax.swing.GroupLayout.Alignment.LEADING))))
                                        .addGroup(pnlGradeLayout.createSequentialGroup()
                                                .addGap(93, 93, 93)
                                                .addComponent(btnAddGrade)
                                                .addGap(71, 71, 71)
                                                .addComponent(btnClearGrade))
                                        .addGroup(pnlGradeLayout.createSequentialGroup()
                                                .addGap(22, 22, 22)
                                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 397,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(104, Short.MAX_VALUE)));
        pnlGradeLayout.setVerticalGroup(
                pnlGradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlGradeLayout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addGroup(pnlGradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblGradeID)
                                        .addComponent(txtGradeID, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlGradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel17)
                                        .addComponent(txtCompName, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlGradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel18)
                                        .addComponent(txtMarks, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlGradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel19)
                                        .addComponent(txtGradeLetter, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlGradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel20)
                                        .addComponent(txtGradeDate, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlGradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddGrade)
                                        .addComponent(btnClearGrade))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 194,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout panelProgressGradesLayout = new javax.swing.GroupLayout(panelProgressGrades);
        panelProgressGrades.setLayout(panelProgressGradesLayout);
        panelProgressGradesLayout.setHorizontalGroup(
                panelProgressGradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelProgressGradesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 429,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pnlGrade, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        panelProgressGradesLayout.setVerticalGroup(
                panelProgressGradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                panelProgressGradesLayout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addGroup(panelProgressGradesLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(pnlGrade, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(pnlProgress, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addContainerGap()));

        panelCentre.addTab("Progress And Grades", panelProgressGrades);

        panelItems.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelItems.setPreferredSize(new java.awt.Dimension(733, 300));

        lblItemType.setText("Item Type:");

        cmbItemType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Component", "Consultation" }));

        lblItemID.setText("Item ID:");

        lblTitle.setText("Title:");

        jLabel6.setText("Due Date:");

        chkCompleted.setText("Completed");

        lblComponentName.setText("Component Name:");

        lblAttempt.setText("Attempt (auto-calculated)");

        spnAttemptNumber.setModel(new javax.swing.SpinnerNumberModel(1, 1, 3, 1));

        jLabel8.setText("Lecturer Name:");

        lblLocation.setText("Location:");

        btnAddItem.setText("Add Item");
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });

        btnRemoveItem.setText("Remove Item");
        btnRemoveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveItemActionPerformed(evt);
            }
        });

        btnUpdateItem.setText("Update Item");
        btnUpdateItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateItemActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        tblItems.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null }
                },
                new String[] {
                        "Item ID", "Type", "Title", "Due Date", "Completed", "Attempt", "Component/Lecturer", "Location"
                }));
        jScrollPane1.setViewportView(tblItems);

        lblOverallCompletion.setText("Overall Completion %");

        javax.swing.GroupLayout panelItemsLayout = new javax.swing.GroupLayout(panelItems);
        panelItems.setLayout(panelItemsLayout);
        panelItemsLayout.setHorizontalGroup(
                panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelItemsLayout.createSequentialGroup()
                                .addGroup(panelItemsLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelItemsLayout.createSequentialGroup()
                                                .addGap(45, 45, 45)
                                                .addGroup(panelItemsLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblItemType)
                                                        .addComponent(lblItemID)
                                                        .addComponent(jLabel6)
                                                        .addComponent(lblComponentName)
                                                        .addComponent(jLabel8)
                                                        .addComponent(lblLocation))
                                                .addGap(35, 35, 35)
                                                .addGroup(panelItemsLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cmbItemType,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(panelItemsLayout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                .addComponent(txtLecturerName,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 124,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(txtLocation))
                                                        .addComponent(txtComponentName,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 158,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(panelItemsLayout.createSequentialGroup()
                                                                .addGroup(panelItemsLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(txtItemID,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                107, Short.MAX_VALUE)
                                                                        .addComponent(txtDueDate))
                                                                .addGap(68, 68, 68)
                                                                .addGroup(panelItemsLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(panelItemsLayout
                                                                                .createSequentialGroup()
                                                                                .addComponent(lblTitle)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(txtItemTitle,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        305,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(panelItemsLayout
                                                                                .createSequentialGroup()
                                                                                .addGroup(panelItemsLayout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(chkCompleted)
                                                                                        .addGroup(panelItemsLayout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(1, 1, 1)
                                                                                                .addComponent(
                                                                                                        lblAttempt)))
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(spnAttemptNumber,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                        .addGroup(panelItemsLayout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                .addGroup(panelItemsLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane1,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 722,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(panelItemsLayout.createSequentialGroup()
                                                                .addComponent(btnAddItem)
                                                                .addGap(27, 27, 27)
                                                                .addComponent(btnUpdateItem)
                                                                .addGap(33, 33, 33)
                                                                .addComponent(btnRemoveItem)
                                                                .addGap(28, 28, 28)
                                                                .addComponent(btnClear))))
                                        .addGroup(panelItemsLayout.createSequentialGroup()
                                                .addGap(43, 43, 43)
                                                .addComponent(lblOverallCompletion,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 295,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(225, Short.MAX_VALUE)));
        panelItemsLayout.setVerticalGroup(
                panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelItemsLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(
                                        panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(cmbItemType, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblItemType))
                                .addGap(27, 27, 27)
                                .addGroup(
                                        panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(lblItemID)
                                                .addComponent(txtItemID, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblTitle)
                                                .addComponent(txtItemTitle, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelItemsLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelItemsLayout.createSequentialGroup()
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel6))
                                        .addGroup(panelItemsLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelItemsLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtDueDate,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(chkCompleted))))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(txtComponentName, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblComponentName)
                                                .addComponent(lblAttempt)
                                                .addComponent(spnAttemptNumber, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel8)
                                                .addComponent(txtLecturerName, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblLocation))
                                .addGap(18, 18, 18)
                                .addGroup(
                                        panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnAddItem)
                                                .addComponent(btnUpdateItem)
                                                .addComponent(btnRemoveItem)
                                                .addComponent(btnClear))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblOverallCompletion)
                                .addContainerGap()));

        panelCentre.addTab("Items ( Failed Components & Tasks )", panelItems);

        panelFooter.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelFooter.setPreferredSize(new java.awt.Dimension(725, 50));

        btnSaveAll.setText("Save All");
        btnSaveAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAllActionPerformed(evt);
            }
        });

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelFooterLayout = new javax.swing.GroupLayout(panelFooter);
        panelFooter.setLayout(panelFooterLayout);
        panelFooterLayout.setHorizontalGroup(
                panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelFooterLayout.createSequentialGroup()
                                .addGap(279, 279, 279)
                                .addComponent(btnSaveAll)
                                .addGap(136, 136, 136)
                                .addComponent(btnClose)
                                .addContainerGap(412, Short.MAX_VALUE)));
        panelFooterLayout.setVerticalGroup(
                panelFooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelFooterLayout.createSequentialGroup()
                                .addContainerGap(11, Short.MAX_VALUE)
                                .addGroup(panelFooterLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSaveAll)
                                        .addComponent(btnClose))
                                .addGap(14, 14, 14)));

        tblPlans.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Plan ID", "Student ID", "Course Code", "Status"
                }));
        jScrollPane5.setViewportView(tblPlans);

        btnRefreshPlans.setText("Refresh Plans");
        btnRefreshPlans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshPlansActionPerformed(evt);
            }
        });

        btnLoadSelectedPlans.setText("Load Selected Plan");
        btnLoadSelectedPlans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadSelectedPlansActionPerformed(evt);
            }
        });

        btnDeletePlan.setText("Delete Selected Plan");
        btnDeletePlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePlanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelFooter, javax.swing.GroupLayout.PREFERRED_SIZE, 978,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(panelCentre,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(panelHeader,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 978,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(41, 41, 41)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(btnLoadSelectedPlans)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(btnDeletePlan)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnRefreshPlans))
                                                        .addComponent(jScrollPane5,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(43, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(panelHeader, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, 0)
                                                .addComponent(panelCentre, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(109, 109, 109)
                                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnRefreshPlans)
                                                        .addComponent(btnLoadSelectedPlans)
                                                        .addComponent(btnDeletePlan))))
                                .addGap(0, 0, 0)
                                .addComponent(panelFooter, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));

        panelFooter.getAccessibleContext().setAccessibleParent(this);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPlanIDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtPlanIDActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtPlanIDActionPerformed

    private void panelCentreFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_panelCentreFocusGained
        // TODO add your handling code here:
    }// GEN-LAST:event_panelCentreFocusGained

    private void btnNewPlanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNewPlanActionPerformed
        String planId = txtPlanID.getText().trim();
        String studentId = txtStudentID.getText().trim();
        String studentName = txtStudentName.getText().trim();
        String courseCode = txtCourseCode.getText().trim();

        if (planId.isEmpty() || studentId.isEmpty() || courseCode.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter Plan ID, Student ID and Course Code.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // to prevent duplicate plans being created
        RecoveryPlan existing = manager.getPlanById(planId);
        if (existing != null) {
            JOptionPane.showMessageDialog(this,
                    "A recovery plan with this Plan ID already exists.\n" +
                            "Use 'Load Plan' or choose a different Plan ID.",
                    "Duplicate Plan ID",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = new Student(studentId, studentName);
        Course course = new Course(courseCode);

        try {
            manager.createPlan(planId, student, course);

            // Sazid R Khan's code starts here
            new NotificationManager().sendRecoveryPlanNotification(studentId, planId, courseCode, (String) cmbStatus.getSelectedItem());
            // Sazid R Khan's code ends here

            JOptionPane.showMessageDialog(this,
                    "Recovery plan created successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadPlanIntoForm(planId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error creating plan: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btnNewPlanActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        String planId = txtPlanID.getText().trim();
        if (planId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Plan ID to load.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadPlanIntoForm(planId);
    }// GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        if (currentPlanId == null) {
            JOptionPane.showMessageDialog(this,
                    "No plan is loaded.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String statusStr = (String) cmbStatus.getSelectedItem();
        RecoveryStatus newStatus = RecoveryStatus.valueOf(statusStr);

        try {
            manager.changeStatus(currentPlanId, newStatus);
            JOptionPane.showMessageDialog(this, "Status updated.");
            loadPlanIntoForm(currentPlanId);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Status Change Blocked",
                    JOptionPane.WARNING_MESSAGE);

            // revert to old value
            cmbStatus.setSelectedItem(currentPlan.getStatus().name());
        }
    }// GEN-LAST:event_jButton3ActionPerformed

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddItemActionPerformed
        if (currentPlanId == null) {
            JOptionPane.showMessageDialog(this,
                    "Please create or load a plan first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String type = (String) cmbItemType.getSelectedItem();
        String itemId = txtItemID.getText().trim();
        String title = txtItemTitle.getText().trim();
        String dueStr = txtDueDate.getText().trim();
        boolean completed = chkCompleted.isSelected();

        if (itemId.isEmpty() || title.isEmpty() || dueStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill Item ID, Title and Due Date.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // DUPLICATE CHECK FOR ITEM ID
        if (currentPlan != null) {
            boolean exists = currentPlan.getItems().stream()
                    .anyMatch(i -> i.getItemId().equalsIgnoreCase(itemId));
            if (exists) {
                JOptionPane.showMessageDialog(this,
                        "An item with this ID already exists in this plan.",
                        "Duplicate Item ID",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(dueStr); // expects YYYY-MM-DD
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Use YYYY-MM-DD.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        RecoveryItem item = null;

        if ("COMPONENT".equalsIgnoreCase(type)) {
            String componentName = txtComponentName.getText().trim();

            // GUI NO LONGER decides the attempt.
            // Manager will compute and set the correct attempt based on
            // existing recovery plans for this student+course.
            int dummyAttempt = 1; // placeholder, will be overridden in RecoveryPlanManager

            CourseComponent component = new CourseComponent("", componentName);
            item = new ComponentRecoveryItem(
                    itemId,
                    title,
                    "",
                    dueDate,
                    completed,
                    component,
                    dummyAttempt);
        } else if ("CONSULTATION".equalsIgnoreCase(type)) {
            String lecturer = txtLecturerName.getText().trim();
            String location = txtLocation.getText().trim();
            item = new ConsultationRecoveryItem(
                    itemId,
                    title,
                    "",
                    dueDate,
                    completed,
                    lecturer,
                    location);
        }

        try {
            manager.addItemToPlan(currentPlanId, item);
            JOptionPane.showMessageDialog(this,
                    "Item added.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadPlanIntoForm(currentPlanId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding item: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btnAddItemActionPerformed

    private void btnAddMilestoneActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddMilestoneActionPerformed
        if (currentPlanId == null) {
            JOptionPane.showMessageDialog(this,
                    "Please create or load a plan first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String msId = txtMilestoneID.getText().trim();
        String week = txtStudyWeek.getText().trim();
        String task = txtMilestoneTask.getText().trim();

        if (msId.isEmpty() || week.isEmpty() || task.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all milestone fields.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // DUPLICATE CHECK FOR MILESTONE ID
        if (currentPlan != null) {
            boolean exists = currentPlan.getMilestones().stream()
                    .anyMatch(m -> m.getMilestoneId().equalsIgnoreCase(msId));
            if (exists) {
                JOptionPane.showMessageDialog(this,
                        "A milestone with this ID already exists in this plan.",
                        "Duplicate Milestone ID",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        RecoveryMilestone ms = new RecoveryMilestone(msId, week, task);
        manager.addMilestoneToPlan(currentPlanId, ms);

        JOptionPane.showMessageDialog(this,
                "Milestone added.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        loadPlanIntoForm(currentPlanId);
    }// GEN-LAST:event_btnAddMilestoneActionPerformed

    private void btnAddProgressActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddProgressActionPerformed
        if (currentPlanId == null) {
            JOptionPane.showMessageDialog(this,
                    "Please create or load a plan first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String progressId = txtProgressID.getText().trim();
        String dateStr = txtProgressDate.getText().trim();
        int percent = (Integer) spnCompletionPercent.getValue();
        String notes = txtNotes.getText().trim();

        if (progressId.isEmpty() || dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill Progress ID and Date.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Prevent duplicate Progress ID within the same plan
        for (ProgressEntry existing : currentPlan.getProgressEntries()) {
            if (existing.getProgressId().equalsIgnoreCase(progressId)) {
                JOptionPane.showMessageDialog(this,
                        "A progress entry with ID '" + progressId + "' already exists.",
                        "Duplicate Progress ID",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Use YYYY-MM-DD.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProgressEntry pe = new ProgressEntry(progressId, date, percent, notes);
        manager.recordProgress(currentPlanId, pe);

        JOptionPane.showMessageDialog(this,
                "Progress recorded.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        loadPlanIntoForm(currentPlanId);
    }// GEN-LAST:event_btnAddProgressActionPerformed

    private void btnAddGradeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddGradeActionPerformed
        if (currentPlanId == null) {
            JOptionPane.showMessageDialog(this,
                    "Please create or load a plan first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String gradeId = txtGradeID.getText().trim();
        String componentName = txtCompName.getText().trim();
        String marksStr = txtMarks.getText().trim();
        String letter = txtGradeLetter.getText().trim();
        String dateStr = txtGradeDate.getText().trim();

        if (gradeId.isEmpty() || componentName.isEmpty()
                || marksStr.isEmpty() || letter.isEmpty() || dateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all grade fields.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Prevent duplicate Grade ID inside the same plan
        for (GradeEntry existing : currentPlan.getGradeEntries()) {
            if (existing.getGradeId().equalsIgnoreCase(gradeId)) {
                JOptionPane.showMessageDialog(this,
                        "A grade entry with ID '" + gradeId + "' already exists.",
                        "Duplicate Grade ID",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        double marks;
        try {
            marks = Double.parseDouble(marksStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Marks must be a number.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Use YYYY-MM-DD.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        GradeEntry ge = new GradeEntry(gradeId, componentName, marks, letter, date);
        manager.recordGrade(currentPlanId, ge);

        JOptionPane.showMessageDialog(this,
                "Grade recorded.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        loadPlanIntoForm(currentPlanId);
    }// GEN-LAST:event_btnAddGradeActionPerformed

    private void btnSaveAllActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSaveAllActionPerformed
        JOptionPane.showMessageDialog(this,
                "Changes are already saved automatically.",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }// GEN-LAST:event_btnSaveAllActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
        AcademicOfficerDashboard dashboard = new AcademicOfficerDashboard();
        dashboard.setVisible(true);
    }// GEN-LAST:event_btnCloseActionPerformed

    private void txtCourseCodeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtCourseCodeActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtCourseCodeActionPerformed

    private void txtCompNameActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtCompNameActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtCompNameActionPerformed

    private void btnUpdateMilestoneActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnUpdateMilestoneActionPerformed
        if (currentPlan == null)
            return;

        int row = tblMilestones.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a milestone to update.");
            return;
        }

        RecoveryMilestone ms = currentPlan.getMilestones().get(row);
        ms.setMilestoneId(txtMilestoneID.getText().trim());
        ms.setStudyWeek(txtStudyWeek.getText().trim());
        ms.setTask(txtMilestoneTask.getText().trim());

        refreshMilestonesTable(currentPlan);
        manager.persistUpdates();
        JOptionPane.showMessageDialog(this, "Milestone updated and saved.");
    }// GEN-LAST:event_btnUpdateMilestoneActionPerformed

    private void btnRemoveMilestoneActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRemoveMilestoneActionPerformed
        if (currentPlan == null)
            return;

        int row = tblMilestones.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a milestone to remove.");
            return;
        }

        currentPlan.getMilestones().remove(row);
        refreshMilestonesTable(currentPlan);
        clearMilestoneForm();
    }// GEN-LAST:event_btnRemoveMilestoneActionPerformed

    private void btnClearMilestoneFormActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnClearMilestoneFormActionPerformed
        clearMilestoneForm();
    }// GEN-LAST:event_btnClearMilestoneFormActionPerformed

    private void btnClearProgressActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnClearProgressActionPerformed
        clearProgressForm();
    }// GEN-LAST:event_btnClearProgressActionPerformed

    private void btnClearGradeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnClearGradeActionPerformed
        clearGradeForm();
    }// GEN-LAST:event_btnClearGradeActionPerformed

    private void btnUpdateItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnUpdateItemActionPerformed
        if (currentPlan == null)
            return;

        int row = tblItems.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an item to update.");
            return;
        }

        RecoveryItem item = currentPlan.getItems().get(row);

        // common fields
        item.setItemId(txtItemID.getText().trim());
        item.setTitle(txtItemTitle.getText().trim());
        item.setDueDate(LocalDate.parse(txtDueDate.getText().trim()));
        item.setCompleted(chkCompleted.isSelected());

        String type = (String) cmbItemType.getSelectedItem();

        if ("COMPONENT".equalsIgnoreCase(type) && item instanceof ComponentRecoveryItem) {
            ComponentRecoveryItem cri = (ComponentRecoveryItem) item;
            cri.setAttemptNumber((Integer) spnAttemptNumber.getValue());
            cri.setComponent(new CourseComponent("", txtComponentName.getText().trim()));
        } else if ("CONSULTATION".equalsIgnoreCase(type) && item instanceof ConsultationRecoveryItem) {
            ConsultationRecoveryItem ci = (ConsultationRecoveryItem) item;
            ci.setLecturerName(txtLecturerName.getText().trim());
            ci.setLocation(txtLocation.getText().trim());
        }

        refreshItemsTable(currentPlan);
        refreshOverallCompletion(currentPlan);
        manager.persistUpdates();
        JOptionPane.showMessageDialog(this, "Item updated and saved.");
    }// GEN-LAST:event_btnUpdateItemActionPerformed

    private void btnRemoveItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRemoveItemActionPerformed
        if (currentPlan == null)
            return;

        int row = tblItems.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an item to remove.");
            return;
        }

        currentPlan.getItems().remove(row);
        refreshItemsTable(currentPlan);
        refreshOverallCompletion(currentPlan);
        clearItemForm();
    }// GEN-LAST:event_btnRemoveItemActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnClearActionPerformed
        clearItemForm();
    }// GEN-LAST:event_btnClearActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        String id = txtStudentID.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Student ID.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        crs.EligibilityAndEnrollment.Student s = part3Checker.getStudentById(id);
        if (s == null) {
            JOptionPane.showMessageDialog(this,
                    "Student not found in eligibility data file.",
                    "Not Found",
                    JOptionPane.INFORMATION_MESSAGE);
            txtStudentName.setText("");
            return;
        }

        txtStudentName.setText(s.getFirstName() + " " + s.getLastName());
    }// GEN-LAST:event_jButton2ActionPerformed

    private void btnRefreshPlansActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRefreshPlansActionPerformed
        refreshPlansTable();
        JOptionPane.showMessageDialog(this,
                "Plan list refreshed.",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }// GEN-LAST:event_btnRefreshPlansActionPerformed

    private void btnLoadSelectedPlansActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnLoadSelectedPlansActionPerformed
        int selectedRow = tblPlans.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a plan from the table first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Assuming Plan ID is column 0:
        String planId = (String) tblPlans.getValueAt(selectedRow, 0);

        if (planId == null || planId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Plan ID in selected row.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Put the Plan ID into the textbox (so user can see it)
        txtPlanID.setText(planId.trim());

        // Use your existing method to load all details of that plan into all tabs
        loadPlanIntoForm(planId.trim());
    }// GEN-LAST:event_btnLoadSelectedPlansActionPerformed

    private void btnClearFormActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnClearFormActionPerformed
        clearForm();
        JOptionPane.showMessageDialog(this, "Form cleared.");
    }// GEN-LAST:event_btnClearFormActionPerformed

    private void btnDeletePlanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDeletePlanActionPerformed
        if (currentPlanId == null) {
            JOptionPane.showMessageDialog(this,
                    "No plan is loaded.",
                    "Delete Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this plan?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION)
            return;

        try {
            // to remove from list + save to file
            manager.deletePlan(currentPlanId);
            JOptionPane.showMessageDialog(this,
                    "Plan deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear UI and refresh list
            clearForm();
            refreshPlansTable();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not delete plan: " + ex.getMessage(),
                    "Delete Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_btnDeletePlanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
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
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmCourseRecoveryPlan().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddGrade;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnAddMilestone;
    private javax.swing.JButton btnAddProgress;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClearForm;
    private javax.swing.JButton btnClearGrade;
    private javax.swing.JButton btnClearMilestoneForm;
    private javax.swing.JButton btnClearProgress;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDeletePlan;
    private javax.swing.JButton btnLoadSelectedPlans;
    private javax.swing.JButton btnNewPlan;
    private javax.swing.JButton btnRefreshPlans;
    private javax.swing.JButton btnRemoveItem;
    private javax.swing.JButton btnRemoveMilestone;
    private javax.swing.JButton btnSaveAll;
    private javax.swing.JButton btnUpdateItem;
    private javax.swing.JButton btnUpdateMilestone;
    private javax.swing.JCheckBox chkCompleted;
    private javax.swing.JComboBox<String> cmbItemType;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblAttempt;
    private javax.swing.JLabel lblCompletePercent;
    private javax.swing.JLabel lblComponentName;
    private javax.swing.JLabel lblGradeID;
    private javax.swing.JLabel lblItemID;
    private javax.swing.JLabel lblItemType;
    private javax.swing.JLabel lblLocation;
    private javax.swing.JLabel lblOverallCompletion;
    private javax.swing.JLabel lblProgressDate;
    private javax.swing.JLabel lblProgressID;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTabbedPane panelCentre;
    private javax.swing.JPanel panelFooter;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelItems;
    private javax.swing.JPanel panelMilestones;
    private javax.swing.JPanel panelProgressGrades;
    private javax.swing.JPanel pnlGrade;
    private javax.swing.JPanel pnlProgress;
    private javax.swing.JSpinner spnAttemptNumber;
    private javax.swing.JSpinner spnCompletionPercent;
    private javax.swing.JTable tblGrades;
    private javax.swing.JTable tblItems;
    private javax.swing.JTable tblMilestones;
    private javax.swing.JTable tblPlans;
    private javax.swing.JTable tblProgress;
    private javax.swing.JTextField txtCompName;
    private javax.swing.JTextField txtComponentName;
    private javax.swing.JTextField txtCourseCode;
    private javax.swing.JTextField txtDueDate;
    private javax.swing.JTextField txtGradeDate;
    private javax.swing.JTextField txtGradeID;
    private javax.swing.JTextField txtGradeLetter;
    private javax.swing.JTextField txtItemID;
    private javax.swing.JTextField txtItemTitle;
    private javax.swing.JTextField txtLecturerName;
    private javax.swing.JTextField txtLocation;
    private javax.swing.JTextField txtMarks;
    private javax.swing.JTextField txtMilestoneID;
    private javax.swing.JTextField txtMilestoneTask;
    private javax.swing.JTextField txtNotes;
    private javax.swing.JTextField txtPlanID;
    private javax.swing.JTextField txtProgressDate;
    private javax.swing.JTextField txtProgressID;
    private javax.swing.JTextField txtStudentID;
    private javax.swing.JTextField txtStudentName;
    private javax.swing.JTextField txtStudyWeek;
    // End of variables declaration//GEN-END:variables
}
