package control;

/**
 *
 * @author Sebastian
 */
import adt.*; 

import adt.SortedSkipList;
import entity.Doctor;
import entity.DutySchedule;
import boundary.DoctorPanel;
import dao.DoctorDAO;
import dao.DutyScheduleDAO;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.LocalDate;

public class MaintainDoctor {
    DutySchedule maxDuty = createMaxDuty();
    DutySchedule minDuty = createMinDuty();
    private ListInterface<Doctor> doctorList = new LinkedList<>();
    private DoctorDAO doctorDAO = new DoctorDAO();
    private DoctorPanel doctorPanel = new DoctorPanel();
    private SortedListInterface<DutySchedule> dutyScheduleList = new SortedSkipList<>(minDuty, maxDuty);
    private DutyScheduleDAO dutyScheduleDAO = new DutyScheduleDAO();

    public MaintainDoctor() {
        Doctor.loadLastId();
        doctorList = doctorDAO.retrieveFromFile();
        dutyScheduleList = dutyScheduleDAO.retrieveFromFile();
    }
    
    public void runDoctorMaintenance() {
        String choice = "0";
        do {
            choice = doctorPanel.getMainMenuChoice();
            switch (choice) {
                case "1": //manage doctor information
                    runDoctorInfoManagement();
                    break;
                case "2": //manage duty schedule
                    dutyScheduleMenu();
                    break;
                case "3": //manage availability
                    runAvailabilityManagement();
                    break;
                case "4": //generate report
                    runReports();
                    break;
                case "0": //exit from the program
                    System.out.println("Exiting Doctor Management System.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (!choice.equals("0"));
    }
    
    public void runDoctorInfoManagement() {
        String choice = "0";
        do {
            choice = doctorPanel.getManageDoctorChoice();
            switch (choice) {
                case "1": //add a new doctor to the system
                    boolean added = addNewDoctor();
                    if (added) {
                        System.out.println("Doctor added successfully.");
                    } else {
                        System.out.println("Error: Clashing IC number.");
                    }
                    break;
                case "2": //list all the doctors in the system
                    doctorPanel.viewAllDoctors(doctorList);
                    break;
                case "3": //update the details of a doctor in the system
                    doctorPanel.viewAllDoctors(doctorList);
                    boolean updated = updateDoctor();
                    if (updated) {
                        System.out.println("Doctor updated successfully.");
                    }
                    break;
                case "4": //remove the details of a doctor in the system
                    doctorPanel.viewAllDoctors(doctorList);
                    boolean removed = removeADoctor();
                    if (removed) {
                        System.out.println("Doctor removed successfully.");
                    } else {
                        System.out.println("Error: Doctor does not exists.");
                    }
                    break;
                case "0": //return to main menu
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (!choice.equals("0"));
    }
    
    public void dutyScheduleMenu() {
        String choice = "0";
        do {
            choice = doctorPanel.getDutyScheduleMenuChoice();
            switch (choice) {
                case "1": //assign duty schedule
                    doctorPanel.viewAllDoctors(doctorList);
                    boolean assigned = assignDutySchedule();
                    if (assigned) {
                        System.out.println("Duty added to schedule successfully.");
                    }
                    break;
                case "2": //view duty schedule by doctor
                    doctorPanel.viewAllDoctors(doctorList);
                    String doctorId = doctorPanel.inputDoctorId();
                    SortedSkipList<DutySchedule> dutyList = getDutySchedulesByDoctor(doctorId);
                    doctorPanel.viewDutySchedulesByDoctor(dutyList);
                    break;
                case "3": //view duty schedule by date
                    LocalDate date = doctorPanel.inputDate();
                    SortedSkipList<DutySchedule> dutyListDate = getDutySchedulesByDate(date);
                    doctorPanel.viewDutySchedulesByDate(dutyListDate, doctorList);
                    break;
                case "4": //remove duty schedule
                    boolean removed = removeDutySchedule();
                    if (removed) {
                        System.out.println("Duty removed successfully.");
                    }
                    break;
                case "0": //back to main menu
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (!choice.equals("0"));
    }
    
    public void runAvailabilityManagement() {
        String availabilityChoice = "0";
        do {
            availabilityChoice = doctorPanel.getAvailabilityMenuChoice();
            switch (availabilityChoice) {
                case "1": //add date
                    doctorPanel.viewAllDoctors(doctorList);
                    String doctorId = doctorPanel.getDoctorToUpdate();
                    Doctor doctorToUpdate = getDoctorById(doctorId);
                    if (doctorToUpdate != null) {
                        System.out.println("Available dates for " + doctorToUpdate.getName());
                        doctorPanel.displayFormattedAvailability(doctorToUpdate.getAvailability(),(SortedSkipList<DutySchedule>)dutyScheduleList, doctorId);
                        LocalDate newDate = doctorPanel.inputNewAvailability();
                        SortedSkipList<LocalDate> tempList = doctorToUpdate.getAvailability();
                        boolean added = tempList.add(newDate);
                        if (added) {
                            doctorToUpdate.setAvailability(tempList);
                            doctorDAO.saveToFile(doctorList);
                        } else {
                            System.out.println("Date already exists.");
                        }
                    } else {
                        System.out.println("Doctor not found with ID: " + doctorId);  // ERROR MESSAGE
                    }
                    break;
                case "2": //view available dates
                    doctorId = doctorPanel.getDoctorToUpdate();
                    doctorToUpdate = getDoctorById(doctorId);
                    if (doctorToUpdate != null) {
                        System.out.println("Available dates for " + doctorToUpdate.getName());
                        doctorPanel.displayFormattedAvailability(doctorToUpdate.getAvailability(),(SortedSkipList<DutySchedule>)dutyScheduleList, doctorId);
                    } else {
                        System.out.println("Doctor not found with ID: " + doctorId);  // ERROR MESSAGE
                    }
                    break;
                case "3": //remove date
                    doctorId = doctorPanel.getDoctorToUpdate();
                    doctorToUpdate = getDoctorById(doctorId);
                    if (doctorToUpdate != null) {  // ADD NULL CHECK
                        LocalDate aDate = doctorPanel.inputAvailabilityToRemove();
                        SortedSkipList<LocalDate> tempRemoveList = doctorToUpdate.getAvailability();
                        boolean exists = tempRemoveList.contains(aDate);
                        if (exists) {
                            tempRemoveList.remove(aDate);
                            doctorToUpdate.setAvailability(tempRemoveList);
                            doctorDAO.saveToFile(doctorList);
                        } else {
                            System.out.println("Date does not exists.");
                        }
                    } else {
                        System.out.println("Doctor not found with ID: " + doctorId);  // ERROR MESSAGE
                    }
                    break;
                case "0": // return to main menu
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (!availabilityChoice.equals("0"));
    }
    
    public void runReports() {
        String choice = "0";
        do {
            choice = doctorPanel.getGenerateReportMenuChoice();
            switch (choice) {
                case "1": //Schedule coverage report
                    generateScheduleCoverageReport();
                    break;
                case "2": //Weekly roster table
                    generateWeeklyRosterTable();
                    break;
                case "0": //return to main menu
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (!choice.equals("0"));
    }
    
    // ==================== DOCTOR MANAGEMENT ====================
    
    public boolean addNewDoctor() {
        Doctor newDoctor = doctorPanel.inputDoctorDetails();
        String newDoctorIc = newDoctor.getIcNo();
        if (getDoctorByIc(newDoctorIc) == null) {//no existing doctor
            doctorList.add(newDoctor);
            doctorDAO.saveToFile(doctorList);
            Doctor.saveLastId();
            return true;
        } else {
            return false;
        }
    }
    
    public boolean removeADoctor() {
        String doctorId = doctorPanel.inputDoctorIdToRemove();
        Doctor doctorToRemove = getDoctorById(doctorId);
        if (doctorToRemove != null) {
            doctorList.remove(doctorToRemove);
            doctorDAO.saveToFile(doctorList);
            removeDutySchedule(doctorId);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean updateDoctor() {
        String doctorId = doctorPanel.getDoctorToUpdate();
        Doctor doctorToUpdate = getDoctorById(doctorId);
        if (doctorToUpdate != null) {
            String choice = "0";
            do {
                choice = doctorPanel.getUpdateChoice();
                switch (choice) {
                    case "1": //name
                        doctorPanel.changeDoctorName(doctorToUpdate);
                        doctorDAO.saveToFile(doctorList);
                        break;
                    case "2": //ic
                        String newIc = doctorPanel.changeDoctorIc(doctorToUpdate);
                        if (getDoctorByIc(newIc) == null) {
                            doctorToUpdate.setIcNo(newIc);
                            doctorDAO.saveToFile(doctorList);
                        } else {
                            System.out.println("Error: Clashing IC no.");
                        }
                        break;
                    case "3": //specialisation
                        doctorPanel.changeDoctorSpecialisation(doctorToUpdate);
                        doctorDAO.saveToFile(doctorList);
                        break;
                    case "4": //contactNo
                        doctorPanel.changeDoctorContactNo(doctorToUpdate);
                        doctorDAO.saveToFile(doctorList);
                        break;
                    case "5": //constultationFee
                        doctorPanel.changeDoctorConsultationFee(doctorToUpdate);
                        doctorDAO.saveToFile(doctorList);
                        break;
                    case "0": //return to main menu
                        System.out.println("Returning to main menu");
                        return false;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } while (!choice.equals("0"));
            return true;
        } else {
            System.out.println("Doctor ID does not exists.");
            return false;
        }
    }

    public Doctor getDoctorById(String doctorId) {
        boolean found = false;
        int index = 0;
        Doctor doctor = null;
        while (!found && index < doctorList.size()) {
            doctor = doctorList.get(index);
            if (doctor.getId().compareTo(doctorId) == 0) {
                found = true;
            }
            index++;
        }
        if (found) {
            return doctor;
        } else {
            return null;
        }
    }
    
    public Doctor getDoctorByIc(String ic) {
        boolean found = false;
        int index = 0;
        Doctor doctor = null;
        while (!found && index < doctorList.size()) {
            doctor = doctorList.get(index);
            if (doctor.getIcNo().compareTo(ic) == 0) {
                found = true;
            }
            index++;
        }
        if (found) {
            return doctor;
        } else {
            return null;
        }
    }
    
    public LinkedList<Doctor> findDoctorsBySpecialization(String specialization) {
        LinkedList<Doctor> listBySpecialisation = new LinkedList<>();
        for (int i = 0; i < doctorList.size(); i++) {
            Doctor doctor = doctorList.get(i);
            if (doctor.getSpecialisation().compareTo(specialization) == 0) {
                listBySpecialisation.add(doctor);
            }
        }
        return listBySpecialisation;
    }

    
    // ==================== DUTY SCHEDULE MANAGEMENT ====================
    
    public boolean assignDutySchedule() {
        String doctorId = doctorPanel.inputDoctorId();
        Doctor doctor = getDoctorById(doctorId);
        if (doctor == null) {
            System.out.println("Doctor not found with ID: " + doctorId);
            return false;
        }
        System.out.println("Available dates for " + doctor.getName());
        doctorPanel.displayFormattedAvailability(doctor.getAvailability(),(SortedSkipList<DutySchedule>)dutyScheduleList, doctorId);
        LocalDate date = doctorPanel.inputDate();
        LocalTime startTime = doctorPanel.inputStartTime();
        LocalTime endTime = doctorPanel.inputEndTime(startTime);
        if (hasDutyScheduleConflict(date, startTime, endTime)) {
            System.out.println("Scheduling conflict detected for doctor " + doctorId);
            return false;
        }
        if (!doctorIsAvailable(doctor, date)) {
            System.out.println("Doctor is not available on this day.");
            return false;
        }
        DutySchedule newDuty = new DutySchedule(doctorId, date, startTime, endTime);
        dutyScheduleList.add(newDuty);
        dutyScheduleDAO.saveToFile(dutyScheduleList);
        return true;
    }

    public boolean removeDutySchedule() {
        LocalDate date = doctorPanel.inputDate();
        LocalTime startTime = doctorPanel.inputStartTime();

        // Create a probe with the same doctor ID pattern that might exist
        DutySchedule probe = new DutySchedule("", date, startTime, startTime.plusHours(1));

        // Use iterator to find the exact duty schedule
        try {
            boolean found = false;
            DutySchedule toRemove = null;

            // Iterate through all duty schedules to find the exact match
            for (DutySchedule duty : (Iterable<DutySchedule>) dutyScheduleList) {

                if (duty.getDate().equals(date) && duty.getStartTime().equals(startTime)) {
                    toRemove = duty;
                    found = true;
                    break;
                }
            }

            if (found && toRemove != null) {
                DutySchedule removed = dutyScheduleList.remove(toRemove);
                if (removed != null) {
                    dutyScheduleDAO.saveToFile(dutyScheduleList);
                    System.out.println("Duty schedule removed successfully.");
                    return true;
                } else {
                    System.out.println("Failed to remove duty schedule.");
                }
            } else {
                System.out.println("Duty schedule not found for " + date + " at " + startTime);
            }
        } catch (Exception e) {
            System.out.println("Error removing duty schedule: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
    
    public void removeDutySchedule(String doctorId) {
        for (DutySchedule duty: (Iterable<DutySchedule>)dutyScheduleList) {
            if (duty.getDoctorId().equals(doctorId)) {
                dutyScheduleList.remove(duty);
                dutyScheduleDAO.saveToFile(dutyScheduleList);
            }
        }
    }

    public SortedSkipList<DutySchedule> getDutySchedulesByDoctor(String doctorId) {
        SortedSkipList<DutySchedule> result = new SortedSkipList<>(createMinDuty(), createMaxDuty());

        try {
            for (DutySchedule duty : (Iterable<DutySchedule>) dutyScheduleList) {
                if (duty.getDoctorId().equals(doctorId)) {
                    result.add(duty);
                }
            }
        } catch (ClassCastException e) {
            System.out.println("Error: Duty schedule list contains wrong object types");
            System.out.println("Please delete duty_schedules.dat and restart the program");
            return new SortedSkipList<>(createMinDuty(), createMaxDuty());
        }

        return result;
    }
    
    public SortedSkipList<DutySchedule> getDutySchedulesByDate(LocalDate date) {
        //LinkedList<DutySchedule> allDuties = getAllDutySchedules();
        SortedSkipList<DutySchedule> result = new SortedSkipList<>(
                new DutySchedule("MIN", LocalDate.MIN, LocalTime.MIN, LocalTime.MIN),
                new DutySchedule("MAX", LocalDate.MAX, LocalTime.MAX, LocalTime.MAX));
        
        try {
            for (DutySchedule duty : (Iterable<DutySchedule>) dutyScheduleList) {
                if (duty.getDate().equals(date)) {
                    result.add(duty);
                }
            }
        } catch (ClassCastException e) {
            System.out.println("Error: Duty schedule list contains wrong object types");
            System.out.println("Please delete duty_schedules.dat and restart the program");
            return new SortedSkipList<>(minDuty, maxDuty);
        }
        
        return result;
    }

    private boolean doctorIsAvailable(Doctor doctor, LocalDate date) {
        SortedSkipList<LocalDate> availabilityList = doctor.getAvailability();
        return (availabilityList.contains(date));
    }
    
    
    // ==================== REPORT GENERATION ====================

    public void generateScheduleCoverageReport() {
        doctorPanel.displayScheduleCoverageReport(doctorList, dutyScheduleList);
    }
    
    public void generateWeeklyRosterTable() {
        doctorPanel.displayWeeklyRosterTable(dutyScheduleList, doctorList);
    }

    public void generateDutyScheduleReport(LocalDate startDate, LocalDate endDate) {
        System.out.println("=== DUTY SCHEDULE REPORT ===");
        System.out.println("Period: " + startDate + " to " + endDate);
        System.out.println("============================");
        
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek day = date.getDayOfWeek();
            System.out.println("\n" + day + " (" + date + "):");
            
            boolean hasDuties = false;
            for (DutySchedule duty : (Iterable<DutySchedule>)dutyScheduleList) {
                if (duty.getDay() == day) {
                    Doctor doctor = getDoctorById(duty.getDoctorId());
                    if (doctor != null) {
                        System.out.println("  " + duty.getStartTime() + " - " + duty.getEndTime() + 
                                         ": " + doctor.getName());
                        hasDuties = true;
                    }
                }
            }
            
            if (!hasDuties) {
                System.out.println("  No duties scheduled");
            }
        }
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private boolean hasDutyScheduleConflict(LocalDate date, LocalTime startTime, LocalTime endTime) {
        DutySchedule probe = new DutySchedule("", date, startTime, endTime);
        return dutyScheduleList.contains(probe);
    }

    // ==================== SENTINEL CREATION METHODS ====================
    
    private DutySchedule createMinDuty() {
        return new DutySchedule("MIN", LocalDate.MIN, LocalTime.MIN, LocalTime.MIN);
    }

    private DutySchedule createMaxDuty() {
        return new DutySchedule("MAX", LocalDate.MAX, LocalTime.MAX, LocalTime.MAX);
    }

    
    // ==================== UI INTEGRATION ====================

    public static void main(String[] args) {
        MaintainDoctor maintainDoctor = new MaintainDoctor();
        maintainDoctor.runDoctorMaintenance();
    }

    private class Node {

        private Doctor data;
        private Node next;

        private Node(Doctor data) {
          this.data = data;
          this.next = null;
        }

        private Node(Doctor data, Node next) {
          this.data = data;
          this.next = next;
        }
    }
}