package de.uni_passau.fim.se2.sbse.suite_generation.crossover;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.StatementChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Pair;

import java.util.Random;

public class SingleCrossover implements Crossover<StatementChromosome> {
    private final Random random;

    public SingleCrossover(Random random) {
        this.random = random;
    }

    @Override
    public Pair<StatementChromosome> apply(StatementChromosome parent1, StatementChromosome parent2) {
        StatementChromosome offspring1;
        StatementChromosome offspring2;

        while (true) {
            try {
                offspring1 = parent1.copy();
                offspring2 = parent2.copy();


                offspring1.getStatements().set(0, parent1.getStatements().getFirst());
                offspring2.getStatements().set(0, parent2.getStatements().getFirst());

                int crossoverPoint = random.nextInt(1, parent1.getSize());

                for (int i = crossoverPoint; i < parent1.size(); i++) {
                    offspring1.getStatements().set(i, parent2.getStatements().get(i));
                    offspring2.getStatements().set(i, parent1.getStatements().get(i));
                }

                offspring1.call();
                offspring2.call();

                return new Pair<>(offspring1, offspring2);

            } catch (Exception e) {
                continue;
            }
        }
    }

    @Override
    public Pair<StatementChromosome> apply(Pair<? extends StatementChromosome> parents) {
        return Crossover.super.apply(parents);
    }

}
