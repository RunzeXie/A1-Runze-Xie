import java.util.List;

/**
 * A general practitioner: the first point of contact for most patients.
 *
 * <p>The class inherits the ID, the name and the roster from
 * {@link HealthProfessional} and adds only what is specific to a GP: the room
 * they consult from, the ability to refer patients to a specialist, and the
 * ability to prescribe.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public class GeneralPractitioner extends HealthProfessional implements Prescriber {

    /** The consulting room the GP works from, for example "Room 1". */
    private String consultingRoom;

    /**
     * Creates a general practitioner who works the default Monday to Friday
     * roster.
     *
     * @param id            the numeric identifier
     * @param name          the full name of the GP
     * @param consultingRoom the consulting room the GP works from
     */
    public GeneralPractitioner(String id, String name, String consultingRoom) {
        super(id, name);
        setConsultingRoom(consultingRoom);
    }

    /**
     * Creates a general practitioner with a caller supplied roster.
     *
     * @param id             the numeric identifier
     * @param name           the full name of the GP
     * @param consultingRoom the consulting room the GP works from
     * @param availableDays  the days of the week on which the GP works
     */
    public GeneralPractitioner(String id, String name, String consultingRoom, List<WeekDay> availableDays) {
        super(id, name, availableDays);
        setConsultingRoom(consultingRoom);
    }

    /**
     * Returns the consulting room the GP works from.
     *
     * @return the consulting room
     */
    public String getConsultingRoom() {
        return consultingRoom;
    }

    /**
     * Changes the consulting room the GP works from.
     *
     * @param consultingRoom the new consulting room
     * @throws IllegalArgumentException if the room is null or blank
     */
    public final void setConsultingRoom(String consultingRoom) {
        if (consultingRoom == null || consultingRoom.trim().isEmpty()) {
            throw new IllegalArgumentException("A general practitioner needs a consulting room.");
        }
        this.consultingRoom = consultingRoom.trim();
    }

    /**
     * Returns the type of this professional.
     *
     * @return "General Practitioner"
     */
    @Override
    public String getProfessionalType() {
        return "General Practitioner";
    }

    /**
     * Describes what a general practitioner does.
     *
     * @return a short description of the GP's duties
     */
    @Override
    public String describeRole() {
        return "First point of contact: assesses everyday illness and injury, "
                + "prescribes medication and refers patients to specialists.";
    }

    /**
     * Prescribes a medication, satisfying the {@link Prescriber} contract.
     *
     * @param medication the name of the medication
     * @param dosage     the dosage instructions
     * @return a message describing the prescription
     * @throws IllegalArgumentException if the medication or dosage is blank
     */
    @Override
    public String prescribe(String medication, String dosage) {
        if (medication == null || medication.trim().isEmpty()) {
            throw new IllegalArgumentException("A medication name is required.");
        }
        if (dosage == null || dosage.trim().isEmpty()) {
            throw new IllegalArgumentException("Dosage instructions are required.");
        }
        return "Dr " + getName() + " (GP, ID " + getId() + ") prescribes "
                + medication.trim() + ", " + dosage.trim() + ".";
    }

    /**
     * Refers a patient to another type of health professional.
     *
     * <p>This behaviour belongs only to a GP, so it is declared here rather than
     * in the base class.</p>
     *
     * @param patientName    the name of the patient
     * @param specialistType the type of professional the patient is referred to
     * @return a message describing the referral
     */
    public String referToSpecialist(String patientName, String specialistType) {
        return "Dr " + getName() + " refers " + patientName + " to a " + specialistType + ".";
    }

    /**
     * Returns all details of the GP, including the details inherited from
     * {@link HealthProfessional}.
     *
     * @return the details of the GP
     */
    @Override
    public String toString() {
        StringBuilder details = new StringBuilder(super.toString());
        details.append(System.lineSeparator());
        details.append(INDENT).append("Consulting room: ").append(consultingRoom);
        return details.toString();
    }
}
