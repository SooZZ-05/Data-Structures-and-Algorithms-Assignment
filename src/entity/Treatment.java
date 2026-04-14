package entity;

/**
 *
 * @author Gan Jun Wei
 */
import adt.ArrayList;

public class Treatment {

    private String treatmentId;
    private String treatmentName;
    private String patientId;
    private String diagnosis;
    private java.util.Date treatmentDate;
    private String doctorNotes;
    private ArrayList<Medicine> prescribedMedicines;
    public String ConsultationId;
    private Consultation consultation;

    public Treatment(String treatmentId, String patientId, String diagnosis) {
        this.treatmentId = treatmentId;
        this.patientId = patientId;
        this.diagnosis = diagnosis;
        this.treatmentDate = new java.util.Date();
        this.doctorNotes = "";
        this.prescribedMedicines = new ArrayList<>();
    }
    
    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public String getConsultationId() {
        return consultation != null ? consultation.getId() : null;
    }

    public String getConsultationPatientName() {
        return (consultation != null && consultation.getPatient() != null)
                ? consultation.getPatient().getName() : null;
    }

    public String getConsultationDoctorName() {
        return (consultation != null && consultation.getDoctor() != null)
                ? consultation.getDoctor().getName() : null;
    }

    // Getters and setters
    public String getTreatmentId() {
        return treatmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public java.util.Date getTreatmentDate() {
        return treatmentDate;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public ArrayList<Medicine> getPrescribedMedicines() {
        return prescribedMedicines;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public void addMedicine(Medicine medicine) {
        prescribedMedicines.add(medicine);
    }

    public double calculateTotalPrice() {
        double total = 0;
        for (Medicine medicine : prescribedMedicines) {
            total += medicine.getUnitPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Treatment ID: " + treatmentId + ", Patient: " + patientId
                + ", Diagnosis: " + diagnosis + ", Date: " + treatmentDate
                + ", Total: $" + calculateTotalPrice();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Treatment treatment = (Treatment) obj;
        return treatmentId.equals(treatment.treatmentId);
    }

    @Override
    public int hashCode() {
        return treatmentId.hashCode();
    }

    public double getTreatmentFee() {
        // If you only charge medicine cost:
        return calculateTotalPrice();

        // OR if you also want a fixed consultation/treatment fee:
        // return calculateTotalPrice() + baseFee;
    }

    public String getTreatmentName() {
        return treatmentName;
    }
}
