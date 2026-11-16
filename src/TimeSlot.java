/**
 * A time of day, for example 08:00 or 14:30, at which a clinic appointment
 * starts.
 *
 * <p>The time is stored as two integers (hour and minute) rather than as text
 * such as "9:00". Storing numbers means the time can be compared
 * <em>correctly</em>: as text, "10:00" would sort before "9:00", which is
 * wrong. The class implements {@link Comparable} so that a whole collection of
 * appointments can be sorted by time by the standard library.</p>
 *
 * <p>The object is immutable: once created, its hour and minute can never
 * change. That suits a booking system, because the time of an existing booking
 * must not drift, and it also means a {@code TimeSlot} can be shared safely
 * between an appointment and a printed timetable.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /** Number of minutes in one hour. */
    private static final int MINUTES_PER_HOUR = 60;

    /** The largest valid hour value. */
    private static final int LAST_HOUR = 23;

    /** The largest valid minute value. */
    private static final int LAST_MINUTE = 59;

    /** The hour of the day, 0 to 23. */
    private final int hour;

    /** The minute within the hour, 0 to 59. */
    private final int minute;

    /**
     * Creates a time slot.
     *
     * @param hour   the hour of the day, 0 to 23
     * @param minute the minute within the hour, 0 to 59
     * @throws IllegalArgumentException if the hour or the minute is out of range
     */
    public TimeSlot(int hour, int minute) {
        if (hour < 0 || hour > LAST_HOUR) {
            throw new IllegalArgumentException("Hour must be between 0 and 23, but was: " + hour);
        }
        if (minute < 0 || minute > LAST_MINUTE) {
            throw new IllegalArgumentException("Minute must be between 0 and 59, but was: " + minute);
        }
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Returns the hour of the day.
     *
     * @return the hour, 0 to 23
     */
    public int getHour() {
        return hour;
    }

    /**
     * Returns the minute within the hour.
     *
     * @return the minute, 0 to 59
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Converts the time to a single number of minutes after midnight.
     *
     * <p>This is what makes comparing and subtracting two times easy: 09:30
     * becomes 570 and 10:00 becomes 600, so 600 is clearly the later time.</p>
     *
     * @return the number of minutes elapsed since midnight
     */
    public int getMinutesAfterMidnight() {
        return hour * MINUTES_PER_HOUR + minute;
    }

    /**
     * Compares this time slot with another one in chronological order.
     *
     * <p>Returning a negative number means this slot is earlier, zero means
     * the same time, and a positive number means this slot is later. This is
     * the method {@code Collections.sort} uses when it orders appointments.</p>
     *
     * @param other the time slot to compare with
     * @return a negative number, zero, or a positive number as described above
     */
    @Override
    public int compareTo(TimeSlot other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare a time slot with null.");
        }
        int thisMinutes = getMinutesAfterMidnight();
        int otherMinutes = other.getMinutesAfterMidnight();
        if (thisMinutes < otherMinutes) {
            return -1;
        }
        if (thisMinutes > otherMinutes) {
            return 1;
        }
        return 0;
    }

    /**
     * Tests whether this time slot represents the same time of day as another
     * object.
     *
     * @param other the object to compare with
     * @return {@code true} if the other object is a {@code TimeSlot} with the
     *         same hour and minute
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TimeSlot)) {
            return false;
        }
        TimeSlot otherSlot = (TimeSlot) other;
        return hour == otherSlot.hour && minute == otherSlot.minute;
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}, so that time
     * slots work correctly in hash based collections.
     *
     * @return a hash code based on the hour and the minute
     */
    @Override
    public int hashCode() {
        return hour * 100 + minute;
    }

    /**
     * Returns the time in 24 hour {@code hh:mm} form.
     *
     * @return the time formatted for display, for example "09:30"
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}
