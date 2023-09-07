package controllers;

import access.TimesheetManager;
import access.TimesheetRowManager;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.Employee;
import models.Timesheet;
import models.TimesheetRow;
import models.TimesheetStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * This class is the controller class
 * for managing timesheet approvers' details in the system.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("tsadController")
@SessionScoped
public class TimesheetApproverDetailController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Class Manager. */
    @Inject
    private TimesheetRowManager tsrm;

    /** Class Manager. */
    @Inject
    private TimesheetManager tsm;

    /** Selected Timesheet. */
    private Timesheet selectedTimesheet;

    /** List of Timesheet Rows. */
    private List<TimesheetRow> timesheetRows;

    /**
     * Selects timesheet to navigate to.
     * 
     * @param timesheet timesheet to navigate to
     * @return page navigation string
     */
    public String selectTimesheetAndNavigateToDetail(Timesheet timesheet) {
        setSelectedTimesheet(timesheet);

        List<TimesheetRow> timesheetRowList = 
            tsrm.findByTimesheetId(selectedTimesheet.getId());
        setTimesheetRows(timesheetRowList);

        return "timesheet-approver-detail?faces-redirect=true";
    }

    /**
     * Sets timesheet's approver details and approves timesheet.
     * 
     * @param employee employee approving timesheet
     */
    public void approve(Employee employee) {
        selectedTimesheet.setApprover(employee);
        selectedTimesheet.setApproverName(employee.getEmpName());
        selectedTimesheet.setResponseDate(LocalDate.now());
        tsm.updateTimesheetStatus(selectedTimesheet,
                TimesheetStatus.APPROVED.getStatus());
    }

    /**
     * Sets timesheet's approver details and rejects timesheet.
     * 
     * @param employee employee rejecting timesheet
     */
    public void reject(Employee employee) {
        selectedTimesheet.setApprover(employee);
        selectedTimesheet.setApproverName(employee.getEmpName());
        selectedTimesheet.setResponseDate(LocalDate.now());
        tsm.updateTimesheetStatus(selectedTimesheet,
                TimesheetStatus.REJECTED.getStatus());
    }

    /**
     * Checks if timesheet has been approved or rejected.
     * 
     * @return true if timesheet has been approved or rejected,
     *         false otherwise
     */
    public boolean renderIfApprovedOrRejected() {
        return (selectedTimesheet.getStatus().equals(
            TimesheetStatus.REJECTED.getStatus())
                || selectedTimesheet.getStatus().equals(
                    TimesheetStatus.APPROVED.getStatus()));
    }

    /**
     * Checks if timesheet has "Pending" status.
     * 
     * @return true if timesheet status is set to "Pending",
     *         false otherwise
     */
    public boolean renderIfPending() {
        return (selectedTimesheet.getStatus().equals(
            TimesheetStatus.PENDING.getStatus()));
    }

    /**
     * Gets the total hours entered for Saturdays on timesheet.
     * 
     * @return total hours entered for Saturdays on timesheet
     */
    public float getSatSum() {
        float sum = 0;
        for (TimesheetRow tsr : timesheetRows) {
            sum += tsr.getSatHours();
        }

        return sum;
    }

    /**
     * Gets the total hours entered for Sundays on timesheet.
     * 
     * @return total hours entered for Sundays on timesheet
     */
    public float getSunSum() {
        float sum = 0;
        for (TimesheetRow tsr : timesheetRows) {
            sum += tsr.getSunHours();
        }

        return sum;
    }

    /**
     * Gets the total hours entered for Mondays on timesheet.
     * 
     * @return total hours entered for Mondays on timesheet
     */
    public float getMonSum() {
        float sum = 0;
        for (TimesheetRow tsr : timesheetRows) {
            sum += tsr.getMonHours();
        }

        return sum;
    }

    /**
     * Gets the total hours entered for Tuesdays on timesheet.
     * 
     * @return total hours entered for Tuesdays on timesheet
     */
    public float getTueSum() {
        float sum = 0;
        for (TimesheetRow tsr : timesheetRows) {
            sum += tsr.getTueHours();
        }

        return sum;
    }

    /**
     * Gets the total hours entered for Wednesdays on timesheet.
     * 
     * @return total hours entered for Wednesdays on timesheet
     */
    public float getWedSum() {
        float sum = 0;
        for (TimesheetRow tsr : timesheetRows) {
            sum += tsr.getWedHours();
        }

        return sum;
    }

    /**
     * Gets the total hours entered for Thursdays on timesheet.
     * 
     * @return total hours entered for Thursdays on timesheet
     */
    public float getThuSum() {
        float sum = 0;
        for (TimesheetRow tsr : timesheetRows) {
            sum += tsr.getThuHours();
        }

        return sum;
    }

    /**
     * Gets the total hours entered for Fridays on timesheet.
     * 
     * @return total hours entered for Fridays on timesheet
     */
    public float getFriSum() {
        float sum = 0;
        for (TimesheetRow tsr : timesheetRows) {
            sum += tsr.getFriHours();
        }

        return sum;
    }

    /**
     * Gets the total hours entered on timesheet.
     * 
     * @return total hours entered on timesheet
     */
    public float getSum() {
        return (getSatSum() + getSunSum() + getMonSum()
                + getTueSum() + getWedSum() + getThuSum() + getFriSum());
    }

    /**
     * Sets the {@code selectedTimesheet} to null before destroying instance.
     */
    @PreDestroy
    public void destroy() {
        selectedTimesheet = null;
    }

    /**
     * Gets selected timesheet.
     * 
     * @return selected timesheet
     */
    public Timesheet getSelectedTimesheet() {
        return selectedTimesheet;
    }

    /**
     * Sets selected timesheet.
     * 
     * @param selectedTimesheet selected timesheet
     */
    public void setSelectedTimesheet(Timesheet selectedTimesheet) {
        this.selectedTimesheet = selectedTimesheet;
    }

    /**
     * Gets list of timesheet's rows.
     * 
     * @return list of timesheet's rows
     */
    public List<TimesheetRow> getTimesheetRows() {
        return this.timesheetRows;
    }

    /**
     * Sets list of timesheet's rows.
     * 
     * @param timesheetRows list of timesheet's rows
     */
    public void setTimesheetRows(List<TimesheetRow> timesheetRows) {
        this.timesheetRows = timesheetRows;
    }
}
