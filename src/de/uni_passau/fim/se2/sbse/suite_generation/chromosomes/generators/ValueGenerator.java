package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators;

import java.util.Arrays;
import java.util.Random;

public class ValueGenerator {
    private final Random random;

    public ValueGenerator(Random random) {
        this.random = random;
    }

    public Object generateValue(Class<?> type, boolean allowNull) {
        if (random.nextDouble() < 0.25 && Number.class.isAssignableFrom(type)) {
            return generateEdgeCase(type);
        }
        if (type.isPrimitive()) {
            return generatePrimitiveValue(type);
        } else {
            return allowNull && random.nextBoolean() ? null : generateObjectValue(type);
        }
    }

    public Object[] generateParameters(Class<?>[] paramTypes, boolean allowNull) {
        return Arrays.stream(paramTypes)
                .map(type -> generateValue(type, allowNull))
                .toArray();
    }

    public Object generateEdgeCase(Class<?> type) {
        int choice = random.nextInt(3); // 0: MIN (-1024), 1: MAX (1023), 2: Zero

        // Handle all numeric types according to requirements
        if (type == Integer.class || type == int.class) {
            return switch (choice) {
                case 0 -> -1024;
                case 1 -> 1023;
                default -> 0;
            };
        } else if (type == Long.class || type == long.class) {
            return switch (choice) {
                case 0 -> -1024L;
                case 1 -> 1023L;
                default -> 0L;
            };
        } else if (type == Double.class || type == double.class) {
            return switch (choice) {
                case 0 -> -1024.0;
                case 1 -> 1023.0;
                default -> 0.0;
            };
        } else if (type == Float.class || type == float.class) {
            return switch (choice) {
                case 0 -> -1024.0f;
                case 1 -> 1023.0f;
                default -> 0.0f;
            };
        } else if (type == Short.class || type == short.class) {
            return (short) switch (choice) {
                case 0 -> -1024;
                case 1 -> 1023;
                default -> 0;
            };
        } else if (type == Byte.class || type == byte.class) {
            return (byte) switch (choice) {
                case 0 -> -1024;
                case 1 -> 1023;
                default -> 0;
            };
        } else if (type == Character.class || type == char.class) {
            // Align with string requirements (ASCII 32-126)
            return switch (choice) {
                case 0 -> (char) 32;   // Space
                case 1 -> (char) 126;  // ~
                default -> (char) 65;  // 'A' (typical ASCII value)
            };
        } else if (type == Boolean.class || type == boolean.class) {
            return random.nextBoolean();
        }

        return null; // Fallback for unsupported types
    }

    public Object generatePrimitiveValue(Class<?> type) {
        if (type == int.class) return random.nextInt(2048) - 1024;
        if (type == boolean.class) return random.nextBoolean();
        if (type == char.class) return (char) (random.nextInt(95) + 32);
        if (type == float.class) return random.nextFloat() * 2048 - 1024;
        if (type == double.class) return random.nextDouble() * 2048 - 1024;
        if (type == long.class) return random.nextLong();
        if (type == short.class) return (short) (random.nextInt(2048) - 1024);
        if (type == byte.class) return (byte) (random.nextInt(256) - 128);
        return 0; // Fallback for unsupported primitives
    }

    public Object generateObjectValue(Class<?> type) {
        if (type == Integer.class) return random.nextInt(2048) - 1024;
        if (type == Boolean.class) return random.nextBoolean();
        if (type == Character.class) return (char) (random.nextInt(95) + 32);
        if (type == Float.class) return random.nextFloat() * 2048 - 1024;
        if (type == Double.class) return random.nextDouble() * 2048 - 1024;
        if (type == Long.class) return random.nextLong();
        if (type == Short.class) return (short) (random.nextInt(2048) - 1024);
        if (type == Byte.class) return (byte) (random.nextInt(256) - 128);
        if (type == String.class) return generateRandomString();
        return null; // Unsupported object types
    }

    public String generateRandomString() {
        int length = random.nextInt(10) + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) (random.nextInt(95) + 32));
        }
        return sb.toString();
    }
}