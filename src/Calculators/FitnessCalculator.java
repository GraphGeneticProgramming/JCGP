package Calculators;

import java.util.concurrent.Callable;

import cartesianNetwork.Individual;
import helperClasses.Digit;
/**
 * Class for Fitness Calculation of an Individual
 * calls the IndividualCalculoator for the actual calculation
 * @author Björn Piepenbrink
 *
 */
public class FitnessCalculator implements Callable<Integer>{

	Individual indiv;
	Digit[] digits;
	boolean allowOutput;
	
	/**
	 * Smaller fitness is better
	 * calculates the fitness for the given Value
	 * 0 if Output is correct
	 * higher if it isn't (+1 for each misidentification) of the data set
	 * @param indiv the Individual for which the fitness shouls be calculated
	 * @param digits the InputData
	 * @param allowOutput if output to the console is allowed
	 */
	public FitnessCalculator(Individual indiv, Digit[] digits, boolean allowOutput){
		this.indiv = indiv;
		this.digits = digits;
		this.allowOutput = allowOutput;
	}

	@Override
	/**
	 * calculates Fitness of the Individual
	 * should be used in Threads to speed up Evaluation
	 */
	public Integer call() throws Exception {
		int fitness=0;
		IndividualCalculator individualCalculator = new IndividualCalculator(indiv);
		for(int i =0; i<digits.length;i++){
			fitness += individualCalculator.calculateFitness(digits[i].toIntArray(), digits[i].getClassification());
		}
		if(allowOutput)System.out.println("Fitness of Individual: = "+fitness);
		return fitness;
	}	
}
