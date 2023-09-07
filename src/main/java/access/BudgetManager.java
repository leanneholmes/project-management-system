package access;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import models.Budget;
import java.io.Serializable;

/**
 * This class provides access to the Budget database table.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Dependent
@Stateless
public class BudgetManager implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Entity Manager. */
    @PersistenceContext(unitName = "pms-jpa")
    private EntityManager em;

    /**
     * Persists a new budget into the database.
     *
     * @param budget the budget to persist
     */
    public void persist(Budget budget) {
        em.persist(budget);
    }

    /**
     * Merges changes made to a budget in the database.
     *
     * @param budget the budget to merge
     */
    public void merge(Budget budget) {
        em.merge(budget);
    }

    /**
     * Deletes a budget from the database.
     *
     * @param budget the budget to delete
     */
    public void delete(Budget budget) {
        em.remove(em.contains(budget) ? budget : em.merge(budget));
    }

}
