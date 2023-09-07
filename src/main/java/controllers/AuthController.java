package controllers;

import access.EmployeeManager;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.ConfigurableNavigationHandler;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import models.Employee;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class represents the authentication controller used for user
 * authentication and session management. The class is responsible for handling
 * user login, logout and password reset functionality.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("authController")
@SessionScoped
public class AuthController implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Messages bundle used to create success and error messages.
     */
    private static ResourceBundle rb = ResourceBundle.getBundle("messages");

    /** Login Credentials. */
    private static Map<String, String> credentialMap;

    /** Default Initial Password. */
    private static final String TEMPORARY_PASSWORD = "temp";

    /** Class Manager. */
    @Inject
    private EmployeeManager employeeManager;

    /** Employee attempting to login. */
    private Employee employeeLoggingIn = new Employee();

    /** Employee currently logged in. */
    private Employee currentEmployee;

    /** Username used for password reset. */
    private String usernameForPasswordReset;

    /** Password used for password reset. */
    private String newPasswordForPasswordReset;

    /** Password confirmation used for password reset. */
    private String confirmNewPasswordForPasswordReset;

    /** Deactivated Employee Accounts. */
    private List<String> deactivatedEmployees;

    /** Update loaded credential information. */
    public void updateCredentials() {
        credentialMap = employeeManager.getAllCredentials();
        deactivatedEmployees = 
            employeeManager.getDeactivatedEmployeeUsernames();
    }

    /**
     * Authenticates the user with the entered username and password.
     *
     * @return navigation string if authentication is successful,
     *         null otherwise.
     */
    public String authenticate() {
        updateCredentials();

        // active, valid username, temporary password stored in DB
        if (!deactivatedEmployees.contains(employeeLoggingIn.getUsername())
                && credentialMap.containsKey(employeeLoggingIn.getUsername())
                && credentialMap.get(employeeLoggingIn.getUsername()).
                    equals(TEMPORARY_PASSWORD)) {
            // valid password entered (i.e. TEMPORARY_PASSWORD)
            if (credentialMap.get(employeeLoggingIn.getUsername()).
                equals(employeeLoggingIn.getPassword())) {
                return handleAuthenticatedEmployee(
                        credentialMap.get(employeeLoggingIn.getUsername()));
            }
            // active valid credentials, set, hashed password
        } else {
            if (!deactivatedEmployees.contains(employeeLoggingIn.getUsername())
                    && credentialMap.containsKey(
                            employeeLoggingIn.getUsername())
                    && EmployeeController.checkPassword(
                            employeeLoggingIn.getPassword(), 
                            credentialMap.get(
                                employeeLoggingIn.getUsername()))) {

                return handleAuthenticatedEmployee(
                        credentialMap.get(employeeLoggingIn.getUsername()));
            }
        }
        // invalid credentials
        getErrorMessage(rb.getString("invalidLoginCredentialsErrorMsg"));
        return null;
    }

    /**
     * Starts employee's session and performs page navigation.
     * 
     * @param hashedPassword hashed password
     * @return page navigation string
     */
    private String handleAuthenticatedEmployee(String hashedPassword) {
        Employee authenticatedEmployee = employeeManager.get(
            employeeLoggingIn.getUsername(), hashedPassword);

        currentEmployee = authenticatedEmployee;
        employeeLoggingIn = new Employee();

        EmployeeController.setEmployees(employeeManager.getAll());

        return "home?faces-redirect=true";
    }

    /**
     * Creates a success Faces Message and adds it to the JSF side of the app.
     *
     * @param message string containing the success message to be displayed
     */
    public static void getSuccessMessage(String message) {
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO,
                message, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    /**
     * Creates an error Faces Message and adds it to the JSF side of the app.
     *
     * @param message string containing the error message to be displayed
     */
    public static void getErrorMessage(String message) {
        FacesMessage facesMessage = new FacesMessage(
                FacesMessage.SEVERITY_ERROR, message, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    /**
     * Determines whether an employee is currently logged in or not.
     * 
     * @return true if an employee is logged in, false otherwise
     */
    public boolean employeeIsLoggedIn() {
        return currentEmployee != null;
    }

    /**
     * Resets an employee's password (performed by the employee in question).
     *
     * @return navigation string.
     */
    public String resetPassword() {
        if (usernameForPasswordReset == null) {
            updatePassword(currentEmployee);
        } else {
            updatePassword(EmployeeController.getEmployeeByUsername(
                usernameForPasswordReset));
        }

        return null;
    }

    /**
     * Updates an employee's password.
     * 
     * @param employee the employee whose password is to be updated.
     */
    private void updatePassword(Employee employee) {
        if (newPasswordForPasswordReset.equals(
                confirmNewPasswordForPasswordReset)) {
            employee.setPassword(EmployeeController.hashPassword(
                    newPasswordForPasswordReset));
            employeeManager.merge(employee);
            getSuccessMessage(rb.getString("successfulPasswordResetMsg"));
            usernameForPasswordReset = null;
            newPasswordForPasswordReset = null;
            confirmNewPasswordForPasswordReset = null;
        } else {
            getErrorMessage(rb.getString("passwordResetErrorMsg"));
        }
    }

    /**
     * Redirects the user to the appropriate "home" page, depending on whether
     * or not they are logged in.
     * 
     * @return A string containing "home" if the user is logged in, a string
     *         containing "index" if the user is not logged in.
     */
    public String onClickHome() {
        return employeeIsLoggedIn() ? "home" : "index";
    }

    /**
     * Logs out the current employee by setting the current employee to null and
     * resetting any password reset fields.
     * Returns the navigation string to the index page.
     * 
     * @return a string containing the navigation information to the index page.
     */
    public String logout() {
        currentEmployee = null;
        usernameForPasswordReset = null;
        newPasswordForPasswordReset = null;
        confirmNewPasswordForPasswordReset = null;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().
            getSession(false);
        session.invalidate();

        return "index?faces-redirect=true";
    }

    /**
     * Checks session timeout status and performs page navigation
     * if session timeouts.
     */
    public void checkSessionTimeout() {
        HttpSession session = (HttpSession) FacesContext.
            getCurrentInstance().getExternalContext().getSession(false);
        if (session == null) {
            ConfigurableNavigationHandler nav = 
                (ConfigurableNavigationHandler) FacesContext.
                    getCurrentInstance().getApplication().
                    getNavigationHandler();
            nav.performNavigation("index?faces-redirect=true");
        }
    }

    /**
     * Gets the current employee who is logged in.
     * 
     * @return the current employee who is logged in
     */
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    /**
     * Sets the current employee who is logged in.
     * 
     * @param currentEmployee the current employee who is logged in
     */
    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    /**
     * Gets messages bundle for success and error messages.
     * 
     * @return the messages bundle for success and error messages
     */
    public static ResourceBundle getRb() {
        return rb;
    }

    /**
     * Gets the employee currently logging in.
     * 
     * @return the employee currently logging in
     */
    public Employee getEmployeeLoggingIn() {
        return employeeLoggingIn;
    }

    /**
     * Sets the employee currently logging in.
     * 
     * @param employeeLoggingIn the employee currently logging in
     */
    public void setEmployeeLoggingIn(Employee employeeLoggingIn) {
        this.employeeLoggingIn = employeeLoggingIn;
    }

    /**
     * Gets the username for resetting a password.
     * 
     * @return the username for resetting a password
     */
    public String getUsernameForPasswordReset() {
        return usernameForPasswordReset;
    }

    /**
     * Sets the username for resetting a password.
     * 
     * @param usernameForPasswordReset the username for resetting a password
     */
    public void setUsernameForPasswordReset(String usernameForPasswordReset) {
        this.usernameForPasswordReset = usernameForPasswordReset;
    }

    /**
     * Gets the new password for resetting a password.
     * 
     * @return the new password for resetting a password
     */
    public String getNewPasswordForPasswordReset() {
        return newPasswordForPasswordReset;
    }

    /**
     * Sets the new password for resetting a password.
     * 
     * @param newPasswordForPasswordReset the new password
     *                                    for resetting a password
     */
    public void setNewPasswordForPasswordReset(
            String newPasswordForPasswordReset) {
        this.newPasswordForPasswordReset = newPasswordForPasswordReset;
    }

    /**
     * Gets the confirmation of the new password for resetting a password.
     * 
     * @return the confirmation of the new password for resetting a password
     */
    public String getConfirmNewPasswordForPasswordReset() {
        return confirmNewPasswordForPasswordReset;
    }

    /**
     * Sets the confirmation of the new password for resetting a password.
     * 
     * @param confirmNewPasswordForPasswordReset the confirmation of the new
     *      password for resetting a password
     */
    public void setConfirmNewPasswordForPasswordReset(
            String confirmNewPasswordForPasswordReset) {
        this.confirmNewPasswordForPasswordReset = 
            confirmNewPasswordForPasswordReset;
    }
}
