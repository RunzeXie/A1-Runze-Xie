import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The demonstration program for Assessment 1.
 *
 * <p>The class contains no system logic of its own: it creates the objects that
 * model the health service, exercises their behaviour and prints the results.
 * In effect it is a short scripted simulation that stands in for real users,
 * covering Parts 1 to 6 of the brief.</p>
 *
 * @author  Runze Xie
 * @version 1.0
 */
public class AssignmentOne {

    /** A full width line used between sections of output. */
    private static final String SECTION_LINE =
            "================================================================================";

    /** A narrower line used between parts of a section. */
    private static final String SUB_LINE =
            "--------------------------------------------------------------------------------";

    /**
     * Runs the demonstration.
     *
     * @param args command line arguments, which are not used
     */
    public static void main(String[] args) {
        // The clinic only takes bookings for the current day. Monday is used as a
        // stand in for "today" so the demonstration prints the same result every
        // time it is run.
        WeekDay clinicDay = WeekDay.MONDAY;

        printBanner(clinicDay);

        // Rosters are built once and shared, which also shows that the roster is
        // copied into each object when it is constructed.
        List<WeekDay> mondayWednesdayFriday =
                Arrays.asList(WeekDay.MONDAY, WeekDay.WEDNESDAY, WeekDay.FRIDAY);
        List<WeekDay> tuesdayThursday =
                Arrays.asList(WeekDay.TUESDAY, WeekDay.THURSDAY);

        // =====================================================================
        // PARTS 1 AND 2 - modelling and specialising health professionals
        // =====================================================================
        printSection("PARTS 1 AND 2  -  HEALTH PROFESSIONALS");

        // Three general practitioners, created two different ways: the three
        // argument constructor uses the default Monday to Friday roster, the four
        // argument constructor takes an explicit roster.
        GeneralPractitioner drNguyen = new GeneralPractitioner(
                "1001", "Alice Nguyen", "Room 1", mondayWednesdayFriday);
        GeneralPractitioner drPetrov = new GeneralPractitioner(
                "1002", "Ivan Petrov", "Room 2", mondayWednesdayFriday);
        GeneralPractitioner drAdeyemi = new GeneralPractitioner(
                "1003", "Tolu Adeyemi", "Room 3");

        // One or two of each of the other types designed in Part 2.
        Surgeon drOkafor = new Surgeon(
                "2001", "Chidi Okafor", "Orthopaedics", "Theatre A", mondayWednesdayFriday);
        Nurse nurseBrown = new Nurse("3001", "Grace Brown", "Ward 2", "Morning");
        Nurse nursePatel = new Nurse("3002", "Ravi Patel", "Ward 3", "Evening", tuesdayThursday);
        Dietitian msCosta = new Dietitian(
                "4001", "Silvia Costa", "Weight management", mondayWednesdayFriday);
        Physiotherapist mrNovak = new Physiotherapist(
                "5001", "Peter Novak", "Sports injury", tuesdayThursday);

        printSubSection("The same object, printed in full");
        System.out.println(drNguyen);
        System.out.println();
        System.out.println(nurseBrown);
        System.out.println();

        printSubSection("Objects cannot be created in an invalid state");
        // An ID must be digits only, so this attempt is refused by the validator
        // inside HealthProfessional rather than creating a broken object.
        try {
            GeneralPractitioner invalidDoctor = new GeneralPractitioner("GP-1001", "Invalid Id", "Room 9");
            System.out.println("  This line never runs: " + invalidDoctor.getName());
        } catch (IllegalArgumentException problem) {
            System.out.println("  Refused a professional with a non numeric ID: " + problem.getMessage());
        }
        // A contact number is required before an appointment can exist.
        try {
            Patient invalidPatient = new Patient("No Number", "12345");
            System.out.println("  This line never runs: " + invalidPatient.getName());
        } catch (IllegalArgumentException problem) {
            System.out.println("  Refused a patient with an invalid mobile: " + problem.getMessage());
        }
        System.out.println();

        // =====================================================================
        // PART 3 - working with the objects through the common type
        // =====================================================================
        printSection("PART 3  -  ALL PROFESSIONALS THROUGH ONE COMMON TYPE");

        // Every type is stored in one list of the abstract base type, so the loop
        // below handles eight objects of five different classes identically.
        List<HealthProfessional> professionals = new ArrayList<>();
        professionals.add(drNguyen);
        professionals.add(drPetrov);
        professionals.add(drAdeyemi);
        professionals.add(drOkafor);
        professionals.add(nurseBrown);
        professionals.add(nursePatel);
        professionals.add(msCosta);
        professionals.add(mrNovak);

        for (HealthProfessional professional : professionals) {
            System.out.println(professional);
            System.out.println("    Duties         : " + professional.describeRole());
            System.out.println();
        }
        System.out.println("Registered health professionals: " + professionals.size());
        System.out.println();

        printSubSection("Behaviour that only one type of professional has");
        System.out.println("  " + drNguyen.referToSpecialist("Jane Doe", "Cardiologist"));
        System.out.println("  " + drOkafor.performSurgery("Omar Haddad", "a knee reconstruction"));
        System.out.println("  " + msCosta.createMealPlan("Jane Doe", "lower her blood pressure"));
        System.out.println("  " + mrNovak.designExerciseProgram("Mia Rossi", 6));
        System.out.println();

        printSubSection("Capabilities shared through interfaces, not through the base class");
        List<Prescriber> prescribers = new ArrayList<>();
        prescribers.add(drNguyen);
        prescribers.add(drOkafor);
        for (Prescriber prescriber : prescribers) {
            System.out.println("  " + prescriber.prescribe("Amoxicillin", "500 mg every 8 hours"));
        }
        List<SampleCollector> collectors = new ArrayList<>();
        collectors.add(nurseBrown);
        collectors.add(nursePatel);
        for (SampleCollector collector : collectors) {
            System.out.println("  " + collector.collectBloodSample("Liam Chen"));
        }
        System.out.println();

        // =====================================================================
        // PART 4 - modelling an appointment
        // =====================================================================
        printSection("PART 4  -  MODELLING AN APPOINTMENT");

        Patient janeDoe = new Patient("Jane Doe", "0412345678");
        Patient liamChen = new Patient("Liam Chen", "0422334455");
        Patient miaRossi = new Patient("Mia Rossi", "0433998877");
        Patient noahWilson = new Patient("Noah Wilson", "0444556677");
        Patient avaThompson = new Patient("Ava Thompson", "0455667788");
        Patient omarHaddad = new Patient("Omar Haddad", "0466778899");

        Appointment morningWithGp = new Appointment(janeDoe, drNguyen, new TimeSlot(9, 0));
        Appointment sameTimeWithSurgeon = new Appointment(omarHaddad, drOkafor, new TimeSlot(9, 0));

        System.out.println("One appointment, printed with the full details of the professional:");
        System.out.println(morningWithGp.toDetailedString());
        System.out.println();

        System.out.println("Two appointments at the same time, ordered by professional ID:");
        // The list is deliberately built in the wrong order, then sorted, which
        // shows that Appointment knows how to order itself.
        List<Appointment> twoBookings = new ArrayList<>();
        twoBookings.add(sameTimeWithSurgeon);
        twoBookings.add(morningWithGp);
        Collections.sort(twoBookings);
        for (Appointment appointment : twoBookings) {
            System.out.println("  " + appointment);
        }
        System.out.println();

        // =====================================================================
        // PART 5 - managing the collection of appointments
        // =====================================================================
        printSection("PART 5  -  MANAGING THE COLLECTION OF APPOINTMENTS");

        ClinicSchedule schedule = new ClinicSchedule();
        AppointmentManager manager = new AppointmentManager(schedule, clinicDay);
        System.out.println("Clinic schedule: " + schedule);
        System.out.println("Every booking check reads this one schedule object, so a change to the");
        System.out.println("clinic's hours or slot length flows through the whole system.");
        System.out.println();

        printSubSection("Adding appointments");
        // The first booking is handed over as a ready made Appointment object, the
        // rest use the convenience overload that takes the three parts.
        System.out.println("  " + manager.addAppointment(morningWithGp));
        System.out.println("  " + manager.addAppointment(janeDoe, msCosta, new TimeSlot(11, 0)));
        System.out.println("  " + manager.addAppointment(liamChen, drNguyen, new TimeSlot(10, 30)));
        System.out.println("  " + manager.addAppointment(sameTimeWithSurgeon));
        System.out.println("  " + manager.addAppointment(miaRossi, nurseBrown, new TimeSlot(8, 0)));
        System.out.println("  " + manager.addAppointment(avaThompson, drNguyen, new TimeSlot(15, 30)));
        System.out.println();

        printSubSection("Bookings that must be refused");
        // The GP already has 09:00, so this is a double booking.
        System.out.println("  " + manager.addAppointment(noahWilson, drNguyen, new TimeSlot(9, 0)));
        // 09:15 is a real time of day, but not one of the clinic's slots.
        System.out.println("  " + manager.addAppointment(noahWilson, nurseBrown, new TimeSlot(9, 15)));
        // The physiotherapist does not work on Mondays.
        System.out.println("  " + manager.addAppointment(noahWilson, mrNovak, new TimeSlot(14, 0)));
        // 17:30 is after the clinic closes.
        System.out.println("  " + manager.addAppointment(noahWilson, drNguyen, new TimeSlot(17, 30)));
        System.out.println();
        System.out.println("  Appointments held after the attempts above: " + manager.getAppointmentCount());
        System.out.println();

        printSubSection("All appointments, earliest first");
        printAppointments(manager.getAppointmentsInTimeOrder());

        printSubSection("Filtered by health professional ID 1001");
        printAppointments(manager.findByProfessional("1001"));

        printSubSection("Filtered by patient mobile number 0412 345 678");
        printAppointments(manager.findByPatientMobile("0412345678"));

        printSubSection("The same day, ordered by professional name (Comparator)");
        printAppointments(manager.sortByProfessionalName(manager.getAppointmentsInTimeOrder()));

        printSubSection("Deciding whether two professionals are the same person");
        // A second object with the same ID represents the same person, which is
        // what the double booking check relies on.
        GeneralPractitioner anotherObjectForSameGp =
                new GeneralPractitioner("1001", "Alice Nguyen", "Room 9");
        System.out.println("  equals() on the same ID       : " + drNguyen.equals(anotherObjectForSameGp));
        System.out.println("  hashCode() also agrees        : "
                + (drNguyen.hashCode() == anotherObjectForSameGp.hashCode()));
        System.out.println("  a different professional      : " + drNguyen.equals(drPetrov));
        System.out.println();

        printSubSection("Cancelling appointments");
        System.out.println("  " + manager.cancelAppointment("1001", new TimeSlot(10, 30)));
        // The same cancellation a second time must fail, because it is now gone.
        System.out.println("  " + manager.cancelAppointment("1001", new TimeSlot(10, 30)));
        // An ID that does not exist must also fail clearly.
        System.out.println("  " + manager.cancelAppointment("9999", new TimeSlot(9, 0)));
        // Cancelling by patient removes every booking held against that number.
        System.out.println("  " + manager.cancelAppointmentsForPatient("0412345678"));
        System.out.println("  " + manager.cancelAppointmentsForPatient("0499999999"));
        System.out.println();

        // =====================================================================
        // PART 6 - the collection after the cancellations
        // =====================================================================
        printSection("PART 6  -  THE UPDATED COLLECTION");

        printAppointments(manager.getAppointmentsInTimeOrder());
        System.out.println("  Appointments remaining: " + manager.getAppointmentCount());
        System.out.println();
        System.out.println("End of demonstration.");
    }

