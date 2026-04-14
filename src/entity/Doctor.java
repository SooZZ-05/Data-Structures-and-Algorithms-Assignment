package entity;

/**
 *
 * @author Sebastian
 */
import adt.SortedListInterface;
import adt.SortedSkipList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author User
 */

public class Doctor implements Comparable<Doctor>, Serializable{
    private static int lastId = 1;
    
    private String doctorId;
    private String name;
    private String icNo;
    private String specialisation;
    private String contactNo;
    private double consultationFee;
    private int consultationCount;
    private SortedListInterface<LocalDate> availability;
    
    public Doctor(String name, String icNo, String specialisation, String contactNo, double consultationFee){
        this.doctorId=generateDoctorId();
        this.name=name;
        this.icNo = icNo;
        this.specialisation=specialisation;
        this.contactNo = contactNo;
        this.consultationFee = consultationFee;
        this.consultationCount = 0;
        this.availability = new SortedSkipList<LocalDate>(LocalDate.MIN, LocalDate.MAX);
    }
    
    @Override
    public int compareTo(Doctor other) {
        return this.doctorId.compareTo(other.getId());
    }
    
    public String getId() {
        return doctorId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getIcNo() {
        return icNo;
    }
    
    public String getSpecialisation() {
        return specialisation;
    }
    
    public String getContactNumber() {
        return contactNo;
    }
  
    public double getConsultationFee() {
        return consultationFee;
    }
    
    public int getConsultationCount() {
        return consultationCount;
    }
    
    public SortedSkipList<LocalDate> getAvailability() {
        return (SortedSkipList<LocalDate>)availability;
    }
    
    private static synchronized String generateDoctorId() {
        return "D" + String.format("%04d", lastId++);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }
    
    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }
    
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
    
    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }
    
    public void setConsultationCount(int consultationCount) {
        this.consultationCount = consultationCount;
    }

    public void setAvailability(SortedSkipList<LocalDate> availability) {
        this.availability = availability;
    }
    
    public void incrementConsultationCount() {
        this.consultationCount++;
    }
    
    public void decrementConsultationCount() {
        this.consultationCount--;
    }
    
    public void resetConsultationCount() {
        this.consultationCount = 0;
    }
    
    @Override
    public String toString(){ 
        return doctorId + " | " + name + " | " + specialisation;
    }

    public static void saveLastId() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("last_doctor_id.dat"))) {
            oos.writeInt(lastId);
        } catch (IOException e) {
            System.out.println("Error saving last ID: " + e.getMessage());
        }
    }
    
    public static void loadLastId() {
        File file = new File("last_doctor_id.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                lastId = ois.readInt();
            } catch (IOException e) {
                System.out.println("Error loading last ID: " + e.getMessage());
            }
        }
    }
}