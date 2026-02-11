package DigitalID;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import Database.Database;


public class Person {
    // Instance Variables
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
    
    // Public Methods
    public String getPersonID() { return _personID; }
    public String getFirstName() { return _firstName; }
    public String getLastName() { return _lastName; }
    public String getAddress() { return _address; }
    public String getBirthdate() { return _birthdate; }
    public boolean getIsSuspended() { return _isSuspended; }
    public void setPersonID(String personID) {  this._personID = personID; }
    public void setFirstName(String firstName) {  this._firstName = firstName; }
    public void setLastName(String lastName) {  this._lastName = lastName; }
    public void setAddress(String address) {  this._address = address; }
    public void setBirthdate(String birthdate) {  this._birthdate = birthdate; }
    public void setIsSuspended(boolean isSuspended) {  this._isSuspended = isSuspended; }
    
    // Private Methods
    
    // Convert the _demeritPoints Mapping into a string
    private String serialiseDemeritPoints() {
        if (_demeritPoints == null || _demeritPoints.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Date, Integer> entry : _demeritPoints.entrySet()) {
            sb.append(entry.getKey().getTime()).append(":").append(entry.getValue()).append(",");
        }
        return sb.substring(0, sb.length() - 1); // Remove the last comma..
    }

    // Map the concatenated string of date:int into HashMap<Date, Integer>
    private static HashMap<Date, Integer> deserialiseDemeritPoints(String data) {
        HashMap<Date, Integer> map = new HashMap<>();
        if (data == null || data.trim().isEmpty()) {
            return map;
        }
        
        String[] entries = data.split(",");
        for (String entry : entries) {
            String[] parts = entry.split(":");
            if (parts.length == 2) {
                Date date = new Date(Long.parseLong(parts[0]));
                Integer points = Integer.parseInt(parts[1]);
                map.put(date, points);
            }
        }
        return map;
    }

    // Convert Person object to file line for database entry
    public String toFileLine(String delimiter) {
        StringBuilder sb = new StringBuilder();
        sb.append(_personID).append(delimiter);
        sb.append(_firstName).append(delimiter);
        sb.append(_lastName).append(delimiter);
        sb.append(_address).append(delimiter);
        sb.append(_birthdate).append(delimiter);
        sb.append(serialiseDemeritPoints()).append(delimiter);
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
        person._demeritPoints = deserialiseDemeritPoints(parts[5]);
        person._isSuspended = Boolean.parseBoolean(parts[6]);

        return person;
    }
    
    // Methods
    public boolean meetsAddPersonRequirements(){
        if (checkNameFormat() && checkAddressFormat() && checkBirthdayFormat()){
            return true;
        }
        return false;
    }

    public boolean addPerson(){
        // Instruction: If the Person's information meets the above conditions
            // (and any other conditions you want to consider) 

        if (checkNameFormat() && checkAddressFormat() && checkBirthdayFormat()){
            // update text file.
            try {
                if (Database.findPerson(this._personID) == null){
                     // the information should be inserted into a TXT file,
                    Database.createPerson(this);
                    
                }
            } catch (Exception e) {
                return false;
            }
            // and the addPerson function should return true.
            return true;
        }
        // Otherwise, do not insert into the TXT file and return false.
        else { return false;}
    }

    public boolean updatePersonalDetails(){

        // TODO: This method allows updating a person's ID, firstName, lastName,
        // address and birthday in a TXT file.

        // Changing personal details will not affect demerit points or suspension status.

        // All relevant conditions from addPerson must also be checked here.
        if (meetsAddPersonRequirements()){
            // Condition 1: If a person is under 18, their address cannot be changed.
            return true;
        }

        // Condition 2: If a person's birthday is changed, then no other personal
        // detail (ID, firstName, lastName, address) can be changed.

        // Condition 3: If the first digit of a person's ID is an even number,
        // then their ID cannot be changed.

        // Instruction: If updated info meets all conditions,
        // update the TXT file and return true.
        // Otherwise, do not update and return false.

        return false;
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



    private boolean checkNameFormat(){
        Boolean correctFormat = true;
        int specialCounter = 0;
    
        // Condition 1: PersonID should be exactly 10 characters long;
        if (this._personID.length() != 10) { return false; }

        // the first two characters should be numbers between 2 and 9,
        if (!Character.isDigit(this._personID.charAt(0)) ||
            this._personID.charAt(0) == 0 || this._personID.charAt(0) == 1){
            correctFormat = false;
        }
        if (!Character.isDigit(this._personID.charAt(1)) ||
            this._personID.charAt(1) == 0 || this._personID.charAt(1) == 1){
            correctFormat = false;
        }
        // there should be at least two special characters between characters 3 and 8,
            // inclusive of 3 and 8?
        for (int i = 2; i < this._personID.length(); i++){
            if (!Character.isLetterOrDigit(this._personID.charAt(i))){
                specialCounter += 1;
            }
        }
        if (specialCounter < 2){
            correctFormat = false;
        }
        // and the last two characters should be upper case letters (A - Z).
        if (!Character.isUpperCase(this._personID.charAt(8)) ||
            !Character.isUpperCase(this._personID.charAt(9))){
            correctFormat = false;
        }

        return correctFormat;
    }

    private boolean checkAddressFormat(){
        // Condition 2: The address of the Person should follow the format:
        // StreetNumber|Street|City|State|Country

        // The State should be only Victoria.
        // Example: 32|Highland Street|Melbourne|Victoria|Australia
        Boolean correctFormat = true;
        int pipeCounter = 0;
        String address = this._address;
        
        for (int i = 0; i < address.length(); i++){
            if (address.charAt(i) == '|'){
                pipeCounter += 1;
            }
        }
        if (pipeCounter != 4) { return false; }

        String details [] = address.split("|");
        
        if (details.length != 5){
            correctFormat = false;
        }
        if (!details[3].equalsIgnoreCase("Victoria")){
            correctFormat = false;
        }
        try {
            Integer.parseInt(details[0]);
        } catch (Exception e){
            correctFormat = false;
        }

        return correctFormat;

    }

    private boolean checkBirthdayFormat(){
        // Condition 3: The birth date format should be: DD-MM-YYYY.

        Boolean correctFormat = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); 
        try {
            LocalDate.parse(getBirthdate(), formatter);
        }
        catch (Exception e){
            correctFormat = false;
        }
        return correctFormat;
    }

    // Private Methods - 2. updatePersonalDetails function
    // Calculate age from Person's birthdate
    private int getAge() {
        if (_birthdate == null || _birthdate.isEmpty()) {
            return 0;
        }

        if (checkBirthdayFormat()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            try {
                LocalDate birthDate = LocalDate.parse(_birthdate, formatter);
                LocalDate currentDate = LocalDate.now();
                return Period.between(birthDate, currentDate).getYears();
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }   

    // Condition 1: If a person is under 18, their address cannot be changed.
    public boolean canChangeAddress() {
        return getAge() >= 18;
    }

    // Condition 3: If the first character/digit of a person's ID is an even number, then their ID cannot be changed.
    public boolean canChangeID() {
        if (_personID == null || _personID.isEmpty()){
            return true; // No ID seems fine
        }
        if (Character.isDigit(_personID.charAt(0)) && Character.getNumericValue(_personID.charAt(0)) % 2 != 0) {
            return true; // If it is a digit, check if its odd
        }
        return false;
    }
}
