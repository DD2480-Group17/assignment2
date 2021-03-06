import java.io.File;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class History {

    private String directoryPath = "";

    private final String testPath = "test_build_history";
    private final String usedPath = "build_history";

    /**
     * Initialize history and create the directory build_history if it does not exist
     */
    public History() {
        this(false);
    }

    /**
     * Initialize history with a path to test folder. This mode is used if the class should be tested as a JUnit test.
     *
     * @param isThisATestCase if isThisATestCase it true the class will redirect all function to a test folder. This
     *                        is done to prevent damege on data during tests. If isThisATestCase is false, the class
     *                        will direct all function to the original folder.
     */
    public History(boolean isThisATestCase) {
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
     * The method execute searchForCommitID(). If the file exists, the method returns the content of the commit.
     * Else it returns null.
     *
     * @param commitID
     * @return content which is the content of the commitID
     */
    public String load(String commitID) throws IOException {
        File fileObj = searchForCommitID(commitID);
        String filename = "";

        if (fileObj != null) {
            filename = fileObj.toString();
        } else {
            return null;
        }

        BufferedReader file = new BufferedReader(new FileReader(filename));
        String contents = "";
        String line = "";

        while ((line = file.readLine()) != null)
            contents += line + "\\n";

        file.close();
        return contents;
    }

    /**
     * The function listHistory returns all the commitID in the directory build_history in execution order, with the latest
     * execution first.
     *
     * @return returns the history as a string, null if it does not exist, the format of the string will be:
     * commitID TIMESTAMP
     * commitID2 TIMESTAMP
     * Where TIMESTAMP has the format year month day hour minute second millisecond, all the entries are separated by T
     * not blank-space.
     */
    public String listHistory() throws Exception {
        File dir = new File(directoryPath);
        if (!dir.exists())
            return "";

        File[] files = dir.listFiles();
        if (files == null)
            throw new FileNotFoundException("Error in reading files");

        ArrayList<CommitIDAndTimeStampHolder> holders = new ArrayList<CommitIDAndTimeStampHolder>();

        for (File file : files) {
            String[] info = file.getName().split("_");
            if (info.length != 2) {
                System.err.println("File with wrong format: "+file.getName());
                continue;
            }
            if (file.getName().charAt(0) == '.'){
                System.err.println("Hidden caches files: " + file.getName());
                continue;
            }
            CommitIDAndTimeStampHolder commitIDAndTimeStampHolder = new CommitIDAndTimeStampHolder(info[0], info[1]);
            holders.add(commitIDAndTimeStampHolder);
        }
        Collections.sort(holders, commitIDAndTimeStampHolderComparator);

        StringBuilder stringBuilder = new StringBuilder();
        for (CommitIDAndTimeStampHolder holder : holders) {
            stringBuilder.append(holder.getCommitID() + "_" + holder.getTimeStamp());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * The method checks if it exists a file with the name commitID. If it exists the function returns the file,
     * otherwise the method returns null.
     *
     * @param commitID
     * @return the file with correct ID or null if it does not exist
     */
    public File searchForCommitID(String commitID) {
        File dir = new File(directoryPath);
        if (!dir.exists())
            return null;

        File[] files = dir.listFiles();
        if (files == null)
            return null;

        for (File file : files) {
            String info = file.getName();;
            if (info.compareTo(commitID) == 0)
                return file;

        }

        return null;
    }

    /**
     * commitIDAndTimeStampHolderComparator is a comparator that is used to sort CommitIDAndTimeStampHolder in descending order
     */
    private Comparator<CommitIDAndTimeStampHolder> commitIDAndTimeStampHolderComparator = new Comparator<CommitIDAndTimeStampHolder>() {
        @Override
        public int compare(CommitIDAndTimeStampHolder o1, CommitIDAndTimeStampHolder o2) {
            String[] time1 = o1.timeStamp.split("T");
            String[] time2 = o2.timeStamp.split("T");
            for (int i = 0; i < time1.length; i++) {
                if (Integer.parseInt(time1[i]) < Integer.parseInt(time2[i])) {
                    return 1;
                } else if (Integer.parseInt(time1[i]) > Integer.parseInt(time2[i])) {
                    return -1;
                }
            }
            return 0;
        }
    };

    /**
     * CommitIDAndTimeStampHolder is a holder class that contain a commitID and timestamp
     */
    private class CommitIDAndTimeStampHolder {
        private String commitID;
        private String timeStamp;

        public CommitIDAndTimeStampHolder(String commitID, String timeStamp) {
            this.commitID = commitID;
            this.timeStamp = timeStamp;
        }

        public String getCommitID() {
            return commitID;
        }

        public String getTimeStamp() {
            return timeStamp;
        }
    }
}
