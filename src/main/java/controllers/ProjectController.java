package controllers;

import access.PayGradeManager;
import access.ProjectManager;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import models.Employee;
import models.Project;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class is the controller class for managing projects in the system.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("projController")
@SessionScoped
public class ProjectController implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Class Manager. */
    @Inject
    private ProjectManager projectManager;

    /** Class Manager. */
    @Inject
    private PayGradeManager payGradeManager;

    /** Controller. */
    @Inject
    private AuthController authController;

    /** Controller. */
    @Inject
    private TreeTableWorkPackageBean ttWorkPackageBean;

    /** List of projects. */
    private List<Project> projects = new ArrayList<>();

    /** Project to be added. */
    private Project projectToAdd = new Project();

    /** Currently Selected Project. */
    private Project currentProject;

    /** Display Success Message boolean. */
    private boolean successMessageToDisplay;

    /**
     * Displays success message when project created successfully.
     */
    public void displayProjectCreationSuccessMessage() {
        if (currentProject != null) {
            if (successMessageToDisplay) {
                AuthController.getSuccessMessage(AuthController.
                    getRb().getString("successfulProjectCreationMsg"));
            }
            successMessageToDisplay = false;
        }
    }

    /**
     * Displays success message when employee successfully assigned to project.
     */
    public void displayProjectAssignmentSuccessMessage() {
        if (currentProject != null) {
            if (successMessageToDisplay) {
                AuthController.
                    getSuccessMessage(AuthController.getRb().
                    getString("successfulEmployeeProjectAssignmentMsg"));
            }
            successMessageToDisplay = false;
        }
    }

    /**
     * Clears success message from the page.
     */
    private void clearSuccessMessage() {
        Iterator<String> itIds = FacesContext.getCurrentInstance().
            getClientIdsWithMessages();
        while (itIds.hasNext()) {
            List<FacesMessage> messageList = 
                FacesContext.getCurrentInstance().getMessageList(itIds.next());
            if (!messageList.isEmpty()) {
                messageList.clear();
            }
        }
    }

    /**
     * Refreshes the list of projects for the current employee.
     */
    public void refreshProjectsList() {
        projects = projectManager.getAll();
    }

    /**
     * Navigates to the projects page and refreshes the list of projects for the
     * current employee.
     * 
     * @return the string "projects?faces-redirect=true"
     */
    public String navigateToProjectsPage() {
        refreshProjectsList();
        return "projects?faces-redirect=true";
    }

    /**
     * Navigates to the project-employees page and sets the selected project.
     * 
     * @param project the project
     * @return the string "project-employees?faces-redirect=true"
     */
    public String navigateToProjectEmployeesPage(Project project) {
        currentProject = project;
        return "project-employees?faces-redirect=true";
    }

    /**
     * Navigates to the project-work-packages page
     * and sets the selected project.
     * 
     * @param project the project
     * @return the string "project-work-packages?faces-redirect=true"
     */
    public String navigateToProjectWorkPackagesPage(Project project) {
        currentProject = project;
        ttWorkPackageBean.init();
        return "project-work-packages?faces-redirect=true";
    }

    /**
     * Gets all projects for the current employee.
     * 
     * @return all projects for the current employee
     */
    public List<Project> getAllProjects() {
        return projectManager.getAll();
    }

    /**
     * Updates all projects in the system.
     * 
     * @return null
     */
    public String updateProjects() {
        for (Project project : projects) {
            projectManager.merge(project);
        }
        refreshProjectsList();

        return null;
    }

    /**
     * Gets all projects assigned to a given employee.
     * 
     * @param employee the employee
     * @return all projects assigned to a given employee
     */
    public List<Project> getAssignedProjects(Employee employee) {
        return projectManager.getAssignedProjects(employee);
    }

    /**
     * Gets all projects assigned to/managed by a given employee.
     * 
     * @param employee the employee
     * @return all projects assigned to/managed by a given employee
     */
    public List<Project> getAssignedAndManagedProjects(Employee employee) {
        List<Project> assignedProjects = getAssignedProjects(employee);
        List<Project> managedProjects = 
            projectManager.getManagedProjects(employee.getEmpNumber());

        return Stream.concat(assignedProjects.stream(),
                managedProjects.stream()).toList();
    }

    /**
     * Adds a new project to the system. Displays a success message if project
     * is successfully created.
     * 
     * @return page navigation string
     */
    @Transactional
    public String addNewProject() {
        updateProjects();
        projectToAdd.setProjectManager(authController.getCurrentEmployee());
        projectManager.persist(projectToAdd);
        refreshProjectsList();
        resetProjectToAdd();
        clearSuccessMessage();
        successMessageToDisplay = true;
        return "projects?faces-redirect=true";
    }

    /**
     * Resets the {@code projectToAdd} attribute
     * to a new project with the current
     * employee set as the project manager.
     */
    private void resetProjectToAdd() {
        projectToAdd = new Project();
        projectToAdd.setProjectManager(authController.getCurrentEmployee());
    }

    /**
     * Validator to check whether or not an entered field corresponds to a
     * project name that already exists within the system.
     * 
     * @param context        faces context
     * @param component      component of the error
     * @param convertedValue value of the component
     */
    public void validateProjectName(FacesContext context, UIComponent component,
            Object convertedValue) {
        if (projectManager.getProjectByName((String) convertedValue) != null) {
            EmployeeController.throwValidationError(
                    "projectNameAlreadyExistsErrorMsg");
        }
    }

    /**
     * Gets a project with a given ID.
     * 
     * @param projectId the project ID
     * @return a project with a given ID
     */
    public Project getProjectById(int projectId) {
        return projectManager.getProjectById(projectId);
    }

    /**
     * Assigns employee to currently selected project
     * and navigates to project-employees page.
     * 
     * @param employee the employee
     * @return the string "project-employees?faces-redirect=true"
     */
    @Transactional
    public String assignEmployeeToProject(Employee employee) {
        currentProject.assignEmployeeToProject(employee);
        updateProjects();
        projectManager.merge(currentProject);
        clearSuccessMessage();
        successMessageToDisplay = true;
        return "project-employees?faces-redirect=true";
    }

    /**
     * Removes employee from currently selected project
     * and navigates to project-employees page.
     * 
     * @param employee the employee
     * @return the string "project-employees?faces-redirect=true"
     */
    @Transactional
    public String removeEmployeeFromProject(Employee employee) {
        currentProject.removeEmployeeFromProject(employee);
        updateProjects();
        projectManager.merge(currentProject);
        return "project-employees?faces-redirect=true";
    }

    /**
     * 
     * Returns the cost of a pay grade for the given pay grade name.
     * 
     * @param name the name of the pay grade
     * @return the cost of the pay grade for the given name and year
     */
    public float getPayGradeCostByName(String name) {
        /* Delegates to payGradeManager to get 
         the cost of the pay grade for the given
         name and year */
        return payGradeManager.getPayGradeCostByNameAndYear(
            name, LocalDate.of(LocalDate.now().getYear(), 1, 1));
    }

    /**
     * Gets all projects in the system.
     * 
     * @return all projects in the system
     */
    public List<Project> getProjects() {
        return projects;
    }

    /**
     * Sets the list of projects associated with this project controller.
     * 
     * @param projects the list of projects to set
     */
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    /**
     * Returns the project that is currently being added to the system.
     * 
     * @return the project that is currently being added to the system
     */
    public Project getProjectToAdd() {
        return projectToAdd;
    }

    /**
     * Sets the project that is currently being added to the system.
     * 
     * @param projectToAdd the project to set as
     *                     the project being added to the system
     */
    public void setProjectToAdd(Project projectToAdd) {
        this.projectToAdd = projectToAdd;
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
     * @param currentProject the currently selected project
     */
    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    /**
     * Gets the pay grade manager.
     * 
     * @return the the paygrade manager
     */
    public PayGradeManager getPayGradeManager() {
        return payGradeManager;
    }

    /**
     * Sets the pay grade manager.
     * 
     * @param payGradeManager the pay grade manager
     */
    public void setPayGradeManager(PayGradeManager payGradeManager) {
        this.payGradeManager = payGradeManager;
    }
}
