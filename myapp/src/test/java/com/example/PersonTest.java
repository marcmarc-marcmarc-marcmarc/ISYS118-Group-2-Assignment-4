package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

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
        assertFalse(person.getPersonID() == personID, "personID shorter than 10 characters should fail");
    }

    /**
     * b - personID length >= 10 (Pass)
     * All conditions satisfied with exactly 10 characters.
     */
    @Test
    @DisplayName("test Person ID Length Exactly Ten")
    public void testPersonIDLengthExactlyTen() {
        String personID = "56@#abcZAB"; 
        assertTrue(person.getPersonID() == personID, "personID of exactly 10 characters should pass");
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
        person.addPerson(); 
        assertFalse(person.getPersonID() == personID, "personID with non-digit first two characters should fail");
    }

    /**
     * d - Only ONE of the first two characters is a digit between 2-9 (Fail)
     */
    @Test
    @DisplayName("test Person ID Only One First Char Digit")
    public void testPersonIDOnlyOneFirstCharDigit() {
        String personID = "51@#abcZAB";
        person.setPersonID(personID);
        person.addPerson();
        assertFalse(person.getPersonID() == personID, "personID with only one valid leading digit should fail");
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
        person.addPerson(); 
        assertTrue(person.getPersonID() == personID, "personID with both first chars as digits 2-9 should pass");
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
        person.addPerson(); 
        assertFalse(person.getPersonID() == personID, "personID with fewer than two special chars in positions 3-8 should fail");
    }

    /**
     * g - Exactly two special characters between characters 3 and 8 (indices 2–7) (Pass)
     */
    @Test
    @DisplayName("test Person ID Exactly Two Special Chars")
    public void testPersonIDExactlyTwoSpecialChars() {
        String personID = "56@#abcdAB";
        person.setPersonID(personID);
        person.addPerson(); 
        assertTrue(person.getPersonID() == personID, "personID with exactly two special chars in positions 3-8 should pass");
    }

    /**
     * h - More than two special characters between characters 3 and 8 (indices 2–7) (Pass)
     */
    @Test
    @DisplayName("test Person ID More Than Two Special Chars")
    public void testPersonIDMoreThanTwoSpecialChars() {
        String personID = "56@#!acdAB";
        person.setPersonID(personID);
        person.addPerson(); 
        assertTrue(person.getPersonID() == personID, "personID with more than two special chars in positions 3-8 should pass");
    }

    /**
     * i - Neither of the last two characters is an uppercase letter (Fail)
     */
    @Test
    @DisplayName("test Person ID Last Two Chars Not Uppercase")
    public void testPersonIDLastTwoCharsNotUppercase() {
        String personID = "56@#abcdab";
        person.setPersonID(personID);
        person.addPerson(); 
        assertFalse(person.getPersonID() == personID, "personID with no uppercase last two characters should fail");
    }

    /**
     * j - Only ONE of the last two characters is an uppercase letter (Fail)
     */
    @Test
    @DisplayName("test Person ID Only One Last Char Uppercase")
    public void testPersonIDOnlyOneLastCharUppercase() {
        String personID = "56@#abcdAb";
        person.setPersonID(personID);
        person.addPerson(); 
        assertFalse(person.getPersonID() == personID, "personID with only one uppercase last character should fail");
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
        person.addPerson(); 
        assertTrue(person.getPersonID() == personID, "personID with both last characters uppercase should pass");
    }
}
