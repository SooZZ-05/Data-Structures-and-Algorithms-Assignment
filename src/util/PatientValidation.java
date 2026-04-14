package util;

/** Author: Teo Geok Woon */

import java.util.Scanner;
import java.util.regex.Pattern;

public final class PatientValidation {
    private PatientValidation() {}

    // Patterns
    public static final Pattern NAME = Pattern.compile("^[A-Za-z ]+$");
    public static final Pattern IC = Pattern.compile("^\\d{12}$");
    public static final Pattern PHONE = Pattern.compile("^01\\d{8}$"); // 10 digits, starts with 01
    public static final Pattern PATIENT_ID = Pattern.compile("^P\\d{4}$"); // e.g. P0001

    // ---------- Basic checks ----------
    public static boolean isBlank(String s)            { return s == null || s.trim().isEmpty(); }
    public static boolean isNonEmpty(String s)         { return !isBlank(s); }

    public static boolean isValidName(String s)        { return isNonEmpty(s) && NAME.matcher(s.trim()).matches(); }
    public static boolean isValidIC(String s)          { return isNonEmpty(s) && IC.matcher(s.trim()).matches(); }
    public static boolean isValidPhone(String s)       { return isNonEmpty(s) && PHONE.matcher(s.trim()).matches(); }
    public static boolean isValidPatientId(String s)   { return isNonEmpty(s) && PATIENT_ID.matcher(s.trim()).matches(); }

    public static boolean isValidGender(String s) {
        if (isBlank(s)) return false;
        String t = s.trim().toLowerCase();
        return t.equals("male") || t.equals("female");
    }

    public static boolean isValidAppointmentType(String s) {
        if (isBlank(s)) return false;
        String t = s.trim().toLowerCase();
        return t.equals("walk-in") || t.equals("walkin") || t.equals("appointment");
    }

    public static boolean isValidAge(String s) {
        if (isBlank(s)) return false;
        try {
            int a = Integer.parseInt(s.trim());
            return a > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isYesNo(String s) {
        if (isBlank(s)) return false;
        String t = s.trim().toLowerCase();
        return t.equals("y") || t.equals("n") || t.equals("yes") || t.equals("no");
    }

    // ---------- Normalizers / converters ----------
    public static String normalizeGender(String s) {
        if (!isValidGender(s)) return s;
        return s.trim().equalsIgnoreCase("male") ? "Male" : "Female";
    }

    public static String normalizeAppointmentType(String s) {
        if (!isValidAppointmentType(s)) return s;
        String t = s.trim().toLowerCase();
        return t.startsWith("walk") ? "Walk-in" : "Appointment";
    }

    public static int parseAge(String s) { return Integer.parseInt(s.trim()); }
    public static boolean toBooleanYesNo(String s) {
        String t = s.trim().toLowerCase();
        return t.equals("y") || t.equals("yes");
    }

    public static String readNonEmpty(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine();
            if (isNonEmpty(s)) return s.trim();
            System.out.println("Input cannot be empty.");
        }
    }

    public static String readName(Scanner sc) {
        while (true) {
            System.out.print("Enter patient name: ");
            String s = sc.nextLine().trim();
            if (isValidName(s)) return s;
            System.out.println("Invalid name! Only letters and spaces are allowed.");
        }
    }

    public static String readIC(Scanner sc) {
        while (true) {
            System.out.print("Enter IC number (without dash): ");
            String s = sc.nextLine().trim();
            if (isValidIC(s)) return s;
            System.out.println("Invalid IC! Must be 12 digits.");
        }
    }

    public static int readAge(Scanner sc) {
        while (true) {
            System.out.print("Enter patient age: ");
            String s = sc.nextLine().trim();
            if (isValidAge(s)) return parseAge(s);
            System.out.println("Invalid age! Enter a positive integer.");
        }
    }

    public static String readGender(Scanner sc) {
        while (true) {
            System.out.print("Enter gender (Male/Female): ");
            String s = sc.nextLine().trim();
            if (isValidGender(s)) return normalizeGender(s);
            System.out.println("Invalid input. Please enter Male or Female.");
        }
    }

    public static String readPhone(Scanner sc) {
        while (true) {
            System.out.print("Enter patient phone number (01xxxxxxxx): ");
            String s = sc.nextLine().trim();
            if (isValidPhone(s)) return s;
            System.out.println("Invalid phone! Must be 10 digits starting with 01.");
        }
    }

    public static String readAppointmentType(Scanner sc) {
        while (true) {
            System.out.print("Appointment type (Walk-in/Appointment): ");
            String s = sc.nextLine().trim();
            if (isValidAppointmentType(s)) return normalizeAppointmentType(s);
            System.out.println("Invalid type. Please enter Walk-in or Appointment.");
        }
    }

    public static boolean readEmergency(Scanner sc) {
        while (true) {
            System.out.print("Emergency case? (y/n): ");
            String s = sc.nextLine().trim();
            if (isYesNo(s)) return toBooleanYesNo(s);
            System.out.println("Please enter y or n.");
        }
    }

    public static String readPatientId(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (isValidPatientId(s)) return s;
            System.out.println("Invalid Patient ID format. Example: P0001");
        }
    }
}
