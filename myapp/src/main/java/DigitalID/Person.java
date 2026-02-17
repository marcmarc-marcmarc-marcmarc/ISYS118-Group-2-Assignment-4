package DigitalID;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Date;

import Database.Database;


public class Person {
    // Instance Variables
    private String _internalID; // immutable key used for object storage and retrieval
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
        this._internalID = UUID.randomUUID().toString();
        this._personID = personID;
        this._firstName = firstName;
        this._lastName = lastName;
        this._address = address;
        this._birthdate = birthdate;
        this._demeritPoints = new HashMap<>();
        this._isSuspended = false;
    }

    private Person(String internalID, String personID, String firstName, String lastName,
               String address, String birthdate, HashMap<Date, Integer> demeritPoints, boolean isSuspended) {
        this._internalID    = internalID; // Restored from file — not regenerated
        this._personID      = personID;
        this._firstName     = firstName;
        this._lastName      = lastName;
        this._address       = address;
        this._birthdate     = birthdate;
        this._demeritPoints = demeritPoints;
        this._isSuspended   = isSuspended;
    }

    
    // Public Methods
    public String getInternalID() { return _internalID; }
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
    
    /**
     * Convert the _demeritPoints Mapping into a string
     * @return _demeritPoints as date:points in formatted string 
     * */
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

    // 
    /**
     * Map the concatenated string of date:int into HashMap<Date, Integer>
     * @param data, _demeritPoints as date:points in string format
     * @return Mapping of _demeritPoints as HashMap<Date, Integer> 
     * */
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

    /**
    * Convert Person object to file line for database entry
    * @param delimiter, char to delimit fields during string serialisation
    * @return Person fields in single string format delimited by `delimiter`
    */
    public String toFileLine(String delimiter) {
        StringBuilder sb = new StringBuilder();
        sb.append(_internalID).append(delimiter);
        sb.append(_personID).append(delimiter);
        sb.append(_firstName).append(delimiter);
        sb.append(_lastName).append(delimiter);
        sb.append(_address).append(delimiter);
        sb.append(_birthdate).append(delimiter);
        sb.append(serialiseDemeritPoints()).append(delimiter);
        sb.append(_isSuspended).append(delimiter);
        return sb.toString();
    }

    /**
     * Convert database entry to Person Object
     * @param line
     * @param delimiter char to regex matchduring string deserialisation
     * @return Person from file line in Object representation
     */
    public static Person fromFileLine(String line, String delimiter) {
        if (line == null || line.trim().isEmpty()) { return null; }
        String[] parts = line.split("\\" + delimiter);
        if (parts.length < 8) { return null; } // Must contain all data members

        String _internalID = parts[0];
        String _personID = parts[1];
        String _firstName = parts[2];
        String _lastName = parts[3];
        String _address = parts[4];
        String _birthdate = parts[5];
        HashMap<Date, Integer> _demeritPoints = deserialiseDemeritPoints(parts[6]);
        boolean _isSuspended = Boolean.parseBoolean(parts[7]);

        // Return Person
        return new Person(_internalID, _personID, _firstName, _lastName, _address, 
                    _birthdate, _demeritPoints, _isSuspended);
    }
    
    // Methods
    /**
     * Helper method for addPerson condition aggregation
     * @return true if (checkNameFormat, checkAddressFormat, & checkBirthdayFormat) == true
     */
    public boolean meetsAddPersonRequirements(){
        if (checkNameFormat() && checkAddressFormat() && checkBirthdayFormat()){
            return true;
        }
        return false;
    }

    /**
     * Method to add person to TXT file ifcheckNameFormat, checkAddressFormat, 
     * & checkBirthdayFormat requirements are met
     * @return true if Person object is successfully added to TXT file
     */
    public boolean addPerson(){
        // add to file if the Person's information meets the required conditions
        // and the personID does not already exist

        // if all three helper methods return true, then the addPerson() conditions
        // have all been met, and the function adds the person and return true.
        if (checkNameFormat() && checkAddressFormat() && checkBirthdayFormat()){
            // update text file.
            try {
                // ensure person does not already exist in file
                if (Database.findPersonByInternalID(this._internalID) == null){
                    // create and add person to TXT file,
                    Database.createPerson(this);
                    
                }
            } catch (Exception e) {
                // if can't add person to file, fucntion returns false
                return false;
            }
            // and the addPerson function should return true.
            return true;
        }
        // if conditions not met, do not insert into the TXT file and return false.
        else { return false;}
    }
    /**
     * 
     * @return true if person.updatePersonalDetails() succeed and TXT file is updated
     */
    public boolean updatePersonalDetails(){
        if (meetsAddPersonRequirements()) { // All conditions from addPerson are checked here.
            try {
                // Fetch existing record from the database to compare against
                Person existing = Database.findPersonByInternalID(_internalID);

                // If person doesn't exist -> return false.
                if (existing == null) {
                    return false;
                }
                // Compare fields against current object, flagging field changes
                boolean isUpdatingPersonID  = !_personID.equals(existing.getPersonID());
                boolean isUpdatingFirstName = !_firstName.equals(existing.getFirstName());
                boolean isUpdatingLastName  = !_lastName.equals(existing.getLastName());
                boolean isUpdatingAddress   = !_address.equals(existing.getAddress());
                boolean isUpdatingBirthdate = !_birthdate.equals(existing.getBirthdate());

                    // Check preconditions via canUpdateFields()
                    if (!canUpdateFields(isUpdatingBirthdate, isUpdatingPersonID,
                                        isUpdatingFirstName, isUpdatingLastName, isUpdatingAddress)) {
                        return false;
                    }
        
                    // updated info meets all conditions -> update the TXT file and return true.
                    Database.updatePerson(this);
                    return true;
                } catch (Exception e) {
                return false;
            }
        }
        // Otherwise, do not update and return false
        return false;
    }

    /**
     * Method to add demerit point deduction to person mapping
     * @param offenseDate date of offense in DD-MM-YYYY format
     * @param points points correlated to associated offense
     * @return 'Success' on success, 'Failed' on failure
     */
    public String addDemeritPoints(String offenseDate, int points){
        // Condition 1: Offense date format must be DD-MM-YYYY.
        if (!checkOffenseDateFormat(offenseDate)) {
            return "Failed";
        }

        // Condition 2: Demerit points must be a whole number between 1–6.
        if (points < 1 || points > 6) {
            return "Failed";
        }

        try {
            // Parse the offense date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate offenseLocalDate = LocalDate.parse(offenseDate, formatter);
            Date offenseDateObj = new Date(offenseLocalDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli());

            // Add the demerit points to the HashMap
            _demeritPoints.put(offenseDateObj, points);

            // Condition 3: Check suspension based on age and total points within 2 years
            LocalDate currentDate = LocalDate.now();
            LocalDate twoYearsAgo = currentDate.minusYears(2);
            
            int totalPoints = 0;
            for (Map.Entry<Date, Integer> entry : _demeritPoints.entrySet()) {
                LocalDate entryDate = entry.getKey().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
                
                // Count points within the last 2 years
                if (!entryDate.isBefore(twoYearsAgo)) {
                    totalPoints += entry.getValue();
                }
            }

            // Determine suspension based on age
            int age = getAge();
            if (age < 21) {
                // If person is under 21 → isSuspended = true if total demerit points within two years exceed 6
                _isSuspended = (totalPoints > 6);
            } else {
                // If person is over 21 → isSuspended = true if total demerit points within two years exceed 12
                _isSuspended = (totalPoints > 12);
            }

            // Update the person record in the database
            Database.updatePerson(this);
            
            return "Success";
        } catch (Exception e) {
            return "Failed";
        }
    }

    /**
     * Helper method to validate offense date format (DD-MM-YYYY)
     */
    private boolean checkOffenseDateFormat(String offenseDate) {
        if (offenseDate == null || offenseDate.isEmpty()) {
            return false;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate.parse(offenseDate, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
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
        // rerquirements vague, chosent to be inclusive of chars 3 and 8.
        for (int i = 2; i < this._personID.length(); i++){
            if (!Character.isLetterOrDigit(this._personID.charAt(i))){
                specialCounter += 1;
            }
        }
        if (specialCounter < 2){
            // less than 2 characters so name format is false
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

        
        // Example: 32|Highland Street|Melbourne|Victoria|Australia
        boolean correctFormat = true;
        int pipeCounter = 0;
        String address = this._address;
        
        // ensure correct amount of |, break early if incorrect
        for (int i = 0; i < address.length(); i++){
            if (address.charAt(i) == '|'){
                pipeCounter += 1;
            }
        }
        if (pipeCounter != 4) { return false; }
          
        String[] details = address.split("\\|");    
      
        if (correctFormat){
            // confirm that all fields have content, i.e. no || together
            if (details.length != 5){
                correctFormat = false;
            }
            // The State should be only Victoria.
            // case sensitive format checking for Victoria
            if (!details[3].equals("Victoria")){
                correctFormat = false;
            }
            // check that address number is an integer, catch if not.
            try {
                Integer.parseInt(details[0]);
            } catch (Exception e){
                correctFormat = false;
            }
        }

        return correctFormat;
    }

    private boolean checkBirthdayFormat(){
        // Condition 3: The birth date format should be: DD-MM-YYYY.

        Boolean correctFormat = true;
        
        // set date pattern to DD-MM-YYYY
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); 
        try {
            // check date matches pattern, additionally checks date is a viable date
            LocalDate.parse(getBirthdate(), formatter);
        }
        catch (Exception e){
            // date has failed, method must return false.
            correctFormat = false;
        }
        return correctFormat;
    }

    // Private Methods - 2. updatePersonalDetails function
    // Calculate age from Person's birthdate
    private int getAge() {
        // If the person does not have a birthday return 0
        if (_birthdate == null || _birthdate.isEmpty()) {
            return 0;
        }

        if (checkBirthdayFormat()) {
            // Match this pattern
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            try {
                LocalDate birthDate = LocalDate.parse(_birthdate, formatter);
                LocalDate currentDate = LocalDate.now();
                // Return birthday - current date
                return Period.between(birthDate, currentDate).getYears();
            } catch (Exception e) {
                // If error occurs return 0
                return 0;
            }
        }
        // If we hit this block return 0
        return 0;
    }   

    // Condition 1: If a person is under 18, their address cannot be changed.
    public boolean canChangeAddress() {
        // If person is 18 or older return true
        return getAge() >= 18;
    }

    // Condition 3: If the first character/digit of a person's ID is an even number, then their ID cannot be changed.
    public boolean canChangeID() {
        if (_personID == null || _personID.isEmpty()){
            return true; // No ID seems fine
        }
        if (Character.isDigit(_personID.charAt(0)) && Character.getNumericValue(_personID.charAt(0)) % 2 != 0) {
            return true; // If it is a digit, check if its odd -> return true
        }
        return false;
    }

    // Helper method to validate batch updates (multiple fields at once)
    public boolean canUpdateFields(boolean isUpdatingBirthDate, 
                                    boolean isUpdatingPersonID, 
                                    boolean isUpdatingFirstName,
                                    boolean isUpdatingLastName,
                                    boolean isUpdatingAddress) {
        // Condition 2: If birthdate is changing no other fields can change
        if (isUpdatingBirthDate && (isUpdatingPersonID || isUpdatingFirstName || 
            isUpdatingLastName || isUpdatingAddress)) {
            return false; 
        }

        // Condition 1: Check can change address if applicable
        if (isUpdatingAddress && !canChangeAddress()) {
            return false;
        }

        // Condition 3: Check can change ID if applicable
        if (isUpdatingPersonID && !canChangeID()) {
            return false;
        }

        return true;
    }
}
