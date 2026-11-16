import java.util.List;

/**
 * A physiotherapist: a health professional who helps patients recover movement
 * through exercise and manual therapy.
 *
 * <p>This is the fourth specific type required by Part 2. It also proves the
 * point made in Part 3: adding a new type to this system means writing one new
 * class and nothing else, because the base class already supplies the shared
 * data and the appointment system already works with any
 * {@link HealthProfessional}.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public class Physiotherapist extends HealthProfessional {

    /** The area of physiotherapy the physiotherapist focuses on. */
    private String focusArea;

    /**
     * Creates a physiotherapist who works the default Monday to Friday roster.
     *
     * @param id        the numeric identifier
     * @param name      the full name of the physiotherapist
     * @param focusArea the area of physiotherapy the physiotherapist focuses on
     */
    public Physiotherapist(String id, String name, String focusArea) {
        super(id, name);
        setFocusArea(focusArea);
    }

    /**
     * Creates a physiotherapist with a caller supplied roster.
     *
     * @param id            the numeric identifier
     * @param name          the full name of the physiotherapist
     * @param focusArea     the area of physiotherapy the physiotherapist focuses on
     * @param availableDays the days of the week on which the physiotherapist works
     */
    public Physiotherapist(String id, String name, String focusArea, List<WeekDay> availableDays) {
        super(id, name, availableDays);
        setFocusArea(focusArea);
    }

    /**
     * Returns the area of physiotherapy the physiotherapist focuses on.
     *
     * @return the focus area
     */
    public String getFocusArea() {
        return focusArea;
    }

    /**
     * Changes the area of physiotherapy the physiotherapist focuses on.
     *
     * @param focusArea the new focus area
     * @throws IllegalArgumentException if the focus area is null or blank
     */
    public final void setFocusArea(String focusArea) {
        if (focusArea == null || focusArea.trim().isEmpty()) {
            throw new IllegalArgumentException("A physiotherapist needs a focus area.");
        }
        this.focusArea = focusArea.trim();
    }

    /**
     * Returns the type of this professional.
     *
     * @return "Physiotherapist"
     */
    @Override
    public String getProfessionalType() {
        return "Physiotherapist";
    }

    /**
     * Describes what a physiotherapist does.
     *
     * @return a short description of the physiotherapist's duties
     */
    @Override
    public String describeRole() {
        return "Treats movement problems with exercise programs and manual therapy, "
                + "specialising in " + focusArea.toLowerCase() + ".";
    }

    /**
     * Designs an exercise program for a patient.
     *
     * @param patientName the name of the patient
     * @param weeks       how many weeks the program should run for
     * @return a message describing the exercise program
     */
    public String designExerciseProgram(String patientName, int weeks) {
        return getName() + " (Physiotherapist, ID " + getId() + ") designs a " + weeks
                + " week " + focusArea.toLowerCase() + " program for " + patientName + ".";
    }

    /**
     * Returns all details of the physiotherapist, including the details inherited
     * from {@link HealthProfessional}.
     *
     * @return the details of the physiotherapist
     */
    @Override
    public String toString() {
        StringBuilder details = new StringBuilder(super.toString());
        details.append(System.lineSeparator());
        details.append(INDENT).append("Focus area     : ").append(focusArea);
        return details.toString();
    }
}
