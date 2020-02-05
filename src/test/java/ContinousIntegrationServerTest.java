import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class ContinousIntegrationServerTest {

    private static Thread t;
    private static volatile boolean started;
    private static final String BUILD_SUCCESS = "BUILD SUCCESS";
    private static final String COMPILE_FAIL = "Compilation failure";
    private static final String TEST_FAIL = "test failures";

    @BeforeAll
    static void beforeAll() throws Exception {

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                // start server at http://localhost:8017/
                try {
                    started = true; // should be before main, not after.
                    ContinousIntegrationServer.main(null);
                } catch (Exception e) {
                    // if InterruptedException then do nothing.
                }
            }
        });

        started = false;
        t.start();
        Thread.currentThread().sleep(5000); // wait 10 seconds until the server starts.
        // Note: does not guarantee that main is done with starting server.
        // this is an ad-hoc solution.
        if (!started) fail("server not started");
        if ((new File("work/")).exists()) FileUtils.cleanDirectory(new File("work/"));
        if ((new File("build_history/")).exists()) FileUtils.cleanDirectory(new File("build_history/"));
        if (!(new File(".token")).exists()) (new File(".token")).createNewFile();
    }

    @AfterAll
    static void afterAll() throws IOException {
        FileUtils.cleanDirectory(new File("work/"));
        FileUtils.cleanDirectory(new File("build_history/"));
        t.interrupt(); // interrupt the thread of the server to make the server stop.
    }

    @BeforeEach
    void setUp() throws IOException {

    }

    /**
     * Clean up commit history after every test.
     * @throws IOException
     */
    @AfterEach
    void tearDown() throws IOException {
        FileUtils.cleanDirectory(new File("build_history/"));
    }

    /**
     * Tests that server clones and compiles https://github.com/georgewbar/test_fail_repo successfully, but
     * the repo fails the unit tests. The command used to compile and test is "mvn install".
     *
     * @throws IOException IO error
     */
    @Test
    void testCloneTestFail() throws IOException {
        assertServerGivesRightStatus("JSONPostTestFail.txt","6f4935b1a14de14860c32d22f9b4979fae4776c8", TEST_FAIL);
    }

    /**
     * Tests that server clones https://github.com/georgewbar/compile_fail_repo successfully, but fails to compile
     * due to syntax error in the project on the repo. The command used to compile is "mvn install".
     *
     * @throws IOException IO error
     */
    @Test
    void testCloneCompileFail() throws IOException {
        assertServerGivesRightStatus("JSONPostCompileFail.txt","1a93cb59b0492997e1830bd8dc8185ac78b0938b", COMPILE_FAIL);
    }

    /**
     * Tests that server clones, compiles and tests the https://github.com/georgewbar/compile_test_success_repo
     * repo by using the "mvn install" command successfully.
     *
     * @throws IOException IO error
     */
    @Test
    void testCloneCompileTestSuccess() throws IOException {
        assertServerGivesRightStatus("JSONPostCompileTestSuccess.txt","4ee10351f0ac904f92e580b5efa6134a272f4a61", BUILD_SUCCESS);
    }

    /**
     * Asserts that the server can, for a repo produced by making a post request that uses jsonFile, commitHashID,
     * and wantedString, give the right judgment wanted from: "BUILD SUCCESS", "Compilation failure", "test failures".
     *
     * @param jsonFile jsonFile for emulating github post request payload for webhooks.
     * @param commitHashID HEAD commit hash of the emulated webhook on push event.
     * @param judgement takes values "BUILD SUCCESS", "Compilation failure", "test failures".
     * @throws IOException IO error
     */
    private void assertServerGivesRightStatus(String jsonFile, String  commitHashID, String judgement) throws IOException {
        URL obj = new URL("http://localhost:8017/webhook/");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();

        String jsonString = FileUtils.readFileToString(new File("textfiles_for_testing/" + jsonFile), "US-ASCII");
        byte[] jsb = jsonString.getBytes("utf-8");
        os.write(jsb, 0, jsb.length);
        os.close();

        int responseCode = con.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) fail("Sending a post request failed.");

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append('\n');
            }
        }

        assertEquals("CI Job Done\n", response.toString());
        assertFileContains(commitHashID, judgement);
    }

    /**
     * Asserts that file with commitHashID filename part contains wantedString.
     *
     * @param commitHashID the wanted first part of the filename of files in build_history directory
     * @param wantedString the string that is wanted to be searched for in the file indicated by commitHashID filename.
     * @throws IOException IO error
     */
    private void assertFileContains(String commitHashID, String wantedString) throws IOException {
        File dir = new File("build_history");
        if (!dir.exists())  fail("build_history directory does not exist");

        File[] files = dir.listFiles();
        if (files == null) fail("error in reading files from build_history directory");

        String fileContent = null;
        for (File file : files) {
            String[] filename = file.getName().split("_");
            if (filename.length != 2) continue;

            if (filename[0].equals(commitHashID)) fileContent = FileUtils.readFileToString(file, (String) null);
        }

        assertTrue(fileContent.contains(wantedString));
    }

}