package other;

import controllers.WorkPackageController;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.WorkPackage;

/**
 * A converter class used to convert a string representation of
 * a work package ID to a WorkPackage object, and vice versa.
 * This class is used in JSF pages to
 * enable input and output of WorkPackage objects.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("workPackageConverter")
@RequestScoped
@FacesConverter(forClass = WorkPackage.class)
public class WorkPackageConverter implements Converter<Object> {

    /** Class Controller. */
    @Inject
    private WorkPackageController workPackageController;

    /**
     * Converts a string representation of a work package ID
     * to a WorkPackage object.
     * 
     * @param context   the FacesContext object
     * @param component the UIComponent object
     * @param value     the string representation of the work package ID
     * @return the corresponding WorkPackage object,
     *         or null if the input string is null or empty
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {

        String id = null;

        if (value != null && !value.isEmpty()) {
            try {
                id = value;
            } catch (NumberFormatException e) {
                System.out.println("test");
            }

            return id != null ? workPackageController.
                getWorkPackageById(value) : null;
        }

        return null;
    }

    /**
     * Converts a WorkPackage object to its string representation,
     * which is the work package ID.
     * 
     * @param context   the FacesContext object
     * @param component the UIComponent object
     * @param value     the WorkPackage object
     * @return the string representation of the work package ID,
     *         or null if the input is null
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value instanceof WorkPackage workPackage) {
            return workPackage.getId();
        }

        return null;
    }
}
