import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LogToString class.
 */
@DisplayName("Tests LogToString class")
class LogToStringTest {

    private LogToString lts;

    /**
     * Create a new LogToString object before each test case.
     */
    @BeforeEach
    void setUp(){
        lts = new LogToString();
    }

    /**
     * Tests that processLine adds new lines every time it is called.
     * Also, tests that logLevel does not affect the result of the log.
     */
    @Test
    @DisplayName("Test processLine adds new line")
    void testProcessLineAddNewLine() {
        lts.processLine("line 1", 0);
        assertEquals("line 1\n", lts.toString());
        lts.processLine("line 2", 1);
        assertEquals("line 1\nline 2\n", lts.toString());
    }

    /**
     * Tests the toString returns a string of the log correctly.
     */
    @Test
    @DisplayName("Test toString return correct string")
    void testToString() {
        lts.processLine("line 1", 0);
        assertEquals("line 1\n", lts.toString());
        lts.processLine("line 2", 1);
        assertEquals("line 1\nline 2\n", lts.toString());
        lts.clear();
        assertEquals("", lts.toString());
        lts.processLine("line 3", 1);
        assertEquals("line 3\n", lts.toString());
    }

    /**
     * Tests that clear method clears the log correctly.
     */
    @Test
    @DisplayName("Test clear clears the log")
    void testClear() {
        lts.processLine("line 1", 0);
        assertEquals("line 1\n", lts.toString());
        lts.clear();
        assertEquals("", lts.toString());
    }
}