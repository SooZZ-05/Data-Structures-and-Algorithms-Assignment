package dao;

import adt.ArrayList;
import entity.Treatment;

public class TreatmentDAO {
    private static TreatmentDAO instance;
    private ArrayList<Treatment> treatments;
    private int treatmentCounter = 1;
    
    private TreatmentDAO() {
        treatments = new ArrayList<>();
    }
    
    public static TreatmentDAO getInstance() {
        if (instance == null) {
            instance = new TreatmentDAO();
        }
        return instance;
    }
    
    // FIXED: Ensure TRT prefix to match validation pattern
    public String generateTreatmentId() {
        return "TRT" + String.format("%03d", treatmentCounter++);
    }
    
    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }
    
    public ArrayList<Treatment> getAllTreatments() {
        return new ArrayList<>(treatments);
    }
    
    public ArrayList<Treatment> getTreatmentsByPatientId(String patientId) {
        ArrayList<Treatment> result = new ArrayList<>();
        for (Treatment treatment : treatments) {
            if (treatment.getPatientId().equals(patientId)) {
                result.add(treatment);
            }
        }
        return result;
    }
    
    public Treatment getTreatmentById(String treatmentId) {
        for (Treatment treatment : treatments) {
            if (treatment.getTreatmentId().equals(treatmentId)) {
                return treatment;
            }
        }
        return null;
    }
    
    public boolean updateTreatment(Treatment updatedTreatment) {
        for (int i = 0; i < treatments.size(); i++) {
            if (treatments.get(i).getTreatmentId().equals(updatedTreatment.getTreatmentId())) {
                treatments.set(i, updatedTreatment);
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteTreatment(String treatmentId) {
        for (int i = 0; i < treatments.size(); i++) {
            if (treatments.get(i).getTreatmentId().equals(treatmentId)) {
                treatments.remove(i);
                return true;
            }
        }
        return false;
    }
}