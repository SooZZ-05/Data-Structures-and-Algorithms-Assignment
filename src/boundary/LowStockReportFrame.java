package boundary;

import control.PharmacyControlInterface;
import entity.Medicine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */
public class LowStockReportFrame extends JFrame {
    private final PharmacyControlInterface control;
    private final DefaultTableModel model;
    private final JSpinner thresholdSpinner;
    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_DATE;

    public LowStockReportFrame(PharmacyControlInterface control) {
        super("Low Stock Report");
        this.control = control;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new Object[]{"ID","Name","Expiry","Unit Price","Qty","Action"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Threshold:"));
        thresholdSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
        JButton refresh = new JButton("Generate");
        top.add(thresholdSpinner);
        top.add(refresh);
        add(top, BorderLayout.NORTH);

        refresh.addActionListener(e -> reload());
        reload();
    }

    private void reload() {
        model.setRowCount(0);
        int t = (int) thresholdSpinner.getValue();
        for (Medicine m : control.lowStock(t)) {
            model.addRow(new Object[]{
                    m.getId(), m.getName(), DF.format(m.getExpiryDate()),
                    String.format("%.2f", m.getUnitPrice()), m.getQuantity(),
                    "Restock"
            });
        }
    }
}
