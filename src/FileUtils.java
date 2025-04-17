import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FileUtils {

    // Method to save properties to a file
    public static void saveProperties(String filePath, Properties properties) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            properties.store(outputStream, null);
        }
    }
}
