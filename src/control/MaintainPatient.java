package control;

/** Author: Teo Geok Woon */

import adt.ArrayQueue;
import adt.LinkedList;
import adt.ListInterface;
import entity.Patient;
import dao.PatientDAO;
import dao.ActivityDAO;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaintainPatient {
    public static class Pair<K,V> implements Serializable {
        private static final long serialVersionUID = 1L;
        public final K key; public V value;
        public Pair(K k, V v) { key = k; value = v; }
    }
    public static class DateWaits implements Serializable {
        private static final long serialVersionUID = 1L;
        public LocalDate date;
        public ListInterface<Long> waits = new LinkedList<>();
        public DateWaits(LocalDate d) { date = d; }
    }

    // ---------- data ----------
    private ListInterface<Patient> records = new LinkedList<>();
    private final ListInterface<Registration> registrations = new LinkedList<>();
    private final ListInterface<DeletionEvent> deletions = new LinkedList<>();
    private final ListInterface<Pair<String, LocalDateTime>> queueEnqueueTimes = new LinkedList<>();
    private final ListInterface<DateWaits> waitHistoryByDate = new LinkedList<>();
    private final ListInterface<QueueEvent> queueEvents = new LinkedList<>();

    private final ArrayQueue<Patient> waitingQueue = new ArrayQueue<>();
    private final ArrayQueue<Patient> emergencyQueue = new ArrayQueue<>();
    private final PatientDAO patientDAO = new dao.PatientDAO();
    private final ActivityDAO activityDAO = new ActivityDAO();
    private int idCounter = 1;

    public ListInterface<Registration> getRegistrations() { return registrations; }
    public ListInterface<QueueEvent>   getQueueEvents()  { return queueEvents; }
    public ListInterface<DeletionEvent> getDeletions()   { return deletions; }
    public ListInterface<Pair<String, LocalDateTime>> getQueueEnqueueTimesList() { return queueEnqueueTimes; }
    public ListInterface<DateWaits> getWaitHistoryByDateList() { return waitHistoryByDate; }

    public MaintainPatient() {
        records = patientDAO.retrieveFromFile();
        reseedIdCounterFromRecords();
        loadActivityFromDisk();
    }

    public static <T> int sizeOf(ArrayQueue<T> q) {
        int n = 0;
        java.util.Iterator<T> it = q.getIterator();
        while (it.hasNext()) { it.next(); n++; }
        return n;
    }

    private static <T> boolean queueContains(ArrayQueue<T> q, T target) {
        java.util.Iterator<T> it = q.getIterator();
        while (it.hasNext()) if (java.util.Objects.equals(it.next(), target)) return true;
        return false;
    }

    private static <T> boolean removeFromQueue(ArrayQueue<T> q, T target) {
        int n = sizeOf(q);
        if (n == 0) return false;
        ArrayQueue<T> tmp = new ArrayQueue<>(n);
        boolean removed = false;
        while (!q.isEmpty()) {
            T x = q.dequeue();
            if (!removed && java.util.Objects.equals(x, target)) removed = true;
            else tmp.enqueue(x);
        }
        while (!tmp.isEmpty()) q.enqueue(tmp.dequeue());
        return removed;
    }

    public static class Registration implements Serializable {
        private static final long serialVersionUID = 1L;
        public final String patientId, name, ic, phone, address, notes, gender, department, appointmentType;
        public final int age;
        public final boolean emergency;
        public final LocalDateTime time;
        Registration(Patient p) {
            this.patientId = p.getPatientId();
            this.name = p.getName();
            this.ic = p.getIc();
            this.age = p.getAge();
            this.phone = p.getPhone();
            this.address = p.getAddress();
            this.notes = p.getNotes();
            this.gender = p.getGender();
            this.department = p.getDepartment();
            this.appointmentType = p.getAppointmentType();
            this.emergency = p.getEmergency();
            this.time = LocalDateTime.now();
        }
    }
    
    public boolean addPatient(Patient p) {
        if (p == null) return false;
        if (icExists(p.getIc())) return false;
        if (records.contains(p)) return false;
        boolean ok = records.add(p);
        if (ok) {
            registrations.add(new Registration(p));
            saveNow();
            saveActivity();
        }
        return ok;
    }

    public enum UpdateField { NAME, IC, AGE, PHONE, ADDRESS, NOTES, GENDER, DEPARTMENT, APPOINTMENT_TYPE, EMERGENCY }

    public boolean updateField(String patientId, UpdateField field, Object value) {
        Patient p = findPatientById(patientId);
        if (p == null || field == null) return false;
        switch (field) {
            case NAME:         if (value instanceof String) p.setName((String) value); else return false; break;
            case IC:           if (value instanceof String) { String newIc = (String) value;
                               if (icExistsForOther(newIc, patientId)) return false; p.setIc(newIc); } else return false; break;
            case AGE:          if (value instanceof Integer) p.setAge((Integer) value); else return false; break;
            case PHONE:        if (value instanceof String) p.setPhone((String) value); else return false; break;
            case ADDRESS:      if (value instanceof String) p.setAddress((String) value); else return false; break;
            case NOTES:        if (value instanceof String) p.setNotes((String) value); else return false; break;
            case GENDER:       if (value instanceof String) p.setGender((String) value); else return false; break;
            case DEPARTMENT:   if (value instanceof String) p.setDepartment((String) value); else return false; break;
            case APPOINTMENT_TYPE:
                               if (value instanceof String) p.setAppointmentType((String) value); else return false; break;
            case EMERGENCY:    if (value instanceof Boolean) p.setEmergency((Boolean) value); else return false; break;
            default: return false;
        }
        saveNow();
        return true;
    }

    public boolean updatePatient(String patientId, String name, String ic, Integer age, String phone, String address,
                                 String notes, String gender, String department, String appointmentType, Boolean emergency) {
        boolean changed = false;
        if (name != null)        changed |= updateField(patientId, UpdateField.NAME, name);
        if (ic != null)          changed |= updateField(patientId, UpdateField.IC, ic);
        if (age != null)         changed |= updateField(patientId, UpdateField.AGE, age);
        if (phone != null)       changed |= updateField(patientId, UpdateField.PHONE, phone);
        if (address != null)     changed |= updateField(patientId, UpdateField.ADDRESS, address);
        if (notes != null)       changed |= updateField(patientId, UpdateField.NOTES, notes);
        if (gender != null)      changed |= updateField(patientId, UpdateField.GENDER, gender);
        if (department != null)  changed |= updateField(patientId, UpdateField.DEPARTMENT, department);
        if (appointmentType != null) changed |= updateField(patientId, UpdateField.APPOINTMENT_TYPE, appointmentType);
        if (emergency != null)   changed |= updateField(patientId, UpdateField.EMERGENCY, emergency);
        return changed;
    }

    public boolean deletePatient(String patientId) {
        Patient p = findPatientById(patientId);
        if (p == null) return false;
        removeFromQueue(emergencyQueue, p);
        removeFromQueue(waitingQueue, p);
        timeRemove(p.getPatientId());
        boolean removed = records.remove(p);
        if (removed) {
            deletions.add(new DeletionEvent(patientId, LocalDateTime.now()));
            saveNow();
            saveActivity();
        }
        return removed;
    }

    public static class QueueEvent implements Serializable {
        private static final long serialVersionUID = 1L;
        public final String patientId;
        public final LocalDateTime time;
        QueueEvent(String patientId, LocalDateTime time) { this.patientId = patientId; this.time = time; }
    }

    public static class DeletionEvent implements Serializable {
        private static final long serialVersionUID = 1L;
        public final String patientId;
        public final LocalDateTime time;
        DeletionEvent(String patientId, LocalDateTime time) { this.patientId = patientId; this.time = time; }
    }

    private void reseedIdCounterFromRecords() {
        int max = 0;
        for (int i = 0; i < records.size(); i++) {
            Patient p = records.get(i);
            if (p == null) continue;
            String id = p.getPatientId();
            if (id != null && id.matches("^P\\d{4}$")) {
                int n = Integer.parseInt(id.substring(1));
                if (n > max) max = n;
            }
        }
        idCounter = max + 1;
    }

    public String generatePatientId() {
        String id;
        do { id = String.format("P%04d", idCounter++); } while (findPatientById(id) != null);
        return id;
    }

    private void logRegistration(Patient p) { registrations.add(new Registration(p)); saveActivity(); }

    public Patient findPatientById(String patientId) {
        for (int i = 0; i < records.size(); i++) {
            Patient p = records.get(i);
            if (p != null && p.getPatientId().equals(patientId)) return p;
        }
        return null;
    }

    private boolean icExistsForOther(String ic, String selfId) {
        if (ic == null) return false;
        String key = ic.replaceAll("[^0-9]", "");
        for (int i = 0; i < records.size(); i++) {
            Patient p = records.get(i);
            if (p == null || p.getIc() == null) continue;
            String id = p.getPatientId();
            if (selfId != null && selfId.equals(id)) continue;
            String icDigits = p.getIc().replaceAll("[^0-9]", "");
            if (icDigits.equals(key)) return true;
        }
        return false;
    }

    public ListInterface<Patient> listAllPatients() {
        records = patientDAO.retrieveFromFile();
        return records;
    }

    public boolean enqueuePatient(String patientId) {
        Patient p = findPatientById(patientId);
        if (p == null) return false;
        if (queueContains(waitingQueue, p) || queueContains(emergencyQueue, p)) return false;

        if (p.getEmergency()) emergencyQueue.enqueue(p);
        else waitingQueue.enqueue(p);

        LocalDateTime now = LocalDateTime.now();
        timePut(p.getPatientId(), now);
        queueEvents.add(new QueueEvent(p.getPatientId(), now));
        saveActivity();
        return true;
    }

    public Patient dequeuePatient() {
        Patient served = null;
        if (!emergencyQueue.isEmpty()) served = emergencyQueue.dequeue();
        else if (!waitingQueue.isEmpty()) served = waitingQueue.dequeue();
        else return null;

        LocalDateTime enq = timeGet(served.getPatientId());
        timeRemove(served.getPatientId());
        if (enq != null) {
            long ms = Duration.between(enq, LocalDateTime.now()).toMillis();
            LocalDate day = LocalDate.now();
            waitsFor(day).add(ms);
            saveActivity();
        }
        return served;
    }

    public Patient peekQueue() {
        if (!emergencyQueue.isEmpty()) return emergencyQueue.getFront();
        if (!waitingQueue.isEmpty()) return waitingQueue.getFront();
        return null;
    }

    public ArrayQueue<Patient> getEmergencyQueueSnapshot() {
        ArrayQueue<Patient> snap = new ArrayQueue<>();
        java.util.Iterator<Patient> it = emergencyQueue.getIterator();
        while (it.hasNext()) snap.enqueue(it.next());
        return snap;
    }

    public ArrayQueue<Patient> getNormalQueueSnapshot() {
        ArrayQueue<Patient> snap = new ArrayQueue<>();
        java.util.Iterator<Patient> it = waitingQueue.getIterator();
        while (it.hasNext()) snap.enqueue(it.next());
        return snap;
    }

    public ArrayQueue<Patient> searchByIc(String query) {
        ArrayQueue<Patient> result = new ArrayQueue<>();
        if (query == null) return result;
        String key = query.trim().replaceAll("[^0-9]", "");
        if (key.isEmpty()) return result;
        for (int i = 0; i < records.size(); i++) {
            Patient p = records.get(i);
            if (p == null) continue;
            String ic = p.getIc();
            if (ic == null) continue;
            String icDigits = ic.replaceAll("[^0-9]", "");
            if (icDigits.contains(key)) result.enqueue(p);
        }
        return result;
    }

    public Patient findPatientByIcExact(String ic) {
        if (ic == null) return null;
        String key = ic.replaceAll("[^0-9]", "");
        for (int i = 0; i < records.size(); i++) {
            Patient p = records.get(i);
            if (p == null || p.getIc() == null) continue;
            String icDigits = p.getIc().replaceAll("[^0-9]", "");
            if (icDigits.equals(key)) return p;
        }
        return null;
    }

    public void logReturningRegistration(String patientId) {
        Patient p = findPatientById(patientId);
        if (p != null) registrations.add(new Registration(p));
    }

    public boolean icExists(String ic) { return findPatientByIcExact(ic) != null; }

    private void loadActivityFromDisk() {
        ActivityDAO.ActivitySnapshot snap = activityDAO.retrieveFromFile();

        this.registrations.clear();
        for (int i = 0; i < snap.registrations.size(); i++) this.registrations.add(snap.registrations.get(i));

        this.deletions.clear();
        for (int i = 0; i < snap.deletions.size(); i++) this.deletions.add(snap.deletions.get(i));

        this.queueEvents.clear();
        for (int i = 0; i < snap.queueEvents.size(); i++) this.queueEvents.add(snap.queueEvents.get(i));

        this.waitHistoryByDate.clear();
        for (int i = 0; i < snap.waitHistoryByDate.size(); i++) this.waitHistoryByDate.add(snap.waitHistoryByDate.get(i));
    }

    private void saveNow() { patientDAO.saveToFile(records); }

    private void saveActivity() {
        ActivityDAO.ActivitySnapshot out = new ActivityDAO.ActivitySnapshot();

        for (int i = 0; i < this.registrations.size(); i++) out.registrations.add(this.registrations.get(i));
        for (int i = 0; i < this.deletions.size(); i++)     out.deletions.add(this.deletions.get(i));
        for (int i = 0; i < this.queueEvents.size(); i++)   out.queueEvents.add(this.queueEvents.get(i));

        // deep copy waits
        for (int i = 0; i < this.waitHistoryByDate.size(); i++) {
            DateWaits src = this.waitHistoryByDate.get(i);
            DateWaits dst = new DateWaits(src.date);
            for (int j = 0; j < src.waits.size(); j++) dst.waits.add(src.waits.get(j));
            out.waitHistoryByDate.add(dst);
        }
        activityDAO.saveToFile(out);
    }

    // ---------- “map-like” helpers ----------
    private LocalDateTime timeGet(String pid) {
        for (int i = 0; i < queueEnqueueTimes.size(); i++) {
            Pair<String, LocalDateTime> p = queueEnqueueTimes.get(i);
            if (p.key.equals(pid)) return p.value;
        }
        return null;
    }
    private void timePut(String pid, LocalDateTime t) {
        for (int i = 0; i < queueEnqueueTimes.size(); i++) {
            if (queueEnqueueTimes.get(i).key.equals(pid)) { queueEnqueueTimes.remove(i); break; }
        }
        queueEnqueueTimes.add(new Pair<>(pid, t));
    }
    private void timeRemove(String pid) {
        for (int i = 0; i < queueEnqueueTimes.size(); i++) {
            if (queueEnqueueTimes.get(i).key.equals(pid)) { queueEnqueueTimes.remove(i); return; }
        }
    }
    private ListInterface<Long> waitsFor(LocalDate d) {
        for (int i = 0; i < waitHistoryByDate.size(); i++) {
            DateWaits dw = waitHistoryByDate.get(i);
            if (dw.date.equals(d)) return dw.waits;
        }
        DateWaits nw = new DateWaits(d);
        waitHistoryByDate.add(nw);
        return nw.waits;
    }

    // ==================== SUMMARY REPORT STRUCTS/LOGIC ====================
    public static class DailyRegistrationStats {
        public int totalRegistrationEvents;
        public int distinctRegistered;
        public int newPatients;
        public int returningPatients;

        public int male, female, genderUnknown;
        public int children, adults, seniors;

        // Department counts without JCF:
        public ListInterface<Pair<String,Integer>> deptCounts = new LinkedList<>();

        public int regWalkIn, regAppointment, regUnknown;

        public int totalEnqueueEvents;
        public int distinctEnqueuedPatients;
        public int enqWalkIn, enqAppointment;

        public int[] hourlyEnqueues = new int[24];
        public int busiestHour = -1, busiestHourCount = 0;
    }

    public static class QueueStatusSnapshot {
        public int queueLength;
        public int emergencyCount;
        public long avgWaitMinutesCurrent;

        public static class DayWait {
            public LocalDate date; public int served; public long avgWaitMinutes;
        }
        public ListInterface<DayWait> last7Days = new LinkedList<>();

        public static class Row {
            public int position; public String name; public int age; public String department;
            public boolean emergency; public long minutesWait;
        }
        public ListInterface<Row> rows = new LinkedList<>();
    }

    private static String digits(String s){ return (s==null) ? "" : s.replaceAll("[^0-9]",""); }

    public DailyRegistrationStats generateDailyRegistrationStats(LocalDate day) {
        DailyRegistrationStats s = new DailyRegistrationStats();

        // Pass 0: earliest registration date PER IC across all time
        ListInterface<Pair<String, LocalDate>> earliestByIc = new LinkedList<>();
        for (int i = 0; i < registrations.size(); i++) {
            Registration r = registrations.get(i);
            String icKey = digits(r.ic);
            int idx = indexOfKey(earliestByIc, icKey);
            if (idx < 0) {
                earliestByIc.add(new Pair<>(icKey, r.time.toLocalDate()));
            } else {
                LocalDate cur = earliestByIc.get(idx).value;
                LocalDate cand = r.time.toLocalDate();
                if (cand.isBefore(cur)) earliestByIc.get(idx).value = cand;
            }
        }

        // Pass 1: count today's events (by IC)
        ListInterface<String> distinctIcToday = new LinkedList<>();
        ListInterface<Pair<String,Integer>> countByIcToday = new LinkedList<>();

        for (int i = 0; i < registrations.size(); i++) {
            Registration r = registrations.get(i);
            if (!r.time.toLocalDate().equals(day)) continue;

            s.totalRegistrationEvents++;

            // distinct ICs today
            String icKey = digits(r.ic);
            if (!distinctIcToday.contains(icKey)) distinctIcToday.add(icKey);

            // per-IC frequency today
            int cIdx = indexOfKey(countByIcToday, icKey);
            if (cIdx < 0) countByIcToday.add(new Pair<>(icKey, 1));
            else countByIcToday.get(cIdx).value = countByIcToday.get(cIdx).value + 1;

            // per-event breakdowns (gender/age/dept/type)
            String g = r.gender == null ? "" : r.gender.trim();
            if ("male".equalsIgnoreCase(g)) s.male++;
            else if ("female".equalsIgnoreCase(g)) s.female++;
            else s.genderUnknown++;

            int age = r.age;
            if (age < 18) s.children++;
            else if (age >= 65) s.seniors++;
            else s.adults++;

            String dept = (r.department == null || r.department.isBlank()) ? "(Unknown)" : r.department;
            int dIdx = indexOfKey(s.deptCounts, dept);
            if (dIdx < 0) s.deptCounts.add(new Pair<>(dept, 1));
            else s.deptCounts.get(dIdx).value = s.deptCounts.get(dIdx).value + 1;

            String at = r.appointmentType == null ? "" : r.appointmentType.trim();
            if ("walk-in".equalsIgnoreCase(at)) s.regWalkIn++;
            else if ("appointment".equalsIgnoreCase(at)) s.regAppointment++;
            else s.regUnknown++;
        }

        // distinct registered today = distinct ICs that appeared today
        s.distinctRegistered = distinctIcToday.size();

        // new patients today = ICs whose earliest-ever registration date == today
        int newCount = 0;
        for (int i = 0; i < distinctIcToday.size(); i++) {
            String icKey = distinctIcToday.get(i);
            int eIdx = indexOfKey(earliestByIc, icKey);
            if (eIdx >= 0 && day.equals(earliestByIc.get(eIdx).value)) newCount++;
        }
        s.newPatients = newCount;

        // returning patients today = ICs with >1 registrations today 
        int returningDistinct = 0;
        for (int i = 0; i < countByIcToday.size(); i++) {
            if (countByIcToday.get(i).value > 1) returningDistinct++;
        }
        s.returningPatients = returningDistinct;

        // Enqueue stats (unchanged)
        ListInterface<String> enqDistinct = new LinkedList<>();
        for (int i = 0; i < queueEvents.size(); i++) {
            QueueEvent e = queueEvents.get(i);
            if (!e.time.toLocalDate().equals(day)) continue;
            s.totalEnqueueEvents++;
            s.hourlyEnqueues[e.time.getHour()]++;
            if (!enqDistinct.contains(e.patientId)) enqDistinct.add(e.patientId);

            Patient p = findPatientById(e.patientId);
            if (p != null) {
                String t = p.getAppointmentType();
                if ("walk-in".equalsIgnoreCase(t)) s.enqWalkIn++;
                else if ("appointment".equalsIgnoreCase(t)) s.enqAppointment++;
            }
        }
        s.distinctEnqueuedPatients = enqDistinct.size();

        for (int h = 0; h < 24; h++) {
            if (s.hourlyEnqueues[h] > s.busiestHourCount) { s.busiestHourCount = s.hourlyEnqueues[h]; s.busiestHour = h; }
        }
        return s;
    }

    private static <V> int indexOfKey(ListInterface<Pair<String, V>> list, String key) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).key.equals(key)) return i;
        }
        return -1;
    }

    public QueueStatusSnapshot generateQueueStatusSnapshot() {
        QueueStatusSnapshot q = new QueueStatusSnapshot();

        ArrayQueue<Patient> em = getEmergencyQueueSnapshot();
        ArrayQueue<Patient> nm = getNormalQueueSnapshot();
        q.emergencyCount = sizeOf(em);
        q.queueLength = q.emergencyCount + sizeOf(nm);

        long totalMs = 0L; int count = 0;
        java.util.Iterator<Patient> it1 = em.getIterator();
        while (it1.hasNext()) {
            Patient p = it1.next();
            LocalDateTime t = timeGet(p.getPatientId());
            if (t != null) { totalMs += Duration.between(t, LocalDateTime.now()).toMillis(); count++; }
        }
        java.util.Iterator<Patient> it2 = nm.getIterator();
        while (it2.hasNext()) {
            Patient p = it2.next();
            LocalDateTime t = timeGet(p.getPatientId());
            if (t != null) { totalMs += Duration.between(t, LocalDateTime.now()).toMillis(); count++; }
        }
        q.avgWaitMinutesCurrent = (count == 0) ? 0 : (totalMs / count) / 1000 / 60;

        LocalDate today = LocalDate.now();
        for (int back = 6; back >= 0; back--) {
            LocalDate d = today.minusDays(back);
            ListInterface<Long> waits = waitsFor(d); // returns existing or creates empty
            long avgMs = 0; int served = waits.size();
            if (served > 0) {
                long sum = 0; for (int i = 0; i < waits.size(); i++) sum += waits.get(i);
                avgMs = sum / served;
            }
            QueueStatusSnapshot.DayWait row = new QueueStatusSnapshot.DayWait();
            row.date = d; row.served = served; row.avgWaitMinutes = (avgMs / 1000) / 60;
            q.last7Days.add(row);
        }

        int pos = 1;
        it1 = em.getIterator();
        while (it1.hasNext()) {
            Patient p = it1.next();
            QueueStatusSnapshot.Row r = new QueueStatusSnapshot.Row();
            r.position = pos++; r.name = p.getName(); r.age = p.getAge();
            r.department = (p.getDepartment()==null || p.getDepartment().isBlank()) ? "-" : p.getDepartment();
            r.emergency = true;
            LocalDateTime t = timeGet(p.getPatientId());
            r.minutesWait = (t == null) ? 0 : Duration.between(t, LocalDateTime.now()).toMinutes();
            q.rows.add(r);
        }
        it2 = nm.getIterator();
        while (it2.hasNext()) {
            Patient p = it2.next();
            QueueStatusSnapshot.Row r = new QueueStatusSnapshot.Row();
            r.position = pos++; r.name = p.getName(); r.age = p.getAge();
            r.department = (p.getDepartment()==null || p.getDepartment().isBlank()) ? "-" : p.getDepartment();
            r.emergency = false;
            LocalDateTime t = timeGet(p.getPatientId());
            r.minutesWait = (t == null) ? 0 : Duration.between(t, LocalDateTime.now()).toMinutes();
            q.rows.add(r);
        }
        return q;
    }
}
