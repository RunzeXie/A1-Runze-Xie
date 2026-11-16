# A1-rxie — Health Service Appointment System

PROG2004 Object Oriented Programming — Assessment 1 (Parts 1–6).

A small Java model of a clinic appointment system: health professionals of
several types, patients, appointments, and a manager that books, filters, sorts
and cancels them. There is no user interface — `AssignmentOne.main` runs a
scripted demonstration and prints the results to the console, as the brief
requires.

> **Before you submit:** rename the project folder and the zip to
> `A1-<your MySCU username>`, and put your own name and student number in the
> `@author` tag of every class and in `AssignmentOne.printBanner`.

---

## 1. Project structure

```
A1-rxie/
├── src/
│   ├── AssignmentOne.java          main method, the Part 3 and Part 6 demonstration
│   ├── HealthProfessional.java     Part 1  abstract base class (ID, name, roster)
│   ├── GeneralPractitioner.java    Part 2  GP + consulting room + referrals
│   ├── Surgeon.java                Part 2  surgeon + specialty + theatre + surgery
│   ├── Nurse.java                  Part 2  nurse + ward + shift + blood samples
│   ├── Dietitian.java              Part 2  dietitian + dietary focus + meal plans
│   ├── Physiotherapist.java        Part 2  physiotherapist + focus + exercise plans
│   ├── Prescriber.java             interface: the ability to prescribe
│   ├── SampleCollector.java        interface: the ability to take a blood sample
│   ├── Patient.java                Part 4  patient name + validated mobile number
│   ├── Appointment.java            Part 4  patient + professional + time slot
│   ├── AppointmentManager.java     Part 5  the collection and all of its operations
│   ├── TimeSlot.java               a time of day, Comparable, ordering by time
│   ├── ClinicSchedule.java         Part 5  the fixed daily set of slots
│   └── WeekDay.java                enum of the seven days
├── sample-output.txt               the console output of a full run, for reference
├── VIDEO_SCRIPT.md                 script and timings for the four pillars video
├── README.md
└── .gitignore
```

### How the classes relate

```
                    <<interface>>          <<interface>>
                     Prescriber            SampleCollector
                         ^                       ^
                         | implements            | implements
                +--------+--------+              |
                |                 |              |
        GeneralPractitioner    Surgeon          Nurse
                ^                 ^              ^
                |    extends      |              |
        +-------+-----------------+--------------+
        |                                       |
   HealthProfessional  (abstract: id, name, List<WeekDay> availableDays,
        |               abstract getProfessionalType(), describeRole())
        |
        |  also extends
   Dietitian, Physiotherapist


   Appointment  *--> Patient                  (composition: an appointment
                *--> HealthProfessional        cannot exist without both)
                *--> TimeSlot

   AppointmentManager  1 --> * Appointment
                       1 --> 1 ClinicSchedule
                       1 --> 1 WeekDay (the current clinic day)
```

---

## 2. How to run

From the project root, with a JDK installed:

```bash
javac -d out src/*.java
java -cp out AssignmentOne
```

In an IDE: create a new Java project, point `src` at the source folder, and run
`AssignmentOne`. No external libraries are used — only the standard library
(`java.util`).

`sample-output.txt` is the console output of a complete run, so you can compare
what you see when you run it yourself. Regenerate it if you change the code:

```bash
java -cp out AssignmentOne > sample-output.txt
```

---

## 3. Design decisions and their justification

The brief asks a set of design questions in each part. These are the answers the
code implements, and they are the points to be ready to explain in the video and
in any interview about the submission.

### Part 1 — Modelling a health professional

| Question from the brief | Decision in the code | Why |
|---|---|---|
| What is genuinely common to all professionals? | `id`, `name`, `availableDays` live in `HealthProfessional` and nowhere else | A surgeon, a nurse and a dietitian all need exactly this data, so putting it in the base class means it is written once and inherited five times |
| How do you store a fixed collection of values such as the working days? | `List<WeekDay>` backed by an `enum` | The set of days is fixed, so an `enum` makes an invalid day impossible; a list (not a single value) because a professional can work several days; the list is sorted and de-duplicated on the way in |
| Controlled access? | All fields `private`; getters return **copies**; setters validate | Returning the internal list would let other code edit the roster behind the object's back. A copy keeps the object in charge of its own state |
| Display all details when printed? | `toString()` overridden; subclasses call `super.toString()` | `System.out.println(obj)` always uses `toString()`, so overriding it is the Java way to make printing "just work" |
| More than one way to construct? | Two constructors: `(id, name)` uses Monday–Friday, `(id, name, days)` takes a roster | Most staff work a normal week. The short constructor removes boilerplate at every call site without removing flexibility |

