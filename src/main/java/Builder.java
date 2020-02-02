import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * A project builder that can clone git repositories, build maven projects and run units tests
 */
public class Builder {
    private static final String tokenFile = ".token";//file path to git access token
    private final int id;
    private LogToString lts;

    /**
     * Initializes Builder with a unique id for creating work directory paths
     *
     * @param id A unique int
     */
    public Builder(int id) {
        this.id = id;
        this.lts = new LogToString();
    }

    /**
     * Reads git access token from file
     *
     * @return The git access token
     */
    private String readToken() throws IOException {
        File file = new File(tokenFile);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String token = reader.readLine();
        reader.close();
        return token;
    }

    /**
     * Runs a shell script responsible for cloning a git repository, building a maven project and running unit tests
     *
     * @param cloneURL   A git repository HTTPS URL
     * @param repoName   The name of the git repository
     * @param branchName The branch of the git repository to checkout
     */
    private int cloneBuildTest(String cloneURL, String repoName, String branchName) throws IOException {
        return runCommandLine("sh cloneBuildTest.sh " + id + " " + cloneURL + " " + repoName + " " + branchName);
    }

    /**
     * Parses the JSON payload, builds and tests the given git repository, logs the results
     * (including the exit status in the last line of log) and then cleans up
     *
     * @param jsonPayload A GitHub webhook JSON String
     */
    public void build(String jsonPayload) {
        // create JsonParser and History object
        JSONParser parser = new JSONParser(jsonPayload);
        History history = new History();

        try {
            // get clone url and insert access token + "@" after https://;
            StringBuilder gitCloneURLSB = new StringBuilder(parser.getCloneURL()).insert(8, readToken() + "@");
            String gitCloneURL = gitCloneURLSB.toString();
            // get repoName, get Branch name, get HEAD commit hash
            String repoName = parser.getRepoName();
            String branchName = parser.getBranchName();
            String headCommitHash = parser.getHeadCommitHash();

            // run clone, build and test script, given clone url, reponame, branch name
            int cloneBuildTestExitValue = cloneBuildTest(gitCloneURL, repoName, branchName);

            //add exit status line to the end of the log history before saving
            lts.processLine("\n", 0);
            lts.processLine("Exit status " + cloneBuildTestExitValue, 0);

            // save log history using History object
            history.save(lts.toString(), headCommitHash);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                cleanup();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Runs a shell script responsible for removing the temporary working directory
     */
    private void cleanup() throws IOException {
        runCommandLine("sh cleanup.sh " + id);
    }

    /**
     * Runs a command line shell script
     *
     * @param line A shell script
     */
    private int runCommandLine(String line) throws IOException {
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(new PumpStreamHandler(lts)); // redirect stdout and stderr to string handler
        CommandLine cmdLine = CommandLine.parse(line);
        return executor.execute(cmdLine);
    }
}
