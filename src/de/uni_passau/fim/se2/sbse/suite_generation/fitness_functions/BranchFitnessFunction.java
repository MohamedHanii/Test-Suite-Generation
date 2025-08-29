package de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.StatementChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;

import java.util.Map;
import java.util.Set;

public class BranchFitnessFunction implements FitnessFunction<StatementChromosome> {

    private final Set<IBranch> branchesToCover;

    public BranchFitnessFunction(Set<IBranch> branchesToCover) {
        this.branchesToCover = branchesToCover;
    }

    @Override
    public double applyAsDouble(StatementChromosome statements) throws NullPointerException {
        final Map<Integer, Double> branchDistances;
        try {
            branchDistances = statements.call();
        } catch (RuntimeException e) {
            return Double.MAX_VALUE;
        }

        double totalDistance = 0.0;
        for (IBranch branch : branchesToCover) {
            final int branchId = branch.getId();
            final double distance = branchDistances.getOrDefault(branchId, Double.MAX_VALUE);
            totalDistance += distance;
        }

        return totalDistance;
    }

    @Override
    public boolean isMinimizing() {
        return true;
    }
}
