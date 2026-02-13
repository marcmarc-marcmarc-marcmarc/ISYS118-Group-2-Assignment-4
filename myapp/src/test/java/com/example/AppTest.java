package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import DigitalID.Person;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Unit tests for Person.addDemeritPoints() method
 * Written by: Mustafa Khoraidah (s3819189)
 * 
 * This test class contains 5 comprehensive test cases for the addDemeritPoints() method:
 * TC-11: Valid demerit points addition
 * TC-12: Invalid offense date format
 * TC-13: Points outside valid range (1-6)
 * TC-14: Under 21 exceeding 6 points triggers suspension
 * TC-15: Over 21 not exceeding 12 points - no suspension
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {
    private static final String DB_FILE = "database.txt";

    /**
     * Clean up database file before each test to ensure a clean state.
     */
    @BeforeEach
    public void setUp() {
        deleteTestFiles();
    }

    /**
     * Clean up database file after each test to avoid pollution.
     */
    @AfterEach
    public void tearDown() {
        deleteTestFiles();
    }

    /**
     * Helper method to delete test files.
     */
    private void deleteTestFiles() {
        try {
            Files.deleteIfExists(Paths.get(DB_FILE));
        } catch (IOException e) {
            System.err.println("Error deleting test files: " + e.getMessage());
        }
    }

    // ==================== Test Cases for addDemeritPoints() ====================

    /**
     * Test Case 11: addDemeritPoints() with valid data (offense date format correct, points within range)
     * Test Data: Valid offense date "15-11-2024", valid points 3
     * Expected Result: Returns "Success" and demerit points are added
     */
    @Test
    @Order(11)
    @DisplayName("TC-11: Valid demerit points addition")
    public void testAddDemeritPointsValid() {
        // Arrange
        Person person = new Person("56s_d%&fAB", "John", "Doe", 
                                   "32|Highland Street|Melbourne|Victoria|Australia", 
                                   "15-11-1990");
        person.addPerson();

        // Act
        String result = person.addDemeritPoints("15-11-2024", 3);

        // Assert
        assertEquals("Success", result, "Adding valid demerit points should return 'Success'");
        assertFalse(person.getIsSuspended(), "Person should not be suspended with only 3 points");
    }

    /**
     * Test Case 12: addDemeritPoints() with invalid offense date format
     * Test Data: Invalid date format "2024-11-15" (YYYY-MM-DD instead of DD-MM-YYYY), valid points 3
     * Expected Result: Returns "Failed"
     */
    @Test
    @Order(12)
    @DisplayName("TC-12: Invalid offense date format")
    public void testAddDemeritPointsInvalidDateFormat() {
        // Arrange
        Person person = new Person("56s_d%&fAB", "John", "Doe", 
                                   "32|Highland Street|Melbourne|Victoria|Australia", 
                                   "15-11-1990");
        person.addPerson();

        // Act
        String result = person.addDemeritPoints("2024-11-15", 3);

        // Assert
        assertEquals("Failed", result, "Invalid date format should return 'Failed'");
    }

    /**
     * Test Case 13: addDemeritPoints() with points outside valid range (1-6)
     * Test Data: Valid date "15-11-2024", invalid points 7 (exceeds maximum of 6)
     * Expected Result: Returns "Failed"
     */
    @Test
    @Order(13)
    @DisplayName("TC-13: Points outside valid range")
    public void testAddDemeritPointsInvalidPointsRange() {
        // Arrange
        Person person = new Person("56s_d%&fAB", "John", "Doe", 
                                   "32|Highland Street|Melbourne|Victoria|Australia", 
                                   "15-11-1990");
        person.addPerson();

        // Act - Test with points above maximum (7)
        String result1 = person.addDemeritPoints("15-11-2024", 7);
        
        // Test with points below minimum (0)
        String result2 = person.addDemeritPoints("15-11-2024", 0);

        // Assert
        assertEquals("Failed", result1, "Points above 6 should return 'Failed'");
        assertEquals("Failed", result2, "Points below 1 should return 'Failed'");
    }

    /**
     * Test Case 14: addDemeritPoints() for person under 21 exceeding 6 points triggers suspension
     * Test Data: Person born "15-11-2010" (age ~16), adding multiple demerit points totaling >6
     * Expected Result: Returns "Success" and isSuspended becomes true
     */
    @Test
    @Order(14)
    @DisplayName("TC-14: Under 21 exceeding 6 points triggers suspension")
    public void testAddDemeritPointsUnder21Suspension() {
        // Arrange - Create person under 21 years old
        Person person = new Person("56s_d%&fAB", "Jane", "Smith", 
                                   "32|Highland Street|Melbourne|Victoria|Australia", 
                                   "15-11-2010"); // Age ~16
        person.addPerson();

        // Act - Add demerit points that total more than 6
        String result1 = person.addDemeritPoints("15-01-2025", 4);
        assertEquals("Success", result1);
        assertFalse(person.getIsSuspended(), "Should not be suspended yet with 4 points");
        
        String result2 = person.addDemeritPoints("15-06-2025", 3);
        assertEquals("Success", result2);
        
        // Assert - Total is 7 points, which exceeds 6 for under 21
        assertTrue(person.getIsSuspended(), "Person under 21 with >6 points should be suspended");
    }

    /**
     * Test Case 15: addDemeritPoints() for person over 21 not exceeding 12 points - no suspension
     * Test Data: Person born "15-11-1990" (age ~35), adding points totaling ≤12
     * Expected Result: Returns "Success" and isSuspended remains false
     */
    @Test
    @Order(15)
    @DisplayName("TC-15: Over 21 not exceeding 12 points - no suspension")
    public void testAddDemeritPointsOver21NoSuspension() {
        // Arrange - Create person over 21 years old
        Person person = new Person("56s_d%&fAB", "Robert", "Johnson", 
                                   "32|Highland Street|Melbourne|Victoria|Australia", 
                                   "15-11-1990"); // Age ~35
        person.addPerson();

        // Act - Add demerit points that total 10 (not exceeding 12)
        String result1 = person.addDemeritPoints("15-01-2025", 6);
        assertEquals("Success", result1);
        assertFalse(person.getIsSuspended(), "Should not be suspended yet with 6 points");
        
        String result2 = person.addDemeritPoints("15-06-2025", 4);
        assertEquals("Success", result2);
        
        // Assert - Total is 10 points, which does not exceed 12 for over 21
        assertFalse(person.getIsSuspended(), "Person over 21 with ≤12 points should NOT be suspended");
    }
}
