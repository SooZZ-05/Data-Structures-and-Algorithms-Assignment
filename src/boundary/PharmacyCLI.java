package boundary;

import control.PharmacyControlInterface;
import control.PharmacyControlImpl;
import entity.DispenseRecord;
import entity.Medicine;

import java.time.LocalDate;
import java.util.Scanner;
import control.PharmacyControlInterface;
import util.DateProvider;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */

public class PharmacyCLI {

    private final PharmacyControlInterface control = new PharmacyControlImpl();
    private final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        new PharmacyCLI().run();
    }

    private void run() {
        seedDemoData();
        while (true) {
            System.out.println("\n=== Pharmacy ===");
            System.out.println("1. Pharmacy Management");            
            System.out.println("2. Search Medicine (by id or name)");
            System.out.println("3. List Medicines");
            System.out.println("4. Reports");           
            System.out.println("5. Personal Buying");
            System.out.println("6. Set demo date (YYYY-MM-DD)");
            System.out.println("7. Reset to system date");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String ch = sc.nextLine().trim();

            try {
                switch (ch) {
                    case "1" -> {
                        System.out.println("\n--- Pharmacy Management ---");
                        System.out.println("1. Add Medicine");
                        System.out.println("2. Delete Medicine");
                        System.out.println("3. Update Quantity");
                        System.out.println("4. Update Price");
                        System.out.println("0. Back");
                        System.out.print("Choose: ");
                        String ch1 = sc.nextLine().trim();

                        switch (ch1) {
                            case "1" -> addMedicine();
                            case "2" -> deleteMedicine();
                            case "3" -> updateQty();
                            case "4" -> updatePrice();
                            case "0" -> {}
                            default -> System.out.println("Invalid choice.");
                        }
                    }
                    case "2" -> searchMedicine();
                    case "3" -> Swing.launch(() -> new MedicineListFrame(control).setVisible(true));
                    case "4" -> {
                        System.out.println("\n--- Reports ---");
                        System.out.println("1. Low Stock Report");
                        System.out.println("2. Dispensing Report");
                        System.out.println("0. Back");
                        System.out.print("Choose: ");
                        String ch2 = sc.nextLine().trim();

                        switch (ch2) {
                            case "1" -> Swing.launch(() -> new LowStockReportFrame(control).setVisible(true));
                            case "2" -> Swing.launch(() -> new DispensingReportFrame(control).setVisible(true));
                            case "0" -> {}
                            default -> System.out.println("Invalid choice.");
                        }
                    }
                    case "5" -> personalBuying();
//                    case "6" -> medicalListBuying();
                    case "6" -> setDemoDate();
                    case "7" -> resetDemoDate();
                    case "0" -> {
                        System.out.println("Bye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }


    private void seedDemoData() {
        control.addMedicine("Paracetamol", LocalDate.now().plusMonths(6), 2.50, 15);
        control.addMedicine("Ibuprofen", LocalDate.now().plusMonths(12), 3.80, 8);   // low stock
        control.addMedicine("Aspirin", LocalDate.now().plusMonths(9), 4.20, 25);
        control.addMedicine("Cough Syrup", LocalDate.now().plusMonths(3), 7.90, 5);  // low stock
    }

    private void addMedicine() {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Expiry (YYYY-MM-DD): ");
        LocalDate exp = LocalDate.parse(sc.nextLine().trim());
        System.out.print("Unit price: ");
        double price = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Quantity: ");
        int qty = Integer.parseInt(sc.nextLine().trim());
        Medicine m = control.addMedicine(name, exp, price, qty);
        System.out.println("Added: " + m);
    }

    private void deleteMedicine() {
        System.out.print("ID to delete: ");
        String id = sc.nextLine().trim();
        boolean ok = control.deleteMedicine(id);
        System.out.println(ok ? "Deleted." : "Not found.");
    }

    private void setDemoDate() {
        System.out.print("Demo date (YYYY-MM-DD): ");
        String s = sc.nextLine().trim();
        try {
            LocalDate d = LocalDate.parse(s);
            DateProvider.setFixedDate(d);
            System.out.println("Demo date set to " + d + ". Today() is now " + DateProvider.today());
        } catch (Exception e) {
            System.out.println("Invalid date. Use format YYYY-MM-DD.");
        }
    }

    private void resetDemoDate() {
        DateProvider.resetSystemDate();
        System.out.println("Back to system date. Today() is " + DateProvider.today());
    }
    
    private void searchMedicine() {
        System.out.print("Search by (1) ID or (2) Name? ");
        String opt = sc.nextLine().trim();

        switch (opt) {
            case "1" -> {
                System.out.print("ID: ");
                Medicine m = control.findMedicineOrNull(sc.nextLine().trim());
                if (m != null) {
                    System.out.println(m);
                } else {
                    System.out.println("Not found");
                }
            }
            case "2" -> {
                System.out.print("Name contains: ");
                Medicine[] list = control.searchMedicineByName(sc.nextLine().trim());
                if (list.length == 0) {
                    System.out.println("No matches.");
                } else {
                    for (Medicine m : list) {
                        System.out.println(m);
                    }
                }
            }
            default -> {
                System.out.println("Invalid choice. Returning to menu...");
            }
        }
    }

    private void updateQty() {
        System.out.print("ID: ");
        String id = sc.nextLine().trim();
        System.out.print("New quantity: ");
        int qty = Integer.parseInt(sc.nextLine().trim());
        System.out.println(control.updateQuantity(id, qty) ? "Updated." : "Not found.");
    }

    private void updatePrice() {
        System.out.print("ID: ");
        String id = sc.nextLine().trim();
        System.out.print("New price: ");
        double p = Double.parseDouble(sc.nextLine().trim());
        System.out.println(control.updatePrice(id, p) ? "Updated." : "Not found.");
    }

    private void personalBuying() {
        double total = 0.0;
        System.out.print("Medicine ID (blank or 0 to cancel): ");
        String id = sc.nextLine().trim();
        if (id.isEmpty() || id.equals("0")) {
            System.out.println("No items entered. Personal buying cancelled.");
            return;
        }

        DispenseRecord r = control.beginPersonalBuying();
        while (true) {
            // quantity for the current id
            System.out.print("Qty: ");
            String qtyStr = sc.nextLine().trim();
            int q;
            try {
                q = Integer.parseInt(qtyStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Skipped.");
                System.out.print("Medicine ID (blank or 0 to finish): ");
                id = sc.nextLine().trim();
                if (id.isEmpty() || id.equals("0")) break;
                continue;
            }
            if (q <= 0) {
                System.out.println("Quantity must be a positive integer. Skipped.");
            } else {
                double newTotal = control.addItemToDispense(r, id, q);
                if (newTotal < 0) {
                    System.out.println("Error: invalid medicine ID or insufficient stock. Not added.");
                } else {
                    total = newTotal;
                    System.out.printf("Added. Current total = RM %.2f%n", total);
                }
            }
            System.out.print("Medicine ID (blank or 0 to finish): ");
            id = sc.nextLine().trim();
            if (id.isEmpty() || id.equals("0")) break;
        }

        System.out.printf("Final total: RM %.2f%n", control.finalizeTotal(r));
    }

    private void medicalListBuying() {
        System.out.println("(Module4) Enter medicine lines (ID and quantity). Enter 0 or blank to finish.");

        DispenseRecord r = null;
        double total = 0.0;
        System.out.print("Medicine ID (0 or blank to cancel): ");
        String id = sc.nextLine().trim();
        if (id.isEmpty() || id.equals("0")) {
            System.out.println("No items entered. Medical list cancelled.");
            return;
        }
        r = control.beginMedicalList();

        while (true) {
            System.out.print("Qty: ");
            int q;
            try {
                q = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Skipped.");
                // ask for next ID
                System.out.print("Medicine ID (0 or blank to finish): ");
                id = sc.nextLine().trim();
                if (id.isEmpty() || id.equals("0")) break;
                continue;
            }
            if (q <= 0) {
                System.out.println("Quantity must be positive. Skipped.");
            } else {
                double newTotal = control.addItemToDispense(r, id, q);
                if (newTotal < 0) {
                    System.out.println("Error: invalid ID or insufficient stock. Not added.");
                } else {
                    total = newTotal;
                    System.out.printf("Added. Current total = RM %.2f%n", total);
                }
            }

            System.out.print("Medicine ID (0 or blank to finish): ");
            id = sc.nextLine().trim();
            if (id.isEmpty() || id.equals("0")) break;
        }

        if (r != null) {
            System.out.printf("Final total (MEDICAL LIST): RM %.2f%n", control.finalizeTotal(r));
        }
    }

    private static class Swing {
        static void launch(Runnable r) {
            javax.swing.SwingUtilities.invokeLater(r);
        }
    }
}
