import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Owns and manages the clinic's collection of appointments for the current day.
 *
 * <p>The collection is an {@link ArrayList}. That is the right choice here
 * because the clinic needs to keep every booking, iterate over all of them,
 * filter them and sort them, and it needs them to stay in a predictable order.
 * An {@code ArrayList} supports all of that, allows duplicates in principle (two
 * different patients can book the same time with different professionals), and
 * gives fast access by position. The uniqueness rule the clinic actually cares
 * about - one professional cannot be in two places at once - is a business rule,
 * not a property of the container, so it is enforced by
 * {@link #addAppointment(Appointment)}.</p>
 *
 * <p>Every operation that can fail returns a message instead of printing one.
 * The caller decides where the message goes, and the outcome is testable.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public class AppointmentManager {

    /** Prefix used when an operation succeeded. */
    private static final String CONFIRMED = "CONFIRMED : ";

    /** Prefix used when a booking was refused. */
    private static final String REJECTED = "REJECTED  : ";

    /** Prefix used when an appointment was removed. */
    private static final String CANCELLED = "CANCELLED : ";

    /** Prefix used when a cancellation could not be carried out. */
    private static final String FAILED = "FAILED    : ";

    /** The appointments currently booked for the clinic day. */
    private final List<Appointment> appointments;

    /** The daily schedule that every booking must fit into. */
    private final ClinicSchedule schedule;

    /** The day the clinic is currently taking bookings for. */
    private final WeekDay clinicDay;

    /**
     * Creates a manager for the given schedule and clinic day.
     *
     * @param schedule  the daily schedule bookings must fit into
     * @param clinicDay the day the clinic is taking bookings for
     * @throws IllegalArgumentException if the schedule or the day is null
     */
    public AppointmentManager(ClinicSchedule schedule, WeekDay clinicDay) {
        if (schedule == null) {
            throw new IllegalArgumentException("An appointment manager needs a clinic schedule.");
        }
        if (clinicDay == null) {
            throw new IllegalArgumentException("An appointment manager needs a clinic day.");
        }
        this.schedule = schedule;
        this.clinicDay = clinicDay;
        this.appointments = new ArrayList<>();
    }

    /**
     * Creates a manager that uses the clinic's standard 08:00 to 16:00 schedule.
     *
     * @param clinicDay the day the clinic is taking bookings for
     */
    public AppointmentManager(WeekDay clinicDay) {
        this(new ClinicSchedule(), clinicDay);
    }

    /**
     * Books an appointment that has already been built as an object.
     *
     * <p>The booking is checked against the schedule, the professional's roster
     * and the existing bookings before it is stored, so the collection can never
     * contain a slot the clinic does not run, or a professional booked twice at
     * the same time.</p>
     *
     * @param appointment the appointment to add
     * @return a message describing the outcome
     */
    public String addAppointment(Appointment appointment) {
        if (appointment == null) {
            return REJECTED + "no appointment details were supplied.";
        }
        HealthProfessional professional = appointment.getProfessional();
        TimeSlot timeSlot = appointment.getTimeSlot();

        if (!schedule.isWithinWorkingDay(timeSlot)) {
            return REJECTED + timeSlot + " is outside the clinic's opening hours. The clinic runs "
                    + schedule.describeSlots() + ".";
        }
        if (!schedule.isBookableSlot(timeSlot)) {
            return REJECTED + timeSlot + " is not the start of one of the clinic's slots. "
                    + "Appointments must start on a " + schedule.getSlotLengthInMinutes()
                    + " minute boundary between " + schedule.getOpeningTime()
                    + " and " + schedule.getClosingTime() + ".";
        }
        if (!professional.worksOn(clinicDay)) {
            return REJECTED + professional.getName() + " (ID " + professional.getId()
                    + ") does not work on " + clinicDay.getFullName() + "s. Available days: "
                    + professional.getAvailabilityDescription() + ".";
        }
        if (isProfessionalBooked(professional, timeSlot)) {
            return REJECTED + professional.getName() + " (ID " + professional.getId()
                    + ") is already booked at " + timeSlot + ". Please choose another time.";
        }

        appointments.add(appointment);
        return CONFIRMED + appointment.getPatient().getName() + " with " + professional.getName()
                + " (ID " + professional.getId() + ") at " + timeSlot
                + " on " + clinicDay.getFullName() + ".";
    }

    /**
     * Books an appointment from its three parts.
     *
     * <p>This overload is the convenient way in for a caller that has the parts
     * of a booking rather than a ready made {@link Appointment}. It checks that
     * all three parts are present and then delegates to
     * {@link #addAppointment(Appointment)} so the booking rules exist in exactly
     * one place.</p>
     *
     * @param patient      the patient being seen
     * @param professional the health professional being booked
     * @param timeSlot     the time at which the appointment starts
     * @return a message describing the outcome
     */
    public String addAppointment(Patient patient, HealthProfessional professional, TimeSlot timeSlot) {
        if (patient == null) {
            return REJECTED + "a patient is required to make a booking.";
        }
        if (professional == null) {
            return REJECTED + "a health professional is required to make a booking.";
        }
        if (timeSlot == null) {
            return REJECTED + "a time slot is required to make a booking.";
        }
        return addAppointment(new Appointment(patient, professional, timeSlot));
    }

    /**
     * Tests whether a professional is already booked at a time.
     *
     * <p>The professional objects are compared with {@code equals}, which uses
     * the ID, so it does not matter whether the caller passes the original object
     * or a different object representing the same person.</p>
     *
     * @param professional the professional to look for
     * @param timeSlot     the time to look for
     * @return {@code true} if the professional already has that slot
     */
    public boolean isProfessionalBooked(HealthProfessional professional, TimeSlot timeSlot) {
        if (professional == null || timeSlot == null) {
            return false;
        }
        for (Appointment appointment : appointments) {
            if (appointment.getProfessional().equals(professional)
                    && appointment.getTimeSlot().equals(timeSlot)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns every appointment, in time order.
     *
     * @return a new list of all appointments, earliest first
     */
    public List<Appointment> getAppointmentsInTimeOrder() {
        return sortByTime(appointments);
    }

    /**
     * Returns the appointments for one health professional, in time order.
     *
     * @param professionalId the numeric ID of the professional
     * @return a new list of matching appointments, earliest first; empty if the
     *         ID is unknown
     */
    public List<Appointment> findByProfessional(String professionalId) {
        List<Appointment> matches = new ArrayList<>();
        if (professionalId == null) {
            return matches;
        }
        String wantedId = professionalId.trim();
        for (Appointment appointment : appointments) {
            if (appointment.getProfessional().getId().equals(wantedId)) {
                matches.add(appointment);
            }
        }
        Collections.sort(matches);
        return matches;
    }

    /**
     * Returns the appointments booked by one patient, in time order.
     *
     * @param mobileNumber the mobile number the booking was made with
     * @return a new list of matching appointments, earliest first; empty if the
     *         number is unknown
     */
    public List<Appointment> findByPatientMobile(String mobileNumber) {
        List<Appointment> matches = new ArrayList<>();
        if (mobileNumber == null) {
            return matches;
        }
        String wantedNumber = mobileNumber.trim();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getMobileNumber().equals(wantedNumber)) {
                matches.add(appointment);
            }
        }
        Collections.sort(matches);
        return matches;
    }

    /**
     * Sorts any collection of appointments into time order.
     *
     * <p>The given list is copied first, so sorting a filtered view never
     * rearranges the manager's own collection.</p>
     *
     * @param source the appointments to sort
     * @return a new list in time order
     */
    public List<Appointment> sortByTime(List<Appointment> source) {
        List<Appointment> sorted = new ArrayList<>(source);
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * Sorts any collection of appointments by the professional's name.
     *
     * <p>This shows a second way of ordering the same objects. {@link Appointment}
     * defines its natural order (by time) through {@code Comparable}, but the
     * clinic also wants to see the day grouped by who is seeing whom, which is a
     * different ordering for the same objects. A {@link Comparator} supplies that
     * alternative order without touching the class.</p>
     *
     * @param source the appointments to sort
     * @return a new list ordered by professional name, then by time
     */
    public List<Appointment> sortByProfessionalName(List<Appointment> source) {
        List<Appointment> sorted = new ArrayList<>(source);
        Collections.sort(sorted, new Comparator<Appointment>() {
            @Override
            public int compare(Appointment first, Appointment second) {
                int byName = first.getProfessional().getName()
                        .compareToIgnoreCase(second.getProfessional().getName());
                if (byName != 0) {
                    return byName;
                }
                return first.getTimeSlot().compareTo(second.getTimeSlot());
            }
        });
        return sorted;
    }

    /**
     * Cancels the appointment a professional has at a particular time.
     *
     * @param professionalId the numeric ID of the professional
     * @param timeSlot       the time of the appointment
     * @return a message describing the outcome
     */
    public String cancelAppointment(String professionalId, TimeSlot timeSlot) {
        if (professionalId == null || timeSlot == null) {
            return FAILED + "a professional ID and a time slot are required to cancel an appointment.";
        }
        String wantedId = professionalId.trim();
        for (int index = 0; index < appointments.size(); index++) {
            Appointment appointment = appointments.get(index);
            if (appointment.getProfessional().getId().equals(wantedId)
                    && appointment.getTimeSlot().equals(timeSlot)) {
                appointments.remove(index);
                return CANCELLED + appointment.getPatient().getName() + "'s " + timeSlot
                        + " appointment with " + appointment.getProfessional().getName()
                        + " (ID " + wantedId + ") has been cancelled.";
            }
        }
        return FAILED + "no appointment was found at " + timeSlot
                + " for a health professional with ID " + wantedId + ".";
    }

    /**
     * Cancels every appointment booked with a patient's mobile number.
     *
     * <p>The loop runs backwards so that removing an element never skips the
     * element after it, which is the classic mistake when removing from a list
     * while walking forwards.</p>
     *
     * @param mobileNumber the mobile number the bookings were made with
     * @return a message describing the outcome
     */
    public String cancelAppointmentsForPatient(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.trim().isEmpty()) {
            return FAILED + "a mobile number is required to cancel an appointment.";
        }
        String wantedNumber = mobileNumber.trim();
        int removed = 0;
        for (int index = appointments.size() - 1; index >= 0; index--) {
            if (appointments.get(index).getPatient().getMobileNumber().equals(wantedNumber)) {
                appointments.remove(index);
                removed++;
            }
        }
        if (removed == 0) {
            return FAILED + "no appointments were found for mobile number " + wantedNumber + ".";
        }
        return CANCELLED + removed + " appointment(s) for mobile number " + wantedNumber
                + " have been cancelled.";
    }

    /**
     * Returns how many appointments are currently booked.
     *
     * @return the number of appointments held
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Returns the day the clinic is taking bookings for.
     *
     * @return the clinic day
     */
    public WeekDay getClinicDay() {
        return clinicDay;
    }

    /**
     * Returns the schedule bookings are checked against.
     *
     * @return the clinic schedule
     */
    public ClinicSchedule getSchedule() {
        return schedule;
    }
}
