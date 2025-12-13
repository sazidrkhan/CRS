/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.notification;

/**
 *
 * @author Sazid R Khan
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import crs.notification.data.StudentRecord;
import crs.notification.data.StudentTextDatabase;

import javax.swing.filechooser.FileSystemView;
import java.io.IOException;

// Swing front-end to drive NotificationManager: search students, prefill email
// templates, and send automated/manual messages.
public class EmailGUI extends JFrame {
    private static final String KEY_ACTIVE = "placeholderActive";
    private static final String KEY_NORMAL_COLOR = "placeholderNormalColor";
    private static final Color BG_COLOR = new Color(255, 204, 153);

    private final NotificationManager notificationManager;
    private final StudentTextDatabase studentDatabase;

    // Automated section components
    private JTextField studentIdField;
    private JTextField autoEmailField;
    private JTextField autoSubjectField;
    private JTextArea autoMessageArea;
    private JTextField autoAttachmentField;

    // Logging
    private JTextArea logArea;

    public EmailGUI() {
        super("Email Notification Module");
        this.notificationManager = new NotificationManager();
        this.studentDatabase = notificationManager.getStudentDatabase();
        initComponents();
        activatePlaceholder(autoSubjectField, "Subject line");
        activatePlaceholder(autoMessageArea, "Type your message...");
    }

    // Builds and lays out all Swing controls for automated and manual sending
    // flows.
    private void initComponents() {
        // Form controls
        JLabel lblStudent = new JLabel("Student ID:");
        studentIdField = new JTextField();
        JButton btnFind = new JButton("Search");
        btnFind.addActionListener(this::onSearchStudent);
        studentIdField.addActionListener(this::onSearchStudent);

        JLabel lblEmail = new JLabel("Email:");
        autoEmailField = new JTextField();

        JLabel lblSubject = new JLabel("Subject:");
        autoSubjectField = new JTextField();
        installPlaceholder(autoSubjectField, "Subject line");

        JLabel lblMessage = new JLabel("Message:");
        autoMessageArea = new JTextArea(5, 20);
        autoMessageArea.setLineWrap(true);
        autoMessageArea.setWrapStyleWord(true);
        installPlaceholder(autoMessageArea, "Type your message...");
        JScrollPane scrollAutoMsg = new JScrollPane(autoMessageArea);

        JLabel lblAttach = new JLabel("Attachment (optional):");
        autoAttachmentField = new JTextField();
        JButton btnBrowse = new JButton("Browse...");
        btnBrowse.addActionListener(this::onBrowseAuto);

        JButton btnAccount = new JButton("Send Account Notification");
        btnAccount.addActionListener(this::onSendAccount);
        JButton btnReset = new JButton("Send Password Reset");
        btnReset.addActionListener(this::onSendReset);
        JButton btnRecovery = new JButton("Send Recovery Plan Notification");
        btnRecovery.addActionListener(this::onSendRecovery);
        JButton btnReport = new JButton("Send Academic Report");
        btnReport.addActionListener(this::onSendReport);
        JButton btnEnrollment = new JButton("Send Enrollment Notification");
        btnEnrollment.addActionListener(this::onSendEnrollment);
        JButton btnManual = new JButton("Send Manual Email");
        btnManual.addActionListener(this::onSendManual);

        JButton btnBack = new JButton("Back to Admin Dashboard");
        btnBack.addActionListener(this::onBackToAdmin);

        configureButton(btnAccount);
        configureButton(btnReset);
        configureButton(btnRecovery);
        configureButton(btnReport);
        configureButton(btnEnrollment);
        configureButton(btnManual);
        configureButton(btnBack);
        fitButtonToText(btnBack, 40, 14);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        buttonsPanel.setBackground(BG_COLOR);
        buttonsPanel.add(btnAccount);
        buttonsPanel.add(btnEnrollment);
        buttonsPanel.add(btnReset);
        buttonsPanel.add(btnRecovery);
        buttonsPanel.add(btnReport);
        buttonsPanel.add(btnManual);
        sizeButtonGrid(buttonsPanel, btnAccount, btnEnrollment, btnReset, btnRecovery, btnReport, btnManual);

        // Logs
        JLabel lblLogs = new JLabel("Logs:");
        logArea = new JTextArea(6, 20);
        logArea.setEditable(false);
        logArea.setFocusable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane scrollLogs = new JScrollPane(logArea);

        // Form panel (top) using GroupLayout
        JPanel formPanel = new JPanel();
        formPanel.setBackground(BG_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        GroupLayout formLayout = new GroupLayout(formPanel);
        formPanel.setLayout(formLayout);
        formLayout.setAutoCreateGaps(true);
        formLayout.setAutoCreateContainerGaps(true);

        formLayout.setHorizontalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(formLayout.createSequentialGroup()
                                .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblStudent)
                                        .addComponent(lblEmail)
                                        .addComponent(lblSubject)
                                        .addComponent(lblMessage)
                                        .addComponent(lblAttach))
                                .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(formLayout.createSequentialGroup()
                                                .addComponent(studentIdField)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(btnFind, GroupLayout.PREFERRED_SIZE, 90,
                                                        GroupLayout.PREFERRED_SIZE))
                                        .addComponent(autoEmailField)
                                        .addComponent(autoSubjectField)
                                        .addComponent(scrollAutoMsg)
                                        .addGroup(formLayout.createSequentialGroup()
                                                .addComponent(autoAttachmentField)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(btnBrowse))))
                        .addComponent(buttonsPanel, GroupLayout.Alignment.LEADING));

        formLayout.setVerticalGroup(
                formLayout.createSequentialGroup()
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblStudent)
                                .addComponent(studentIdField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnFind))
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblEmail)
                                .addComponent(autoEmailField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblSubject)
                                .addComponent(autoSubjectField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lblMessage)
                                .addComponent(scrollAutoMsg, GroupLayout.PREFERRED_SIZE, 120,
                                        GroupLayout.PREFERRED_SIZE))
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblAttach)
                                .addComponent(autoAttachmentField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnBrowse))
                        .addComponent(buttonsPanel));

        // Log panel (bottom, fixed height via preferred size on scroll pane)
        JPanel logPanel = new JPanel(new BorderLayout(0, 6));
        logPanel.setBorder(BorderFactory.createEmptyBorder(6, 12, 12, 12));
        logPanel.setBackground(BG_COLOR);
        scrollLogs.setPreferredSize(new Dimension(100, 140));
        logPanel.add(lblLogs, BorderLayout.NORTH);
        logPanel.add(scrollLogs, BorderLayout.CENTER);

        JPanel backPanel = new JPanel();
        backPanel.setBackground(BG_COLOR);
        backPanel.add(btnBack);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG_COLOR);
        content.add(formPanel, BorderLayout.CENTER);
        content.add(logPanel, BorderLayout.SOUTH);
        content.add(backPanel, BorderLayout.NORTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(content);
        setPreferredSize(new Dimension(780, 640));
        pack();
        setLocationRelativeTo(null);
    }

    private void onSearchStudent(ActionEvent e) {
        String id = studentIdField.getText() == null ? "" : studentIdField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a student ID to search.", "Missing ID",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        StudentRecord student = studentDatabase.findById(id);
        if (student == null) {
            autoEmailField.setText("");
            activatePlaceholder(autoSubjectField, "Subject line");
            activatePlaceholder(autoMessageArea, "Type your message...");
            JOptionPane.showMessageDialog(this, "Student not found: " + id, "Not found",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        autoEmailField.setText(student.getEmail());
        activatePlaceholder(autoSubjectField, "Subject line");
        activatePlaceholder(autoMessageArea, "Type your message...");
        appendLog("Loaded student " + student.getId());
    }

    private void onBrowseAuto(ActionEvent e) {
        JFileChooser chooser = createProjectRootChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
            File selected = chooser.getSelectedFile();
            autoAttachmentField.setText(selected.getAbsolutePath());
        }
    }

    private void onSendAccount(ActionEvent e) {
        String id = studentIdField.getText().trim();
        sendWithDialog(() -> notificationManager.sendAccountNotification(id), "Account notification sent for " + id);
    }

    private void onSendReset(ActionEvent e) {
        String id = studentIdField.getText().trim();
        sendWithDialog(() -> notificationManager.sendPasswordReset(id), "Password reset sent for " + id);
    }

    private void onSendRecovery(ActionEvent e) {
        String id = studentIdField.getText().trim();
        String summary = getActualText(autoMessageArea);
        sendWithDialog(() -> notificationManager.sendRecoveryPlanNotification(id, summary),
                "Recovery plan sent for " + id);
    }

    private void onSendReport(ActionEvent e) {
        String id = studentIdField.getText().trim();
        String path = autoAttachmentField.getText().trim();
        sendWithDialog(() -> notificationManager.sendAcademicReportNotification(id, path),
                "Academic report sent for " + id);
    }

    private void onSendEnrollment(ActionEvent e) {
        String id = studentIdField.getText().trim();
        String details = getActualText(autoMessageArea);
        sendWithDialog(() -> notificationManager.sendEnrollmentNotification(id, details),
                "Enrollment notification sent for " + id);
    }

    private void onSendManual(ActionEvent e) {
        String recipient = autoEmailField.getText().trim();
        String subject = getActualText(autoSubjectField);
        String body = getActualText(autoMessageArea);
        String attachment = autoAttachmentField.getText().trim();

        String successMessage = recipient.isEmpty() ? "Manual email sent" : "Manual email sent to " + recipient;

        sendWithDialog(
                () -> notificationManager.sendCustomEmail(
                        recipient,
                        subject,
                        body,
                        attachment.isEmpty() ? null : attachment),
                successMessage);
    }

    private void sendWithDialog(ThrowingRunnable action, String successMessage) {
        try {
            action.run();
            appendLog(successMessage);
        } catch (Exception ex) {
            appendLog("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void appendLog(String text) {
        logArea.append(text + "\n");
    }

    private void configureButton(JButton button) {
        Dimension pref = button.getPreferredSize();
        button.setPreferredSize(pref);
        button.setMaximumSize(pref);
        button.setMinimumSize(pref);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // Ensures a single button has enough width/height to show its full label.
    private void fitButtonToText(JButton button, int padWidth, int padHeight) {
        FontMetrics metrics = button.getFontMetrics(button.getFont());
        int textWidth = metrics.stringWidth(button.getText());
        int textHeight = metrics.getHeight();
        Dimension target = new Dimension(textWidth + padWidth,
                Math.max(textHeight + padHeight, button.getPreferredSize().height));
        button.setPreferredSize(target);
        button.setMinimumSize(target);
        button.setMaximumSize(target);
    }

    // Sizes the grid to fit its buttons based on their preferred sizes and layout
    // gaps.
    private void sizeButtonGrid(JPanel panel, JButton... buttons) {
        int cols = ((GridLayout) panel.getLayout()).getColumns();
        int rows = ((GridLayout) panel.getLayout()).getRows();
        int hgap = ((GridLayout) panel.getLayout()).getHgap();
        int vgap = ((GridLayout) panel.getLayout()).getVgap();

        int maxW = 0;
        int maxH = 0;
        for (JButton b : buttons) {
            Dimension d = b.getPreferredSize();
            maxW = Math.max(maxW, d.width + 20); // add padding
            maxH = Math.max(maxH, d.height + 12);
        }

        int width = cols * maxW + (cols - 1) * hgap;
        int height = rows * maxH + (rows - 1) * vgap;
        Dimension gridSize = new Dimension(width, height);
        panel.setPreferredSize(gridSize);
        panel.setMinimumSize(gridSize);
        panel.setMaximumSize(gridSize);
    }

    private JFileChooser createProjectRootChooser() {
        Path root = Paths.get("").toAbsolutePath().normalize();
        File rootFile = root.toFile();
        FileSystemView view = new SingleRootFileSystemView(rootFile);
        JFileChooser chooser = new JFileChooser(view);
        chooser.setCurrentDirectory(rootFile);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setAcceptAllFileFilterUsed(true);
        return chooser;
    }

    private void onBackToAdmin(ActionEvent e) {
        try {
            new crs.ui.CourseAdminDashboard().setVisible(true);
        } catch (Exception ex) {
            appendLog("Unable to open Admin Dashboard: " + ex.getMessage());
            return;
        }
        this.dispose();
    }

    // Placeholder behavior for any text component. Hint text shows only when empty
    // and not focused.
    private void installPlaceholder(JTextComponent field, String placeholder) {
        final java.awt.Color normalColor = field.getForeground();
        field.putClientProperty(KEY_NORMAL_COLOR, normalColor);
        field.putClientProperty("placeholderText", placeholder);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Boolean.TRUE.equals(field.getClientProperty(KEY_ACTIVE))) {
                    field.putClientProperty(KEY_ACTIVE, Boolean.FALSE);
                    field.setForeground(normalColor);
                    field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    activatePlaceholder(field, placeholder);
                }
            }
        });

        activatePlaceholder(field, placeholder);
    }

    private void activatePlaceholder(JTextComponent field, String placeholder) {
        Object normal = field.getClientProperty(KEY_NORMAL_COLOR);
        java.awt.Color normalColor = normal instanceof java.awt.Color ? (java.awt.Color) normal : field.getForeground();
        field.putClientProperty(KEY_ACTIVE, Boolean.TRUE);
        field.setForeground(normalColor.darker());
        field.setText(placeholder);
    }

    private String getActualText(JTextComponent field) {
        if (Boolean.TRUE.equals(field.getClientProperty(KEY_ACTIVE))) {
            return "";
        }
        return field.getText() == null ? "" : field.getText();
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmailGUI().setVisible(true));
    }

    // Simple FileSystemView that restricts navigation to a single root directory.
    private static class SingleRootFileSystemView extends FileSystemView {
        private final File root;

        SingleRootFileSystemView(File root) {
            this.root = root;
        }

        @Override
        public File createNewFolder(File containingDir) throws IOException {
            File folder = new File(containingDir, "New Folder");
            if (!folder.mkdir()) {
                throw new IOException("Failed to create folder");
            }
            return folder;
        }

        @Override
        public File getDefaultDirectory() {
            return root;
        }

        @Override
        public File getHomeDirectory() {
            return root;
        }

        @Override
        public File[] getRoots() {
            return new File[] { root };
        }

        @Override
        public boolean isRoot(File file) {
            return root.equals(file);
        }

        @Override
        public File getParentDirectory(File dir) {
            if (dir == null) {
                return null;
            }
            if (root.equals(dir)) {
                return root;
            }
            File parent = dir.getParentFile();
            if (parent == null) {
                return root;
            }
            if (!parent.toPath().normalize().startsWith(root.toPath().normalize())) {
                return root;
            }
            return parent;
        }
    }
}
