package models;

/**
 * This enum represents the status of a timesheet in the system.
 * 
 * @author The Bug Busters
 * @version 1
 */
public enum TimesheetStatus {

    /** Draft (Not yet submitted). */
    DRAFT("Draft"),

    /** Submitted and Pending Review. */
    PENDING("Pending"),

    /** Rejected by Timesheet Approver. */
    REJECTED("Rejected"),

    /** Approved by Timesheet Approver. */
    APPROVED("Approved");

    /** Current Status. */
    private final String status;

    /**
     * Paramter Constructor.
     * Initializes status.
     * 
     * @param status the status
     */
    TimesheetStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the timesheet's status.
     * 
     * @return the timesheet's status
     */
    public String getStatus() {
        return this.status;
    }
}
