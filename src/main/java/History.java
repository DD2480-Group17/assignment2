import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class History {

    private String directoryPath = "";

    private final String testPath = "test_build_history";
    private final String usedPath = "build_history";

    /**
     * Initialize history and create the directory build_history if it does not exist
     */
    public History() {
        directoryPath = usedPath;
        File file = new File(directoryPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

        /**
         * Initialize history with a path to test folder. This mode is used if the class should be tested as a JUnit test.
         *
         * @param isThisATestCase, if isThisATestCase it true the class will redirect all function to a test folder. This
         *                         is done to prevent damege on data during tests. If isThisATestCase is false, the class
         *                         will direct all function to the original folder.
         */
     public History(boolean isThisATestCase){
            if (isThisATestCase)
                directoryPath = testPath;
            else
                directoryPath = usedPath;
            File file = new File(directoryPath);
            if (!file.exists()) {
                file.mkdir();
            }
        }

    /**
     * The method save(), saves the information provided from a process into a new file.
     * The method assigns a timestamp and saves it as a file with the name
     * commitID + " " + TIMESTAMP in the map build_history
     * @param buildResult, contains the result from the build
     * @param commitID, unique ID for the process should be searchable
     */
    public void save(String buildResult, String commitID) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String time = now.toString();
        String timestamp = time.replace("-", "T");
        timestamp = timestamp.replace(".", "T");
        timestamp = timestamp.replace(":", "T");

        String filename = directoryPath + "/" + commitID + " " + timestamp + ".txt";

        FileWriter writer = new FileWriter(filename);
        writer.write(buildResult);
        writer.close();
    }

    /**
     * The method execute searchForCommitID(). If the file exists, the method returns the content of the commit.
     * Else it returns null.
     * @param commitID
     * @return content, which is the content of the commitID
     */
    public String load(String commitID){
        // if searchForCommits() returns null , return null
        return null;
    }

    /**
     * The function listHistory returns all the commitID in the directory build_history in execution order
     * @return, returns the history as a string, null if it does not exist
     */
    public String listHistory(){
        return null;
    }

    /**
     * The function checks if the file containing the name commiteID exists and return the file, otherwise null
     * @param commitID
     * @return the file with correct ID or null if it does not exist
     */
    private File searchForCommitID(String commitID){
        return null;
    }

    public static void main(String[] args) throws Exception{
        History history = new History(true);
        history.save("this is a test", "testCommit");
    }
}
