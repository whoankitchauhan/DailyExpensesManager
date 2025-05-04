import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AuthManager {

    // Loads user credentials from the users.properties file
    public static Properties loadUsers() throws IOException {
        Properties users = new Properties();
        FileInputStream file = new FileInputStream("config/users.properties");
        users.load(file);
        file.close();
        return users;
    }

    // Verifies if the entered credentials match existing user data
    public static boolean authenticateUser(String username, String password) {
        try {
            Properties users = loadUsers();
            String storedPassword = users.getProperty(username);
            return storedPassword != null && storedPassword.equals(password);
        } catch (IOException e) {
            System.out.println("Error while reading users file: " + e.getMessage());
        }
        return false;
    }

    // Registers a new user by adding credentials to the users.properties file
    public static boolean registerUser(String username, String password) {
        try {
            Properties users = loadUsers();
            if (users.containsKey(username)) {
                return false; // Username already taken
            } else {
                users.setProperty(username, password);
                saveProperties("config/users.properties", users);
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error while writing to users file: " + e.getMessage());
        }
        return false;
    }

    // Saves updated properties back to the specified file path
    private static void saveProperties(String filePath, Properties properties) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            properties.store(outputStream, null);
        }
    }
}
