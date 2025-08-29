package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.BranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.Mutation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StatementChromosome extends Chromosome<StatementChromosome> {

    private final Mutation<StatementChromosome> mutation;
    private final Crossover<StatementChromosome> crossover;
    private final List<Statement> statements;
    private final Set<IBranch> branchesToCover;
    private Map<Integer, Double> branchDistances;
    private int rank;
    private int density;
    private double fitness;

    public StatementChromosome(List<Statement> statements, Mutation<StatementChromosome> mutation, Crossover<StatementChromosome> crossover, Set<IBranch> branchesToCover) {
        this.mutation = mutation;
        this.crossover = crossover;
        this.statements = statements;
        this.branchesToCover = branchesToCover;
    }

    @Override
    public StatementChromosome copy() {
        return new StatementChromosome(statements, mutation, crossover,branchesToCover);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof StatementChromosome)) return false;
        return statements.equals(((StatementChromosome) other).statements);
    }

    @Override
    public int hashCode() {
        return statements.hashCode();
    }

    @Override
    public Map<Integer, Double> call() throws RuntimeException {
        BranchTracer.getInstance().clear();
        final BranchTracer tracer = BranchTracer.getInstance();

        for (Statement statement : statements) {
            statement.run();
        }

        Map<Integer, Double> branchDistances = new HashMap<>();
        for (IBranch branch : branchesToCover) {
            int branchId = branch.getId();
            final double distance = tracer.getDistances().getOrDefault(branchId, Double.MAX_VALUE);
            branchDistances.put(branchId, distance);
        }
        return branchDistances;
    }

    @Override
    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public StatementChromosome self() {
        return this;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Map<Integer, Double> getBranchDistances() {
        return branchDistances;
    }

    public void setBranchDistances(Map<Integer, Double> branchDistances) {
        this.branchDistances = branchDistances;
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }

    public int getSize(){
        return statements.size();
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
