package access;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import models.Employee;
import models.Project;
import java.io.Serializable;
import java.util.List;

/**
 * This class provides access to the Project database table.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Dependent
@Stateless
public class ProjectManager implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Entity Manager. */
    @PersistenceContext(unitName = "pms-jpa")
    private EntityManager em;

    /** Class Manager. */
    @Inject
    private EmployeeManager employeeManager;

    /**
     * Returns a list of all projects from the database.
     *
     * @return a list of all projects
     */
    public List<Project> getAll() {
        return em.createQuery(
                "SELECT DISTINCT p FROM Project p "
                        + "LEFT JOIN FETCH p.assignedEmployees",
                Project.class).getResultList();
    }

    /**
     * Gets all projects assigned to a given employee.
     * 
     * @param employee the employee
     * @return list of all projects assigned to a given employee
     */
    public List<Project> getAssignedProjects(Employee employee) {
        TypedQuery<Project> query = em.createQuery(
                "SELECT DISTINCT p FROM Project p "
                        + "LEFT JOIN FETCH p.assignedEmployees "
                        + "WHERE :employee MEMBER OF p.assignedEmployees",
                Project.class);

        return query.setParameter("employee", employee).getResultList();
    }

    /**
     * Gets all projects managed by a given employee.
     * 
     * @param empNumber the employee's number
     * @return list of all projects managed by a given employee
     */
    public List<Project> getManagedProjects(long empNumber) {
        TypedQuery<Project> query = em.createQuery(
                "SELECT DISTINCT p FROM Project p "
                        + "LEFT JOIN FETCH p.assignedEmployees "
                        + "WHERE p.projectManager.empNumber = :empNumber",
                Project.class);

        return query.setParameter("empNumber", empNumber).getResultList();
    }

    /**
     * Returns a Project object with the given project ID.
     *
     * @param projectId the project ID
     * @return a Project object with the given project ID
     */
    public Project getProjectById(int projectId) {
        TypedQuery<Project> query = em.createQuery(
                "SELECT DISTINCT p FROM Project p "
                        + "LEFT JOIN FETCH p.assignedEmployees "
                        + "WHERE p.projectId = :projectId",
                Project.class);

        return query.setParameter("projectId", projectId).getSingleResult();
    }

    /**
     * Returns a Project object with the given project name,
     * or null if no such project exists.
     *
     * @param projectName the project name
     * @return a Project object with the given project name,
     *         or null if no such project exists
     */
    public Project getProjectByName(String projectName) {
        TypedQuery<Project> query = em.createQuery(
                "SELECT DISTINCT p FROM Project p "
                        + "LEFT JOIN FETCH p.assignedEmployees "
                        + "WHERE p.projectName = :projectName",
                Project.class);

        try {
            return query.setParameter("projectName", projectName).
                getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * Persists a new project into the database.
     *
     * @param project the project to persist
     */
    public void persist(Project project) {
        em.persist(project);
    }

    /**
     * Deletes a project from the database.
     *
     * @param project the project to delete
     */
    public void delete(Project project) {
        em.remove(em.contains(project) ? project : em.merge(project));
    }

    /**
     * Merges changes made to a project in the database.
     *
     * @param project the project to merge
     */
    public void merge(Project project) {
        em.merge(project);
        em.flush();
    }
}
