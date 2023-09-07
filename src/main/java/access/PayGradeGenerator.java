package access;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import models.PayGrade;

/**
 * This class provides access to the PayGrade database table
 * and updates their information.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Singleton
public class PayGradeGenerator {

    /** Inflation Rate (3%). */
    private static final float INFLATION_RATE = 1.03F;

    /** Entity Manager. */
    @PersistenceContext
    private EntityManager em;

    /**
     * Gets the current pay grades from the database, creates
     * a new set of pay grades based on the current ones,
     * and updates the database with the new ones.
     * This is scheduled to happen on December 31.
     */
    @Schedule(dayOfMonth = "31", month = "12")
    public void generatePayGrades() {
        // Get the current pay grades from the database
        List<PayGrade> currentPayGrades = em.createQuery(
            "SELECT p FROM PayGrade p", PayGrade.class).
                getResultList();

        // Create new pay grades based on the current ones
        List<PayGrade> updatedPayGrades = new ArrayList<>();
        for (PayGrade payGrade : currentPayGrades) {
            PayGrade updatedPayGrade = new PayGrade();
            updatedPayGrade.setName(payGrade.getName());
            updatedPayGrade.setYear(LocalDate.now().plusDays(1));
            updatedPayGrade.setCost(payGrade.getCost() * INFLATION_RATE);
            updatedPayGrades.add(updatedPayGrade);
        }

        // Persist the new pay grades to the database
        for (PayGrade updatedPayGrade : updatedPayGrades) {
            em.persist(updatedPayGrade);
        }
    }
}
