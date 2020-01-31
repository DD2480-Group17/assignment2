import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

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

    /**
     * Initializes Builder with a unique id for creating work directory paths
     *
     * @param id A unique int
     */
    public Builder(int id) {
        this.id = id;
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
    private void cloneBuildTest(String cloneURL, String repoName, String branchName) throws IOException {
        runCommandLine("sh cloneBuildTest.sh " + id + " " + cloneURL + " " + repoName + " " + branchName);
    }

    /**
     * Parses the JSON payload, builds and tests the given git repository and then cleans up
     *
     * @param jsonPayload A GitHub webhook JSON String
     */
    public void build(String jsonPayload) {
        JSONParser parser = new JSONParser(jsonPayload);
        StringBuilder gitCloneURL = new StringBuilder(parser.getCloneURL());
        try {
            gitCloneURL.insert(8, readToken() + "@");// insert access token + "@" after https://
            cloneBuildTest(gitCloneURL.toString(), parser.getRepoName(), parser.getBranchName());
            cleanup();
        } catch (IOException e) {
            try {
                System.err.println(e.getMessage());
                cleanup();
            } catch (IOException e1) {
                System.err.println(e1.getMessage());
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
    private void runCommandLine(String line) throws IOException {
        DefaultExecutor executor = new DefaultExecutor();
        CommandLine cmdLine = CommandLine.parse(line);
        executor.execute(cmdLine);
    }
}
