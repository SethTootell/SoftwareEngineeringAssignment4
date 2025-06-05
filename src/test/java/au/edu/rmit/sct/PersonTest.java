package au.edu.rmit.sct;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

public class PersonTest {
    
    //addPerson()
    @Test //#1.1
    void testAddPerson_validInput() {
        File file = new File("PersonsFiles/23$%_&@!AB.txt");
        if (file.exists()) {
            file.delete();
        }
        Date birthDate = Date.from(LocalDate.of(2005, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23$%_&@!AB", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        assertTrue(p.addPerson());
    }

    @Test //#1.2
    void testAddPerson_invalidIDLength() {
        Date birthDate = Date.from(LocalDate.of(2005, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23$%_&@!A", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        assertFalse(p.addPerson());
    }

    @Test //#1.3
    void testAddPerson_invalidStateInAddress() {
        Date birthDate = Date.from(LocalDate.of(2005, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23$%_&@!AB", "Alice", "Smith",
                "12|Main St|Melbourne|NSW|Australia", birthDate, demerits, false);
        assertFalse(p.addPerson());
    }

    @Test //#1.4
    void testAddPerson_notEnoughSpecialCharacters() {
        Date birthDate = Date.from(LocalDate.of(2005, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23abcdefAB", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        assertFalse(p.addPerson());
    }

    @Test //#1.5
    void testAddPerson_duplicateFile() {
        Date birthDate = Date.from(LocalDate.of(2005, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23$%_&@!AB", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        p.addPerson(); 
        assertFalse(p.addPerson()); 
    }

    //updatePersonalDetails()
    @Test //#2.1
    void testUpdateDetails_Under18AddressChange() {
        Date birthDate = Date.from(LocalDate.of(2010, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person original = new Person("26@#$%_!KL", "Ethan", "Collins",
                "88|Old St|Geelong|Victoria|Australia", birthDate, demerits, false);
        original.addPerson();

        Person updated = new Person("26@#$%_!KL", "Ethan", "Collins",
                "99|New St|Geelong|Victoria|Australia", birthDate, demerits, false);
    
        assertFalse(original.updatePersonalDetails(updated));
    }

    @Test //#2.2
    void testUpdateDetails_ChangeBirthdayAndAddress() {
        Date birthDate = Date.from(LocalDate.of(2004, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person original = new Person("27^&*()_MN", "Fiona", "Lee",
                "33|New St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        original.addPerson();

        Date newBirthDate = Date.from(LocalDate.of(2003, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Person updated = new Person("27^&*()_MN", "Fiona", "Lee",
                "99|New St|Melbourne|Victoria|Australia", newBirthDate, demerits, false);
    
        assertFalse(original.updatePersonalDetails(updated));
    }

    @Test //#2.3
    void testUpdateDetails_EvenIDCannotChange() {
        Date birthDate = Date.from(LocalDate.of(2000, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person original = new Person("28$%_&@!OP", "George", "Harris",
                "45|Hill St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        original.addPerson();

        Person updated = new Person("22$%_&@!AA", "George", "Harris",
                "45|Hill St|Melbourne|Victoria|Australia", birthDate, demerits, false);
    
        assertFalse(original.updatePersonalDetails(updated));
    }

    @Test //#2.4
    void testUpdateDetails_OnlyChangeBirthday() {
        Date birthDate = Date.from(LocalDate.of(2001, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person original = new Person("29$%_&@!QR", "Hannah", "Kim",
             "23|High St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        original.addPerson();

        Date newBirthDate = Date.from(LocalDate.of(2001, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Person updated = new Person("29$%_&@!QR", "Hannah", "Kim",
                "23|High St|Melbourne|Victoria|Australia", newBirthDate, demerits, false);
    
        assertTrue(original.updatePersonalDetails(updated));
    }

    @Test //#2.5
    void testUpdateDetails_ValidUpdate() {
        Date birthDate = Date.from(LocalDate.of(2000, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person original = new Person("39$%_&@!ST", "Isaaac", "Brown",
                "12|West St|Geelong|Victoria|Australia", birthDate, demerits, false);
        original.addPerson();

        Person updated = new Person("39$%_&@!ST", "Isaac", "Brown",
                "66|West St|Geelong|Victoria|Australia", birthDate, demerits, false);
    
        assertTrue(original.updatePersonalDetails(updated));
    }

    //addDemeritPoints()

        private boolean getIsSuspendedFromFile(String personId) {
        Path filePath = Paths.get("PersonsFiles", personId + ".txt");
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.startsWith("isSuspended:")) {
                    return Boolean.parseBoolean(line.split(":")[1].trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("isSuspended field not found for personId: " + personId);
    }

    @Test //#3.1
    void testAddDemeritPoints_Under21_NotSuspended() {
        Date birthDate = Date.from(LocalDate.of(2005, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23$%_&@!AB", "Alice", "Smith",
            "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        p.addPerson();
        String result = p.addDemeritPoints(3);
        assertEquals("Success", result);
        assertFalse(getIsSuspendedFromFile("23$%_&@!AB")); 
    }

    @Test //#3.2
    void testAddDemeritPoints_Over21_NotSuspended() {
        Date birthDate = Date.from(LocalDate.of(2000, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("39$%_&@!ST", "Isaac", "Brown",
            "12|West St|Geelong|Victoria|Australia", birthDate, demerits, false);
        p.addPerson();
        String result = p.addDemeritPoints(6);
        assertEquals("Success", result);
        assertFalse(getIsSuspendedFromFile("39$%_&@!ST")); 
    }

    @Test //#3.3
    void testAddDemeritPoints_InvalidZero() {
        Date birthDate = Date.from(LocalDate.of(2004, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("27^&*()_MN", "Fiona", "Lee",
            "33|New St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        p.addPerson();
        String result = p.addDemeritPoints(0);
        assertEquals("Failure", result);
    }

    @Test //#3.4
    void testAddDemeritPoints_Under21_Suspended() {
        Date birthDate = Date.from(LocalDate.of(2005, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        demerits.put(Date.from(LocalDate.now().minusMonths(6).atStartOfDay(ZoneId.systemDefault()).toInstant()), 5);
        Person p = new Person("29$%_&@!QR", "Hannah", "Kim",
            "23|High St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        p.addPerson();
        String result = p.addDemeritPoints(3);
        assertEquals("Success", result);
        assertTrue(getIsSuspendedFromFile("29$%_&@!QR"));
    }

    @Test //#3.5
    void testAddDemeritPoints_Over21_Suspended() {
        Date birthDate = Date.from(LocalDate.of(2000, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        demerits.put(Date.from(LocalDate.now().minusMonths(3).atStartOfDay(ZoneId.systemDefault()).toInstant()), 10);
        Person p = new Person("28$%_&@!OP", "George", "Harris",
            "45|Hill St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        p.addPerson();
        String result = p.addDemeritPoints(5);
        assertEquals("Success", result);
        assertTrue(getIsSuspendedFromFile("28$%_&@!OP"));
    }
   
}
