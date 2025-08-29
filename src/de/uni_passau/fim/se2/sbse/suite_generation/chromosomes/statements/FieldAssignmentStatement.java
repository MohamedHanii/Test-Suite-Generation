package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import java.lang.reflect.Field;

import static de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.FormatParameter.formatParameter;

public class FieldAssignmentStatement implements Statement {

    private final Field field;
    private final Object value;
    private final Object cutInstance;

    public FieldAssignmentStatement(Field field, Object value, Object cutInstance) {
        this.field = field;
        this.value = value;
        this.cutInstance = cutInstance;
    }

    @Override
    public void run() {
        try {
            field.setAccessible(true);
            field.set(cutInstance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to assign field", e);
        }
    }

    @Override
    public String toString() {
        return "this." + field.getName() + " = " + formatValue() + ";";
    }

    private String formatValue() {
        return formatParameter(value);
    }
}
