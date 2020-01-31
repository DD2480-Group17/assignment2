import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JSON Parser Test")
class JSONParserTest {
    final String correctJsonString = "{\n" +
            "  \"ref\": \"refs/heads/testBranchName\",\n" +
            "  \"repository\": {\n" +
            "    \"name\": \"testRepoName\",\n" +
            "    \"clone_url\": \"https://github.com/Test/testRepoName.git\",\n" +
            "  }\n" +
            "}";
    final String missingBracketJsonString = "\n" +
            "  \"ref\": \"refs/heads/testBranchName\",\n" +
            "  \"repository\": {\n" +
            "    \"name\": \"testRepoName\",\n" +
            "    \"clone_url\": \"https://github.com/Test/testRepoName.git\",\n" +
            "  }\n" +
            "}";
    final JSONParser correctJsonParser = new JSONParser(correctJsonString);
    final JSONParser missingBracketJsonParser = new JSONParser(missingBracketJsonString);

    @Test
    @DisplayName("Test getCloneURL correct")
    void getCloneURL() {
        assertEquals(correctJsonParser.getCloneURL(), "https://github.com/Test/testRepoName.git");
    }

    @Test()
    @DisplayName("Test getCloneURL exception")
    void getCloneURLException() {
        assertThrows(JSONException.class, missingBracketJsonParser::getCloneURL);
    }

    @Test
    @DisplayName("Test getBranchName correct")
    void getBranchName() {
        assertEquals(correctJsonParser.getBranchName(), "testBranchName");
    }

    @Test()
    @DisplayName("Test getBranchName exception")
    void getBranchNameException() {
        assertThrows(JSONException.class, missingBracketJsonParser::getBranchName);
    }

    @Test
    @DisplayName("Test getRepoName correct")
    void getRepoName() {
        assertEquals(correctJsonParser.getRepoName(), "testRepoName");
    }

    @Test()
    @DisplayName("Test getRepoName exception")
    void getRepoNameException() {
        assertThrows(JSONException.class, missingBracketJsonParser::getRepoName);
    }
}