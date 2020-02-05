import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class HTML {
    public HashMap<String, String[]> commitHashMap = new HashMap<>();
    public String[] commitList;
    public History history;

    /**
     *
     * @param isThisATestCase if isThisATestCase is false the class will create a "real" History object and get all commit
     *                        history as a string called commitHistory from the history class and run saveAllCommitInfo() which
     *                        saves the commit history in the hash map commitHashMap.
     */
    public HTML(boolean isThisATestCase) {

        if (isThisATestCase) {
            commitHashMap.clear();
            history = new History(true);
        }
        else {
            String commitHistory = "";
            history = new History(false);
            try {
                commitHistory = this.getAllHistory();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.saveAllCommitInfo(commitHistory);
        }
    }

    /**
     * Collects all commitID by using method listHistory() from History class
     *
     * @return history which contains all commitID in History class
     */
    public String getAllHistory() throws Exception {
        String historyList = "";
        try {
            historyList = history.listHistory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    /**
     * Reads and returns the content of a file
     * @param filename or a name of a path
     * @return content which is the content of the file
     * @throws IOException
     */
    public String readFile(String filename) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(filename));
        String content = "";
        String line = "";
        while ((line = file.readLine()) != null)
            content += line + "\n";

        return content;
    }

    /**
     * Split the historyContent and add the commitID, timestamp and commitContent into a hashmap.
     * @param historyContent
     */
    public void saveAllCommitInfo(String historyContent) {
        commitList = historyContent.split("\n");
        String commitContent = "";

        for (int i = 0; i < commitList.length; i++) {
            String commit = commitList[i];
            String[] commitInfo = commit.split("_");
            String commitID = commitInfo[0];
            String timestamp = commitInfo[1];

            try {
                commitContent = history.load(commitID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] info = new String[2];
            info[0] = timestamp;
            commitContent = removeContent(commitContent);
            info[1] = commitContent;
            commitHashMap.put(commitID, info);
        }
    }

    /**
     * Remove unecessary lines from the commitContent by matching a regex.
     * @param commitContent
     * @return
     */
    public String removeContent(String commitContent){
        String content;
        Pattern pattern = Pattern.compile("Running(.*)Results");
        Matcher matcher = pattern.matcher(commitContent);
        if (matcher.find()){
            content = matcher.group(1);
        }
        else{
            content = "";
        }
        content = content.replace("[INFO]", "");
        return content;
    }

    /**
     * Parse the data in the hashmap into HTML format and returns the new htmlContent
     * @return htmlContent which is the data
     */
    public String parseHTMLContent() {
        String htmlContent = "";
        for(int i = 0; i<commitHashMap.size();i++) {

            String commit = commitList[i];
            String[] commitInfo = commit.split("_");
            String commitID = commitInfo[0];
            commitInfo = commitHashMap.get(commitID); //Override

            String timestamp = commitInfo[0];
            String commitContent = commitInfo[1];
            commitContent = commitContent.replace("Running", "<br>");
            commitContent = commitContent.replace("\\n", "<br>");
            timestamp = timestamp.replace("T", " ");
            htmlContent += "<div id=" + commitID + "<p><b>Commit ID:</b> " + commitID + "<br> <b>Timestamp:</b> " + timestamp + "<br><br>" + commitContent + "</p><br>";
        }
        return htmlContent;
    }

    /**
     * Reads the templet of the HTML file and add the HTML format of the history of commits
     * @return htmlContent which is the
     */
    public String createHTMLPayload() {
        String htmlContent = "";
        try {
            htmlContent = readFile("src/main/HTML/history.html");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        String historyContent = parseHTMLContent();
        htmlContent = htmlContent.replace("$", historyContent);

        return htmlContent;
    }
}
