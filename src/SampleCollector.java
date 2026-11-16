/**
 * The ability to take a blood sample from a patient.
 *
 * <p>This is a second capability interface, deliberately kept separate from
 * {@link Prescriber}. A nurse can collect a sample but cannot prescribe, and a
 * doctor can prescribe; some professionals might do both. Modelling each
 * capability as its own interface keeps those facts independent, so a class can
 * implement just the abilities it really has.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public interface SampleCollector {

    /**
     * Takes a blood sample from a patient.
     *
     * @param patientName the name of the patient
     * @return a message describing the sample that was collected
     */
    String collectBloodSample(String patientName);
}
