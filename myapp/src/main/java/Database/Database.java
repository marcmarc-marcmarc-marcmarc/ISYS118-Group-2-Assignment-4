package Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DigitalID.Person;

public class Database {
    // Static Variables
    public static final String DB_FILE = "../database.txt";
    public static final String DELIMITER = "|";
    
    // Add Person Object to the Database
    public static void createPerson(Person person) throws IOException {
        ensureDatabaseExists(); // Create DB if necessary

        // Ensure personID doesn't already exist
        if (findPerson(person.getPersonID()) != null) {
            throw new IllegalArgumentException("Person with ID " + person.getPersonID() + " already exists!!!");
        }
        
        // Write person to new line
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DB_FILE, true))) {
            writer.write(person.toFileLine(DELIMITER));
            writer.newLine();
        }

    }
    
    // Update existing Person within DB
    public static void updatePerson(Person updatedPerson) throws IOException {
        File file = new File(DB_FILE);
        if (!file.exists()) {
            // Throw an error if file doesn't exist. We can't update a non-existent entry
            throw new IOException("Database file not found.... can't update non-existent record");
        }
        
        List<String> allLines = new ArrayList<>();
        boolean found = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Person person = Person.fromFileLine(line, DELIMITER);
                if (person != null && person.getPersonID().equals(updatedPerson.getPersonID())) {
                    allLines.add(updatedPerson.toFileLine(DELIMITER));
                    found = true;
                } else {
                    allLines.add(line);
                }
            }
        }
        
        if (!found) {
            throw new IllegalArgumentException("Person with ID " + updatedPerson.getPersonID() + " not found");
        }
        
        writeAllLines(allLines);
    }

    // Search for person within DB by _personID
    public static Person findPerson(String personID) throws IOException {
        File file = new File(DB_FILE);
        if (!file.exists()) {
            return null;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Person person = Person.fromFileLine(line, DELIMITER);
                if (person != null && person.getPersonID().equals(personID)) {
                    return person;
                }
            }
        }
        return null;
    }

    // Helper function to write all lines back into DB
    private static void writeAllLines(List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DB_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    // Helper function to check if DB exists
    private static void ensureDatabaseExists() throws IOException {
        File file = new File(DB_FILE);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

}
