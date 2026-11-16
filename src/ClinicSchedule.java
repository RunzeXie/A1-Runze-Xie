import java.util.ArrayList;
import java.util.List;

/**
 * The fixed daily schedule of time slots that the clinic runs to.
 *
 * <p>Because the schedule is modelled as an object rather than being hard coded
 * inside the booking logic, it is the only place that knows when the clinic
 * opens, when it closes and how long a slot lasts. If the clinic later moves to
 * 20 minute slots, or opens at 07:30, only this class is changed and every
 * booking check elsewhere keeps working.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public class ClinicSchedule {

    /** Number of minutes in one hour. */
    private static final int MINUTES_PER_HOUR = 60;

    /** The opening time used when the caller does not supply one: 08:00. */
    private static final int DEFAULT_OPENING_HOUR = 8;

    /** The closing time used when the caller does not supply one: 16:00. */
    private static final int DEFAULT_CLOSING_HOUR = 16;

    /** The length of one appointment slot used by default: 30 minutes. */
    private static final int DEFAULT_SLOT_LENGTH = 30;

    /** The first time at which an appointment may start. */
    private final TimeSlot openingTime;

    /** The time at which the clinic stops taking appointments. */
    private final TimeSlot closingTime;

    /** How many minutes each appointment slot lasts. */
    private final int slotLengthInMinutes;

    /**
     * Creates the clinic's standard schedule: 08:00 to 16:00 in 30 minute
     * slots.
     */
    public ClinicSchedule() {
        this(new TimeSlot(DEFAULT_OPENING_HOUR, 0),
             new TimeSlot(DEFAULT_CLOSING_HOUR, 0),
             DEFAULT_SLOT_LENGTH);
    }

    /**
     * Creates a schedule with a caller supplied opening time, closing time and
     * slot length.
     *
     * @param openingTime        the first bookable time
     * @param closingTime        the time the clinic closes; the last slot must
     *                           finish by then
     * @param slotLengthInMinutes the length of one slot in minutes
     * @throws IllegalArgumentException if the times are null, if the opening
     *                                  time is not before the closing time, if
     *                                  the slot length is not positive, or if
     *                                  the slot length does not divide the
     *                                  working day evenly
     */
    public ClinicSchedule(TimeSlot openingTime, TimeSlot closingTime, int slotLengthInMinutes) {
        if (openingTime == null || closingTime == null) {
            throw new IllegalArgumentException("A schedule needs an opening and a closing time.");
        }
        if (openingTime.compareTo(closingTime) >= 0) {
            throw new IllegalArgumentException("The opening time must be before the closing time.");
        }
        if (slotLengthInMinutes <= 0) {
            throw new IllegalArgumentException("The slot length must be greater than zero.");
        }
        int workingMinutes = closingTime.getMinutesAfterMidnight() - openingTime.getMinutesAfterMidnight();
        if (workingMinutes % slotLengthInMinutes != 0) {
            throw new IllegalArgumentException("The slot length must divide the working day evenly.");
        }
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.slotLengthInMinutes = slotLengthInMinutes;
    }

    /**
     * Returns the first bookable time of the day.
     *
     * @return the opening time
     */
    public TimeSlot getOpeningTime() {
        return openingTime;
    }

    /**
     * Returns the time at which the clinic closes.
     *
     * @return the closing time
     */
    public TimeSlot getClosingTime() {
        return closingTime;
    }

    /**
     * Returns the length of one appointment slot.
     *
     * @return the slot length in minutes
     */
    public int getSlotLengthInMinutes() {
        return slotLengthInMinutes;
    }

    /**
     * Builds every bookable time slot of the day.
     *
     * <p>The slots are generated from the opening time rather than listed by
     * hand, so the generated set always matches the configured schedule.</p>
     *
     * @return a new list containing every slot from the opening time up to,
     *         but not including, the closing time
     */
    public List<TimeSlot> getTimeSlots() {
        List<TimeSlot> slots = new ArrayList<>();
        int closingMinutes = closingTime.getMinutesAfterMidnight();
        for (int minutes = openingTime.getMinutesAfterMidnight();
                minutes < closingMinutes;
                minutes = minutes + slotLengthInMinutes) {
            slots.add(new TimeSlot(minutes / MINUTES_PER_HOUR, minutes % MINUTES_PER_HOUR));
        }
        return slots;
    }

    /**
     * Tests whether a time falls inside the clinic's opening hours.
     *
     * @param timeSlot the slot to test
     * @return {@code true} if the time is on or after opening, and before
     *         closing
     */
    public boolean isWithinWorkingDay(TimeSlot timeSlot) {
        if (timeSlot == null) {
            return false;
        }
        int openingMinutes = openingTime.getMinutesAfterMidnight();
        int closingMinutes = closingTime.getMinutesAfterMidnight();
        int slotMinutes = timeSlot.getMinutesAfterMidnight();
        return slotMinutes >= openingMinutes && slotMinutes < closingMinutes;
    }

    /**
     * Tests whether a time slot is one that the clinic can actually book.
     *
     * <p>A slot is bookable when it starts inside the working day and is
     * aligned with the schedule, so 09:15 is rejected even though it is a
     * valid time of day.</p>
     *
     * @param timeSlot the slot to test
     * @return {@code true} if the slot belongs to this schedule
     */
    public boolean isBookableSlot(TimeSlot timeSlot) {
        if (!isWithinWorkingDay(timeSlot)) {
            return false;
        }
        int openingMinutes = openingTime.getMinutesAfterMidnight();
        int slotMinutes = timeSlot.getMinutesAfterMidnight();
        return (slotMinutes - openingMinutes) % slotLengthInMinutes == 0;
    }

    /**
     * Describes the schedule in a single readable sentence, which is useful
     * both for the console output and for rejection messages.
     *
     * @return a description such as
     *         "08:00-16:00 in 30 minute slots (16 slots per day)"
     */
    public String describeSlots() {
        return openingTime + "-" + closingTime + " in " + slotLengthInMinutes
                + " minute slots (" + getTimeSlots().size() + " slots per day)";
    }

    /**
     * Returns the schedule in a readable form.
     *
     * @return the schedule description
     */
    @Override
    public String toString() {
        return describeSlots();
    }
}
