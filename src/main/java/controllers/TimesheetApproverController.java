package controllers;

import access.TimesheetManager;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.Employee;
import models.Timesheet;
import models.TimesheetStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the controller class 
 * for managing timesheet approvers in the system.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("tsaController")
@SessionScoped
public class TimesheetApproverController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Class Manager. */
    @Inject
    private TimesheetManager tsm;

    /**
     * Gets suitable timesheets for approval for the given employee.
     * 
     * @param employee the employee
     * @return list of suitable timesheets for approval for the given employee
     */
    public List<Timesheet> getTimesheetsToApprove(Employee employee) {
        List<Timesheet> timesheetList = new ArrayList<>();

        if (employee.getEmpType().equals("Admin")) {
            // in case of admin, see all timesheets
            timesheetList.addAll(tsm.findAll());
        } else if (employee.getEmpType().equals("PM")) {
            // in case of pm, see supervisees' timesheets
            for (Employee supervisee : employee.getSupervisees()) {
                timesheetList.addAll(supervisee.getTimesheets());
            }
        } else {
            // in case of employee and hr, see approvees' timesheets
            for (Employee approvee : employee.getApprovees()) {
                timesheetList.addAll(approvee.getTimesheets());
            }
        }

        // exclue Draft timesheet
        timesheetList.removeIf(ts -> ts.getStatus().
            equals(TimesheetStatus.DRAFT.getStatus()));

        return timesheetList;
    }
}
