package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class represents an employee in the system. An employee has a unique
 * employee number, a username, a password, a name, an employee type, a pay
 * grade, a supervisor, a collection of supervisees, a boolean flag indicating
 * if the employee is disabled, the amount of overtime hours worked, the amount
 * of flextime hours available, a collection of projects the employee manages, a
 * collection of work packages the employee is assigned to, a collection of
 * timesheets the employee submitted, and a collection of approvals the employee
 * received.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Entity
@Table(name = "employee")
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Employee Number. */
    @Id
    @Column(name = "emp_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empNumber;

    /** Username. */
    @Column(name = "emp_username")
    private String username;

    /** Password. */
    @Column(name = "emp_password")
    private String password;

    /** Employee Full Name. */
    @Column(name = "emp_name")
    private String empName;

    /** Employee Type. */
    @Column(name = "emp_type")
    private String empType;

    /** Employee's Account Deactivation Flag. */
    @Column(name = "emp_deactivated")
    private boolean empDeactivated;

    /** Employee's Pay Grade. */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_grade_id")
    private PayGrade payGrade;

    /** Employee's Supervisor. */
    @ManyToOne
    @JoinColumn(name = "emp_supervisor_id")
    private Employee supervisor;

    /** Employee's Timesheet Approver. */
    @ManyToOne
    @JoinColumn(name = "emp_approver_id")
    private Employee approver;

    /** List of Employee's Projects. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "projectManager")
    private Collection<Project> projects = new ArrayList<>();

    /** List of Employee's Timesheets. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employee")
    private Collection<Timesheet> timesheets = new ArrayList<>();

    /** List of Employee's Supervisees. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "supervisor")
    private Collection<Employee> supervisees = new ArrayList<Employee>();

    /** List of Employee's Approvees. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "approver")
    private Collection<Employee> approvees = new ArrayList<Employee>();

    /**
     * Default Constructor.
     */
    public Employee() {
    }

    /**
     * Parameter Constructor.
     * Initializes password.
     * 
     * @param password the password
     */
    public Employee(String password) {
        this.password = password;
    }

    /**
     * Gets list of supervisees assigned to a given project.
     * 
     * @param project the project
     * @return list of supervisees assigned to a given project
     */
    public List<Employee> getSuperviseesAssignedToProject(Project project) {
        List<Employee> assignedEmployees = new ArrayList<>();

        for (Employee supervisee : supervisees) {
            if (project.getAssignedEmployees().contains(supervisee)) {
                assignedEmployees.add(supervisee);
            }
        }

        return assignedEmployees;
    }

    /**
     * Gets the employee's number.
     * 
     * @return the employee's number
     */
    public Long getEmpNumber() {
        return empNumber;
    }

    /**
     * Sets the employee's number.
     * 
     * @param empNumber the employee's number
     */
    public void setEmpNumber(Long empNumber) {
        this.empNumber = empNumber;
    }

    /**
     * Gets the employee's username.
     * 
     * @return the employee's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the employee's username.
     * 
     * @param username the employee's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the employee's password.
     * 
     * @return the employee's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the employee's password.
     * 
     * @param password the employee's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the employee'e full name.
     * 
     * @return the employee'e full name
     */
    public String getEmpName() {
        return empName;
    }

    /**
     * Sets the employee'e full name.
     * 
     * @param empName the employee'e full name
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    /**
     * Gets the employee's type.
     * 
     * @return the employee's type
     */
    public String getEmpType() {
        return empType;
    }

    /**
     * Sets the employee's type.
     * 
     * @param empType the employee's type
     */
    public void setEmpType(String empType) {
        this.empType = empType;
    }

    /**
     * Gets employee's account deactivation flag.
     * 
     * @return employee's account deactivation flag
     */
    public boolean isEmpDeactivated() {
        return empDeactivated;
    }

    /**
     * Sets employee's account deactivation flag.
     * 
     * @param empDeactivated employee's account deactivation flag
     */
    public void setEmpDeactivated(boolean empDeactivated) {
        this.empDeactivated = empDeactivated;
    }

    /**
     * Gets the employee's pay grade.
     * 
     * @return the employee's pay grade
     */
    public PayGrade getPayGrade() {
        return payGrade;
    }

    /**
     * Sets the employee's pay grade.
     * 
     * @param payGrade the employee's pay grade
     */
    public void setPayGrade(PayGrade payGrade) {
        this.payGrade = payGrade;
    }

    /**
     * Gets the employee's supervisees.
     * 
     * @return the employee's supervisees
     */
    public Collection<Employee> getSupervisees() {
        return supervisees;
    }

    /**
     * Sets the employee's supervisees.
     * 
     * @param supervisees the employee's supervisees
     */
    public void setSupervisees(Collection<Employee> supervisees) {
        this.supervisees = supervisees;
    }

    /**
     * Gets the employee's supervisor.
     * 
     * @return the employee's supervisor
     */
    public Employee getSupervisor() {
        return supervisor;
    }

    /**
     * Sets the employee's supervisor.
     * 
     * @param supervisor the employee's supervisor
     */
    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * Gets the employee's timesheet approver.
     * 
     * @return the employee's timesheet approver
     */
    public Employee getApprover() {
        return approver;
    }

    /**
     * Sets the employee's timesheet approver.
     * 
     * @param approver the employee's timesheet approver
     */
    public void setApprover(Employee approver) {
        this.approver = approver;
    }

    /**
     * Gets the employee's approvees.
     * 
     * @return the employee's approvees
     */
    public Collection<Employee> getApprovees() {
        return approvees;
    }

    /**
     * Sets the employee's approvees.
     * 
     * @param approvals the employee's approvees
     */
    public void setApprovees(Collection<Employee> approvals) {
        this.approvees = approvals;
    }

    /**
     * Gets the employee's projects.
     * 
     * @return the employee's projects
     */
    public Collection<Project> getProjects() {
        return projects;
    }

    /**
     * Sets the employee's projects.
     * 
     * @param projects the employee's projects
     */
    public void setProjects(Collection<Project> projects) {
        this.projects = projects;
    }

    /**
     * Gets the employee's timesheets.
     * 
     * @return the employee's timesheets
     */
    public Collection<Timesheet> getTimesheets() {
        return timesheets;
    }

    /**
     * Sets the employee's timesheets.
     * 
     * @param timesheets the employee's timesheets
     */
    public void setTimesheets(Collection<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }

    /**
     * Formats object for printing.
     * 
     * @return formatted string
     */
    @Override
    public String toString() {
        return username;
    }

    /**
     * Compares an employee to another object.
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
        Employee employee = (Employee) obj;

        return (employee.empNumber.equals(this.empNumber));
    }

    /**
     * Returns hash code for employee's number.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return this.empNumber.intValue();
    }
}
