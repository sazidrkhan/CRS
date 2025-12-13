/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package crs;
import javax.swing.*;
import java.awt.*;

import crs.EligibilityAndEnrollmentFrames.MainForm;
import crs.recovery.gui.FrmCourseRecoveryPlan;              // recovery GUI
import crs.reporting.ui.FrmAcademicReport; // reporting GUI (your .form JFrame)
import crs.reporting.ui.AcademicReportPanel;

public class Crs extends JFrame {

    public Crs() {
        setTitle("CRS Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 220);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Select a module to launch:");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        root.add(title, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton btnEligibility = new JButton("Eligibility & Enrollment");
        JButton btnRecovery = new JButton("Course Recovery Plan");
        JButton btnReporting = new JButton("Academic Reporting");

        buttons.add(btnEligibility);
        buttons.add(btnRecovery);
        buttons.add(btnReporting);

        root.add(buttons, BorderLayout.CENTER);

        // ===== Actions =====

        btnEligibility.addActionListener(e -> {
            new MainForm().setVisible(true);
            dispose(); // close launcher
        });

        btnRecovery.addActionListener(e -> {
            new FrmCourseRecoveryPlan().setVisible(true);
            dispose();
        });

        btnReporting.addActionListener(e -> {
    try {
        // 1) Create a window to host the JPanel form
        JFrame f = new JFrame("CRS – Academic Reporting");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 2) Put your JPanel form inside the frame
        f.setContentPane(new crs.reporting.ui.FrmAcademicReport());

        // 3) Size the window nicely
        f.pack();                      // uses preferred sizes from the form
        f.setSize(900, 600);           // optional: force same size as your reporting UI
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        // 4) Now it’s safe to close the launcher
        dispose();

    } 
    catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
                this,
                "Failed to open Academic Reporting:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
});


        setContentPane(root);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Crs().setVisible(true));
    }
}
