package controllers;

import access.ProjectManager;
import access.TimesheetManager;
import access.TimesheetRowManager;
import access.WorkPackageManager;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.Timesheet;
import models.EditableTimesheetRow;
import models.TimesheetRow;
import models.Project;
import models.WorkPackage;
import models.Employee;
import models.TimesheetStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is the controller class
 * for managing timesheets' details in the system.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("tsdController")
@SessionScoped
public class TimesheetDetailController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Class Manager. */
    @Inject
    private TimesheetRowManager tsrm;

    /** Class Manager. */
    @Inject
    private TimesheetManager tsm;

    /** Class Manager. */
    @Inject
    private WorkPackageManager wpm;

    /** Project Manager. */
    @Inject
    private ProjectManager pm;

    /** Selected timesheet. */
    private Timesheet selectedTimesheet;

    /** List of editable rows of the selected timesheet. */
    private List<EditableTimesheetRow> list;

    /** Controller. */
    @Inject
    private AuthController authController;

    /** Currenlty Selected Project. */
    private Project currentProject;

    /** Currently Selected Work Package. */
    private WorkPackage currentWorkPackage;

    /** Timesheet Row being edited. */
    private EditableTimesheetRow timesheetRowToEdit;

    /**
     * Gets the employee's projects.
     * 
     * @param employee the employee
     * @return the employee's projects
     */
    public Set<Project> getProjectsOf(Employee employee) {
        return new HashSet<>(employee.getProjects());
    }

    /**
     * Gets all projects assigned to a given employee.
     * 
     * @param employee the employee
     * @return all projects assigned to a given employee
     */
    public Set<Project> getAssignedProjects(Employee employee) {
        return new HashSet<>(pm.getAssignedProjects(employee));
    }

    /**
     * Gets all work packages for a given project.
     * 
     * @param projectId the project ID
     * @return list of work packages for a given project
     */
    public List<WorkPackage> getWorkPackagesByProjectId(int projectId) {
        System.out.println("project ID: " + projectId);

        return wpm.getAllForProject(projectId);
    }

    /**
     * Gets all work packages for a given employee.
     *
     * @param employee the employee
     * @return list of work packages for a given employee
     */
    public List<WorkPackage> getAssignedWorkPackages(Employee employee) {
        return wpm.getAllForEmployee(employee);
    }

    /**
     * Selects timesheet and performs page navigation.
     *
     * @param timesheet the timesheet
     * @return page navigation string
     */
    public String selectTimesheetAndNavigateToDetails(Timesheet timesheet) {
        System.out.println(wpm.getAllForEmployee(
                authController.getCurrentEmployee()));
        setSelectedTimesheet(timesheet);
        refresh();

        return "timesheet-detail?faces-redirect=true";
    }

    /**
     * Gets the work packages of the currently selected project.
     * 
     * @return the work packages of the currently selected project
     */
    public Set<WorkPackage> getWorkPackagesOfCurrentProject() {
        if (currentProject == null) {
            System.out.println("NO WORK PACKAGES FOUND");
            return new HashSet<>(Collections.emptyList());
        }

        return new HashSet<>(currentProject.getWorkPackages());
    }

    /**
     * Refreshes selected timesheet and list of timesheet's rows.
     */
    public void refresh() {
        int timesheetId = selectedTimesheet.getId();
        list = new ArrayList<>();

        setSelectedTimesheet(tsm.findByTimesheetId(timesheetId));

        List<TimesheetRow> tsrList = tsrm.findByTimesheetId(timesheetId);
        for (TimesheetRow tsr : tsrList) {
            EditableTimesheetRow etsr = new EditableTimesheetRow(tsr);
            list.add(etsr);
        }
    }

    /**
     * Save changes made to timesheet row.
     * 
     * @param etsr the timesheet row
     */
    public void save(EditableTimesheetRow etsr) {
        if (etsr.getTimesheetRow().getId() == 0) {
            // if timesheet is new (id == 0), insert
            tsrm.insert(etsr.getTimesheetRow());
        } else if (etsr.isEditable()) {
            System.out.println("updating tsr");
            // if timesheet is not new and editable, update
            tsrm.update(etsr.getTimesheetRow());
        }

        etsr.setEditable(false);
        timesheetRowToEdit = null;
        refresh();
    }

    /**
     * Enables editing settings on timesheet row.
     * 
     * @param otherTimesheetRowToEdit the timesheet row
     */
    public void edit(EditableTimesheetRow otherTimesheetRowToEdit) {
        for (EditableTimesheetRow etsr : list) {
            if (etsr == otherTimesheetRowToEdit) {
                etsr.setEditable(true);
                this.timesheetRowToEdit = etsr;
                currentProject = etsr.getTimesheetRow().getProject();
                currentWorkPackage = etsr.getTimesheetRow().getWorkPackage();
                break;
            }
        }
    }

    /**
     * Deletes timesheet row from system.
     * 
     * @param etsr the timesheet row
     */
    public void delete(EditableTimesheetRow etsr) {
        if (etsr.getTimesheetRow().getId() != 0) {
            tsrm.delete(etsr.getTimesheetRow());
        }

        list.remove(etsr);
    }

    /**
     * Adds new timesheet row to timesheet.
     */
    public void add() {
        System.out.println("add");
        EditableTimesheetRow etsr = 
            new EditableTimesheetRow(selectedTimesheet, true);
        list.add(etsr);
    }

    /**
     * Submits timesheet for approval.
     * 
     * @param employee employee currently logged in
     */
    public void submit(Employee employee) {
        selectedTimesheet.setSignedDate(LocalDate.now());
        tsm.updateTimesheetStatus(selectedTimesheet,
                TimesheetStatus.PENDING.getStatus());

        refresh();
    }

    /**
     * Checks if selected timesheet has "Draft" or "Rejected" status.
     * 
     * @return true if timesheet has "Draft" or "Rejected" status,
     *         false otherwise
     */
    public boolean renderIfDraftOrRejected() {
        return (selectedTimesheet.getStatus().equals(
                TimesheetStatus.DRAFT.getStatus())
                || selectedTimesheet.getStatus().equals(
                        TimesheetStatus.REJECTED.getStatus()));
    }

    /**
     * Checks if selected timesheet has "Pending" or "Approved" status.
     * 
     * @return true if timesheet has "Pending" or "Approved" status,
     *         false otherwise
     */
    public boolean renderIfPendingOrApproved() {
        return (selectedTimesheet.getStatus().equals(
                TimesheetStatus.PENDING.getStatus())
                || selectedTimesheet.getStatus().equals(
                        TimesheetStatus.APPROVED.getStatus()));
    }

    /**
     * Checks if selected timesheet has "Approved" or "Rejected" status.
     * 
     * @return true if timesheet has "Approved" or "Rejected" status,
     *         false otherwise
     */
    public boolean renderIfApprovedOrRejected() {
        return (selectedTimesheet.getStatus().equals(
                TimesheetStatus.REJECTED.getStatus())
                || selectedTimesheet.getStatus().equals(
                        TimesheetStatus.APPROVED.getStatus()));
    }

    /**
     * Validator to check whether or not the hour input is positive.
     * 
     * @param context   faces context
     * @param component component of the error
     * @param value     value of the component
     * @throws ValidatorException input is not positive
     */
    public void validateHourInput(
            FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        String inputString = value.toString().trim();
        float inputHour = Float.parseFloat(inputString);
        final int MAX_HOUR = 24;

        if (inputHour < 0) {
            throw new ValidatorException(
                    new FacesMessage("invalid hour input: "
                            + inputHour + ", hour must be positive"));
        } else if (inputHour > MAX_HOUR) {
            throw new ValidatorException(
                    new FacesMessage("invalid hour input: "
                            + inputHour + ", hour must be less than 24"));
        }
    }

    /**
     * Checks if a given work package has been charged to.
     * 
     * @param workPackage the work package
     * @return true if a given work package has been charged to, false otherwise
     */
    public boolean checkIfWorkPackageHasBeenChargedTo(WorkPackage workPackage) {
        List<WorkPackage> existingWorkPackages = timesheetRowToEdit == null
                ? tsm.getAllWorkPackagesSubmittedForTimesheet(selectedTimesheet)
                : tsm.getAllWorkPackagesSubmittedForTimesheet(selectedTimesheet,
                        timesheetRowToEdit.getTimesheetRow().getId());
        return (existingWorkPackages.contains(workPackage) 
            && currentWorkPackage != workPackage);
    }

    /**
     * Checks if all work packages in a given project have been charged to.
     * 
     * @param project the project
     * @return true if all work packages in a given project have been charged to
     */
    public boolean checkIfAllWorkPackagesInProjectHaveBeenChargedTo(
        Project project) {
        List<WorkPackage> submittedWorkPackages = 
            tsm.getAllWorkPackagesSubmittedForTimesheet(selectedTimesheet);

        System.out.println("verifying size: " + "project wp size: " 
            + project.getWorkPackages().size()
            + "submitted size: " + submittedWorkPackages.size());

        System.out.println("work packages: ");
        for (WorkPackage wp : project.getWorkPackages()) {
            System.out.println("wp id: " + wp.getId());
        }

        return project.getWorkPackages().size() == submittedWorkPackages.size();
    }

    /**
     * Validates the the work package field of a form submission 
     * (a work package can only be charged to once per timesheet).
     * 
     * @param context        the Faces context
     * @param component      the UI component
     * @param convertedValue the converted value of the component
     * @throws ValidatorException if the work package is not valid
     */
    public void validateFormWorkPackageField(FacesContext context,
            UIComponent component, Object convertedValue) {
        if (convertedValue != null) {
            List<WorkPackage> existingWorkPackages = timesheetRowToEdit == null
                    ? tsm.getAllWorkPackagesSubmittedForTimesheet(
                        selectedTimesheet)
                    : tsm.getAllWorkPackagesSubmittedForTimesheet(
                        selectedTimesheet,
                        timesheetRowToEdit.getTimesheetRow().getId());
            if (existingWorkPackages.contains((WorkPackage) convertedValue)) {
                EmployeeController.throwValidationError(
                    "duplicateWorkPackageErrorMsg");
            }
        }
    }

    /**
     * Gets total hours on timesheet.
     * 
     * @return total hours on timesheet
     */
    public float getSum() {
        float sum = 0;
        for (EditableTimesheetRow etsr : list) {
            sum += etsr.getTotalHours();
        }

        return sum;
    }

    /**
     * Gets total Saturday hours on timesheet.
     * 
     * @return total Saturday hours on timesheet
     */
    public float getSatSum() {
        float sum = 0;
        for (EditableTimesheetRow etsr : list) {
            sum += etsr.getTimesheetRow().getSatHours();
        }

        return sum;
    }

    /**
     * Gets total Sunday hours on timesheet.
     * 
     * @return total Sunday hours on timesheet
     */
    public float getSunSum() {
        float sum = 0;
        for (EditableTimesheetRow etsr : list) {
            sum += etsr.getTimesheetRow().getSunHours();
        }

        return sum;
    }

    /**
     * Gets total Monday hours on timesheet.
     * 
     * @return total Monday hours on timesheet
     */
    public float getMonSum() {
        float sum = 0;
        for (EditableTimesheetRow etsr : list) {
            sum += etsr.getTimesheetRow().getMonHours();
        }

        return sum;
    }

    /**
     * Gets total Tuesday hours on timesheet.
     * 
     * @return total Tuesday hours on timesheet
     */
    public float getTueSum() {
        float sum = 0;
        for (EditableTimesheetRow etsr : list) {
            sum += etsr.getTimesheetRow().getTueHours();
        }

        return sum;
    }

    /**
     * Gets total Wednesday hours on timesheet.
     * 
     * @return total Wednesday hours on timesheet
     */
    public float getWedSum() {
        float sum = 0;
        for (EditableTimesheetRow etsr : list) {
            sum += etsr.getTimesheetRow().getWedHours();
        }

        return sum;
    }

    /**
     * Gets total Thursday hours on timesheet.
     * 
     * @return total Thursday hours on timesheet
     */
    public float getThuSum() {
        float sum = 0;
        for (EditableTimesheetRow etsr : list) {
            sum += etsr.getTimesheetRow().getThuHours();
        }

        return sum;
    }

    /**
     * Gets total Friday hours on timesheet.
     * 
     * @return total Friday hours on timesheet
     */
    public float getFriSum() {
        float sum = 0;
        for (EditableTimesheetRow etsr : list) {
            sum += etsr.getTimesheetRow().getFriHours();
        }

        return sum;
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
     * Gets list of editable timesheet rows.
     * 
     * @return list of editable timesheet rows
     */
    public List<EditableTimesheetRow> getList() {
        if (list == null) {
            refresh();
        }

        return list;
    }

    /**
     * Sets list of editable timesheet rows.
     * 
     * @param list list of editable timesheet rows
     */
    public void setList(List<EditableTimesheetRow> list) {
        this.list = list;
    }

    /**
     * Sets the {@code selectedTimesheet} to null before destroying instance.
     */
    @PreDestroy
    public void destroy() {
        selectedTimesheet = null;
    }
}
