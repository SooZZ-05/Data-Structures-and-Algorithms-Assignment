package boundary;

import control.PharmacyControlInterface;
import entity.DispenseRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */

public class DispensingReportFrame extends JFrame {
    private final PharmacyControlInterface control;
    private final DefaultTableModel model;
    private final JComboBox<String> typeBox;
    private final JFormattedTextField fromDate;
    private final JFormattedTextField toDate;
    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_DATE;

    public DispensingReportFrame(PharmacyControlInterface control) {
        super("Dispensing Report");
        this.control = control;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 500);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new Object[]{"Record ID","Timestamp","Buyer Type","Items","Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typeBox = new JComboBox<>(new String[]{"All","Personal Buying","Medical List"});
        fromDate = new JFormattedTextField(DF.toFormat());
        fromDate.setColumns(10);
        toDate = new JFormattedTextField(DF.toFormat());
        toDate.setColumns(10);

        JButton refresh = new JButton("Generate");
        top.add(new JLabel("Type:"));
        top.add(typeBox);
        top.add(new JLabel("From (YYYY-MM-DD):"));
        top.add(fromDate);
        top.add(new JLabel("To:"));
        top.add(toDate);
        top.add(refresh);
        add(top, BorderLayout.NORTH);

        refresh.addActionListener(e -> reload());

        reload();
    }

    private void reload() {
        model.setRowCount(0);
        LocalDate from = parseDate(fromDate.getText());
        LocalDate to = parseDate(toDate.getText());
        DispenseRecord.BuyerType type = switch (typeBox.getSelectedIndex()) {
            case 1 -> DispenseRecord.BuyerType.PERSONAL_BUYING;
            case 2 -> DispenseRecord.BuyerType.MEDICAL_LIST;
            default -> null;
        };

        for (DispenseRecord r : control.listDispenseRecords()) {
            LocalDate d = r.getTimestamp().toLocalDate();
            if (from != null && d.isBefore(from)) continue;
            if (to != null && d.isAfter(to)) continue;
            if (type != null && r.getBuyerType() != type) continue;

            String items = "";
            var arr = r.getItems();
            for (int i = 0; i < arr.length; i++) {
                var it = arr[i];
                String part = it.medicineId + " x" + it.quantity + " RM" + String.format("%.2f", it.unitPrice);
                if (items.isEmpty()) items = part; else items = items + "; " + part;
            }
            model.addRow(new Object[]{
                r.getRecordId(),
                r.getTimestamp().toString(),
                r.getBuyerType().name(),
                items,
                String.format("%.2f", r.total())
            });
        }
    }

    private LocalDate parseDate(String s) {
        try {
            if (s == null || s.isBlank()) return null;
            return LocalDate.parse(s.trim(), DF);
        } catch (Exception e) {
            return null;
        }
    }
}
