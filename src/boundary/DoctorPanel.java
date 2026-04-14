package boundary;

/**
 *
 * @author Sebastian
 */
import adt.LinkedList;
import adt.ListInterface;
import adt.SortedSkipList;
import entity.Doctor;
import entity.DutySchedule;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import adt.SortedListInterface;
import java.util.Objects;

public class DoctorPanel {
    Scanner scanner = new Scanner(System.in);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public String getMainMenuChoice() {
        System.out.println("\n=== DOCTOR MANAGEMENT SYSTEM ===");
        System.out.println("1. Manage Doctor Information");
        System.out.println("2. Manage Duty Schedules");
        System.out.println("3. Manage Availability");
        System.out.println("4. Generate Report");
        System.out.println("0. Exit");
        System.out.print("Please choose an option: ");

        String choice = scanner.nextLine();
        System.out.println();
        return choice;
    }
    
    public String getManageDoctorChoice() {
        System.out.println("\n=== DOCTOR INFORMATION MANAGEMENT ===");
        System.out.println("1. Add Doctor");
        System.out.println("2. View All Doctors");
        System.out.println("3. Update Doctor");
        System.out.println("4. Remove Doctor");
        System.out.println("0. Back To Main Menu");
        System.out.print("Please choose an option: ");

        String choice = scanner.nextLine();
        System.out.println();
        return choice;
    }
    
    public String getDutyScheduleMenuChoice() {
        System.out.println("\n=== DUTY SCHEDULE MANAGEMENT ===");
        System.out.println("1. Assign Duty Schedule");
        System.out.println("2. View Duty Schedules by Doctor");
        System.out.println("3. View Duty Schedules by Date");
        System.out.println("4. Remove Duty Schedule");
        System.out.println("0. Back to Main Menu");
        System.out.print("Please choose an option: ");

        String choice = scanner.nextLine();
        return choice;
    }
    
    public String getAvailabilityMenuChoice() {
        System.out.println("=== AVAILABILITY MANAGEMENT ===");
        System.out.println("1. Add Available Date");
        System.out.println("2. View Available Dates");
        System.out.println("3. Remove Available Date");
        System.out.println("0. Back To Main Menu");
        System.out.print("Please choose an option: ");
        
        String choice = scanner.nextLine();
        return choice;
    }
    
    public String getGenerateReportMenuChoice() {
        System.out.println("=== REPORT GENERATION ===");
        System.out.println("1. Schedule Coverage Report");
        System.out.println("2. Weekly Roster Table");
        System.out.println("0. Back To Main Menu");
        System.out.print("Please choose an option: ");
        
        String choice = scanner.nextLine();
        return choice;
    }
    
    public Doctor inputDoctorDetails() {
        System.out.println("\n=== ADD NEW DOCTOR ===");
        
        System.out.print("Enter doctor name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter IC number: ");
        String icNo = scanner.nextLine();
        
        System.out.print("Enter specialization: ");
        String specialization = scanner.nextLine();
        
        System.out.print("Enter contact number: ");
        String contactNo = scanner.nextLine();
        
        double consultationFee = 0;
        boolean validFee = false;
        while (!validFee) {
            System.out.print("Enter consultation fee: ");
            try {
                consultationFee = Double.parseDouble(scanner.nextLine());
                validFee = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid fee format. Please enter a valid number.");
            }
        }
        
        return new Doctor(name, icNo, specialization, contactNo, consultationFee);
    }

    public void viewAllDoctors(ListInterface<Doctor> doctorList) {
        System.out.println("\n=== ALL DOCTORS ===");
        
        if (doctorList.isEmpty()) {
            System.out.println("No doctors found in the system.");
            pressEnterToContinue();
            return;
        }

        final int ID_WIDTH = 10;
        final int NAME_WIDTH = 20;
        final int IC_WIDTH = 15;
        final int SPECIALIZATION_WIDTH = 20;
        final int CONTACT_WIDTH = 15;
        final int FEE_WIDTH = 15;

        System.out.println(String.format("%-" + ID_WIDTH + "s | %-" + NAME_WIDTH + "s | %-" + 
            IC_WIDTH + "s | %-" + SPECIALIZATION_WIDTH + "s | %-" + CONTACT_WIDTH + "s | %-" + FEE_WIDTH + "s",
            "Doctor Id", "Name", "IC Number", "Specialisation", "Contact No.", "Consultation Fee"));

        System.out.println("-".repeat(ID_WIDTH + NAME_WIDTH + IC_WIDTH + SPECIALIZATION_WIDTH + 
            CONTACT_WIDTH + FEE_WIDTH + 15));

        // Print each doctor's information
        for (int i = 0; i < doctorList.size(); i++) {
            Doctor doctor = doctorList.get(i);

            // Format each field with proper wrapping
            String id = wrapText(doctor.getId(), ID_WIDTH);
            String name = wrapText(doctor.getName(), NAME_WIDTH);
            String icNo = wrapText(doctor.getIcNo(), IC_WIDTH);
            String specialization = wrapText(doctor.getSpecialisation(), SPECIALIZATION_WIDTH);
            String contactNo = wrapText(doctor.getContactNumber(), CONTACT_WIDTH);
            String fee = wrapText(String.format("RM %.2f", doctor.getConsultationFee()), FEE_WIDTH);

            System.out.println(String.format("%-" + ID_WIDTH + "s | %-" + NAME_WIDTH + "s | %-" + 
                IC_WIDTH + "s | %-" + SPECIALIZATION_WIDTH + "s | %-" + CONTACT_WIDTH + "s | %-" + FEE_WIDTH + "s",
                id, name, icNo, specialization, contactNo, fee));
        }

        System.out.println("\nTotal doctors: " + doctorList.size());
        pressEnterToContinue();
    }
    
    private String wrapText(String text, int maxWidth) {
        if (text == null) {
            return "";
        }

        if (text.length() <= maxWidth) {
            return text;
        }

        // Simple wrapping: truncate with ellipsis if too long
        return text.substring(0, Math.max(0, maxWidth - 3)) + "...";
    }

    public String getDoctorToUpdate() {
        System.out.println("\n=== UPDATE DOCTOR ===");
        
        System.out.print("Enter doctor ID to update: ");
        String doctorId = scanner.nextLine();
        return doctorId;
    }
        
