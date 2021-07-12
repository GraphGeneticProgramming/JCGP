package Calculators;

import cartesianNetwork.Individual;
import cartesianNetwork.Node;
import helperClasses.Functions;

/**
 * calculates UsedNodes, Output and Fitness value of an Individual
 * 
 * @author Björn Piepenbrink
 *
 */
public class IndividualCalculator {

	Individual indiv;
	boolean[] usedNodes;

	/**
	 * generates the IndividualCalculator calculates all the usedNodes so that
	 * they can be used later for putput calculation
	 * 
	 * @param indiv The Individual which should be processed
	 * @throws Exception if an error occurs
	 */
	public IndividualCalculator(Individual indiv) throws Exception {
		usedNodes = new boolean[indiv.getNodes().size()];
		this.indiv = indiv;
		usedNodes = usedNodes();
	}

	/**
	 * calculates the Ouptut of the given Individual with the given input
	 * 
	 * @param input The Input for the Individual
	 * @return The Output for the given Input
	 * @throws Exception if an error occurs during calculation
	 */
	public double[] calculateOutput(int[] input) throws Exception {
		if (indiv.getInputAmount() != input.length) {
			throw new Exception("gegebene InputLänge entspricht nicht der angegebenen InputLänge des Individuums");
		}
		// go forward through all nodes (if used) and calculate all the outputs
		// of those
		double[] outputs = new double[indiv.getNodes().size()];

		// calculate output for all Nodes (if used)
		for (int i = 0; i < outputs.length; i++) {
			// check if node is used
			if (usedNodes[i]) {
				Node currentNode = indiv.getNodes().get(i);
				// get input adresses of node
				int input1 = currentNode.getInput1();
				int input2 = currentNode.getInput2();
				// save the input in input1 & input 2 so that the output can be
				// calculated
				double realinput1;
				double realinput2;
				if (input1 < indiv.getInputAmount()) {
					realinput1 = input[input1];
				} else {
					realinput1 = outputs[input1 - indiv.getInputAmount()];
				}
				if (input2 < indiv.getInputAmount()) {
					realinput2 = input[input2];
				} else {
					realinput2 = outputs[input2 - indiv.getInputAmount()];
				}
				outputs[i] = Functions.getResultForFunction(indiv.getFunctionSet(), realinput1, realinput2,
						currentNode.getFunction());
			}
		}
		// calculate Output for the Network
		double outputsOfNet[] = new double[indiv.getOutputAmount()];
		for (int i = 0; i < indiv.getOutputAmount(); i++) {
			int adressOfOutput = indiv.getOutput()[i];
			if (adressOfOutput < indiv.getInputAmount()) {
				outputsOfNet[i] = input[adressOfOutput];
			} else {
				outputsOfNet[i] = outputs[adressOfOutput - indiv.getInputAmount()];
			}
		}
		return outputsOfNet;
	}

	/**
	 * Returns the used Nodes of an Individual this method assumes you only want
	 * the boolean[] of the nodes thats why the input and output data is always
	 * used
	 * 
	 * @return all the UsedNodes of the Individual
	 */
	private boolean[] usedNodes() throws Exception {
		if (indiv.usedNodesHasBeenCalculated())
			return indiv.getUsedNodes();
		// initialize boolean array with all values = false (default)
		boolean[] usedNodes = new boolean[indiv.getNodes().size()];

		// Find active nodes by going from back to front
		// begin with output node
		int outputInput = 0;
		int inputAmount = indiv.getInputAmount();
		for (int i = 0; i < indiv.getOutputAmount(); i++) {
			outputInput = indiv.getOutput()[i];// where the output gets its data
			if (outputInput >= inputAmount) {
				usedNodes[outputInput - inputAmount] = true;
				// substract inputAmountarray because the boolean is only as
				// large as the node-Array
			}
			// else: output uses input node as input so no node is used
		}

		// check all Nodes
		for (int i = usedNodes.length - 1; i >= 0; i--) {
			if (usedNodes[i] == true) {
				// mark both InputNodes as used
				int firstInput = indiv.getNodes().get(i).getInput1();
				int secondInput = indiv.getNodes().get(i).getInput2();
				// check if Node uses InputData as an Input
				if (!(firstInput < inputAmount)) {
					usedNodes[firstInput - inputAmount] = true;
				}
				if (!(secondInput < inputAmount)) {
					usedNodes[secondInput - inputAmount] = true;
				}
			}
		}
		indiv.setUsedNodes(usedNodes);
		return usedNodes;
	}

	/**
	 * Smaller fitness is better calculates the fitness for the given Value 0 if
	 * Output is correct higher if it isn't
	 * 
	 * @param input
	 *            Input
	 * @param wantedOutput
	 *            the Output that should be reached
	 * @return The Fitness Value of the Individual
	 * @throws Exception if an error occurs
	 */
	public int calculateFitness(int[] input, int wantedOutput) throws Exception {
		double[] output = calculateOutput(input);
		if (output.length == 1) {
			if ((int) Math.round(output[0]) == wantedOutput) {
				return 0;
			} else {
				return 1;
			}
		} else if (output.length == 4) {
			int outputInInteger = -1;
			// check Outputs
			for (int i = 0; i < output.length; i++) {
				if (output[i] != 0 && output[i] != 1) {
					throw new Exception("Only Outputs of 1 and 0 are allowed if the output length is 4");
				}
			}
			// Binary Output to Int Input
			if (output[0] == 0) {
				if (output[1] == 0) {
					if (output[2] == 0) {
						if (output[3] == 0) {
							outputInInteger = 0;
						} else {
							outputInInteger = 1;
						}
					} else {
						if (output[3] == 0) {
							outputInInteger = 2;
						} else {
							outputInInteger = 3;
						}
					}
				} else {
					if (output[2] == 0) {
						if (output[3] == 0) {
							outputInInteger = 4;
						} else {
							outputInInteger = 5;
						}
					} else {
						if (output[3] == 0) {
							outputInInteger = 6;
						} else {
							outputInInteger = 7;
						}
					}
				}
			} else {
				if (output[1] == 0 && output[2] == 0) {
					if (output[3] == 0) {
						outputInInteger = 8;
					} else {
						outputInInteger = 9;
					}
				}
			}

			if (outputInInteger == wantedOutput) {
				return 0;
			} else {
				return 1;
			}

		} else {
			throw new Exception("Output Length of " + output.length + " is unknown and cannot be further processed\n"
					+ "additional Code in IndividualCalculator is needed for calcultaing Fitness");
		}
	}
}
