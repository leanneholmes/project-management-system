package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * This class represents a timesheet in the system.
 * 
 * @author The Bug Busters.
 * @version 1
 */
@Entity
@Table(name = "timesheet")
public class Timesheet implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID. */
    @Id
    @Column(name = "timesheet_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Status. */
    @Column(name = "timesheet_status")
    private String status;

    /** End Date. */
    @Column(name = "timesheet_end_date")
    private LocalDate endDate;

    /** Number of rows. */
    @Column(name = "timesheet_num_rows")
    private int numRows;

    /** Signed Date. */
    @Column(name = "timesheet_signed_date")
    private LocalDate signedDate;

    /** Response Date. */
    @Column(name = "timesheet_response_date")
    private LocalDate responseDate;

    /** Overtime. */
    @Column(name = "timesheet_overtime")
    private float overtime;

    /** Flextime. */
    @Column(name = "timesheet_flextime")
    private float flextime;

    /** Comments. */
    @Column(name = "timesheet_comments")
    private String comments;

    /** Owner. */
    @ManyToOne
    @JoinColumn(name = "timesheet_emp")
    private Employee employee;

    /** Owner's Signature. */
    @Lob
    @Column(name = "timesheet_emp_signature", columnDefinition = "mediumblob")
    private String empSignature;

    /** Owner's Name. */
    @Column(name = "timesheet_emp_name")
    private String empName;

    /** Approver. */
    @ManyToOne
    @JoinColumn(name = "timesheet_approver")
    private Employee approver;

    /** Approver's Signature. */
    @Lob
    @Column(name = "timesheet_approver_signature", 
        columnDefinition = "mediumblob")
    private String approverSignature;

    /** Approver's Name. */
    @Column(name = "timesheet_approver_name")
    private String approverName;

    /** Timesheet's rows. */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "timesheet")
    private Collection<TimesheetRow> rows = new ArrayList<>();

    /**
     * Gets the timesheet ID.
     * 
     * @return the timesheet ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the timesheet ID.
     * 
     * @param id the timesheet ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the timesheet status.
     * 
     * @return the timesheet status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the timesheet status.
     * 
     * @param status the timesheet status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the timesheet end date.
     * 
     * @return the timesheet end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the timesheet end date.
     * 
     * @param endDate the timesheet end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the timesheet's rows.
     * 
     * @return the timesheet's rows
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Sets the timesheet's rows.
     * 
     * @param numRows the timesheet's rows
     */
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    /**
     * Gets the timesheet signed date.
     * 
     * @return the timesheet signed date
     */
    public LocalDate getSignedDate() {
        return signedDate;
    }

    /**
     * Sets the timesheet signed date.
     * 
     * @param signedDate the timesheet signed date
     */
    public void setSignedDate(LocalDate signedDate) {
        this.signedDate = signedDate;
    }

    /**
     * Gets the timesheet response date.
     * 
     * @return the timesheet response date
     */
    public LocalDate getResponseDate() {
        return responseDate;
    }

    /**
     * Sets the timesheet response date.
     * 
     * @param responseDate the timesheet response date
     */
    public void setResponseDate(LocalDate responseDate) {
        this.responseDate = responseDate;
    }

    /**
     * Gets the timesheet overtime.
     * 
     * @return the timesheet overtime
     */
    public float getOvertime() {
        return overtime;
    }

    /**
     * Sets the timesheet overtime.
     * 
     * @param overtime the timesheet overtime
     */
    public void setOvertime(float overtime) {
        this.overtime = overtime;
    }

    /**
     * Gets the timesheet flextime.
     * 
     * @return the timesheet flextime
     */
    public float getFlextime() {
        return flextime;
    }

    /**
     * Sets the timesheet flextime.
     * 
     * @param flextime the timesheet flextime
     */
    public void setFlextime(float flextime) {
        this.flextime = flextime;
    }

    /**
     * Gets the timesheet comments.
     * 
     * @return the timesheet comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the timesheet comments.
     * 
     * @param comments the timesheet comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Gets the timesheet owner.
     * 
     * @return the timesheet owner
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets the timesheet owner.
     * 
     * @param employee the timesheet owner
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Gets the timesheet's owner signature.
     * 
     * @return the timesheet's owner signature
     */
    public String getEmpSignature() {
        return empSignature;
    }

    /**
     * Sets the timesheet's owner signature.
     * 
     * @param empSignature the timesheet's owner signature
     */
    public void setEmpSignature(String empSignature) {
        this.empSignature = empSignature;
    }

    /**
     * Gets the timesheet's owner name.
     * 
     * @return the timesheet's owner name
     */
    public String getEmpName() {
        return empName;
    }

    /**
     * Sets the timesheet's owner name.
     * 
     * @param empName the timesheet's owner name
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    /**
     * Gets the timesheet's approver.
     * 
     * @return the timesheet's approver
     */
    public Employee getApprover() {
        return approver;
    }

    /**
     * Sets the timesheet's approver.
     * 
     * @param approver the timesheet's approver
     */
    public void setApprover(Employee approver) {
        this.approver = approver;
    }

    /**
     * Gets the timesheet's approver signature.
     * 
     * @return the timesheet's approver signature
     */
    public String getApproverSignature() {
        return approverSignature;
    }

    /**
     * Sets the timesheet's approver signature.
     * 
     * @param approverSignature the timesheet's approver signature
     */
    public void setApproverSignature(String approverSignature) {
        this.approverSignature = approverSignature;
    }

    /**
     * Gets the timesheet's approver name.
     * 
     * @return the timesheet's approver name
     */
    public String getApproverName() {
        return approverName;
    }

    /**
     * Sets the timesheet's approver name.
     * 
     * @param approverName the timesheet's approver name
     */
    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    /**
     * Gets the timesheet's rows.
     * 
     * @return the timesheet's rows
     */
    public Collection<TimesheetRow> getRows() {
        return rows;
    }

    /**
     * Sets the timesheet's rows.
     * 
     * @param rows the timesheet's rows
     */
    public void setRows(Collection<TimesheetRow> rows) {
        this.rows = rows;
    }

    /**
     * Formats object for printing.
     * 
     * @return formatted string
     */
    @Override
    public String toString() {
        return "Timesheet [id=" + id + ", status=" + status
                + ", endDate=" + endDate + ", numRows=" + numRows
                + ", signedDate=" + signedDate + ", employee=" + employee + "]";
    }

}