    public String getUpdateChoice() {
        System.out.println("Select field to update:");
        System.out.println("1. Name");
        System.out.println("2. IC Number");
        System.out.println("3. Specialization");
        System.out.println("4. Contact Number");
        System.out.println("5. Consultation Fee");
        System.out.println("0. Return to previous page");
        System.out.print("Enter choice: ");
        
        String fieldChoice = scanner.nextLine();
        return fieldChoice;
    }

    public void changeDoctorName(Doctor doctor) {
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        doctor.setName(newName);
    }
    
    public String changeDoctorIc(Doctor doctor) {
        System.out.print("Enter new IC number: ");
        String newIc = scanner.nextLine();
        return newIc;
    }
    
    public void changeDoctorSpecialisation(Doctor doctor) {
        System.out.print("Enter new specialisation: ");
        String newSpecialisation = scanner.nextLine();
        doctor.setSpecialisation(newSpecialisation);
    }
    
    public void changeDoctorContactNo(Doctor doctor) {
        System.out.print("Enter new contact number: ");
        String newContactNo = scanner.nextLine();
        doctor.setContactNo(newContactNo);
    }
    
    public void changeDoctorConsultationFee(Doctor doctor) {
        System.out.print("Enter new consultation fee: ");
        try {
            double newFee = Double.parseDouble(scanner.nextLine());
            doctor.setConsultationFee(newFee);
        } catch (NumberFormatException e) {
            System.out.println("Invalid fee format. Update cancelled.");
        }
    }
    // In DoctorPanel.java

    public void displayFormattedAvailability(SortedSkipList<LocalDate> availability) {
        if (availability == null || availability.isEmpty()) {
            System.out.println("No available dates scheduled.");
            return;
        }

        // Group dates by month using your custom ADTs
        ListInterface<LocalDate> allDates = new LinkedList<>();
        for (LocalDate date : availability) {
            allDates.add(date);
        }

        // Create a map-like structure using paired lists
        ListInterface<YearMonth> months = new LinkedList<>();
        ListInterface<ListInterface<LocalDate>> datesByMonth = new LinkedList<>();

        for (int i = 0; i < allDates.size(); i++) {
            LocalDate date = allDates.get(i);
            YearMonth yearMonth = YearMonth.from(date);

            int monthIndex = -1;
            for (int j = 0; j < months.size(); j++) {
                if (months.get(j).equals(yearMonth)) {
                    monthIndex = j;
                    break;
                }
            }

            if (monthIndex == -1) {
                months.add(yearMonth);
                ListInterface<LocalDate> monthDates = new LinkedList<>();
                monthDates.add(date);
                datesByMonth.add(monthDates);
            } else {
                datesByMonth.get(monthIndex).add(date);
            }
        }

        // Display each month calendar
        for (int i = 0; i < months.size(); i++) {
            YearMonth yearMonth = months.get(i);
            ListInterface<LocalDate> monthDates = datesByMonth.get(i);
            displayMonthCalendar(yearMonth, monthDates);
        }

        System.out.println("Total available days: " + availability.getLength());
    }

    public void displayFormattedAvailability(SortedSkipList<LocalDate> availability, 
                                           SortedSkipList<DutySchedule> dutySchedules,
                                           String doctorId) {
        if (availability == null || availability.isEmpty()) {
            System.out.println("No available dates scheduled.");
            return;
        }

        // Extract duty dates for this specific doctor
        ListInterface<LocalDate> dutyDates = new LinkedList<>();
        if (dutySchedules != null) {
            for (DutySchedule duty : dutySchedules) {
                if (duty.getDoctorId().equals(doctorId)) {
                    dutyDates.add(duty.getDate());
                }
            }
        }

        // Convert availability to list for easier processing
        ListInterface<LocalDate> availableDates = new LinkedList<>();
        for (LocalDate date : availability) {
            availableDates.add(date);
        }

        // Group dates by month
        ListInterface<YearMonth> months = new LinkedList<>();
        ListInterface<ListInterface<LocalDate>> availableDatesByMonth = new LinkedList<>();
        ListInterface<ListInterface<LocalDate>> dutyDatesByMonth = new LinkedList<>();

        // Process available dates
        for (int i = 0; i < availableDates.size(); i++) {
            LocalDate date = availableDates.get(i);
            YearMonth yearMonth = YearMonth.from(date);

            int monthIndex = getMonthIndex(months, yearMonth);
            if (monthIndex == -1) {
                months.add(yearMonth);
                ListInterface<LocalDate> monthAvailableDates = new LinkedList<>();
                monthAvailableDates.add(date);
                availableDatesByMonth.add(monthAvailableDates);

                ListInterface<LocalDate> monthDutyDates = new LinkedList<>();
                dutyDatesByMonth.add(monthDutyDates);
            } else {
                availableDatesByMonth.get(monthIndex).add(date);
            }
        }

        // Process duty dates
        for (int i = 0; i < dutyDates.size(); i++) {
            LocalDate date = dutyDates.get(i);
            YearMonth yearMonth = YearMonth.from(date);

            int monthIndex = getMonthIndex(months, yearMonth);
            if (monthIndex == -1) {
                months.add(yearMonth);
                ListInterface<LocalDate> monthAvailableDates = new LinkedList<>();
                availableDatesByMonth.add(monthAvailableDates);

                ListInterface<LocalDate> monthDutyDates = new LinkedList<>();
                monthDutyDates.add(date);
                dutyDatesByMonth.add(monthDutyDates);
            } else {
                dutyDatesByMonth.get(monthIndex).add(date);
            }
        }

        // Display each month calendar with both availability and duty information
        for (int i = 0; i < months.size(); i++) {
            YearMonth yearMonth = months.get(i);
            ListInterface<LocalDate> monthAvailableDates = availableDatesByMonth.get(i);
            ListInterface<LocalDate> monthDutyDates = dutyDatesByMonth.get(i);
            displayMonthCalendar(yearMonth, monthAvailableDates, monthDutyDates);
        }

        System.out.println("Total available days: " + availability.getLength());
        System.out.println("Total duty days: " + dutyDates.size());
    }

    private int getMonthIndex(ListInterface<YearMonth> months, YearMonth target) {
        for (int i = 0; i < months.size(); i++) {
            if (months.get(i).equals(target)) {
                return i;
            }
        }
        return -1;
    }

