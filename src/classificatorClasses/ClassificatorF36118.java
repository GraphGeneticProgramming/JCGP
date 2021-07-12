package classificatorClasses;
public final class ClassificatorF36118 {
  private double[] outputs;

  private int[] input;

  public ClassificatorF36118() {
    this.outputs = new double[1196];
  }

  public int[] getResult(int[] input) {
    this.input = input;

        outputCalculator();

        double[] output = new double[1];

        output[0]=outputs[819];


        //cast back to int array
        int[] toReturn = new int[output.length];
        for(int i=0;i<output.length;i++){
        toReturn[i]=(int) Math.round(output[i]);
        }
        ;
    return toReturn;
  }

  private void outputCalculator() {
    node200();
        node213();
        node214();
        node228();
        node231();
        node248();
        node254();
        node281();
        node284();
        node286();
        node309();
        node322();
        node328();
        node390();
        node414();
        node430();
        node654();
        node776();
        node819();
        ;
  }

  private static double function(double in1, double in2, int functionNr) {
    switch(functionNr){
    case 1://ADD
    return in1+in2;
    case 2://SUB
    return in1-in2;
    case 3://MULT
    return in1*in2;
    case 4://DIVIDE
    if(in2==0)return 0;
    return in1/in2;
    case 5://ADD CONST
    return in1+1;
    case 6://SUB CONST
    return in1-1;
    case 7://DIV CONST
    return in1/2;
    case 8://SQRT
    return Math.sqrt(in1);
    case 9://SQUARE
    double sqrt = Math.pow(in1,2);
    if(Double.isNaN(sqrt))return 1;
    return sqrt;
    case 10://COS
    return Math.cos(in1);
    case 11://SIN
    return Math.sin(in1);
    case 12://NOP
    return in1;
    case 13://CONST
    return 1;
    case 14://ABS
    return Math.abs(in1);
    case 15://MIN
    return Math.min(in1,in2);
    case 16://MAX
    return Math.max(in1,in2);
    case 17://LOG2
    return (Math.log(in1) / Math.log(2));
    case 18://ROUND
    return (Math.round(in1));
    case 19://FRAC
    return (in1%1);
    case 20://RECIPRICAL
    return (1/in1);
    case 21://RSQRT
    return (1/Math.sqrt(in1));
    default:
    throw new IllegalArgumentException("Function is not known");
    }}

  private void node200() {
    double node1 = (double) input[135];
        double node2 = (double) input[175];
    outputs[200] = (function(node1,node2,7));
  }

  private void node213() {
    double node1 = (double) input[187];
        double node2 = (double) input[116];
    outputs[213] = (function(node1,node2,12));
  }

  private void node214() {
    double node1 = (double) input[178];
        double node2 = (double) input[41];
    outputs[214] = (function(node1,node2,16));
  }

  private void node228() {
    double node1 = outputs[200];
        double node2 = (double) input[88];
    outputs[228] = (function(node1,node2,1));
  }

  private void node231() {
    double node1 = (double) input[165];
        double node2 = outputs[214];
    outputs[231] = (function(node1,node2,10));
  }

  private void node248() {
    double node1 = (double) input[97];
        double node2 = (double) input[144];
    outputs[248] = (function(node1,node2,11));
  }

  private void node254() {
    double node1 = (double) input[65];
        double node2 = outputs[248];
    outputs[254] = (function(node1,node2,15));
  }

  private void node281() {
    double node1 = (double) input[35];
        double node2 = outputs[254];
    outputs[281] = (function(node1,node2,13));
  }

  private void node284() {
    double node1 = (double) input[7];
        double node2 = (double) input[164];
    outputs[284] = (function(node1,node2,6));
  }

  private void node286() {
    double node1 = outputs[228];
        double node2 = (double) input[157];
    outputs[286] = (function(node1,node2,5));
  }

  private void node309() {
    double node1 = outputs[231];
        double node2 = outputs[284];
    outputs[309] = (function(node1,node2,7));
  }

  private void node322() {
    double node1 = (double) input[93];
        double node2 = outputs[309];
    outputs[322] = (function(node1,node2,1));
  }

  private void node328() {
    double node1 = outputs[322];
        double node2 = outputs[286];
    outputs[328] = (function(node1,node2,16));
  }

  private void node390() {
    double node1 = (double) input[91];
        double node2 = outputs[213];
    outputs[390] = (function(node1,node2,11));
  }

  private void node414() {
    double node1 = (double) input[119];
        double node2 = outputs[390];
    outputs[414] = (function(node1,node2,16));
  }

  private void node430() {
    double node1 = (double) input[113];
        double node2 = outputs[281];
    outputs[430] = (function(node1,node2,19));
  }

  private void node654() {
    double node1 = outputs[328];
        double node2 = outputs[414];
    outputs[654] = (function(node1,node2,4));
  }

  private void node776() {
    double node1 = outputs[430];
        double node2 = (double) input[145];
    outputs[776] = (function(node1,node2,7));
  }

  private void node819() {
    double node1 = outputs[654];
        double node2 = outputs[776];
    outputs[819] = (function(node1,node2,9));
  }
}
