package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import DigitalID.Person;


/**
 * Unit tests for the Person class.
 */
public class PersonTest {

    private Person person;

    /**
     * create Person object prior to each test.
     */
    @BeforeEach
    public void setUp() {
        person = new Person();
    }

    /**
     * Clean up after each test .
     */
    @AfterEach
    public void tearDown() {
        person = null;
    }


    // -------------------------------------------------------------------------
    // getPersonID() - personID Validation Tests
    // Condition 1: personID must be exactly 10 characters long (a, b)
    // Condition 2: First two characters must be digits between 2 and 9 (c, d, e)
    // Condition 3: At least two special characters between characters 3 and 8 (f, g, h)
    // Condition 4: Last two characters must be uppercase letters (A-Z) (i, j, k)
    // -------------------------------------------------------------------------

    /**
     * a - personID length < 10 (Fail)
     * All other conditions are valid; only length is violated.
     */
    @Test
    @DisplayName("test Person ID Length Too Short")
    public void testPersonIDLengthTooShort() {
        String personID = "23@#abcAB"; 
        person.setPersonID(personID);
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        assertFalse(person.checkNameFormat(), "personID shorter than 10 characters should fail");
    }

    /**
     * b - personID length >= 10 (Pass)
     * All conditions satisfied with exactly 10 characters.
     */
    @Test
    @DisplayName("test Person ID Length Exactly Ten")
    public void testPersonIDLengthExactlyTen() {
        String personID = "564#**cZAB"; 
        person.setPersonID(personID);
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        assertTrue(person.checkNameFormat(), "personID of exactly 10 characters should pass");
    }

    /**
     * c - First two characters are NOT digits between 2-9 (Fail)
     * Both first characters are outside the valid range (e.g. letters).
     */
    @Test
    @DisplayName("test Person ID First Two Chars Not Digits")
    public void testPersonIDFirstTwoCharsNotDigits() {
        String personID = "AB@#abcZAB";
        person.setPersonID(personID);
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        person.addPerson(); 
        assertFalse(person.checkNameFormat(), "personID with non-digit first two characters should fail");
    }

    /**
     * d - Only ONE of the first two characters is a digit between 2-9 (Fail)
     */
    @Test
    @DisplayName("test Person ID Only One First Char Digit")
    public void testPersonIDOnlyOneFirstCharDigit() {
        String personID = "5A@#abcZAB";
        person.setPersonID(personID);
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        person.addPerson();
        assertFalse(person.checkNameFormat(), "personID with only one valid leading digit should fail");
    }

    /**
     * e - Both first two characters are digits between 2-9 (Pass)
     * All other conditions are also satisfied.
     */
    @Test
    @DisplayName("test Person ID Both First Chars Valid Digits")
    public void testPersonIDBothFirstCharsValidDigits() {
        String personID = "37@#abcZAB";
        person.setPersonID(personID);
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        person.addPerson(); 
        assertTrue(person.checkNameFormat(), "personID with both first chars as digits 2-9 should pass");
    }

    /**
     * f - Fewer than two special characters between characters 3 and 8 (indices 2–7) (Fail)
     * Only one special character present in that range.
     */
    @Test
    @DisplayName("test Person ID Too Few Special Chars")
    public void testPersonIDTooFewSpecialChars() {
        String personID = "56@abcdeAB";
        person.setPersonID(personID);
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        person.addPerson(); 
        assertFalse(person.checkNameFormat(), "personID with fewer than two special chars in positions 3-8 should fail");
    }

    /**
     * g - Exactly two special characters between characters 3 and 8 (indices 2–7) (Pass)
     */
    @Test
    @DisplayName("test Person ID Exactly Two Special Chars")
    public void testPersonIDExactlyTwoSpecialChars() {
        String personID = "56@#abcdAB";
        person.setPersonID(personID);
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        person.addPerson(); 
        assertTrue(person.checkNameFormat(), "personID with exactly two special chars in positions 3-8 should pass");
    }

