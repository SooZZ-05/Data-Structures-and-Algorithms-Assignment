package boundary;

/** Author: Teo Geok Woon */

import adt.ArrayQueue;
import adt.ListInterface;
import control.MaintainPatient;
import control.MaintainPatient.Pair;
import entity.Patient;
import util.PatientValidation;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Scanner;

public class PatientPanel {
    private final MaintainPatient manager;
    private final Scanner scanner;

    private static void sep()     { System.out.println("=".repeat(100)); }
    private static void midsep()  { System.out.println("-".repeat(100)); }
    private static void center(String s){ System.out.println(centerLine(s, 100)); }

    public PatientPanel() {
        manager = new MaintainPatient();
        scanner = new Scanner(System.in);
    }

    public void showMenu() {
        System.out.println("\nMenu:");
        System.out.println("1) Register new patient");
        System.out.println("2) View all patients");
        System.out.println("3) Search patient by ID");
        System.out.println("4) Search patients by IC Number");
        System.out.println("5) Update patient record");
        System.out.println("6) Delete patient record");
        System.out.println("7) Enqueue patient to waiting queue");
        System.out.println("8) Dequeue next patient");
        System.out.println("9) View waiting queue");
        System.out.println("10) Summary Report");
        System.out.println("0) Exit");
        System.out.print("Enter choice: ");
        System.out.flush();
    }

