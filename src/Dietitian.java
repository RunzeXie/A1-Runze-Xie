import java.util.List;

/**
 * A dietitian: a health professional who plans what a patient eats.
 *
 * <p>A dietitian adds a dietary focus and the ability to create a meal plan.
 * Everything about identity, naming and rostering is inherited from
 * {@link HealthProfessional}, so none of it is repeated here.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public class Dietitian extends HealthProfessional {

    /** The area of dietetics the dietitian specialises in. */
    private String dietaryFocus;

    /**
     * Creates a dietitian who works the default Monday to Friday roster.
     *
     * @param id           the numeric identifier
     * @param name         the full name of the dietitian
     * @param dietaryFocus the area of dietetics the dietitian specialises in
     */
    public Dietitian(String id, String name, String dietaryFocus) {
        super(id, name);
        setDietaryFocus(dietaryFocus);
    }

    /**
     * Creates a dietitian with a caller supplied roster.
     *
     * @param id            the numeric identifier
     * @param name          the full name of the dietitian
     * @param dietaryFocus  the area of dietetics the dietitian specialises in
     * @param availableDays the days of the week on which the dietitian works
     */
    public Dietitian(String id, String name, String dietaryFocus, List<WeekDay> availableDays) {
        super(id, name, availableDays);
        setDietaryFocus(dietaryFocus);
    }

    /**
     * Returns the area of dietetics the dietitian specialises in.
     *
     * @return the dietary focus
     */
    public String getDietaryFocus() {
        return dietaryFocus;
    }

    /**
     * Changes the area of dietetics the dietitian specialises in.
     *
     * @param dietaryFocus the new dietary focus
     * @throws IllegalArgumentException if the focus is null or blank
     */
    public final void setDietaryFocus(String dietaryFocus) {
        if (dietaryFocus == null || dietaryFocus.trim().isEmpty()) {
            throw new IllegalArgumentException("A dietitian needs a dietary focus.");
        }
        this.dietaryFocus = dietaryFocus.trim();
    }

    /**
     * Returns the type of this professional.
     *
     * @return "Dietitian"
     */
    @Override
    public String getProfessionalType() {
        return "Dietitian";
    }

    /**
     * Describes what a dietitian does.
     *
     * @return a short description of the dietitian's duties
     */
    @Override
    public String describeRole() {
        return "Assesses dietary needs and designs meal plans for patients with "
                + "medical conditions, specialising in " + dietaryFocus.toLowerCase() + ".";
    }

    /**
     * Creates a meal plan for a patient.
     *
     * <p>This behaviour is unique to a dietitian, so it is declared here and not
     * in the base class.</p>
     *
     * @param patientName the name of the patient
     * @param goal        the goal of the plan, for example "lower blood pressure"
     * @return a message describing the meal plan
     */
    public String createMealPlan(String patientName, String goal) {
        return getName() + " (Dietitian, ID " + getId() + ") prepares a "
                + dietaryFocus.toLowerCase() + " meal plan for " + patientName
                + " to " + goal + ".";
    }

    /**
     * Returns all details of the dietitian, including the details inherited from
     * {@link HealthProfessional}.
     *
     * @return the details of the dietitian
     */
    @Override
    public String toString() {
        StringBuilder details = new StringBuilder(super.toString());
        details.append(System.lineSeparator());
        details.append(INDENT).append("Dietary focus  : ").append(dietaryFocus);
        return details.toString();
    }
}
