package cartesianNetwork;

import java.io.Serializable;

/**
 * One Node of PCGP
 * consists of two Inputs and the used Function number
 * @author Björn Piepenbrink
 *
 */
public class Node implements Serializable{

	
	private static final long serialVersionUID = 1L;
	//functionGene (Address of the used function)
	int function;
	//inputGene1 (Address of the first Input of the Node)
	int input1;
	//inputGene2 (Address of the first Input of the Node)
	int input2;
	
	/**
	 * One Node of CGP
	 * @param function Adress of the used Function
	 * @param input1 Adress of the first used Input
	 * @param input2 Adress of the second Input
	 */
	public Node (int function, int input1, int input2){
		this.function = function;
		this.input1 = input1;
		this.input2 = input2;
	}
	

	public int getFunction() {
		return function;
	}

	public void setFunction(int function) {
		this.function = function;
	}

	public int getInput1() {
		return input1;
	}

	public void setInput1(int input1) {
		this.input1 = input1;
	}

	public int getInput2() {
		return input2;
	}

	public void setInput2(int input2) {
		this.input2 = input2;
	}
	
	public String toString(){
		String toReturn="";
		toReturn += "Function: "+function+"\n";
		toReturn += "Input1: "+input1+"\n";
		toReturn += "Input2: "+input1+"\n";
		return toReturn;
	}
	
}
