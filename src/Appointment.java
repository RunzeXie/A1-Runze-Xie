/**
 * A single booking: one patient, one health professional and one time slot on
 * the clinic's current day.
 *
 * <p>The three parts of a booking are stored as references rather than as copies
 * of text, which is what lets a single appointment work with <em>any</em> type
 * of health professional. Because the field is declared as
 * {@link HealthProfessional}, a general practitioner, a surgeon, a nurse, a
 * dietitian or a physiotherapist can all be booked without the appointment class
 * knowing which of them it holds. The appointment then relies on polymorphism
 * when it prints or compares that professional.</p>
 *
 * <p>All fields are final: once a booking exists, its patient, professional and
 * time cannot be swapped for different ones. If any detail must change, the
 * booking is cancelled and a new one is made, which keeps the record honest.
 * The constructor refuses nulls and the referenced classes validate their own
 * contents, so an incomplete appointment (for example one with no contact
 * number) cannot be created.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public class Appointment implements Comparable<Appointment> {

    /** Width of the time column used when appointments are listed. */
    private static final int TIME_COLUMN_WIDTH = 8;

    /** Width of the patient column used when appointments are listed. */
    private static final int PATIENT_COLUMN_WIDTH = 30;

    /** Indentation used when the professional's details are shown. It lines the
     *  extra lines up with the text that follows the "Booked with:" label. */
    private static final String DETAIL_INDENT = "             ";

    /** The patient who is being seen. */
    private final Patient patient;

    /** The health professional the patient is booked with. */
    private final HealthProfessional professional;

    /** The time at which the appointment starts. */
    private final TimeSlot timeSlot;

    /**
     * Creates an appointment.
     *
     * @param patient      the patient being seen
     * @param professional the health professional being booked
     * @param timeSlot     the time at which the appointment starts
     * @throws IllegalArgumentException if any of the three parts is null
     */
    public Appointment(Patient patient, HealthProfessional professional, TimeSlot timeSlot) {
        if (patient == null) {
            throw new IllegalArgumentException("An appointment must have a patient.");
        }
        if (professional == null) {
            throw new IllegalArgumentException("An appointment must have a health professional.");
        }
        if (timeSlot == null) {
            throw new IllegalArgumentException("An appointment must have a time slot.");
        }
        this.patient = patient;
        this.professional = professional;
        this.timeSlot = timeSlot;
    }

    /**
     * Returns the patient being seen.
     *
     * @return the patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Returns the health professional being booked.
     *
     * @return the health professional
     */
    public HealthProfessional getProfessional() {
        return professional;
    }

    /**
     * Returns the time at which the appointment starts.
     *
     * @return the time slot
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * Orders appointments by time, earliest first.
     *
     * <p>Two appointments at the same time, for different professionals, are
     * allowed to appear in any order, so a professional ID is used as a
     * tiebreaker to make the order predictable when the demonstration is run
     * again.</p>
     *
     * @param other the appointment to compare with
     * @return a negative number if this appointment is earlier, zero if the
     *         times and the professionals match, otherwise a positive number
     */
    @Override
    public int compareTo(Appointment other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare an appointment with null.");
        }
        int byTime = timeSlot.compareTo(other.timeSlot);
        if (byTime != 0) {
            return byTime;
        }
        return professional.getId().compareTo(other.professional.getId());
    }

    /**
     * Tests whether this appointment and another object describe the same
     * booking.
     *
     * @param other the object to compare with
     * @return {@code true} if the patient, the professional and the time all
     *         match
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Appointment)) {
            return false;
        }
        Appointment otherAppointment = (Appointment) other;
        return patient.equals(otherAppointment.patient)
                && professional.equals(otherAppointment.professional)
                && timeSlot.equals(otherAppointment.timeSlot);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return a hash code built from the three parts of the booking
     */
    @Override
    public int hashCode() {
        return (patient.hashCode() * 31 + professional.hashCode()) * 31 + timeSlot.hashCode();
    }

    /**
     * Returns the booking as one aligned line, so that a list of appointments is
     * easy to scan by eye.
     *
     * <p>The professional is printed through {@link HealthProfessional#getSummary()},
     * which calls {@code getProfessionalType()} on the real object, so a nurse
     * and a surgeon identify themselves correctly without any {@code if}
     * statement here.</p>
     *
     * @return a single line containing the time, the patient and the professional
     */
    @Override
    public String toString() {
        return pad(timeSlot.toString(), TIME_COLUMN_WIDTH)
                + pad(patient.toString(), PATIENT_COLUMN_WIDTH)
                + professional.getSummary();
    }

    /**
     * Returns the booking with the full details of the chosen health
     * professional included.
     *
     * <p>This is used when a single appointment is shown on its own, where there
     * is room to display the professional's specialty, room or ward.</p>
     *
     * @return a multi line description of the booking
     */
    public String toDetailedString() {
        String lineSeparator = System.lineSeparator();
        String indentedProfessional = professional.toString()
                .replace(lineSeparator, lineSeparator + DETAIL_INDENT);
        StringBuilder details = new StringBuilder();
        details.append("    Time       : ").append(timeSlot);
        details.append(lineSeparator).append("    Patient    : ").append(patient);
        details.append(lineSeparator).append("    Booked with: ").append(indentedProfessional);
        return details.toString();
    }

    /**
     * Pads text with spaces until it reaches a given width.
     *
     * <p>This is a small helper used only for column alignment in the console
     * output. Text that is already wider than the column is returned unchanged,
     * so a long name never breaks the layout of the line.</p>
     *
     * @param text  the text to pad
     * @param width the width the text should occupy
     * @return the text followed by enough spaces to reach the width
     */
    private static String pad(String text, int width) {
        StringBuilder padded = new StringBuilder(text);
        while (padded.length() < width) {
            padded.append(' ');
        }
        return padded.toString();
    }
}
