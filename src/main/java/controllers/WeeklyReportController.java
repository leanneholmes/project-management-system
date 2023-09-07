package controllers;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.Employee;
import models.Project;
import models.TimesheetRow;
import models.TimesheetStatus;
import models.WorkPackage;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import access.ProjectManager;
import access.WorkPackageManager;

/**
 * This class is the controller for weekly report management and generation.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("wrptController")
@SessionScoped
public class WeeklyReportController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Class Manager. */
    @Inject
    private ProjectManager projectManager;

    /** Class Manager. */
    @Inject
    private WorkPackageManager WorkPackageManager;

    /** Currently Selected Project. */
    private Project currentProject;

    /** Currently Selected Work Package. */
    private WorkPackage currentWorkPackage;

    /** Row List of Current Timesheet. */
    private List<TimesheetRow> timesheetRowList;

    /**
     * Default Constructor.
     * Initializes current project, current work package and row list.
     */
    public WeeklyReportController() {
        currentProject = null;
        currentWorkPackage = null;
        timesheetRowList = Collections.emptyList();
    }

    /**
     * Initializes current project, current work package and row list.
     */
    @PostConstruct
    public void init() {
        currentProject = null;
        currentWorkPackage = null;
        timesheetRowList = Collections.emptyList();
    }

    /**
     * Gets the employee's projects.
     * 
     * @param employee the employee
     * @return the employee's projects
     */
    public Set<Project> getProjectsOf(Employee employee) {
        System.out.println("getting projects for employee " 
            + employee.getUsername());
        Set<Project> managedProjects = new HashSet<>(
            projectManager.getManagedProjects(employee.getEmpNumber()));
        Set<Project> assignedProjects = new HashSet<>(
            projectManager.getAssignedProjects(employee));

        Set<Project> allProjects = new HashSet<>();
        allProjects.addAll(managedProjects);
        allProjects.addAll(assignedProjects);

        return allProjects;
    }

    /**
     * Gets the work packages of the currently selected project.
     * 
     * @return the work packages of the currently selected project
     */
    public Set<WorkPackage> getWorkPackagesOfCurrentProject() {
        if (currentProject == null) {
            return new HashSet<>(Collections.emptyList());
        }

        return new HashSet<>(WorkPackageManager.getAllForProject(
            currentProject.getProjectId()));
    }

    /**
     * Gets data needed for weekly report generation.
     */
    public void generateWeeklyReport() {
        if (currentProject == null || currentWorkPackage == null) {
            init();
            return;
        }

        if (currentWorkPackage.getTimesheetRows() == null) {
            timesheetRowList = Collections.emptyList();
            return;
        }

        timesheetRowList = new ArrayList<>(
            currentWorkPackage.getTimesheetRows()).stream().
            filter(tsr -> tsr.getTimesheet().getStatus().equals(
                TimesheetStatus.APPROVED.getStatus())).
                collect(Collectors.toList());
    }

    /**
     * Gets total work hours for a given employee in the current timesheet.
     * 
     * @param employee the employee
     * @return total work hours for a given employee in the current timesheet
     */
    public float getTotalHoursOf(Employee employee) {
        float hours = 0;
        List<TimesheetRow> list = new ArrayList<>(timesheetRowList);
        list.removeIf(
                tsr -> !tsr.getTimesheet().getEmployee().equals(employee));

        for (TimesheetRow tsr : list) {
            hours += tsr.getTotalHours();
        }

        return hours;
    }

    /**
     * Gets total work hours in the current timesheet.
     * 
     * @return total work hours in the current timesheet
     */
    public float getTotalHours() {
        float totalHours = 0;
        for (TimesheetRow tsr : timesheetRowList) {
            totalHours += tsr.getTotalHours();
        }

        return totalHours;
    }

    /**
     * Checks that Controller's data is initialized.
     * 
     * @return true if a project and work package are selected, false otherwise
     */
    public boolean generateButtonDisabled() {
        return currentProject == null || currentWorkPackage == null;
    }

    /**
     * Gets the currently selected project.
     * 
     * @return the currently selected project
     */
    public Project getCurrentProject() {
        return currentProject;
    }

    /**
     * Sets the currently selected project.
     * 
     * @param currentProject the selected project
     */
    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    /**
     * Gets list of rows of the current timesheet.
     * 
     * @return list of rows of the current timesheet
     */
    public List<TimesheetRow> getTimesheetRowList() {
        return timesheetRowList;
    }

    /**
     * Sets list of rows of the current timesheet.
     * 
     * @param timesheetRowList list of rows of the current timesheet
     */
    public void setTimesheetRowList(List<TimesheetRow> timesheetRowList) {
        this.timesheetRowList = timesheetRowList;
    }

    /**
     * Gets currently selected work package.
     * 
     * @return currently selected work package
     */
    public WorkPackage getCurrentWorkPackage() {
        return currentWorkPackage;
    }

    /**
     * Sets currently selected work package.
     * 
     * @param currentWorkPackage selected work package
     */
    public void setCurrentWorkPackage(WorkPackage currentWorkPackage) {
        this.currentWorkPackage = currentWorkPackage;
    }
}
