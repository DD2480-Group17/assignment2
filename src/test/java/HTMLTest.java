import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HTMLTest {

    /**
     * Test that the readFile() method reads and returns the correct content of a file.
     * Input: the filename/path to the file "test_build_history/test.txt"
     * Expected value: "test\n"
     */
    @Test
    void testReadFile() {
        Utility.clearDirectory("test_build_history");
        File file = new File("test_build_history/test.txt");
        String filename = file.toString();
        HTML html = new HTML(true);

        try {

            FileWriter writer = new FileWriter(file);
            writer.write("test");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        try {
            String correct = "test\n";
            assertEquals(correct, html.readFile(filename));

        } catch (Exception e) {

            e.printStackTrace();
            fail();

        } finally {
            file.delete();
        }
    }

    /**
     * Test that the saveAllCommmitInfo() saves the commitID, timestamp and content of each commit.
     * Expected value: the content and the timestamp is correct for the commitID test1_20T03T20T30
     */
    @Test
    void testSaveAllCommitInfo() {
        Utility.clearDirectory("test_build_history");
        HTML html = new HTML(true);
        File file = new File("test_build_history/test1_20T03T20T30");
        File file1 = new File("test_build_history/test2_20T03T19T30");
        try {

            FileWriter writer = new FileWriter(file);
            writer.write("[INFO]  T E S T S\n" +
                    "[INFO] -------------------------------------------------------\n" +
                    "[INFO] Running UtilityTest\n" +
                    "[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.007 s - in UtilityTest\n" +
                    "[INFO] Running JSONParserTest\n" +
                    "[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s - in JSONParserTest\n" +
                    "[INFO] Running LogToStringTest\n" +
                    "[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s - in LogToStringTest\n" +
                    "[INFO] Running HistoryTest\n" +
                    "[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s - in HistoryTest\n" +
                    "[INFO] \n" +
                    "[INFO] Results:\n" +
                    "[INFO] \n" +
                    "[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0");
            writer.close();

            writer = new FileWriter(file1);
            writer.write("[INFO]  T E S T S\n" +
                    "[INFO] -------------------------------------------------------\n" +
                    "[INFO] Running UtilityTest\n" +
                    "[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.007 s - in UtilityTest\n" +
                    "[INFO] Running JSONParserTest\n" +
                    "[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s - in JSONParserTest\n" +
                    "[INFO] Running LogToStringTest\n" +
                    "[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s - in LogToStringTest\n" +
                    "[INFO] Running HistoryTest\n" +
                    "[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s - in HistoryTest\n" +
                    "[INFO] \n" +
                    "[INFO] Results:\n" +
                    "[INFO] \n" +
                    "[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0");
            writer.close();


        } catch (IOException e) {

            e.printStackTrace();
            fail();
        }

        html.saveAllCommitInfo("test1_20T03T20T30\ntest2_20T03T19T30\n");
        String[] info = html.commitHashMap.get("test1_20T03T20T30");
        assertEquals(" UtilityTest\\n Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.007 s - in UtilityTest\\n " +
                "Running JSONParserTest\\n Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.003 s - in JSONParserTest\\n " +
                "Running LogToStringTest\\n Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s - in LogToStringTest\\n " +
                "Running HistoryTest\\n Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s - in HistoryTest\\n \\n ", info[1]);
        assertEquals("20T03T20T30", info[0]);
        Utility.clearDirectory("test_build_history");
        html.commitHashMap.clear();
    }

    /**
     * Test that the removeContent() removes unnecessary information from the build.
     * Expected value: UtilityTest  Tests run: 1 ...... - in HistoryTest
     */
    @Test
    void testRemoveContent() {
        HTML html1 = new HTML(true);
        Utility.clearDirectory("test_build_history");
        String commitContent = "[INFO]" +
                "[INFO] ------------------------------------------------------- " +
                "[INFO]  T E S T S " +
                "[INFO] ------------------------------------------------------- " +
                "[INFO] Running UtilityTest " +
                "[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s - in UtilityTest " +
                "[INFO] Running JSONParserTest " +
                "[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.002 s - in JSONParserTest " +
                "[INFO] Running HTMLTest[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.002 s - in HTMLTest " +
                "[INFO] Running LogToStringTest " +
                "[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s - in LogToStringTest " +
                "[INFO] Running HistoryTest " +
                "[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s - in HistoryTest " +
                "[INFO] " +
                "[INFO] Results: " +
                "[INFO] " +
                "[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0 " +
                "[INFO] " +
                "[INFO] " +
                "[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ assignment2 --- " +
                "[INFO] Building jar: /Users/johannaiivanainen/Documents/KTH/DD2480/assignment2/target/assignment2-1.0-SNAPSHOT.jar " +
                "[INFO] " +
                "[INFO] --- maven-install-plugin:2.4:install (default-install) @ assignment2 --- " +
                "[INFO] Installing /Users/johannaiivanainen/Documents/KTH/DD2480/assignment2/target/assignment2-1.0-SNAPSHOT.jar to /Users/johannaiivanainen/.m2/repository/group17/assignment2/1.0-SNAPSHOT/assignment2-1.0-SNAPSHOT.jar " +
                "[INFO] Installing /Users/johannaiivanainen/Documents/KTH/DD2480/assignment2/pom.xml to /Users/johannaiivanainen/.m2/repository/group17/assignment2/1.0-SNAPSHOT/assignment2-1.0-SNAPSHOT.pom " +
                "[INFO] ------------------------------------------------------------------------ " +
                "[INFO] BUILD SUCCESS " +
                "[INFO] ------------------------------------------------------------------------ " +
                "[INFO] Total time:  10.174 s " +
                "[INFO] Finished at: 2020-02-05T09:51:47+01:00 " +
                "[INFO] ------------------------------------------------------------------------";

        assertEquals(" UtilityTest  Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s - in UtilityTest  " +
                "Running JSONParserTest  Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.002 s - in JSONParserTest  " +
                "Running HTMLTest Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.002 s - in HTMLTest  " +
                "Running LogToStringTest  Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.004 s - in LogToStringTest  " +
                "Running HistoryTest  Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s - in HistoryTest   ", html1.removeContent(commitContent));
    }
}