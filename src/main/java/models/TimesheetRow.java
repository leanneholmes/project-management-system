package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;

/**
 * Represents a row in a timesheet. Each row contains information about the
 * hours worked by an employee for a particular project and work package
 * combination.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Entity
@Table(name = "timesheet_row")
public class TimesheetRow implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID. */
    @Id
    @Column(name = "row_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Number. */
    @Column(name = "row_num")
    private int rowNum;

    /** Saturday Work Hours. */
    @Column(name = "row_sat_hours")
    private float satHours;

    /** Sunday Work Hours. */
    @Column(name = "row_sun_hours")
    private float sunHours;

    /** Monday Work Hours. */
    @Column(name = "row_mon_hours")
    private float monHours;

    /** Tuesday Work Hours. */
    @Column(name = "row_tue_hours")
    private float tueHours;

    /** Wednesday Work Hours. */
    @Column(name = "row_wed_hours")
    private float wedHours;

    /** Thursday Work Hours. */
    @Column(name = "row_thu_hours")
    private float thuHours;

    /** Friday Work Hours. */
    @Column(name = "row_fri_hours")
    private float friHours;

    /** Notes. */
    @Column(name = "row_notes")
    private String notes;

    /** Timesheet. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "row_timesheet_id")
    private Timesheet timesheet;

    /** Project. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "row_project_id")
    private Project project;

    /** Work Package. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "row_wp_id")
    private WorkPackage workPackage;

    /**
     * Gets the row ID.
     * 
     * @return the row ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the row ID.
     * 
     * @param id the row ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the row number.
     * 
     * @return the row number
     */
    public int getRowNum() {
        return rowNum;
    }

    /**
     * Sets the row number.
     * 
     * @param rowNum the row number
     */
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    /**
     * Gets the row's saturday work hours.
     * 
     * @return the row's saturday work hours
     */
    public float getSatHours() {
        return satHours;
    }

    /**
     * Sets the row's saturday work hours.
     * 
     * @param satHours the row's saturday work hours
     */
    public void setSatHours(float satHours) {
        this.satHours = satHours;
    }

    /**
     * Gets the row's sunday work hours.
     * 
     * @return the row's sunday work hours
     */
    public float getSunHours() {
        return sunHours;
    }

    /**
     * Sets the row's sunday work hours.
     * 
     * @param sunHours the row's sunday work hours
     */
    public void setSunHours(float sunHours) {
        this.sunHours = sunHours;
    }

    /**
     * Gets the row's monday work hours.
     * 
     * @return the row's monday work hours
     */
    public float getMonHours() {
        return monHours;
    }

    /**
     * Sets the row's monday work hours.
     * 
     * @param monHours the row's monday work hours
     */
    public void setMonHours(float monHours) {
        this.monHours = monHours;
    }

    /**
     * Gets the row's tuesday work hours.
     * 
     * @return the row's tuesday work hours
     */
    public float getTueHours() {
        return tueHours;
    }

    /**
     * Sets the row's tuesday work hours.
     * 
     * @param tueHours the row's tuesday work hours
     */
    public void setTueHours(float tueHours) {
        this.tueHours = tueHours;
    }

    /**
     * Gets the row's wednesday work hours.
     * 
     * @return the row's wednesday work hours
     */
    public float getWedHours() {
        return wedHours;
    }

    /**
     * Sets the row's wednesday work hours.
     * 
     * @param wedHours the row's wednesday work hours
     */
    public void setWedHours(float wedHours) {
        this.wedHours = wedHours;
    }

    /**
     * Gets the row's thursday work hours.
     * 
     * @return the row's thursday work hours
     */
    public float getThuHours() {
        return thuHours;
    }

    /**
     * Sets the row's thursday work hours.
     * 
     * @param thuHours the row's thursday work hours
     */
    public void setThuHours(float thuHours) {
        this.thuHours = thuHours;
    }

    /**
     * Gets the row's friday work hours.
     * 
     * @return the row's friday work hours
     */
    public float getFriHours() {
        return friHours;
    }

    /**
     * Sets the row's friday work hours.
     * 
     * @param friHours the row's friday work hours
     */
    public void setFriHours(float friHours) {
        this.friHours = friHours;
    }

    /**
     * Gets the row notes.
     * 
     * @return the row notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the row notes.
     * 
     * @param notes the row notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the timesheet.
     * 
     * @return the timesheet
     */
    public Timesheet getTimesheet() {
        return timesheet;
    }

    /**
     * Sets the timesheet.
     * 
     * @param timesheet the timesheet
     */
    public void setTimesheet(Timesheet timesheet) {
        this.timesheet = timesheet;
    }

    /**
     * Gets the project.
     * 
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the project.
     * 
     * @param project the project
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Gets the work package.
     * 
     * @return the work package
     */
    public WorkPackage getWorkPackage() {
        return workPackage;
    }

    /**
     * Sets the work package.
     * 
     * @param workPackage the work package
     */
    public void setWorkPackage(WorkPackage workPackage) {
        this.workPackage = workPackage;
    }

    /**
     * Gets the total row hours.
     * 
     * @return sum of hours of every day
     */
    public float getTotalHours() {
        return (getSatHours() + getSunHours() + getMonHours()
                + getTueHours() + getWedHours() 
                + getThuHours() + getFriHours());
    }

    /**
     * Formats object for printing.
     * 
     * @return formatted string
     */
    @Override
    public String toString() {
        return "TimesheetRow [id=" + id
                + ", rowNum=" + rowNum
                + ", satHours=" + satHours
                + ", sunHours=" + sunHours
                + ", monHours=" + monHours
                + ", tueHours=" + tueHours
                + ", wedHours=" + wedHours
                + ", thuHours=" + thuHours
                + ", friHours=" + friHours
                + ", timesheet=" + timesheet
                + ", project=" + project
                + ", workPackage=" + workPackage + "]";
    }
}
