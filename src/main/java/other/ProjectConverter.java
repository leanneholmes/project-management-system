package other;

import controllers.ProjectController;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.Project;

/**
 * A converter class used to convert a string representation of a project ID
 * to a Project object, and vice versa. This class is used
 * in JSF pages to enable input and output of Project objects.
 * 
 * @author The Bug Busters
 * @version 1
 */
@Named("projectConverter")
@RequestScoped
@FacesConverter(forClass = Project.class)
public class ProjectConverter implements Converter<Object> {
    
    /** Class Controller. */
    @Inject
    private ProjectController projectController;

    /**
     * Converts a string representation of a project ID
     * to a Project object.
     * 
     * @param context the FacesContext object
     * @param component    the UIComponent object
     * @param value        the string representation of the project ID
     * @return the corresponding Project object, or null if the input string is
     *         null or empty
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
         String value) {

        int id = -1;

        if (value != null && !value.isEmpty()) {
            try {
                id = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("test");
            }

            return id != -1 ? projectController.getProjectById(id) : null;
        }

        return null;
    }

    /**
     * Converts a Project object to its string representation, 
     * which is the project ID.
     * 
     * @param context the FacesContext object
     * @param component    the UIComponent object
     * @param value        the Project object
     * @return the string representation of the project ID,
     *       or null if the input is null
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component,
         Object value) {

        if (value instanceof Project project) {
            return Integer.toString(project.getProjectId());
        }

        return null;
    }
}
