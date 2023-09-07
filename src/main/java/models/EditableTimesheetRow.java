package models;

import java.io.Serializable;

/**
 * Represents an editable row in a timesheet. Each editable row contains
 * a reference to a row in a timesheet and an editable flag.
 * 
 * @author The Bug Busters
 * @version 1
 */
public class EditableTimesheetRow implements Serializable {

    /** Row in timesheet. */
    private TimesheetRow timesheetRow;

    /** Editable flag. */
    private boolean editable;

    /**
     * Default Constructor.
     * Initializes editable flag.
     */
    public EditableTimesheetRow() {
        this.editable = false;
    }

    /**
     * Parameter Constructor (TimesheetRow).
     * Initializes editable flag and timesheet row.
     * 
     * @param timesheetRow the timesheet row
     */
    public EditableTimesheetRow(TimesheetRow timesheetRow) {
        this.timesheetRow = timesheetRow;
        this.editable = false;
    }

    /**
     * Parameter Constructor (Timesheet, boolean).
     * Initializes editable flag and timesheet row.
     * 
     * @param timesheet the timesheet
     * @param editable the editable flag
     */
    public EditableTimesheetRow(Timesheet timesheet, boolean editable) {
        this.timesheetRow = new TimesheetRow();
        this.timesheetRow.setTimesheet(timesheet);
        this.editable = editable;
    }

    /**
     * Gets the total hours in the timesheet row.
     * 
     * @return the total hours in the timesheet row
     */
    public float getTotalHours() {
        return timesheetRow.getSatHours() 
                + timesheetRow.getSunHours() 
                + timesheetRow.getMonHours() 
                + timesheetRow.getTueHours() 
                + timesheetRow.getWedHours() 
                + timesheetRow.getThuHours() 
                + timesheetRow.getFriHours();
    }

    /**
     * Gets the timesheet row.
     * 
     * @return the timesheet row
     */
    public TimesheetRow getTimesheetRow() {
        return timesheetRow;
    }

    /**
     * Sets the timesheet row.
     * 
     * @param timesheetRow the timesheet row
     */
    public void setTimesheetRow(TimesheetRow timesheetRow) {
        this.timesheetRow = timesheetRow;
    }

    /**
     * Gets the editable flag.
     * 
     * @return the editable flag
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets the editable flag.
     * 
     * @param editable the editable flag
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
