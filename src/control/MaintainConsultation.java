package control;

import adt.*;
import dao.ConsultationDAO;
import entity.*;
import java.util.Scanner;

/**
 *
 * @author Low Zi Qing
 */
public class MaintainConsultation {

    private ListInterface<Consultation> consultations;
    private ListInterface<Patient> patients;
    private ListInterface<Doctor> doctors;
    private Scanner sc = new Scanner(System.in);
    private ConsultationDAO consultationDAO;

    public MaintainConsultation(ListInterface<Patient> patients, ListInterface<Doctor> doctors) {
        this.consultations = new LinkedList<>();
        this.patients = patients;
        this.doctors = doctors;
        this.consultationDAO = new ConsultationDAO();
        this.consultations = consultationDAO.retrieveFromFile();

        int maxId = 0;
        for (int i = 0; i < consultations.size(); i++) {
            String id = consultations.get(i).getId();
            int num = Integer.parseInt(id.substring(1));
            if (num > maxId) {
                maxId = num;
            }
        }
        Consultation.setCounter(maxId + 1);
    }

    // Adding new consultation
    public void addConsultation() {
        System.out.println("\n--- Add Consultation ---");

        // Select patient
        System.out.println("\nSelect patient:");
        for (int i = 0; i < patients.size(); i++) {
            System.out.println((i + 1) + ". " + patients.get(i).getName());
        }
        int pChoice = getValidatedChoice(patients.size());
        Patient selectedPatient = patients.get(pChoice - 1);

        // Select doctor
        System.out.println("\nSelect doctor:");
        for (int i = 0; i < doctors.size(); i++) {
            System.out.println((i + 1) + ". " + doctors.get(i).getName());
        }
        int dChoice = getValidatedChoice(doctors.size());
        Doctor selectedDoctor = doctors.get(dChoice - 1);

        // Enter diagnosis
        String diagnosis;
        do {
            System.out.print("Enter diagnosis: ");
            diagnosis = sc.nextLine().trim();
            if (diagnosis.isEmpty()) {
                System.out.println("Diagnosis cannot be empty.");
            }
        } while (diagnosis.isEmpty());

        // Follow-up
        boolean followUp = false;
        String input;
        do {
            System.out.print("Is follow-up required? (yes/no): ");
            input = sc.nextLine().trim().toLowerCase();
            if (input.equals("yes")) {
                followUp = true;
                break;
            } else if (input.equals("no")) {
                followUp = false;
                break;
            } else {
                System.out.println("Invalid input! Enter 'yes' or 'no'.");
            }
        } while (true);

        Consultation consultation = new Consultation(selectedPatient, selectedDoctor, diagnosis);
        consultation.setFollowUpRequired(followUp);
        consultations.add(consultation);
        consultationDAO.saveToFile(consultations);

        // Display added consultation in table
        ListInterface<Consultation> added = new LinkedList<>();
        added.add(consultation);
        System.out.println("\n--- Consultation Added ---");
        printConsultationsTable(added);
    }

    // View all consultations
    public void viewConsultations() {
        System.out.println("\n--- All Consultations ---");
        printConsultationsTable(consultations);
    }

    // =================== UPDATE ===================
    public void updateConsultation() {
        viewConsultations();
        if (consultations.isEmpty()) {
            return;
        }

        System.out.print("Enter Consultation ID to update: ");
        String id = sc.nextLine().trim();

        Consultation consultation = null;
        for (int i = 0; i < consultations.size(); i++) {
            if (consultations.get(i).getId().equalsIgnoreCase(id)) {
                consultation = consultations.get(i);
                break;
            }
        }

        if (consultation == null) {
            System.out.println("Consultation ID not found!");
            return;
        }

        int option;
        do {
            System.out.println("\n--- Update Consultation ---");
            System.out.println("1. Update Diagnosis");
            System.out.println("2. Change Doctor");
            System.out.println("3. Change Patient");
            System.out.println("4. Toggle Follow-up Requirement");
            System.out.println("0. Finish Updating");
            System.out.print("Enter choice: ");

            option = getValidatedChoice(4, true); // allow 0
            switch (option) {
                case 1 -> {
                    System.out.print("Enter new diagnosis: ");
                    String diagnosis = sc.nextLine().trim();
                    if (!diagnosis.isEmpty()) {
                        consultation.setDiagnosis(diagnosis);
                    } else {
                        System.out.println("Diagnosis cannot be empty.");
                    }
                }
                case 2 -> {
                    System.out.println("\nSelect new doctor:");
                    for (int i = 0; i < doctors.size(); i++) {
                        System.out.println((i + 1) + ". " + doctors.get(i).getName());
                    }
                    int choice = getValidatedChoice(doctors.size());
                    consultation.setDoctor(doctors.get(choice - 1));
                }
                case 3 -> {
                    System.out.println("\nSelect new patient:");
                    for (int i = 0; i < patients.size(); i++) {
                        System.out.println((i + 1) + ". " + patients.get(i).getName());
                    }
                    int choice = getValidatedChoice(patients.size());
                    consultation.setPatient(patients.get(choice - 1));
                }
                case 4 ->
                    consultation.setFollowUpRequired(!consultation.isFollowUpRequired());
                case 0 ->
                    System.out.println("Finished updating consultation.");
            }
        } while (option != 0);

        ListInterface<Consultation> updated = new LinkedList<>();
        updated.add(consultation);
        System.out.println("\n--- Updated Consultation ---");
        printConsultationsTable(updated);
        consultationDAO.saveToFile(consultations);
    }

