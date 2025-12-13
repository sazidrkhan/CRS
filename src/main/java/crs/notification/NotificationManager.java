/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.notification;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Paths;

// import crs.notification.NotificationLogger;
import crs.notification.data.StudentRecord;
import crs.notification.data.StudentTextDatabase;

// Coordinates student lookups, template rendering, and dispatch via MailSender for CRS.
public class NotificationManager {
    // Small abstraction to decouple the manager from the concrete mail sender
    // implementation (Dependency Inversion).
    public interface MailSender {
        void sendEmail(NotificationMessage emailMessage) throws Exception;

        void sendWithAttachment(NotificationMessage emailMessage) throws Exception;
    }

    private static final Logger LOGGER = NotificationLogger.getLogger();

    private final StudentTextDatabase studentDatabase;
    private final MailSender mailSender;

    public NotificationManager() {
        this(new StudentTextDatabase(), new EmailService());
    }

    // Main constructor for injecting test doubles or alternate mail senders.
    public NotificationManager(StudentTextDatabase studentDatabase, MailSender mailSender) {
        this.studentDatabase = studentDatabase;
        this.mailSender = mailSender;
    }

    // Sends a basic account-creation email.
    public void sendAccountNotification(String studentId) throws Exception {
        StudentRecord student = requireStudent(studentId);
        EmailMessage message = new EmailMessage(
                student.getEmail(),
                "Account Created",
                formatAccountBody(student),
                null);
        mailSender.sendEmail(message);
        log("Account notification sent to " + student.getEmail());
    }

    public void sendPasswordReset(String username) {
        try {
            StudentRecord student = requireStudentByUsername(username);
            EmailMessage message = new EmailMessage(
                    student.getEmail(),
                    "Password Reset",
                    formatPasswordResetBody(student),
                    null);
            mailSender.sendEmail(message);
            log("Password reset sent to " + student.getEmail());
        } catch (Exception ex) {
            log("Failed to send password reset for username '" + username + "': " + ex.getMessage());
            throw new IllegalArgumentException("Failed to send password reset: " + ex.getMessage(), ex);
        }
    }

    // Sends enrollment confirmation with optional details.
    public void sendEnrollmentNotification(String studentId, String details) {
        try {
            StudentRecord student = requireStudent(studentId);
            EmailMessage message = new EmailMessage(
                    student.getEmail(),
                    "Enrollment Confirmation",
                    formatEnrollmentBody(student, details),
                    null);
            mailSender.sendEmail(message);
            log("Enrollment notification sent to " + student.getEmail());
        } catch (Exception ex) {
            log("Failed to send enrollment notification for id '" + studentId + "': " + ex.getMessage());
            throw new IllegalArgumentException("Failed to send enrollment notification: " + ex.getMessage(), ex);
        }
    }

    // Sends recovery plan updates; no attachment or summary text needed.
    public void sendRecoveryPlanNotification(String studentId, String recoverySummary) throws Exception {
        // Backward-compatible wrapper without plan metadata.
        sendRecoveryPlanNotification(studentId, null, null, null);
    }

    // Sends recovery plan updates with plan metadata (plan ID, course ID, status).
    public void sendRecoveryPlanNotification(String studentId, String planId, String courseId, String planStatus)
            throws Exception {
        StudentRecord student = requireStudent(studentId);
        EmailMessage message = new EmailMessage(
                student.getEmail(),
                "Recovery Plan Update",
                formatRecoveryPlanBody(student, planId, courseId, planStatus),
                null);
        mailSender.sendEmail(message);
        log("Recovery plan notification sent to " + student.getEmail());
    }

    public void sendAcademicReportNotification(String studentId, String reportPath) throws Exception {
        StudentRecord student = requireStudent(studentId);
        String body = formatAcademicReportBody(student, reportPath);
        EmailMessage message = new EmailMessage(
                student.getEmail(),
                "Academic Report",
                body,
                reportPath);
        mailSender.sendWithAttachment(message);
        log("Academic report sent to " + student.getEmail());
    }

    // Sends an email summarizing which profile fields were updated.
    public void sendDetailsUpdatedNotification(String studentId) throws Exception {
        StudentRecord student = requireStudent(studentId);
        String body = formatDetailsUpdatedBody(student);
        EmailMessage message = new EmailMessage(
                student.getEmail(),
                "Account Details Updated",
                body,
                null);
        mailSender.sendEmail(message);
        log("Details update notification sent to " + student.getEmail());
    }

    // Sends a manually composed email to any recipient, with an optional
    // attachment.
    public void sendCustomEmail(String recipient, String subject, String body, String attachmentPath) throws Exception {
        if (recipient == null || recipient.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient email is required");
        }

        String trimmedRecipient = recipient.trim();
        String safeSubject = subject == null ? "" : subject;
        String safeBody = body == null ? "" : body;
        String safeAttachment = (attachmentPath == null || attachmentPath.trim().isEmpty())
                ? null
                : attachmentPath.trim();

        EmailMessage message = new EmailMessage(trimmedRecipient, safeSubject, safeBody, safeAttachment);

        if (safeAttachment == null) {
            mailSender.sendEmail(message);
        } else {
            mailSender.sendWithAttachment(message);
        }

        log("Manual email sent to " + trimmedRecipient);
    }

