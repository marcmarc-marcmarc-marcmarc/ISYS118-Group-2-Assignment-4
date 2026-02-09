package Database;

import java.io.File;
import java.io.IOException;

public class Database {
    // final Vars
    public static final String DB_FILE = "../database.txt";
    public static final String DELIMITER = "|";
    
    // Check if DB exists
    private static void checkDatabaseExists() throws IOException {
        File file = new File(DB_FILE);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
}