    // Delete consultation
    public void deleteConsultation() {
        viewConsultations();
        if (consultations.isEmpty()) {
            return;
        }

        System.out.print("Enter Consultation ID to delete: ");
        String id = sc.nextLine().trim();

        Consultation toRemove = null;
        for (int i = 0; i < consultations.size(); i++) {
            if (consultations.get(i).getId().equalsIgnoreCase(id)) {
                toRemove = consultations.get(i);
                break;
            }
        }

        if (toRemove != null) {
            consultations.remove(toRemove);
            consultationDAO.saveToFile(consultations);
            ListInterface<Consultation> removed = new LinkedList<>();
            removed.add(toRemove);
            System.out.println("\n--- Deleted Consultation ---");
            printConsultationsTable(removed);
        } else {
            System.out.println("Consultation ID not found!");
        }
    }

    // Search consultations with various criteria
    public void searchByConsultationId(String consultationId) {
        ListInterface<Consultation> results = new LinkedList<>();
        for (int i = 0; i < consultations.size(); i++) {
            if (consultations.get(i).getId().equalsIgnoreCase(consultationId)) {
                results.add(consultations.get(i));
            }
        }
        System.out.println("\n--- Search Results for Consultation ID: " + consultationId + " ---");
        printConsultationsTable(results);
    }

    public void searchByPatientName(String patientName) {
        ListInterface<Consultation> results = new LinkedList<>();
        for (int i = 0; i < consultations.size(); i++) {
            if (consultations.get(i).getPatient().getName().equalsIgnoreCase(patientName)) {
                results.add(consultations.get(i));
            }
        }
        System.out.println("\n--- Search Results for Patient: " + patientName + " ---");
        printConsultationsTable(results);
    }

    public void searchByDoctorName(String doctorName) {
        ListInterface<Consultation> results = new LinkedList<>();
        for (int i = 0; i < consultations.size(); i++) {
            if (consultations.get(i).getDoctor().getName().equalsIgnoreCase(doctorName)) {
                results.add(consultations.get(i));
            }
        }
        System.out.println("\n--- Search Results for Doctor: " + doctorName + " ---");
        printConsultationsTable(results);
    }

    // Reports generation
    public void dailyReport() {
        System.out.println("\n--- Daily Consultation Report ---");
        ListInterface<ReportEntry> report = new LinkedList<>();

        for (int i = 0; i < consultations.size(); i++) {
            String dateKey = consultations.get(i).getDateTime().toLocalDate().toString();
            boolean found = false;

            for (int j = 0; j < report.size(); j++) {
                if (report.get(j).getPeriod().equals(dateKey)) {
                    report.get(j).increment();
                    found = true;
                    break;
                }
            }

            if (!found) {
                report.add(new ReportEntry(dateKey));
            }
        }

        if (report.isEmpty()) {
            System.out.println("No consultations recorded.");
            return;
        }

        System.out.printf("%-15s %-10s%n", "Date", "No. of Consultations");
        System.out.println("-------------------------------");
        for (int i = 0; i < report.size(); i++) {
            ReportEntry r = report.get(i);
            System.out.printf("%-15s %-10d%n", r.getPeriod(), r.getCount());
        }
    }

