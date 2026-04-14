package control;

import adt.SortedLinkedList;
import adt.SortedListInterface;
import entity.Medicine;
import util.Comparer;
import util.IdGenerator;

import java.time.LocalDate;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */

public class MedicineMaintenance {
    private final SortedListInterface<Medicine> list =
            new SortedLinkedList<>(new Comparer<Medicine>() {
                @Override public int compare(Medicine a, Medicine b) {
                    return a.getId().compareToIgnoreCase(b.getId());
                }
            });

    public Medicine add(String name, LocalDate expiryDate, double unitPrice, int quantity) {
        String id = IdGenerator.nextId(list.size());
        Medicine med = new Medicine(id, name, expiryDate, unitPrice, quantity);
        list.add(med);
        return med;
    }

    public boolean deleteById(String id) {
        int idx = indexOfId(id);
        if (idx < 0) return false;
        list.removeAt(idx);
        for (int i = idx; i < list.size(); i++) {
            Medicine m = list.get(i);
            int newNumber = 1000 + (i + 1);
            m.setId("m" + newNumber);
        }
        return true;
    }

    public Medicine findByIdOrNull(String id) {
        int idx = indexOfId(id);
        return idx < 0 ? null : list.get(idx);
    }

    public Medicine[] searchByName(String namePart) {
        String q = namePart == null ? "" : namePart.trim().toLowerCase();
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            Medicine m = list.get(i);
            if (m.getName().toLowerCase().contains(q)) count++;
        }
        Medicine[] out = new Medicine[count];
        int k = 0;
        for (int i = 0; i < list.size(); i++) {
            Medicine m = list.get(i);
            if (m.getName().toLowerCase().contains(q)) out[k++] = m;
        }
        return out;
    }

    public boolean updateQuantity(String id, int newQty) {
        Medicine m = findByIdOrNull(id);
        if (m == null) return false;
        m.setQuantity(newQty);
        return true;
    }

    public boolean updatePrice(String id, double newPrice) {
        Medicine m = findByIdOrNull(id);
        if (m == null) return false;
        m.setUnitPrice(newPrice);
        return true;
    }

    public Medicine[] listAll() {
        Medicine[] out = new Medicine[list.size()];
        for (int i = 0; i < list.size(); i++) out[i] = list.get(i);
        return out;
    }

    public Medicine[] listAllSorted(Comparer<Medicine> by) {
        Medicine[] arr = listAll();
        for (int i = 1; i < arr.length; i++) {
            Medicine key = arr[i];
            int j = i - 1;
            while (j >= 0 && by.compare(arr[j], key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        return arr;
    }

    public Medicine[] lowStock(int threshold) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) if (list.get(i).getQuantity() < threshold) count++;
        Medicine[] out = new Medicine[count];
        int k = 0;
        for (int i = 0; i < list.size(); i++) {
            Medicine m = list.get(i);
            if (m.getQuantity() < threshold) out[k++] = m;
        }
        return out;
    }

    private int indexOfId(String id) {
        if (id == null) return -1;
        for (int i = 0; i < list.size(); i++) {
            if (id.equalsIgnoreCase(list.get(i).getId())) return i;
        }
        return -1;
    }
}
