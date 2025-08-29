package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators;

import java.util.Set;

public class TypeUtils {
    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        if (type.isPrimitive()) {
            return true;
        }
        return Set.of(
                Integer.class, Boolean.class, Character.class,
                Float.class, Double.class, Long.class,
                Short.class, Byte.class
        ).contains(type);
    }
}
