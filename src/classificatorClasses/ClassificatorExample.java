package classificatorClasses;

public final class ClassificatorExample {
	private double[] outputs;

	private int[] input;

	public ClassificatorExample() {
		this.outputs = new double[1196];
	}

	public int[] getResult(int[] input) {
		this.input = input;

		outputCalculator();
		
		double[] output = new double[1];
		output[0] = outputs[369];

		// cast back to int array
		int[] toReturn = new int[output.length];
		for (int i = 0; i < output.length; i++) {
			toReturn[i] = (int) Math.round(output[i]);
		}
		return toReturn;
	}

	private void outputCalculator() {
		node202();
		node246();
		node255();
		node264();
		node296();
		node305();
		node369();
	}

	private static double function(double in1, double in2, int functionNr) {
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
			throw new IllegalArgumentException("Function is not known");
		}
	}

	private void node202() {
		double node1 = (double) input[189];
		double node2 = (double) input[1];
		outputs[202] = (function(node1, node2, 18));
	}

	private void node246() {
		double node1 = (double) input[105];
		double node2 = (double) input[102];
		outputs[246] = (function(node1, node2, 4));
	}

	private void node255() {
		double node1 = (double) input[188];
		double node2 = (double) input[3];
		outputs[255] = (function(node1, node2, 14));
	}

	private void node264() {
		double node1 = (double) input[105];
		double node2 = outputs[246];
		outputs[264] = (function(node1, node2, 1));
	}

	private void node296() {
		double node1 = outputs[264];
		double node2 = outputs[255];
		outputs[296] = (function(node1, node2, 1));
	}

	private void node305() {
		double node1 = outputs[296];
		double node2 = outputs[202];
		outputs[305] = (function(node1, node2, 1));
	}

	private void node369() {
		double node1 = outputs[305];
		double node2 = (double) input[45];
		outputs[369] = (function(node1, node2, 9));
	}
}
