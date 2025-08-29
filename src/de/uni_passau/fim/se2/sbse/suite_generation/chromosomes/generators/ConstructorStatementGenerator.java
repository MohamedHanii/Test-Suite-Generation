package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.ConstructorStatement;

import java.lang.reflect.Constructor;
import java.util.Random;

public class ConstructorStatementGenerator {
    private final Class<?> cutClass;
    private final Random random;
    private final ValueGenerator valueGenerator;

    public ConstructorStatementGenerator(Class<?> cutClass, Random random) {
        this.cutClass = cutClass;
        this.random = random;
        this.valueGenerator = new ValueGenerator(random);
    }

    public ConstructorStatement generate() {
        Constructor<?>[] constructors = cutClass.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new IllegalStateException("No constructors found for " + cutClass.getName());
        }

        Constructor<?> constructor = constructors[random.nextInt(constructors.length)];
        Object[] params = valueGenerator.generateParameters(constructor.getParameterTypes(), true);
        ConstructorStatement statement = new ConstructorStatement(constructor, params);
        statement.run();
        return statement;
    }
}
