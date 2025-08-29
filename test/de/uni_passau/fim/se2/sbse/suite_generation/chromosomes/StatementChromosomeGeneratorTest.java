package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators.ConstructorStatementGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators.FieldAssignmentStatementGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators.MethodStatementGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.ConstructorStatement;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.FieldAssignmentStatement;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.MethodStatement;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.Mutation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StatementChromosomeGeneratorTest {


    @Mock
    private Random random;

    @Mock
    private Mutation<StatementChromosome> mutation;

    @Mock
    private Crossover<StatementChromosome> crossover;

    @Mock
    private ConstructorStatementGenerator constructorGenerator;

    @Mock
    private MethodStatementGenerator methodGenerator;

    @Mock
    private FieldAssignmentStatementGenerator fieldAssignmentGenerator;

    @Mock
    private ConstructorStatement constructorStatement;

    @Mock
    private MethodStatement methodStatement;

    @Mock
    private FieldAssignmentStatement fieldAssignmentStatement;

    @Mock
    private IBranch branch;

    private StatementChromosomeGenerator generator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Set<IBranch> branchesToCover = new HashSet<>(Collections.singletonList(branch));

        generator = new StatementChromosomeGenerator(
                random,
                mutation,
                crossover,
                branchesToCover,
                constructorGenerator,
                methodGenerator,
                fieldAssignmentGenerator
        );

    }

    @Test
    void testGet_GeneratesStatementChromosomeWithConstructorAndOtherStatements() {
        when(constructorGenerator.generate()).thenReturn(constructorStatement);
        when(random.nextDouble()).thenReturn(0.7, 0.9, 0.8); // Simulate random outcomes
        when(methodGenerator.generate()).thenReturn((MethodStatement) methodStatement);
        when(fieldAssignmentGenerator.generate()).thenReturn((FieldAssignmentStatement) fieldAssignmentStatement);

        doNothing().when(constructorStatement).run();
        doNothing().when(methodStatement).run();
        doNothing().when(fieldAssignmentStatement).run();

        StatementChromosome chromosome = generator.get();

        assertNotNull(chromosome);
        assertEquals(50, chromosome.getStatements().size());
        assertTrue(chromosome.getStatements().contains(constructorStatement));
        verify(constructorGenerator).generate();
        verify(methodGenerator, atLeastOnce()).generate();
        verify(fieldAssignmentGenerator, atLeastOnce()).generate();
    }

    @Test
    void testGet_HandlesExceptionsDuringStatementRun() {
        when(constructorGenerator.generate()).thenReturn(constructorStatement);
        when(random.nextDouble()).thenReturn(0.7, 0.9, 0.8);
        when(methodGenerator.generate()).thenReturn(methodStatement);
        when(fieldAssignmentGenerator.generate()).thenReturn(fieldAssignmentStatement);

        doThrow(new RuntimeException("Error during run")).when(methodStatement).run();

        StatementChromosome chromosome = generator.get();

        assertNotNull(chromosome);
        assertEquals(50, chromosome.getStatements().size());
        verify(methodStatement, atLeastOnce()).run();
    }

}
