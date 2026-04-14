package dao;

/** Author: Teo Geok Woon */

import adt.LinkedList;
import adt.ListInterface;
import entity.Patient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PatientDAO {
    private String fileName = "patient.dat";
  
    public void saveToFile(ListInterface<Patient> doctorList) {
        File file = new File(fileName);
        try {
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(doctorList);
            ooStream.close();
        } catch (FileNotFoundException ex) {
            System.out.println("\nFile not found");
        } catch (IOException ex) {
            System.out.println("\nCannot save to file");
        }
    }

    public ListInterface<Patient> retrieveFromFile() {
        File file = new File(fileName);
        ListInterface<Patient> patientList = new LinkedList<>();
        try {
          ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
          patientList = (LinkedList<Patient>) (oiStream.readObject());
          oiStream.close();
        } catch (FileNotFoundException ex) {
          System.out.println("\nNo such file.");
        } catch (IOException ex) {
          System.out.println("\nCannot read from file.");
        } catch (ClassNotFoundException ex) {
          System.out.println("\nClass not found.");
        } finally {
          return patientList;
        }
    }
}
