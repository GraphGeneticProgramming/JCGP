package helperClasses;

/**
 * Fuction-Lookup-Table of PCGP
 * 
 * @author Björn Piepenrbink
 *
 */
public class Functions {

	/**
	 * @param functionSet
	 *            the acitve functionSet
	 * @return the hightest Adress of a Function / Number of functions in the
	 *         given function set
	 */
	static public int getNrFunctions(int functionSet) {
		if (functionSet == 1) {
			return 4;
		}
		if (functionSet == 2) {
			return 22;
		}
		if (functionSet == 3) {
			return 16;
		}
		if (functionSet == 4) {
			return 8;
		}

		if (functionSet == 5) {
			return 3;
		}
		if (functionSet == 6) {
			return 21;
		}
		throw new IllegalArgumentException("FunctionSet is not known");
	}

	/**
	 * 
	 * @param functionSet the active functionSet
	 * @param in1 the first input
	 * @param in2 the second input
	 * @param functionNr the adress of the function
	 * @return the output for the given values
	 */
	static public double getResultForFunction(int functionSet, double in1, double in2, int functionNr) {
		if (functionSet == 1) {
			return getResultForFunctionSet1(in1, in2, functionNr);
		}
		if (functionSet == 2) {
			return getResultForFunctionSet2(in1, in2, functionNr);
		}
		if (functionSet == 3) {
			return getResultForFunctionSet3(in1, in2, functionNr);
		}
		if (functionSet == 4) {
			return getResultForFunctionSet4(in1, in2, functionNr);
		}
		if (functionSet == 5) {
			return getResultForFunctionSet5(in1, in2, functionNr);
		}
		if (functionSet == 6) {
			return getResultForFunctionSet6(in1, in2, functionNr);
		}
		throw new IllegalArgumentException("FunctionSet is not known");
	}

	/**
	 * small Functionset
	 */
	static private double getResultForFunctionSet1(double in1, double in2, int functionNr) {
		switch (functionNr) {
		case 1:// ADD
			return in1 + in2;
		case 2:// SUB
			return in1 - in2;
		case 3:// MULT
			return in1 * in2;
		case 4:// DIVIDE
			if (in2 == 0)
				return 0;
			return in1 / in2;
		default:
			throw new IllegalArgumentException("Function is not known");
		}
	}

	/**
	 * FunctionSet out of the Book Cartesian Genetic Programming by Miller
	 */
	static private double getResultForFunctionSet2(double in1, double in2, int functionNr) {
		switch (functionNr) {
		case 1:// ADD
			return in1 + in2;
		case 2:// SUB
			return in1 - in2;
		case 3:// MULT
			return in1 * in2;
		case 4:// DIVIDE
			if (in2 == 0)
				return 0;
			return in1 / in2;
		case 5:// ADD CONST
			return in1 + 255;
		case 6:// SUB CONST
			return in1 - 255;
		case 7:// DIV CONST
			return in1 / 255;
		case 8:// SQRT
			return Math.sqrt(in1);
		case 9:// POW
			double pow = Math.pow(in1, in2);
			if (Double.isNaN(pow))
				return 1;
			return pow;
		case 10:// SQUARE
			double sqrt = Math.pow(in1, 2);
			if (Double.isNaN(sqrt))
				return 1;
			return sqrt;
		case 11:// COS
			return Math.cos(in1);
		case 12:// SIN
			return Math.sin(in1);
		case 13:// NOP
			return in1;
		case 14:// CONST
			return 255;
		case 15:// ABS
			return Math.abs(in1);
		case 16:// MIN
			return Math.min(in1, in2);
		case 17:// MAX
			return Math.max(in1, in2);
		case 18:// LOG2
			return (Math.log(in1) / Math.log(2));
		case 19:// ROUND
			return (Math.round(in1));
		case 20:// FRAC
			return (in1 % 1);
		case 21:// RECIPRICAL
			return (1 / in1);
		case 22:// RSQRT
			return (1 / Math.sqrt(in1));
		default:
			throw new IllegalArgumentException("Function " + functionNr + " is not known");
		}
	}

