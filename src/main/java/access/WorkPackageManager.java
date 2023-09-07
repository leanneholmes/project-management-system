package access;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import models.WorkPackage;
import models.Budget;
import models.Employee;
import models.Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides access to the WorkPackage database table.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Dependent
@Stateless
public class WorkPackageManager implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Entity Manager. */
    @PersistenceContext(unitName = "pms-jpa")
    private EntityManager em;

    /**
     * Returns a list of all work packages from the database.
     *
     * @return a list of all work packages
     */
    public List<WorkPackage> getAll() {
        return em.createQuery("SELECT DISTINCT wp FROM WorkPackage wp "
                + "LEFT JOIN FETCH wp.assignedEmployees",
                WorkPackage.class).getResultList();
    }

    /**
     * Gets all work packages for the given project ID.
     * 
     * @param projectId the project ID
     * @return all work packages for the given project ID
     */
    public List<WorkPackage> getAllForProject(int projectId) {
        System.out.println("getting WPs for project " + projectId);
        TypedQuery<WorkPackage> query = em.createQuery(
                "SELECT DISTINCT wp FROM WorkPackage wp "
                        + "LEFT JOIN FETCH wp.assignedEmployees "
                        + "WHERE wp.project.projectId = "
                        + ":projectId",
                WorkPackage.class);

        return query.setParameter("projectId", projectId).getResultList();
    }

    /**
     * Gets all work packages for the given employee.
     * 
     * @param employee the employee
     * @return all work packages for the given employee
     */
    public List<WorkPackage> getAllForEmployee(Employee employee) {
        TypedQuery<WorkPackage> query = em.createQuery(
                "SELECT DISTINCT wp FROM WorkPackage wp "
                        + "LEFT JOIN FETCH wp.assignedEmployees "
                        + "WHERE :employee MEMBER OF wp.assignedEmployees",
                WorkPackage.class);

        return query.setParameter("employee", employee).getResultList();
    }

    /**
     * Get all children work packages for the given work package ID.
     * 
     * @param parentId the work package ID
     * @return all children work packages for the given work package ID
     */
    public List<WorkPackage> getChildren(String parentId) {
        TypedQuery<WorkPackage> query = em.createQuery(
                "SELECT DISTINCT wp FROM WorkPackage wp "
                        + "LEFT JOIN FETCH wp.assignedEmployees "
                        + "WHERE wp.workPackageParent.id = :parentId",
                WorkPackage.class);

        return query.setParameter("parentId", parentId).getResultList();
    }

    /**
     * Gets all first level work packages (work packages with no parent)
     * for a given project.
     * 
     * @param project the project
     * @return list of all first level work packages for a given project
     */
    public List<WorkPackage> getAllFirstLevelWorkPackagesForProject(
            Project project) {

        TypedQuery<WorkPackage> query = em.createQuery(
                "SELECT DISTINCT wp FROM WorkPackage wp "
                        + "LEFT JOIN FETCH wp.assignedEmployees "
                        + "WHERE wp.project = :project "
                        + "AND wp.workPackageParent IS NULL",
                WorkPackage.class);
        return query.setParameter("project", project).getResultList();
    }

    /**
     * Gets all first level work packages' (work packages with no parent) IDs
     * for a given project.
     * 
     * @param project the project
     * @return list of all first level work packages' IDs for a given project
     */
    public List<String> getAllFirstLevelIdsForProject(Project project) {
        TypedQuery<String> query = em.createQuery(
                "SELECT wp.id FROM WorkPackage wp "
                        + "WHERE wp.project = :project "
                        + "AND wp.workPackageParent "
                        + "IS NULL ORDER BY SUBSTRING(wp.id, LENGTH(wp.id))",
                String.class);
        return query.setParameter("project", project).getResultList();
    }

    /**
     * Get all children work packages' IDs for the given work package ID.
     * 
     * @param parentId the work package ID
     * @return all children work packages' IDs for the given work package ID
     */
    public List<String> getChildrenIds(String parentId) {
        System.out.println("getting child Ids");
        TypedQuery<String> query = em.createQuery(
                "SELECT wp.id FROM WorkPackage wp "
                        + "WHERE wp.workPackageParent.id = :parentId "
                        + "ORDER BY SUBSTRING(wp.id, LENGTH(wp.id))",
                String.class);

        return query.setParameter("parentId", parentId).getResultList();
    }

    /**
     * Gets the work package with the given ID.
     * 
     * @param id the work package ID
     * @return the work package with the given ID
     */
    public WorkPackage getWorkPackageById(String id) {
        return em.find(WorkPackage.class, id);
    }

    /**
     * Persists a new work package into the database.
     *
     * @param workPackage the work package to persist
     */
    public void persist(WorkPackage workPackage) {
        em.persist(workPackage);
        em.flush();
    }

    /**
     * Merges changes made to a work package in the database.
     *
     * @param workPackage the work package to merge
     */
    public void merge(WorkPackage workPackage) {
        if (!workPackage.getWorkPackageChildren().isEmpty()) {
            System.out.println("parent being merged has at least one child");
        }
        em.merge(workPackage);
        em.flush();
    }

    /**
     * Deletes a work package from the database.
     *
     * @param workPackage the work package to delete
     */
    public void delete(WorkPackage workPackage) {
        WorkPackage mergedWorkPackage = em.merge(workPackage);
        em.persist(mergedWorkPackage);
        em.remove(mergedWorkPackage);
        em.flush();
    }

    /**
     * Gets the reponsible engineer's budget estimate
     * for the given work package.
     * 
     * @param workPackage the work package
     * @return the reponsible engineer's budget estimate
     *         for the given work package
     */
    public Budget getEngineerEstimateByWorkPackage(WorkPackage workPackage) {
        TypedQuery<Budget> query = em.createQuery(
                "SELECT b FROM Budget b WHERE b.id = :budgetId",
                Budget.class);

        return query.setParameter("budgetId",
                workPackage.getEngineerEstimate().getId()).getSingleResult();
    }

    /**
     * Gets the rolling budget estimate for the given work package.
     * 
     * @param workPackage the work package
     * @return the rolling budget estimate for the given work package
     */
    public Budget getRollingEstimateByWorkPackage(WorkPackage workPackage) {
        TypedQuery<Budget> query = em.createQuery(
                "SELECT b FROM Budget b WHERE b.id = :budgetId", Budget.class);

        return query.setParameter("budgetId",
                workPackage.getRollingEstimate().getId()).getSingleResult();
    }

    /**
     * Gets the responsible engineer's budget estimates
     * of all children work packages for the given work package.
     * 
     * @param parent the work package
     * @return the responsible engineer's budget estimates
     *         of all children work packages for the given work package
     */
    public List<Budget> getEngineerEstimatesOfChildren(WorkPackage parent) {
        List<Budget> engineerEstimatesOfChildren = new ArrayList<>();

        for (WorkPackage child : parent.getWorkPackageChildren()) {
            TypedQuery<Budget> query = em.createQuery(
                    "SELECT b FROM Budget b WHERE b.id = :budgetId", 
                    Budget.class);
            query.setParameter("budgetId", child.getEngineerEstimate().getId());
            Budget result = query.getSingleResult();
            if (result != null) {
                engineerEstimatesOfChildren.add(result);
            }
        }

        return engineerEstimatesOfChildren;
    }

    /**
     * Gets the rolling budget estimates
     * of all children work packages for the given work package.
     * 
     * @param parent the work package
     * @return the rolling budget estimates
     *         of all children work packages for the given work package
     */
    public List<Budget> getRollingEstimatesOfChildren(WorkPackage parent) {
        List<Budget> rollingEstimatesOfChildren = new ArrayList<>();

        for (WorkPackage child : parent.getWorkPackageChildren()) {
            TypedQuery<Budget> query = em.createQuery(
                    "SELECT b FROM Budget b WHERE b.id = :budgetId", 
                    Budget.class);
            query.setParameter("budgetId", child.getRollingEstimate().getId());
            Budget result = query.getSingleResult();
            if (result != null) {
                rollingEstimatesOfChildren.add(result);
            }
        }

        return rollingEstimatesOfChildren;
    }
}
