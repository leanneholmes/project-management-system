package controllers;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.Budget;
import models.Employee;
import models.Project;
import models.TimesheetRow;
import models.TimesheetStatus;
import models.WorkPackage;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import access.ProjectManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * This class is the controller for monthly report management and generation.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("mrptController")
@SessionScoped
public class MonthlyReportController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Class Manager. */
    @Inject
    private ProjectManager projectManager;

    /** Currently Project. */
    private Project currentProject;

    /** Work Packages in Current Project. */
    private Set<WorkPackage> workPackageList;

    /**
     * Default Constructor.
     * Initializes current project and work package list.
     */
    public MonthlyReportController() {
        currentProject = null;
        workPackageList = new HashSet<>(Collections.emptyList());
    }

    /**
     * Initializes current project and work package list.
     */
    @PostConstruct
    public void init() {
        currentProject = null;
        workPackageList = new HashSet<>(Collections.emptyList());
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
     * Gets data needed for monthly report generation.
     */
    public void generateMonthlyReport() {
        if (currentProject == null) {
            init();
            return;
        }

        if (currentProject.getWorkPackages() == null) {
            workPackageList = new HashSet<>(Collections.emptyList());
            return;
        }

        workPackageList = new HashSet<>(currentProject.getWorkPackages());
    }

    /**
     * Gets total estimated budget for a given work package.
     * 
     * @param wp the work package
     * @return total estimated budget for a given work package
     */
    public float getBudget(WorkPackage wp) {
        Budget budget = wp.getEngineerEstimate();

        return budget.getDS() + budget.getSS() + budget.getJS()
                + budget.getP1() + budget.getP2() + budget.getP3()
                + budget.getP4() + budget.getP5() + budget.getP6();
    }

    /**
     * Gets planned cost for a given work package.
     * 
     * @param wp the work package
     * @return planned cost for a given work package
     */
    public float getPlannedValue(WorkPackage wp) {
        long daysBetweenStartAndEnd = ChronoUnit.DAYS.between(
            wp.getStartDate(), wp.getEndDate()) + 1;
        long daysBetweenStartAndToday = Math.min(
                ChronoUnit.DAYS.between(wp.getStartDate(),
                        LocalDate.now()) + 1,
                daysBetweenStartAndEnd);
        float bac = getBudget(wp);

        return (bac * ((float) daysBetweenStartAndToday
                / daysBetweenStartAndEnd));
    }

    /**
     * Gets actual cost for a given work package.
     * 
     * @param wp the work package
     * @return actual cost for a given work package
     */
    public float getActualCost(WorkPackage wp) {
        float sum = 0f;

        List<TimesheetRow> list = new ArrayList<>(
            wp.getTimesheetRows()).stream().
            filter(tsr -> tsr.getTimesheet().getStatus().equals(
                TimesheetStatus.APPROVED.getStatus())).toList();

        for (TimesheetRow tsr : list) {
            float cost = tsr.getTimesheet().getEmployee().
                getPayGrade().getCost();
            sum += tsr.getTotalHours() * cost;
        }

        return sum;
    }

    /**
     * Gets CPI (Consumer Price Index) for a given work package.
     * 
     * @param wp the work package
     * @return CPI for a given work package
     */
    public float getCPI(WorkPackage wp) {
        float planned = getPlannedValue(wp);
        float actual = getActualCost(wp);

        return actual / planned;
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
     * Sets the current project.
     * 
     * @param currentProject the project
     */
    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    /**
     * Gets the list of work packages in the current project.
     * 
     * @return the list of work packages in the current project
     */
    public Set<WorkPackage> getWorkPackageList() {
        return workPackageList;
    }

    /**
     * Sets the list of work packages in the current project.
     * 
     * @param workPackageList the list of work packages in the current project
     */
    public void setWorkPackageList(Set<WorkPackage> workPackageList) {
        this.workPackageList = workPackageList;
    }
}
