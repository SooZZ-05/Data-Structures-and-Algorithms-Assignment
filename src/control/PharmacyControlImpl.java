package control;

import dao.DispenseRecordDao;
import entity.DispenseRecord;
import entity.Medicine;
import util.Comparer;
import util.DateProvider;

import java.time.LocalDate;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */

public class PharmacyControlImpl implements PharmacyControlInterface {
    private final MedicineMaintenance meds = new MedicineMaintenance();
    private final DispenseRecordDao sales = new DispenseRecordDao();

    @Override
    public Medicine addMedicine(String name, LocalDate expiry, double unitPrice, int quantity) {
        return meds.add(name, expiry, unitPrice, quantity);
    }

    @Override
    public boolean deleteMedicine(String id) { return meds.deleteById(id); }

    @Override
    public Medicine findMedicineOrNull(String id) { return meds.findByIdOrNull(id); }

    @Override
    public Medicine[] searchMedicineByName(String namePart) { return meds.searchByName(namePart); }

    @Override
    public boolean updateQuantity(String id, int newQty) { return meds.updateQuantity(id, newQty); }

    @Override
    public boolean updatePrice(String id, double newPrice) { return meds.updatePrice(id, newPrice); }

    @Override
    public Medicine[] listMedicines() { return meds.listAll(); }

    @Override
    public Medicine[] listMedicinesSorted(Comparer<Medicine> by) { return meds.listAllSorted(by); }

    @Override
    public Medicine[] lowStock(int threshold) { return meds.lowStock(threshold); }

    @Override
    public DispenseRecord beginPersonalBuying() { return sales.startRecord(DispenseRecord.BuyerType.PERSONAL_BUYING); }

    @Override
    public DispenseRecord beginMedicalList() { return sales.startRecord(DispenseRecord.BuyerType.MEDICAL_LIST); }

    @Override
    public double addItemToDispense(DispenseRecord record, String medicineId, int qty) {
        Medicine m = meds.findByIdOrNull(medicineId);
        if (m == null) throw new IllegalArgumentException("No medicine " + medicineId);
        if (qty <= 0) throw new IllegalArgumentException("qty must be positive");
        if (m.isExpired(DateProvider.today())) {
            throw new IllegalArgumentException(
                "Medicine " + m.getId() + " (" + m.getName() + ") expired on " + m.getExpiryDate());
        }
        if (m.getQuantity() < qty) throw new IllegalArgumentException("Insufficient stock");
        record.addItem(new DispenseRecord.Item(m.getId(), m.getName(), m.getUnitPrice(), qty));
        meds.updateQuantity(medicineId, m.getQuantity() - qty);
        return record.total();
    }

    @Override
    public double finalizeTotal(DispenseRecord record) { return record.total(); }

    @Override
    public DispenseRecord[] listDispenseRecords() { 
        return sales.listAll(); 
    }
}
