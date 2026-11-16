import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The information and behaviour that is common to every health professional in
 * the clinic, whether that is a general practitioner, a surgeon, a nurse, a
 * dietitian or a physiotherapist.
 *
 * <p>The class is {@code abstract} on purpose. There is no such thing in the
 * clinic as a health professional with no specialty, so allowing
 * {@code new HealthProfessional(...)} would let a meaningless object exist.
 * Making the class abstract stops that at compile time, and the two abstract
 * methods force every concrete subclass to state what kind of professional it
 * is and what it actually does.</p>
 *
 * <p>All fields are private and are only reachable through validating methods,
 * so an object can never be put into an invalid state by other code: an ID
 * made of letters, a blank name or an empty roster is rejected immediately.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public abstract class HealthProfessional {

    /** The indentation every subclass uses when adding its own details. */
    protected static final String INDENT = "    ";

    /** The roster given to a professional when no roster is supplied. */
    private static final List<WeekDay> DEFAULT_AVAILABLE_DAYS = createDefaultAvailableDays();

    /** The numeric identifier of the professional, for example "1001". */
    private final String id;

    /** The full name of the professional. */
    private String name;

    /** The days of the week on which the professional works. */
    private List<WeekDay> availableDays;

    /**
     * Creates a health professional who works the default roster, Monday to
     * Friday.
     *
     * <p>This is the second way of constructing an object mentioned in the
     * brief: most staff work a normal week, so the caller only supplies the ID
     * and the name and the sensible default is applied for them.</p>
     *
     * @param id   the numeric identifier, for example "1001"
     * @param name the full name of the professional
     */
    public HealthProfessional(String id, String name) {
        this(id, name, DEFAULT_AVAILABLE_DAYS);
    }

    /**
     * Creates a health professional with a caller supplied roster.
     *
     * @param id            the numeric identifier, for example "1001"
     * @param name          the full name of the professional
     * @param availableDays the days of the week on which they work
     * @throws IllegalArgumentException if the ID is not made of digits, if the
     *                                  name is blank, or if the roster is
     *                                  empty
     */
    public HealthProfessional(String id, String name, List<WeekDay> availableDays) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException(
                    "A health professional ID must contain digits only, but was: " + id);
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("A health professional must have a name.");
        }
        this.id = id.trim();
        this.name = name.trim();
        this.availableDays = normaliseDays(availableDays);
    }

    /**
     * Builds the default Monday to Friday roster.
     *
     * <p>The list is wrapped as unmodifiable so that the shared default cannot
     * be changed by accident from outside the class.</p>
     *
     * @return an unmodifiable list containing Monday to Friday
     */
    private static List<WeekDay> createDefaultAvailableDays() {
        List<WeekDay> days = new ArrayList<>();
        days.add(WeekDay.MONDAY);
        days.add(WeekDay.TUESDAY);
        days.add(WeekDay.WEDNESDAY);
        days.add(WeekDay.THURSDAY);
        days.add(WeekDay.FRIDAY);
        return Collections.unmodifiableList(days);
    }

    /**
     * Validates a roster and copies it into a new list.
     *
     * <p>A copy is made so that the caller cannot later change the professional's
     * roster by keeping a reference to the list they passed in. Duplicate days
     * are dropped and the days are sorted into Monday to Sunday order.</p>
     *
     * @param days the roster supplied by the caller
     * @return a sorted, duplicate free copy of the roster
     * @throws IllegalArgumentException if the roster is null, empty or contains
     *                                  a null day
     */
    private static List<WeekDay> normaliseDays(List<WeekDay> days) {
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("A health professional must work on at least one day.");
        }
        List<WeekDay> copy = new ArrayList<>();
        for (WeekDay day : days) {
            if (day == null) {
                throw new IllegalArgumentException("A working day must not be null.");
            }
            if (!copy.contains(day)) {
                copy.add(day);
            }
        }
        Collections.sort(copy);
        return copy;
    }

    /**
     * Tests whether a value is a valid professional ID, which must contain
     * digits only.
     *
     * @param id the value to test
     * @return {@code true} if the ID is not null, not blank and made of digits
     */
    public static boolean isValidId(String id) {
        if (id == null) {
            return false;
        }
        String trimmed = id.trim();
        if (trimmed.isEmpty()) {
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
     * Returns the numeric identifier of the professional.
     *
     * @return the ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the professional.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the professional.
     *
     * @param name the new name
     * @throws IllegalArgumentException if the name is null or blank
     */
    public final void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("A health professional must have a name.");
        }
        this.name = name.trim();
    }

    /**
     * Returns the days on which the professional works.
     *
     * <p>A copy is returned rather than the internal list, so that the roster
     * can be read but not edited from outside the class.</p>
     *
     * @return a copy of the roster, sorted Monday to Sunday
     */
    public List<WeekDay> getAvailableDays() {
        return new ArrayList<>(availableDays);
    }

    /**
     * Replaces the roster of the professional.
     *
     * @param availableDays the new roster
     * @throws IllegalArgumentException if the roster is null, empty or contains
     *                                  a null day
     */
    public final void setAvailableDays(List<WeekDay> availableDays) {
        this.availableDays = normaliseDays(availableDays);
    }

    /**
     * Tests whether the professional works on a particular day.
     *
     * @param day the day to test
     * @return {@code true} if the professional is rostered on that day
     */
    public boolean worksOn(WeekDay day) {
        if (day == null) {
            return false;
        }
        return availableDays.contains(day);
    }

    /**
     * Returns the roster as a short readable list, for example "Mon, Wed, Fri".
     *
     * @return the roster in short form
     */
    public String getAvailabilityDescription() {
        StringBuilder description = new StringBuilder();
        for (int index = 0; index < availableDays.size(); index++) {
            if (index > 0) {
                description.append(", ");
            }
            description.append(availableDays.get(index).getShortName());
        }
        return description.toString();
    }

    /**
     * Returns one line that identifies the professional and their type.
     *
     * <p>This is used by {@link Appointment}, which needs to name the chosen
     * professional without reprinting the whole record.</p>
     *
     * @return a summary such as "[General Practitioner] Alice Nguyen (ID: 1001)"
     */
    public String getSummary() {
        return "[" + getProfessionalType() + "] " + name + " (ID: " + id + ")";
    }

    /**
     * Returns the type of health professional, for example
     * "General Practitioner".
     *
     * <p>The method is abstract because only a subclass knows the answer. It is
     * what lets code that holds a {@code HealthProfessional} reference still
     * display, and reason about, the real type of the object.</p>
     *
     * @return the type of professional
     */
    public abstract String getProfessionalType();

    /**
     * Returns a short sentence describing what this type of professional does.
     *
     * <p>Each subclass supplies its own answer, so two objects referenced
     * through {@code HealthProfessional} respond differently to the same call.</p>
     *
     * @return a description of the professional's duties
     */
    public abstract String describeRole();

    /**
     * Returns all details of the professional in a readable form, so that
     * {@code System.out.println(professional)} prints something useful.
     *
     * <p>Subclasses call this method and then append their own extra details,
     * which means the shared information is formatted in exactly one place.</p>
     *
     * @return the details of the professional
     */
    @Override
    public String toString() {
        StringBuilder details = new StringBuilder();
        details.append(getSummary());
        details.append(System.lineSeparator());
        details.append(INDENT).append("Available days : ").append(getAvailabilityDescription());
        return details.toString();
    }

    /**
     * Tests whether this professional and another object mean the same person.
     *
     * <p>Two professionals are the same person when they carry the same ID; the
     * name is ignored because names can be corrected or abbreviated. This is the
     * rule the booking system relies on to spot a professional who is already
     * booked at a time.</p>
     *
     * @param other the object to compare with
     * @return {@code true} if the other object is a health professional with the
     *         same ID
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HealthProfessional)) {
            return false;
        }
        HealthProfessional otherProfessional = (HealthProfessional) other;
        return id.equals(otherProfessional.id);
    }

    /**
     * Returns a hash code based on the ID, consistent with
     * {@link #equals(Object)}.
     *
     * @return the hash code of the ID
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
