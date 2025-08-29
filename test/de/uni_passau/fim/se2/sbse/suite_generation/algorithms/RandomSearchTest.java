package de.uni_passau.fim.se2.sbse.suite_generation.algorithms;


import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.ChromosomeGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.StatementChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.BranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions.StoppingCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RandomSearchTest {
    @Mock
    private StoppingCondition stoppingCondition;

    @Mock
    private ChromosomeGenerator<StatementChromosome> chromosomeGenerator;

    @Mock
    private FitnessFunction<StatementChromosome> fitnessFunction;

    @Mock
    private IBranch branch1, branch2;

    @Mock
    private BranchTracer branchTracer;

    private Set<IBranch> branchesToCover;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(branch1.getId()).thenReturn(1);
        when(branch2.getId()).thenReturn(2);
        branchesToCover = new HashSet<>(Arrays.asList(branch1, branch2));
    }

    @Test
    public void testInitialization() throws Exception {
        try (MockedStatic<BranchTracer> mockedBranchTracer = mockStatic(BranchTracer.class)) {
            mockedBranchTracer.when(BranchTracer::getInstance).thenReturn(branchTracer);

            RandomSearch<StatementChromosome> randomSearch = new RandomSearch<>(
                    stoppingCondition, chromosomeGenerator, fitnessFunction, branchesToCover);

            Field archiveField = RandomSearch.class.getDeclaredField("archive");
            archiveField.setAccessible(true);
            Map<Integer, StatementChromosome> archive =
                    (Map<Integer, StatementChromosome>) archiveField.get(randomSearch);

            Field bestDistancesField = RandomSearch.class.getDeclaredField("bestDistances");
            bestDistancesField.setAccessible(true);
            Map<Integer, Double> bestDistances =
                    (Map<Integer, Double>) bestDistancesField.get(randomSearch);

            assertEquals(2, archive.size());
            assertNull(archive.get(1));
            assertNull(archive.get(2));

            assertEquals(2, bestDistances.size());
            assertEquals(Double.MAX_VALUE, bestDistances.get(1), 0.0);
            assertEquals(Double.MAX_VALUE, bestDistances.get(2), 0.0);
        }
    }

    @Test
    public void testUpdateArchiveImprovesDistance() throws Exception {
        StatementChromosome candidate = mock(StatementChromosome.class);
        Map<Integer, Double> distances = new HashMap<>();
        distances.put(1, 0.5);
        distances.put(2, 2.0);

        when(chromosomeGenerator.get()).thenReturn(candidate);
        when(fitnessFunction.applyAsDouble(candidate)).thenReturn(0.0);
        when(stoppingCondition.searchCanContinue()).thenReturn(true, false);
        when(branchTracer.getDistances()).thenReturn(distances);

        try (MockedStatic<BranchTracer> mockedBranchTracer = mockStatic(BranchTracer.class)) {
            mockedBranchTracer.when(BranchTracer::getInstance).thenReturn(branchTracer);

            RandomSearch<StatementChromosome> randomSearch = new RandomSearch<>(
                    stoppingCondition, chromosomeGenerator, fitnessFunction, branchesToCover);

            List<StatementChromosome> suite = randomSearch.findSolution();

            Field archiveField = RandomSearch.class.getDeclaredField("archive");
            archiveField.setAccessible(true);

            Field bestDistancesField = RandomSearch.class.getDeclaredField("bestDistances");
            bestDistancesField.setAccessible(true);
            Map<Integer, Double> bestDistances =
                    (Map<Integer, Double>) bestDistancesField.get(randomSearch);

            assertEquals(0.5, bestDistances.get(1), 0.0);
            assertEquals(2.0, bestDistances.get(2), 0.0);
        }
    }

    @Test
    public void testBuildTestSuite() throws Exception {
        StatementChromosome candidate1 = mock(StatementChromosome.class);

        RandomSearch<StatementChromosome> randomSearch = new RandomSearch<>(
                stoppingCondition, chromosomeGenerator, fitnessFunction, branchesToCover);

        Field archiveField = RandomSearch.class.getDeclaredField("archive");
        archiveField.setAccessible(true);
        Map<Integer, StatementChromosome> archive =
                (Map<Integer, StatementChromosome>) archiveField.get(randomSearch);

        archive.put(1, candidate1);
        archive.put(2, candidate1);
        archive.put(3, null);

        List<StatementChromosome> suite = randomSearch.buildTestSuite();

        assertEquals(1, suite.size());
        assertTrue(suite.contains(candidate1));
    }

    @Test
    public void testStoppingConditionNotifications() {
        when(stoppingCondition.searchCanContinue()).thenReturn(true, false);
        StatementChromosome candidate = mock(StatementChromosome.class);
        when(chromosomeGenerator.get()).thenReturn(candidate);
        when(fitnessFunction.applyAsDouble(candidate)).thenReturn(0.0);

        try (MockedStatic<BranchTracer> mockedBranchTracer = mockStatic(BranchTracer.class)) {
            mockedBranchTracer.when(BranchTracer::getInstance).thenReturn(branchTracer);
            when(branchTracer.getDistances()).thenReturn(new HashMap<>());

            RandomSearch<StatementChromosome> randomSearch = new RandomSearch<>(
                    stoppingCondition, chromosomeGenerator, fitnessFunction, branchesToCover);

            randomSearch.findSolution();

            verify(stoppingCondition, times(2)).notifySearchStarted();
            verify(stoppingCondition, times(1)).notifyFitnessEvaluation();
        }
    }

    @Test
    public void testFindSolutionTerminatesWhenStoppingConditionMet() {
        when(stoppingCondition.searchCanContinue()).thenReturn(false);

        RandomSearch<StatementChromosome> randomSearch = new RandomSearch<>(
                stoppingCondition, chromosomeGenerator, fitnessFunction, branchesToCover);

        List<StatementChromosome> suite = randomSearch.findSolution();

        assertTrue(suite.isEmpty());
        verify(chromosomeGenerator, never()).get();
    }
}
