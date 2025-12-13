package crs.recovery.model;

/**
 * Represents the status of a course recovery plan.
 * <p>
 * This enum defines the possible states that a recovery plan can be in
 * throughout its lifecycle, from initial planning to completion or cancellation.
 * </p>
 *
 * @author Sazid R Khan
 */
public enum RecoveryStatus {
    
    /** The recovery plan has been created but not yet started. */
    PLANNED,
    
    /** The recovery plan is currently being executed. */
    IN_PROGRESS,
    
    /** The recovery plan has been successfully completed. */
    COMPLETED,
    
    /** The recovery plan has been cancelled and will not be completed. */
    CANCELLED
}