    public void weeklyReport() {
        System.out.println("\n--- Weekly Consultation Report ---");
        ListInterface<ReportEntry> report = new LinkedList<>();

        for (int i = 0; i < consultations.size(); i++) {
            String weekKey = String.valueOf(
                    consultations.get(i).getDateTime()
                            .get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR)
            );
            boolean found = false;
            for (int j = 0; j < report.size(); j++) {
                if (report.get(j).getPeriod().equals(weekKey)) {
                    report.get(j).increment();
                    found = true;
                    break;
                }
            }
            if (!found) {
                report.add(new ReportEntry(weekKey));
            }
        }

        if (report.isEmpty()) {
            System.out.println("No consultations recorded.");
            return;
        }

        System.out.printf("%-10s %-10s%n", "Week", "No. of Consultations");
        System.out.println("--------------------");
        for (int i = 0; i < report.size(); i++) {
            ReportEntry r = report.get(i);
            System.out.printf("%-10s %-10d%n", r.getPeriod(), r.getCount());
        }
    }

    public void monthlyReport() {
        System.out.println("\n--- Monthly Consultation Report ---");
        ListInterface<ReportEntry> report = new LinkedList<>();

        for (int i = 0; i < consultations.size(); i++) {
            String monthKey = consultations.get(i).getDateTime().getMonth() + " "
                    + consultations.get(i).getDateTime().getYear();
            boolean found = false;

            for (int j = 0; j < report.size(); j++) {
                if (report.get(j).getPeriod().equals(monthKey)) {
                    report.get(j).increment();
                    found = true;
                    break;
                }
            }

            if (!found) {
                report.add(new ReportEntry(monthKey));
            }
        }

        if (report.isEmpty()) {
            System.out.println("No consultations recorded.");
            return;
        }

        System.out.printf("%-15s %-10s%n", "Month", "No. of Consultations");
        System.out.println("-------------------------------");

        for (int i = 0; i < report.size(); i++) {
            ReportEntry r = report.get(i);
            System.out.printf("%-15s %-10d%n", r.getPeriod(), r.getCount());
        }
    }

    public void followUpReport() {
        System.out.println("\n--- Follow-up Appointment Report ---");
        ListInterface<Consultation> followUps = new LinkedList<>();
        for (int i = 0; i < consultations.size(); i++) {
            if (consultations.get(i).isFollowUpRequired()) {
                followUps.add(consultations.get(i));
            }
        }
        printConsultationsTable(followUps);
    }

    public void summaryReport() {
        int total = consultations.size();
        int followUps = 0;
        for (int i = 0; i < total; i++) {
            if (consultations.get(i).isFollowUpRequired()) {
                followUps++;
            }
        }

        System.out.println("\n--- Summary Report ---");
        System.out.printf("%-25s : %d%n", "Total consultations", total);
        System.out.printf("%-25s : %d%n", "Follow-up consultations", followUps);
        System.out.printf("%-25s : %d%n", "Routine consultations", (total - followUps));
    }

    // Print consultations in tabular format
    private void printConsultationsTable(ListInterface<Consultation> list) {
        if (list.isEmpty()) {
            System.out.println("No consultations found.");
            return;
        }

        System.out.printf("%-4s %-6s %-18s %-18s %-30s %-20s %-10s%n",
                "No.", "ID", "Patient", "Doctor", "Diagnosis", "Date & Time", "Follow-Up");
        System.out.println("---------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < list.size(); i++) {
            Consultation c = list.get(i);
            System.out.printf("%-4s %-6s %-18s %-18s %-30s %-20s %-10s%n",
                    i + 1,
                    c.getId(),
                    c.getPatient().getName(),
                    c.getDoctor().getName(),
                    c.getDiagnosis(),
                    c.getDateTime().toLocalDate() + " " + c.getDateTime().toLocalTime().withNano(0),
                    c.isFollowUpRequired() ? "Yes" : "No");
        }
    }

    // Validation helpers
    private int getValidatedChoice(int max) {
        return getValidatedChoice(max, false);
    }

    private int getValidatedChoice(int max, boolean allowZero) {
        int choice = -1;
        while (true) {
            System.out.print("Enter choice" + (allowZero ? " (0-" + max + ")" : " (1-" + max + ")") + ": ");
            String input = sc.nextLine().trim();
            if (input.matches("\\d+")) {
                choice = Integer.parseInt(input);
                if ((allowZero && choice >= 0 && choice <= max) || (!allowZero && choice >= 1 && choice <= max)) {
                    break;
                }
            }
            System.out.println("Invalid input! Try again.");
        }
        return choice;
    }

}