    private void handleChoice(String c) {
        switch (c) {
            case "1": clear(); registerPatient(); break;
            case "2": clear(); viewAllPatients(); break;
            case "3": clear(); viewPatientById(); break;
            case "4": clear(); searchByIc(); break;
            case "5": clear(); updatePatient(); break;
            case "6": clear(); deletePatient(); break;
            case "7": clear(); enqueuePatient(); break;
            case "8": clear(); dequeuePatient(); break;
            case "9": clear(); viewQueue(); break;
            case "10": clear(); runReports(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void registerPatient() {
        System.out.println("--- Register Patient ---");
        String ic = PatientValidation.readIC(scanner);
        Patient existing = manager.findPatientByIcExact(ic);
        if (existing != null) {
            System.out.println("This IC is already registered as: " + existing.getName() + " (ID " + existing.getPatientId() + ")");
            System.out.println("1) Use existing record and enqueue");
            System.out.println("2) Update details then enqueue");
            System.out.println("3) Cancel");
            System.out.print("Choice: ");
            String c = scanner.nextLine().trim();

            switch (c) {
                case "1": {
                    manager.logReturningRegistration(existing.getPatientId());
                    boolean enq = manager.enqueuePatient(existing.getPatientId());
                    System.out.println(enq ? "Enqueued existing patient." : "Already in queue or failed.");
                    return;
                }
                case "2": {
                    interactiveUpdateFields(existing.getPatientId());
                    manager.logReturningRegistration(existing.getPatientId());
                    boolean enq2 = manager.enqueuePatient(existing.getPatientId());
                    System.out.println(enq2 ? "Enqueued." : "Already in queue or failed.");
                    return;
                }
                default: System.out.println("Cancelled."); return;
            }
        }
        String id = manager.generatePatientId();
        String name = PatientValidation.readName(scanner);
        int age = PatientValidation.readAge(scanner);
        String gender = PatientValidation.readGender(scanner);
        String faculty = PatientValidation.readNonEmpty(scanner, "Enter department: ");
        String phone = PatientValidation.readPhone(scanner);
        String addr = PatientValidation.readNonEmpty(scanner, "Enter patient Address: ");
        System.out.print("Enter notes (optional): ");
        String notes = scanner.nextLine().trim();
        String appointmentType = PatientValidation.readAppointmentType(scanner);
        boolean emergency = PatientValidation.readEmergency(scanner);

        Patient p = new Patient(id, name, ic, age, phone, addr, notes, gender, faculty, appointmentType, emergency);
        boolean ok = manager.addPatient(p);
        System.out.println(ok ? "Registered. Patient ID: " + id : "Registration failed (duplicate).");
    }

    private void viewAllPatients() {
        System.out.println("--- All Patients ---");
        ListInterface<Patient> all = manager.listAllPatients();
        if (all.isEmpty()) { System.out.println("(no patients)"); return; }
        for (int i = 0; i < all.size(); i++) System.out.println(all.get(i));
    }

    private void viewPatientById() {
        System.out.print("Enter patient ID: ");
        String id = scanner.nextLine().trim();
        Patient p = manager.findPatientById(id);
        System.out.println(p == null ? "Not found." : p);
    }

    private void interactiveUpdateFields(String patientId) {
        Patient p = manager.findPatientById(patientId);
        if (p == null) { System.out.println("Patient not found."); return; }
        while (true) {
            System.out.println("\n--- Update Patient (" + p.getPatientId() + " | " + p.getName() + ") ---");
            System.out.println("1) Name"); System.out.println("2) IC"); System.out.println("3) Age");
            System.out.println("4) Phone"); System.out.println("5) Address"); System.out.println("6) Notes");
            System.out.println("7) Gender"); System.out.println("8) Department");
            System.out.println("9) Appointment Type"); System.out.println("10) Emergency");
            System.out.println("0) Done");
            System.out.print("Choose field: ");
            String c = scanner.nextLine().trim();

            boolean ok = false;
            switch (c) {
                case "1": ok = manager.updateField(patientId, MaintainPatient.UpdateField.NAME, PatientValidation.readName(scanner)); break;
                case "2": {
                    String newIc = PatientValidation.readIC(scanner);
                    ok = manager.updateField(patientId, MaintainPatient.UpdateField.IC, newIc);
                    if (!ok) System.out.println("IC already in use by another patient.");
                    break;
                }
                case "3": ok = manager.updateField(patientId, MaintainPatient.UpdateField.AGE, Integer.valueOf(PatientValidation.readAge(scanner))); break;
                case "4": ok = manager.updateField(patientId, MaintainPatient.UpdateField.PHONE, PatientValidation.readPhone(scanner)); break;
                case "5": ok = manager.updateField(patientId, MaintainPatient.UpdateField.ADDRESS, PatientValidation.readNonEmpty(scanner,"Enter new address: ")); break;
                case "6": System.out.print("Enter notes (can be blank): "); ok = manager.updateField(patientId, MaintainPatient.UpdateField.NOTES, scanner.nextLine().trim()); break;
                case "7": ok = manager.updateField(patientId, MaintainPatient.UpdateField.GENDER, PatientValidation.readGender(scanner)); break;
                case "8": ok = manager.updateField(patientId, MaintainPatient.UpdateField.DEPARTMENT, PatientValidation.readNonEmpty(scanner,"Enter department/faculty: ")); break;
                case "9": ok = manager.updateField(patientId, MaintainPatient.UpdateField.APPOINTMENT_TYPE, PatientValidation.readAppointmentType(scanner)); break;
                case "10": ok = manager.updateField(patientId, MaintainPatient.UpdateField.EMERGENCY, Boolean.valueOf(PatientValidation.readEmergency(scanner))); break;
                case "0": System.out.println("Done updating."); return;
                default: System.out.println("Invalid choice."); continue;
            }
            System.out.println(ok ? "Updated." : "Update failed.");
            p = manager.findPatientById(patientId);
        }
    }

    private void updatePatient() {
        String id = PatientValidation.readPatientId(scanner, "Enter patient ID to update: ");
        interactiveUpdateFields(id);
    }

    private void deletePatient() {
        String id = PatientValidation.readPatientId(scanner, "Enter patient ID to delete: ");
        boolean ok = manager.deletePatient(id);
        System.out.println(ok ? "Deleted." : "Delete failed (not found).");
    }

    private void enqueuePatient() {
        String id = PatientValidation.readPatientId(scanner, "Enter patient ID to enqueue: ");
        boolean ok = manager.enqueuePatient(id);
        System.out.println(ok ? "Enqueued." : "Enqueue failed (not found or already in queue).");
    }

    private void dequeuePatient() {
        Patient p = manager.dequeuePatient();
        System.out.println(p == null ? "Queue is empty." : "Dequeued: " + p);
    }

    private void viewQueue() {
        System.out.println("--- Queue ---");
        ArrayQueue<Patient> em = manager.getEmergencyQueueSnapshot();
        ArrayQueue<Patient> nm = manager.getNormalQueueSnapshot();
        if (em.isEmpty() && nm.isEmpty()) { System.out.println("(queue empty)"); return; }

        int pos = 1;
        System.out.println("Emergency Queue");
        System.out.println("===============");
        Iterator<Patient> itEm = em.getIterator();
        if (!itEm.hasNext()) System.out.println("(none)");
        else while (itEm.hasNext()) System.out.printf("%d) %s%n", pos++, itEm.next().getName());

        System.out.println();
        System.out.println("Normal Queue");
        System.out.println("============");
        Iterator<Patient> itNm = nm.getIterator();
        if (!itNm.hasNext()) System.out.println("(none)");
        else while (itNm.hasNext()) System.out.printf("%d) %s%n", pos++, itNm.next().getName());
    }

    private void searchByIc() {
        System.out.print("Enter IC search term: ");
        String term = scanner.nextLine().trim();
        ArrayQueue<Patient> res = manager.searchByIc(term);
        Iterator<Patient> it = res.getIterator();
        if (!it.hasNext()) { System.out.println("No matches."); return; }
        while (it.hasNext()) System.out.println(it.next());
    }

    private String getReportMenuChoice() {
        sep(); center("REPORTS"); sep();
        System.out.println("1) Daily Patient Registration Report");
        System.out.println("2) Patient Queue Status Report");
        System.out.println("0) Back to Main Menu");
        System.out.print("Choose: ");
        return scanner.nextLine().trim();
    }

    private void runReports() {
        while (true) {
            clear();
            String ch = getReportMenuChoice();
            switch (ch) {
                case "1": {
                    clear();
                    LocalDate day = readDateOrToday();
                    clear();
                    MaintainPatient.DailyRegistrationStats stats = manager.generateDailyRegistrationStats(day);
                    printDailyRegistrationReport(day, stats);
                    pressEnter();
                    break;
                }
                case "2": {
                    clear();
                    MaintainPatient.QueueStatusSnapshot snap = manager.generateQueueStatusSnapshot();
                    printQueueStatusReport(snap);
                    pressEnter();
                    break;
                }
                case "0": return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private LocalDate readDateOrToday() {
        System.out.print("Use today? (Y/n): ");
        String yn = scanner.nextLine().trim();
        if (!yn.equalsIgnoreCase("n")) return LocalDate.now();
        while (true) {
            System.out.print("Enter date (yyyy-MM-dd): ");
            String s = scanner.nextLine().trim();
            try { return LocalDate.parse(s); } catch (Exception ex) { System.out.println("Invalid date."); }
        }
    }

    private void printDailyPatientHeader() {
        sep();
        center("PATIENT MANAGEMENT SUBSYSTEM");
        center("DAILY PATIENT REGISTRATION REPORT");
        sep();
        String ts = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("Generated at: %s%n", ts);
        midsep();
    }

    private void printDailyRegistrationReport(LocalDate day, MaintainPatient.DailyRegistrationStats s) {
        printDailyPatientHeader();
        System.out.printf("Date: %s%n", day);
        midsep();

        System.out.printf("Total REGISTRATION events today            : %d%n", s.totalRegistrationEvents);
        System.out.printf("Total DISTINCT patients registered today   : %d%n", s.distinctRegistered);
        System.out.printf("New patient registrations today            : %d%n", s.newPatients);
        System.out.printf("Returning registrations today              : %d%n", s.returningPatients);
        System.out.println();

        System.out.printf("Total VISITS (enqueues) today              : %d%n", s.totalEnqueueEvents);
        System.out.printf("Distinct patients ENQUEUED today           : %d%n", s.distinctEnqueuedPatients);
        midsep();

        System.out.println("Hourly Enqueue Histogram:");
        System.out.println("Hour | Count | Bar");
        System.out.println("-----+-------+----------------------------------------");
        for (int h = 0; h < 24; h++) System.out.printf("%02d   | %5d | %s%n", h, s.hourlyEnqueues[h], bar(s.hourlyEnqueues[h]));
        if (s.busiestHour >= 0) System.out.printf("%nBusiest hour: %02d:00 (%d enqueues)%n", s.busiestHour, s.busiestHourCount);
        midsep();

        System.out.printf("Gender (registrations): Male=%d  Female=%d  Unknown=%d%n", s.male, s.female, s.genderUnknown);
        System.out.printf("Age groups (registrations): Children(<18)=%d  Adults(18-64)=%d  Seniors(65+)=%d%n", s.children, s.adults, s.seniors);

        System.out.println("Departments (by registration events):");
        if (s.deptCounts.isEmpty()) System.out.println("  (none)");
        else {
            for (int i = 0; i < s.deptCounts.size(); i++) {
                Pair<String,Integer> e = s.deptCounts.get(i);
                System.out.printf("  - %-25s : %d%n", e.key, e.value);
            }
        }
        System.out.println();

        System.out.printf("Appointment Types (registrations): Walk-in=%d  Appointment=%d  Unknown=%d%n", s.regWalkIn, s.regAppointment, s.regUnknown);
        System.out.printf("Appointment Types (enqueues)     : Walk-in=%d  Appointment=%d%n", s.enqWalkIn, s.enqAppointment);
        if (s.regAppointment == 0) System.out.println("Registration ratio (Walk-in : Appointment) : ∞ : 1");
        else System.out.printf("Registration ratio (Walk-in : Appointment) : %.2f : 1%n", (s.regWalkIn * 1.0) / s.regAppointment);
        if (s.enqAppointment == 0) System.out.println("Enqueue ratio (Walk-in : Appointment)      : ∞ : 1");
        else System.out.printf("Enqueue ratio (Walk-in : Appointment)      : %.2f : 1%n", (s.enqWalkIn * 1.0) / s.enqAppointment);

        sep(); center("END OF THE REPORT"); sep();
    }

    private void printQueueStatusReport(MaintainPatient.QueueStatusSnapshot q) {
        sep(); center("PATIENT QUEUE STATUS REPORT"); sep();
        String ts = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.printf("Generated at: %s%n", ts);
        midsep();

        System.out.printf("Current queue length                       : %d%n", q.queueLength);
        System.out.printf("Estimated average wait (currently waiting) : %d minutes%n", q.avgWaitMinutesCurrent);
        System.out.printf("Priority cases (emergencies) in queue      : %d%n", q.emergencyCount);
        midsep();

        System.out.println("Daily Wait (last 7 days):");
        System.out.println("Date        | Served | Avg Wait (min)");
        System.out.println("------------+--------+---------------");
        for (int i = 0; i < q.last7Days.size(); i++) {
            MaintainPatient.QueueStatusSnapshot.DayWait d = q.last7Days.get(i);
            System.out.printf("%s | %6d | %13d%n", d.date, d.served, d.avgWaitMinutes);
        }
        midsep();

        System.out.println("Current Queue Composition:");
        System.out.printf("%-8s %-22s %-5s %-16s %-10s %-10s%n", "Pos", "Name", "Age", "Department", "Emergency", "MinWait");
        System.out.println("--------+----------------------+-----+----------------+----------+----------");
        for (int i = 0; i < q.rows.size(); i++) {
            MaintainPatient.QueueStatusSnapshot.Row r = q.rows.get(i);
            System.out.printf("%-8d %-22s %-5d %-16s %-10s %-10d%n",
                    r.position, r.name, r.age, r.department, (r.emergency ? "YES" : "no"), r.minutesWait);
        }
        sep(); center("END OF THE REPORT"); sep();
    }

    private static String centerLine(String s, int w) {
        if (s.length() >= w) return s;
        int pad = (w - s.length())/2;
        return " ".repeat(Math.max(0, pad)) + s;
    }
    private static String bar(int n) { return "#".repeat(Math.min(40, Math.max(0, n))); }

    private void pressEnter() { System.out.print("\nPress Enter to continue..."); scanner.nextLine(); }

    public static void clear() { System.out.print("\033[H\033[2J"); System.out.flush(); }

    public void start() {
        System.out.println("=== Patient Management Module ===");
        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) { System.out.println("Exiting. Goodbye."); break; }
            if (!choice.matches("^(?:[0-9]|10)$")) {  // FIX: 0–10 only
                System.out.println("Please enter a valid menu option (0-10).");
                continue;
            }
            handleChoice(choice);
            System.out.println();
        }
    }

}
