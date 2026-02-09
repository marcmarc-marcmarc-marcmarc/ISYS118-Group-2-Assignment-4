package Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import DigitalID.Person;

public class Database {
    // Final Vars
    public static final String DB_FILE = "../database.txt";
    public static final String DELIMITER = "|";
    
    // Add Person Object to the Database
    private static void createPerson(Person person) throws IOException {
        ensureDatabaseExists(); // Create DB if necessary

        // Ensure personID doesn't already exist

        // Write person to new line
    }
    
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

    // Helper function to check if DB exists
    private static void ensureDatabaseExists() throws IOException {
        File file = new File(DB_FILE);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

}
