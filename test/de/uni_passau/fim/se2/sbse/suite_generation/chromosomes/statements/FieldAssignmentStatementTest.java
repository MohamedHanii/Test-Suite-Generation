package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FieldAssignmentStatementTest {
    static class TestClass {
        private String privateField;
        public int publicField;
        protected boolean protectedField;
    }

    @Test
    void testFieldAssignmentToPrivateField() throws Exception {
        TestClass instance = new TestClass();
        Field field = TestClass.class.getDeclaredField("privateField");
        String value = "NewValue";

        FieldAssignmentStatement statement = new FieldAssignmentStatement(field, value, instance);

        statement.run();

        assertEquals(value, instance.privateField);
        assertEquals("this.privateField = \"NewValue\";", statement.toString());
    }

    @Test
    void testFieldAssignmentToPublicField() throws Exception {
        TestClass instance = new TestClass();
        Field field = TestClass.class.getDeclaredField("publicField");
        int value = 42;

        FieldAssignmentStatement statement = new FieldAssignmentStatement(field, value, instance);


        statement.run();

        assertEquals(value, instance.publicField);
        assertEquals("this.publicField = 42;", statement.toString());
    }

    @Test
    void testFieldAssignmentToProtectedField() throws Exception {
        TestClass instance = new TestClass();
        Field field = TestClass.class.getDeclaredField("protectedField");
        boolean value = true;

        FieldAssignmentStatement statement = new FieldAssignmentStatement(field, value, instance);

        statement.run();

        assertEquals(value, instance.protectedField);
        assertEquals("this.protectedField = true;", statement.toString());
    }

    @Test
    void testFieldAssignmentToNullValue() throws Exception {
        TestClass instance = new TestClass();
        Field field = TestClass.class.getDeclaredField("privateField");

        FieldAssignmentStatement statement = new FieldAssignmentStatement(field, null, instance);


        statement.run();

        assertNull(instance.privateField);
        assertEquals("this.privateField = null;", statement.toString());
    }

}
