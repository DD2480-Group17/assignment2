import org.json.JSONObject;

/**
 * A JSON parser which can retrieve specific values from a GitHub webhook
 */
public class JSONParser {
    private String jsonString;

    /**
     * Initializes a new JSONParser
     *
     * @param jsonString A JSON String
     */
    public JSONParser(String jsonString) {
        this.jsonString = jsonString;
    }

    /**
     * Retrieves the HTTPS clone URL from the JSON String
     *
     * @return The value under "repository": and then "clone_url": from the JSON String
     */
    public String getCloneURL() {
        String name = getValue(new String[]{"repository", "clone_url"});
        return removeSurroundingQuotes(name);
    }

    /**
     * Retrieves the branch name from the JSON String
     *
     * @return Part of the value under "ref": from the JSON String
     */
    public String getBranchName() {
        String name = getValue("ref");
        name = removeSurroundingQuotes(name);
        return name.substring(11);// remove the first part: "refs/heads/"
    }

    /**
     * Retrieves the repository name from the JSON String
     *
     * @return The value under "repository": and then "name": from the JSON String
     */
    public String getRepoName() {
        String name = getValue(new String[]{"repository", "name"});
        return removeSurroundingQuotes(name);
    }

    /**
     * Retrieves the value under all the keys in respective order from the JSON String
     *
     * @param keys The keys to the retrieved values in use order
     * @return The value under all the keys from the JSON String
     */
    private String getValue(String[] keys) {
        String temp = jsonString;
        for (String key : keys) {
            JSONObject object = new JSONObject(temp);
            temp = JSONObject.valueToString(object.get(key));
        }
        return temp;
    }

    /**
     * Retrieves the value under the key from the JSON String
     *
     * @param key The key to the retrieved value
     * @return The value under the key from the JSON String
     */
    private String getValue(String key) {
        return getValue(new String[]{key});
    }

    /**
     * Removes the first and last char from a String
     *
     * @param string A String
     * @return The String without the first and last char
     */
    private String removeSurroundingQuotes(String string) {
        return string.substring(1, string.length() - 1);
    }

    /**
     * Gets HEAD commit hash value.
     *
     * @return HEAD commit hash value.
     */
    public String getHeadCommitHash() {
        String commit_id = getValue(new String[]{"head_commit", "id"});
        commit_id = removeSurroundingQuotes(commit_id);
        return commit_id;
    }

    /**
     * Retrieves owner name from the JSON String
     *
     * @return The value under "owner" and then "name" from the JSON String
     */
    public String getOwnerName() {
        String res = getValue(new String[]{"repository", "owner", "name"});
        return removeSurroundingQuotes(res);
    }
}
