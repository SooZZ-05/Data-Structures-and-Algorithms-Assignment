package dao;

import adt.ArrayList;
import adt.HashMap;
import entity.Medicine;

public class MedicineDAO {
    private static MedicineDAO instance;
    private ArrayList<Medicine> medicines;
    private HashMap<String, ArrayList<String>> diagnosisMedicineMap;
    
    private MedicineDAO() {
        medicines = new ArrayList<>();
        diagnosisMedicineMap = new HashMap<>();
//        initializeMedicines();
        initializeDiagnosisMedicineMap();
    }
    
    public static MedicineDAO getInstance() {
        if (instance == null) {
            instance = new MedicineDAO();
        }
        return instance;
    }
//    
//    private void initializeMedicines() {
//        medicines.add(new Medicine("M001", "Antibiotic", 25.00, "For bacterial infections"));
//        medicines.add(new Medicine("M002", "Paracetamol", 8.50, "Pain and fever relief"));
//        medicines.add(new Medicine("M003", "Ibuprofen", 12.00, "Anti-inflammatory pain relief"));
//        medicines.add(new Medicine("M004", "Aspirin", 7.00, "Pain relief and blood thinner"));
//        medicines.add(new Medicine("M005", "Antihistamine", 15.00, "For allergies and itching"));
//        medicines.add(new Medicine("M006", "Cough Syrup", 18.00, "Relieves cough symptoms"));
//        medicines.add(new Medicine("M007", "Decongestant", 10.50, "Clears nasal congestion"));
//        medicines.add(new Medicine("M008", "Antacid", 9.00, "Relieves heartburn and indigestion"));
//        medicines.add(new Medicine("M009", "Antidiarrheal", 11.00, "Controls diarrhea"));
//        medicines.add(new Medicine("M010", "Lozenges", 6.50, "Sore throat relief"));
//        medicines.add(new Medicine("M011", "Insulin", 45.00, "Diabetes management"));
//        medicines.add(new Medicine("M012", "Blood Pressure Med", 32.00, "Hypertension treatment"));
//        medicines.add(new Medicine("M013", "Inhaler", 28.00, "Asthma relief"));
//        medicines.add(new Medicine("M014", "Antifungal Cream", 14.00, "Treats fungal infections"));
//        medicines.add(new Medicine("M015", "Muscle Relaxant", 20.00, "Relieves muscle pain"));
//    }
    
