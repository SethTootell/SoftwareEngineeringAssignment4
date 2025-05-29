package au.edu.rmit.sct;
import java.util.HashMap;
import java.util.Date;

public class Person {
    private String personId;
    private String firstName;
    private String lastName;
    private String address;
    private Date birthDate;
    private HashMap<Date, Integer> demeritPoints;
    private boolean isSuspended;

    protected Person(String personId,
                  String firstName,
                  String lastName,
                  String address,
                  Date birthDate,
                  HashMap<Date, Integer> demeritPoints,
                  boolean isSuspended)
    {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthDate = birthDate;
        this.demeritPoints = demeritPoints;
        this.isSuspended = isSuspended;
    }


    public boolean addPerson() {
        // Condition 1: personID must be exactly 10 characters long
        // first two characters must be numbers between 2 and 9
        // there must be at least two special characters between characters 3 and 8
        // last two characters must be uppercase letters A-Z
        // personID must not already exist

        // Condition 2: the address should be in the format
        // Street Number|Street|City|State|Country
        // State must be victoria

        // Condition 3: format of birthdate must be DD-MM-YYYY

        // The information must be placed in a text file that should be human-readable
        // ONLY if the prior three conditions are met
        return false; // TODO
    }

    public boolean updatePersonalDetails() {
        // Condition 1: If person is under 18 address cannot be changed
        // Condition 2: If a persona birthday is going to be changed, no other personal detail can be changed
        // Condition 3: If the first character/digit of a person's ID is an even number, then their ID cannot be changed
        // If the above conditions are met their information in the text file must be updated
        return false; // TODO
    }

    public String addDemeritPoints() {
        // Condition 1: The format of the date of the offense should follow the following format:  DD-MM-YYYY
        // Condition 2: The demerit points must be a whole number between 1 and 6,
        // If the person is under 21, isSuspended should be set to true if demeritPoints within 2 years is > 6
        // If the person is 21 or over, the above should be set to true if exceeding 12 over two years
        // The demerit points should be updated in the person's file, the function should return "Success" if the above is passed
        // otherwise, "Failure"
        return "Success";
    }
}
