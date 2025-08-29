package de.uni_passau.fim.se2.sbse.suite_generation.algorithms;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.Chromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.ChromosomeGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.StatementChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.selection.Selection;
import de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions.StoppingCondition;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Pair;

import java.util.*;

public class MOSA<C extends Chromosome<C>> implements GeneticAlgorithm<C> {

    private final StoppingCondition stoppingCondition;
    private final ChromosomeGenerator<C> chromosomeGenerator;
    private final int populationSize;
    private final Set<IBranch> branches;
    private final Selection<C> selection;
    private static final double P_XOVER = 0.8;
    private final Random random;
    private final FitnessFunction<C> fitnessFunction;

    public MOSA(StoppingCondition stoppingCondition, ChromosomeGenerator<C> chromosomeGenerator, int populationSize, Set<IBranch> branches, Selection<C> selection, Random random, FitnessFunction<C> fitnessFunction) {
        this.stoppingCondition = stoppingCondition;
        this.chromosomeGenerator = chromosomeGenerator;
        this.populationSize = populationSize;
        this.branches = branches;
        this.selection = selection;
        this.random = random;
        this.fitnessFunction = fitnessFunction;
    }

    @Override
    public List<C> findSolution() {
        notifySearchStarted();
        Map<Integer, C> archive = new HashMap<>();
        List<C> population = getInitialPopulation(archive);

        while(searchCanContinue()){
            List<C> offspringPopulation = generateOffspring(population);
            List<C> combined = new ArrayList<>(population);
            combined.addAll(offspringPopulation);

            updateArchive(archive,offspringPopulation);

            List<List<C>> fronts = preferenceSorting(combined,archive);
            population = new ArrayList<>();

            for (List<C> front : fronts) {
                subvectorDominanceAssignment(front,archive);

                for (C individual : front) {
                    population.add(individual);
                    if (population.size() == populationSize) {
                        break;
                    }
                }
                if (population.size() == populationSize) {
                    break;
                }
            }
        }

        return new ArrayList<>(archive.values());
    }

    public List<C> getInitialPopulation(Map<Integer, C> archive) {
        List<C> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            C candidate = chromosomeGenerator.get();
            ((StatementChromosome)candidate).setFitness(fitnessFunction.applyAsDouble(candidate));
            population.add(candidate);
            ((StatementChromosome)candidate).setBranchDistances(candidate.call());
        }
        notifyFitnessEvaluation(populationSize);
        updateArchive(archive, population);

        List<List<C>> fronts = preferenceSorting(population,archive);

        for (List<C> front : fronts) {
            subvectorDominanceAssignment(front,archive);
        }

