import java.util.List;

/**
 * A surgeon: a doctor who performs operations in an operating theatre.
 *
 * <p>Like a general practitioner, a surgeon can prescribe medication, so this
 * class also implements {@link Prescriber}. Unlike a GP, a surgeon has a
 * surgical specialty and works in a theatre, and can perform a procedure.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public class Surgeon extends HealthProfessional implements Prescriber {

    /** The surgical specialty, for example "Orthopaedics". */
    private String surgicalSpecialty;

    /** The operating theatre the surgeon works in, for example "Theatre A". */
    private String operatingTheatre;

    /**
     * Creates a surgeon who works the default Monday to Friday roster.
     *
     * @param id                the numeric identifier
     * @param name              the full name of the surgeon
     * @param surgicalSpecialty the surgical specialty
     * @param operatingTheatre  the operating theatre the surgeon works in
     */
    public Surgeon(String id, String name, String surgicalSpecialty, String operatingTheatre) {
        super(id, name);
        setSurgicalSpecialty(surgicalSpecialty);
        setOperatingTheatre(operatingTheatre);
    }

    /**
     * Creates a surgeon with a caller supplied roster.
     *
     * @param id                the numeric identifier
     * @param name              the full name of the surgeon
     * @param surgicalSpecialty the surgical specialty
     * @param operatingTheatre  the operating theatre the surgeon works in
     * @param availableDays     the days of the week on which the surgeon works
     */
    public Surgeon(String id, String name, String surgicalSpecialty, String operatingTheatre,
                   List<WeekDay> availableDays) {
        super(id, name, availableDays);
        setSurgicalSpecialty(surgicalSpecialty);
        setOperatingTheatre(operatingTheatre);
    }

    /**
     * Returns the surgical specialty of the surgeon.
     *
     * @return the surgical specialty
     */
    public String getSurgicalSpecialty() {
        return surgicalSpecialty;
    }

    /**
     * Changes the surgical specialty of the surgeon.
     *
     * @param surgicalSpecialty the new specialty
     * @throws IllegalArgumentException if the specialty is null or blank
     */
    public void setSurgicalSpecialty(String surgicalSpecialty) {
        if (surgicalSpecialty == null || surgicalSpecialty.trim().isEmpty()) {
            throw new IllegalArgumentException("A surgeon needs a surgical specialty.");
        }
        this.surgicalSpecialty = surgicalSpecialty.trim();
    }

    /**
     * Returns the operating theatre the surgeon works in.
     *
     * @return the operating theatre
     */
    public String getOperatingTheatre() {
        return operatingTheatre;
    }

    /**
     * Changes the operating theatre the surgeon works in.
     *
     * @param operatingTheatre the new theatre
     * @throws IllegalArgumentException if the theatre is null or blank
     */
    public void setOperatingTheatre(String operatingTheatre) {
        if (operatingTheatre == null || operatingTheatre.trim().isEmpty()) {
            throw new IllegalArgumentException("A surgeon needs an operating theatre.");
        }
        this.operatingTheatre = operatingTheatre.trim();
    }

    /**
     * Returns the type of this professional.
     *
     * @return "Surgeon"
     */
    @Override
    public String getProfessionalType() {
        return "Surgeon";
    }

    /**
     * Describes what a surgeon does.
     *
     * @return a short description of the surgeon's duties
     */
    @Override
    public String describeRole() {
        return "Performs operations in " + operatingTheatre
                + " and manages surgical patients before and after their procedure.";
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
        return "Dr " + getName() + " (Surgeon, ID " + getId() + ") prescribes "
                + medication.trim() + ", " + dosage.trim() + ".";
    }

    /**
     * Performs a surgical procedure on a patient.
     *
     * @param patientName the name of the patient
     * @param procedure   the procedure being performed
     * @return a message describing the operation
     */
    public String performSurgery(String patientName, String procedure) {
        return "Dr " + getName() + " performs " + procedure + " on " + patientName
                + " in " + operatingTheatre + ".";
    }

    /**
     * Returns all details of the surgeon, including the details inherited from
     * {@link HealthProfessional}.
     *
     * @return the details of the surgeon
     */
    @Override
    public String toString() {
        StringBuilder details = new StringBuilder(super.toString());
        details.append(System.lineSeparator());
        details.append(INDENT).append("Specialty      : ").append(surgicalSpecialty);
        details.append(System.lineSeparator());
        details.append(INDENT).append("Theatre        : ").append(operatingTheatre);
        return details.toString();
    }
}
