package controllers;

import access.TimesheetManager;
import access.TimesheetRowManager;
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
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the controller class for managing timesheets in the system.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("tsController")
@SessionScoped
public class TimesheetController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Class Manager. */
    @Inject
    private TimesheetManager tsm;

    /** Class Manager. */
    @Inject
    private TimesheetRowManager tsrm;

    /** Timesheet's end date. */
    private LocalDate endDate;

    /**
     * Get all timesheets for the given employee.
     * 
     * @param empNumber the employee's number
     * @return list of timesheets for the given employee
     */
    public List<Timesheet> getTimesheetsByEmpNumber(long empNumber) {
        return tsm.findByEmpNumber(empNumber);
    }

    /**
     * Adds timesheet into the system.
     * 
     * @param employee timesheet owner
     */
    public void addTimesheet(Employee employee) {
        Timesheet timesheet = new Timesheet();

        timesheet.setEmployee(employee);
        timesheet.setEmpName(employee.getEmpName());
        timesheet.setStatus(TimesheetStatus.DRAFT.getStatus());
        timesheet.setEndDate(endDate);

        tsm.addTimesheet(timesheet);
    }

    /**
     * Delete timesheet from the system.
     * 
     * @param timesheet the timesheet
     */
    public void delete(Timesheet timesheet) {
        tsm.deleteTimesheet(timesheet);
    }

    /**
     * Gets list of disabled days.
     * 
     * @return list of disabled days
     */
    public List<Integer> getDisabledDays() {
        final Integer SUNDAY = 0;
        final Integer MONDAY = 1;
        final Integer TUESDAY = 2;
        final Integer WEDNESDAY = 3;
        final Integer THURSDAY = 4;
        final Integer SATURDAY = 6;

        List<Integer> list = new ArrayList<>();
        list.add(SUNDAY);
        list.add(MONDAY);
        list.add(TUESDAY);
        list.add(WEDNESDAY);
        list.add(THURSDAY);
        list.add(SATURDAY);

        return list;
    }

    /**
     * Gets list of disabled dates for a given employee.
     * 
     * @param employee the employee
     * @return list of disabled dates for a given employee
     */
    public List<LocalDate> getDisabledDates(Employee employee) {
        List<LocalDate> disabledDates = new ArrayList<>();

        long empNumber = employee.getEmpNumber();
        List<Timesheet> timesheetList = tsm.findByEmpNumber(empNumber);

        for (Timesheet ts : timesheetList) {
            disabledDates.add(ts.getEndDate());
        }

        return disabledDates;
    }

    /**
     * Gets total hours for a given timesheet.
     * 
     * @param timesheet the timesheet
     * @return total hours for a given timesheet
     */
    public float getTotalHours(Timesheet timesheet) {
        float totalHours = 0;

        List<TimesheetRow> timesheetRowList = 
            tsrm.findByTimesheetId(timesheet.getId());
        for (TimesheetRow tsr : timesheetRowList) {
            totalHours += tsr.getTotalHours();
        }

        return totalHours;
    }

    /**
     * Checks if timesheet has "Draft" or "Rejected" status.
     * 
     * @param timesheet the timesheet
     * @return true if timesheet has "Draft" or "Rejected" status,
     *         false otherwise
     */
    public boolean renderIfDraftOrRejected(Timesheet timesheet) {
        return (timesheet.getStatus().equals(TimesheetStatus.DRAFT.getStatus())
                || timesheet.getStatus().equals(
                    TimesheetStatus.REJECTED.getStatus()));
    }

    /**
     * Gets timesheet's end date.
     * 
     * @return timesheet's end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets timesheet's end date.
     * 
     * @param endDate timesheet's end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