    private void displayMonthCalendar(YearMonth yearMonth, ListInterface<LocalDate> availableDates) {
        System.out.println("\n+---------------------------------+");
        System.out.printf("| %s %-22s |\n", yearMonth.getMonth().toString(), yearMonth.getYear());
        System.out.println("+---------------------------------+");
        System.out.println("|Sun  Mon  Tue  Wed  Thu  Fri  Sat|");
        System.out.println("+---------------------------------+");

        LocalDate firstOfMonth = yearMonth.atDay(1);
        int daysInMonth = yearMonth.lengthOfMonth();

        // Print leading spaces
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday = 0
        for (int i = 0; i < dayOfWeek; i++) {
            System.out.print("    ");
        }

        // Print each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = yearMonth.atDay(day);
            boolean isAvailable = containsDate(availableDates, currentDate);

            if (isAvailable) {
                System.out.printf("[\033[32m%2d\033[0m] ", day); // Green for available
            } else {
                System.out.printf(" %2d  ", day); // Normal for unavailable
            }

            // New line at end of week
            if ((day + dayOfWeek) % 7 == 0 || day == daysInMonth) {
                System.out.println();
            }
        }

        System.out.println("+---------------------------------+");
        System.out.println("[\033[32m##\033[0m] = Available (" + availableDates.size() + " days this month)");
    }

    private void displayMonthCalendar(YearMonth yearMonth, 
                                 ListInterface<LocalDate> availableDates,
                                 ListInterface<LocalDate> dutyDates) {
        System.out.println("\n+---------------------------------+");
        System.out.printf("| %s %-24s |\n", yearMonth.getMonth().toString(), yearMonth.getYear());
        System.out.println("+---------------------------------+");
        System.out.println("|Sun  Mon  Tue  Wed  Thu  Fri  Sat|");
        System.out.println("+---------------------------------+");

        LocalDate firstOfMonth = yearMonth.atDay(1);
        int daysInMonth = yearMonth.lengthOfMonth();

        // Print leading spaces
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday = 0
        for (int i = 0; i < dayOfWeek; i++) {
            System.out.print("    ");
        }

        // Print each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = yearMonth.atDay(day);
            boolean isAvailable = containsDate(availableDates, currentDate);
            boolean isOnDuty = containsDate(dutyDates, currentDate);

            if (isOnDuty) {
                System.out.printf("[\033[31m%2d\033[0m] ", day); // Red for duty
            } else if (isAvailable) {
                System.out.printf("[\033[32m%2d\033[0m] ", day); // Green for available only
            } else {
                System.out.printf(" %2d  ", day); // Normal for unavailable
            }

            // New line at end of week
            if ((day + dayOfWeek) % 7 == 0 || day == daysInMonth) {
                System.out.println();
            }
        }

        System.out.println("+---------------------------------+");
        System.out.println("[\033[32m##\033[0m] = Available (" + availableDates.size() + " days)");
        System.out.println("[\033[31m##\033[0m] = On Duty (" + dutyDates.size() + " days)");
        System.out.println(" ##  = Not Available");
    }

    private boolean containsDate(ListInterface<LocalDate> dates, LocalDate target) {
        for (int i = 0; i < dates.size(); i++) {
            if (dates.get(i).equals(target)) {
                return true;
            }
        }
        return false;
    }

    private DayCoverageStats calculateDailyCoverage(LocalDate date, 
                                               SortedListInterface<DutySchedule> dutyScheduleList,
                                               ListInterface<Doctor> doctorList) {
        DayCoverageStats stats = new DayCoverageStats();
        stats.coverageGaps = "None";

        // Get all unique specializations from doctors
        ListInterface<String> allSpecializations = new LinkedList<>();
        ListInterface<String> specializationsPresent = new LinkedList<>();

        for (int i = 0; i < doctorList.size(); i++) {
            String specialization = doctorList.get(i).getSpecialisation();
            if (!containsString(allSpecializations, specialization)) {
                allSpecializations.add(specialization);
            }
        }

        // Check duties for this date
        try {
            for (DutySchedule duty : (Iterable<DutySchedule>) dutyScheduleList) {
                if (duty.getDate().equals(date)) {
                    stats.doctorCount++;
                    long hours = java.time.Duration.between(duty.getStartTime(), duty.getEndTime()).toHours();
                    stats.totalHours += hours;

                    // Track which specializations are covered
                    Doctor doctor = findDoctorById(duty.getDoctorId(), doctorList);
                    if (doctor != null) {
                        String specialization = doctor.getSpecialisation();
                        if (!containsString(specializationsPresent, specialization)) {
                            specializationsPresent.add(specialization);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error calculating daily coverage: " + e.getMessage());
        }

        // Identify coverage gaps - check if any specializations are missing
        if (specializationsPresent.size() < allSpecializations.size()) {
            ListInterface<String> missingSpecializations = new LinkedList<>();
            for (int i = 0; i < allSpecializations.size(); i++) {
                String spec = allSpecializations.get(i);
                if (!containsString(specializationsPresent, spec)) {
                    missingSpecializations.add(spec);
                }
            }
            if (missingSpecializations.size() > 0) {
                StringBuilder gaps = new StringBuilder("Missing: ");
                for (int i = 0; i < missingSpecializations.size(); i++) {
                    if (i > 0) gaps.append(", ");
                    gaps.append(missingSpecializations.get(i));
                }
                stats.coverageGaps = gaps.toString();
            }
        }

        return stats;
    }

    private boolean containsString(ListInterface<String> list, String target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) {
                return true;
            }
        }
        return false;
    }

    private int countScheduledDaysNext7Days(String doctorId, SortedListInterface<DutySchedule> dutyScheduleList) {
        int count = 0;
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(6);

        ListInterface<LocalDate> uniqueDays = new LinkedList<>();

        try {
            for (DutySchedule duty : (Iterable<DutySchedule>) dutyScheduleList) {
                if (duty.getDoctorId().equals(doctorId) && 
                    !duty.getDate().isBefore(today) && 
                    !duty.getDate().isAfter(endDate)) {

                    if (!containsDate(uniqueDays, duty.getDate())) {
                        uniqueDays.add(duty.getDate());
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error counting scheduled days: " + e.getMessage());
        }

        return count;
    }



    public void displayWeeklyRosterTable(SortedListInterface<DutySchedule> dutyScheduleList,
                                       ListInterface<Doctor> doctorList) {
        System.out.println("\n=== WEEKLY DUTY ROSTER ===");
        System.out.println("Week: " + LocalDate.now() + " to " + LocalDate.now().plusDays(6));
        System.out.println("===========================");

        if (dutyScheduleList == null || doctorList == null) {
            System.out.println("No data available.");
            pressEnterToContinue();
            return;
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(6);

        // Get all duties for the next 7 days
        ListInterface<DutySchedule> weeklyDuties = new LinkedList<>();

        try {
            for (DutySchedule duty : (Iterable<DutySchedule>) dutyScheduleList) {
                if (duty != null && !duty.getDate().isBefore(startDate) && !duty.getDate().isAfter(endDate)) {
                    weeklyDuties.add(duty);
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving weekly duties: " + e.getMessage());
            pressEnterToContinue();
            return;
        }

        if (weeklyDuties.isEmpty()) {
            System.out.println("No duties scheduled for the next 7 days.");
            pressEnterToContinue();
            return;
        }

        // Create simpler data structure - group by date first
        ListInterface<LocalDate> weekDates = new LinkedList<>();
        ListInterface<ListInterface<DutySchedule>> dutiesByDate = new LinkedList<>();

        // Initialize 7 days
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            weekDates.add(date);
            dutiesByDate.add(new LinkedList<>());
        }

        // Group duties by date
        for (int i = 0; i < weeklyDuties.size(); i++) {
            try {
                DutySchedule duty = weeklyDuties.get(i);
                if (duty != null) {
                    int dateIndex = getDateIndex(weekDates, duty.getDate());
                    if (dateIndex != -1) {
                        dutiesByDate.get(dateIndex).add(duty);
                    }
                }
            } catch (Exception e) {
                // Skip problematic duties
                continue;
            }
        }

        // Display simplified table
        displaySimplifiedWeeklyTable(weekDates, dutiesByDate, doctorList);

        pressEnterToContinue();
    }

    private void displaySimplifiedWeeklyTable(ListInterface<LocalDate> weekDates,
                                           ListInterface<ListInterface<DutySchedule>> dutiesByDate,
                                           ListInterface<Doctor> doctorList) {
        final int DOCTOR_WIDTH = 20;
        final int DAY_WIDTH = 15;
        final int TOTAL_WIDTH = 8;

        // Table header
        System.out.print(String.format("%-" + DOCTOR_WIDTH + "s", "Doctor"));
        for (int i = 0; i < weekDates.size(); i++) {
            LocalDate date = weekDates.get(i);
            String header = date.getDayOfWeek().toString().substring(0, 3) + 
                           " (" + date.getDayOfMonth() + ")";
            System.out.print(String.format("| %-" + (DAY_WIDTH - 2) + "s ", header));
        }
        System.out.print(String.format("| %-" + TOTAL_WIDTH + "s", "Total"));
        System.out.println();

        // Separator line
        System.out.print("-".repeat(DOCTOR_WIDTH));
        for (int i = 0; i < weekDates.size(); i++) {
            System.out.print("+" + "-".repeat(DAY_WIDTH));
        }
        System.out.print("+" + "-".repeat(TOTAL_WIDTH));
        System.out.println();

        // Track doctor totals - we'll calculate as we go instead of storing in a list
        ListInterface<String> allDoctorNames = new LinkedList<>();

        // First, collect all unique doctors from the duties
        for (int dateIndex = 0; dateIndex < dutiesByDate.size(); dateIndex++) {
            ListInterface<DutySchedule> dateDuties = dutiesByDate.get(dateIndex);
            for (int dutyIndex = 0; dutyIndex < dateDuties.size(); dutyIndex++) {
                try {
                    DutySchedule duty = dateDuties.get(dutyIndex);
                    Doctor doctor = findDoctorById(duty.getDoctorId(), doctorList);
                    if (doctor != null) {
                        String doctorName = doctor.getName();
                        if (!containsDoctor(allDoctorNames, doctorName)) {
                            allDoctorNames.add(doctorName);
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

        // Display each doctor's row
        for (int doctorIndex = 0; doctorIndex < allDoctorNames.size(); doctorIndex++) {
            String doctorName = allDoctorNames.get(doctorIndex);
            int totalDays = 0;

            System.out.print(String.format("%-" + DOCTOR_WIDTH + "s", 
                doctorName.length() > DOCTOR_WIDTH - 2 ? 
                doctorName.substring(0, DOCTOR_WIDTH - 3) + "." : doctorName));

            // For each day, check if this doctor has duty
            for (int dateIndex = 0; dateIndex < weekDates.size(); dateIndex++) {
                ListInterface<DutySchedule> dateDuties = dutiesByDate.get(dateIndex);
                String dutyTime = "-";

                for (int dutyIndex = 0; dutyIndex < dateDuties.size(); dutyIndex++) {
                    try {
                        DutySchedule duty = dateDuties.get(dutyIndex);
                        Doctor doctor = findDoctorById(duty.getDoctorId(), doctorList);
                        if (doctor != null && doctor.getName().equals(doctorName)) {
                            dutyTime = duty.getStartTime().format(timeFormatter) + "-" + 
                                      duty.getEndTime().format(timeFormatter);
                            totalDays++;
                            break;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }

                System.out.print(String.format("| %-" + (DAY_WIDTH - 2) + "s ", dutyTime));
            }

            // Display total - no need to store in a separate list
            System.out.print(String.format("| %-" + (TOTAL_WIDTH - 2) + "s ", totalDays + " days"));
            System.out.println();
        }

        // Bottom separator
        System.out.print("-".repeat(DOCTOR_WIDTH));
        for (int i = 0; i < weekDates.size(); i++) {
            System.out.print("+" + "-".repeat(DAY_WIDTH));
        }
        System.out.print("+" + "-".repeat(TOTAL_WIDTH));
        System.out.println();

        // Bottom row: Number of doctors working each day
        System.out.print(String.format("%-" + DOCTOR_WIDTH + "s", "Doctors Working:"));
        for (int dateIndex = 0; dateIndex < weekDates.size(); dateIndex++) {
            int doctorsCount = dutiesByDate.get(dateIndex).size();
            System.out.print(String.format("| %-" + (DAY_WIDTH - 2) + "s ", doctorsCount + " Drs"));
        }
        System.out.print(String.format("| %-" + TOTAL_WIDTH + "s", ""));
        System.out.println();
    }

    // Helper methods
    private boolean containsDoctor(ListInterface<String> doctorNames, String doctorName) {
        for (int i = 0; i < doctorNames.size(); i++) {
            try {
                if (doctorNames.get(i).equals(doctorName)) {
                    return true;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return false;
    }

    private int getDateIndex(ListInterface<LocalDate> dates, LocalDate targetDate) {
        for (int i = 0; i < dates.size(); i++) {
            try {
                if (dates.get(i).equals(targetDate)) {
                    return i;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return -1;
    }

    private Doctor findDoctorById(String doctorId, ListInterface<Doctor> doctorList) {
        if (doctorList == null) return null;

        for (int i = 0; i < doctorList.size(); i++) {
            try {
                Doctor doctor = doctorList.get(i);
                if (doctor != null && doctor.getId().equals(doctorId)) {
                    return doctor;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    private void displayWeeklyTable(ListInterface<String> doctorNames, 
                                  ListInterface<LocalDate> weekDates,
                                  ListInterface<ListInterface<String>> scheduleGrid,
                                  ListInterface<Integer> doctorDayCounts,
                                  ListInterface<Integer> dayDoctorCounts) {
        final int DOCTOR_WIDTH = 20;
        final int DAY_WIDTH = 15;
        final int TOTAL_WIDTH = 8;

        // Table header
        System.out.print(String.format("%-" + DOCTOR_WIDTH + "s", "Doctor"));
        for (int i = 0; i < weekDates.size(); i++) {
            LocalDate date = weekDates.get(i);
            String header = date.getDayOfWeek().toString().substring(0, 3) + 
                           " (" + date.getDayOfMonth() + ")";
            System.out.print(String.format("| %-" + (DAY_WIDTH - 2) + "s ", header));
        }
        System.out.print(String.format("| %-" + TOTAL_WIDTH + "s", "Total"));
        System.out.println();

        // Separator line
        System.out.print("-".repeat(DOCTOR_WIDTH));
        for (int i = 0; i < weekDates.size(); i++) {
            System.out.print("+" + "-".repeat(DAY_WIDTH));
        }
        System.out.print("+" + "-".repeat(TOTAL_WIDTH));
        System.out.println();

        // Table rows (doctors)
        for (int i = 0; i < doctorNames.size(); i++) {
            String doctorName = doctorNames.get(i);
            ListInterface<String> schedule = scheduleGrid.get(i);
            int totalDays = doctorDayCounts.get(i);

            // Wrap doctor name if too long
            String displayName = doctorName;
            if (doctorName.length() > DOCTOR_WIDTH - 2) {
                displayName = doctorName.substring(0, DOCTOR_WIDTH - 3) + ".";
            }

            System.out.print(String.format("%-" + DOCTOR_WIDTH + "s", displayName));

            // Print each day's duty time
            for (int day = 0; day < weekDates.size(); day++) {
                String dutyTime = schedule.get(day);
                System.out.print(String.format("| %-" + (DAY_WIDTH - 2) + "s ", dutyTime));
            }

            // Doctor total days working
            String totalDisplay = totalDays + " days";
            System.out.print(String.format("| %-" + (TOTAL_WIDTH - 2) + "s ", totalDisplay));
            System.out.println();
        }

        // Bottom separator
        System.out.print("-".repeat(DOCTOR_WIDTH));
        for (int i = 0; i < weekDates.size(); i++) {
            System.out.print("+" + "-".repeat(DAY_WIDTH));
        }
        System.out.print("+" + "-".repeat(TOTAL_WIDTH));
        System.out.println();

        // Bottom row: Number of doctors working each day
        System.out.print(String.format("%-" + DOCTOR_WIDTH + "s", "Doctors Working:"));
        for (int i = 0; i < weekDates.size(); i++) {
            int doctorsCount = dayDoctorCounts.get(i);
            String countDisplay = doctorsCount + " Drs";
            System.out.print(String.format("| %-" + (DAY_WIDTH - 2) + "s ", countDisplay));
        }
        System.out.print(String.format("| %-" + TOTAL_WIDTH + "s", ""));
        System.out.println();
    }

    // Helper methods
    private int getDoctorIndex(ListInterface<String> doctorNames, String doctorName) {
        for (int i = 0; i < doctorNames.size(); i++) {
            if (doctorNames.get(i).equals(doctorName)) {
                return i;
            }
        }
        return -1;
    }

    private int getIndex(ListInterface<String> list, String target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) {
                return i;
            }
        }
        return -1;
    }

    private int getDayIndex(ListInterface<DayOfWeek> list, DayOfWeek target) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == target) {
                return i;
            }
        }
        return -1;
    }

    private void displayWeeklyTableWithAggregations(ListInterface<String> scheduleDoctors,
                                                  ListInterface<ListInterface<String>> doctorSchedules,
                                                  ListInterface<String> doctorNames,
                                                  ListInterface<Integer> doctorDayCounts,
                                                  ListInterface<DayOfWeek> daysOfWeek,
                                                  ListInterface<Integer> dayDoctorCounts,
                                                  LocalDate startDate) {
        final int DOCTOR_WIDTH = 20;
        final int DAY_WIDTH = 15;
        final int TOTAL_WIDTH = 8;

        // Table header
        System.out.print(String.format("%-" + DOCTOR_WIDTH + "s", "Doctor"));
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            String header = currentDate.getDayOfWeek().toString().substring(0, 3) + 
                           " (" + currentDate.getDayOfMonth() + ")";
            System.out.print(String.format("| %-" + (DAY_WIDTH - 2) + "s ", header));
        }
        System.out.print(String.format("| %-" + TOTAL_WIDTH + "s", "Total"));
        System.out.println();

        // Separator line
        int totalColumns = DOCTOR_WIDTH + (7 * DAY_WIDTH) + TOTAL_WIDTH;
        System.out.print("-".repeat(DOCTOR_WIDTH));
        for (int i = 0; i < 7; i++) {
            System.out.print("+" + "-".repeat(DAY_WIDTH));
        }
        System.out.print("+" + "-".repeat(TOTAL_WIDTH));
        System.out.println();

        // Table rows
        for (int i = 0; i < scheduleDoctors.size(); i++) {
            String doctorName = scheduleDoctors.get(i);
            ListInterface<String> schedule = doctorSchedules.get(i);
            int totalDays = 0;

            // Find the total days for this doctor
            for (int j = 0; j < doctorNames.size(); j++) {
                if (doctorNames.get(j).equals(doctorName)) {
                    totalDays = doctorDayCounts.get(j);
                    break;
                }
            }

            // Wrap doctor name if too long
            String displayName = doctorName;
            if (doctorName.length() > DOCTOR_WIDTH - 2) {
                displayName = doctorName.substring(0, DOCTOR_WIDTH - 3) + ".";
            }

            System.out.print(String.format("%-" + DOCTOR_WIDTH + "s", displayName));

            // Print each day's duty time
            for (int day = 0; day < 7; day++) {
                String dutyTime = schedule.get(day);

                // Color code: green for duties, default for no duty
                if (!dutyTime.equals("-")) {
                    System.out.print(String.format("| \033[32m%-" + (DAY_WIDTH - 2) + "s\033[0m ", dutyTime));
                } else {
                    System.out.print(String.format("| %-" + (DAY_WIDTH - 2) + "s ", dutyTime));
                }
            }

            // Doctor total days working
            String totalDisplay = totalDays + " days";
            if (totalDays > 0) {
                System.out.print(String.format("| \033[34m%-" + (TOTAL_WIDTH - 2) + "s\033[0m ", totalDisplay));
            } else {
                System.out.print(String.format("| %-" + (TOTAL_WIDTH - 2) + "s ", totalDisplay));
            }
            System.out.println();
        }

        // Bottom row: Number of doctors working each day
        System.out.print("-".repeat(DOCTOR_WIDTH));
        for (int i = 0; i < 7; i++) {
            System.out.print("+" + "-".repeat(DAY_WIDTH));
        }
        System.out.print("+" + "-".repeat(TOTAL_WIDTH));
        System.out.println();

        System.out.print(String.format("%-" + DOCTOR_WIDTH + "s", "Doctors Working:"));
        for (int i = 0; i < 7; i++) {
            int doctorsCount = dayDoctorCounts.get(i);
            String countDisplay = doctorsCount + " Drs";
            if (doctorsCount > 0) {
                System.out.print(String.format("| \033[33m%-" + (DAY_WIDTH - 2) + "s\033[0m ", countDisplay));
            } else {
                System.out.print(String.format("| %-" + (DAY_WIDTH - 2) + "s ", countDisplay));
            }
        }
        System.out.print(String.format("| %-" + TOTAL_WIDTH + "s", ""));
        System.out.println();

        System.out.println("\nLegend: \033[32mGreen\033[0m = On Duty, - = No Duty");
        System.out.println("        \033[34mBlue\033[0m = Doctor Total Days, \033[33mYellow\033[0m = Doctors Per Day");
    }

    // Alternative compact version for many doctors - with aggregations
    public void displayCompactWeeklyRoster(SortedListInterface<DutySchedule> dutyScheduleList,
                                         ListInterface<Doctor> doctorList) {
        System.out.println("\n=== COMPACT WEEKLY ROSTER ===");
        System.out.println("Week: " + LocalDate.now() + " to " + LocalDate.now().plusDays(6));
        System.out.println("==============================");

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(6);

        // Group duties by day and track statistics
        ListInterface<LocalDate> dates = new LinkedList<>();
        ListInterface<ListInterface<DutySchedule>> dutiesByDay = new LinkedList<>();
        ListInterface<String> doctorNames = new LinkedList<>();
        ListInterface<Integer> doctorDayCounts = new LinkedList<>();

        // Initialize dates and empty duty lists
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            dates.add(date);
            dutiesByDay.add(new LinkedList<>());
        }

        try {
            for (DutySchedule duty : (Iterable<DutySchedule>) dutyScheduleList) {
                if (!duty.getDate().isBefore(startDate) && !duty.getDate().isAfter(endDate)) {
                    int dayIndex = getDateIndex(dates, duty.getDate());
                    if (dayIndex != -1) {
                        dutiesByDay.get(dayIndex).add(duty);
                    }

                    // Update doctor day count
                    Doctor doctor = findDoctorById(duty.getDoctorId(), doctorList);
                    if (doctor != null) {
                        String doctorName = doctor.getName();
                        int doctorIndex = getIndex(doctorNames, doctorName);
                        if (doctorIndex == -1) {
                            doctorNames.add(doctorName);
                            doctorDayCounts.add(1);
                        } else {
                            int currentCount = doctorDayCounts.get(doctorIndex);
                            doctorDayCounts.set(doctorIndex, currentCount + 1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error generating roster: " + e.getMessage());
            pressEnterToContinue();
            return;
        }

        // Display day-by-day summary with doctor counts
        final int DAY_WIDTH = 12;
        final int TIME_WIDTH = 12;
        final int DOCTOR_WIDTH = 20;

        int totalWeekDuties = 0;
        int totalWeekDoctors = 0;

        for (int i = 0; i < dates.size(); i++) {
            LocalDate date = dates.get(i);
            ListInterface<DutySchedule> dayDuties = dutiesByDay.get(i);
            String dayName = date.getDayOfWeek().toString();
            int dayDoctorCount = dayDuties.size();

            System.out.println("\n" + date + " (" + dayName + ") - " + dayDoctorCount + " doctors");
            System.out.println("-".repeat(DAY_WIDTH + TIME_WIDTH + DOCTOR_WIDTH + 4));

            if (dayDuties.size() > 0) {
                // Sort by start time (using bubble sort since we can't use Collections.sort)
                sortDutiesByStartTime(dayDuties);

                System.out.println(String.format("%-" + TIME_WIDTH + "s | %-" + DOCTOR_WIDTH + "s", 
                    "Time", "Doctor"));
                System.out.println("-".repeat(TIME_WIDTH + DOCTOR_WIDTH + 3));

                for (int j = 0; j < dayDuties.size(); j++) {
                    DutySchedule duty = dayDuties.get(j);
                    Doctor doctor = findDoctorById(duty.getDoctorId(), doctorList);
                    String doctorName = (doctor != null) ? doctor.getName() : "Unknown";
                    String timeSlot = duty.getStartTime().format(timeFormatter) + "-" + 
                                    duty.getEndTime().format(timeFormatter);

                    System.out.println(String.format("%-" + TIME_WIDTH + "s | %-" + DOCTOR_WIDTH + "s", 
                        timeSlot, doctorName));
                }
                totalWeekDuties += dayDuties.size();
                totalWeekDoctors = Math.max(totalWeekDoctors, dayDoctorCount);
            } else {
                System.out.println("No duties scheduled");
            }
        }

        // Display summary statistics
        System.out.println("\n=== WEEKLY SUMMARY ===");
        System.out.println("-".repeat(20));
        System.out.println("Total Duties: " + totalWeekDuties);
        System.out.println("Peak Daily Doctors: " + totalWeekDoctors);

        // Doctor workload summary
        if (doctorNames.size() > 0) {
            System.out.println("\nDoctor Workload Summary:");
            for (int i = 0; i < doctorNames.size(); i++) {
                if (doctorDayCounts.get(i) > 0) {
                    System.out.println(String.format("  %-20s: %d days", doctorNames.get(i), doctorDayCounts.get(i)));
                }
            }
        }

        pressEnterToContinue();
    }

    private void sortDutiesByStartTime(ListInterface<DutySchedule> duties) {
        // Simple bubble sort for small lists
        for (int i = 0; i < duties.size() - 1; i++) {
            for (int j = 0; j < duties.size() - i - 1; j++) {
                DutySchedule duty1 = duties.get(j);
                DutySchedule duty2 = duties.get(j + 1);

                if (duty1.getStartTime().compareTo(duty2.getStartTime()) > 0) {
                    // Swap
                    duties.set(j, duty2);
                    duties.set(j + 1, duty1);
                }
            }
        }
    }

    // Helper class for YearMonth (since we can't use java.time.YearMonth directly with custom ADTs)
    private static class YearMonth {
        private final int year;
        private final int month;

        public YearMonth(int year, int month) {
            this.year = year;
            this.month = month;
        }

        public static YearMonth from(LocalDate date) {
            return new YearMonth(date.getYear(), date.getMonthValue());
        }

        public int getYear() { return year; }
        public int getMonthValue() { return month; }
        public String getMonth() { 
            return java.time.Month.of(month).toString();
        }
        public int lengthOfMonth() {
            return java.time.Month.of(month).length(java.time.Year.isLeap(year));
        }
        public LocalDate atDay(int day) {
            return LocalDate.of(year, month, day);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            YearMonth other = (YearMonth) obj;
            return year == other.year && month == other.month;
        }

        @Override
        public int hashCode() {
            return Objects.hash(year, month);
        }
    }
    
    public LocalDate inputNewAvailability() {
        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter new available date (yyyy-MM-dd): ");
            try {
                date = LocalDate.parse(scanner.nextLine(), dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        return date;
    }
    
    public LocalDate inputAvailabilityToRemove() {
        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter a date to remove (yyyy-MM-dd): ");
            try {
                date = LocalDate.parse(scanner.nextLine(), dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        return date;
    }
    
    public String inputDoctorIdToRemove() {
        System.out.println("\n=== REMOVE DOCTOR ===");
        
        System.out.print("Enter doctor ID to remove: ");
        String doctorId = scanner.nextLine();
        
        return doctorId;
    }

    public String inputDoctorId() {
        System.out.print("Enter doctor ID: ");
        String doctorId = scanner.nextLine();
        return doctorId;
    }
    
    public LocalDate inputDate() {
        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter date (yyyy-MM-dd): ");
            try {
                date = LocalDate.parse(scanner.nextLine(), dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        return date;
    }
    
    public LocalTime inputStartTime() {
        LocalTime startTime = null;
        while (startTime == null) {
            System.out.print("Enter start time (HH:mm): ");
            try {
                startTime = LocalTime.parse(scanner.nextLine(), timeFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use HH:mm.");
            }
        }
        return startTime;
    }

    public LocalTime inputEndTime(LocalTime startTime) {
        LocalTime endTime = null;
        while (endTime == null || endTime.isBefore(startTime)) {
            System.out.print("Enter end time (HH:mm): ");
            try {
                endTime = LocalTime.parse(scanner.nextLine(), timeFormatter);
                if (endTime.isBefore(startTime)) {
                    System.out.println("End time cannot be before start time.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use HH:mm.");
            }
        }
        
        return endTime;
    }
    
    public LocalDate inputStartDate() {
        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter start date (yyyy-MM-dd): ");
            try {
                date = LocalDate.parse(scanner.nextLine(), dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        return date;
    }

    public LocalDate inputEndDate(LocalDate startDate) {
        LocalDate endDate = null;
        while (endDate == null || endDate.isBefore(startDate)) {
            System.out.print("Enter date (yyyy-MM-dd): ");
            try {
                endDate = LocalDate.parse(scanner.nextLine(), dateFormatter);
                if (endDate.isBefore(startDate)) {
                    System.out.println("End date cannot be before start date.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        return endDate;
    }

    public void viewDutySchedulesByDoctor(SortedSkipList<DutySchedule> dutyList) {
        System.out.println("\n=== VIEW DUTY SCHEDULES BY DOCTOR ===");

        if (dutyList.isEmpty()) {
            System.out.println("No duty schedules found.");
            pressEnterToContinue();
            return;
        }

        String doctorId = null;
        for (DutySchedule duty : dutyList) {
            doctorId = duty.getDoctorId();
            break;
        }

        System.out.println("Duty schedules for doctor " + doctorId + ":");

        for (DutySchedule duty : dutyList) {
            System.out.println(duty.getDate() + " | " + 
                             duty.getStartTime() + " - " + 
                             duty.getEndTime());
        }

        System.out.println("Total duties: " + dutyList.getLength());
        pressEnterToContinue();
    }

    public void viewDutySchedulesByDate(SortedSkipList<DutySchedule> dutyList, ListInterface<Doctor> doctorList) {
        System.out.println("\n=== VIEW DUTY SCHEDULES BY DATE ===");

        if (dutyList.isEmpty()) {
            System.out.println("No duty schedules found.");
            pressEnterToContinue();
            return;
        }

        // Extract date from the first element in the list
        LocalDate date = null;
        for (DutySchedule duty : dutyList) {
            date = duty.getDate();
            break;
        }

        System.out.println("Duty schedules for " + date + ":");

        // Helper method to find doctor by ID
        for (DutySchedule duty : dutyList) {
            Doctor doctor = findDoctorById(duty.getDoctorId(), doctorList);
            if (doctor != null) {
                System.out.println("Dr. " + doctor.getName() + 
                                 " (" + doctor.getSpecialisation() + ") | " + 
                                 duty.getStartTime() + " - " + 
                                 duty.getEndTime());
            } else {
                System.out.println("Doctor ID: " + duty.getDoctorId() + 
                                 " | " + duty.getStartTime() + " - " + 
                                 duty.getEndTime() + " (Doctor not found)");
            }
        }

        System.out.println("Total duties: " + dutyList.getLength());
        pressEnterToContinue();
    }

    // Helper method to find doctor by ID
//    private Doctor findDoctorById(String doctorId, ListInterface<Doctor> doctorList) {
//        for (int i = 0; i < doctorList.size(); i++) {
//            Doctor doctor = doctorList.get(i);
//            if (doctor.getId().equals(doctorId)) {
//                return doctor;
//            }
//        }
//        return null;
//    }

    public void displayScheduleCoverageReport(ListInterface<Doctor> doctorList, 
                                            SortedListInterface<DutySchedule> dutyScheduleList) {
        System.out.println("\n=== DOCTOR SCHEDULE COVERAGE & CONFLICT REPORT ===");
        System.out.println("Period: Next 7 Days");
        System.out.println("Generated on: " + LocalDate.now());
        System.out.println("===================================================");

        if (doctorList.isEmpty()) {
            System.out.println("No doctors found in the system.");
            pressEnterToContinue();
            return;
        }

        // Analyze coverage for the next 7 days
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(6);

        System.out.println("Analysis Period: " + startDate + " to " + endDate);
        System.out.println();

        // 1. Daily Coverage Summary
        System.out.println("DAILY COVERAGE SUMMARY:");
        System.out.println("-----------------------");

        final int DAY_WIDTH = 20;
        final int DOCTORS_WIDTH = 10;
        final int HOURS_WIDTH = 15;
        final int SPECIALIZATION_WIDTH = 70;

        System.out.println(String.format("%-" + DAY_WIDTH + "s | %-" + DOCTORS_WIDTH + "s | %-" + 
            HOURS_WIDTH + "s | %-" + SPECIALIZATION_WIDTH + "s",
            "Date", "Doctors", "Total Hours", "Coverage Gaps"));

        System.out.println("-".repeat(DAY_WIDTH + DOCTORS_WIDTH + HOURS_WIDTH + SPECIALIZATION_WIDTH + 9));

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayCoverageStats stats = calculateDailyCoverage(date, dutyScheduleList, doctorList);

            String dayName = date.getDayOfWeek().toString();
            String dateStr = wrapText(date.toString() + " (" + dayName.substring(0, 3) + ")", DAY_WIDTH);
            String doctors = wrapText(String.valueOf(stats.doctorCount), DOCTORS_WIDTH);
            String hours = wrapText(String.format("%.1f hrs", stats.totalHours), HOURS_WIDTH);
            String gaps = wrapText(stats.coverageGaps, SPECIALIZATION_WIDTH);

            System.out.println(String.format("%-" + DAY_WIDTH + "s | %-" + DOCTORS_WIDTH + "s | %-" + 
                HOURS_WIDTH + "s | %-" + SPECIALIZATION_WIDTH + "s",
                dateStr, doctors, hours, gaps));
        }

        System.out.println();

        // 2. Potential Conflicts
        System.out.println("POTENTIAL SCHEDULING ISSUES:");
        System.out.println("----------------------------");
        identifySchedulingConflicts(dutyScheduleList, doctorList);

        System.out.println();

        // 3. Doctor Schedule Summary
        System.out.println("DOCTOR SCHEDULE SUMMARY (Next 7 Days):");
        System.out.println("--------------------------------------");

        for (int i = 0; i < doctorList.size(); i++) {
            Doctor doctor = doctorList.get(i);
            int scheduledDays = countScheduledDaysNext7Days(doctor.getId(), dutyScheduleList);
            int availableDays = countAvailableDaysNext7Days(doctor);

            String status = "OK";
            if (scheduledDays > availableDays) {
                status = "CONFLICT: Scheduled but not available";
            } else if (scheduledDays == 0 && availableDays > 0) {
                status = "UNDERUTILIZED: Available but not scheduled";
            }

            System.out.println(String.format("%-20s: %d duties, %d available days - %s", 
                doctor.getName(), scheduledDays, availableDays, status));
        }

        pressEnterToContinue();
    }

    
    private void identifySchedulingConflicts(SortedListInterface<DutySchedule> dutyScheduleList,
                                           ListInterface<Doctor> doctorList) {
        boolean foundConflicts = false;

        // Check for overlapping duties for the same doctor
        try {
            for (int i = 0; i < doctorList.size(); i++) {
                Doctor doctor = doctorList.get(i);
                SortedSkipList<DutySchedule> doctorDuties = new SortedSkipList<>(
                        new DutySchedule("MIN", LocalDate.MIN, LocalTime.MIN, LocalTime.MIN), 
                        new DutySchedule("MAX", LocalDate.MAX, LocalTime.MAX, LocalTime.MAX));

                // Collect all duties for this doctor
                for (DutySchedule duty : (Iterable<DutySchedule>) dutyScheduleList) {
                    if (duty.getDoctorId().equals(doctor.getId())) {
                        doctorDuties.add(duty);
                    }
                }

                // Check for overlaps
                DutySchedule previousDuty = null;
                for (DutySchedule currentDuty : (Iterable<DutySchedule>) doctorDuties) {
                    if (previousDuty != null && 
                        currentDuty.getDate().equals(previousDuty.getDate()) &&
                        currentDuty.getStartTime().isBefore(previousDuty.getEndTime())) {
                        System.out.println("OVERLAP: Dr. " + doctor.getName() + " on " + 
                                         currentDuty.getDate() + " (" + 
                                         previousDuty.getStartTime() + "-" + previousDuty.getEndTime() + " vs " +
                                         currentDuty.getStartTime() + "-" + currentDuty.getEndTime() + ")");
                        foundConflicts = true;
                    }
                    previousDuty = currentDuty;
                }
            }
        } catch (Exception e) {
            System.out.println("Error identifying conflicts: " + e.getMessage());
        }

        if (!foundConflicts) {
            System.out.println("No scheduling conflicts detected.");
        }
    }

    private int countAvailableDaysNext7Days(Doctor doctor) {
        int count = 0;
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(6);
        SortedSkipList<LocalDate> availability = doctor.getAvailability();

        for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (availability.contains(date)) {
                count++;
            }
        }
        return count;
    }

    // Helper class for daily coverage statistics
    private class DayCoverageStats {
        int doctorCount = 0;
        double totalHours = 0;
        String coverageGaps = "None";
    }

    private void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}