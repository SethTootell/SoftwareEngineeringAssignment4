package au.edu.rmit.sct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.lang.System;

public class Person {
    private String personId;
    private String firstName;
    private String lastName;
    private String address;
    private Date birthDate;
    private HashMap<Date, Integer> demeritPoints;
    private boolean isSuspended;

    private static final Set<Character> SPECIAL_CHARACTERS_SET = Set.of(
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '=', '_', '+',
            '[', '{', ']', '}', '\\', '|', ';', ':', '\'', '"', ',', '<', '.', '>', '/', '?'
    );

    private final int VICTORIA_INDEX = 3;
    private final String VICTORIA = "Victoria";


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

    public static String formatDate(Date date, DateTimeFormatter formatter) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(formatter);
    }

    public String formatToText() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String birthDateFormatted = formatDate(birthDate, dtf);

        StringBuilder map = new StringBuilder();
        for (Map.Entry<Date, Integer> entry : demeritPoints.entrySet()) {
            Date key = entry.getKey();
            Integer value = entry.getValue();
            map.append("demeritPoints: (")
                    .append(formatDate(key, dtf))
                    .append(", ")
                    .append(value)
                    .append(")\n");
        }

        String demeritPointsString = map.toString();

        return "personID:" + personId + "\n"
                + "firstName:" + firstName + "\n"
                + "lastName:" + lastName + "\n"
                + "address:" + address + "\n"
                + "birthDate:" + birthDateFormatted + "\n"
                + demeritPointsString
                + "isSuspended:" + isSuspended + "\n";

    }

    private static boolean checkCharInAsciiRange(char c, char start, char end) {
        return c >= start && c <= end;
    }

    private boolean verifyPersonID() {
        // Condition 1: personID must be exactly 10 characters long
        // first two characters must be numbers between 2 and 9
        // there must be at least two special characters between characters 3 and 8
        // last two characters must be uppercase letters A-Z
        // DOES NOT check if personID already exists
        if (personId == null || personId.isEmpty()) return false;
        if (personId.length() != 10) return false;
        char[] chars = personId.toCharArray();

        // check first two characters
        if (!Person.checkCharInAsciiRange(chars[0], '2', '9')) return false;
        if (!Person.checkCharInAsciiRange(chars[1], '2', '9')) return false;

        // check characters 3-8
        int num_special_characters = 0;
        for (int i = 2; i <= 7; i++) {
            if (SPECIAL_CHARACTERS_SET.contains(chars[i])) num_special_characters++;
        }
        if (num_special_characters < 2) return false;

        // check characters 9 and 10
        if (!Person.checkCharInAsciiRange(chars[8], 'A', 'Z')) return false;
        return Person.checkCharInAsciiRange(chars[9], 'A', 'Z');

        // if passed everything stated, personID is valid
    }

    public boolean verifyAddress() {
        // Condition 2: the address should be in the format
        // Street Number|Street|City|State|Country
        // State must be victoria

        String[] addressParts = address.split("\\|");
        if (addressParts.length != 5) return false;
        return addressParts[VICTORIA_INDEX].equals(VICTORIA);
    }

    private boolean doesPersonFileExist() {
        Path dir = Paths.get("PersonsFiles");
        String[] fileNames = dir.toFile().list();
        String lookingFor = personId + ".txt";
        if (fileNames == null) return false;
        return Arrays.asList(fileNames).contains(lookingFor);
    }

    private Path getFilePath() {
        return Paths.get("PersonsFiles", personId + ".txt");
    }
    private Path createPersonFile() {
        // Define the directory path where person files will be stored -- yihan
        Path dir = Paths.get("PersonsFiles");
        // If the directory does not exist, create it -- yihan
        if (!Files.exists(dir)) {
            try {
                Files.createDirectory(dir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Path path = getFilePath();
        try {
            Files.createFile(path);
            return path;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void writeToFile(Path file) {
        try {
            Files.writeString(file, formatToText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean toTextFile() {
        // The format of this text file is going to be as follows
        // FIELD_NAME: VALUE
        // for the HashMap where multiple values exist, the name will simply be repeated, i.e.
        // MAP: VALUE
        // MAP: ANOTHER_VALUE

        // first we will check that the file does not already exist
        if (doesPersonFileExist()) return false;

        // now we create a new file
        Path file = createPersonFile();
        writeToFile(file);
        return true;
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
        // in java the equivalent would be dd-MM-yyyy
        // and is a given using the Date class

        // The information must be placed in a text file that should be human-readable
        // ONLY if the prior three conditions are met
        if (!verifyPersonID()) return false;
        if (!verifyAddress()) return false;
        return toTextFile(); //yihan
    }


    private void copyFrom(Person other) {
        this.personId = other.personId;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.address = other.address;
        this.birthDate = other.birthDate;
        this.demeritPoints = other.demeritPoints;
        this.isSuspended = other.isSuspended;
    }
    private boolean isUpdatingBirthDate(Person newDetails) {
        return !Objects.equals(birthDate, newDetails.birthDate);
    }

    ///  checks if the personID is even and if personID is being changed
    private boolean personIdUpdateCheck(Person newDetails) {
        boolean IdChanged = !Objects.equals(personId, newDetails.personId);
        char first = personId.charAt(0);
        boolean isEven = Character.isDigit(first) && (Character.getNumericValue(first) & 1) == 0;
        return !(IdChanged && isEven);
    }

    private boolean equalsExcludingBirthDate(Person other) {
        return (Objects.equals(personId, other.personId) &&
                Objects.equals(firstName, other.firstName) &&
                Objects.equals(lastName, other.lastName) &&
                Objects.equals(address, other.address) &&
                isSuspended == other.isSuspended);
    }
    private boolean birthDateUpdateCheck(Person newDetails) {
        return this.equalsExcludingBirthDate(newDetails) && !isUpdatingBirthDate(newDetails);
    }

    private int getAgeInYears() {
        LocalDate birthDate = this.birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        return Period.between(birthDate, now).getYears();
    }

    private boolean isChangingAddressWhenUnderage(Person newDetails) {
        return getAgeInYears() < 18 && !Objects.equals(address, newDetails.address);
    }
    private boolean canUpdateDetails(Person newDetails) {
        if (isChangingAddressWhenUnderage(newDetails)) return false;
        if (!personIdUpdateCheck(newDetails)) return false;
        return birthDateUpdateCheck(newDetails);
    }

    private void updateTextFile(Person newDetails) {
        if (!doesPersonFileExist()) {
            newDetails.toTextFile();
        };
        Path path = getFilePath();
        try {
            Files.delete(path);
            Path newPath = newDetails.getFilePath();
            writeToFile(newPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updatePersonalDetails(Person newDetails) {
        // Condition 1: If person is under 18 address cannot be changed
        // Condition 2: If a persons birthday is going to be changed, no other personal detail can be changed
        // Condition 3: If the first character/digit of a person's ID is an even number, then their ID cannot be changed
        // If the above conditions are met their information in the text file must be updated
        if (!canUpdateDetails(newDetails)) return false;
        updateTextFile(newDetails);
        return true;
    }


    private static boolean isValidNumberOfPoints(int points) {
        return points >= 1 && points <= 6;
    }

    private ArrayList<Date> getDemeritPointsWithinTwoYears() {
        ArrayList<Date> points = new ArrayList<>();
        LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
        for (Date dates: demeritPoints.keySet()) {
            LocalDate date = dates.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (!date.isBefore(twoYearsAgo)) {
                points.add(dates);
            }
        }
        return points;
    }

    private void updateDemeritsField(int points) {
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        demeritPoints.put(today, points);
    }

    private void updateSuspension(int points) {
        updateDemeritsField(points);
        ArrayList<Date> demeritDates = getDemeritPointsWithinTwoYears();
        int numPoints = demeritDates.stream().mapToInt(d -> demeritPoints.getOrDefault(d, 0)).sum();

        if (getAgeInYears() >= 21 && numPoints > 12) {
            isSuspended = true;
        }
        else if (getAgeInYears() < 21 && numPoints > 6) {
            isSuspended = true;
        }
        else isSuspended = false;
    }
    public String addDemeritPoints(int points) {
        // Condition 1: The format of the date of the offense should follow the following format:  DD-MM-YYYY
        // Condition 2: The demerit points must be a whole number between 1 and 6,
        // If the person is under 21, isSuspended should be set to true if demeritPoints within 2 years is > 6
        // If the person is 21 or over, the above should be set to true if exceeding 12 over two years
        // The demerit points should be updated in the person's file, the function should return "Success" if the above is passed
        // otherwise, "Failure"
        if (!isValidNumberOfPoints(points)) return "Failure";
        updateSuspension(points);
        updateTextFile(this);
        return "Success";
    }
}
