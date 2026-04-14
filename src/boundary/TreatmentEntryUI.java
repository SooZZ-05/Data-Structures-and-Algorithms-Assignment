package boundary;

import control.MedicalTreatmentManagement;
import dao.MedicineDAO;
import entity.Medicine;
import entity.Treatment;
import util.checkValidation;
import adt.ArrayList;
import control.MedicalTreatmentManagement;
import dao.MedicineDAO;
import entity.DispenseRecord;
import java.util.Scanner;
import control.PharmacyControlInterface;
import control.PharmacyControlImpl;
import entity.DispenseRecord;
import entity.Medicine;
import java.time.LocalDate;
import java.util.Scanner;
import control.PharmacyControlInterface;
import util.DateProvider;

public class TreatmentEntryUI {
    private MedicalTreatmentManagement management;
    private MedicineDAO medicineDAO;
    private final PharmacyControlInterface control = new PharmacyControlImpl();
    private final Scanner sc = new Scanner(System.in);
    
    public TreatmentEntryUI() {
        management = new MedicalTreatmentManagement();
        medicineDAO = MedicineDAO.getInstance();
        Scanner scanner = new Scanner(System.in);
    }
    
    public void start() {
        System.out.println("=== Medical Treatment Management System ===");
        
        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Create New Treatment");
            System.out.println("2. Add Medicine to Treatment");
            System.out.println("3. View Treatment Details");
            System.out.println("4. View Patient Treatment History");
            System.out.println("5. Calculate Treatment Total");
            System.out.println("6. Get Recommended Medicines for Diagnosis");
            System.out.println("7. View All Medicine Prices");
            System.out.println("8. Update Treatment Notes");
            System.out.println("9. Exit");
            
            int choice = checkValidation.getValidInt("Choose option: ", 1, 9);
            
            switch (choice) {
                case 1:
                    createTreatment();
                    break;
                case 2:
                    addMedicineToTreatment();
                    break;
                case 3:
                    viewTreatmentDetails();
                    break;
                case 4:
                    viewPatientHistory();
                    break;
                case 5:
                    calculateTotal();
                    break;
                case 6:
                    getRecommendedMedicines();
                    break;
                case 7:
                    viewAllMedicinePrices();
                    break;
                case 8:
                    updateTreatmentNotes();
                    break;
                case 9:
                    System.out.println("Exiting system... Thank you!");
                    return;
            }
        }
    }
    
    // NEW METHOD: View All Medicine Prices
    private void viewAllMedicinePrices() {
        System.out.println("\n=== View All Medicine Prices ===");
        
        ArrayList<Medicine> medicines = medicineDAO.getAllMedicines();
        if (medicines.isEmpty()) {
            System.out.println("No medicines available in the system!");
            return;
        }
        
        System.out.println("============================================================================");
        System.out.println("|                            ALL MEDICINE PRICES                           |");
        System.out.println("============================================================================");
        System.out.printf("| %-8s %-20s %-11s %-30s |\n", "ID", "Medicine Name", "Price", "Description");
        System.out.println("============================================================================");
        
        for (Medicine medicine : medicines) {
            System.out.printf("| %-8s %-20s RM%-9.2f %-30s |\n",
                medicine.getId(),
                medicine.getName(),
                medicine.getUnitPrice());
//                truncateDescription(medicine.getDescription(), 30));
        }
        System.out.println("============================================================================");
    }
    
    // Helper method to truncate long descriptions
    private String truncateDescription(String description, int maxLength) {
        if (description.length() <= maxLength) {
            return description;
        }
        return description.substring(0, maxLength - 3) + "";
    }
    
    private void createTreatment() {
        System.out.println("\n=== Create New Treatment ===");
        
        String patientId = checkValidation.getValidPatientId();
        
        ArrayList<String> diagnoses = medicineDAO.getAllDiagnoses();
        if (diagnoses.isEmpty()) {
            System.out.println("No diagnoses available!");
            return;
        }
        
        System.out.println("Available Diagnoses:");
        for (int i = 0; i < diagnoses.size(); i++) {
            System.out.println((i + 1) + ". " + diagnoses.get(i));
        }
        
        int diagChoice = checkValidation.getValidInt("Select diagnosis number: ", 1, diagnoses.size());
        String diagnosis = diagnoses.get(diagChoice - 1);
        
        Treatment treatment = management.createTreatment(patientId, diagnosis);
        
        if (treatment == null) {
            System.out.println("Failed to create treatment!");
            return;
        }
        
        String treatmentId = treatment.getTreatmentId();
        System.out.println("Created Treatment with ID: " + treatmentId);
        
        ArrayList<Medicine> recommended = medicineDAO.getRecommendedMedicines(diagnosis);
        if (!recommended.isEmpty()) {
            System.out.println("\n=== Recommended Medicines for " + diagnosis + " ===");
            System.out.println("============================================================");
            System.out.printf("| %-20s %-11s %-21s   |\n", "Medicine", "Price", "Description");
            System.out.println("============================================================");
            
            for (Medicine medicine : recommended) {
                System.out.printf("| %-20s RM%-9.2f %-23s |\n",
                    medicine.getName(),
                    medicine.getUnitPrice());
//                    truncateDescription(medicine.getDescription(), 25));
            }
            System.out.println("============================================================");
            
            if (checkValidation.getYesNoConfirmation("\nDo you want to add all recommended medicines?")) {
                System.out.println("\n=== Adding Recommended Medicines ===");
                for (Medicine medicine : recommended) {
                    boolean success = management.addMedicineToTreatment(treatmentId, medicine.getId());
                    if (success) {
                        System.out.println("Added: " + medicine.getName() + " (RM" + 
                            String.format("%.2f", medicine.getUnitPrice()) + ")");
                    } else {
                        System.out.println("Failed to add: " + medicine.getName());
                    }
                }
            }
        }
        
        double total = management.calculateTreatmentTotal(treatmentId);
        System.out.println("\n Total Amount: RM" + String.format("%.2f", total));
        
        // Ask for additional notes
        if (checkValidation.getYesNoConfirmation("\nDo you want to add doctor notes?")) {
            updateTreatmentNotesForTreatment(treatmentId);
        }
    }
    
    private void addMedicineToTreatment() {
        System.out.println("(Module4) Enter medicine lines (ID and quantity). Enter 0 or blank to finish.");

        DispenseRecord r = null;
        double total = 0.0;
        System.out.print("Medicine ID (0 or blank to cancel): ");
        String id = sc.nextLine().trim();
        if (id.isEmpty() || id.equals("0")) {
            System.out.println("No items entered. Medical list cancelled.");
            return;
        }
        r = control.beginMedicalList();

        while (true) {
            System.out.print("Qty: ");
            int q;
            try {
                q = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Skipped.");
                // ask for next ID
                System.out.print("Medicine ID (0 or blank to finish): ");
                id = sc.nextLine().trim();
                if (id.isEmpty() || id.equals("0")) break;
                continue;
            }
            if (q <= 0) {
                System.out.println("Quantity must be positive. Skipped.");
            } else {
                double newTotal = control.addItemToDispense(r, id, q);
                if (newTotal < 0) {
                    System.out.println("Error: invalid ID or insufficient stock. Not added.");
                } else {
                    total = newTotal;
                    System.out.printf("Added. Current total = RM %.2f%n", total);
                }
            }

            System.out.print("Medicine ID (0 or blank to finish): ");
            id = sc.nextLine().trim();
            if (id.isEmpty() || id.equals("0")) break;
        }

        if (r != null) {
            System.out.printf("Final total (MEDICAL LIST): RM %.2f%n", control.finalizeTotal(r));
        }
    }
    
    private void viewTreatmentDetails() {
        System.out.println("\n=== View Treatment Details ===");
        
        String treatmentId = checkValidation.getValidTreatmentId();
        Treatment treatment = management.getTreatment(treatmentId);
        
        if (treatment != null) {
            System.out.println("\n TREATMENT DETAILS");
            System.out.println("======================================================================");
            System.out.printf("| %-30s: %-34s |\n", "Treatment ID", treatment.getTreatmentId());
            System.out.printf("| %-30s: %-34s |\n", "Patient ID", treatment.getPatientId());
            System.out.printf("| %-30s: %-34s |\n", "Diagnosis", treatment.getDiagnosis());
            System.out.printf("| %-30s: %-26s |\n", "Date", treatment.getTreatmentDate());
            System.out.println("======================================================================");
            
            if (treatment.getPrescribedMedicines().isEmpty()) {
                System.out.printf("| %-60s |\n", "  No medicines prescribed");
            } else {
                System.out.printf("| %-30s %-11s %-23s |\n", "MEDICINE", "PRICE", "DESCRIPTION");
                System.out.println("======================================================================");
                
                for (Medicine medicine : treatment.getPrescribedMedicines()) {
                    System.out.printf("| %-30s RM%-9.2f %-23s |\n",
                        medicine.getName(),
                        medicine.getUnitPrice());
//                        truncateDescription(medicine.getDescription(), 18));
                }
            }
            System.out.println("======================================================================");
            System.out.printf("| %-29s: RM%-25.2f         |\n", "TOTAL PRICE", treatment.calculateTotalPrice());
            
            if (treatment.getDoctorNotes() != null && !treatment.getDoctorNotes().isEmpty()) {
                System.out.println("======================================================================");
                System.out.printf("| %-29s: %-35s |\n", "DOCTOR NOTES", truncateString(treatment.getDoctorNotes(), 25));
            }
            System.out.println("======================================================================");
        } else {
            System.out.println("Treatment not found!");
        }
    }
    
    // Helper method to truncate strings
    private String truncateString(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "";
    }
    
    private void viewPatientHistory() {
        System.out.println("\n=== View Patient Treatment History ===");
        
        String patientId = checkValidation.getValidPatientId();
        ArrayList<Treatment> treatments = management.getPatientTreatments(patientId);
        
        if (treatments == null || treatments.isEmpty()) {
            System.out.println("No treatments found for patient " + patientId);
        } else {
            System.out.println("\n Treatment History for Patient " + patientId + ":");
            System.out.println("----------------------------------------------------------------------");
            System.out.printf("%-12s %-12s %-20s %-15s %-8s\n", 
                "TreatmentID", "Date", "Diagnosis", "Medicines", "Total");
            System.out.println("----------------------------------------------------------------------");
            
            for (Treatment treatment : treatments) {
                System.out.printf("%-12s %-12s %-20s %-15d RM%-7.2f\n",
                    treatment.getTreatmentId(),
                    treatment.getTreatmentDate().toString().substring(0, 10),
                    treatment.getDiagnosis().length() > 18 ? 
                        treatment.getDiagnosis().substring(0, 15) + "" : treatment.getDiagnosis(),
                    treatment.getPrescribedMedicines().size(),
                    treatment.calculateTotalPrice());
            }
        }
    }
    
    private void calculateTotal() {
        System.out.println("\n=== Calculate Treatment Total ===");
        
        String treatmentId = checkValidation.getValidTreatmentId();
        double total = management.calculateTreatmentTotal(treatmentId);
        
        if (total > 0) {
            System.out.println(" Total price for treatment " + treatmentId + ": RM" + String.format("%.2f", total));
        } else {
            System.out.println("Treatment not found or has no medicines!");
        }
    }
    
    private void getRecommendedMedicines() {
        System.out.println("\n=== Get Recommended Medicines for Diagnosis ===");
    
        ArrayList<String> diagnoses = medicineDAO.getAllDiagnoses();
        System.out.println("Available Diagnoses:");
        for (int i = 0; i < diagnoses.size(); i++) {
            System.out.println((i + 1) + ". " + diagnoses.get(i));
        }
    
        int diagChoice = checkValidation.getValidInt("Select diagnosis number: ", 1, diagnoses.size());
        String diagnosis = diagnoses.get(diagChoice - 1);

        ArrayList<Medicine> recommended = medicineDAO.getRecommendedMedicines(diagnosis);

        if (recommended.isEmpty()) {
            System.out.println("No specific recommendations for " + diagnosis);
        } else {
            System.out.println("\n Recommended medicines for " + diagnosis + ":");
            System.out.println("================================================================");
            System.out.printf("| %-20s %-15s %-21s   |\n", "Medicine", "Price", "Description");
            System.out.println("================================================================");

            double totalCost = 0;
            for (Medicine medicine : recommended) {
                System.out.printf("| %-20s RM%-9.2f %-27s |\n",
                    medicine.getName(),
                    medicine.getUnitPrice());
//                    medicine.getDescription().length() > 25 ? 
//                        medicine.getDescription().substring(0, 22) + "" : medicine.getDescription());
                totalCost += medicine.getUnitPrice();
            }
            System.out.println("================================================================");
            System.out.printf("| %-20s RM%-9.2f %-27s |\n", "TOTAL COST", totalCost, "");
            System.out.println("================================================================");
        }
    }
    
    private void updateTreatmentNotes() {
        System.out.println("\n=== Update Treatment Notes ===");
        
        String treatmentId = checkValidation.getValidTreatmentId();
        updateTreatmentNotesForTreatment(treatmentId);
    }
    
    private void updateTreatmentNotesForTreatment(String treatmentId) {
        Treatment treatment = management.getTreatment(treatmentId);
        if (treatment == null) {
            System.out.println("Treatment not found!");
            return;
        }
        
        String notes = checkValidation.getValidNotes();
        if (management.updateTreatmentNotes(treatmentId, notes)) {
            System.out.println("Doctor notes updated successfully!");
        } else {
            System.out.println("Failed to update notes!");
        }
    }
}