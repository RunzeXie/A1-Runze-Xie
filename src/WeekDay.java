/**
 * The days of the week on which health professionals can be rostered.
 *
 * <p>An {@code enum} is used for the week days because the set of possible
 * values is fixed and known in advance. This means the system can never store
 * an invalid day (for example "Funday"), and each constant also carries the
 * text that the system prints, so presentation detail lives with the data it
 * describes.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public enum WeekDay {

    /** Monday. */
    MONDAY("Monday", "Mon"),

    /** Tuesday. */
    TUESDAY("Tuesday", "Tue"),

    /** Wednesday. */
    WEDNESDAY("Wednesday", "Wed"),

    /** Thursday. */
    THURSDAY("Thursday", "Thu"),

    /** Friday. */
    FRIDAY("Friday", "Fri"),

    /** Saturday. */
    SATURDAY("Saturday", "Sat"),

    /** Sunday. */
    SUNDAY("Sunday", "Sun");

    /** The name of the day written in full. */
    private final String fullName;

    /** The three letter abbreviation used in compact output. */
    private final String shortName;

    /**
     * Creates a week day constant.
     *
     * <p>The constructor is implicitly private, which is exactly what an enum
     * needs: the complete list of constants is declared above and no other
     * {@code WeekDay} object can ever be created.</p>
     *
     * @param fullName  the name of the day written in full
     * @param shortName the three letter abbreviation of the day
     */
    private WeekDay(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    /**
     * Returns the name of the day written in full.
     *
     * @return the full name of the day, for example "Wednesday"
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Returns the three letter abbreviation of the day.
     *
     * @return the short name of the day, for example "Wed"
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Returns the full name of the day so that printing a week day reads
     * naturally instead of printing the constant name in capitals.
     *
     * @return the full name of the day
     */
    @Override
    public String toString() {
        return fullName;
    }
}
