package models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The Project class represents a project in the system.
 * 
 * @author The Bug Busters.
 * @version 1
 */
@Entity
@Table(name = "project")
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID. */
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;

    /** Name. */
    @Column(name = "project_name")
    private String projectName;

    /** Description. */
    @Column(name = "project_description")
    private String projectDescription;

    /** Project Manager. */
    @ManyToOne
    @JoinColumn(name = "project_manager_number")
    private Employee projectManager;

    /** Project's Allocated Budget. */
    @Column(name = "project_allocated_budget")
    private Double projectAllocatedBudget;

    /** Employees Assigned to Project. */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "employee_project", joinColumns = {
            @JoinColumn(name = "ep_project_id") }, inverseJoinColumns = {
                    @JoinColumn(name = "ep_emp_id") })
    private Set<Employee> assignedEmployees = new HashSet<>();

    /** Work Packages related to Project. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project")
    private Set<WorkPackage> workPackages = new HashSet<>();

    /** Timesheet Rows. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "project")
    private Collection<TimesheetRow> timesheetRows = new ArrayList<>();

    /**
     * Default Contructor.
     */
    public Project() {
    }

    /**
     * Assigns given employee to project.
     * 
     * @param employee the employee
     */
    public void assignEmployeeToProject(Employee employee) {
        this.assignedEmployees.add(employee);
    }

    /**
     * Removes the given employee from project.
     * 
     * @param employee the employee
     */
    public void removeEmployeeFromProject(Employee employee) {
        this.assignedEmployees.remove(employee);
    }

    /**
     * Checks if all supervisees of a given employee are assigned to project.
     * 
     * @param employee the employee
     * @return true if all supervisees of a given employee
     *         are assigned to project, false otherwise
     */
    public boolean checkIfAllEmployeesAssignedToProject(Employee employee) {
        for (Employee supervisee : employee.getSupervisees()) {
            if (!this.getAssignedEmployees().contains(supervisee)
                    && !this.getProjectManager().equals(supervisee)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the project ID.
     * 
     * @return the project ID
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Sets the project ID.
     * 
     * @param projectId the project ID
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * Gets the project name.
     * 
     * @return the project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the project name.
     * 
     * @param projectName the project name
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Gets the project description.
     * 
     * @return the project description
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * Sets the project description.
     * 
     * @param projectDescription the project description
     */
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    /**
     * Gets the project's project manager.
     * 
     * @return the project's project manager
     */
    public Employee getProjectManager() {
        return projectManager;
    }

    /**
     * Sets the project's project manager.
     * 
     * @param projectManager the project's project manager
     */
    public void setProjectManager(Employee projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * Gets the project's allocated budget.
     * 
     * @return the project's allocated budget
     */
    public Double getProjectAllocatedBudget() {
        return projectAllocatedBudget;
    }

    /**
     * Sets the project's allocated budget.
     * 
     * @param projectAllocatedBudget the project's allocated budget
     */
    public void setProjectAllocatedBudget(Double projectAllocatedBudget) {
        this.projectAllocatedBudget = projectAllocatedBudget;
    }

    /**
     * Gets the employees assigned to project.
     * 
     * @return the employees assigned to project
     */
    public Set<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    /**
     * Sets the employees assigned to project.
     * 
     * @param assignedEmployees the employees assigned to project
     */
    public void setAssignedEmployees(Set<Employee> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
    }

    /**
     * Gets the work packages related to project.
     * 
     * @return the work packages related to project
     */
    public Set<WorkPackage> getWorkPackages() {
        return workPackages;
    }

    /**
     * Sets the work packages related to project.
     * 
     * @param workPackages the work packages related to project
     */
    public void setWorkPackages(Set<WorkPackage> workPackages) {
        this.workPackages = workPackages;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Project project = (Project) obj;

        return project.projectId == this.projectId;
    }

    @Override
    public int hashCode() {
        return this.projectId;
    }
}
