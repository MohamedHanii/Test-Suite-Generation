package de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.StatementChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BranchFitnessFunctionTest {

    @Mock
    private StatementChromosome chromosome;

    @Mock
    private IBranch branch1, branch2, branch3;

    private BranchFitnessFunction fitnessFunction;
    private final Map<Integer, Double> testDistances = new HashMap<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(branch1.getId()).thenReturn(1);
        when(branch2.getId()).thenReturn(2);
        when(branch3.getId()).thenReturn(3);

        testDistances.clear();
        testDistances.put(1, 0.5);
        testDistances.put(2, 1.5);
    }

    @Test
    public void testValidDistanceCalculation() {
        // Setup test scenario
        when(chromosome.call()).thenReturn(testDistances);
        fitnessFunction = new BranchFitnessFunction(Set.of(branch1, branch2));

        // Execute and verify
        assertEquals(2.0, fitnessFunction.applyAsDouble(chromosome), 0.001);
    }

    @Test
    public void testMissingBranchDistanceHandling() {
        // Add third branch not in distance map
        when(chromosome.call()).thenReturn(testDistances);
        fitnessFunction = new BranchFitnessFunction(Set.of(branch1, branch2, branch3));

        // Expected: 0.5 + 1.5 + Double.MAX_VALUE
        double expected = 0.5 + 1.5 + Double.MAX_VALUE;
        assertEquals(expected, fitnessFunction.applyAsDouble(chromosome), 0.001);
    }

    @Test
    public void testExceptionHandling() {
        when(chromosome.call()).thenThrow(new RuntimeException("Simulated error"));
        fitnessFunction = new BranchFitnessFunction(Set.of(branch1));

        assertEquals(Double.MAX_VALUE, fitnessFunction.applyAsDouble(chromosome), 0.0);
    }

    @Test
    public void testOptimizationDirection() {
        fitnessFunction = new BranchFitnessFunction(Set.of(branch1));
        assertTrue(fitnessFunction.isMinimizing());
    }
}
