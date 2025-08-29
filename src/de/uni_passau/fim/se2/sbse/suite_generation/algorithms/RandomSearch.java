package de.uni_passau.fim.se2.sbse.suite_generation.algorithms;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.Chromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.ChromosomeGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.StatementChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.BranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions.StoppingCondition;

import java.util.*;
import java.util.stream.Collectors;

public class RandomSearch<C extends Chromosome<C>> implements GeneticAlgorithm<C> {

    private final StoppingCondition stoppingCondition;
    private final ChromosomeGenerator<C> chromosomeGenerator;
    private final FitnessFunction<C> fitnessFunction;
    private final Map<Integer, C> archive;
    private final Map<Integer, Double> bestDistances;
    private final Set<IBranch> branchesToCover;

    public RandomSearch(StoppingCondition stoppingCondition, ChromosomeGenerator<C> chromosomeGenerator, FitnessFunction<C> fitnessFunction, Set<IBranch> branchesToCover) {
        this.stoppingCondition = stoppingCondition;
        this.chromosomeGenerator = chromosomeGenerator;
        this.fitnessFunction = fitnessFunction;
        this.branchesToCover = branchesToCover;

        this.archive = new HashMap<>();
        this.bestDistances = new HashMap<>();
        for (IBranch branch : branchesToCover) {
            int branchId = branch.getId();
            archive.put(branchId, null);
            bestDistances.put(branchId, Double.MAX_VALUE);
        }
    }

    @Override
    public List<C> findSolution() {
        notifySearchStarted();
        getStoppingCondition().notifySearchStarted();
        while(searchCanContinue()){
            C candidate = chromosomeGenerator.get();

            fitnessFunction.applyAsDouble(candidate);
            stoppingCondition.notifyFitnessEvaluation();

            updateArchive(candidate);
        }

        return buildTestSuite();
    }

    private void updateArchive(C candidate) {
        Map<Integer, Double> currentDistances = BranchTracer.getInstance().getDistances();

        for (IBranch branch : branchesToCover) {
            int branchId = branch.getId();
            double currentDistance = currentDistances.getOrDefault(branchId, Double.MAX_VALUE);
            double bestDistance = bestDistances.get(branchId);

            if (currentDistance < bestDistance) {
                bestDistances.put(branchId, currentDistance);
                archive.put(branchId, candidate.copy());
            }
        }
    }

    public List<C> buildTestSuite() {
        return archive.values().stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public StoppingCondition getStoppingCondition() {
        return stoppingCondition;
    }

}