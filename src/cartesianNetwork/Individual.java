package cartesianNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;//nextInt is normally exclusive of the top value,so add 1 to make it inclusive

import helperClasses.Functions;

/**
 * Adresses of the Individual as follows:
 * 
 * input Addresses: [0 , (inputAmount-1)] node Addresses:
 * [inputAmount,(inputAmount+nodes.size()-1)
 * 
 * @author Björn Piepenbrink
 *
 */
public class Individual implements Serializable {

	private static final long serialVersionUID = 1L;

	// the Nodes the Individual posseses
	private ArrayList<Node> nodes; // one Node contains 3 genes

	// the number of input the individual can process
	private int inputAmount;

	// output Adresses of the CGP network
	private int[] output;

	// the function set used by all the nodes in the individual
	private int functionSet;

	// allows node only to be connected to the node before ist (furthest
	// nodePost- levelsback) or an input
	private int levelsBack;

	// states if the fitness for the Individual has already been calculated
	private boolean hasFitness;
	// the fitness of the Individual
	private int fitness;

	// states if the used Nodes of the Individual have already been assigned
	private boolean usedNodesHasBeenCalculated;
	// the used Nodes for Output Calculation of the Individual
	private boolean[] usedNodes;
	
	/**
	 * generates a random Individual
	 * @param nodeAmount Number of Nodes
	 * @param inputAmount Number of Inputs
	 * @param outputAmount Number of outputs
	 * @param functionSet The used FunctionSet
	 * @param levelsBack the allowed levelsback
	 */
	public Individual(int nodeAmount, int inputAmount, int outputAmount, int functionSet, int levelsBack) {
		this.inputAmount = inputAmount;
		this.functionSet = functionSet;
		this.levelsBack = levelsBack;

		nodes = new ArrayList<>();
		for (int i = 0; i < nodeAmount; i++) {
			int function;
			int input1;
			int input2;
			function = ThreadLocalRandom.current().nextInt(1, Functions.getNrFunctions(functionSet) + 1);

			if (levelsBack <= 0) {
				// i = current position of Node
				// inputAmout + i = adress of current Node
				// upper Bound of ThreadLocalRandom.current().nextInt is
				// exclusive!
				input1 = ThreadLocalRandom.current().nextInt(0, inputAmount + i);
				input2 = ThreadLocalRandom.current().nextInt(0, inputAmount + i);
			} else {
				int currentlevelsBackMax = levelsBack;
				if (currentlevelsBackMax >= i)
					currentlevelsBackMax = i;

				input1 = ThreadLocalRandom.current().nextInt(0, inputAmount + currentlevelsBackMax);
				if (input1 >= inputAmount) {
					// input now only contains the chosen levelsBack-value
					input1 -= inputAmount;
					// get the node-adress of the referenced node, relative to
					// the current one
					// position of the current node = inputAmount + i
					input1 = inputAmount + i - input1 - 1;
				}
				input2 = ThreadLocalRandom.current().nextInt(0, inputAmount + currentlevelsBackMax);
				if (input2 >= inputAmount) {
					input2 -= inputAmount;
					input2 = inputAmount + i - input2 - 1;
				}
			}
			nodes.add(new Node(function, input1, input2));
		}
		output = new int[outputAmount];
		for (int i = 0; i < outputAmount; i++) {
			if (levelsBack <= 0) {
				output[i] = ThreadLocalRandom.current().nextInt(0, inputAmount + nodeAmount);
			} else {
				int currentlevelsBackMax = levelsBack;
				if (currentlevelsBackMax >= nodes.size())
					currentlevelsBackMax = nodes.size();

				int inputOfOutput = ThreadLocalRandom.current().nextInt(0, inputAmount + currentlevelsBackMax);
				if (inputOfOutput >= inputAmount) {
					// node doesnt use an input as input
					// but another node
					inputOfOutput -= inputAmount;
					// get the number of levelsback
					inputOfOutput = inputAmount + nodeAmount - inputOfOutput - 1;
				}
				output[i] = inputOfOutput;
			}
		}

		hasFitness = false;
		usedNodesHasBeenCalculated = false;
	}