	/**
	 * FunctionSet by Roman Kalkreuth
	 */
	static private double getResultForFunctionSet3(double in1, double in2, int functionNr) {
		switch (functionNr) {
		case 1:// CONSTANT
			return 255;
		case 2:// IDENTITY
			return (int) in1;
		case 3:// INVERSION
			return (255 - (int) in1);
		case 4:// BITWISE OR
			return ((int) in1 | (int) in2);
		case 5:// BITWISE AND
			return ((int) in1 & (int) in2);
		case 6:// BITWISE NAND
			return ((int) (~((int) in1 & (int) in2) & 0xFF));
		case 7:// BITWISE OR (in1 INVERS)
			return ((int) ((~(int) in1) & 0xFF | (int) in2));
		case 8:// BITWISE XOR
			return ((int) ((int) in1 ^ (int) in2));
		case 9:// RIGHTSHIFT ONE
			return ((int) ((int) in1 >>> 1));
		case 10:// RIGHTSHIFT TWO
			return ((int) ((int) in1 >>> 2));
		case 11:// SWAP
			int leftNibble = ((int) in1 << 4) & 0xFF;
			int rightNibble = ((int) in2 >>> 4) & 0xFF;
			return ((int) leftNibble | rightNibble);
		case 12:// ADD SAT
			int result = ((int) in1 + (int) in2);
			return Math.min(result, 255);
		case 13:// ADD
			return ((int) ((int) in1 + (int) in2) & 0xFFFF);
		case 14:// AVERAGE
			int average = Math.min((int) in1 + (int) in2, 255);
			return (average >>> 1);
		case 15:// MAX
			return Math.max((int) in1, (int) in2);
		case 16:// MIN
			return Math.min((int) in1, (int) in2);
		default:
			throw new IllegalArgumentException("Function is not known");
		}
	}

	/**
	 * FunctionSet by Roman Kalkreuth
	 */
	static private double getResultForFunctionSet4(double in1, double in2, int functionNr) {
		switch (functionNr) {
		case 1:// BITWISE OR
			return ((int) in1 | (int) in2);
		case 2:// BITWISE AND
			return ((int) in1 & (int) in2);
		case 3:// BITWISE XOR
			return ((int) ((int) in1 ^ (int) in2));
		case 4:// ADD SAT
			int result = ((int) in1 + (int) in2);
			return Math.min(result, 255);
		case 5:// ADD
			return ((int) ((int) in1 + (int) in2) & 0xFFFF);
		case 6:// AVERAGE
			int average = Math.min((int) in1 + (int) in2, 255);
			return (average >>> 1);
		case 7:// MAX
			return Math.max((int) in1, (int) in2);
		case 8:// MIN
			return Math.min((int) in1, (int) in2);
		default:
			throw new IllegalArgumentException("Function is not known");
		}
	}

	/**
	 * small Functionset for 0 & 1 as Inputs
	 */
	static private double getResultForFunctionSet5(double in1, double in2, int functionNr) {
		switch (functionNr) {
		case 1:// AND
			if (in1 == 1 && in2 == 1)
				return 1;
			return 0;
		case 2:// OR
			if (in1 == 1 || in2 == 1)
				return 1;
			return 0;
		case 3:// NOT
			if (in1 == 1)
				return 0;
			return 1;
		default:
			throw new IllegalArgumentException("Function is not known");
		}
	}

	/**
	 * FunctionSet out of the Book Cartesian Genetic Programming by Miller without Power-Function
	 */
	static private double getResultForFunctionSet6(double in1, double in2, int functionNr) {
		switch (functionNr) {
		case 1:// ADD
			return in1 + in2;
		case 2:// SUB
			return in1 - in2;
		case 3:// MULT
			return in1 * in2;
		case 4:// DIVIDE
			if (in2 == 0)
				return 0;
			return in1 / in2;
		case 5:// ADD CONST
			return in1 + 1;
		case 6:// SUB CONST
			return in1 - 1;
		case 7:// DIV CONST
			return in1 / 2;
		case 8:// SQRT
			return Math.sqrt(in1);
		case 9:// SQUARE
			double sqrt = Math.pow(in1, 2);
			if (Double.isNaN(sqrt))
				return 1;
			return sqrt;
		case 10:// COS
			return Math.cos(in1);
		case 11:// SIN
			return Math.sin(in1);
		case 12:// NOP
			return in1;
		case 13:// CONST
			return 1;
		case 14:// ABS
			return Math.abs(in1);
		case 15:// MIN
			return Math.min(in1, in2);
		case 16:// MAX
			return Math.max(in1, in2);
		case 17:// LOG2
			return (Math.log(in1) / Math.log(2));
		case 18:// ROUND
			return (Math.round(in1));
		case 19:// FRAC
			return (in1 % 1);
		case 20:// RECIPRICAL
			return (1 / in1);
		case 21:// RSQRT
			return (1 / Math.sqrt(in1));
		default:
			throw new IllegalArgumentException("Function " + functionNr + " is not known");
		}
	}
}
