package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * This class represents a budget for a certain year, with allocations for
 * different cost categories. Contains getter and setter methods for each cost
 * category.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Entity
@Table(name = "budget")
public class Budget implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Budget ID. */
    @Id
    @Column(name = "budget_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Budget Year. */
    @Column(name = "budget_year")
    private int year;

    /** Budget Labour Grade JS. */
    @Column(name = "budget_JS")
    private float JS;

    /** Budget Labour Grade SS. */
    @Column(name = "budget_SS")
    private float SS;

    /** Budget Labour Grade DS. */
    @Column(name = "budget_DS")
    private float DS;

    /** Budget Labour Grade P1. */
    @Column(name = "budget_P1")
    private float p1;

    /** Budget Labour Grade P2. */
    @Column(name = "budget_P2")
    private float p2;

    /** Budget Labour Grade P3. */
    @Column(name = "budget_P3")
    private float p3;

    /** Budget Labour Grade P4. */
    @Column(name = "budget_P4")
    private float p4;

    /** Budget Labour Grade P5. */
    @Column(name = "budget_P5")
    private float p5;

    /** Budget Labour Grade P6. */
    @Column(name = "budget_P6")
    private float p6;

    /**
     * Default Constructor.
     * Initializes budget year to current year.
     */
    public Budget() {
        this.year = LocalDate.now().getYear();
    }

    /**
     * Parameter Constructor.
     * Initializes Labour Grades.
     * 
     * @param JS Labour Grade JS
     * @param SS Labour Grade SS
     * @param DS Labour Grade DS
     * @param P1 Labour Grade P1
     * @param P2 Labour Grade P2
     * @param P3 Labour Grade P3
     * @param P4 Labour Grade P4
     * @param P5 Labour Grade P5
     * @param P6 Labour Grade P6
     */
    public Budget(float JS, float SS, float DS, float P1, 
        float P2, float P3, float P4, float P5, float P6) {
        this();
        this.JS = JS;
        this.SS = SS;
        this.DS = DS;
        this.p1 = P1;
        this.p2 = P2;
        this.p3 = P3;
        this.p4 = P4;
        this.p5 = P5;
        this.p6 = P6;
    }

    /**
     * Gets the budget ID.
     * 
     * @return the budget ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the budget ID.
     * 
     * @param id the budget ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the budget year.
     * 
     * @return the budget year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the budget year.
     * 
     * @param year the budget year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets budget for labour grade JS.
     * 
     * @return budget for labour grade JS
     */
    public float getJS() {
        return JS;
    }

    /**
     * Sets budget for labour grade JS.
     * 
     * @param jS budget for labour grade JS
     */
    public void setJS(float jS) {
        JS = jS;
    }

    /**
     * Gets budget for labour grade SS.
     * 
     * @return budget for labour grade SS
     */
    public float getSS() {
        return SS;
    }

    /**
     * Sets budget for labour grade SS.
     * 
     * @param sS budget for labour grade SS
     */
    public void setSS(float sS) {
        SS = sS;
    }

    /**
     * Gets budget for labour grade DS.
     * 
     * @return budget for labour grade DS
     */
    public float getDS() {
        return DS;
    }

    /**
     * Sets budget for labour grade DS.
     * 
     * @param dS budget for labour grade DS
     */
    public void setDS(float dS) {
        DS = dS;
    }

    /**
     * Gets budget for pay grade level 1.
     * 
     * @return budget for pay grade level 1
     */
    public float getP1() {
        return p1;
    }

    /**
     * Sets budget for pay grade level 1.
     * 
     * @param p1 budget for pay grade level 1
     */
    public void setP1(float p1) {
        this.p1 = p1;
    }

    /**
     * Gets budget for pay grade level 2.
     * 
     * @return budget for pay grade level 2
     */
    public float getP2() {
        return p2;
    }

    /**
     * Sets budget for pay grade level 2.
     * 
     * @param p2 budget for pay grade level 2
     */
    public void setP2(float p2) {
        this.p2 = p2;
    }

    /**
     * Gets budget for pay grade level 3.
     * 
     * @return budget for pay grade level 3
     */
    public float getP3() {
        return p3;
    }

    /**
     * Sets budget for pay grade level 3.
     * 
     * @param p3 budget for pay grade level 3
     */
    public void setP3(float p3) {
        this.p3 = p3;
    }

    /**
     * Gets budget for pay grade level 4.
     * 
     * @return budget for pay grade level 4
     */
    public float getP4() {
        return p4;
    }

    /**
     * Sets budget for pay grade level 4.
     * 
     * @param p4 budget for pay grade level 4
     */
    public void setP4(float p4) {
        this.p4 = p4;
    }

    /**
     * Gets budget for pay grade level 5.
     * 
     * @return budget for pay grade level 5
     */
    public float getP5() {
        return p5;
    }

    /**
     * Sets budget for pay grade level 5.
     * 
     * @param p5 budget for pay grade level 5
     */
    public void setP5(float p5) {
        this.p5 = p5;
    }

    /**
     * Gets budget for pay grade level 6.
     * 
     * @return budget for pay grade level 6
     */
    public float getP6() {
        return p6;
    }

    /**
     * Sets budget for pay grade level 6.
     * 
     * @param p6 budget for pay grade level 6
     */
    public void setP6(float p6) {
        this.p6 = p6;
    }

    /**
     * Formats budget information for printing.
     * 
     * @return formatted budget information
     */
    @Override
    public String toString() {
        return "Budget [id=" + id + ", year=" + year + "]";
    }

}
