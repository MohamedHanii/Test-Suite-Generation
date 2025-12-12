# Test Suite Generation

A Java-based test suite generation framework that implements search-based software engineering (SBSE) techniques to automatically generate test cases for Java classes.

## Overview

This project implements various search algorithms to automatically generate test suites that maximize branch coverage. It uses bytecode instrumentation to trace branch execution and fitness functions to guide the search process towards better test cases.

## Features

- **Multiple Search Algorithms**: 
  - Random Search
  - MOSA (Many-Objective Sorting Algorithm)
  - Genetic Algorithm framework
- **Automatic Test Generation**: Generates constructor calls, method invocations, and field assignments
- **Branch Coverage Optimization**: Uses fitness functions to maximize branch coverage
- **Bytecode Instrumentation**: Automatically instruments classes to trace branch execution
- **Extensible Architecture**: Modular design allowing easy addition of new algorithms and fitness functions

## Project Structure

```
src/de/uni_passau/fim/se2/sbse/suite_generation/
├── algorithms/          # Search algorithm implementations
├── chromosomes/         # Test case representation and generation
├── crossover/          # Genetic algorithm crossover operators
├── examples/           # Example classes for testing
├── fitness_functions/  # Fitness evaluation functions
├── instrumentation/    # Bytecode instrumentation for branch tracing
├── mutation/          # Genetic algorithm mutation operators
├── selection/         # Parent selection strategies
├── stopping_conditions/ # Algorithm termination conditions
└── utils/             # Utility classes and helpers
```

## Requirements

- Java 23 or higher
- Maven 3.6+
- ASM 9.7 (for bytecode manipulation)

## Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd Test-Suite-Generation
```

2. Build the project:
```bash
mvn clean compile
```

3. Run tests:
```bash
mvn test
```

## Usage

### Command Line Interface

The main application provides a command-line interface for running test suite generation:

```bash
java -javaagent:target/Test-Suite-Generation.jar -cp target/classes de.uni_passau.fim.se2.sbse.suite_generation.Main [options] <algorithms>
```

### Options

- `-c, --class`: The name of the class under test (required)
- `-p, --package`: Package containing the class under test (default: examples package)
- `-f, --max-evaluations`: Maximum fitness evaluations (default: 500)
- `-z, --size`: Population size for genetic algorithms (default: 10, must be even)
- `-r, --repetitions`: Number of search repetitions (default: 10)
- `-s, --seed`: Fixed random seed for reproducibility

### Algorithms

- `RANDOM_SEARCH`: Random search algorithm
- `MOSA`: Many-Objective Sorting Algorithm

### Example Usage

Generate test suite for SimpleExample class using MOSA:

```bash
java -javaagent:target/Test-Suite-Generation.jar -cp target/classes de.uni_passau.fim.se2.sbse.suite_generation.Main -c SimpleExample MOSA
```

Compare multiple algorithms:

```bash
java -javaagent:target/Test-Suite-Generation.jar -cp target/classes de.uni_passau.fim.se2.sbse.suite_generation.Main -c SimpleExample -f 1000 RANDOM_SEARCH MOSA
```

## Example Classes

The project includes several example classes for testing:

- **SimpleExample**: Basic class with simple conditional logic
- **Feature**: More complex class with multiple methods and branches
- **Stack**: Stack implementation with various edge cases
- **DeepBranches**: Class with deeply nested conditional statements

## How It Works

1. **Instrumentation**: The Java agent instruments the target class to trace branch execution
2. **Test Generation**: Search algorithms generate test cases using various strategies
3. **Fitness Evaluation**: Each test case is evaluated based on branch coverage
4. **Search Optimization**: Algorithms use fitness scores to guide the search towards better solutions
5. **Result Generation**: Final test suite with optimal branch coverage is produced

## Architecture

### Search Algorithms
- **SearchAlgorithm**: Base interface for all search algorithms
- **RandomSearch**: Simple random test generation
- **MOSA**: Multi-objective optimization for test generation
- **GeneticAlgorithm**: Framework for genetic algorithm implementations

### Chromosomes
- **Chromosome**: Base representation for test cases
- **StatementChromosome**: Test case as a sequence of statements
- **Statement Types**: Constructor calls, method invocations, field assignments

### Fitness Functions
- **FitnessFunction**: Base interface for fitness evaluation
- **BranchFitnessFunction**: Evaluates test cases based on branch coverage

### Instrumentation
- **InstrumentingAgent**: Java agent for bytecode instrumentation
- **BranchTracer**: Tracks branch execution during test runs
- **BranchDistanceTransformer**: Transforms bytecode to measure branch distances

## Development

### Adding New Algorithms

1. Implement the `SearchAlgorithm` interface
2. Add the algorithm type to `SearchAlgorithmType` enum
3. Update the `AlgorithmBuilder` to support the new algorithm

### Adding New Fitness Functions

1. Extend the `FitnessFunction` class
2. Implement the fitness evaluation logic
3. Register the function in the appropriate algorithm

### Running with Custom Classes

1. Place your class in the appropriate package
2. Use the `-c` and `-p` options to specify your class
3. Ensure your class can be instrumented (public methods, accessible constructors)

## Testing

The project includes comprehensive test coverage:

```bash
# Run all tests
mvn test

# Generate coverage report
mvn jacoco:report

# Run mutation testing
mvn pitest:mutationCoverage
```

## Results

Test results are stored in the `example_results/` directory, showing:
- Branch coverage achieved
- Number of test cases generated
- Fitness evaluation statistics
- Algorithm performance comparisons

## License

This project is part of the Search-Based Software Engineering course at Universität Passau.

## Acknowledgments

- Built with ASM for bytecode manipulation
- Uses JUnit 5 for testing framework
- Implements MOSA algorithm for multi-objective optimization
- Developed as part of SBSE research and education 
