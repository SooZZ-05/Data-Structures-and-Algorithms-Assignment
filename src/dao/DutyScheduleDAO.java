package dao;

/**
 *
 * @author Sebastian
 */
import adt.SortedListInterface;
import adt.SortedSkipList;
import entity.DutySchedule;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author HP001
 */
public class DutyScheduleDAO {
    private String fileName = "dutySchedule.dat";
  
  public void saveToFile(SortedListInterface<DutySchedule> dutyScheduleList) {
    File file = new File(fileName);
    try {
      ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
      ooStream.writeObject(dutyScheduleList);
      ooStream.close();
    } catch (FileNotFoundException ex) {
      System.out.println("\nFile not found");
    } catch (IOException ex) {
      System.out.println("\nCannot save to file");
      ex.printStackTrace();
    }
  }

  public SortedListInterface<DutySchedule> retrieveFromFile() {
    File file = new File(fileName);
    SortedListInterface<DutySchedule> dutyScheduleList = new SortedSkipList<>(
            new DutySchedule("MIN", LocalDate.MIN, LocalTime.MIN, LocalTime.MIN),
            new DutySchedule("MAX", LocalDate.MAX, LocalTime.MAX, LocalTime.MAX));
    try {
      ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
      dutyScheduleList = (SortedSkipList<DutySchedule>) (oiStream.readObject());
      oiStream.close();
    } catch (FileNotFoundException ex) {
      System.out.println("\nNo such file.");
    } catch (IOException ex) {
      System.out.println("\nCannot read from file.");
    } catch (ClassNotFoundException ex) {
      System.out.println("\nClass not found.");
    } finally {
      return dutyScheduleList;
    }
  }
}