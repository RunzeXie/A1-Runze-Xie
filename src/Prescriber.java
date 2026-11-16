/**
 * The ability to prescribe medication.
 *
 * <p>Prescribing is not something that every health professional can do, so it
 * does not belong in {@link HealthProfessional}. It is also not something that
 * only one type of professional can do, so it should not be copied into each
 * doctor class either. A capability like this is best expressed as an interface:
 * the classes that can prescribe declare that they implement it, and any code
 * that only needs "something that can prescribe" can depend on this small
 * interface instead of on a particular class.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public interface Prescriber {

    /**
     * Prescribes a medication for a patient.
     *
     * @param medication the name of the medication
     * @param dosage     the dosage instructions, for example "500 mg twice a day"
     * @return a message describing the prescription
     */
    String prescribe(String medication, String dosage);
}
