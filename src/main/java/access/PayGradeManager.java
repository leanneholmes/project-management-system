package access;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import models.PayGrade;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * This class provides access to the PayGrade database table.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Dependent
@Stateless
public class PayGradeManager implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Entity Manager. */
    @PersistenceContext(unitName = "pms-jpa")
    private EntityManager em;

    /**
     * Returns a list of all pay grades from the database.
     * 
     * @return a list of all pay grades
     */
    public List<PayGrade> getAll() {
        return em.createQuery("SELECT p FROM PayGrade p",
                PayGrade.class).getResultList();
    }

    /**
     * Returns a PayGrade object with the given pay grade ID.
     *
     * @param paygradeId the pay grade ID
     * @return a PayGrade object with the given pay grade ID
     */
    public PayGrade getPayGradeById(int paygradeId) {
        return em.createQuery(
                "SELECT p FROM PayGrade p WHERE p.gradeId = :paygradeId",
                PayGrade.class).getSingleResult();
    }

    /**
     * Gets cost from pay grade with the given name and year.
     * 
     * @param name the pay grade name
     * @param year the pay grade year
     * @return the pay grade cost
     */
    public float getPayGradeCostByNameAndYear(String name, LocalDate year) {
        TypedQuery<Float> query = em.createQuery(
                "SELECT p.cost FROM PayGrade p "
                        + "WHERE p.name = :name AND p.year = :year",
                Float.class);
        query.setParameter("name", name);
        query.setParameter("year", year);

        return query.getSingleResult();
    }

    /**
     * Persists a new pay grade into the database.
     *
     * @param paygrade the pay grade to persist
     */
    public void persist(PayGrade paygrade) {
        em.persist(paygrade);
    }

    /**
     * Deletes a pay grade from the database.
     *
     * @param paygrade the pay grade to delete
     */
    public void delete(PayGrade paygrade) {
        em.remove(em.contains(paygrade) ? paygrade : em.merge(paygrade));
    }

    /**
     * Merges changes made to a pay grade in the database.
     *
     * @param paygrade the pay grade to merge
     */
    public void merge(PayGrade paygrade) {
        em.merge(paygrade);
    }
}
