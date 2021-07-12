package OutPutCalculation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Calculates and Saves Statistics for the given Values
 * 
 * @author Björn Piepenbrink
 *
 */
public class StatisticsCalculator {

	/**
	 * Calculates and Saves Statistics for the given Values
	 * @param outputs the Fitness values of the Individuals
	 * @return A String that contains all relevant statistics
	 */
	public static String calculateOutput(ArrayList<Integer> outputs) {
		String toReturn = "\n Statistics: \n number of Inpputs: "+outputs.size()+"\n \n";
		Collections.sort(outputs);

		double median = getMedian(outputs);
		double arithmeticMiddle = getArithmeticMiddle(outputs);
		double smallestValue = getSmallestValue(outputs);
		double biggestValue = getBiggestValue(outputs);
		double upperQuartile = pQuartile(0.75, outputs);
		double lowerQuartile = pQuartile(0.25, outputs);
		double standardDeviation = standardDeviation(arithmeticMiddle, outputs);
		double standardError = standardErrorOfTheMean(standardDeviation,outputs);

		//with 5 
		DecimalFormat format = new DecimalFormat("#.##");
		// Median
		toReturn += "Median: " + format.format(median) + "\n";
		// arithmetic Middle
		toReturn += "Arithmetic Mean: " + format.format(arithmeticMiddle) + "\n";
		// Standardfehler
		toReturn += "Standard Error of the Mean: " + format.format(standardError) + "\n";
		// maximum Deviation
		toReturn += "Smallest Value: " + format.format(smallestValue) + "\n";
		toReturn += "Biggest Value: " + format.format(biggestValue) + "\n";
		// Beide Quartile
		toReturn += "Lower Quartile: " + format.format(lowerQuartile) + "\n";
		toReturn += "Upper Quartile: " + format.format(upperQuartile) + "\n";
		// Standardabweichung
		toReturn += "Standard Deviation: " + format.format(standardDeviation) + "\n";
		return toReturn;
	}

	private static double getMedian(ArrayList<Integer> outputs) {
		return pQuartile(0.5, outputs);
	}

	private static double getArithmeticMiddle(ArrayList<Integer> outputs) {
		double sum = 0;
		for (Integer in : outputs) {
			sum += in.intValue();
		}
		return (sum / outputs.size());
	}

	private static double getSmallestValue(ArrayList<Integer> outputs) {
		return outputs.get(0);
	}

	private static double getBiggestValue(ArrayList<Integer> outputs) {
		return outputs.get(outputs.size() - 1);
	}

	private static double pQuartile(double p, ArrayList<Integer> outputs) {
		double np = outputs.size() * p;
		if (np % 1 != 0) {
			int index = (new Double(Math.floor(np + 1) - 1).intValue());
			return outputs.get(index);
		} else {
			double toReturn;

			int index1 = (new Double(np)).intValue();
			int index2 = (new Double(np)).intValue() - 1;

			toReturn = (0.5) * (outputs.get(index1) + outputs.get(index2));

			return toReturn;
		}
	}

	private static double variance(double arihthMiddle, ArrayList<Integer> outputs) {
		double sum = 0;
		for(int i = 0 ; i<outputs.size();i++){
			double x = outputs.get(i) - arihthMiddle;
			x = Math.pow(x, 2);
			x = (x / (outputs.size()-1));
			
			sum += x;
		}
		return sum;
	}
	
	private static double standardDeviation(double arihthMiddle, ArrayList<Integer> outputs) {
		return Math.sqrt(variance(arihthMiddle, outputs));
	}
	
	private static double standardErrorOfTheMean(double standardDeviation , ArrayList<Integer> outputs) {
		return ((standardDeviation)/(Math.sqrt(outputs.size())));
	}
	
}
