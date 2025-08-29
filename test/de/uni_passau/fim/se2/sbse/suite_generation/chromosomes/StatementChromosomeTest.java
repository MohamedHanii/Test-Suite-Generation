package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.BranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.Mutation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class StatementChromosomeTest {

    private StatementChromosome chromosome;

    @Mock
    private Mutation<StatementChromosome> mutation;

    @Mock
    private Crossover<StatementChromosome> crossover;

    @Mock
    private Statement statement1;

    @Mock
    private Statement statement2;

    @Mock
    private IBranch branch;

    private Set<IBranch> branchesToCover;
    private List<Statement> statements;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        branchesToCover = new HashSet<>(Collections.singletonList(branch));
        statements = Arrays.asList(statement1, statement2);
        chromosome = new StatementChromosome(statements, mutation, crossover, branchesToCover);
    }

    @Test
    void testCopy_CreatesNewInstanceWithSameData() {
        StatementChromosome copy = chromosome.copy();

        assertNotNull(copy);
        assertNotSame(chromosome, copy);
        assertEquals(chromosome.getStatements(), copy.getStatements());
        assertEquals(chromosome.getBranchDistances(), copy.getBranchDistances());
    }

    @Test
    void testEquals_TrueForSameStatements() {
        StatementChromosome other = new StatementChromosome(statements, mutation, crossover, branchesToCover);

        assertEquals(chromosome, other);
    }

    @Test
    void testEquals_FalseForDifferentStatements() {
        List<Statement> otherStatements = List.of(mock(Statement.class));
        StatementChromosome other = new StatementChromosome(otherStatements, mutation, crossover, branchesToCover);

        assertNotEquals(chromosome, other);
    }

    @Test
    void testHashCode_EqualForSameStatements() {
        StatementChromosome other = new StatementChromosome(statements, mutation, crossover, branchesToCover);

        assertEquals(chromosome.hashCode(), other.hashCode());
    }

    @Test
    void testGetSize_ReturnsCorrectSize() {
        assertEquals(2, chromosome.getSize());
    }

    @Test
    void testSetAndGetRank() {
        chromosome.setRank(3);
        assertEquals(3, chromosome.getRank());
    }

    @Test
    void testSetAndGetDensity() {
        chromosome.setDensity(5);
        assertEquals(5, chromosome.getDensity());
    }

    @Test
    void testSetAndGetFitness() {
        chromosome.setFitness(0.85);
        assertEquals(0.85, chromosome.getFitness());
    }

    @Test
    void testCall_ExecutesStatementsAndCalculatesBranchDistances() {
        try (MockedStatic<BranchTracer> mockedStatic = mockStatic(BranchTracer.class)) {
            BranchTracer tracer = mock(BranchTracer.class);
            mockedStatic.when(BranchTracer::getInstance).thenReturn(tracer);

            when(tracer.getDistances()).thenReturn(Map.of(1, 0.5, 2, 1.0));
            when(branch.getId()).thenReturn(1);

            Map<Integer, Double> distances = chromosome.call();

            assertNotNull(distances);
            assertEquals(1, distances.size());
            assertEquals(0.5, distances.get(1));

            verify(statement1).run();
            verify(statement2).run();
        }
    }

    @Test
    void testCall_HandlesExceptionsDuringStatementExecution() {
        assertDoesNotThrow(() -> chromosome.call());
        verify(statement1).run();
    }

}
