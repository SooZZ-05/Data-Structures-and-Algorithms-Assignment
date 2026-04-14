package dao;

/** Author: Teo Geok Woon */

import adt.LinkedList;
import adt.ListInterface;
import control.MaintainPatient; // inner classes Registration/DeletionEvent/QueueEvent/DateWaits

import java.io.*;

public class ActivityDAO {
    private final File file;

    public ActivityDAO() { this("activity.dat"); }
    public ActivityDAO(String path) { this.file = new File(path); }

    public static class ActivitySnapshot implements Serializable {
        private static final long serialVersionUID = 1L;
        public ListInterface<MaintainPatient.Registration> registrations = new LinkedList<>();
        public ListInterface<MaintainPatient.DeletionEvent> deletions     = new LinkedList<>();
        public ListInterface<MaintainPatient.QueueEvent>   queueEvents    = new LinkedList<>();
        public ListInterface<MaintainPatient.DateWaits>    waitHistoryByDate = new LinkedList<>();
    }

    public synchronized void saveToFile(ActivitySnapshot snap) {
        if (file.getParentFile() != null) file.getParentFile().mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(snap);
        } catch (IOException ex) {
            System.err.println("\nCannot save activity to " + file.getAbsolutePath());
            ex.printStackTrace();
        }
    }

    public synchronized ActivitySnapshot retrieveFromFile() {
        ActivitySnapshot snap = new ActivitySnapshot();
        if (!file.exists()) return snap;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (obj instanceof ActivitySnapshot) snap = (ActivitySnapshot) obj;
            else System.err.println("activity.dat contains unexpected type; starting fresh.");
        } catch (EOFException eof) {
            // empty file: keep defaults
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("\nCannot read activity from " + file.getAbsolutePath());
            ex.printStackTrace();
        }
        return snap;
    }
}
