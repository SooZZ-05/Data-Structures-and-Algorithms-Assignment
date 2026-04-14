package control;

import adt.ArrayList;
import dao.MedicineDAO;
import dao.TreatmentDAO;
import entity.Medicine;
import entity.Treatment;
import java.util.regex.Pattern;

public class MedicalTreatmentManagement {
    private final TreatmentDAO treatmentDAO;
    private final MedicineDAO medicineDAO;
    
    public MedicalTreatmentManagement() {
        treatmentDAO = TreatmentDAO.getInstance();
        medicineDAO = MedicineDAO.getInstance();
    }
    
    public boolean isValidPatientId(String patientId) {
        return patientId != null && Pattern.matches("^P\\d{4}$", patientId);
    }
    
    // FIXED: Updated pattern to match TRT prefix
    public boolean isValidTreatmentId(String treatmentId) {
        return treatmentId != null && Pattern.matches("^TRT\\d+$", treatmentId);
    }
    
    public boolean isValidMedicineId(String medicineId) {
        return medicineId != null && Pattern.matches("^M\\d{3}$", medicineId);
    }
    
    public boolean isValidDiagnosis(String diagnosis) {
        return diagnosis != null && Pattern.matches("^[a-zA-Z\\s\\-]{2,100}$", diagnosis);
    }
    
    public Treatment createTreatment(String patientId, String diagnosis) {
        if (!isValidPatientId(patientId) || !isValidDiagnosis(diagnosis)) {
            System.out.println("Invalid patient ID or diagnosis!");
            return null;
        }
        
        String treatmentId = treatmentDAO.generateTreatmentId();
        Treatment treatment = new Treatment(treatmentId, patientId, diagnosis);
        treatmentDAO.addTreatment(treatment);
        return treatment;
    }
    
    public boolean addMedicineToTreatment(String treatmentId, String medicineId) {
        System.out.println("Adding medicine " + medicineId + " to treatment " + treatmentId);
        
        if (!isValidTreatmentId(treatmentId)) {
            System.out.println("Invalid treatment ID: " + treatmentId);
            return false;
        }
        
        if (!isValidMedicineId(medicineId)) {
            System.out.println("Invalid medicine ID: " + medicineId);
            return false;
        }
        
        Treatment treatment = treatmentDAO.getTreatmentById(treatmentId);
        Medicine medicine = medicineDAO.getMedicineById(medicineId);
        
        if (treatment == null) {
            System.out.println("Treatment not found: " + treatmentId);
            return false;
        }
        
        if (medicine == null) {
            System.out.println("Medicine not found: " + medicineId);
            return false;
        }
        
        treatment.addMedicine(medicine);
        return treatmentDAO.updateTreatment(treatment);
    }
    
    public Treatment getTreatment(String treatmentId) {
        return treatmentDAO.getTreatmentById(treatmentId);
    }
    
    public ArrayList<Treatment> getPatientTreatments(String patientId) {
        return treatmentDAO.getTreatmentsByPatientId(patientId);
    }
    
    public ArrayList<Treatment> getAllTreatments() {
        return treatmentDAO.getAllTreatments();
    }
    
    public double calculateTreatmentTotal(String treatmentId) {
        Treatment treatment = treatmentDAO.getTreatmentById(treatmentId);
        return treatment != null ? treatment.calculateTotalPrice() : 0;
    }
    
    public ArrayList<Medicine> getRecommendedMedicines(String diagnosis) {
        return medicineDAO.getRecommendedMedicines(diagnosis);
    }
    
    public ArrayList<Medicine> getAllMedicines() {
        return medicineDAO.getAllMedicines();
    }
    
    public boolean updateTreatmentNotes(String treatmentId, String notes) {
        Treatment treatment = treatmentDAO.getTreatmentById(treatmentId);
        if (treatment != null) {
            treatment.setDoctorNotes(notes);
            return treatmentDAO.updateTreatment(treatment);
        }
        return false;
    }

    public ArrayList<String> getAllDiagnoses() {
        return medicineDAO.getAllDiagnoses();
    }
    
    public void displayMedicinePrices() {
        ArrayList<Medicine> medicines = medicineDAO.getAllMedicines();
        if (medicines.isEmpty()) {
            System.out.println("No medicines available!");
            return;
        }

        System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         ALL MEDICINE PRICES                         ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-8s %-20s %-10s %-30s ║\n", "ID", "Medicine Name", "Price", "Description");
        System.out.println("╠══════════════════════════════════════════════════════════════════════╣");

        for (Medicine medicine : medicines) {
            System.out.printf("║ %-8s %-20s RM%-9.2f %-30s ║\n",
                medicine.getId(),
                medicine.getName(),
                medicine.getUnitPrice());
//                medicine.getDescription().length() > 30 ? 
//                    medicine.getDescription().substring(0, 27) + "..." : medicine.getDescription()
        }
        
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
    }
}