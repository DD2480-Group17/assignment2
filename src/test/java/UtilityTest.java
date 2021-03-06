import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {
    /**
     * testClearDirectory tests the functionality of the method clearDirectory. This is done by adding three files to
     * the directory test_build_history and then call on the method clearDirectory.
     * <p>
     * Expected output: length of the files in the directory should be 0 or null
     */
    @Test
    public void testClearDirectory() {
        File dir = new File("test_build_history");
        if (!dir.exists())
            dir.mkdir();
        File file = new File("test_build_history/test 20T03T20T30");
        File file1 = new File("test_build_history/test 20T01T20T30");
        File file2 = new File("test_build_history/test 20T03T19T30");
        try {

            FileWriter writer = new FileWriter(file);
            writer.write("hello");
            writer.close();

            writer = new FileWriter(file1);
            writer.write("hello");
            writer.close();

            writer = new FileWriter(file2);
            writer.write("hello");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        Utility.clearDirectory("test_build_history");

        File[] files = dir.listFiles();
        if (files != null) {
            assertEquals(0, files.length);
        }
    }

    @Test
    @DisplayName("If token exists, test that it has a size > 0")
    public void testReadTokenExisting() {
        try {
            Path newFilePath = Paths.get(".token");
            Files.createFile(newFilePath);
            String token = Utility.readToken();
            assertTrue(token.length() > 0);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    @DisplayName("If token does not exist, create a new file and test that it is read correctly")
    public void testReadTokenNew() {
        try {
            Path newFilePath = Paths.get(".token");
            Files.createFile(newFilePath);
            FileWriter writer = new FileWriter(".token");
            writer.write("testToken");
            writer.close();
            BufferedReader reader = new BufferedReader(new FileReader(new File(".token")));
            String token = reader.readLine();
            assertEquals("testToken", token);
        } catch (IOException ignored) {
        }
    }
}