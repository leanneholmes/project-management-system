package access;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import models.Employee;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides access to the Employee database table.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Dependent
@Stateless
public class EmployeeManager implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Entity Manager. */
    @PersistenceContext(unitName = "pms-jpa")
    private EntityManager em;

    /**
     * Returns a list of all employees from the database.
     *
     * @return a list of all employees
     */
    public List<Employee> getAll() {
        return em.createQuery(
            "SELECT e FROM Employee e ORDER BY e.empDeactivated",
                Employee.class).getResultList();
    }

    /**
     * Returns an Employee object with the given username and password.
     *
     * @param username the employee's username
     * @param password the employee's password
     * @return an Employee object with the given username and password
     */
    public Employee get(String username, String password) {
        TypedQuery<Employee> query = em.createQuery(
                "SELECT e FROM Employee e WHERE e.username = :username"
                        + " AND e.password = :password",
                Employee.class);

        return query.setParameter("username", username).setParameter(
                "password", password).getSingleResult();
    }

    /**
     * Persists a new employee into the database.
     *
     * @param employee the employee to persist
     */
    public void persist(Employee employee) {
        em.persist(employee);
    }

    /**
     * Returns a map of all employee credentials (username and password).
     *
     * @return a map of all employee credentials
     */
    public Map<String, String> getAllCredentials() {
        Map<String, String> credentialMap = new HashMap<>();

        TypedQuery<Object[]> query = em.createQuery(
                "SELECT e.username, e.password FROM Employee e",
                Object[].class);
        List<Object[]> result = query.getResultList();

        for (Object[] arr : result) {
            String username = (String) arr[0];
            String password = (String) arr[1];

            credentialMap.put(username, password);
        }

        return credentialMap;
    }

    /**
     * Returns a list of all deactivated employees from the database.
     *
     * @return a list of all deactivated employees
     */
    public List<String> getDeactivatedEmployeeUsernames() {
        TypedQuery<String> query = em.createQuery(
            "SELECT e.username FROM Employee e WHERE e.empDeactivated = true",
            String.class);
        return query.getResultList();
    }

    /**
     * Returns an Employee object with the given employee number.
     *
     * @param empNumber the employee number
     * @return an Employee object with the given employee number
     */
    public Employee getEmpByEmpNumber(long empNumber) {
        return em.createQuery(
                "SELECT e FROM Employee e WHERE e.emp_number = empNumber",
                Employee.class).getSingleResult();
    }

    /**
     * Merges changes made to an employee in the database.
     *
     * @param emp the employee to merge
     */
    public void merge(Employee emp) {
        em.merge(emp);
    }
}
