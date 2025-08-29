package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

public class ConstructorStatementTest {
    static class TestClass {
        private final String value;
        private final int number;

        public TestClass(String value, int number) {
            this.value = value;
            this.number = number;
        }

        public String getValue() {
            return value;
        }

        public int getNumber() {
            return number;
        }
    }

    @Test
    void testConstructorStatementValid() throws Exception {
        Constructor<TestClass> constructor = TestClass.class.getConstructor(String.class, int.class);
        Object[] parameters = {"test", 42};

        ConstructorStatement statement = new ConstructorStatement(constructor, parameters);
        statement.run();

        TestClass instance = (TestClass) ConstructorStatement.getCurrentInstance();
        assertNotNull(instance);
        assertEquals("test", instance.getValue());
        assertEquals(42, instance.getNumber());
    }

    @Test
    void testToString() throws Exception {
        Constructor<TestClass> constructor = TestClass.class.getConstructor(String.class, int.class);
        Object[] parameters = {"example", 99};

        ConstructorStatement statement = new ConstructorStatement(constructor, parameters);
        String result = statement.toString();

        assertEquals("TestClass obj = new TestClass(\"example\", 99);", result);
    }

    @Test
    void testConstructorStatementNullConstructor() {
        Constructor<?> constructor = null;
        Object[] parameters = {};

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new ConstructorStatement(constructor, parameters));
        assertEquals("Cannot invoke \"java.lang.reflect.Constructor.setAccessible(boolean)\" because \"this.constructor\" is null", exception.getMessage());
    }

    @Test
    void testConstructorStatementRunFails() throws Exception {
        Constructor<TestClass> constructor = TestClass.class.getConstructor(String.class, int.class);
        Object[] invalidParameters = {42, "invalid"}; // Incorrect order of parameters

        ConstructorStatement statement = new ConstructorStatement(constructor, invalidParameters);

        RuntimeException exception = assertThrows(RuntimeException.class, statement::run);
        assertTrue(exception.getMessage().contains("Failed to instantiate TestClass"));
    }

    @Test
    void testConstructorStatementNullParameters() throws Exception {
        Constructor<TestClass> constructor = TestClass.class.getConstructor(String.class, int.class);
        Object[] parameters = {null, 0};


        ConstructorStatement statement = new ConstructorStatement(constructor, parameters);
        statement.run();

        TestClass instance = (TestClass) ConstructorStatement.getCurrentInstance();
        assertNotNull(instance);
        assertNull(instance.getValue());
        assertEquals(0, instance.getNumber());
    }

    @Test
    void testFormatParameter() throws Exception {
        Constructor<TestClass> constructor = TestClass.class.getConstructor(String.class, int.class);
        Object[] parameters = {"text", 123, null};

        ConstructorStatement statement = new ConstructorStatement(constructor, parameters);

        String toStringResult = statement.toString();
        assertEquals("TestClass obj = new TestClass(\"text\", 123, null);", toStringResult);
    }
}
