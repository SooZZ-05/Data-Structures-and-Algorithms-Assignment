import java.util.Scanner;
import adt.ListInterface;
import java.util.Scanner;
import boundary.*;
import control.MaintainDoctor;
import dao.*;
import entity.*;

public class Main {
    public static void main(String[] args) {
        PatientPanel patientPanel;
        DoctorPanel doctorPanel;
        ConsultationPanel consultationPanel;
        PatientDAO patientDAO = new PatientDAO();
        DoctorDAO doctorDAO = new DoctorDAO();

        ListInterface<Patient> patients = patientDAO.retrieveFromFile();
        ListInterface<Doctor> doctors = doctorDAO.retrieveFromFile();

        patientPanel = new PatientPanel();
        doctorPanel = new DoctorPanel();
        consultationPanel = new ConsultationPanel(patients, doctors);
        Scanner sc = new Scanner(System.in);
        MaintainDoctor maintainDoctor = new MaintainDoctor();
        int choice;

        do {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Manage Patients");
            System.out.println("2. Manage Doctors");
            System.out.println("3. Manage Consultations");
            System.out.println("4. Manage Treatments");
            System.out.println("5. Manage Medicines");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    // Example call
                    patientPanel.start();
                    break;
                case 2:
                    maintainDoctor.runDoctorMaintenance();
                    break;
                case 3:
                    consultationPanel.menu();
                    break;
                case 4:
                    TreatmentEntryUI ui = new TreatmentEntryUI();
                    ui.start();
                    break;
                case 5:
                    PharmacyCLI.main(args);
                    break;
                case 0:
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);

        sc.close();
    }
}