package entity;

/**
 *
 * @author Low Zi Qing
 */
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Consultation implements Comparable<Consultation>, Serializable {

    private static final long serialVersionUID = 1L;
    private static int counter = 1;
    private final String id;
    private Patient patient;
    private Doctor doctor;
    private String diagnosis;
    private LocalDateTime dateTime;
    private int durationMinutes;
    private boolean followUpRequired;
    private List<Treatment> treatments;
    private List<Medicine> medicines;

    public Consultation(Patient patient, Doctor doctor, String diagnosis,
            LocalDateTime dateTime, boolean followUpRequired,
            List<Treatment> treatments, List<Medicine> medicines) {
        this.id = "C" + (counter++);
        this.patient = patient;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
        this.dateTime = dateTime;
        this.followUpRequired = followUpRequired;
        this.treatments = treatments;
        this.medicines = medicines;
    }

    public Consultation(String id, Patient patient, Doctor doctor, String diagnosis, LocalDateTime dateTime, int durationMinutes, boolean followUpRequired, List<Treatment> treatments, List<Medicine> medicines) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
        this.dateTime = dateTime;
        this.durationMinutes = durationMinutes;
        this.followUpRequired = followUpRequired;
        this.treatments = treatments;
        this.medicines = medicines;
    }

    public Consultation(Patient patient, Doctor doctor, String diagnosis,
            LocalDateTime dateTime, boolean followUpRequired) {
        this(patient, doctor, diagnosis, dateTime, followUpRequired,
                new java.util.ArrayList<>(), new java.util.ArrayList<>());
    }

    public Consultation(Patient patient, Doctor doctor, String diagnosis) {
        this(patient, doctor, diagnosis, LocalDateTime.now(), false,
                new java.util.ArrayList<>(), new java.util.ArrayList<>());
    }

    public Consultation(String id, Patient patient, Doctor doctor, String diagnosis, LocalDateTime dateTime, boolean followUpRequired) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
        this.dateTime = dateTime;
        this.followUpRequired = followUpRequired;
    }

    public static Consultation createMinConsultation() {
        Patient minPatient = new Patient("MIN", "MIN", "MIN", 0, "MIN", "MIN", "MIN", "MIN", "MIN", "MIN", false);
        Doctor minDoctor = new Doctor("MIN", "MIN", "MIN", "MIN", 0.0);
        return new Consultation(minPatient, minDoctor, "MIN", LocalDateTime.MIN, false);
    }

    public static Consultation createMaxConsultation() {
        Patient maxPatient = new Patient("MAX", "MAX", "MAX", Integer.MAX_VALUE, "MAX", "MAX", "MAX", "MAX", "MAX", "MAX", true);
        Doctor maxDoctor = new Doctor("MAX", "MAX", "MAX", "MAX", Double.MAX_VALUE);
        return new Consultation(maxPatient, maxDoctor, "MAX", LocalDateTime.MAX, true);
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void setDoctor(Doctor doctor) {
        if (doctor != null) {
            this.doctor = doctor;
        } else {
            System.out.println("Doctor cannot be null.");
        }
    }

    public void setPatient(Patient patient) {
        if (patient != null) {
            this.patient = patient;
        } else {
            System.out.println("Patient cannot be null.");
        }
    }

    public boolean isFollowUpRequired() {
        return followUpRequired;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public static void setCounter(int c) {
        counter = c;
    }

    public void setDiagnosis(String diagnosis) {
        if (diagnosis != null && !diagnosis.trim().isEmpty()) {
            this.diagnosis = diagnosis;
        } else {
            System.out.println("Diagnosis cannot be empty.");
        }
    }

    public void setFollowUpRequired(boolean followUpRequired) {
        this.followUpRequired = followUpRequired;
    }

    public double calculateTotalFee() {
        double total = 0;
        if (doctor != null) {
            total += doctor.getConsultationFee();
        }
        if (treatments != null) {
            for (Treatment t : treatments) {
                total += t.getTreatmentFee();
            }
        }
        if (medicines != null) {
            for (Medicine m : medicines) {
                total += m.getUnitPrice();
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return "Consultation{"
                + "id='" + id + '\''
                + ", patient=" + patient.getName()
                + ", doctor=" + doctor.getName()
                + ", diagnosis='" + diagnosis + '\''
                + ", dateTime=" + dateTime
                + ", followUpRequired=" + followUpRequired
                + '}';
    }

    @Override
    public int compareTo(Consultation other) {
        return this.dateTime.compareTo(other.dateTime);
    }

}