    /**
     * h - More than two special characters between characters 3 and 8 (indices 2–7) (Pass)
     */
    @Test
    @DisplayName("test Person ID More Than Two Special Chars")
    public void testPersonIDMoreThanTwoSpecialChars() {
        String personID = "56@#!acdAB";
        person.setPersonID(personID);
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        person.addPerson(); 
        assertTrue(person.checkNameFormat(), "personID with more than two special chars in positions 3-8 should pass");
    }

    /**
     * i - Neither of the last two characters is an uppercase letter (Fail)
     */
    @Test
    @DisplayName("test Person ID Last Two Chars Not Uppercase")
    public void testPersonIDLastTwoCharsNotUppercase() {
        String personID = "56@#abcdab";
        person.setPersonID(personID);
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        person.addPerson(); 
        assertFalse(person.checkNameFormat(), "personID with no uppercase last two characters should fail");
    }

    /**
     * j - Only ONE of the last two characters is an uppercase letter (Fail)
     */
    @Test
    @DisplayName("test Person ID Only One Last Char Uppercase")
    public void testPersonIDOnlyOneLastCharUppercase() {
        String personID = "56@#abcdAb";
        person.setPersonID(personID);
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        person.addPerson(); 
        assertFalse(person.checkNameFormat(), "personID with only one uppercase last character should fail");
    }

    /**
     * k - Both of the last two characters are uppercase letters (Pass)
     * All other conditions are also satisfied.
     */
    @Test
    @DisplayName("test Person ID Both Last Chars Uppercase")
    public void testPersonIDBothLastCharsUppercase() {
        String personID = "56@#abcdAB";
        person.setPersonID(personID);
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        person.addPerson(); 
        assertTrue(person.checkNameFormat(), "personID with both last characters uppercase should pass");
    }

    // -------------------------------------------------------------------------
    // updatePersonalDetails() - updatePersonalDetails Validation Tests
    // Condition 1: If a person is under 18, their address cannot be changed. (a, b, c)
    // Condition 2: If a person's birthday is going to be changed, then no other field
    //              (i.e, person's ID, firstName, lastName, address) can be changed. (d, e, f)
    // Condition 3: If the first character/digit of a person's ID is an even number,
    //              then their ID cannot be changed. (g, h, i)
    // -------------------------------------------------------------------------

    /**
     * a - Update address when person is under 18 (Fail)
     * A person under 18 (Age = 16) should not be allowed to change their address.
     */
    @Test
    @DisplayName("test Update Address When Person Is Under 18")
    public void testUpdateAddressPersonUnder18() {
        assertEquals(null, person.getPersonID(), "personID should be null from default constructor.");
        String pastDate = LocalDate.now().minusYears(16).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")); // 16 Y.O in dd-MM-yyyy format
        person.setBirthdate(pastDate); 
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        boolean result = person.updatePersonalDetails();
        assertFalse(result, "Updating address for a person under 18 should fail");
    }

    /**
     * b - Update address when person is 18 (Pass)
     * A person who is 18 should be allowed to change their address.
     */
    @Test
    @DisplayName("test Update Address When Person Is 18")
    public void testUpdateAddressPersonIs18() {
        assertEquals(null, person.getPersonID(), "personID should be null from default constructor.");
        String pastDate = LocalDate.now().minusYears(18).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")); // 18 Y.O in dd-MM-yyyy format
        person.setBirthdate(pastDate); 
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        boolean result = person.updatePersonalDetails();
        assertTrue(result, "Updating address for a person who is exactly 18 should pass");
    }

    /**
     * c - Update address when person's age is null (Fail)
     * If age has not been set, address cannot be changed.
     */
    @Test
    @DisplayName("test Update Address When Person Age Is Null")
    public void testUpdateAddressPersonAgeIsNull() {
        assertEquals(null, person.getPersonID(), "personID should be null from default constructor."); 
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        boolean result = person.updatePersonalDetails();
        assertFalse(result, "Updating address when age is null should fail");
    }

