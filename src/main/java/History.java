import java.io.File;

public class History {

    /**
     * Initialize history and create the directory build_history if it does not exist
     */
    public History(){
        
    }
    /**
     * The method save, saves the information in the parameter, provided from a process.
     * The function assigns a timestamp to the information and saves it as a file with the name
     * commitID + " " + TIMESTAMP in the map build_history
     * @param buildResult contains the result from the build
     * @param commitID unique ID for the commit/process/push should be searchable
     */
    public void save(String buildResult, String commitID){

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
}
