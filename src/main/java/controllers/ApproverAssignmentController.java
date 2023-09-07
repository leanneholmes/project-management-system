package controllers;

import access.EmployeeManager;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.Employee;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the controller for Approver Assignment, containing methods to
 * assign approvers for other employees and modify some employee information.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("aaController")
@SessionScoped
public class ApproverAssignmentController implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Class Manager. */
    @Inject
    private EmployeeManager em;

    /** List of Supervisees. */
    private List<Employee> superviseesList;

    /**
     * Gets all supervisees for the given employee,
     * stores them in a list, and performs page navigation.
     * 
     * @param employee the employee
     * @return navigation string
     */
    public String setListAndNavigateToApproverAssignment(Employee employee) {
        superviseesList = new ArrayList<>(employee.getSupervisees());

        return "approver-assignment?faces-redirect=true";
    }

    /**
     * Gets all employees suitable to be an approver for a given employee,
     * including the employee currently logged in and
     * the supervisees of the employee currently logged in,
     * but not the employee to be assigned an approver.
     * 
     * @param currentEmployee employee currently logged in
     * @param employee employee to be assigned an approver
     * @return list of employees suitable to be an approver for a given employee
     */
    public List<Employee> getEmployeesToSelect(
            Employee currentEmployee, Employee employee) {

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(currentEmployee);
        employeeList.addAll(currentEmployee.getSupervisees());
        employeeList.remove(employee);

        return employeeList;
    }

    /**
     * Merges changes made to an employee in the database.
     *
     * @param employee the employee to merge
     */
    public void update(Employee employee) {
        em.merge(employee);
    }

    /**
     * Gets list of supervisees.
     * 
     * @return list of supervisees
     */
    public List<Employee> getSuperviseesList() {
        return superviseesList;
    }

    /**
     * Sets list of supervisees.
     * 
     * @param superviseesList list of supervisees
     */
    public void setSuperviseesList(List<Employee> superviseesList) {
        this.superviseesList = superviseesList;
    }
}
