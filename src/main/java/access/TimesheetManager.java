package access;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import models.Timesheet;
import models.WorkPackage;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * This class provides access to the Provides database table.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Dependent
@Stateless
public class TimesheetManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** Entity Manager. */
    @PersistenceContext(unitName = "pms-jpa")
    private EntityManager em;

    /**
     * Returns a list of all timesheets from the database.
     *
     * @return a list of all timesheets
     */
    public List<Timesheet> findAll() {
        TypedQuery<Timesheet> query = em.createQuery(
            "SELECT t FROM Timesheet t", Timesheet.class);
        return query.getResultList();
    }

    /**
     * Returns a list of all timesheets for the given employee.
     *
     * @param empNumber the employee number
     * @return a list of all timesheets for the given employee
     */
    public List<Timesheet> findByEmpNumber(long empNumber) {
        TypedQuery<Timesheet> query = em.createQuery(
                "SELECT t FROM Timesheet t "
                        + "WHERE t.employee.empNumber = :empNumber",
                Timesheet.class);
        query.setParameter("empNumber", empNumber);

        return query.getResultList();
    }

    /**
     * Returns a Timesheet object with the given timesheet ID.
     *
     * @param timesheetId the timesheet ID
     * @return a Timesheet object with the given timesheet ID
     */
    public Timesheet findByTimesheetId(int timesheetId) {
        return em.find(Timesheet.class, timesheetId);
    }

    /**
     * Persists a new timesheet into the database.
     *
     * @param timesheet the timesheet to persist
     */
    public void addTimesheet(Timesheet timesheet) {
        em.persist(timesheet);
    }

    /**
     * Deletes a timesheet from the database.
     *
     * @param timesheet the timesheet to delete
     */
    public void deleteTimesheet(Timesheet timesheet) {
        Timesheet ts = em.find(Timesheet.class, timesheet.getId());

        em.remove(ts);
    }

    /**
     * Merges changes made to a timesheet in the database
     * and sets the status of the Timesheet object.
     *
     * @param timesheet the timesheet to merge
     * @param status    the new timesheet status
     */
    public void updateTimesheetStatus(Timesheet timesheet, String status) {
        timesheet.setStatus(status);

        em.merge(timesheet);
    }

    /**
     * Gets all work packages submitted for a given timesheet.
     * 
     * @param timesheet the timesheet
     * @return all work packages submitted for a given timesheet
     */
    public List<WorkPackage> getAllWorkPackagesSubmittedForTimesheet(
        Timesheet timesheet) {
        TypedQuery<WorkPackage> query = em.createQuery(
            "SELECT DISTINCT w FROM TimesheetRow tr "
            + "JOIN tr.workPackage w WHERE tr.timesheet = :timesheet",
            WorkPackage.class);
        query.setParameter("timesheet", timesheet);

        return query.getResultList();
    }

    /**
     * Gets all work packages submitted for a given timesheet and timesheet row.
     * 
     * @param timesheet the timesheet
     * @param timesheetRowId the timesheet row ID
     * @return all work packages submitted for a given timesheet 
     *      and timesheet row
     */
    public List<WorkPackage> getAllWorkPackagesSubmittedForTimesheet(
        Timesheet timesheet, int timesheetRowId) {
        TypedQuery<WorkPackage> query = em.createQuery(
            "SELECT DISTINCT w FROM TimesheetRow tr "
            + "JOIN tr.workPackage w WHERE tr.timesheet = :timesheet "
            + "AND tr.id != :timesheetRowId",
            WorkPackage.class);
        query.setParameter("timesheet", timesheet);
        query.setParameter("timesheetRowId", timesheetRowId);

        return query.getResultList();
    }
}
