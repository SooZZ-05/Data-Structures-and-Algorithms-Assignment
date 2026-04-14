package entity;

/**
 *
 * @author Low Zi Qing
 */
public class ReportEntry {
    private String period; // could be date, week, or month
    private int count;

    public ReportEntry(String period) {
        this.period = period;
        this.count = 1;
    }

    public String getPeriod() {
        return period;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        count++;
    }
}
