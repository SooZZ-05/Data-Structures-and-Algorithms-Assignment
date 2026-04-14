package dao;

import entity.DispenseRecord;
import util.DateProvider;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */

public class DispenseRecordDao {
    private DispenseRecord[] records = new DispenseRecord[16];
    private int size = 0;
    private int seq = 1;

    public DispenseRecord startRecord(DispenseRecord.BuyerType type) {
        String id = "d" + (1000 + seq++);
//        DispenseRecord r = new DispenseRecord(id, LocalDateTime.now(), type);
        DispenseRecord r = new DispenseRecord(id, DateProvider.nowDateTime(), type);
        ensureCapacity(size + 1);
        records[size++] = r;
        return r;
    }

    public DispenseRecord[] listAll() {
        DispenseRecord[] out = new DispenseRecord[size];
        for (int i = 0; i < size; i++) out[i] = records[i];
        return out;
    }

    public DispenseRecord[] filter(LocalDate from, LocalDate to, DispenseRecord.BuyerType typeOrNull) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (accept(records[i], from, to, typeOrNull)) count++;
        }
        DispenseRecord[] out = new DispenseRecord[count];
        int k = 0;
        for (int i = 0; i < size; i++) {
            if (accept(records[i], from, to, typeOrNull)) out[k++] = records[i];
        }
        return out;
    }

    private boolean accept(DispenseRecord r, LocalDate from, LocalDate to, DispenseRecord.BuyerType typeOrNull) {
        LocalDate d = r.getTimestamp().toLocalDate();
        boolean in = (from == null || !d.isBefore(from)) &&
                     (to == null || !d.isAfter(to));
        if (!in) return false;
        return typeOrNull == null || r.getBuyerType() == typeOrNull;
    }

    private void ensureCapacity(int min) {
        if (min <= records.length) return;
        int newCap = Math.max(min, records.length * 2);
        DispenseRecord[] n = new DispenseRecord[newCap];
        for (int i = 0; i < size; i++) n[i] = records[i];
        records = n;
    }
}
