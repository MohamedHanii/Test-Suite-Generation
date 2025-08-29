package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TypeUtilsTest {

    @Test
    void testIsPrimitiveOrWrapperWithPrimitives() {
        assertTrue(TypeUtils.isPrimitiveOrWrapper(int.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(boolean.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(char.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(float.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(double.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(long.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(short.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(byte.class));
    }

    @Test
    void testIsPrimitiveOrWrapperWithWrappers() {
        assertTrue(TypeUtils.isPrimitiveOrWrapper(Integer.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(Boolean.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(Character.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(Float.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(Double.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(Long.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(Short.class));
        assertTrue(TypeUtils.isPrimitiveOrWrapper(Byte.class));
    }

    @Test
    void testIsPrimitiveOrWrapperWithOtherTypes() {
        assertFalse(TypeUtils.isPrimitiveOrWrapper(String.class));
        assertFalse(TypeUtils.isPrimitiveOrWrapper(Object.class));
    }
}
