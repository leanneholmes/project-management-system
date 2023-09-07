package controllers;

import access.EmployeeManager;
import access.PayGradeManager;
import access.ProjectManager;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.Employee;
import models.PayGrade;
import models.Project;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

/**
 * This class is the controller for employee management, containing methods to
 * add, update, and delete employees, as well as methods to retrieve employees
 * from the system.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("employeeController")
@SessionScoped
public class EmployeeController implements Serializable {
    private static final long serialVersionUID = 1L;

    /** List of employees. */
    private static List<Employee> employees;

    /** List of pay grades. */
    private static List<PayGrade> payGrades;

    /**
     * Map of valid employee types to be used in dropdown menus.
     */
    private static Map<String, EmpType> empTypes;

    /** Class Manager. */
    @Inject
    private EmployeeManager employeeManager;

    /** Class Manager. */
    @Inject
    private PayGradeManager paygradeManager;

    /** Class Manager. */
    @Inject
    private ProjectManager projectManager;

    /**
     * Employee instance used solely for the purpose of adding new employees to
     * the system. Said employee's password should automatically be set to
     * "temp".
     */
    private Employee employeeToAdd = new Employee("temp");

    /**
     * Employee instance used to reference an employee in the system
     * when assigning them some responsibility.
     */
    private Employee employeeToAssign = new Employee("temp");

    /**
     * Enum defining valid employee types.
     */
    public enum EmpType {
        /** Human Resources. */
        HR,
        /** Project Manager. */
        PM,
        /** Regular Employee. */
        Employee
    }

    static {
        empTypes = new LinkedHashMap<String, EmpType>();
        empTypes.put("HR", EmpType.HR);
        empTypes.put("PM", EmpType.PM);
        empTypes.put("Employee", EmpType.Employee);
    }

    /** Display Success Message boolean. */
    private boolean successMessageToDisplay;

    /**
     * Displays a success message when employee is created successfully.
     */
    public void displayCreationSuccessMessage() {
        if (successMessageToDisplay) {
            AuthController.getSuccessMessage(AuthController.getRb().
                getString("successfulEmployeeCreationMsg"));
        }
        successMessageToDisplay = false;
    }

    /**
     * Clears any success messages that may be displayed.
     */
    private void clearSuccessMessage() {
        Iterator<String> itIds = FacesContext.getCurrentInstance().
            getClientIdsWithMessages();
        while (itIds.hasNext()) {
            List<FacesMessage> messageList = FacesContext.
                getCurrentInstance().getMessageList(itIds.next());
            if (!messageList.isEmpty()) {
                messageList.clear();
            }
        }
    }

    /**
     * Initializes the employee and pay grade lists upon instantiation.
     */
    @PostConstruct
    public void refreshLists() {
        employees = employeeManager.getAll();
        payGrades = paygradeManager.getAll();
    }

    /**
     * Updates the employee list and the supervisee collections
     * of each employee.
     */
    public void updateEmployees() {
        clearSupervisees();

        for (Employee emp : employees) {
            if (emp.getSupervisor() != null) {
                emp.getSupervisor().getSupervisees().add(emp);
            }
            employeeManager.merge(emp);
        }

        refreshLists();
    }

    /**
     * Clears the supervisee collections of each employee in preparation for
     * updating.
     */
    private void clearSupervisees() {
        for (Employee employee : employees) {
            employee.getSupervisees().removeAll(employees);
        }
    }

    /**
     * Adds a new employee to the system.
     * 
     * @return navigation string
     */
    public String addNewEmployee() {
        updateEmployees();
        employeeManager.persist(employeeToAdd);
        employeeToAdd = new Employee("temp");
        clearSuccessMessage();
        successMessageToDisplay = true;
        refreshLists();
        return "employees?faces-redirect=true";
    }

    /**
     * Deactivates employee account in the system.
     * 
     * @param employee the employee account
     */
    public void deactivateEmployee(Employee employee) {
        updateEmployees();

        if (employee.getSupervisor() != null) {
            employee.getSupervisor().getSupervisees().remove(employee);
        }

        if (employee.getSupervisees().size() > 0) {
            for (Employee emp : employee.getSupervisees()) {
                emp.setSupervisor(null);
            }
        }

        employee.setEmpDeactivated(true);
        employeeManager.merge(employee);
        refreshLists();
    }

    /**
     * Activates disabled employee account in the system.
     * 
     * @param employee the employee account
     */
    public void activateEmployee(Employee employee) {
        updateEmployees();
        employee.setEmpDeactivated(false);
        employeeManager.merge(employee);
        refreshLists();
    }

    /**
     * Returns employee with a given employee number.
     * 
     * @param empNumber the employee number field of the employee
     * @return the employee.
     */
    public Employee getEmployee(long empNumber) {
        return employeeManager.getEmpByEmpNumber(empNumber);
    }

    /**
     * Returns the employee with the given username.
     *
     * @param username The employee's username.
     * @return The employee, null if they don't exist in the employee list.
     */
    public static Employee getEmployeeByUsername(String username) {
        for (int i = 0; i < employees.size(); i++) {
            Employee nextEmployee = employees.get(i);
            if (nextEmployee.getUsername().equals(username)) {
                return nextEmployee;
            }
        }
        return null;
    }

    /**
     * Retrieves the pay grade with the specified name and year.
     * 
     * @param name the name of the pay grade to retrieve.
     * @param year the year of the pay grade to retrieve.
     * @return the pay grade with the specified name and year,
     *         or null if no such pay grade exists.
     */
    public static PayGrade getPayGradeByNameAndYear(String name, int year) {
        System.out.println(
                "getting pay grade with name " + name + " and year " + year);
        for (int i = 0; i < payGrades.size(); i++) {
            PayGrade nextPaygrade = payGrades.get(i);
            if (nextPaygrade.getName().equals(name)
                    && nextPaygrade.getYear().getYear() == year) {
                return nextPaygrade;
            }
        }
        return null;
    }

    /**
     * Checks that all supervisees for a given employee and project 
     * are assigned to the project.
     * 
     * @param employee the employee
     * @param project the project
     * @return true if all supervisees for a given employee and project 
     *      are assigned to the project, false otherwise
     */
    public boolean checkIfAllSuperviseesAssignedToProject(Employee employee,
            Project project) {

        updateEmployees();

        return (project.getAssignedEmployees().size() 
            == employee.getSupervisees().size());
    }

    /**
     * Gets list of employees.
     * 
     * @return list of employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * Sets the list of employees.
     * 
     * @param employees the list of employees to set
     */
    public static void setEmployees(List<Employee> employees) {
        EmployeeController.employees = employees;
    }

    /**
     * Returns the employee instance used for adding a new employee.
     * 
     * @return the employeeToAdd instance
     */
    public Employee getEmployeeToAdd() {
        return employeeToAdd;
    }

    /**
     * Sets the employee instance used for adding a new employee.
     * 
     * @param employeeToAdd the employee instance to set
     */
    public void setEmployeeToAdd(Employee employeeToAdd) {
        this.employeeToAdd = employeeToAdd;
    }

    /**
     * Gets reference to employee to be assigned some responsibility.
     * 
     * @return reference to employee to be assigned some responsibility
     */
    public Employee getEmployeeToAssign() {
        return employeeToAssign;
    }

    /**
     * Sets reference to employee to be assigned some responsibility.
     * 
     * @param employeeToAssign reference to employee 
     *      to be assigned some responsibility
     */
    public void setEmployeeToAssign(Employee employeeToAssign) {
        this.employeeToAssign = employeeToAssign;
    }

    /**
     * Gets employee types.
     * 
     * @return employee types
     */
    public Map<String, EmpType> getEmpTypes() {
        return empTypes;
    }

    /**
     * Returns the list of valid pay grades.
     * 
     * @return the list of valid pay grades
     */
    public List<PayGrade> getPayGrades() {
        return payGrades;
    }

    /**
     * Sets the list of valid pay grades.
     * 
     * @param payGrades the list of pay grades to set
     */
    public void setPayGrades(List<PayGrade> payGrades) {
        EmployeeController.payGrades = payGrades;
    }

    /**
     * Throws a validation error with the specified message.
     * 
     * @param message string containing message to be displayed when expception
     *                is thrown.
     */
    public static void throwValidationError(String message) {
        throw new ValidatorException(
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        AuthController.getRb().getString(message), null));
    }

    /**
     * Validates the employee type field of a form submission.
     * 
     * @param context        the Faces context
     * @param component      the UI component
     * @param convertedValue the converted value of the component
     * @throws ValidatorException if the employee type is not valid
     */
    public void validateFormEmpTypeField(FacesContext context,
            UIComponent component, Object convertedValue) {
        if (convertedValue != null) {
            for (EmpType et : EmpType.values()) {
                if (convertedValue.equals(et.name())) {
                    return;
                }
            }
            throwValidationError("invalidEmployeeTypeErrorMsg");
        }
    }

    /**
     * Validates the pay grade field of a form submission.
     * 
     * @param context        the Faces context
     * @param component      the UI component
     * @param convertedValue the converted value of the component
     * @throws ValidatorException if the pay grade is not valid
     */
    public void validateFormPayGradeField(FacesContext context,
            UIComponent component, Object convertedValue) {
        if (convertedValue != null) {
            for (PayGrade pg : payGrades) {
                if (((PayGrade) convertedValue).equals(pg)) {
                    return;
                }
            }
            throwValidationError("invalidPaygradeErrorMsg");
        }
    }

    /**
     * Validator for a new username being added to the system. Usernames must be
     * unique.
     * 
     * @param context        faces context
     * @param component      component of the error
     * @param convertedValue value of the component
     */
    public void validateNewUsername(FacesContext context, UIComponent component,
            Object convertedValue) {
        if (getEmployeeByUsername((String) convertedValue) != null) {
            throwValidationError("usernameAlreadyExistsErrorMsg");
        }
    }

    /**
     * Validator to check whether or not an entered field corresponds to a
     * username that already exists within the system.
     * 
     * @param context        faces context
     * @param component      component of the error
     * @param convertedValue value of the component
     */
    public void validateExistingUsername(FacesContext context,
            UIComponent component, Object convertedValue) {
        if (getEmployeeByUsername((String) convertedValue) == null) {
            throwValidationError("userNonexistentErrorMsg");
        }
    }

    /**
     * Hashes password from plain text input.
     * 
     * @param plainTextPassword password as plain text
     * @return hashed password
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Checks if plain text password matches with hashed password.
     * 
     * @param plainPassword  password as plain text
     * @param hashedPassword hashed password
     * @return true if passwords match, false otherwise
     */
    public static boolean checkPassword(String plainPassword,
            String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
