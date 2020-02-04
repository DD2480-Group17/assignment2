import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.*;
import org.eclipse.jgit.api.Git;
import java.time.LocalDateTime;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONException;

import java.io.*;
import java.util.Collections;

/**
 * A project builder that can clone git repositories, build maven projects and run units tests
 */
public class Builder {
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
     * Parses the jsonPayload, clones the git repository, switches branch, runs mvn install,
     * saves build output to file with the format HeadCommitHash + YEARTMONTHTDAYTHOURTMINUTESTSECONDTMILLISECOND and cleans up
     *
     * @param jsonPayload A Git webhook payload
     */
    public void build(String jsonPayload) {
        //parse JSON
        JSONParser parser = new JSONParser(jsonPayload);
        String commitHash, branchName, cloneURL, repoName;
        try {
            commitHash = parser.getHeadCommitHash();
            branchName = parser.getBranchName();
            cloneURL = parser.getCloneURL();
            repoName = parser.getRepoName();
        } catch (JSONException e) {
            System.err.println(e.getMessage());
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        String time = now.toString();
        String timestamp = time.replace("-", "T");
        timestamp = timestamp.replace(".", "T");
        timestamp = timestamp.replace(":", "T");

        String repoDir = "work/temp" + id + "/" + repoName;

        // clone repo
        try {
            cloneRepo(cloneURL, branchName, repoDir);
        } catch (GitAPIException e) {
            System.err.println(e.getMessage());
        }

        // maven install
        try {
            String outputFilePath = "build_history/" + parser.getHeadCommitHash() + "_" + timestamp;
            mavenInstall(repoDir, outputFilePath);
        } catch (MavenInvocationException | FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        // cleanup
        try {
            FileUtils.deleteDirectory(new File("work/temp" + id));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Runs mvn install in the specified directory and outputs build log to file
     *
     * @param repoDir        A directory with maven project in it, including a pom.xml file
     * @param outputFilePath The file path to write output to
     */
    private void mavenInstall(String repoDir, String outputFilePath) throws MavenInvocationException, FileNotFoundException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFileName(repoDir + "/pom.xml");
        request.setGoals(Collections.singletonList("install"));

        Invoker invoker = new DefaultInvoker();
        PrintStreamHandler handler = new PrintStreamHandler(new PrintStream(outputFilePath), true);
        invoker.setOutputHandler(handler);
        invoker.execute(request);
    }

    /**
     * Clones a Git repository and switches branch
     *
     * @param repoURI A Git repository URI
     * @param branchName A branch name
     * @param repoDir A directory in which to clone the repo
     */
    private void cloneRepo(String repoURI, String branchName, String repoDir) throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI(repoURI)
                .setDirectory(new File(repoDir))
                .setBranch(branchName)
                .call();
    }
}

