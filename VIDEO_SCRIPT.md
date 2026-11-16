# Video script — the four pillars of OOP in A1-RunzeXie

Maximum 5 minutes. The rubric rewards four things for **each** pillar: name it,
explain the concept in your own words, point to the exact place in your code,
and justify why you used it there. This script is a skeleton with timings — say
it in your own words, do not read it out.

Before recording: have the project open, `AssignmentOne` running in one window
and the code visible in the other, font size big enough to read on video. Run
the program once so the console output is on screen.

---

## 0:00 – 0:25 · Introduction

> "This is my PROG2004 Assessment 1. It models a clinic appointment system: five
> types of health professional, patients, appointments, and a manager that books,
> filters, sorts and cancels them. There is no user interface — `AssignmentOne`
> is a scripted demonstration. I'll explain how my code uses the four pillars of
> object-oriented programming."

*(Show the console output for a few seconds. Do not describe every class — you
only have five minutes.)*

---

## 0:25 – 1:30 · Pillar 1: Encapsulation

**Name it, then explain it:** "Encapsulation means an object hides its own data
and only lets the outside world change it in ways it allows, so the object
protects its own rules."

**Point at the code:**

- `HealthProfessional.java` — every field is `private` (`id`, `name`,
  `availableDays`).
- `getAvailableDays()` returns `new ArrayList<>(availableDays)` — a **copy**, not
  the internal list.
- `setAvailableDays()` passes the new value through `normaliseDays(...)`, which
  rejects `null`, rejects an empty roster, drops duplicates and sorts the days.
- `HealthProfessional.isValidId(...)` — the ID must be digits only, checked in
  the constructor.
- `Patient.isValidMobileNumber(...)` — 10 digits, starting with 0.
- In the demo: the two `try`/`catch` blocks in `AssignmentOne` show a bad ID and a
  bad phone number being refused.

**Justify it:** "There is no way to create a nurse with an empty name or a
patient with a five-digit phone number, because the only path to the data is
through validation. Returning a copy of the roster matters because otherwise
another class could hold a reference and quietly change who works on Monday,
after the booking checks already relied on it. That would be a bug that is almost
impossible to find."

---

## 1:30 – 2:35 · Pillar 2: Inheritance

**Name it, then explain it:** "Inheritance lets one class build on another, so
the shared data and behaviour live in one place and the specific classes only add
what is different about them."

**Point at the code:**

- `HealthProfessional` is the base class.
- `GeneralPractitioner extends HealthProfessional` — its constructor calls
  `super(id, name, availableDays)` first, then sets `consultingRoom`.
- Same pattern in `Surgeon`, `Nurse`, `Dietitian`, `Physiotherapist`.
- `INDENT` in the base class is `protected static final`, so every subclass
  formats its output the same way.
- Each subclass `toString()` starts with `super.toString()`.

**Justify it:** "The brief says a GP, a specialist and a nurse record different
information — but the ID, the name and the working days are identical for all of
them. If I copied those three fields into five classes I would have five places
to change when the roster rule changes, and five chances to change only four of
them. With inheritance there is one copy. And because the subclass constructor
calls `super(...)` first, the inherited part is always validated before the
subclass adds anything of its own, so an object can never be half-built."

---

## 2:35 – 3:35 · Pillar 3: Polymorphism

**Name it, then explain it:** "Polymorphism means one reference type can point at
different concrete objects, and the version of the method that runs is the one
belonging to the real object."

**Point at the code:**

- `List<HealthProfessional> professionals` in `AssignmentOne` — eight objects,
  five different classes, one loop.
- Inside the loop, `professional.describeRole()` — the GP, the surgeon and the
  nurse each run their own version.
- `getProfessionalType()` is used by `getSummary()`, so `Appointment.toString()`
  prints the correct type with no `if` statement.
- `Collections.sort` calling `Appointment.compareTo`, which delegates to
  `TimeSlot.compareTo`.
- Interfaces: `List<Prescriber> prescribers` holds a GP and a surgeon;
  `List<SampleCollector>` holds two nurses.

**Justify it:** "Part 3 asks whether the different types can be processed
together — polymorphism is the answer. Without it I would need a chain of `if
(obj instanceof Surgeon)` checks, and every one of them would have to be updated
when a new type is added. With it, adding a sixth type changes nothing in the
loop. The `Prescriber` interface shows the same idea for abilities rather than
types: only some professionals can prescribe, so the code that prescribes asks
for a `Prescriber`, not for a particular class."

---

## 3:35 – 4:35 · Pillar 4: Abstraction

**Name it, then explain it:** "Abstraction means modelling only what the system
needs and hiding the detail. I declare what must happen and let the subclasses
decide how."

**Point at the code:**

- `public abstract class HealthProfessional` — it cannot be instantiated. There
  is no such thing as a health professional with no specialty.
- Two abstract methods: `getProfessionalType()` and `describeRole()` — every
  subclass is *forced* by the compiler to answer them.
- `ClinicSchedule` — the rest of the system asks `isBookableSlot(slot)` and never
  needs to know that the clinic opens at 08:00, closes at 16:00, or that slots
  are 30 minutes. Changing the clinic's hours is a change to one object, not to
  the booking code.
- `AppointmentManager` — it depends on "a schedule" and "a health professional",
  not on five specific classes.

**Justify it:** "I chose an abstract class rather than a concrete one because the
brief hints that a plain generic professional makes no sense. If the class were
concrete, someone could create one, store it in the appointment list, and the
system would have a professional with no specialty and no duties. Making it
abstract turns that mistake into a compile error instead of a runtime surprise.
The design principle I applied is: depend on the least specific thing that does
the job — the manager only needs to know that a professional exists and works on
certain days."

---

## 4:35 – 5:00 · Wrap up

> "Those are the four pillars in my design: encapsulation through private fields
> and validating methods, inheritance from `HealthProfessional` into five
> specialised classes, polymorphism through the base type and the two capability
> interfaces, and abstraction through the abstract base class and the schedule
> object. Thank you."

*(Stop talking. Do not add a summary of every class — the five minutes are up.)*

---

## Rehearsal checklist

- [ ] Under 5 minutes when spoken at a normal pace — time yourself twice.
- [ ] Each pillar: named → explained → code shown → choice justified. All four
      steps, or the rubric loses the top band.
- [ ] The code you point at is on screen and readable (zoom in; do not show a
      whole file at 100% scale).
- [ ] Say the class and method names out loud as you point.
- [ ] Upload to SCU OneDrive, set the sharing so the marker can open it, and
      paste the link into the submission comments.
