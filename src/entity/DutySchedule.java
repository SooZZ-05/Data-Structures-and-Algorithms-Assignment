package entity;

/**
 *
 * @author Sebastian
 */
import adt.SortedSkipList;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Objects;

public class DutySchedule implements Comparable<DutySchedule>, Serializable {
    private String doctorId;
    private LocalDate date;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime dateTime; // Combined date + time for sorting
    private SortedSkipList<Consultation> consultationList;

    public DutySchedule(String doctorId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.doctorId = doctorId;
        this.date = date;
        this.day = date.getDayOfWeek();
        this.startTime = startTime;
        this.endTime = endTime;
        this.dateTime = LocalDateTime.of(date, startTime);
        Consultation minConsultation = createMinConsultation();
        Consultation maxConsultation = createMaxConsultation();
        this.consultationList = new SortedSkipList<>(minConsultation, maxConsultation);
    }

    @Override
//    public int compareTo(DutySchedule other) {
//        return this.dateTime.compareTo(other.dateTime);
//    }
    public int compareTo(DutySchedule other) {
        // First compare by date
        int dateCompare = this.date.compareTo(other.date);
        if (dateCompare != 0) {
            return dateCompare;
        }
        
        // If same date, compare by start time
        int timeCompare = this.startTime.compareTo(other.startTime);
        if (timeCompare != 0) {
            return timeCompare;
        }
        
        // If same start time, compare by doctor ID
        return this.doctorId.compareTo(other.doctorId);
    }
    
     @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DutySchedule that = (DutySchedule) obj;
        return Objects.equals(doctorId, that.doctorId) &&
               Objects.equals(date, that.date) &&
               Objects.equals(startTime, that.startTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(doctorId, date, startTime);
    }
    
    public String getDoctorId() {
        return doctorId;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public SortedSkipList<Consultation> getConsultationList() {
        return consultationList;
    }
    
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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
}