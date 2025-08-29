package de.uni_passau.fim.se2.sbse.suite_generation.algorithms;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.ChromosomeGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.StatementChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.selection.Selection;
import de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions.StoppingCondition;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MOSATest {

    @Mock
    private StoppingCondition stoppingCondition;

    @Mock
    private ChromosomeGenerator<StatementChromosome> chromosomeGenerator;

    @Mock
    private Selection<StatementChromosome> selection;

    @Mock
    private FitnessFunction<StatementChromosome> fitnessFunction;

    @Mock
    private IBranch branch1, branch2;

    private Set<IBranch> branches;
    private Random random;
    private MOSA<StatementChromosome> mosa;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(branch1.getId()).thenReturn(1);
        when(branch2.getId()).thenReturn(2);
        branches = new HashSet<>(Arrays.asList(branch1, branch2));
        random = new Random(42);
        mosa = new MOSA<>(stoppingCondition, chromosomeGenerator, 10,
                branches, selection, random, fitnessFunction);
    }

    @Test
    public void testGetInitialPopulation() {
        List<StatementChromosome> mockPopulation = Arrays.asList(
                createMockChromosome(0.5, Map.of(1, 0.5, 2, 1.0)),
                createMockChromosome(0.3, Map.of(1, 0.3, 2, 0.8))
        );

        when(chromosomeGenerator.get())
                .thenReturn(mockPopulation.get(0), mockPopulation.get(1));
        when(fitnessFunction.applyAsDouble(any()))
                .thenAnswer(inv -> ((StatementChromosome) inv.getArgument(0)).getFitness());

        Map<Integer, StatementChromosome> archive = new HashMap<>();
        List<StatementChromosome> population = mosa.getInitialPopulation(archive);

        assertEquals(10, population.size());
        verify(fitnessFunction, times(10)).applyAsDouble(any());
    }

    @Test
    public void testUpdateArchive() {
        Map<Integer, StatementChromosome> archive = new HashMap<>();

        StatementChromosome candidate1 = createMockChromosome(0.0, Map.of(1, 0.0, 2, Double.MAX_VALUE));
        StatementChromosome candidate2 = createMockChromosome(0.0, Map.of(1, Double.MAX_VALUE, 2, 0.0));

        mosa.updateArchive(archive, Arrays.asList(candidate1, candidate2));

        assertEquals(2, archive.size());
        assertTrue(archive.containsKey(1));
        assertTrue(archive.containsKey(2));
    }

    @Test
    public void testPreferenceSorting() {

        List<StatementChromosome> population = Arrays.asList(
                createMockChromosome(0.5, Map.of(1, 0.5, 2, 1.0)),
                createMockChromosome(0.3, Map.of(1, 0.3, 2, 0.8)),
                createMockChromosome(0.0, Map.of(1, 0.0, 2, 0.0))
        );

        Map<Integer, StatementChromosome> archive = new HashMap<>();
        List<List<StatementChromosome>> fronts = mosa.preferenceSorting(population, archive);

        assertEquals(3, fronts.size());
        assertEquals(1, fronts.get(0).size());
    }

    @Test
    public void testFastNonDominatedSort() {
        List<StatementChromosome> solutions = Arrays.asList(
                createMockChromosome(1.0, Map.of(1, 1.0, 2, 2.0)),
                createMockChromosome(0.8, Map.of(1, 0.8, 2, 1.5)),
                createMockChromosome(0.5, Map.of(1, 0.5, 2, 1.0))
        );

        List<List<Integer>> fronts = mosa.fastNonDominatedSort(solutions, new HashMap<>());
        assertEquals(3, fronts.size());
        assertEquals(1, fronts.get(0).size());
    }

    @Test
    public void testDominates() {
        StatementChromosome dominant = createMockChromosome(0.5, Map.of(1, 0.5, 2, 0.7));
        StatementChromosome weak = createMockChromosome(1.0, Map.of(1, 1.0, 2, 1.0));

        assertTrue(mosa.dominates(dominant, weak, new HashMap<>()));
        assertFalse(mosa.dominates(weak, dominant, new HashMap<>()));
    }

    @Test
    public void testSubvectorDominanceAssignment() {
        List<StatementChromosome> solutions = Arrays.asList(
                createMockChromosome(1.0, Map.of(1, 1.0)),
                createMockChromosome(0.5, Map.of(1, 0.5)),
                createMockChromosome(0.3, Map.of(1, 0.3))
        );

        mosa.subvectorDominanceAssignment(solutions, new HashMap<>());
        solutions.sort(Comparator.comparingDouble(c -> ((StatementChromosome) c).getDensity()));

        assertEquals(1.0,
                solutions.get(0).getBranchDistances().get(1), 0.01);
    }

    @Test
    public void testGenerateOffspring() {
        StatementChromosome parent1 = createMockChromosome(0.5, Map.of(1, 0.5));
        StatementChromosome parent2 = createMockChromosome(0.7, Map.of(1, 0.7));
        List<StatementChromosome> population = Arrays.asList(parent1, parent2);

        when(selection.apply(any())).thenReturn(parent1, parent2);
        when(parent1.crossover(parent2))
                .thenReturn(new Pair<>(parent1, parent2));

        List<StatementChromosome> offspring = mosa.generateOffspring(population);

        assertEquals(2, offspring.size());
        verify(fitnessFunction, times(2)).applyAsDouble(any());
    }

    private StatementChromosome createMockChromosome(double fitness, Map<Integer, Double> distances) {
        Map<Integer, Double> fullDistances = new HashMap<>();
        fullDistances.put(1, Double.MAX_VALUE);
        fullDistances.put(2, Double.MAX_VALUE);
        fullDistances.putAll(distances);

        StatementChromosome mock = mock(StatementChromosome.class);
        when(mock.getBranchDistances()).thenReturn(fullDistances);
        when(mock.copy()).thenReturn(mock);
        when(mock.mutate()).thenReturn(mock);
        when(mock.call()).thenReturn(distances);
        return mock;
    }

}
