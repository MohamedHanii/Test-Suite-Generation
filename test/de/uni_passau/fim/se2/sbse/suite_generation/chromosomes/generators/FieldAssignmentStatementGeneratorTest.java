package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.ConstructorStatement;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.FieldAssignmentStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Random;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.*;

public class FieldAssignmentStatementGeneratorTest {
    static class TestClass {
        private int intValue;
        private String stringValue;
        private boolean booleanValue;
        private static double staticValue;
    }

    private FieldAssignmentStatementGenerator generator;

    @BeforeEach
    void setUp() {
        ConstructorStatement statement = new ConstructorStatement(
                TestClass.class.getDeclaredConstructors()[0],
                new Object[]{}
        );
        statement.run();

        generator = new FieldAssignmentStatementGenerator(TestClass.class, new Random());
    }

    @Test
    void testGenerateFieldAssignmentStatement() {
        FieldAssignmentStatement statement = generator.generate();

        assertNotNull(statement);
        assertNotNull(statement.toString());

        statement.run();

        Object instance = ConstructorStatement.getCurrentInstance();
        assertNotNull(instance);

        boolean atLeastOneFieldAssigned = false;
        for (Field field : TestClass.class.getDeclaredFields()) {
            if (!field.getType().isPrimitive() && !TypeUtils.isPrimitiveOrWrapper(field.getType())) continue;
            field.setAccessible(true);
            try {
                if (field.get(instance) != null) {
                    atLeastOneFieldAssigned = true;
                    break;
                }
            } catch (IllegalAccessException e) {
                fail("Field access failed: " + e.getMessage());
            }
        }

        assertTrue(atLeastOneFieldAssigned);
    }

    @Test
    void testGenerateReturnsNullWhenNoFields() {
        class EmptyClass {
            private static int staticField;
            private String nonPrimitiveField;
        }

        FieldAssignmentStatementGenerator emptyGenerator = new FieldAssignmentStatementGenerator(EmptyClass.class, new Random());
        FieldAssignmentStatement statement = emptyGenerator.generate();

        assertNull(statement);
    }

    @Test
    void testGenerateHandlesPrimitiveFields() throws NoSuchFieldException {
        FieldAssignmentStatement statement = generator.generate();

        assertNotNull(statement);

        // Ensure the generated statement assigns a primitive or wrapper field
        Field field = statement.getClass().getDeclaredField("field");
        field.setAccessible(true);
        try {
            Field assignedField = (Field) field.get(statement);
            assertTrue(
                    assignedField.getType().isPrimitive() || TypeUtils.isPrimitiveOrWrapper(assignedField.getType())
            );
        } catch (IllegalAccessException e) {
            fail("Field access failed: " + e.getMessage());
        }
    }
}
