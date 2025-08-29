package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.ConstructorStatement;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.MethodStatement;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Random;

public class MethodStatementGenerator {
    private final Class<?> cutClass;
    private final Random random;
    private final ValueGenerator valueGenerator;

    public MethodStatementGenerator(Class<?> cutClass, Random random) {
        this.cutClass = cutClass;
        this.random = random;
        this.valueGenerator = new ValueGenerator(random);
    }

    public MethodStatement generate() {
        Method[] methods = Arrays.stream(cutClass.getDeclaredMethods())
                .filter(m -> m.getDeclaringClass() == cutClass)
                .toArray(Method[]::new);

        if (methods.length == 0) {
            return null;
        }

        Method method = methods[random.nextInt(methods.length)];
        Object[] params = valueGenerator.generateParameters(method.getParameterTypes(), true);
        boolean isStatic = Modifier.isStatic(method.getModifiers());
        Object instance = isStatic ? null : ConstructorStatement.getCurrentInstance();
        return new MethodStatement(instance, method, params);
    }
}
