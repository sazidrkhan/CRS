/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crs.notification;

/**
 *
 * @author Sazid R Khan
 */

// Email-specific notification that adds an optional attachment path to the
// common
// notification payload.
public class EmailMessage extends NotificationMessage {
    private final String attachmentPath;

    // Primary constructor capturing the entire payload at creation time.
    public EmailMessage(String recipient, String subject, String body, String attachmentPath) {
        super(recipient, subject, body);
        this.attachmentPath = attachmentPath;
    }

    @Override
    public String getAttachmentPath() {
        return attachmentPath;
    }
}