	/**
	 * generates an Individual with already given values
	 * 
	 * @param nodes
	 *            The nodes used in the Individual
	 * @param inputAmount
	 *            the Number of Inputs
	 * @param output
	 *            The outputs Adress(es)
	 * @param funcionSet
	 *            the used Function Set
	 * @param levelsBack
	 *            the levels-back-Valueß
	 */
	public Individual(ArrayList<Node> nodes, int inputAmount, int[] output, int funcionSet, int levelsBack) {
		this.nodes = nodes;
		this.inputAmount = inputAmount;
		this.output = output;
		this.functionSet = funcionSet;
		this.levelsBack = levelsBack;
		hasFitness = false;
		usedNodesHasBeenCalculated = false;
	}
	
	/**
	 * generate Offspring by Mutation (point-mutation-operator)
	 * @param mutation_rate Percentage of Nodes that should be changed
	 * @return a Mutated Individual - Offspring of the current Individual
	 * @throws Exception 
	 */
	public Individual mutation(double mutation_rate) throws Exception {
		// calculate actual amount of genes that should be changed
		// mutation_rate * number of genes
		int genesToChange = new Double(mutation_rate * (nodes.size() * 3 + output.length)).intValue();
		return mutation(genesToChange);
	}
	
	/**
	 * generate Offspring by Mutation (point-mutation-operator)
	 * 
	 * @param num_mutations
	 *            Number of Mutations that should occur
	 * @return a Mutated Individual - Offspring of the current Individual
	 */
	private Individual mutation(int num_mutations) throws Exception {
		// copy nodes
		ArrayList<Node> nOdes = new ArrayList<>();
		for (Node node : nodes) {
			nOdes.add(new Node(node.getFunction(), node.getInput1(), node.getInput2()));
		}
		int outputAmount = output.length;
		// copy outputs
		int[] outputChild = new int[outputAmount];
		for (int i = 0; i < outputAmount; i++) {
			outputChild[i] = output[i];
		}
		// generate new offspring
		Individual offspring = new Individual(nOdes, inputAmount, outputChild, functionSet, levelsBack);

		// mutate
		if (num_mutations < 0)
			throw new IllegalArgumentException();
		if (num_mutations == 0)
			return offspring;
		for (int i = 0; i < num_mutations; i++) {
			// all node-genes can be changed + the output genes

			// one node contains 3 genes
			// input genes cannot be changed
			int geneToChange = ThreadLocalRandom.current().nextInt(0, nOdes.size() * 3 + outputAmount);

			// output gene
			if (geneToChange >= nOdes.size() * 3) {
				int outputToChange = geneToChange - (nOdes.size() * 3);
				int randomOutput;
				if (levelsBack <= 0) {
					// between 0 and inputAmount+NodeAmount -1(
					// because inputs begins at 0
					randomOutput = ThreadLocalRandom.current().nextInt(0, inputAmount + nOdes.size());
				} else {
					// levelsBack
					int currentlevelsBackMax = levelsBack;
					if (currentlevelsBackMax >= nOdes.size())
						currentlevelsBackMax = nOdes.size();
					randomOutput = ThreadLocalRandom.current().nextInt(0, inputAmount + currentlevelsBackMax);
					if (randomOutput >= inputAmount) {
						// node doesnt use an input as input
						// but another node
						randomOutput -= inputAmount;
						// get the number of levelsback
						randomOutput = inputAmount + nOdes.size() - randomOutput -1;
					}
				}
				
				offspring.getOutput()[outputToChange] = randomOutput;
			}

			// 0,3,6,9,... is function gene
			else if (geneToChange % 3 == 0) {
				int randomFunction = ThreadLocalRandom.current().nextInt(1, Functions.getNrFunctions(functionSet) + 1);
				nOdes.get(geneToChange / 3).setFunction(randomFunction);
			}

			// 1,2,4,5,6,7,... is connection gene
			else {
				int posOfNode = (geneToChange/3);
				int randomConnection;
				if (levelsBack <= 0) {
					// random Input between 0 and inputAmount+currentNode -1
					// (because inputs begins at 0)
					randomConnection = ThreadLocalRandom.current().nextInt(0, inputAmount + posOfNode);
					// (no +1 because current node can't be an input)
				} else {
					// levelsBack
					int maxLevelsBack = levelsBack;
					if (maxLevelsBack >= posOfNode)
						maxLevelsBack = posOfNode;

					randomConnection = ThreadLocalRandom.current().nextInt(0, inputAmount + maxLevelsBack);
					if (randomConnection >= inputAmount) {
						// node doesnt use an input as input
						// but another node
						randomConnection -= inputAmount;
						// get the number of levelsback
						randomConnection = inputAmount + posOfNode - maxLevelsBack - 1;
					}
				}

				if (geneToChange % 3 == 1) {
					nOdes.get(posOfNode).setInput1(randomConnection);
				} else {
					nOdes.get(posOfNode).setInput2(randomConnection);
				}
			}

		}
		return offspring;
	}
	
