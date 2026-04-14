package entity;

/** Author: Teo Geok Woon */

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;
    private String patientId;
    private String name;
    private String ic;
    private String phone;
    private int age;
    private String address;
    private String notes;
    private String gender;           
    private String department;          
    private String appointmentType;  
    private boolean emergency;       
    private LocalDateTime registrationTime; 
    private LocalDate firstRegistrationDate;

    public Patient(String patientId, String name, String ic, int age, String phone, String address, 
                   String notes, String gender, String department, String appointmentType, boolean emergency) {
        this.patientId = patientId;
        this.name = name;
        this.ic = ic;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.notes = notes;
        this.gender = gender;
        this.department = department;
        this.appointmentType = appointmentType;
        this.emergency = emergency;
        this.registrationTime = LocalDateTime.now();
        this.firstRegistrationDate = this.registrationTime.toLocalDate();
    }

    public String getIc() {
        return ic;
    }
    public String getPatientId() { 
        return patientId; 
    }
    public String getName() { 
        return name; 
    }
    public String getPhone() { 
        return phone; 
    }
    public int getAge() { 
        return age; 
    }
    public String getAddress() { 
        return address; 
    }
    public String getNotes() { 
        return notes; 
    }
    public String getGender() {
        return gender; 
    }
    public String getDepartment() { 
        return department; 
    }
    public String getAppointmentType() { 
        return appointmentType; 
    }
    public boolean getEmergency() { 
        return emergency; 
    }
    public LocalDateTime getRegistrationTime() { 
        return registrationTime; 
    }
    public LocalDate getFirstRegistrationDate() {
        return firstRegistrationDate;
    }
    
    public void setFirstRegistrationDate(LocalDate d) {
        this.firstRegistrationDate = d;
    }
    public void setNotes(String notes) { 
        this.notes = notes; 
    }
    public void setGender(String gender) { 
        this.gender = gender; 
    }
    public void setAddress(String address) { 
        this.address = address; 
    }
    public void setAge(int age) { 
        this.age = age; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }
    public void setName(String name) { 
        this.name = name; 
    }
    public void setPatientId(String patientId) { 
        this.patientId = patientId; 
    }   
    public void setIc(String ic) {
        this.ic = ic;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }
    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }
    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }
 
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Patient)) return false;
        Patient p = (Patient) o;
        return this.patientId != null && this.patientId.equals(p.patientId);
    }

    @Override
    public String toString() {
        return String.format("ID:%s | Name:%s | IC:%s | Age:%d | Gender:%s | Faculty:%s | Phone:%s | Addr:%s | Type:%s | Emergency:%b | Notes:%s",
                patientId, name, ic, age, gender, department, phone, address, appointmentType, emergency, notes == null ? "" : notes);
    }
}
