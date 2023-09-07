package other;

import controllers.EmployeeController;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import models.Employee;

/**
 * A converter class used to convert a string representation of an employee's
 * username to an Employee object, and vice versa. This class is used in JSF
 * pages to enable input and output of Employee objects.
 * 
 * @author The Bug Busters
 * @version 1
 */
@FacesConverter("employeeConverter")
public class EmployeeConverter implements Converter<Object> {

    /**
     * Converts a string representation of an employee's
     * username to an Employee object. 
     * If the input string is null or empty, returns null.
     * 
     * @param facesContext The FacesContext of the current request
     * @param component    The UIComponent that triggered the conversion
     * @param value        The string value to convert
     * @return The Employee object corresponding to the input string,
     *       or null if the input string is null or empty
     */
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component,
            String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        Employee employee = EmployeeController.getEmployeeByUsername(value);
        return employee;
    }

    /**
     * Converts an Employee object to a string representation
     * of its username. 
     * If the input object is null, returns an empty string.
     * 
     * @param facesContext The FacesContext of the current request
     * @param component    The UIComponent that triggered the conversion
     * @param value        The Employee object to convert
     * @return The username of the input Employee object,
     *       or an empty string if the
     *       input object is null
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
