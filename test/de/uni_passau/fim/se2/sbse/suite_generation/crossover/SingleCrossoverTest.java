package de.uni_passau.fim.se2.sbse.suite_generation.crossover;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.StatementChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SingleCrossoverTest {

    @Mock
    private StatementChromosome parent1, parent2, offspring1, offspring2;

    @Mock
    private Statement s1, s2, s3, s4, w, x, y, z;

    @Mock
    Random random;

    private SingleCrossover crossover;

    private List<Statement> offspring1Stats;
    private List<Statement> offspring2Stats;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        List<Statement> parent1Statements = Arrays.asList(s1, s2, s3, s4);
        List<Statement> parent2Statements = Arrays.asList(w, x, y, z);

        when(parent1.copy()).thenReturn(offspring1);
        when(parent2.copy()).thenReturn(offspring2);
        when(parent1.getSize()).thenReturn(4);
        when(parent2.getSize()).thenReturn(4);
        when(parent1.getStatements()).thenReturn(parent1Statements);
        when(parent2.getStatements()).thenReturn(parent2Statements);

        offspring1Stats = new ArrayList<>(parent1Statements);
        offspring2Stats = new ArrayList<>(parent2Statements);
        when(offspring1.getStatements()).thenReturn(offspring1Stats);
        when(offspring2.getStatements()).thenReturn(offspring2Stats);


        when(random.nextInt(anyInt(), anyInt())).thenReturn(2);
        crossover = new SingleCrossover(random);
    }

    @Test
    public void testCrossoverOperation() {
        Pair<StatementChromosome> result = crossover.apply(parent1, parent2);

        assertSame(offspring1, result.getFst());
        assertSame(offspring2, result.getSnd());

        assertArrayEquals(new Statement[]{s1, s2, s3, s4}, offspring1Stats.toArray());

        assertArrayEquals(new Statement[]{w, x, y, z}, offspring2Stats.toArray());

        verify(offspring1).call();
        verify(offspring2).call();
    }

    @Test
    public void testEdgeCaseCrossoverAtStart() {
        when(random.nextInt(1, 4)).thenReturn(1);
        crossover.apply(parent1, parent2);

        assertArrayEquals(new Statement[]{s1, s2, s3, s4}, offspring1Stats.toArray());
        assertArrayEquals(new Statement[]{w, x, y, z}, offspring2Stats.toArray());
    }

    @Test
    public void testEdgeCaseCrossoverAtEnd() {
        when(random.nextInt(1, 4)).thenReturn(3);
        crossover.apply(parent1, parent2);

        assertArrayEquals(new Statement[]{s1, s2, s3, s4}, offspring1Stats.toArray());
        assertArrayEquals(new Statement[]{w, x, y, z}, offspring2Stats.toArray());
    }
}
