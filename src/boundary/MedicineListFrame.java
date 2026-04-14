package boundary;

import control.PharmacyControlInterface;
import entity.Medicine;
import util.Comparer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import util.DateProvider;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */

public class MedicineListFrame extends JFrame {
    private final PharmacyControlInterface control;
    private final DefaultTableModel model;
    private final JTable table;
    private final JComboBox<String> sortBox;

    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_DATE;

    public MedicineListFrame(PharmacyControlInterface control) {
        super("Medicine List");
        this.control = control;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

//        model = new DefaultTableModel(new Object[]{"ID", "Name", "Expiry", "Unit Price", "Qty"}, 0) {
//            @Override public boolean isCellEditable(int r, int c) { return false; }
//        };
        model = new DefaultTableModel(new Object[]{"ID", "Name", "Expiry", "Unit Price", "Qty", "Expired"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortBox = new JComboBox<>(new String[]{"ID", "Name", "Unit Price", "Quantity", "Expiry Date"});
        JButton refresh = new JButton("Refresh");
        top.add(new JLabel("Sort by:"));
        top.add(sortBox);
        top.add(refresh);
        add(top, BorderLayout.NORTH);

        refresh.addActionListener(e -> reload());
        sortBox.addActionListener(e -> reload());

        reload();
    }

    private void reload() {
        model.setRowCount(0);
        Comparer<Medicine> cmp;
        switch (sortBox.getSelectedIndex()) {
            case 0: cmp = Medicine.BY_ID; break;
            case 1: cmp = Medicine.BY_NAME; break;
            case 2: cmp = Medicine.BY_PRICE; break;
            case 3: cmp = Medicine.BY_QTY; break;
            case 4: cmp = Medicine.BY_EXPIRY; break;
            default: cmp = Medicine.BY_ID;
        }
        Medicine[] meds = control.listMedicinesSorted(cmp);
//        for (Medicine m : meds) {
//            model.addRow(new Object[]{
//                    m.getId(),
//                    m.getName(),
//                    DF.format(m.getExpiryDate()),
//                    String.format("%.2f", m.getUnitPrice()),
//                    m.getQuantity()
//            });
//        }
        for (Medicine m : meds) {
            boolean expired = m.isExpired(DateProvider.today());
            model.addRow(new Object[]{
                m.getId(),
                m.getName(),
                DF.format(m.getExpiryDate()),
                String.format("%.2f", m.getUnitPrice()),
                m.getQuantity(),
                expired ? "Yes" : "No"
            });
        }
    }
}
