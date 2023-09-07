package controllers;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import access.BudgetManager;
import access.EmployeeManager;
import access.PayGradeManager;
import access.ProjectManager;
import access.WorkPackageManager;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import models.Budget;
import models.Employee;
import models.Project;
import models.WorkPackage;

/**
 * This class is the controller class for managing work packages in the system.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("workPackageController")
@SessionScoped
public class WorkPackageController implements Serializable {

    /**
     * Maximum Number of Children Work Packages for every parent work package.
     */
    public static final int MAX_WP_CHILDREN = 9;

    private static final long serialVersionUID = 1L;

    /** List of all work packages. */
    private static List<WorkPackage> workPackages;

    /** Controller. */
    @Inject
    private AuthController authController;

    /** Class Manager. */
    @Inject
    private WorkPackageManager workPackageManager;

    /** Class Manager. */
    @Inject
    private BudgetManager budgetManager;

    /** Class Manager. */
    @Inject
    private EmployeeManager employeeManager;

    /** Class Manager. */
    @Inject
    private ProjectManager projectManager;

    /** Class Manager. */
    @Inject
    private PayGradeManager payGradeManager;

    /** Controller. */
    @Inject
    private ProjectController projectController;

    /** Controller. */
    @Inject
    private TreeTableWorkPackageBean ttWorkPackageBean;

    /** WorkPackage object used as a temporary container. */
    private WorkPackage workPackageToAdd = new WorkPackage();

    /**
     * Budget object used as a temporaty container
     * for responsible engineer estimate.
     */
    private Budget engineerEstimateToAdd = new Budget();

    /** Budget object used as a temporaty container for rolling estimate. */
    private Budget rollingEstimateToAdd;

    /** Display Success Message boolean. */
    private boolean successMessageToDisplay;

    /** Display Success Message for register creation boolean. */
    private boolean creationSuccessMessageToDisplay;

    /** Display Success Message for register deletion boolean. */
    private boolean deletionSuccessMessageToDisplay;

    /** Currently selected work package. */
    private WorkPackage currentWorkPackage;

    /** Editable State boolean. */
    private boolean editable;

    /** Today's Date. */
    private LocalDate todaysDate = LocalDate.now();

    /**
     * Refreshes loaded list of work packages.
     */
    @PostConstruct
    public void refreshLists() {
        workPackages = workPackageManager.getAll();
    }

    /**
     * Deletes a work package from the system, updates data based on deletion,
     * and navigates to project-work-packages page.
     * 
     * @param workPackage the work package
     * @return the string "project-work-packages?faces-redirect=true"
     */
    @Transactional
    public String deleteWorkPackage(WorkPackage workPackage) {
        if (workPackage.getWorkPackageParent() != null) {
            WorkPackage parent = workPackages.get(workPackages.indexOf(
                    workPackage.getWorkPackageParent()));
            removeChildFromParent(workPackage, parent);
        }
        List<Project> projects = projectController.getProjects();
        Project project = projects.get(projects.indexOf(
                workPackage.getProject()));
        project.getWorkPackages().remove(workPackage);
        projectManager.merge(project);
        projectController.refreshProjectsList();
        projects.set(projects.indexOf(project), project);
        refreshLists();
        deleteWorkPackageBudgets(workPackage);
        updateWorkPackages();
        updateParentBudgets(workPackage);
        clearSuccessMessage();
        deletionSuccessMessageToDisplay = true;
        ttWorkPackageBean.saveExpandedState(ttWorkPackageBean.getRoot());
        refreshListsAndBean();
        return "project-work-packages?faces-redirect=true";
    }

    /**
     * Removes child work package from parent work package.
     * 
     * @param workPackage the child work package
     * @param parent      the parent work package
     */
    @Transactional
    private void removeChildFromParent(WorkPackage workPackage,
            WorkPackage parent) {
        if (workPackage != null) {
            parent.getWorkPackageChildren().remove(workPackage);
            workPackageManager.merge(parent);
        }
    }

    /**
     * Deletes budgets related to a given work package.
     * 
     * @param workPackage the work package
     */
    @Transactional
    private void deleteWorkPackageBudgets(WorkPackage workPackage) {
        budgetManager.delete(workPackage.getEngineerEstimate());
        budgetManager.delete(workPackage.getRollingEstimate());
    }

    /**
     * Removes all employees from a given work package.
     * 
     * @param workPackage the work package
     */
    @Transactional
    private void removeEmployeesFromWorkPackage(WorkPackage workPackage) {
        for (Employee employee : workPackage.getAssignedEmployees()) {
            workPackage.removeEmployeeFromWorkPackage(employee);
            employeeManager.merge(employee);
        }

        workPackageManager.merge(workPackage);
    }

    /**
     * Adds a new work package to the given project and parent work package,
     * and navigates to the project-work-packages page.
     * 
     * @param project the project
     * @param parent  the parent work package
     * @return the string "project-work-packages?faces-redirect=true"
     */
    @Transactional
    public String addNewWorkPackage(Project project, WorkPackage parent) {
        updateWorkPackages();
        persistNewBudgets();
        setWorkPackageProperties(project, parent);
        workPackageManager.persist(workPackageToAdd);
        if (!parent.isPlaceholder()) {
            workPackageManager.merge(parent);
        }
        updateParentBudgets(workPackageToAdd);
        resetPlaceholderInstances();
        updateWorkPackages();
        clearSuccessMessage();
        creationSuccessMessageToDisplay = true;
        ttWorkPackageBean.saveExpandedState(ttWorkPackageBean.getRoot());
        refreshListsAndBean();
        return "project-work-packages?faces-redirect=true";
    }

    /**
     * Persist new responsible engineer
     * and rolling budget estimates into database.
     */
    private void persistNewBudgets() {
        budgetManager.persist(engineerEstimateToAdd);
        rollingEstimateToAdd = new Budget(engineerEstimateToAdd.getJS(),
                engineerEstimateToAdd.getSS(), engineerEstimateToAdd.getDS(),
                engineerEstimateToAdd.getP1(), engineerEstimateToAdd.getP2(),
                engineerEstimateToAdd.getP3(), engineerEstimateToAdd.getP4(),
                engineerEstimateToAdd.getP5(), engineerEstimateToAdd.getP6());
        budgetManager.persist(rollingEstimateToAdd);
    }

    /**
     * Sets properties of work package to add
     * (Project it belongs to, parent work package, ID,
     * responsible engineer's budget estimate, and
     * rolling budget estimate).
     * 
     * @param project project work package to add belongs to
     * @param parent  parent work package
     */
    @Transactional
    private void setWorkPackageProperties(Project project, WorkPackage parent) {
        workPackageToAdd.setProject(project);
        if (!parent.isPlaceholder()) {
            workPackageToAdd.setWorkPackageParent(parent);
            parent.getWorkPackageChildren().add(workPackageToAdd);
            workPackageManager.merge(parent);
        }
        workPackageToAdd.setId(generateWorkPackageId(workPackageToAdd));
        System.out.println("id set to " + workPackageToAdd.getId());
        workPackageToAdd.setEngineerEstimate(engineerEstimateToAdd);
        workPackageToAdd.setRollingEstimate(rollingEstimateToAdd);
    }

    /**
     * Resets Placeholder Addition Objects.
     */
    private void resetPlaceholderInstances() {
        engineerEstimateToAdd = new Budget();
        workPackageToAdd = new WorkPackage();
    }

    /**
     * Refreshes loaded list of work packages and
     * work package tree.
     */
    private void refreshListsAndBean() {
        refreshLists();
        ttWorkPackageBean.init();
    }

    /**
     * Gets the work package with the given ID.
     * 
     * @param packageId the work package ID
     * @return the work package with the given ID
     */
    public WorkPackage getWorkPackageById(String packageId) {
        return workPackageManager.getWorkPackageById(packageId);
    }

    /**
     * Merges changes made to work packages in the database.
     */
    public void updateWorkPackages() {
        for (WorkPackage wp : workPackages) {
            System.out.println("attempting to update wp: " + wp.getId());
            workPackageManager.merge(wp);
        }

        refreshLists();
    }

    /**
     * Displays success message when work package created successfully.
     */
    public void displayWorkPackageCreationSuccessMessage() {
        if (currentWorkPackage != null) {
            if (creationSuccessMessageToDisplay) {
                AuthController.getSuccessMessage(AuthController.
                    getRb().getString("successfulWorkPackageCreationMsg"));
            }
            creationSuccessMessageToDisplay = false;
            ttWorkPackageBean.restoreExpandedState(ttWorkPackageBean.getRoot());
        }
    }

    /**
     * Displays success message when work package deleted successfully.
     */
    public void displayWorkPackageDeletionSuccessMessage() {
        if (currentWorkPackage != null) {
            if (deletionSuccessMessageToDisplay) {
                AuthController.getSuccessMessage(AuthController.getRb().
                    getString("successfulWorkPackageDeletionMsg"));
            }
            deletionSuccessMessageToDisplay = false;
        }
    }

    /**
     * Displays success message when work package assigned successfully.
     */
    public void displayWorkPackageAssignmentSuccessMessage() {
        if (currentWorkPackage != null) {
            if (successMessageToDisplay) {
                AuthController.
                    getSuccessMessage(
                        AuthController.getRb().getString(
                            "successfulEmployeeWorkPackageAssignmentMsg"));
            }
            successMessageToDisplay = false;
        }
    }

    /**
     * Clears success message from the page.
     */
    private void clearSuccessMessage() {
        Iterator<String> itIds = 
            FacesContext.getCurrentInstance().getClientIdsWithMessages();
        while (itIds.hasNext()) {
            List<FacesMessage> messageList = 
                FacesContext.getCurrentInstance().getMessageList(itIds.next());
            if (!messageList.isEmpty()) {
                messageList.clear();
            }
        }
    }

    /**
     * Navigates to work package page and selects work package.
     * 
     * @param workPackage the work package to be selected
     * @return the string "work-package?faces-redirect=true"
     */
    public String navigateToWorkPackage(WorkPackage workPackage) {
        currentWorkPackage = workPackage;
        editable = false;
        return "work-package?faces-redirect=true";
    }

    /**
     * Navigates to work-package-employees page and selects work package.
     * 
     * @param workPackage the work package to be selected
     * @return the string "work-package-employees?faces-redirect=true"
     */
    public String navigateToWorkPackageEmployeesPage(WorkPackage workPackage) {
        currentWorkPackage = workPackage;
        return "work-package-employees?faces-redirect=true";
    }

    /**
     * Merges changes made to a work package in the database
     * and navigates to the work-package page.
     * 
     * @return the string "work-package?faces-redirect=true"
     */
    @Transactional
    public String saveCurrentWorkPackage() {
        System.out.println("fetched name: " + currentWorkPackage.getName());
        workPackageManager.merge(currentWorkPackage);
        budgetManager.merge(currentWorkPackage.getEngineerEstimate());
        budgetManager.merge(currentWorkPackage.getRollingEstimate());
        updateParentBudgets(currentWorkPackage);
        editable = false;
        System.out.println("Saved");
        System.out.println(currentWorkPackage.getName());
        return "work-package?faces-redirect=true";
    }

    /**
     * Assigns a given employee to the currently selected work package
     * and navigates to the work-package-employees page.
     * 
     * @param employee the employee
     * @return the string "work-package-employees?faces-redirect=true"
     */
    @Transactional
    public String assignEmployeeToWorkPackage(Employee employee) {
        currentWorkPackage.assignEmployeeToWorkPackage(employee);
        updateWorkPackages();
        workPackageManager.merge(currentWorkPackage);
        clearSuccessMessage();
        successMessageToDisplay = true;
        return "work-package-employees?faces-redirect=true";
    }

    /**
     * Removes a given employee from the currently selected work package
     * and navigates to the work-package-employees page.
     * 
     * @param employee the employee
     * @return the string "work-package-employees?faces-redirect=true"
     */
    @Transactional
    public String removeEmployeeFromWorkPackage(Employee employee) {
        currentWorkPackage.removeEmployeeFromWorkPackage(employee);
        updateWorkPackages();
        workPackageManager.merge(currentWorkPackage);
        return "work-package-employees?faces-redirect=true";
    }

    /**
     * Updates the responsible engineer and rolling budget estimates
     * of all work packages above a given work package in the work package tree.
     * 
     * @param workPackage the work package
     */
    public void updateParentBudgets(WorkPackage workPackage) {
        if (workPackage.getWorkPackageParent() != null) {
            updateParentBudget(workPackage, "engineerEstimate");
            updateParentBudget(workPackage, "rollingEstimate");
            updateParentBudgets(workPackage.getWorkPackageParent());
        }
    }

    /**
     * Updates the given budget type
     * of the parent of a given work package.
     * 
     * @param workPackage  the work package
     * @param typeOfBudget the budget type
     */
    @Transactional
    public void updateParentBudget(
            WorkPackage workPackage, String typeOfBudget) {

        WorkPackage parent = workPackages.get(
                workPackages.indexOf(workPackage.getWorkPackageParent()));

        System.out.println("updating " + typeOfBudget + " for "
                + parent.getId());

        float jsTotal = 0;
        float ssTotal = 0;
        float dsTotal = 0;
        float p1Total = 0;
        float p2Total = 0;
        float p3Total = 0;
        float p4Total = 0;
        float p5Total = 0;
        float p6Total = 0;

        List<Budget> budgetsToAdd = typeOfBudget.equals("engineerEstimate")
                ? workPackageManager.getEngineerEstimatesOfChildren(parent)
                : workPackageManager.getRollingEstimatesOfChildren(parent);

        System.out.println("size of estimates of children: "
                + budgetsToAdd.size());

        for (Budget budget : budgetsToAdd) {
            jsTotal += budget.getJS();
            ssTotal += budget.getSS();
            dsTotal += budget.getDS();
            p1Total += budget.getP1();
            p2Total += budget.getP2();
            p3Total += budget.getP3();
            p4Total += budget.getP4();
            p5Total += budget.getP5();
            p6Total += budget.getP6();
        }

        Budget updatedEstimate = new Budget(
                jsTotal, ssTotal, dsTotal, p1Total,
                p2Total, p3Total, p4Total, p5Total, p6Total);

        budgetManager.persist(updatedEstimate);
        System.out.println("new " + typeOfBudget + " persisted");

        if (typeOfBudget.equals("engineerEstimate")) {
            Budget oldEngineerEstimate = parent.getEngineerEstimate();
            parent.setEngineerEstimate(updatedEstimate);
            budgetManager.delete(oldEngineerEstimate);

        } else {
            Budget oldRollingEstimate = parent.getRollingEstimate();
            parent.setRollingEstimate(updatedEstimate);
            budgetManager.delete(oldRollingEstimate);
        }

        System.out.println("new budgets set and old budgets deleted");
        workPackageManager.merge(parent);
        System.out.println("new " + typeOfBudget + " completed");
    }

    /**
     * Gets the cost of the budget estimate
     * for the given budget type and project.
     * 
     * @param project      the project
     * @param estimateType the budget type
     * @return the cost of the budget estimate
     *         for the given budget type and project
     */
    public float getProjectBudgetEstimate(
            Project project, String estimateType) {

        System.out.println("method running");
        List<WorkPackage> firstLevelWorkPackages = 
            workPackageManager.getAllFirstLevelWorkPackagesForProject(project);
        float result = 0;

        for (WorkPackage wp : firstLevelWorkPackages) {
            Budget estimate = estimateType.equals("engineerEstimate")
                    ? wp.getEngineerEstimate()
                    : wp.getRollingEstimate();

            result += estimate.getJS()
                    * payGradeManager.getPayGradeCostByNameAndYear(
                            "JS", LocalDate.of(LocalDate.now().getYear(), 
                            1, 1));
            result += estimate.getDS()
                    * payGradeManager.getPayGradeCostByNameAndYear(
                            "DS", LocalDate.of(LocalDate.now().getYear(), 
                            1, 1));
            result += estimate.getSS()
                    * payGradeManager.getPayGradeCostByNameAndYear(
                            "SS", LocalDate.of(LocalDate.now().getYear(), 
                            1, 1));
            result += estimate.getP1()
                    * payGradeManager.getPayGradeCostByNameAndYear(
                            "P1", LocalDate.of(LocalDate.now().getYear(), 
                            1, 1));
            result += estimate.getP2()
                    * payGradeManager.getPayGradeCostByNameAndYear(
                            "P2", LocalDate.of(LocalDate.now().getYear(), 
                            1, 1));
            result += estimate.getP3()
                    * payGradeManager.getPayGradeCostByNameAndYear(
                            "P3", LocalDate.of(LocalDate.now().getYear(), 
                            1, 1));
            result += estimate.getP4()
                    * payGradeManager.getPayGradeCostByNameAndYear(
                            "P4", LocalDate.of(LocalDate.now().getYear(), 
                            1, 1));
            result += estimate.getP5()
                    * payGradeManager.getPayGradeCostByNameAndYear(
                            "P5", LocalDate.of(LocalDate.now().getYear(), 
                            1, 1));
            result += estimate.getP6()
                    * payGradeManager.getPayGradeCostByNameAndYear(
                            "P6", LocalDate.of(LocalDate.now().getYear(), 
                            1, 1));
        }

        return result;
    }

    /**
     * Generates the work package ID for the given work package.
     * 
     * @param workPackage the work package
     * @return the work package ID for the given work package
     */
    public String generateWorkPackageId(WorkPackage workPackage) {
        WorkPackage parent = workPackage.getWorkPackageParent();
        String parentId = (parent == null ? null : parent.getId());

        if (parentId == null || parentId.isEmpty()) {
            return generateFirstLevelWorkPackageId(workPackage);
        } else {
            return generateChildWorkPackageId(parentId);
        }
    }

    /**
     * Checks if work package has reached
     * the maximum amount of child work packages.
     * 
     * @param workPackage the work package
     * @return true if work package has reached
     *         the maximum amount of child work packages, false otherwise
     */
    public boolean checkIfChildrenMaxedOut(WorkPackage workPackage) {
        Project workPackageProject = workPackage.getProject();

        if (workPackage.getName().equals(workPackageProject.getProjectName())) {
            return workPackageManager.getAllFirstLevelWorkPackagesForProject(
                    workPackageProject).size() >= MAX_WP_CHILDREN;
        } else {
            return (workPackage.getWorkPackageChildren().size() 
                >= MAX_WP_CHILDREN);
        }
    }

    /**
     * Generates work package ID for a first level work package.
     * 
     * @param workPackage the first level work package
     * @return work package ID for a first level work package
     */
    private String generateFirstLevelWorkPackageId(WorkPackage workPackage) {
        System.out.println("generating first level WP");
        StringBuilder id = new StringBuilder();

        id = id.append("P").append(
            workPackage.getProject().getProjectId()).append("WP");
        List<String> firstLevelIds = workPackageManager.
            getAllFirstLevelIdsForProject(workPackage.getProject());

        if (firstLevelIds.isEmpty()) {
            return id.append("1").toString();
        }

        Collections.sort(firstLevelIds);

        final int BEGIN_INDEX = 4;
        int lastNumber = 0;
        for (String firstLevelId : firstLevelIds) {
            int number = Integer.parseInt(firstLevelId.substring(BEGIN_INDEX));
            if (number - lastNumber > 1) {
                return id.append(lastNumber + 1).toString();
            }
            lastNumber = number;
        }

        // If we reach this point, all IDs are consecutive
        return id.append(lastNumber + 1).toString();
    }

    /**
     * Generates work package ID for child work package.
     * 
     * @param parentId the parent of the child work package
     * @return work package ID for child work package
     */
    private String generateChildWorkPackageId(String parentId) {
        List<String> siblingIds = workPackageManager.getChildrenIds(parentId);

        if (siblingIds.isEmpty()) {
            return parentId + "1";
        }

        Collections.sort(siblingIds);

        int lastNumber = 0;
        for (String id : siblingIds) {
            int number = Integer.parseInt(id.substring(parentId.length()));
            if (number - lastNumber > 1) {
                return parentId + (lastNumber + 1);
            }
            lastNumber = number;
        }

        // If we reach this point, all IDs are consecutive
        return parentId + (lastNumber + 1);
    }

    /**
     * Flips the state of the work packages' editable state boolean.
     */
    public void toggleEditable() {
        System.out.println("toggleEditable()");
        editable = !editable;
    }

    /**
     * Gets the list of all work packages.
     * 
     * @return the list of all work packages
     */
    public static List<WorkPackage> getWorkPackages() {
        return workPackages;
    }

    /**
     * Sets the list of all work packages.
     * 
     * @param workPackages the list of all work packages
     */
    public static void setWorkPackages(List<WorkPackage> workPackages) {
        WorkPackageController.workPackages = workPackages;
    }

    /**
     * Gets the work package to add.
     * 
     * @return the work package to add
     */
    public WorkPackage getWorkPackageToAdd() {
        return workPackageToAdd;
    }

    /**
     * Sets the work package to add.
     * 
     * @param workPackageToAdd the work package to add
     */
    public void setWorkPackageToAdd(WorkPackage workPackageToAdd) {
        this.workPackageToAdd = workPackageToAdd;
    }

    /**
     * Gets the responsible engineer's budget estimate to add.
     * 
     * @return the responsible engineer's budget estimate to add
     */
    public Budget getEngineerEstimateToAdd() {
        return engineerEstimateToAdd;
    }

    /**
     * Sets the responsible engineer's budget estimate to add.
     * 
     * @param engineerEstimateToAdd the responsible engineer's budget
     *                              estimate to add
     */
    public void setEngineerEstimateToAdd(Budget engineerEstimateToAdd) {
        this.engineerEstimateToAdd = engineerEstimateToAdd;
    }

    /**
     * Gets the rolling budget estimate to add.
     * 
     * @return the rolling budget estimate to add
     */
    public Budget getRollingEstimateToAdd() {
        return rollingEstimateToAdd;
    }

    /**
     * Sets the rolling budget estimate to add.
     * 
     * @param rollingEstimateToAdd the rolling budget estimate to add
     */
    public void setRollingEstimateToAdd(Budget rollingEstimateToAdd) {
        this.rollingEstimateToAdd = rollingEstimateToAdd;
    }

    /**
     * Gets the currently selected work package.
     * 
     * @return the currently selected work package
     */
    public WorkPackage getCurrentWorkPackage() {
        return currentWorkPackage;
    }

    /**
     * Sets the currently selected work package.
     * 
     * @param currentWorkPackage the selected work package
     */
    public void setCurrentWorkPackage(WorkPackage currentWorkPackage) {
        this.currentWorkPackage = currentWorkPackage;
    }

    /**
     * Gets today's date.
     * 
     * @return today's date
     */
    public LocalDate getTodaysDate() {
        return todaysDate;
    }

    /**
     * Sets today's date.
     * 
     * @param todaysDate today's date
     */
    public void setTodaysDate(LocalDate todaysDate) {
        this.todaysDate = todaysDate;
    }

    /**
     * Gets the state of the editable state boolean for work packages.
     * 
     * @return the state of the editable state boolean for work packages
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets the state of the editable state boolean for work packages.
     * 
     * @param editable the state of the editable state boolean for work packages
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
