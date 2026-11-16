/**
 * The basic details the clinic records about a patient who is booking an
 * appointment.
 *
 * <p>Patient is a class of its own rather than two strings inside
 * {@link Appointment}. That keeps the "a patient has a name and a contact
 * number, both of which must be valid" rule in one place, lets the same patient
 * object be reused across several appointments, and means a future patient
 * record (date of birth, Medicare number, notes) can grow without every
 * appointment method having to change.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public class Patient {

    /** A valid Australian mobile number has ten digits. */
    private static final int MOBILE_NUMBER_LENGTH = 10;

    /** Mobile numbers in Australia begin with a zero. */
    private static final char FIRST_MOBILE_DIGIT = '0';

    /** The full name of the patient. */
    private final String name;

    /** The mobile phone number of the patient, stored as digits only. */
    private final String mobileNumber;

    /**
     * Creates a patient.
     *
     * @param name         the full name of the patient
     * @param mobileNumber the mobile phone number, ten digits starting with zero
     * @throws IllegalArgumentException if the name is blank or the mobile number
     *                                  is not a valid Australian mobile number
     */
    public Patient(String name, String mobileNumber) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("A patient must have a name.");
        }
        if (!isValidMobileNumber(mobileNumber)) {
            throw new IllegalArgumentException(
                    "A patient needs a ten digit mobile number starting with 0, but was: " + mobileNumber);
        }
        this.name = name.trim();
        this.mobileNumber = mobileNumber.trim();
    }

    /**
     * Tests whether a value is a valid Australian mobile number.
     *
     * <p>The number must be ten characters long, made of digits only, and start
     * with a zero. Checking it here, in one place, means every {@code Patient}
     * that exists has a usable contact number for the clinic to call.</p>
     *
     * @param mobileNumber the value to test
     * @return {@code true} if the value is a valid mobile number
     */
    public static boolean isValidMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            return false;
        }
        String trimmed = mobileNumber.trim();
        if (trimmed.length() != MOBILE_NUMBER_LENGTH) {
            return false;
        }
        if (trimmed.charAt(0) != FIRST_MOBILE_DIGIT) {
            return false;
        }
        for (int index = 0; index < trimmed.length(); index++) {
            if (!Character.isDigit(trimmed.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the full name of the patient.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the mobile number exactly as it was stored, which is the form used
     * when searching for a patient's appointments.
     *
     * @return the ten digit mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Returns the mobile number grouped for display, for example
     * "0412 345 678".
     *
     * @return the mobile number formatted for reading
     */
    public String getFormattedMobileNumber() {
        if (mobileNumber.length() != MOBILE_NUMBER_LENGTH) {
            return mobileNumber;
        }
        return mobileNumber.substring(0, 4) + " "
                + mobileNumber.substring(4, 7) + " "
                + mobileNumber.substring(7);
    }

    /**
     * Returns the details of the patient in a short readable form.
     *
     * @return the name followed by the formatted mobile number
     */
    @Override
    public String toString() {
        return name + " (" + getFormattedMobileNumber() + ")";
    }

    /**
     * Tests whether this patient and another object mean the same person, which
     * is decided by the mobile number.
     *
     * @param other the object to compare with
     * @return {@code true} if the other object is a patient with the same mobile
     *         number
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }
        Patient otherPatient = (Patient) other;
        return mobileNumber.equals(otherPatient.mobileNumber);
    }

    /**
     * Returns a hash code based on the mobile number, consistent with
     * {@link #equals(Object)}.
     *
     * @return the hash code of the mobile number
     */
    @Override
    public int hashCode() {
        return mobileNumber.hashCode();
    }
}
