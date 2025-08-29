package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodStatementTest {

    static class TestClass {
        private int value;

        public void setValue(int value) {
            this.value = value;
        }

        public void throwException() {
            throw new RuntimeException("Test exception");
        }

        public int getValue() {
            return value;
        }
    }

    @Test
    void testRunMethodInvocation() throws NoSuchMethodException {
        TestClass instance = new TestClass();
        Method method = TestClass.class.getMethod("setValue", int.class);

        MethodStatement statement = new MethodStatement(instance, method, new Object[]{42});
        statement.run();

        assertEquals(42, instance.getValue());
    }

    @Test
    void testRunHandlesExceptionGracefully() throws NoSuchMethodException {
        TestClass instance = new TestClass();
        Method method = TestClass.class.getMethod("throwException");

        MethodStatement statement = new MethodStatement(instance, method, new Object[]{});

        assertDoesNotThrow(statement::run);
    }

    @Test
    void testToStringWithoutParameters() throws NoSuchMethodException {
        TestClass instance = new TestClass();
        Method method = TestClass.class.getMethod("throwException");

        MethodStatement statement = new MethodStatement(instance, method, new Object[]{});
        String expected = "this.throwException();";

        assertEquals(expected, statement.toString());
    }

    @Test
    void testToStringWithParameters() throws NoSuchMethodException {
        TestClass instance = new TestClass();
        Method method = TestClass.class.getMethod("setValue", int.class);

        MethodStatement statement = new MethodStatement(instance, method, new Object[]{42});
        String expected = "this.setValue(42);";

        assertEquals(expected, statement.toString());
    }

    @Test
    void testToStringWithMultipleParameters() throws NoSuchMethodException {
        class TestMultipleParams {
            public void methodWithParams(int a, String b) {
            }
        }

        TestMultipleParams instance = new TestMultipleParams();
        Method method = TestMultipleParams.class.getMethod("methodWithParams", int.class, String.class);

        MethodStatement statement = new MethodStatement(instance, method, new Object[]{10, "hello"});
        String expected = "this.methodWithParams(10, \"hello\");";

        assertEquals(expected, statement.toString());
    }
}
