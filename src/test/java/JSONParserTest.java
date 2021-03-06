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
            "    \"owner\": {\n" +
            "      \"name\": \"testOwnerName\",\n" +
            "    },\n" +
            "  },\n" +
            "  \"head_commit\": {\n" +
            "       \"id\": \"abc123\" \n" +
            "  }\n" +
            "}";
    // missingBracketJsonString has a curly bracket missing at the beginning
    final String missingBracketJsonString = "\n" +
            "  \"ref\": \"refs/heads/testBranchName\",\n" +
            "  \"repository\": {\n" +
            "    \"name\": \"testRepoName\",\n" +
            "    \"clone_url\": \"https://github.com/Test/testRepoName.git\",\n" +
            "    \"owner\": {\n" +
            "      \"name\": \"testOwnerName\",\n" +
            "    },\n" +
            "  },\n" +
            "  \"head_commit\": {\n" +
            "       \"id\": \"abc123\" \n" +
            "  }\n" +
            "}";

    final JSONParser correctJsonParser = new JSONParser(correctJsonString);
    final JSONParser missingBracketJsonParser = new JSONParser(missingBracketJsonString);

    @Test
    @DisplayName("Test getCloneURL correct")
    void getCloneURL() {
        assertEquals("https://github.com/Test/testRepoName.git", correctJsonParser.getCloneURL());
    }

    @Test()
    @DisplayName("Test getCloneURL exception")
    void getCloneURLException() {
        assertThrows(JSONException.class, missingBracketJsonParser::getCloneURL);
    }

    @Test
    @DisplayName("Test getBranchName correct")
    void getBranchName() {
        assertEquals( "testBranchName", correctJsonParser.getBranchName());
    }

    @Test()
    @DisplayName("Test getBranchName exception")
    void getBranchNameException() {
        assertThrows(JSONException.class, missingBracketJsonParser::getBranchName);
    }

    @Test
    @DisplayName("Test getRepoName correct")
    void getRepoName() {
        assertEquals("testRepoName", correctJsonParser.getRepoName());
    }

    @Test()
    @DisplayName("Test getRepoName exception")
    void getRepoNameException() {
        assertThrows(JSONException.class, missingBracketJsonParser::getRepoName);
    }

    /**
     * Tests that getHeadCommitHash returns the correct HEAD commit Hash.
     */
    @Test
    @DisplayName("Test getHeadCommitHash correct")
    void getHeadCommitHash() {
        assertEquals("abc123", correctJsonParser.getHeadCommitHash());
    }

    /**
     * Tests that getHeadCommitHash returns exception if the json object is not correct.
     */
    @Test()
    @DisplayName("Test getHeadCommitHash exception")
    void getGetHeadCommitHashException() {
        assertThrows(JSONException.class, missingBracketJsonParser::getHeadCommitHash);
    }

    @Test
    @DisplayName("Test getOwnerName correct")
    void getOwnerName() {
        assertEquals("testOwnerName", correctJsonParser.getOwnerName());
    }

    @Test()
    @DisplayName("Test getOwnerName exception")
    void getOwnerNameException() {
        assertThrows(JSONException.class, missingBracketJsonParser::getOwnerName);
    }
}