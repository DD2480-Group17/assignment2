import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        File dir = new File("test_build_history");
        File[] files = dir.listFiles();
        if (files != null) {
            assertEquals(0, files.length);
        }
    }
}