### Part 2 — Specialising

- **Relationship:** inheritance (`extends`). The shared code is inherited, not
  copied — there is no duplicated ID/name/roster logic in any subclass.
- **Unique data and behaviour:** each subclass adds only what is its own
  (`consultingRoom`, `surgicalSpecialty` + `operatingTheatre`, `ward` + `shift`,
  `dietaryFocus`, `focusArea`) plus its own methods (`referToSpecialist`,
  `performSurgery`, `collectBloodSample`, `createMealPlan`,
  `designExerciseProgram`).
- **Two ways to construct:** every subclass offers a short constructor
  (default roster) and a long one (explicit roster).
- **Setting up both parts:** each subclass constructor calls `super(...)` first,
  so the inherited state is validated by the base class, then validates its own
  fields. An object can therefore never be half-built.
- **Presenting both parts:** `toString()` in each subclass starts from
  `super.toString()` and appends its own lines, so the shared fields are
  formatted in exactly one place.
- **Same type, different behaviour:** `getProfessionalType()` and
  `describeRole()` are abstract, so a `List<HealthProfessional>` can hold five
  different classes and each object answers for itself.
- **No plain "generic" professional:** `HealthProfessional` is `abstract`, which
  makes `new HealthProfessional(...)` a compile error and forces every subclass
  to implement the abstract methods.
- **Capabilities as interfaces:** prescribing is implemented by
  `GeneralPractitioner` and `Surgeon`; taking blood is implemented by `Nurse`.
  These are capabilities, not kinds of professional, so they belong in
  `Prescriber` and `SampleCollector` rather than in the base class.

### Part 3 — Working with the objects

- One `ArrayList<HealthProfessional>`, one enhanced `for` loop, eight objects,
  five classes: no `if`, no repetition, and each object prints itself.
- Adding a sixth type later means writing **one new class** and adding one line
  to that list. No existing class changes — that is the test of a good design.
- The same polymorphism is used for capabilities: `List<Prescriber>` and
  `List<SampleCollector>`.

### Part 4 — Modelling an appointment

- **`Patient` is its own class.** The name/phone validation then lives in one
  place, one patient object can be reused across bookings, and a richer patient
  record can be added later without touching `Appointment`.
- **An appointment holds** a `Patient`, a `HealthProfessional` and a
  `TimeSlot` — as references, not as copied text.
- **Any type of professional** works because the field is declared as the
  abstract base type. The appointment never needs to know which subclass it has.
- **Never incomplete:** all three fields are `final` and the constructor rejects
  `null`; the referenced objects validate their own contents (a phone number
  must be 10 digits starting with 0). A bad appointment cannot be constructed.
- **Ordering:** `Appointment implements Comparable<Appointment>`, delegating the
  time comparison to `TimeSlot.compareTo` and breaking ties on professional ID.
  `Collections.sort` can then order any collection of appointments.
- **Storing the time:** two `int`s (hour, minute) inside `TimeSlot`, compared via
  `getMinutesAfterMidnight()`. As text, `"10:00"` would sort before `"9:00"`;
  as numbers 600 > 540, which is correct.

### Part 5 — Managing the collection

- **Data structure: `ArrayList`.** The clinic needs to keep every booking in
  order, iterate, filter and sort, and allow two patients to hold the same time
  with different professionals — all natural for a list. The real uniqueness
  rule ("one professional, one slot") is a business rule, so it is enforced in
  `addAppointment`, not by the container.
- **Any type of professional:** the add method takes `HealthProfessional` (and
  `Appointment` holds one), so nothing changes when a new type is added.
- **Cancelling something that does not exist:** returns a `FAILED :` message
  naming what could not be found, rather than throwing or silently doing nothing.
- **Feedback:** every operation returns a prefixed message —
  `CONFIRMED`, `REJECTED`, `CANCELLED`, `FAILED`. The manager never prints; the
  caller decides where the message goes. That keeps the logic testable and the
  output formatted in one place.
