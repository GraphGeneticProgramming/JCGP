package a_main;

import java.util.ArrayList;

import OutPutCalculation.StatisticsCalculator;
import cartesianNetwork.Individual;
import cartesianNetwork.Network;
import dataIO.SaveAndLoadIndividuals;
import helperClasses.Digit;

/**
 * runs the CGP Net
 * 
 * @author Björn Piepenbrink
 *
 */
public class CGP_NetHandler {

	// functions
	int functionSet;

	// CGP
	double mutation_rate;
	int nodeAmount;
	int maxGenerations;
	int outputAmount;
	int levelsBack;
	int mu;
	int lambda;

	// InputData
	Digit[] digits;

	boolean printOutput;

	String path;
	
	/**
	 * 
	 * @param digits The InputData on which CGP should operate
	 * @param functionSet The Function Set that should be used
	 * @param mutation_rate The Mutation Rate for the Individuals
	 * @param nodeAmount The Amount of Nodes an Individual posesses
	 * @param maxGenerations The maximum Number of Generations an Individual should run through
	 * @param outputAmount The number of Outputs of the CGPnet
	 * @param printOutput if Output to the console is allowed
	 * @param levelsBack The value of the levels-Back
	 * @param mu mu for the (mu+lambda)-Strategy
	 * @param lambda Lambda for the (mu+lambda)-Strategy
	 * @param path The path where the best Individuals should be saved
	 */
	public CGP_NetHandler(Digit[] digits, int functionSet, double mutation_rate, int nodeAmount, int maxGenerations,
			int outputAmount, boolean printOutput, int levelsBack, int mu, int lambda, String path) {
		this.functionSet = functionSet;
		this.mutation_rate = mutation_rate;
		this.nodeAmount = nodeAmount;
		this.maxGenerations = maxGenerations;
		this.outputAmount = outputAmount;
		this.digits = digits;
		this.printOutput = printOutput;
		this.levelsBack = levelsBack;
		this.mu = mu;
		this.lambda = lambda;
		this.path = path;

		if (lambda <= mu) {
			throw new IllegalArgumentException("mu has to be smaller than lambda");
		}
	}

	/**
	 * runs CGP a number of times and returns the best Individual that was
	 * produced
	 * 
	 * @param outOf
	 *            number of CGP runs
	 * @return the best Individual of all outcomes of the runs
	 * @throws Exception if an unexpected problem occurs
	 */
	public Individual getBestIndividualOfMultiple(int outOf) throws Exception {
		ArrayList<Individual> bestIndividuals = getBestIndividuals(outOf);
		int bestFitness = bestIndividuals.get(0).getFitness();
		Individual bestIndividual = bestIndividuals.get(0);
		for (Individual indiv : bestIndividuals) {
			// lower Fitness means better Fitness
			if (bestFitness > indiv.getFitness()) {
				bestFitness = indiv.getFitness();
				bestIndividual = indiv;
			}
		}
		return bestIndividual;
	}

	/**
	 * runs the CGPnet a number of times an returns the best Individual for each
	 * run
	 * 
	 * @param outOf
	 *            the number of times CGP should run
	 * @return the best Individuals of the runs
	 * @throws Exception if an unexpected problem occurs
	 */
	public ArrayList<Individual> getBestIndividuals(int outOf) throws Exception {
		ArrayList<Individual> bestIndividuals = new ArrayList<>();
		for (int i = 0; i < outOf; i++) {
			ArrayList<Individual> bestIndividualsCurrent = getBestIndividual(i);
			for (Individual e : bestIndividualsCurrent) {
				bestIndividuals.add(e);
			}
		}

		String toPrint = "Fitness of the best Individuals:\n";
		ArrayList<Integer> fitness = new ArrayList<>();
		for (Individual ind : bestIndividuals) {
			fitness.add(ind.getFitness());

			// show used Nodes
			boolean[] uN = ind.getUsedNodes();
			int sum = 0;
			for (int i = 0; i < uN.length; i++) {
				if (uN[i]) {
					sum++;
				}
			}
			toPrint += ind.getFitness() + "     UsedNodes:" + sum + "\n";
		}

		toPrint += StatisticsCalculator.calculateOutput(fitness);

		if (printOutput) {
			System.out.println(toPrint);
		}
		String fileName = "fs_" + functionSet + "_mr_" + mutation_rate + "_nodes_" + nodeAmount + "_levelsBack_"
				+ levelsBack + "_Gens_" + maxGenerations + "_outputs_" + outputAmount;
		SaveAndLoadIndividuals.saveStatistics(toPrint, path, fileName);

		return bestIndividuals;
	}

	/**
	 * runs the CGPnet for the given values
	 * 
	 * @return the best Individuals for this run of CGP
	 * @throws Exception if an unexpected problem occurs
	 */
	public ArrayList<Individual> getBestIndividual() throws Exception {
		Network net = new Network(mutation_rate, nodeAmount, maxGenerations, digits, outputAmount, functionSet,
				printOutput, levelsBack, mu, lambda);
		ArrayList<Individual> bestIndivs = net.evolutionAlgorithm();

		SaveAndLoadIndividuals.saveIndividual(bestIndivs, path, "BestIndividual");

		return bestIndivs;
	}

	private ArrayList<Individual> getBestIndividual(int i) throws Exception {
		Network net = new Network(mutation_rate, nodeAmount, maxGenerations, digits, outputAmount, functionSet,
				printOutput, levelsBack, mu, lambda);
		ArrayList<Individual> bestIndivs = net.evolutionAlgorithm();

		SaveAndLoadIndividuals.saveIndividual(bestIndivs, path, "BestIndividual_" + i);

		return bestIndivs;
	}
}
