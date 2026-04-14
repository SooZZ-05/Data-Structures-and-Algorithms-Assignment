package entity;

import util.Comparer;

import java.time.LocalDate;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */

public class Medicine {
    private String id;              // e.g. m1001
    private String name;
    private LocalDate expiryDate;
    private double unitPrice;
    private int quantity;

    public Medicine(String id, String name, LocalDate expiryDate, double unitPrice, int quantity) {
        if (id == null || name == null || expiryDate == null) throw new NullPointerException();
        this.id = id;
        this.name = name;
        this.expiryDate = expiryDate;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isExpired(java.time.LocalDate today) {
        return !expiryDate.isAfter(today);
    }

    @Override public String toString() {
        return "Medicine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", expiryDate=" + expiryDate +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }

    // Custom comparers (no java.util.Comparator)
    public static final Comparer<Medicine> BY_ID = new Comparer<Medicine>() {
        @Override public int compare(Medicine a, Medicine b) {
            return a.id.compareToIgnoreCase(b.id);
        }
    };
    public static final Comparer<Medicine> BY_NAME = new Comparer<Medicine>() {
        @Override public int compare(Medicine a, Medicine b) {
            return a.name.compareToIgnoreCase(b.name);
        }
    };
    public static final Comparer<Medicine> BY_PRICE = new Comparer<Medicine>() {
        @Override public int compare(Medicine a, Medicine b) {
            return Double.compare(a.unitPrice, b.unitPrice);
        }
    };
    public static final Comparer<Medicine> BY_QTY = new Comparer<Medicine>() {
        @Override public int compare(Medicine a, Medicine b) {
            return Integer.compare(a.quantity, b.quantity);
        }
    };
    public static final Comparer<Medicine> BY_EXPIRY = new Comparer<Medicine>() {
        @Override public int compare(Medicine a, Medicine b) {
            if (a.expiryDate.isEqual(b.expiryDate)) return 0;
            return a.expiryDate.isBefore(b.expiryDate) ? -1 : 1;
        }
    };
}