	/**
	 * @return if Fitness has already been calculated
	 */
	public boolean hasFitness() {
		return hasFitness;
	}

	/**
	 * @param fitness
	 *            the Fitness of the Individual
	 * @throws Exception
	 *             if fitness has already been calculated
	 */
	public void setFitness(int fitness) throws Exception {
		if (!hasFitness) {
			this.fitness = fitness;
			hasFitness = true;
		} else {
			throw new Exception("tried to change already calculated fitness");
		}
	}

	/**
	 * use HasFitness() to see if Individual already has assigned FitnessValue
	 * 
	 * @return the Fitness of the Individual
	 * @throws Exception
	 *             if no fitness has been calculated for the Individual
	 */
	public int getFitness() throws Exception {
		if (hasFitness)
			return fitness;
		else
			throw new Exception("tried to access non-existing fitness");
	}

	/**
	 * @return if used Nodes have already been calculated
	 */
	public boolean usedNodesHasBeenCalculated() {
		return usedNodesHasBeenCalculated;
	}

	/**
	 * use usedNodesHasBeenCalculated() to see if Individual has assignes
	 * UsedNodes Value
	 * 
	 * @return the UsedNodes of the Individual
	 * @throws Exception
	 *             if usedNodes hasn't been calculated
	 */
	public boolean[] getUsedNodes() throws Exception {
		if (usedNodesHasBeenCalculated)
			return usedNodes;
		else
			throw new Exception("tried to access non-existing UsedNodes");
	}

	/**
	 * @param usedNodes
	 *            the usedNodes of the Individual
	 * @throws Exception
	 *             if usedNodes has already been assigned
	 */
	public void setUsedNodes(boolean[] usedNodes) throws Exception {
		if (!usedNodesHasBeenCalculated) {
			this.usedNodes = usedNodes;
			usedNodesHasBeenCalculated = true;
		} else
			throw new Exception("tried to change already calculated used Nodes of an Individual");
	}

	/**
	 * 
	 * @return the Nodes of the Individual
	 */
	public ArrayList<Node> getNodes() {
		return nodes;
	}

	/**
	 * 
	 * @return the number of Inputs of an Individual
	 */
	public int getInputAmount() {
		return inputAmount;
	}

	/**
	 * 
	 * @return the number of outputs an individual posseses
	 */
	public int getOutputAmount() {
		return output.length;
	}

	/**
	 * 
	 * @return the outputAdresses of the Individual
	 */
	public int[] getOutput() {
		return output;
	}
	
	public void setOutput(int[] output) {
		this.output = output;
	}

	/**
	 * 
	 * @return the Function-Set of the Individual
	 */
	public int getFunctionSet() {
		return functionSet;
	}
	
	/**
	 * 
	 * @return the levels-back of the Individual
	 */
	public int getLevelsBack() {
		return levelsBack;
	}

	/**
	 * 
	 * @return the Individual as a String
	 */
	public String toString() {
		String toReturn = "";
		toReturn += "InputAmount:" + inputAmount + "\n";
		for (Node node : nodes) {
			toReturn += node.toString();
		}
		for (int i = 0; i < output.length; i++) {
			toReturn += "Output" + i + ":" + output[i] + "\n";
		}
		toReturn += "HasFitness " + hasFitness + ": " + fitness + "\n";
		return toReturn;
	}



}