- **The fixed daily schedule:** `ClinicSchedule` holds the opening time, closing
  time and slot length, and can generate the slots. Moving to 20-minute slots is
  a one-line change in one object, and `isBookableSlot` then rejects everything
  that no longer fits — the booking logic is untouched.
- **Are two professionals the same?** Same ID means same person, implemented with
  `equals`/`hashCode` on `id`. Name is ignored because names get corrected or
  abbreviated. This is what makes the double-booking check reliable even if the
  caller passes a different object for the same doctor.
- **Time order:** `Collections.sort` uses the natural order from `Comparable`.
  A second view is provided with a `Comparator` (`sortByProfessionalName`), which
  re-orders the *same* objects by a different rule without changing the class.

### Part 6 — Demonstration

`AssignmentOne.main` covers, in order:

1. Parts 1–2: eight professionals of five types, created two ways, printed in
   full; two invalid objects rejected with their messages.
2. Part 3: all of them processed through `HealthProfessional`, then each type's
   own behaviour, then the two capability interfaces.
3. Part 4: one appointment in full detail, and two same-time appointments sorted.
4. Part 5: six successful bookings, then four refusals — double booking, a time
   that is not a clinic slot, a professional who does not work that day, and a
   time outside opening hours.
5. Filtering by professional ID, filtering by patient mobile, sorting by
   professional name, and the `equals`/`hashCode` rule.
6. Cancelling by professional + time (twice, so the second shows the failure
   message), cancelling an unknown ID, cancelling by patient number (removes
   both of that patient's bookings), and cancelling an unknown number.
7. The updated collection, and a final count.

---

## 4. Assumptions

- The clinic day is a `WeekDay` constant (`WeekDay.MONDAY`) rather than a real
  date, because the brief restricts bookings to the current day and a fixed day
  makes the demonstration repeatable. `AppointmentManager` takes the day as a
  constructor argument, so swapping in `LocalDate.now()` later is a one-line
  change.
- A professional's roster is checked when a booking is made, so a professional
  who does not work that day cannot be booked.
- Two patients may hold the same time with different professionals; the brief
  only forbids double-booking one professional.
- `IllegalArgumentException` is used for programmer errors (an invalid ID, a
  blank name); the manager deliberately returns messages instead for booking
  outcomes, because those are normal business results.

---

## 5. Extending the system

To add, for example, a Radiographer:

1. Write `Radiographer extends HealthProfessional`, adding the fields that are
   specific to it (imaging modality, scanning room).
2. Override `getProfessionalType()` and `describeRole()`.
3. Add one line to the `professionals` list in `AssignmentOne`.

Nothing in `Appointment`, `AppointmentManager`, `ClinicSchedule` or any existing
professional changes. That is the benefit of programming to the base type.

---

## 6. Submission checklist

| Item | Detail |
|---|---|
| Project zip | `A1-<username>.zip`, containing the whole project (`src` at minimum) |
| GitHub repository | Public, or private with the tutor and unit assessor invited as collaborators. Commit regularly with descriptive messages from now until the due date — a single "final" commit will lose marks on the professional practice criterion |
| WakaTime | Screenshot or PDF report saved as `A1-<username>-WakaTime.png/jpg/pdf`. WakaTime must actually be running while you work in the IDE |
| Video | Maximum 5 minutes, uploaded to SCU OneDrive, shareable link added to the submission comments |
| GenAI declaration | In the submission comments — this assessment is GenAI **Level 2**, so code and comments are not to be generated. Read the brief's declaration wording and state honestly what was used |

### Suggested commit plan (so the history shows incremental progress)

Commit in this order, one logical step at a time, with messages like these:

```
Add WeekDay enum for professional rosters
Add TimeSlot with Comparable so times sort correctly
Add abstract HealthProfessional with validated id, name and roster
Add GeneralPractitioner extending HealthProfessional
Add Surgeon and Nurse with capability interfaces
Add Dietitian and Physiotherapist
Demonstrate all professionals through the common base type
Add Patient with mobile number validation
Add Appointment with Comparable ordering by time
Add ClinicSchedule and slot validation
Add AppointmentManager with booking and double-booking checks
Add filtering by professional and by patient
Add sorting by time and by professional name
Add cancellation by professional/time and by patient
Add error case demonstrations and formatted console output
Add Javadoc and inline comments
```
