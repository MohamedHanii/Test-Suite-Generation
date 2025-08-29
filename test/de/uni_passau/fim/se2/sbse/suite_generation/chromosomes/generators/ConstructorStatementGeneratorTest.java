package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.ConstructorStatement;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConstructorStatementGeneratorTest {
    static class TestClassWithDefaultConstructor {
        public TestClassWithDefaultConstructor() {
        }
    }

    static class TestClassWithParameters {
        private final int value;
        private final String name;

        public TestClassWithParameters(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    static class TestClassWithNoConstructor {
        private TestClassWithNoConstructor() {
        }
    }

    @Test
    void testGenerateDefaultConstructor() {
        ConstructorStatementGenerator generator = new ConstructorStatementGenerator(TestClassWithDefaultConstructor.class, new Random());
        ConstructorStatement statement = generator.generate();

        assertNotNull(statement);
        assertNotNull(ConstructorStatement.getCurrentInstance());
        assertTrue(ConstructorStatement.getCurrentInstance() instanceof TestClassWithDefaultConstructor);
    }

    @Test
    void testGenerateConstructorWithParameters() {
        ConstructorStatementGenerator generator = new ConstructorStatementGenerator(TestClassWithParameters.class, new Random());
        ConstructorStatement statement = generator.generate();

        assertNotNull(statement);
        assertNotNull(ConstructorStatement.getCurrentInstance());
        assertTrue(ConstructorStatement.getCurrentInstance() instanceof TestClassWithParameters);
    }

    @Test
    void testGenerateUsesRandomConstructor() {
        class TestMultipleConstructors {
            public TestMultipleConstructors() {
            }

            public TestMultipleConstructors(int value) {
            }
        }

        ConstructorStatementGenerator generator = new ConstructorStatementGenerator(TestMultipleConstructors.class, new Random());
        ConstructorStatement statement = generator.generate();

        assertNotNull(statement);
        assertNotNull(ConstructorStatement.getCurrentInstance());
        assertTrue(ConstructorStatement.getCurrentInstance() instanceof TestMultipleConstructors);
    }
}
