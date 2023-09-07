package other;

import java.time.LocalDate;

import controllers.EmployeeController;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import models.PayGrade;

/**
 * A converter class used to convert a string representation of a pay grade name
 * to a PayGrade object for the current year, and vice versa. This class is used
 * in JSF pages to enable input and output of PayGrade objects.
 * 
 * @author The Bug Busters
 * @version 1
 */
@FacesConverter("payGradeConverter")
public class PayGradeConverter implements Converter<Object> {
   
    /**
     * Converts a string representation of a pay grade name and year
     * to a PayGrade object.
     * 
     * @param facesContext the FacesContext object
     * @param component    the UIComponent object
     * @param value        the string representation of the pay grade
     *       name and year
     * @return the corresponding PayGrade object, or null if the input string is
     *         null or empty
     */
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component,
            String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        int currentYear = LocalDate.now().getYear();

        PayGrade payGrade = EmployeeController.
            getPayGradeByNameAndYear(value, currentYear);
        return payGrade;
    }

    /**
     * Converts a PayGrade object to its string representation, which is the pay
     * grade name and year.
     * 
     * @param facesContext the FacesContext object
     * @param component    the UIComponent object
     * @param value        the PayGrade object
     * @return the string representation of the pay grade name and year,
     *       or an empty string if the input is null
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent component,
            Object value) {
        if (value == null) {
            return "";
        }

        return value.toString();
    }
}
