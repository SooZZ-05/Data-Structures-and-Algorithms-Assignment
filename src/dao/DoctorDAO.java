package dao;

/**
 *
 * @author Sebastian
 */

import adt.LinkedList;
import adt.ListInterface;
import entity.Doctor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DoctorDAO {
    private String fileName = "doctors.dat";
  
    public void saveToFile(ListInterface<Doctor> doctorList) {
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

    public ListInterface<Doctor> retrieveFromFile() {
        File file = new File(fileName);
        ListInterface<Doctor> doctorList = new LinkedList<>();
        try {
          ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
          doctorList = (LinkedList<Doctor>) (oiStream.readObject());
          oiStream.close();
        } catch (FileNotFoundException ex) {
          System.out.println("\nNo such file.");
        } catch (IOException ex) {
          System.out.println("\nCannot read from file.");
        } catch (ClassNotFoundException ex) {
          System.out.println("\nClass not found.");
        } finally {
          return doctorList;
        }
    }
}