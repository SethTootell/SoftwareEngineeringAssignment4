package au.edu.rmit.sct;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Date;

public class PersonTest {
    
    //addPerson()
    @Test //#1
    void testAddPerson_validInput() {
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
        Person p = new Person("23$%&@AB", "Bob", "Brown",
                "12|Main St|Melbourne|Victoria|Australia", birthDate, demerits, false);
        assertFalse(p.addPerson());
    }

    @Test //#3
    void testAddPerson_invalidStateInAddress() {
        Date birthDate = Date.from(LocalDate.of(2000, 5, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        HashMap<Date, Integer> demerits = new HashMap<>();
        Person p = new Person("23$%&@!AB", "Cathy", "Davis",
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
   
}
