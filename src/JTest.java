
import org.json.CDL;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
 
public class JTest {
 
    public static void main(String[] args) {
        InputStream inputStream = JTest.class.getClassLoader().getResourceAsStream("2.csv");
        String csvAsString = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        Path path = Paths.get("output.json");
        String json = CDL.toJSONArray(csvAsString).toString();
        try {
            Files.write(path, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}