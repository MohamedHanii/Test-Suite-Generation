package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.ConstructorStatement;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.FieldAssignmentStatement;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Random;

public class FieldAssignmentStatementGenerator {
    private final Class<?> cutClass;
    private final Random random;
    private final ValueGenerator valueGenerator;

    public FieldAssignmentStatementGenerator(Class<?> cutClass, Random random) {
        this.cutClass = cutClass;
        this.random = random;
        this.valueGenerator = new ValueGenerator(random);
    }

    public FieldAssignmentStatement generate() {
        Field[] fields = Arrays.stream(cutClass.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .filter(f -> TypeUtils.isPrimitiveOrWrapper(f.getType()))
                .toArray(Field[]::new);

        if (fields.length == 0) {
            return null;
        }

        Field field = fields[random.nextInt(fields.length)];
        Object value = valueGenerator.generateValue(field.getType(), true);
        Object instance = ConstructorStatement.getCurrentInstance();

        return new FieldAssignmentStatement(field, value, instance);
    }
}
