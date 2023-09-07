package access;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import models.TimesheetRow;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * This class provides access to the TimesheetRow database table.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Dependent
@Stateless
public class TimesheetRowManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Entity Manager. */
    @PersistenceContext(unitName = "pms-jpa")
    private EntityManager em;

    /**
     * Returns a list of all TimesheetRows with the given timesheet ID.
     *
     * @param timesheetId the timesheet ID
     * @return a list of all TimesheetRows with the given timesheet ID
     */
    public List<TimesheetRow> findByTimesheetId(int timesheetId) {
        TypedQuery<TimesheetRow> query = em.createQuery(
                "SELECT tsr FROM TimesheetRow tsr "
                        + "WHERE tsr.timesheet.id = :timesheetId",
                TimesheetRow.class);
        query.setParameter("timesheetId", timesheetId);

        return query.getResultList();
    }

    /**
     * Persists a new TimesheetRow into the database.
     *
     * @param tsr the TimesheetRow to persist
     */
    public void insert(TimesheetRow tsr) {
        em.persist(tsr);
    }

    /**
     * Merges changes made to a TimesheetRow in the database.
     *
     * @param tsr the TimesheetRow to merge
     */
    public void update(TimesheetRow tsr) {
        try {
            TypedQuery<TimesheetRow> query = em.createQuery(
                    "SELECT tr FROM TimesheetRow tr "
                    + "JOIN FETCH tr.workPackage JOIN FETCH tr.project "
                    + "JOIN FETCH tr.timesheet WHERE tr.id = :rowId",
                    TimesheetRow.class);
            query.setParameter("rowId", tsr.getId());
            TimesheetRow row = query.getSingleResult();
            if (row != null) {
                em.merge(tsr);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
    }

    /**
     * Deletes a TimesheetRow from the database.
     *
     * @param tsr the TimesheetRow to delete
     */
    public void delete(TimesheetRow tsr) {
        TimesheetRow timesheetRow = em.find(TimesheetRow.class, tsr.getId());
        em.remove(timesheetRow);
    }
}
