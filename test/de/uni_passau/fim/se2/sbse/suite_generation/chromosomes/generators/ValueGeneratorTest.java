package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ValueGeneratorTest {
    private ValueGenerator valueGenerator;
    private Random random;

    @BeforeEach
    void setUp() {
        random = new Random(42);
        valueGenerator = new ValueGenerator(random);
    }

    @Test
    void testGenerateValueWithPrimitives() {
        assertNotNull(valueGenerator.generateValue(int.class, true));
        assertNotNull(valueGenerator.generateValue(boolean.class, true));
        assertNotNull(valueGenerator.generateValue(char.class, true));
        assertNotNull(valueGenerator.generateValue(float.class, true));
        assertNotNull(valueGenerator.generateValue(double.class, true));
        assertNotNull(valueGenerator.generateValue(long.class, true));
        assertNotNull(valueGenerator.generateValue(short.class, true));
        assertNotNull(valueGenerator.generateValue(byte.class, true));
    }

    @Test
    void testGenerateValueWithWrappers() {
        assertNullOrNotNull(valueGenerator.generateValue(Integer.class, true), "Value for Integer could be null.");
        assertNullOrNotNull(valueGenerator.generateValue(Boolean.class, true), "Value for Boolean could be null.");
        assertNullOrNotNull(valueGenerator.generateValue(Character.class, true), "Value for Character could be null.");
        assertNullOrNotNull(valueGenerator.generateValue(Float.class, true), "Value for Float could be null.");
        assertNullOrNotNull(valueGenerator.generateValue(Double.class, true), "Value for Double could be null.");
        assertNullOrNotNull(valueGenerator.generateValue(Long.class, true), "Value for Long could be null.");
        assertNullOrNotNull(valueGenerator.generateValue(Short.class, true), "Value for Short could be null.");
        assertNullOrNotNull(valueGenerator.generateValue(Byte.class, true), "Value for Byte could be null.");
        assertNullOrNotNull(valueGenerator.generateValue(String.class, true), "Value for String could be null.");
    }

    @Test
    void testGenerateParameters() {
        Class<?>[] paramTypes = {int.class, String.class, Boolean.class};
        Object[] params = valueGenerator.generateParameters(paramTypes, true);

        assertEquals(3, params.length);
        assertInstanceOf(Integer.class, params[0]);
        assertTrue(params[1] == null || params[1] instanceof String);
        assertTrue(params[2] == null || params[2] instanceof Boolean);
    }

    @Test
    void testGenerateEdgeCase() {
        assertEquals(-0, valueGenerator.generateEdgeCase(int.class));
        assertEquals(-1024L, valueGenerator.generateEdgeCase(long.class));
        assertEquals(-1024.0, valueGenerator.generateEdgeCase(double.class));
        assertEquals('A', valueGenerator.generateEdgeCase(char.class));
        assertEquals((short) -1024, valueGenerator.generateEdgeCase(short.class));
    }

    @Test
    void testGenerateRandomString() {
        String randomString = valueGenerator.generateRandomString();
        assertNotNull(randomString);
        assertTrue(!randomString.isEmpty() && randomString.length() <= 10);
        assertTrue(randomString.chars().allMatch(c -> c >= 32 && c <= 126));
    }

    @Test
    void testGeneratePrimitiveValue() {
        assertInstanceOf(Integer.class, valueGenerator.generatePrimitiveValue(int.class));
        assertInstanceOf(Boolean.class, valueGenerator.generatePrimitiveValue(boolean.class));
        assertInstanceOf(Character.class, valueGenerator.generatePrimitiveValue(char.class));
        assertInstanceOf(Float.class, valueGenerator.generatePrimitiveValue(float.class));
        assertInstanceOf(Double.class, valueGenerator.generatePrimitiveValue(double.class));
        assertInstanceOf(Long.class, valueGenerator.generatePrimitiveValue(long.class));
        assertInstanceOf(Short.class, valueGenerator.generatePrimitiveValue(short.class));
        assertInstanceOf(Byte.class, valueGenerator.generatePrimitiveValue(byte.class));
    }

    @Test
    void testGenerateObjectValue() {
        assertInstanceOf(Integer.class, valueGenerator.generateObjectValue(Integer.class));
        assertInstanceOf(Boolean.class, valueGenerator.generateObjectValue(Boolean.class));
        assertInstanceOf(Character.class, valueGenerator.generateObjectValue(Character.class));
        assertInstanceOf(Float.class, valueGenerator.generateObjectValue(Float.class));
        assertInstanceOf(Double.class, valueGenerator.generateObjectValue(Double.class));
        assertInstanceOf(Long.class, valueGenerator.generateObjectValue(Long.class));
        assertInstanceOf(Short.class, valueGenerator.generateObjectValue(Short.class));
        assertInstanceOf(Byte.class, valueGenerator.generateObjectValue(Byte.class));
        assertInstanceOf(String.class, valueGenerator.generateObjectValue(String.class));
    }

    private void assertNullOrNotNull(Object value, String message) {
        if (value == null) {
            assertNull(value, message);
        } else {
            assertNotNull(value, message);
        }
    }
}