    /**
     * d - Update birthdate and update first name (Fail)
     * If birthdate is being changed, no other fields can change simultaneously.
     */
    @Test
    @DisplayName("test Update Birthdate And First Name Together")
    public void testUpdateBirthdateAndFirstName() {
        assertEquals(null, person.getPersonID(), "personID should be null from default constructor.");
        String pastDate = LocalDate.now().minusYears(25).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")); // 25 Y.O in dd-MM-yyyy format
        person.setBirthdate(pastDate); 
        person.setPersonID("12!@qwerTY");
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        boolean result = person.updatePersonalDetails();
        assertFalse(result, "Updating birthdate alongside firstName should fail");
    }

    /**
     * e - Update birthdate and all other fields (Fail)
     * Changing birthdate while also changing other fields (personID, firstName, lastName, address) not permitted.
     */
    @Test
    @DisplayName("test Update Birthdate And All Other Fields")
    public void testUpdateBirthdateAndAllOtherFields() {
        assertEquals(null, person.getPersonID(), "personID should be null from default constructor.");
        String pastDate = LocalDate.now().minusYears(25).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")); // 25 Y.O in dd-MM-yyyy format
        person.setBirthdate(pastDate); 
        person.setPersonID("12!@qwerTY");
        person.setFirstName("NewFirstName");
        person.setLastName("NewLastName");
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        boolean result = person.updatePersonalDetails();
        assertFalse(result, "Updating birthdate alongside all other fields should fail");
    }

    /**
     * f - Update all fields except birthdate (Pass)
     * Changing personID, firstName, lastName, and address without touching
     * birthdate should be permitted (assuming other conditions are met).
     */
    @Test
    @DisplayName("test Update All Fields Except Birthdate")
    public void testUpdateAllFieldsExceptBirthdate() {
        person.setPersonID("12!@qwerTY");
        person.setFirstName("NewFirstName");
        person.setLastName("NewLastName");
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        boolean result = person.updatePersonalDetails();
        assertTrue(result, "Updating all fields except birthdate should pass");
    }

    /**
     * g - Update personID when first character of personID is an even-numbered digit (Fail)
     * If the first digit of the current personID is even, the ID cannot be changed.
     */
    @Test
    @DisplayName("test Update PersonID When First Char Is Even Digit")
    public void testUpdatePersonIDFirstCharEvenDigit() {
        assertEquals(null, person.getPersonID(), "personID should be null from default constructor.");
        person.setPersonID("23!@qwerTY");
        person.setFirstName("NewFirstName");
        person.setLastName("NewLastName");
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        boolean result = person.updatePersonalDetails();
        assertTrue(result, "Updating fields & setting PersonID.char(0) to even numbered int should pass");
        person.setPersonID("42!@qwerTY");
        result = person.updatePersonalDetails();
        assertFalse(result, "Updating personID when first digit is even should fail");
    }

    /**
     * h - Update personID when first character of personID is null (Pass)
     * If the personID has not been set, the ID can be changed freely.
     */
    @Test
    @DisplayName("test Update PersonID When First Char Is Null")
    public void testUpdatePersonIDFirstCharIsNull() {
        assertEquals(null, person.getPersonID(), "personID should be null from default constructor.");
        person.setPersonID("12!@qwerTY");
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        boolean result = person.updatePersonalDetails();
        assertTrue(result, "Updating personID when existing personID is null should pass");
    }

    /**
     * i - Update personID when first character of personID is an odd-numbered digit (Pass)
     * If the first digit of the current personID is odd, the ID can be changed.
     */
    @Test
    @DisplayName("test Update PersonID When First Char Is Odd Digit")
    public void testUpdatePersonIDFirstCharOddDigit() {
        assertEquals(null, person.getPersonID(), "personID should be null from default constructor.");
        person.setPersonID("12!@qwerTY");
        String pastDate = LocalDate.now().minusYears(25).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")); // 25 Y.O in dd-MM-yyyy format
        person.setBirthdate(pastDate); 
        person.setAddress("32|Highland Street|Melbourne|Victoria|Australia");
        boolean result = person.updatePersonalDetails();
        assertTrue(result, "Updating personID when first digit is odd should pass");
    }
}
