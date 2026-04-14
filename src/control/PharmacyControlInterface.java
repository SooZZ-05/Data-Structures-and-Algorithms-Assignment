package control;

import entity.DispenseRecord;
import entity.Medicine;
import util.Comparer;

import java.time.LocalDate;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */

public interface PharmacyControlInterface {
    Medicine addMedicine(String name, LocalDate expiry, double unitPrice, int quantity);
    boolean deleteMedicine(String id);
    Medicine findMedicineOrNull(String id);
    Medicine[] searchMedicineByName(String namePart);
    boolean updateQuantity(String id, int newQty);
    boolean updatePrice(String id, double newPrice);
    Medicine[] listMedicines();
    Medicine[] listMedicinesSorted(Comparer<Medicine> by);
    Medicine[] lowStock(int threshold);
    DispenseRecord beginPersonalBuying();
    DispenseRecord beginMedicalList();
    double addItemToDispense(DispenseRecord record, String medicineId, int qty);
    double finalizeTotal(DispenseRecord record);
    DispenseRecord[] listDispenseRecords();
}