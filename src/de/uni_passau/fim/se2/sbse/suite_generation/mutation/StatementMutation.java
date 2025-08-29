package de.uni_passau.fim.se2.sbse.suite_generation.mutation;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.StatementChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;

import java.util.List;
import java.util.Random;

public class StatementMutation implements Mutation<StatementChromosome> {

    private final Random random;

    public StatementMutation(Random random) {
        this.random = random;
    }

    @Override
    public StatementChromosome apply(StatementChromosome statements) {
        StatementChromosome mutated;

        while (true) {
            mutated = statements.copy();
            int mutationType = random.nextInt(2);
            switch (mutationType) {
                case 0:
                    deleteStatement(mutated);
                    break;
                case 1:
                    swapStatements(mutated);
                    break;
            }
            try {
                mutated.call();
                break;
            } catch (Exception _) {
            }
        }

        return mutated;
    }

    public void deleteStatement(StatementChromosome chromosome) {
        Random random = new Random();
        List<Statement> statements = chromosome.getStatements();

        if (!statements.isEmpty()) {
            int index = random.nextInt(statements.size());
            statements.remove(index);
        }
    }

    public void swapStatements(StatementChromosome chromosome) {
        Random random = new Random();
        List<Statement> statements = chromosome.getStatements();

        if (statements.size() > 1) {
            int index1 = random.nextInt(statements.size() - 1) + 1;
            int index2 = random.nextInt(statements.size() - 1) + 1;

            while (index1 == index2) {
                index2 = random.nextInt(statements.size());
            }

            Statement temp = statements.get(index1);
            statements.set(index1, statements.get(index2));
            statements.set(index2, temp);
        }
    }
}
