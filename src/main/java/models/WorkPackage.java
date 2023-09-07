package models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * The WorkPackage class represents a work package in the system. It includes
 * the ID, name, start date, end date, project, responsible engineer, engineer
 * estimate, rolling estimate, and collections of employee work packages and
 * timesheet rows associated with it.
 *
 * @author The Bug Busters.
 * @version 1
 */
@Entity
@Table(name = "work_package")
public class WorkPackage implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Placeholder Work Package Flag. */
    @Transient
    private boolean placeholder;

    /** ID. */
    @Id
    @Column(name = "wp_id")
    private String id;

    /** Name. */
    @Column(name = "wp_name")
    private String name;

    /** Start Date. */
    @Column(name = "wp_start_date")
    private LocalDate startDate;

    /** End Date. */
    @Column(name = "wp_end_date")
    private LocalDate endDate;

    /** Project. */
    @ManyToOne
    @JoinColumn(name = "wp_project_id")
    private Project project;

    /** Responsible Engineer. */
    @ManyToOne
    @JoinColumn(name = "wp_parent_id")
    private WorkPackage workPackageParent;

    /** Responsible Engineer. */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wp_resp_eng_id")
    private Employee responsibleEngineer;

    /** Responsible Engineer's Budget Estimate. */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wp_eng_est_id")
    private Budget engineerEstimate;

    /** Responsible Engineer's Rolling Budget Estimate. */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wp_rolling_est_id")
    private Budget rollingEstimate;

    /** Employees Assigned to Work Package. */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "employee_work_package", joinColumns = {
        @JoinColumn(name = "ewp_wp_id") }, inverseJoinColumns = {
            @JoinColumn(name = "ewp_emp_id") })
    private Set<Employee> assignedEmployees = new HashSet<>();

    /** Timesheet's rows. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "workPackage")
    private Collection<TimesheetRow> timesheetRows = new ArrayList<>();

    /** Children Work Packages. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "workPackageParent")
    private Set<WorkPackage> workPackageChildren = new HashSet<>();

    /**
     * Default Constructor.
     */
    public WorkPackage() {
    }

    /**
     * Assigns a given employee to work package.
     * 
     * @param employee the employee
     */
    public void assignEmployeeToWorkPackage(Employee employee) {
        this.assignedEmployees.add(employee);
    }

    /**
     * Removes a given employee from work package.
     * 
     * @param employee the employee
     */
    public void removeEmployeeFromWorkPackage(Employee employee) {
        this.assignedEmployees.remove(employee);
    }

    /**
     * Gets the work package placeholder flag.
     * 
     * @return the work package placeholder flag
     */
    public boolean isPlaceholder() {
        return placeholder;
    }

    /**
     * Sets the work package placeholder flag.
     * 
     * @param placeholder the work package placeholder flag
     */
    public void setPlaceholder(boolean placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * Gets the work package ID.
     * 
     * @return the work package ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the work package ID.
     * 
     * @param id the work package ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the work package name.
     *
     * @return the work package name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the work package name.
     *
     * @param name the work package name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the work package start date.
     *
     * @return the work package start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the work package start date.
     *
     * @param startDate the work package start date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the work package end date.
     *
     * @return the work package end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the work package end date.
     *
     * @param endDate the work package end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the work package's project.
     *
     * @return the work package's project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the work package's project.
     *
     * @param project the work package's project
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Gets the work package's responsible engineer.
     *
     * @return the work package's responsible engineer
     */
    public Employee getResponsibleEngineer() {
        return responsibleEngineer;
    }

    /**
     * Sets the work package's responsible engineer.
     *
     * @param responsibleEngineer the work package's responsible engineer
     */
    public void setResponsibleEngineer(Employee responsibleEngineer) {
        this.responsibleEngineer = responsibleEngineer;
    }

    /**
     * Gets the work package's responsible engineer budget estimate.
     *
     * @return the work package's responsible engineer budget estimate
     */
    public Budget getEngineerEstimate() {
        return engineerEstimate;
    }

    /**
     * Sets the work package's responsible engineer budget estimate.
     *
     * @param engineerEstimate the work package's responsible engineer
     *                         budget estimate
     */
    public void setEngineerEstimate(Budget engineerEstimate) {
        this.engineerEstimate = engineerEstimate;
    }

    /**
     * Gets the work package's responsible engineer rolling budget estimate.
     *
     * @return the work package's responsible engineer rolling budget estimate
     */
    public Budget getRollingEstimate() {
        return rollingEstimate;
    }

    /**
     * Sets the work package's responsible engineer rolling budget estimate.
     *
     * @param rollingEstimate the work package's responsible engineer
     *                        rolling budget estimate
     */
    public void setRollingEstimate(Budget rollingEstimate) {
        this.rollingEstimate = rollingEstimate;
    }

    /**
     * Gets the work package's assigned employees.
     * 
     * @return the work package's assigned employees
     */
    public Set<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    /**
     * Sets the work package's assigned employees.
     * 
     * @param assignedEmployees the work package's assigned employees
     */
    public void setAssignedEmployees(Set<Employee> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
    }

    /**
     * Gets the parent work package.
     * 
     * @return the parent work package
     */
    public WorkPackage getWorkPackageParent() {
        return workPackageParent;
    }

    /**
     * Sets the parent work package.
     * 
     * @param workPackageParent the parent work package
     */
    public void setWorkPackageParent(WorkPackage workPackageParent) {
        this.workPackageParent = workPackageParent;
    }

    /**
     * Gets the timesheet's rows.
     * 
     * @return the timesheet's rows
     */
    public Collection<TimesheetRow> getTimesheetRows() {
        return timesheetRows;
    }

    /**
     * Sets the timesheet's rows.
     * 
     * @param timesheetRows the timesheet's rows
     */
    public void setTimesheetRows(Collection<TimesheetRow> timesheetRows) {
        this.timesheetRows = timesheetRows;
    }

    /**
     * Gets the child work packages.
     * 
     * @return the child work packages
     */
    public Collection<WorkPackage> getWorkPackageChildren() {
        return workPackageChildren;
    }

    /**
     * Set the child work packages.
     * 
     * @param workPackageChildren the child work packages
     */
    public void setWorkPackageChildren(
            Set<WorkPackage> workPackageChildren) {
        this.workPackageChildren = workPackageChildren;
    }

    /**
     * Checks if work package is of the lowest level.
     * 
     * @return true if work package has no child work packages, false otherwise.
     */
    public boolean isLowestLevelWorkPackage() {
        return this.workPackageChildren.isEmpty();
    }

    /**
     * Formats object for printing.
     * 
     * @return the work package name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compares a WorkPackage to another object.
     * 
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        WorkPackage workPackage = (WorkPackage) obj;

        return workPackage.id.equals(this.id);
    }

    /**
     * Returns hash code for the work package ID.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

}
