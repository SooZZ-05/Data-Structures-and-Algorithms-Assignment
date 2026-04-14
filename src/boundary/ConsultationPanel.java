package boundary;

/**
 *
 * @author Low Zi Qing
 */
import adt.LinkedList;
import adt.ListInterface;
import java.util.Scanner;
import control.MaintainConsultation;
import entity.*;

public class ConsultationPanel {

    private final MaintainConsultation mc;
    private static final Scanner sc = new Scanner(System.in);

    public ConsultationPanel(ListInterface<Patient> patients, ListInterface<Doctor> doctors) {
        mc = new MaintainConsultation(patients, doctors); // pass patients/doctors
    }

    public void menu() {
        int choice;
        do {
            System.out.println("\n=== Clinic Consultation Management ===");
            System.out.println("1. Add Consultation");
            System.out.println("2. View All Consultations");
            System.out.println("3. Search Consultation");
            System.out.println("4. Modify Consultation");
            System.out.println("5. Delete Consultation");
            System.out.println("6. Reports");
            System.out.println("0. Exit System");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input! Enter number: ");
                sc.next(); // clear invalid input
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    mc.addConsultation();
                    pause();
                }
                case 2 -> {
                    mc.viewConsultations();
                    pause();
                }
                case 3 ->
                    searchConsultation();
                case 4 -> {
                    mc.updateConsultation();
                    pause();
                }
                case 5 -> {
                    mc.deleteConsultation();
                    pause();
                }
                case 6 ->
                    reportMenu();
                case 0 ->
                    System.out.println("Exiting system... Goodbye!");
                default ->
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 0);
    }

    private void reportMenu() {
        int option;
        do {
            System.out.println("\n--- Consultation Reports ---");
            System.out.println("1. Daily Report");
            System.out.println("2. Weekly Report");
            System.out.println("3. Monthly Report");
            System.out.println("4. Follow-up Appointments Report");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input! Enter number: ");
                sc.next();
            }
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> {
                    mc.dailyReport();
                    pause();
                }
                case 2 -> {
                    mc.weeklyReport();
                    pause();
                }
                case 3 -> {
                    mc.monthlyReport();
                    pause();
                }
                case 4 -> {
                    mc.followUpReport();
                    pause();
                }
                case 0 ->
                    System.out.println("Returning to previous menu...");
                default ->
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (option != 0);
    }

    public void searchConsultation() {
        int choice;
        do {
            System.out.println("\n--- Search Consultation ---");
            System.out.println("1. By Consultation ID");
            System.out.println("2. By Patient Name");
            System.out.println("3. By Doctor Name");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input! Enter number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Consultation ID: ");
                    String id = sc.nextLine().trim();
                    mc.searchByConsultationId(id);
                    pause();
                }
                case 2 -> {
                    System.out.print("Enter Patient Name: ");
                    String patient = sc.nextLine().trim();
                    mc.searchByPatientName(patient);
                    pause();
                }
                case 3 -> {
                    System.out.print("Enter Doctor Name: ");
                    String doctor = sc.nextLine().trim();
                    mc.searchByDoctorName(doctor);
                    pause();
                }
                case 0 ->
                    System.out.println("Returning to previous menu...");
                default ->
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 0);
    }

    private void pause() {
        System.out.print("\nPress Enter to continue...");
        sc.nextLine();
    }

    // Reusable method to display consultations in a table
    public static void main(String[] args) {

        ListInterface<Patient> patients = new LinkedList<>();

        patients.add(new Patient("P001", "Alice", "001122-01-1234", 30, "012-1112223", "123 Street A", "No notes", "Female", "Cardiology", "Routine", false));
        patients.add(new Patient("P002", "Bob", "990101-02-5678", 40, "013-2223334", "456 Street B", "Diabetic", "Male", "General Medicine", "Routine", false));
        patients.add(new Patient("P003", "Charlie", "980303-03-6789", 25, "014-3334445", "789 Street C", "Allergic to penicillin", "Male", "Orthopedics", "Follow-up", true));

        ListInterface<Doctor> doctors = new LinkedList<>();

        doctors.add(new Doctor("Dr. Smith", "900101-01-1234", "Cardiology", "012-3456789", 100.0));
        doctors.add(new Doctor("Dr. Johnson", "850505-05-5678", "General Medicine", "013-9876543", 150.0));
        doctors.add(new Doctor("Dr. Lee", "920303-03-4321", "Orthopedics", "014-1234567", 120.0));

        MaintainConsultation mc = new MaintainConsultation(patients, doctors);

// Adding synthetic consultations
//        mc.addConsultation(new Consultation(patients.get(0), doctors.get(0), "Routine heart check-up"));
//        mc.addConsultation(new Consultation(patients.get(1), doctors.get(1), "High fever and flu symptoms"));
//        mc.addConsultation(new Consultation(patients.get(2), doctors.get(2), "Back pain follow-up"));
//        mc.addConsultation(new Consultation(patients.get(0), doctors.get(1), "Blood test follow-up"));
        ConsultationPanel ui = new ConsultationPanel(patients, doctors);
        ui.menu();
    }

}
