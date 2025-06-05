package au.edu.rmit.sct;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Date;

public class PersonTest {
    
    //addPerson()
    @Test //#1
    void testAddPerson_validInput() {
        File file = new File("PersonsFiles/23$%_&@!AB.txt");
        if (file.exists()) {
            file.delete();
        }
        Date birthDate = Date.from(LocalDate.of(2000, 5, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23$%_&@!AB", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        assertTrue(p.addPerson());
    }

    @Test //#2
    void testAddPerson_invalidIDLength() {
        Date birthDate = Date.from(LocalDate.of(2000, 5, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23$%&@AC", "Bob", "Brown",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        assertFalse(p.addPerson());
    }

    @Test //#3
    void testAddPerson_invalidStateInAddress() {
        Date birthDate = Date.from(LocalDate.of(2000, 5, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23$%&@!AD", "Cathy", "Davis",
                "12|Main St|Melbourne|NSW|Australia", birthDate, demerits, false);
        assertFalse(p.addPerson());
    }

    @Test //#4
    void testAddPerson_notEnoughSpecialCharacters() {
        Date birthDate = Date.from(LocalDate.of(2000, 5, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23abcdefAB", "David", "Evans",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        assertFalse(p.addPerson());
    }

    @Test //#5
    void testAddPerson_duplicateFile() {
        Date birthDate = Date.from(LocalDate.of(2000, 5, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23$%_&@!AB", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        p.addPerson(); 
        assertFalse(p.addPerson()); 
    }

    //updatePersonalDetails()
    @Test //#1
    void testUpdateDetails_Under18AddressChange() {
        Date birthDate = Date.from(LocalDate.of(2010, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person original = new Person("24$%_&@!AB", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        original.addPerson();

        Person updated = new Person("24$%_&@!AB", "Alice", "Smith",
                "99|New St|Geelong|Victoria|Australia", birthDate, demerits, false);
    
        assertFalse(original.updatePersonalDetails(updated));
    }

    @Test //#2
    void testUpdateDetails_ChangeBirthdayAndAddress() {
        Date birthDate = Date.from(LocalDate.of(2000, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person original = new Person("25$%_&@!AB", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        original.addPerson();

        Date newBirthDate = Date.from(LocalDate.of(2002, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Person updated = new Person("25$%_&@!AB", "Alice", "Smith",
                "99|New St|Melbourne|Victoria|Australia", newBirthDate, demerits, false);
    
        assertFalse(original.updatePersonalDetails(updated));
    }

    @Test //#3
    void testUpdateDetails_EvenIDCannotChange() {
        Date birthDate = Date.from(LocalDate.of(2000, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person original = new Person("26$%_&@!AB", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        original.addPerson();

        Person updated = new Person("22$%_&@!AB", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
    
        assertFalse(original.updatePersonalDetails(updated));
    }

    @Test //#4
    void testUpdateDetails_OnlyChangeBirthday() {
        Date birthDate = Date.from(LocalDate.of(2000, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person original = new Person("27$%_&@!AB", "Alice", "Smith",
             "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        original.addPerson();

        Date newBirthDate = Date.from(LocalDate.of(2001, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Person updated = new Person("27$%_&@!AB", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", newBirthDate, demerits, false);
    
        assertTrue(original.updatePersonalDetails(updated));
    }

    @Test //#5
    void testUpdateDetails_ValidUpdate() {
        Date birthDate = Date.from(LocalDate.of(2000, 10, 14).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person original = new Person("28$%_&@!AB", "Alice", "Smith",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        original.addPerson();

        Person updated = new Person("28$%_&@!AB", "Alicia", "Smith",
                "12|New St|Geelong|Victoria|Australia", birthDate, demerits, false);
    
        assertTrue(original.updatePersonalDetails(updated));
    }
   
}
