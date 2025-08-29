package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FormatParameterTest {

    @Test
    void testFormatNull() {
        assertEquals("null", FormatParameter.formatParameter(null));
    }

    @Test
    void testFormatString() {
        assertEquals("\"Hello, World!\"", FormatParameter.formatParameter("Hello, World!"));
    }

    @Test
    void testFormatStringWithSpecialCharacters() {
        assertEquals("\"Line1\\nLine2\\tTabbed\"", FormatParameter.formatParameter("Line1\nLine2\tTabbed"));
        assertEquals("\"Quote: \\\"\"", FormatParameter.formatParameter("Quote: \""));
        assertEquals("\"Backslash: \\\\\"", FormatParameter.formatParameter("Backslash: \\"));
    }

    @Test
    void testFormatCharacter() {
        assertEquals("'A'", FormatParameter.formatParameter('A'));
        assertEquals("'\\n'", FormatParameter.formatParameter('\n'));
        assertEquals("'\\t'", FormatParameter.formatParameter('\t'));
        assertEquals("'\\''", FormatParameter.formatParameter('\''));
    }

    @Test
    void testFormatBoolean() {
        assertEquals("true", FormatParameter.formatParameter(true));
        assertEquals("false", FormatParameter.formatParameter(false));
    }

    @Test
    void testFormatFloat() {
        assertEquals("3.14f", FormatParameter.formatParameter(3.14f));
        assertEquals("-1.0f", FormatParameter.formatParameter(-1.0f));
    }

    @Test
    void testFormatLong() {
        assertEquals("123456789L", FormatParameter.formatParameter(123456789L));
        assertEquals("-987654321L", FormatParameter.formatParameter(-987654321L));
    }

    @Test
    void testFormatDouble() {
        assertEquals("3.14", FormatParameter.formatParameter(3.14));
        assertEquals("2.0", FormatParameter.formatParameter(2.0));
        assertEquals("-1500.0", FormatParameter.formatParameter(-1.5E3));
    }

    @Test
    void testFormatInteger() {
        assertEquals("42", FormatParameter.formatParameter(42));
        assertEquals("-7", FormatParameter.formatParameter(-7));
    }

    @Test
    void testFormatUnexpectedType() {
        assertEquals("null", FormatParameter.formatParameter(new Object()));
    }

    @Test
    void testEscapeString() {
        assertEquals("\"Line1\\nLine2\"", FormatParameter.formatParameter("Line1\nLine2"));
        assertEquals("\"Tab\\tCharacter\"", FormatParameter.formatParameter("Tab\tCharacter"));
        assertEquals("\"Quote: \\\"Hello\\\"\"", FormatParameter.formatParameter("Quote: \"Hello\""));
    }

    @Test
    void testEscapeChar() {
        assertEquals("'\\n'", FormatParameter.formatParameter('\n'));
        assertEquals("'\\t'", FormatParameter.formatParameter('\t'));
        assertEquals("'\\''", FormatParameter.formatParameter('\''));
        assertEquals("'\\\\'", FormatParameter.formatParameter('\\'));
        assertEquals("'A'", FormatParameter.formatParameter('A'));
    }
}
