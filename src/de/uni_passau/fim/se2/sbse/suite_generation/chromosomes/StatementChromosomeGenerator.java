package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators.ConstructorStatementGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators.FieldAssignmentStatementGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.generators.MethodStatementGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.Mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class StatementChromosomeGenerator implements ChromosomeGenerator<StatementChromosome> {

    private static final int MAX_STATEMENTS = 50;

    private final Random random;
    private final Mutation<StatementChromosome> mutation;
    private final Crossover<StatementChromosome> crossover;
    private final Set<IBranch> branchesToCover;
    private final ConstructorStatementGenerator constructorGenerator;
    private final MethodStatementGenerator methodGenerator;
    private final FieldAssignmentStatementGenerator fieldAssignmentGenerator;

    public StatementChromosomeGenerator(Random random, Mutation<StatementChromosome> mutation,
                                        Crossover<StatementChromosome> crossover, Class<?> cutClass,
                                        Set<IBranch> branchesToCover) {
        this(random, mutation, crossover, branchesToCover,
                new ConstructorStatementGenerator(cutClass, random),
                new MethodStatementGenerator(cutClass, random),
                new FieldAssignmentStatementGenerator(cutClass, random));
    }

    public StatementChromosomeGenerator(Random random, Mutation<StatementChromosome> mutation,
                                        Crossover<StatementChromosome> crossover, Set<IBranch> branchesToCover,
                                        ConstructorStatementGenerator constructorGenerator,
                                        MethodStatementGenerator methodGenerator,
                                        FieldAssignmentStatementGenerator fieldAssignmentGenerator) {
        this.random = random;
        this.mutation = mutation;
        this.crossover = crossover;
        this.branchesToCover = branchesToCover;
        this.constructorGenerator = constructorGenerator;
        this.methodGenerator = methodGenerator;
        this.fieldAssignmentGenerator = fieldAssignmentGenerator;
    }

    @Override
    public StatementChromosome get() {
        List<Statement> statements = new ArrayList<>();
        statements.add(constructorGenerator.generate());

        while (statements.size() < MAX_STATEMENTS) {
            Statement newStatement;

            if (random.nextDouble() < 0.8) {
                newStatement = methodGenerator.generate();
            } else {
                newStatement = fieldAssignmentGenerator.generate();
            }

            if (newStatement != null) {
                try {
                    newStatement.run();
                    statements.add(newStatement);
                } catch (Exception e) {
                    System.out.println("Error running statement: " + e.getMessage());
                }
            }
        }

        return new StatementChromosome(statements, mutation, crossover, branchesToCover);
    }


}