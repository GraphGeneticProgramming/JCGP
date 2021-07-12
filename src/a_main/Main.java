package a_main;
import classificator.ClassificatorClassGenerator;
import classificatorClasses.*;
import dataIO.CSVReader;
import helperClasses.Digit;

public class Main {

	// picture
	static boolean binary = false;
	static int treshold = 50;
	
	//functions
	static int functionSet = 6;
	
	//levelsback
	static int levelsBack = -1;
	
	//Strategy
	static int mu = 1; 
	static int lambda = 4;
	
	//CGP
	static double mutation_rate = 0.06;
	static int nodeAmount = 1000;
	static int maxGenerations = 8000;
	static int outputAmount = 1;
	
	static int tries = 10;
	static boolean printOutput = true;
	
	static String path = "./";
	static String MNISTpath = "./";

	public static void main(String[] args) throws Exception {
		runCGP();
		//generateCode();
		//testClassificator();
		System.out.println("Done");
	}
	
	/**
	 * runs the CGP net for the given Parameters
	 * @throws Exception if smth goes wrong
	 */
	public static void runCGP() throws Exception{
		double time = System.currentTimeMillis();
		
		Digit[] digits = CSVReader.getLearnSet(MNISTpath);
		
		for(Digit dig : digits){
			dig.makeSmaller();
			dig.makeBinary(treshold);
		}
		
		CGP_NetHandler cgp_NetHandler = new CGP_NetHandler(digits, functionSet, mutation_rate, nodeAmount, maxGenerations, outputAmount,printOutput,levelsBack,mu,lambda,path);
		cgp_NetHandler.getBestIndividualOfMultiple(tries);
		
		double timeTaken = (System.currentTimeMillis()-time)/1000;
		System.out.println("Time taken:"+timeTaken/60 +"min");
		System.out.println("MutationRate:"+mutation_rate);
		System.out.println("Levelsback:"+levelsBack);
	}
	
	/**
	 * generates Code out of an Individual
	 * @throws Exception if smth goes wrong
	 */
	public static void generateCode() throws Exception{
		ClassificatorClassGenerator gen= new ClassificatorClassGenerator();
		String indivName = "F36118.txt";
		gen.generateCode(path+indivName,"T:\\BachelorArbeit\\CGP\\src\\classificatorClasses");
	}
	
	/**
	 * prüft ob der Klassifikator, dieselbe Fitness besitzt wie ursprünglich angegeben
	 */
	public static void testClassificator(){
		Digit[] digits = CSVReader.getLearnSet(MNISTpath);
		for(Digit dig : digits){
			dig.makeSmaller();
			dig.makeBinary(treshold);
		}
		

		ClassificatorF36118 classificator = new ClassificatorF36118();
		int fitness = 0;
		for(Digit digit : digits){
			int result = classificator.getResult(digit.toIntArray())[0];
			if(result != digit.getClassification()){
				fitness ++;
			}
		}
		System.out.println(fitness);
	}
}