    /**
     * Prints the opening banner, including the day used for the demonstration.
     *
     * @param clinicDay the day the clinic is taking bookings for
     */
    private static void printBanner(WeekDay clinicDay) {
        System.out.println(SECTION_LINE);
        System.out.println(" PROG2004 Object Oriented Programming - Assessment 1");
        System.out.println(" Health Service Appointment System");
        System.out.println(" Student: Runze Xie");
        System.out.println(" Clinic day used for this demonstration: " + clinicDay.getFullName());
        System.out.println(SECTION_LINE);
        System.out.println();
    }

    /**
     * Prints a main section heading.
     *
     * @param title the heading text
     */
    private static void printSection(String title) {
        System.out.println(SECTION_LINE);
        System.out.println(" " + title);
        System.out.println(SECTION_LINE);
        System.out.println();
    }

    /**
     * Prints a sub heading within a section.
     *
     * @param title the sub heading text
     */
    private static void printSubSection(String title) {
        System.out.println(SUB_LINE);
        System.out.println(" " + title);
        System.out.println(SUB_LINE);
    }

    /**
     * Prints a list of appointments, or a clear message when the list is empty.
     *
     * @param appointments the appointments to print
     */
    private static void printAppointments(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("  (no appointments to show)");
        } else {
            for (int index = 0; index < appointments.size(); index++) {
                System.out.println("  " + (index + 1) + ". " + appointments.get(index));
            }
            System.out.println("  " + appointments.size() + " appointment(s) listed.");
        }
        System.out.println();
    }
}