    private void initializeDiagnosisMedicineMap() {
        // Fever-related diagnoses
        ArrayList<String> feverMeds = new ArrayList<>();
        feverMeds.add("M002"); feverMeds.add("M003");
        diagnosisMedicineMap.put("Fever", feverMeds);
        
        // Common Cold
        ArrayList<String> coldMeds = new ArrayList<>();
        coldMeds.add("M002"); coldMeds.add("M006"); coldMeds.add("M007"); coldMeds.add("M010");
        diagnosisMedicineMap.put("Common Cold", coldMeds);
        
        // Flu
        ArrayList<String> fluMeds = new ArrayList<>();
        fluMeds.add("M002"); fluMeds.add("M006"); fluMeds.add("M007");
        diagnosisMedicineMap.put("Flu", fluMeds);
        
        // Headache
        ArrayList<String> headacheMeds = new ArrayList<>();
        headacheMeds.add("M002"); headacheMeds.add("M003"); headacheMeds.add("M004");
        diagnosisMedicineMap.put("Headache", headacheMeds);
        
        // Migraine
        ArrayList<String> migraineMeds = new ArrayList<>();
        migraineMeds.add("M003"); migraineMeds.add("M004");
        diagnosisMedicineMap.put("Migraine", migraineMeds);
        
        // Back Pain
        ArrayList<String> backPainMeds = new ArrayList<>();
        backPainMeds.add("M003"); backPainMeds.add("M015");
        diagnosisMedicineMap.put("Back Pain", backPainMeds);
        
        // Muscle Pain
        ArrayList<String> musclePainMeds = new ArrayList<>();
        musclePainMeds.add("M003"); musclePainMeds.add("M015");
        diagnosisMedicineMap.put("Muscle Pain", musclePainMeds);
        
        // Joint Pain
        ArrayList<String> jointPainMeds = new ArrayList<>();
        jointPainMeds.add("M003"); jointPainMeds.add("M015");
        diagnosisMedicineMap.put("Joint Pain", jointPainMeds);
        
        // Cough
        ArrayList<String> coughMeds = new ArrayList<>();
        coughMeds.add("M006"); coughMeds.add("M010");
        diagnosisMedicineMap.put("Cough", coughMeds);
        
        // Sore Throat
        ArrayList<String> soreThroatMeds = new ArrayList<>();
        soreThroatMeds.add("M010"); soreThroatMeds.add("M002");
        diagnosisMedicineMap.put("Sore Throat", soreThroatMeds);
        
        // Asthma
        ArrayList<String> asthmaMeds = new ArrayList<>();
        asthmaMeds.add("M013");
        diagnosisMedicineMap.put("Asthma", asthmaMeds);
        
        // Allergy
        ArrayList<String> allergyMeds = new ArrayList<>();
        allergyMeds.add("M005");
        diagnosisMedicineMap.put("Allergy", allergyMeds);
        
        // Skin Rash
        ArrayList<String> skinRashMeds = new ArrayList<>();
        skinRashMeds.add("M005"); skinRashMeds.add("M014");
        diagnosisMedicineMap.put("Skin Rash", skinRashMeds);
        
        // Stomach Pain
        ArrayList<String> stomachPainMeds = new ArrayList<>();
        stomachPainMeds.add("M008"); stomachPainMeds.add("M009");
        diagnosisMedicineMap.put("Stomach Pain", stomachPainMeds);
        
        // Heartburn
        ArrayList<String> heartburnMeds = new ArrayList<>();
        heartburnMeds.add("M008");
        diagnosisMedicineMap.put("Heartburn", heartburnMeds);
        
        // Diarrhea
        ArrayList<String> diarrheaMeds = new ArrayList<>();
        diarrheaMeds.add("M009");
        diagnosisMedicineMap.put("Diarrhea", diarrheaMeds);
        
        // Hypertension
        ArrayList<String> hypertensionMeds = new ArrayList<>();
        hypertensionMeds.add("M012");
        diagnosisMedicineMap.put("Hypertension", hypertensionMeds);
        
        // Diabetes
        ArrayList<String> diabetesMeds = new ArrayList<>();
        diabetesMeds.add("M011");
        diagnosisMedicineMap.put("Diabetes", diabetesMeds);
        
        // Bacterial Infection
        ArrayList<String> bacterialInfectionMeds = new ArrayList<>();
        bacterialInfectionMeds.add("M001");
        diagnosisMedicineMap.put("Bacterial Infection", bacterialInfectionMeds);
        
        // Fungal Infection
        ArrayList<String> fungalInfectionMeds = new ArrayList<>();
        fungalInfectionMeds.add("M014");
        diagnosisMedicineMap.put("Fungal Infection", fungalInfectionMeds);
    }
    
    public ArrayList<Medicine> getRecommendedMedicines(String diagnosis) {
        ArrayList<Medicine> recommended = new ArrayList<>();
        ArrayList<String> medicineIds = diagnosisMedicineMap.get(diagnosis);
        
        if (medicineIds != null) {
            for (String medicineId : medicineIds) {
                Medicine medicine = getMedicineById(medicineId);
                if (medicine != null) {
                    recommended.add(medicine);
                }
            }
        }
        return recommended;
    }
    
    public Medicine getMedicineById(String medicineId) {
        for (Medicine medicine : medicines) {
            if (medicine.getId().equals(medicineId)) {
                return medicine;
            }
        }
        return null;
    }
    
    public ArrayList<Medicine> getAllMedicines() {
        return new ArrayList<>(medicines);
    }
    
    public ArrayList<String> getAllDiagnoses() {
        ArrayList<String> diagnoses = new ArrayList<>();
        diagnoses.add("Fever");
        diagnoses.add("Common Cold");
        diagnoses.add("Flu");
        diagnoses.add("Headache");
        diagnoses.add("Migraine");
        diagnoses.add("Back Pain");
        diagnoses.add("Muscle Pain");
        diagnoses.add("Joint Pain");
        diagnoses.add("Cough");
        diagnoses.add("Sore Throat");
        diagnoses.add("Asthma");
        diagnoses.add("Allergy");
        diagnoses.add("Skin Rash");
        diagnoses.add("Stomach Pain");
        diagnoses.add("Heartburn");
        diagnoses.add("Diarrhea");
        diagnoses.add("Hypertension");
        diagnoses.add("Diabetes");
        diagnoses.add("Bacterial Infection");
        diagnoses.add("Fungal Infection");
        return diagnoses;
    }
}