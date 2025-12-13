package crs.notification;

// Base notification payload capturing common fields for any outbound message.
// Subclasses add channel-specific attributes (e.g., attachments for email).
public abstract class NotificationMessage {
    private final String recipient;
    private final String subject;
    private final String body;

    protected NotificationMessage(String recipient, String subject, String body) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    // Allows subclasses to expose attachments or other channel-specific payloads.
    public abstract String getAttachmentPath();

    public boolean hasAttachment() {
        String path = getAttachmentPath();
        return path != null && !path.trim().isEmpty();
    }
}
