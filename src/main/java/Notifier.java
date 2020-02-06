import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class Notifier {
    /**
     * Sends a HTTP POST to the given URL with the given JSON data, authenticated with token
     *
     * @param url        A URL to which the POST is sent
     * @param jsonString JSON data in String format
     * @param token      Personal access token
     */
    private void post(URL url, String jsonString, String token) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "token " + token);
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        connection.getInputStream();
    }

    /**
     * Sends a HTTP POST to GitHub's API to update the status for a given commit
     *
     * @param requestJson The webhook JSON data which triggered this status update
     * @param state       The state of the status, either "error", "failure", "pending" or "success"
     * @param description A short description of the status
     * @param context     A string label to differentiate this status from the status of other systems
     * @param targetURL   The target URL to associate with this status, null if none
     */
    public void postStatus(String requestJson, String state, String description, String context, String targetURL) {
        JSONParser jsonParser = new JSONParser(requestJson);
        try {
            URL url = new URL("https://api.github.com/repos/" + jsonParser.getOwnerName() + "/" + jsonParser.getRepoName() + "/statuses/" + jsonParser.getHeadCommitHash());
            String postJson = "{\n" +
                    "  \"state\": \"" + state + "\",\n" +
                    (targetURL != null ? "  \"target_url\": \"" + targetURL + "\",\n" : "") +// optionally include targetURL
                    "  \"description\": \"" + description + "\",\n" +
                    "  \"context\": \"" + context + "\"\n" +
                    "}";
            post(url, postJson, Utility.readToken());
            System.out.println("Sent status update \"" + state + "\" to: " + url);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
