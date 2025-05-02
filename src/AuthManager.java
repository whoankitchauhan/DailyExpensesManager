import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AuthManager {

    // Load users from the users.properties file
    public static Properties loadUsers() throws IOException {
        Properties users = new Properties();
        FileInputStream file = new FileInputStream("config/users.properties");
        users.load(file);
        file.close();
        return users;
    }

    // Method to authenticate a user during login
    public static boolean authenticateUser(String username, String password) {
        try {
            Properties users = loadUsers();
            String storedPassword = users.getProperty(username);
            if (storedPassword != null && storedPassword.equals(password)) {
                return true; // User authenticated successfully
            }
        } catch (IOException e) {
            System.out.println("Error while reading users file: " + e.getMessage());
        }
        return false; // Invalid username or password
    }

    // Method to register a new user
    public static boolean registerUser(String username, String password) {
        try {
            Properties users = loadUsers();
            if (users.containsKey(username)) {
                return false; // User already exists
            } else {
                users.setProperty(username, password);
                // Save to the properties file (to persist the new user)
                saveProperties("config/users.properties", users);
                return true; // User registered successfully
            }
        } catch (IOException e) {
            System.out.println("Error while writing to users file: " + e.getMessage());
        }
        return false;
    }

    private static void saveProperties(String filePath, Properties properties) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            properties.store(outputStream, null);
        }
    }

}
