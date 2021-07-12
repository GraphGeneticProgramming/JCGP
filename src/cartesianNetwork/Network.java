package cartesianNetwork;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import Calculators.FitnessCalculator;
import helperClasses.Digit;

/**
 * This class is responsible for the (mu + lambda)-Algorithm of PCGP
 * 
 * @author Björn Piepenbrink
 *
 */
public class Network {

	// number of Nodes in the Network
	int nodeAmount;
	// number of Inputs
	int inputAmount;
	// number of Outputs
	int outputAmount;
	// inpuData
	Digit[] inputdata;
	// mutationrate
	double mutationrate;
	// generationLimit
	int generationLimit;
	// FuunctionSet
	int functionSet;
	// LevelsBack
	int levelsBack;
	// für (mu+lambda)-Strategie
	int mu, lambda;
	// indicates if solution is found
	boolean solutionFound;
	// indicates if Network should print to Console
	boolean print;

	/**
	 * Constructs a CGP Network with the given values
	 * 
	 * @param mutationrate
	 *            how much an Individual can be changed during one mutation
	 * @param nodeAmount
	 *            the Amount of Nodes in the net
	 * @param generationLimit
	 *            the maximum number of allowed generations
	 * @param inputdata
	 *            the InputData used for Evaluation
	 * @param outputAmount
	 *            the number of Output the Network can possess (currently only 1
	 *            or 4 (if boolean))
	 * @param functionSet
	 *            the FunctionSet that the nodes can use (1-4 and 5 for boolean)
	 * @param print
	 *            if the net is allowed to print to console during Calculation
	 * @param levelsBack
	 *            the levels-Back value the Individual should obey to
	 * @param mu
	 *            defines the (mu + lambda) - Strategy
	 * @param lambda
	 *            defines the (mu + lambda) - Strategy
	 */
	public Network(double mutationrate, int nodeAmount, int generationLimit, Digit[] inputdata, int outputAmount,
			int functionSet, boolean print, int levelsBack, int mu, int lambda) {
		this.mutationrate = mutationrate;
		this.nodeAmount = nodeAmount;
		this.inputdata = inputdata;
		this.inputAmount = inputdata[0].toIntArray().length;
		this.generationLimit = generationLimit;
		this.outputAmount = outputAmount;
		this.functionSet = functionSet;
		this.levelsBack = levelsBack;
		this.print = print;
		this.mu = mu;
		this.lambda = lambda;
		solutionFound = false;

	}

	/**
	 * runs the EvolutionAlgorithm of CGP for the constructed Net
	 * 
	 * @return the best Individual
	 * @throws Exception if a unexpected problem occurs
	 */
	public ArrayList<Individual> evolutionAlgorithm() throws Exception {
		if (print)
			System.out.println("GENERATION: 0");
		ArrayList<Individual> individuals = getXRandomIndiv(mu + lambda);
		ArrayList<Individual> parents = getFittestXIndivs(mu, individuals);
		int gens = 1;
		while (!solutionFound && gens <= generationLimit) {
			if (print)
				System.out.println("GENERATION: " + gens);
			individuals = new ArrayList<>();
			// add parents to generation
			for (Individual parent : parents) {
				individuals.add(parent);
			}

			// generate lamda offsprings
			for (int i = 0; i < lambda; i++) {
				individuals.add(mutate(parents));
			}

			parents = getFittestXIndivs(mu, individuals);
			gens++;
		}

		return parents;
	}

	/**
	 * mutates an Individual
	 * @param parents th paretn that should be mutated
	 * @return the mutated offspring
	 * @throws Exception
	 */
	private Individual mutate(ArrayList<Individual> parents) throws Exception {
		// choose random parent to mutate
		int randomParent = ThreadLocalRandom.current().nextInt(0, parents.size());
		Individual mutated = parents.get(randomParent).mutation(mutationrate);
		return mutated;
	}

	/**
	 * generates x Random Individual for the Start of the Algorithm
	 * 
	 * @return
	 * @throws Exception
	 */
	private ArrayList<Individual> getXRandomIndiv(int x) throws Exception {
		ArrayList<Individual> individuals = new ArrayList<>();
		for (int i = 0; i < x; i++) {
			individuals.add(new Individual(nodeAmount, inputAmount, outputAmount, functionSet, levelsBack));
		}
		return individuals;
	}

	/**
	 * Calculates Fitness for all the given
	 * 
	 * @param indivs
	 * @return
	 * @throws Exception
	 */
	private ArrayList<Individual> getFittestXIndivs(int nr, ArrayList<Individual> indivs) throws Exception {
		if (print)
			System.out.println("calculating Fittest individual out of " + indivs.size());
		indivs = calculateFitness(indivs);
		return getFittestIndivs(nr, indivs);
	}

	/**
	 * 
	 * @param nr the number of Individual that will be returned
	 * @param indivs The list of Individual that the best ones will be taken from
	 * @return the best nr-Individuals
	 * @throws Exception
	 */
	private ArrayList<Individual> getFittestIndivs(int nr, ArrayList<Individual> indivs) throws Exception {
		ArrayList<Individual> fittestInd = new ArrayList<>();
		for (int i = 0; i < nr; i++) {
			fittestInd.add(getFittestAndDelete(indivs));
		}
		if (print) {
			System.out.println("FITTEST INDIVIDUAL:");
			for (Individual i : fittestInd) {
				if (i.getFitness() == 0) {
					solutionFound = true;
				}
				System.out.println("    " + i.getFitness());
			}
		}
		return fittestInd;
	}

	/**
	 * select the best Individual of the given lisst and deletes it from the list
	 * @param indivs The list of Individuals
	 * @return
	 * @throws Exception
	 */
	private Individual getFittestAndDelete(ArrayList<Individual> indivs) throws Exception {
		// since parents are on the beginning og the list
		Individual fittestInd = indivs.get(0);
		int fittestFitness = indivs.get(0).getFitness();
		int pos = 0;
		for (int i = 1; i < indivs.size(); i++) {
			int currentfitness = indivs.get(i).getFitness();
			// smaller fitness means better
			if (currentfitness <= fittestFitness) {
				fittestFitness = currentfitness;
				fittestInd = indivs.get(i);
				pos = i;
			}
		}
		indivs.remove(pos);
		return fittestInd;
	}

	/**
	 * Calcualtes Fitness for the given Individuals
	 * uses Threading to speed up evaluation
	 * @param indiv the individuals
	 * @return The Individuals with calcualted Fitness-Values
	 * @throws Exception
	 */
	private ArrayList<Individual> calculateFitness(ArrayList<Individual> indiv) throws Exception {
		ExecutorService executor = Executors.newCachedThreadPool();
		ArrayList<Future<Integer>> results = new ArrayList<>();
		for (int i = 0; i < indiv.size(); i++) {
			if (!(indiv.get(i).hasFitness())) {
				results.add(executor.submit(new FitnessCalculator(indiv.get(i), inputdata, print)));
			} else {
				results.add(null);
			}
		}
		for (int i = 0; i < indiv.size(); i++) {
			if (!(indiv.get(i).hasFitness())) {
				indiv.get(i).setFitness(results.get(i).get());
			}
		}
		executor.shutdown();
		return indiv;
	}
}
