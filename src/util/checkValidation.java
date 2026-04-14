package util;

/**
 *
 * @author Gan Jun Wei
 */

import java.util.Scanner;
import java.util.regex.Pattern;

public class checkValidation {
    private static final Scanner scanner = new Scanner(System.in);
    
    // Regex patterns
    private static final Pattern PATIENT_ID_PATTERN = Pattern.compile("^P\\d{4}$");
    private static final Pattern TREATMENT_ID_PATTERN = Pattern.compile("^T\\d+$");
    private static final Pattern MEDICINE_ID_PATTERN = Pattern.compile("^M\\d{3}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");
    private static final Pattern DIAGNOSIS_PATTERN = Pattern.compile("^[a-zA-Z\\s\\-]{2,100}$");
    private static final Pattern NOTES_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s\\.,!?\\-]{0,500}$");
    
    // Numeric validation
    public static int getValidInt(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max + "!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }
    
    public static double getValidDouble(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a value between " + min + " and " + max + "!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }
    
    // String validation with regex
    public static String getValidString(String prompt, Pattern pattern, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty() && pattern.matcher(input).matches()) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }
    
    // Specific validation methods
    public static String getValidPatientId() {
        return getValidString("Enter Patient ID (PXXXX): ", 
            PATIENT_ID_PATTERN, 
            "Invalid Patient ID! Format: P followed by 4 digits (e.g., P0001)");
    }
    
    public static String getValidTreatmentId() {
        return getValidString("Enter Treatment ID: ", 
            TREATMENT_ID_PATTERN, 
            "Invalid Treatment ID! Format: T followed by numbers (e.g., T123)");
    }
    
    public static String getValidMedicineId() {
        return getValidString("Enter Medicine ID (MXXX): ", 
            MEDICINE_ID_PATTERN, 
            "Invalid Medicine ID! Format: M followed by 3 digits (e.g., M001)");
    }
    
    public static String getValidName(String type) {
        return getValidString("Enter " + type + " Name: ", 
            NAME_PATTERN, 
            "Invalid name! Only letters and spaces (2-50 characters)");
    }
    
    public static String getValidDiagnosis() {
        return getValidString("Enter Diagnosis: ", 
            DIAGNOSIS_PATTERN, 
            "Invalid diagnosis! Only letters, spaces, and hyphens (2-100 characters)");
    }
    
    public static String getValidNotes() {
        return getValidString("Enter Doctor Notes (optional): ", 
            NOTES_PATTERN, 
            "Notes too long! Maximum 500 characters allowed.");
    }
    
    // Yes/No validation
    public static boolean getYesNoConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (Y/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("Y") || input.equals("YES")) {
                return true;
            } else if (input.equals("N") || input.equals("NO")) {
                return false;
            }
            System.out.println("Please enter 'Y' for Yes or 'N' for No!");
        }
    }
    
    // Empty input validation
    public static String getNonEmptyString(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }
    
    // Date validation (simple version)
    public static String getValidDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD): ");
            String date = scanner.nextLine().trim();
            if (isValidDate(date)) {
                return date;
            }
            System.out.println("Invalid date format! Please use YYYY-MM-DD.");
        }
    }
    
    private static boolean isValidDate(String date) {
        return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }
}