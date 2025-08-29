package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.FormatParameter.formatParameter;

public class MethodStatement implements Statement {
    private final Object instance;
    private final Method method;
    private final Object[] parameters;

    public MethodStatement(Object instance, Method method, Object[] parameters) {
        this.instance = instance;
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public void run() {
        try {
            method.setAccessible(true);
            method.invoke(instance, parameters);
        } catch (Exception _) {

        }
    }

    @Override
    public String toString() {
        return "this." + method.getName() + "(" + formatParameters() + ");";
    }

    private String formatParameters() {
        List<String> formattedParams = new ArrayList<>();
        for (Object param : parameters) {
            formattedParams.add(formatParameter(param));
        }
        return String.join(", ", formattedParams);
    }

}
