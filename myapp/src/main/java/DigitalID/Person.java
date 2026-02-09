package DigitalID;
import java.util.HashMap;
import java.util.Date;
import Database.Database;


public class Person {
    // Data Members
    private String _personID;
    private String _firstName;
    private String _lastName;
    private String _address;
    private String _birthdate;
    private HashMap<Date, Integer> _demeritPoints; // Holds demerit points with the offense date
    private boolean _isSuspended;

    // Constructors
    public Person() {}

    public Person(String personID, String firstName, String lastName, 
                    String address, String birthdate) {
        this._personID = personID;
        this._firstName = firstName;
        this._lastName = lastName;
        this._address = address;
        this._birthdate = birthdate;
        this._demeritPoints = new HashMap<>();
        this._isSuspended = false;
    }
    
    // Getters and Setters
    public String getPersonID(String personID) { return _personID; }
    public String getFirstName(String firstName) { return _firstName; }
    public String getLastName(String lastName) { return _lastName; }
    public String getAddress(String address) { return _address; }
    public String getBirthdate(String birthdate) { return _birthdate; }
    public boolean getIsSuspended(boolean isSuspended) { return _isSuspended; }

    public void setPersonID(String personID) {  this._personID = personID; }
    public void setFirstName(String firstName) {  this._firstName = firstName; }
    public void setLastName(String lastName) {  this._lastName = lastName; }
    public void setAddress(String address) {  this._address = address; }
    public void setBirthdate(String birthdate) {  this._birthdate = birthdate; }
    public void setIsSuspended(boolean isSuspended) {  this._isSuspended = isSuspended; }
    
    // Helpers

    // Convert Person object to file line for database entry
    public String toFileLine(String delimiter) {
        StringBuilder sb = new StringBuilder();
        sb.append(_personID).append(delimiter);
        sb.append(_firstName).append(delimiter);
        sb.append(_lastName).append(delimiter);
        sb.append(_address).append(delimiter);
        sb.append(_birthdate).append(delimiter);
        sb.append(_demeritPoints).append(delimiter);
        sb.append(_isSuspended).append(delimiter);
        return sb.toString();
    }

    // Convert database entry to Person Object
    public static Person fromFileLine(String line, String delimiter) {
        // Must exist
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        // Must contain all data members
        String[] parts = line.split("\\" + delimiter);
        if (parts.length < 7) {
            return null;
        }

        // Build Person
        Person person = new Person(parts[0], parts[1], parts[2], parts[3], parts[4]);
        // TODO serialise the demerit points mapping here
        person._isSuspended = Boolean.parseBoolean(parts[6]);

        return person;
    }
    // Methods
    public boolean addPerson(){

        // TODO: This method adds information about a person to a TXT file.

        // Condition 1: PersonID should be exactly 10 characters long;
        // the first two characters should be numbers between 2 and 9,
        // there should be at least two special characters between characters 3 and 8,
        // and the last two characters should be upper case letters (A - Z).
        // Example: "56s_d%&fAB"

        // Condition 2: The address of the Person should follow the format:
        // StreetNumber|Street|City|State|Country
        // The State should be only Victoria.
        // Example: 32|Highland Street|Melbourne|Victoria|Australia

        // Condition 3: The birth date format should be: DD-MM-YYYY.
        // Example: 15-11-1990

        // Instruction: If the Person's information meets the above conditions
        // (and any other conditions you want to consider),
        // the information should be inserted into a TXT file,
        // and the addPerson function should return true.

        // Otherwise, do not insert into the TXT file and return false.


        return true;
    }

    public boolean updatePersonalDetails(){

        // TODO: This method allows updating a person's ID, firstName, lastName,
        // address and birthday in a TXT file.

        // Changing personal details will not affect demerit points or suspension status.

        // All relevant conditions from addPerson must also be checked here.

        // Condition 1: If a person is under 18, their address cannot be changed.

        // Condition 2: If a person's birthday is changed, then no other personal
        // detail (ID, firstName, lastName, address) can be changed.

        // Condition 3: If the first digit of a person's ID is an even number,
        // then their ID cannot be changed.

        // Instruction: If updated info meets all conditions,
        // update the TXT file and return true.
        // Otherwise, do not update and return false.

        return true;
    }

    public String addDemeritPoints(){

        // TODO: This method adds demerit points for a person in a TXT file.

        // Condition 1: Offense date format must be DD-MM-YYYY.
        // Example: 15-11-1990

        // Condition 2: Demerit points must be a whole number between 1–6.

        // Condition 3:
        // If person is under 21 → isSuspended = true if total demerit points
        // within two years exceed 6.
        // If person is over 21 → isSuspended = true if total demerit points
        // within two years exceed 12.

        // Instruction: If conditions are met, insert into TXT file
        // and return "Success". Otherwise return "Failed".


        return "Success";
    }

}