        return population;
    }

    public void updateArchive(Map<Integer, C> archive, List<C> population) {
        for (C testCase : population) {
            for (IBranch branch : branches) {
                int branchId = branch.getId();
                if(archive.containsKey(branchId)) {
                    continue;
                }

                Map<Integer, Double> branchDistances = testCase.call();
                double distance = branchDistances.get(branchId);

                if (distance  == 0.0){
                    archive.putIfAbsent(branchId, testCase.copy());
                }
            }
        }
    }

    public List<List<C>> preferenceSorting(List<C> population, Map<Integer, C> archive) {
        List<List<C>> fronts = new ArrayList<>();
        List<C> f0 = new ArrayList<>();

        for (IBranch branch : branches) {
            int branchId = branch.getId();
            if(!archive.containsKey(branchId)) {
                C best = findBestForGoal(population, branch.getId());
                if (best != null && !f0.contains(best)) {
                    f0.add(best);
                }
            }
        }

        f0.forEach(t -> ((StatementChromosome)t).setRank(0));
        fronts.add(f0);


        List<C> remaining = new ArrayList<>();
        for (C test : population) {
            if (!f0.contains(test)) {
                remaining.add(test);
            }
        }

        if (!remaining.isEmpty()) {
            List<List<Integer>> remainingFronts = fastNonDominatedSort(remaining, archive);

            for (int i = 0; i < remainingFronts.size(); i++) {
                List<C> front = new ArrayList<>();
                for (int index : remainingFronts.get(i)) {
                    front.add(remaining.get(index));
                }

                for (C statements : front) {
                    ((StatementChromosome) statements).setRank(i + 1);
                }
                fronts.add(front);
            }
        }
        return fronts;
    }

    public List<List<Integer>> fastNonDominatedSort(List<C> solutions, Map<Integer, C> archive) {
        List<List<Integer>> front = new ArrayList<>();
        front.add(new ArrayList<>());

        int numSolutions = solutions.size();

        List<List<Integer>> S = new ArrayList<>(numSolutions);
        int[] n = new int[numSolutions];

        for (int p = 0; p < numSolutions; p++) {
            S.add(new ArrayList<>());
            n[p] = 0;
        }

        for (int p = 0; p < numSolutions; p++) {
            S.set(p, new ArrayList<>());
            n[p] = 0;
            for (int q = 0; q < numSolutions; q++) {
                if (dominates(solutions.get(p), solutions.get(q), archive)) {
                    S.get(p).add(q);
                } else if (dominates(solutions.get(q), solutions.get(p), archive)) {
                    n[p]++;
                }
            }

            if (n[p] == 0) {
                front.getFirst().add(p);
                ((StatementChromosome) solutions.get(p)).setRank(0);
            }
        }

        int i = 0;
        while (!front.get(i).isEmpty()) {
            List<Integer> Q = new ArrayList<>();
            for (int p : front.get(i)) {
                for (int q : S.get(p)) {
                    n[q]--;
                    if (n[q] == 0) {
                        Q.add(q);
                        ((StatementChromosome) solutions.get(q)).setRank(i + 1);
                    }
                }
            }
            i++;
            front.add(Q);
        }

        front.removeLast();
        return front;
    }

    private C findBestForGoal(List<C> population, Integer goal) {
        // If the population is empty, return null
        if (population.isEmpty()) {
            return null;
        }

        // Initialize a variable to track the best individual and its goal value
        C bestIndividual = null;
        double bestGoalValue = Double.MAX_VALUE;  // Start with a very large value

        // Iterate through the population manually
        for (C individual : population) {
            double goalValue = individual.call().get(goal);

            // Check if the current individual is better (smaller goal value)
            if (goalValue < bestGoalValue) {
                bestGoalValue = goalValue;
                bestIndividual = individual;
            }
        }

        // Return the best individual found, or null if no valid individual
        return bestIndividual;
    }

    boolean dominates(C solution1, C solution2, Map<Integer, C> archive) {
        for (IBranch branch : branches) {
            if (archive.containsKey(branch.getId())) {
                continue;
            }

            double distance1 = ((StatementChromosome)solution1).getBranchDistances().getOrDefault(branch.getId(), Double.MAX_VALUE);
            double distance2 = ((StatementChromosome)solution2).getBranchDistances().getOrDefault(branch.getId(), Double.MAX_VALUE);

            if (distance1 > distance2) {
                return false;
            }
        }

        for (IBranch branch : branches) {
            if (archive.containsKey(branch.getId())) {
                continue;
            }

            double distance1 = ((StatementChromosome)solution1).getBranchDistances().getOrDefault(branch.getId(), Double.MAX_VALUE);
            double distance2 = ((StatementChromosome)solution2).getBranchDistances().getOrDefault(branch.getId(), Double.MAX_VALUE);

            if (distance1 < distance2) {
                return true;
            }
        }

        return false;
    }

    private int svd(C solution1, C solution2) {
        int count = 0;
        for (IBranch branch : branches) {
            int branchId = branch.getId();
            double distance1 = ((StatementChromosome) solution1).getBranchDistances().get(branchId);
            double distance2 = ((StatementChromosome) solution2).getBranchDistances().get(branchId);
            if (distance1 < distance2) {
                count++;
            }
        }
        return count;
    }

    public void subvectorDominanceAssignment(List<C> solutions, Map<Integer, C> archive) {
        int n = solutions.size();

        for (int i = 0; i < n; i++) {
            C current = solutions.get(i);
            ((StatementChromosome) current).setDensity(0);
            int maxCount = 0;

            for (int j = 0; j < n; j++) {
                if (i == j) continue;

                C other = solutions.get(j);
                int v = svd(current, other);

                if (v > maxCount) {
                    maxCount = v;
                }
            }
            ((StatementChromosome) current).setDensity(maxCount);
        }

        solutions.sort(Comparator.comparingDouble(c -> ((StatementChromosome) c).getDensity()));
    }

    public List<C> generateOffspring(List<C> population) {
        List<C> offspringPopulation = new ArrayList<>();

        while (offspringPopulation.size() < population.size()) {
            C parent1 = selection.apply(population);
            C parent2 =  selection.apply(population);

            C offspring1;
            C offspring2;

            if (random.nextDouble() < P_XOVER) {
                Pair<C> offspring = parent1.crossover(parent2);
                offspring1 = offspring.getFst();
                offspring2 = offspring.getSnd();
            } else {
                offspring1 = parent1.copy();
                offspring2 = parent2.copy();
            }

            //calculate fitness function
            offspring1 = offspring1.mutate();
            offspring2 = offspring2.mutate();

            ((StatementChromosome)offspring1).setFitness(fitnessFunction.applyAsDouble(offspring1));
            ((StatementChromosome)offspring2).setFitness(fitnessFunction.applyAsDouble(offspring2));

            ((StatementChromosome)offspring1).setBranchDistances(offspring1.call());
            ((StatementChromosome)offspring2).setBranchDistances(offspring2.call());

            offspringPopulation.add(offspring1);
            offspringPopulation.add(offspring2);
        }

        notifyFitnessEvaluation(offspringPopulation.size());
        return offspringPopulation;
    }

    @Override
    public StoppingCondition getStoppingCondition() {
        return stoppingCondition;
    }
}
