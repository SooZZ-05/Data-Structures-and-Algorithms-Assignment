package entity;

import java.time.LocalDateTime;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */

public class DispenseRecord {
    public enum BuyerType { PERSONAL_BUYING, MEDICAL_LIST }

    public static final class Item {
        public final String medicineId;
        public final String medicineName;
        public final double unitPrice;
        public final int quantity;
        public Item(String medicineId, String medicineName, double unitPrice, int quantity) {
            this.medicineId = medicineId;
            this.medicineName = medicineName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }
        public double lineTotal() { return unitPrice * quantity; }
    }

    private final String recordId;
    private final LocalDateTime timestamp;
    private final BuyerType buyerType;

    private Item[] items = new Item[8];
    private int size = 0;

    public DispenseRecord(String recordId, LocalDateTime timestamp, BuyerType buyerType) {
        if (recordId == null || timestamp == null || buyerType == null) throw new NullPointerException();
        this.recordId = recordId;
        this.timestamp = timestamp;
        this.buyerType = buyerType;
    }

    public String getRecordId() { return recordId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public BuyerType getBuyerType() { return buyerType; }

    public void addItem(Item item) {
        ensure(size + 1);
        items[size++] = item;
    }

    public Item[] getItems() {
        Item[] out = new Item[size];
        for (int i = 0; i < size; i++) out[i] = items[i];
        return out;
    }

    public double total() {
        double t = 0.0;
        for (int i = 0; i < size; i++) t += items[i].lineTotal();
        return t;
    }

    private void ensure(int min) {
        if (min <= items.length) return;
        int nc = Math.max(min, items.length * 2);
        Item[] n = new Item[nc];
        for (int i = 0; i < size; i++) n[i] = items[i];
        items = n;
    }
}
