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
 * The PayGrade class represents the pay grade of an employee. It contains the
 * ID, name, cost, and year of the pay grade.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Entity
@Table(name = "paygrade")
public class PayGrade implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID. */
    @Id
    @Column(name = "grade_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name. */
    @Column(name = "grade_name")
    private String name;

    /** Cost. */
    @Column(name = "grade_cost")
    private float cost;

    /** Year. */
    @Column(name = "grade_year")
    private LocalDate year;

    /**
     * Default Constructor.
     */
    public PayGrade() {
    }

    /**
     * Gets the pay grade ID.
     * 
     * @return the pay grade ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the pay grade ID.
     * 
     * @param gradeId the pay grade ID
     */
    public void setId(Long gradeId) {
        this.id = gradeId;
    }

    /**
     * Gets the pay grade name.
     * 
     * @return the pay grade name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the pay grade name.
     * 
     * @param name the pay grade name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the pay grade cost.
     * 
     * @return the pay grade cost
     */
    public float getCost() {
        return cost;
    }

    /**
     * Sets the pay grade cost.
     * 
     * @param cost the pay grade cost
     */
    public void setCost(float cost) {
        this.cost = cost;
    }

    /**
     * Gets the pay grade year.
     * 
     * @return the pay grade year
     */
    public LocalDate getYear() {
        return year;
    }

    /**
     * Sets the pay grade year.
     * 
     * @param year the pay grade year
     */
    public void setYear(LocalDate year) {
        this.year = year;
    }

    /**
     * Formats the object for printing.
     * 
     * @return formatted string
     */
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Compares a pay grade to another object.
     * 
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        PayGrade payGrade = (PayGrade) obj;

        return (payGrade.id == this.id);
    }

    /**
     * Returns hash code for pay grade's ID.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return this.id.intValue();
    }
    
}
