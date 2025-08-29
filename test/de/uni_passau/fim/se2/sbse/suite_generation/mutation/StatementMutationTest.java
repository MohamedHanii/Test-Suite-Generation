package de.uni_passau.fim.se2.sbse.suite_generation.mutation;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.StatementChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StatementMutationTest {

    @Mock
    private Statement s1, s2, s3;

    @Test
    public void testDeleteStatementReducesSize() {
        StatementChromosome chromosome = mock(StatementChromosome.class);
        List<Statement> statements = new ArrayList<>(Arrays.asList(s1, s2, s3));
        when(chromosome.getStatements()).thenReturn(statements);

        StatementMutation mutation = new StatementMutation(new Random());
        mutation.deleteStatement(chromosome);

        assertEquals(2, statements.size());
    }

    @Test
    public void testSwapChangesOrderForTwoElements() {
        StatementChromosome chromosome = mock(StatementChromosome.class);
        List<Statement> statements = new ArrayList<>(Arrays.asList(s1, s2));
        when(chromosome.getStatements()).thenReturn(statements);


        StatementMutation mutation = new StatementMutation(new FixedIndexRandom(1, 0));

        mutation.swapStatements(chromosome);

        assertArrayEquals(new Statement[]{s2, s1}, statements.toArray());
    }

    @Test
    public void testApplyRetriesOnFailure() {
        StatementChromosome original = mock(StatementChromosome.class);
        StatementChromosome badMutant = mock(StatementChromosome.class);
        StatementChromosome goodMutant = mock(StatementChromosome.class);

        when(original.copy()).thenReturn(badMutant, goodMutant);
        when(badMutant.call()).thenThrow(new RuntimeException());
        when(goodMutant.call()).thenReturn(null);

        Random random = mock(Random.class);
        when(random.nextInt(2)).thenReturn(0);

        StatementMutation mutation = new StatementMutation(random);
        StatementChromosome result = mutation.apply(original);

        assertSame(goodMutant, result);
        verify(badMutant).call();
        verify(goodMutant).call();
    }

    private static class FixedIndexRandom extends Random {
        private final int[] values;
        private int index = 0;

        FixedIndexRandom(int... values) {
            this.values = values;
        }

        @Override
        public int nextInt(int bound) {
            return values[index++ % values.length];
        }
    }
}