    // Centralized logging hook for this manager.
    public void log(String message) {
        LOGGER.log(Level.INFO, message);
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    // Backwards-compat accessor if callers still expect the concrete type.
    public EmailService getEmailService() {
        return mailSender instanceof EmailService ? (EmailService) mailSender : null;
    }

    // Fetches a student by id or throws a meaningful error to callers.
    private StudentRecord requireStudent(String studentId) throws Exception {
        StudentRecord student = studentDatabase.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }
        return student;
    }

    // Looks up by username, then falls back to id for compatibility.
    private StudentRecord requireStudentByUsername(String username) throws Exception {
        StudentRecord student = studentDatabase.findByUsername(username);
        if (student == null) {
            // Fallback: some callers may still pass an ID; try that too.
            student = studentDatabase.findById(username);
        }
        if (student == null) {
            throw new IllegalArgumentException("Student not found for username: " + username);
        }
        return student;
    }

    // Chooses the best available name for email greeting.
    private String nameFor(StudentRecord student) {
        String full = student.getFullName();
        if (full != null && !full.trim().isEmpty()) {
            return full;
        }
        return student.getFirstName() != null ? student.getFirstName() : "student";
    }

    // --- Email templates ---

    private String formatAccountBody(StudentRecord student) {
        String program = sanitizeProgram(student.getProgram());
        String username = safe(firstNonBlank(student.getUsername(), student.getId()));
        String email = safe(student.getEmail());
        return "Hello " + nameFor(student) + ",\n\n" +
                "Your CRS account is now active. You can sign in and start using the portal.\n\n" +
                "\tStudent ID  : " + safe(student.getId()) + "\n" +
                "\tUsername    : " + username + "\n" +
                "\tEmail       : " + email + "\n" +
                "\tProgram     : " + program + "\n" +
                "\tYear        : " + safe(student.getYear()) + "\n\n" +
                "If you did not request this account, please contact the academic office.";
    }

    private String formatPasswordResetBody(StudentRecord student) {
        return "Hello " + nameFor(student) + ",\n\n" +
                "We have successfully completed a password reset for your CRS account with the following details.\n\n" +
                "\tUsername : " + safe(firstNonBlank(student.getUsername(), student.getId())) + "\n" +
                "\tEmail       : " + safe(student.getEmail()) + "\n\n" +
                "If you did not request this change, please contact the academic office immediately.";
    }

    private String formatEnrollmentBody(StudentRecord student, String details) {
        String detailLine = (details == null || details.trim().isEmpty())
                ? "\tDetails     : Not provided"
                : "\tDetails     : " + details.trim();
        return "Hello " + nameFor(student) + ",\n\n" +
                "You have been enrolled successfully.\n\n" +
                "\tStudent ID  : " + safe(student.getId()) + "\n" +
                "\tProgram     : " + safe(student.getProgram()) + "\n" +
                "\tYear        : " + safe(student.getYear()) + "\n" +
                detailLine + "\n\n" +
                "If anything looks incorrect, reply to this email or contact the academic office.";
    }

    private String formatRecoveryPlanBody(StudentRecord student, String planId, String courseId,
            String planStatus) {
        return "Hello " + nameFor(student) + ",\n\n" +
                "Here is your latest recovery plan update:\n\n" +
                "\tPlan ID    : " + safe(planId) + "\n" +
                "\tStudent ID : " + safe(student.getId()) + "\n" +
                "\tCourse ID  : " + safe(courseId) + "\n" +
                "\tStatus     : " + safe(planStatus) + "\n" +
                "\tProgram    : " + safe(student.getProgram()) + "\n" +
                "\tYear       : " + safe(student.getYear()) + "\n\n" +
                "Please review and follow the outlined steps.";
    }

    private String formatAcademicReportBody(StudentRecord student, String reportPath) {
        String fileName = reportPath == null ? "the attached report" : Paths.get(reportPath).getFileName().toString();
        return "Hello " + nameFor(student) + ",\n\n" +
                "Your academic report is attached (" + fileName + ").\n\n" +
                "\tStudent ID : " + safe(student.getId()) + "\n" +
                "\tProgram    : " + safe(student.getProgram()) + "\n" +
                "\tYear       : " + safe(student.getYear()) + "\n\n" +
                "If the attachment is missing or unreadable, please let us know.";
    }

    private String formatDetailsUpdatedBody(StudentRecord student) {
        return "Hello " + nameFor(student) + ",\n\n" +
                "Your CRS account details were updated as requested.\n\n" +
                "If you did not request these changes, please contact the academic office immediately.";
    }

    private String safe(String value) {
        return (value == null || value.trim().isEmpty()) ? "N/A" : value.trim();
    }

    private String firstNonBlank(String first, String fallback) {
        if (first != null && !first.trim().isEmpty()) {
            return first.trim();
        }
        return fallback == null ? "" : fallback.trim();
    }

    // mergeForPreview removed: generic update notice uses stored record.

    // Some data sources leak hashed tokens into the program field; mask them for
    // users.
    private String sanitizeProgram(String programRaw) {
        String value = safe(programRaw);
        if (value.matches("(?i)[0-9a-f]{32,}")) {
            return "N/A";
        }
        return value;
    }

    public StudentTextDatabase getStudentDatabase() {
        return studentDatabase;
    }
}
