import org.junit.jupiter.api.Test;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    /**
     * testListHistory is a JUnit test of the method listHistory in the history class. The function is creating
     * three files in a test_folder, if listHistory returns the name of those files with timestamps in ascending orders the
     * test case is considered successful. All other outputs are considered as a fail.
     * <p>
     * Input: create files test_build_history/test 20T03T20T30 test_build_history/test 20T01T20T30 test_build_history/test 20T03T19T30
     * Expected output: test 20T01T20T30 test 20T03T19T30 test 20T03T20T30
     */
    @Test
    public void testListHistory() {
        History history = new History(true);

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
        }
        try {
            String correct = "test 20T01T20T30\ntest 20T03T19T30\ntest 20T03T20T30\n";
            assertTrue(history.listHistory().compareTo(correct) == 0);
        } catch (Exception e) {
            fail();
            e.printStackTrace();
        } finally {
            file.delete();
            file1.delete();
            file2.delete();
        }

    }

    /**
     * testSearchForCommitID is a JUnit test of the method searchForCommitID in the history class. The function is creating
     * three files in a test_folder, the test then uses the function to search for a file that exists and expect the method
     * to return it. The second test is to search for a file that does not exists and expect the program to return false.
     * <p>
     * Input: create files test_build_history/test1 20T03T20T30 test_build_history/test2 20T01T20T30 test_build_history/test3 20T03T19T30
     * Search: test1
     * Expected output: File with the name test1 20T03T20T30
     * Search: test2
     * Expected output: null
     */
    @Test
    public void testSearchForCommitID() {
        History history = new History(true);

        File file = new File("test_build_history/test1 20T03T20T30");
        File file1 = new File("test_build_history/test2 20T01T20T30");
        File file2 = new File("test_build_history/test3 20T03T19T30");
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
        }
        try {
            File result = history.searchForCommitID("test1");
            assertNotNull(result);
            assertTrue(result.getName().compareTo(file.getName()) == 0);
            result = history.searchForCommitID("test4");
            assertNull(result);
        } catch (Exception e) {
            fail();
            e.printStackTrace();
        } finally {
            file.delete();
            file1.delete();
            file2.delete();
        }
    }

}