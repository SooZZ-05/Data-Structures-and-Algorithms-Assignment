package dao;

/**
 *
 * @author Low Zi Qing
 */
import adt.LinkedList;
import adt.ListInterface;
import entity.Consultation;

import java.io.*;

public class ConsultationDAO {

    private final String fileName;

    public ConsultationDAO() {
        this("consultation.dat");
    }

    public ConsultationDAO(String fileName) {
        this.fileName = fileName;
    }

    public void saveToFile(ListInterface<Consultation> consultations) {
        // Implementation should overwrite existing file
        // Example using ObjectOutputStream:
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("consultation.dat"))) {
            oos.writeObject(consultations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public ListInterface<Consultation> retrieveFromFile() {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("No file found, returning empty list.");
            return new LinkedList<>();
        }

        try (ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = oiStream.readObject();
            if (obj instanceof ListInterface) {
                return (ListInterface<Consultation>) obj;
            } else {
                System.err.println("File content is not a Consultation list.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error reading consultations: " + ex.getMessage());
        }
        return new LinkedList<>();
    }
}
