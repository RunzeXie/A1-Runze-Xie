import java.util.List;

/**
 * A nurse: a health professional who provides day to day patient care and can
 * take blood samples.
 *
 * <p>A nurse cannot prescribe, so this class does not implement
 * {@link Prescriber}; it implements {@link SampleCollector} instead. That
 * difference is exactly why the two capabilities are separate interfaces rather
 * than methods on {@link HealthProfessional}.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public class Nurse extends HealthProfessional implements SampleCollector {

    /** The ward the nurse works on, for example "Ward 2". */
    private String ward;

    /** The shift the nurse works, for example "Morning". */
    private String shift;

    /**
     * Creates a nurse who works the default Monday to Friday roster.
     *
     * @param id    the numeric identifier
     * @param name  the full name of the nurse
     * @param ward  the ward the nurse works on
     * @param shift the shift the nurse works
     */
    public Nurse(String id, String name, String ward, String shift) {
        super(id, name);
        setWard(ward);
        setShift(shift);
    }

    /**
     * Creates a nurse with a caller supplied roster.
     *
     * @param id            the numeric identifier
     * @param name          the full name of the nurse
     * @param ward          the ward the nurse works on
     * @param shift         the shift the nurse works
     * @param availableDays the days of the week on which the nurse works
     */
    public Nurse(String id, String name, String ward, String shift, List<WeekDay> availableDays) {
        super(id, name, availableDays);
        setWard(ward);
        setShift(shift);
    }

    /**
     * Returns the ward the nurse works on.
     *
     * @return the ward
     */
    public String getWard() {
        return ward;
    }

    /**
     * Changes the ward the nurse works on.
     *
     * @param ward the new ward
     * @throws IllegalArgumentException if the ward is null or blank
     */
    public void setWard(String ward) {
        if (ward == null || ward.trim().isEmpty()) {
            throw new IllegalArgumentException("A nurse needs a ward.");
        }
        this.ward = ward.trim();
    }

    /**
     * Returns the shift the nurse works.
     *
     * @return the shift
     */
    public String getShift() {
        return shift;
    }

    /**
     * Changes the shift the nurse works.
     *
     * @param shift the new shift
     * @throws IllegalArgumentException if the shift is null or blank
     */
    public void setShift(String shift) {
        if (shift == null || shift.trim().isEmpty()) {
            throw new IllegalArgumentException("A nurse needs a shift.");
        }
        this.shift = shift.trim();
    }

    /**
     * Returns the type of this professional.
     *
     * @return "Nurse"
     */
    @Override
    public String getProfessionalType() {
        return "Nurse";
    }

    /**
     * Describes what a nurse does.
     *
     * @return a short description of the nurse's duties
     */
    @Override
    public String describeRole() {
        return "Provides day to day patient care on " + ward
                + " during the " + shift.toLowerCase() + " shift and takes blood samples.";
    }

    /**
     * Takes a blood sample, satisfying the {@link SampleCollector} contract.
     *
     * @param patientName the name of the patient
     * @return a message describing the sample that was collected
     * @throws IllegalArgumentException if the patient name is blank
     */
    @Override
    public String collectBloodSample(String patientName) {
        if (patientName == null || patientName.trim().isEmpty()) {
            throw new IllegalArgumentException("A patient name is required.");
        }
        return "Nurse " + getName() + " (ID " + getId() + ") collects a blood sample from "
                + patientName.trim() + " on " + ward + ".";
    }

    /**
     * Returns all details of the nurse, including the details inherited from
     * {@link HealthProfessional}.
     *
     * @return the details of the nurse
     */
    @Override
    public String toString() {
        StringBuilder details = new StringBuilder(super.toString());
        details.append(System.lineSeparator());
        details.append(INDENT).append("Ward           : ").append(ward);
        details.append(System.lineSeparator());
        details.append(INDENT).append("Shift          : ").append(shift);
        return details.toString();
    }
}
