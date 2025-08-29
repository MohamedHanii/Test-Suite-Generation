package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ConstructorStatement implements Statement {
    private final Constructor<?> constructor;
    private final Object[] parameters;
    private static Object currentInstance;

    public static Object getCurrentInstance() {
        return currentInstance;
    }

    public ConstructorStatement(Constructor<?> constructor, Object[] parameters) {
        this.constructor = constructor;
        this.parameters = parameters;
        this.constructor.setAccessible(true);
    }

    @Override
    public void run() {
        try {
            currentInstance = constructor.newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate " + constructor.getDeclaringClass().getSimpleName(), e);
        }
    }

    @Override
    public String toString() {
        final String className = constructor.getDeclaringClass().getSimpleName();
        final String params = String.join(", ", Arrays.stream(parameters)
                .map(this::formatParameter)
                .toArray(String[]::new));
        return String.format("%s obj = new %s(%s);", className, className, params);
    }

    private String formatParameter(Object param) {
        if (param == null) {
            return "null";
        } else if (param instanceof String) {
            return "\"" + param + "\"";
        } else if (param instanceof Character) {
            return "'" + param + "'";
        } else {
            return param.toString();
        }
    }
}