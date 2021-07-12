package classificator;

import java.nio.file.Paths;
import java.util.ArrayList;
import javax.lang.model.element.Modifier;
import com.squareup.javapoet.*;
import cartesianNetwork.Individual;
import cartesianNetwork.Node;
import dataIO.SaveAndLoadIndividuals;

public class ClassificatorClassGenerator {
	Individual indiv;
	
	/**
	 * generates Code out of the Indicidual and saves it
	 * @param path The Path where the Individual is saved
	 * @param savingPath The path where the Code should be saved
	 * @throws Exception is an error occurs
	 */
	public void generateCode(String path,String savingPath) throws Exception {
		try {
			this.indiv = SaveAndLoadIndividuals.readIndividual(path);
		} catch (Exception e) {
			System.out.println("Please initialize the Object again with a valid Path");
		}
		
		generateCode(indiv,savingPath);
	}
	
	/**
	 * generates Code out of an Individual
	 * @param indiv the Individual
	 * @param path the path where the code should be saved
	 * @throws Exception if an error occurs
	 */
	public void generateCode(Individual indiv,String path) throws Exception {	
		this.indiv = indiv;
		if(indiv == null)return;
		System.out.println("Generating Code for Individual with Fitness: "+indiv.getFitness());
		MethodSpec constructor = MethodSpec.constructorBuilder()
			    .addModifiers(Modifier.PUBLIC)
			    .addStatement("this.outputs = new double["+(indiv.getNodes().size()+indiv.getInputAmount())+"]")
			    .build();
		
		String classname = "Classificator";
		
		TypeSpec generatedCodeExample = TypeSpec.classBuilder(classname)
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
				.addField(double[].class,"outputs",Modifier.PRIVATE)
				.addField(int[].class,"input",Modifier.PRIVATE)
				.addMethod(constructor)
				.addMethod(generateGetResultMethod(indiv))
				.addMethod(generateOutputCalculator(indiv))
				.addMethod(generateFunctionMethods(indiv))
				.addMethods(generateNodeFunctions(indiv))
				.build();

		JavaFile javaFile = JavaFile.builder("", generatedCodeExample).build();
		javaFile.writeTo(Paths.get(path));
	}
	
	private static MethodSpec generateGetResultMethod(Individual indiv){
		String code ="";
		code += "this.input = input;\n\n";
		code += "outputCalculator();\n\n";
		
		code += "double[] output = new double["+indiv.getOutputAmount()+"];\n\n";
		
		for(int i=0; i<indiv.getOutputAmount();i++){
			if(indiv.getOutput()[i] < indiv.getInputAmount()){
				code +=	"output["+i+"]=input["+indiv.getOutput()[i]+"];\n";
			}
			else{
				code +=	"output["+i+"]=outputs["+indiv.getOutput()[i]+"];\n";
			}
		}
		
		code +="\n\n//cast back to int array\n";
		code += "int[] toReturn = new int[output.length];\n";
		code += "for(int i=0;i<output.length;i++){\n"
			 + "toReturn[i]=(int) Math.round(output[i]);\n"
			 	+ "}\n";

		MethodSpec getResult = MethodSpec.methodBuilder("getResult")
				.addModifiers(Modifier.PUBLIC)
				.returns(int[].class)
				.addParameter(int[].class, "input")
				.addStatement(code)
				.addStatement("return toReturn").build();
		return getResult;
	}
	
	private static MethodSpec generateOutputCalculator(Individual indiv) throws Exception{
		String code ="";
		for(int i=0;i<indiv.getNodes().size();i++){
			if(indiv.getUsedNodes()[i]){
				code+="node"+(i+indiv.getInputAmount())+"();\n";
			}
		}
		
		MethodSpec getResult = MethodSpec.methodBuilder("outputCalculator")
				.addModifiers(Modifier.PRIVATE)
				.returns(void.class)
				.addStatement(code).build();
		return getResult;
	}
	
	private static ArrayList<MethodSpec> generateNodeFunctions(Individual indiv) throws Exception{
		ArrayList<MethodSpec> nodeMethods = new ArrayList<>();
		//nodeFunctions
		for(int i=0;i<indiv.getNodes().size();i++){
			Node node = indiv.getNodes().get(i);
			if(indiv.getUsedNodes()[i]==true){
				
				String input = "";
				
				if(node.getInput1()<indiv.getInputAmount()){
					input+=  "double node1 = (double) input["+node.getInput1()+"];\n";
				}
				else{
					input+=  "double node1 = outputs["+node.getInput1()+"];\n";
				}
				if(node.getInput2()<indiv.getInputAmount()){
					input+=  "double node2 = (double) input["+node.getInput2()+"]";
				}
				else{
					input+=  "double node2 = outputs["+node.getInput2()+"]";
				}
				
				
				MethodSpec getResult = MethodSpec.methodBuilder("node"+(i+indiv.getInputAmount()))
						.addModifiers(Modifier.PRIVATE)
						.returns(void.class)
						.addStatement(input)
						.addStatement("outputs["+(i+indiv.getInputAmount())+"] = (function(node1,node2,"+node.getFunction()+"))").build();
				nodeMethods.add(getResult);
			}
		}
		return nodeMethods;
	}

	private static MethodSpec generateFunctionMethods(Individual indiv) throws Exception{
		String codeBlock="";
		if(indiv.getFunctionSet()==1){
			codeBlock += "switch(functionNr){\n"
					+ "case 1://ADD\n"
					+ "return in1+in2;\n"
					+ "case 2://SUB\n"
					+ "return in1-in2;\n"
					+ "case 3://MULT\n"
					+ "return in1*in2;\n"
					+ "case 4://DIVIDE\n"
					+ "if(in2==0)return 0;\n"
					+ "return in1/in2;\n"
					+ "default:\n"
					+ "throw new IllegalArgumentException(\"Function is not known\");\n"
					+ "}";
		}
		else if(indiv.getFunctionSet()==2){
			codeBlock +="switch(functionNr){\n"
					+ "case 1://ADD\n"
					+ "return in1+in2;\n"
					+ "case 2://SUB\n"
					+ "return in1-in2;\n"
					+ "case 3://MULT\n"
					+ "return in1*in2;\n"
					+ "case 4://DIVIDE\n"
					+ "if(in2==0)return 0;\n"
					+ "return in1/in2;\n"
					+ "case 5://ADD CONST\n"
					+ "return in1+255;\n"
					+ "case 6://SUB CONST\n"
					+ "return in1-255;\n"
					+ "case 7://DIV CONST\n"
					+ "return in1/255;\n"
					+ "case 8://SQRT\n"
					+ "return Math.sqrt(in1);\n"
					+ "case 9://POW\n"
					+ "double pow = Math.pow(in1,in2);\n"
					+ "if(Double.isNaN(pow))return 1;\n"
					+ "return pow;\n"
					+ "case 10://SQUARE\n"
					+ "double sqrt = Math.pow(in1,2);\n"
					+ "if(Double.isNaN(sqrt))return 1;\n"
					+ "return sqrt;\n"
					+ "case 11://COS\n"
					+ "return Math.cos(in1);\n"
					+ "case 12://SIN\n"
					+ "return Math.sin(in1);\n"
					+ "case 13://NOP\n"
					+ "return in1;\n"
					+ "case 14://CONST\n"
					+ "return 255;\n"
					+ "case 15://ABS\n"
					+ "return Math.abs(in1);\n"
					+ "case 16://MIN\n"
					+ "return Math.min(in1,in2);\n"
					+ "case 17://MAX\n"
					+ "return Math.max(in1,in2);\n"
					+ "case 18://LOG2\n"
					+ "return (Math.log(in1) / Math.log(2));\n"
					+ "case 19://ROUND\n"
					+ "return (Math.round(in1));\n"
					+ "case 20://FRAC\n"
					+ "return (in1%1);\n"
					+ "case 21://RECIPRICAL\n"
					+ "return (1/in1);\n"
					+ "case 22://RSQRT\n"
					+ "return (1/Math.sqrt(in1));\n"
					+ "default:\n"
					+ "throw new IllegalArgumentException(\"Function is not known\");\n"
					+ "}";
		}
		else if(indiv.getFunctionSet()==3){
			codeBlock += "switch(functionNr){\n"
					+ "case 1://CONSTANT\n"
					+ "return 255;\n"
					+ "case 2://IDENTITY\n"
					+ "return (int)in1;\n"
					+ "case 3://INVERSION\n"
					+ "return (255-(int)in1);\n"
					+ "case 4://BITWISE OR\n"
					+ "return ((int)in1 | (int)in2);\n"
					+ "case 5://BITWISE AND\n"
					+ "return ((int)in1 & (int)in2);\n"
					+ "case 6://BITWISE NAND\n"
					+ "return ((int)(~((int)in1 & (int)in2)&0xFF));\n"
					+ "case 7://BITWISE OR (in1 INVERS)\n"
					+ "return ((int)( (~(int)in1)&0xFF | (int)in2));\n"
					+ "case 8://BITWISE XOR\n"
					+ "return ((int)((int)in1 ^ (int)in2));\n"
					+ "case 9://RIGHTSHIFT ONE\n"
					+ "return ((int)((int)in1>>>1));\n"
					+ "case 10://RIGHTSHIFT TWO\n"
					+ "return ((int)((int)in1>>>2));\n"
					+ "case 11://SWAP\n"
					+ "int leftNibble = ((int)in1<<4)&0xFF;\n"
					+ "int rightNibble = ((int)in2>>>4)&0xFF;\n"
					+ "return ((int)leftNibble | rightNibble);\n"
					+ "case 12://ADD SAT\n"
					+ "int result = ((int)in1 + (int)in2);\n"
					+ "return Math.min(result,255);\n"
					+ "case 13://ADD\n"
					+ "return ((int)((int)in1 + (int)in2) &0xFFFF);\n"
					+ "case 14://AVERAGE\n"
					+ "int average = Math.min((int)in1 + (int)in2,255);\n"
					+ "return (average>>>1);\n"
					+ "case 15://MAX\n"
					+ "return Math.max((int)in1,(int)in2);\n"
					+ "case 16://MIN\n"
					+ "return Math.min((int)in1,(int)in2);\n"
					+ "default:\n"
					+ "throw new IllegalArgumentException(\"Function is not known\");\n"
					+ "}";
		}
		else if(indiv.getFunctionSet()==4){
			codeBlock += "switch(functionNr){\n"
					+ "case 1://BITWISE OR\n"
					+ "return ((int)in1 | (int)in2);\n"
					+ "case 2://BITWISE AND\n"
					+ "return ((int)in1 & (int)in2);\n"
					+ "case 3://BITWISE XOR\n"
					+ "return ((int)((int)in1 ^ (int)in2));\n"
					+ "case 4://ADD SAT\n"
					+ "int result = ((int)in1 + (int)in2);\n"
					+ "return Math.min(result,255);\n"
					+ "case 5://ADD\n"
					+ "return ((int)((int)in1 + (int)in2) &0xFFFF);\n"
					+ "case 6://AVERAGE\n"
					+ "int average = Math.min((int)in1 + (int)in2,255);\n"
					+ "return (average>>>1);\n"
					+ "case 7://MAX\n"
					+ "return Math.max((int)in1,(int)in2);\n"
					+ "case 8://MIN\n"
					+ "return Math.min((int)in1,(int)in2);\n"
					+ "default:\n"
					+ "throw new IllegalArgumentException(\"Function is not known\");\n"
					+ "}";
		}
		else if(indiv.getFunctionSet()==5){
			codeBlock +="switch(functionNr){\n"
					+ "case 1://AND\n"
					+ "if(in1==1&&in2==1)return 1;\n"
					+ "return 0;\n"
					+ "case 2://OR\n"
					+ "if(in1==1||in2==1)return 1;\n"
					+ "return 0;\n"
					+ "case 3://NOT\n"
					+ "if(in1==1)return 0;\n"
					+ "return 1;\n"
					+ "default:\n"
					+ "throw new IllegalArgumentException(\"Function is not known\");\n"
					+ "}";
		}
		else if(indiv.getFunctionSet() == 6){
			codeBlock +="switch(functionNr){\n"
					+ "case 1://ADD\n"
					+ "return in1+in2;\n"
					+ "case 2://SUB\n"
					+ "return in1-in2;\n"
					+ "case 3://MULT\n"
					+ "return in1*in2;\n"
					+ "case 4://DIVIDE\n"
					+ "if(in2==0)return 0;\n"
					+ "return in1/in2;\n"
					+ "case 5://ADD CONST\n"
					+ "return in1+1;\n"
					+ "case 6://SUB CONST\n"
					+ "return in1-1;\n"
					+ "case 7://DIV CONST\n"
					+ "return in1/2;\n"
					+ "case 8://SQRT\n"
					+ "return Math.sqrt(in1);\n"
					+ "case 9://SQUARE\n"
					+ "double sqrt = Math.pow(in1,2);\n"
					+ "if(Double.isNaN(sqrt))return 1;\n"
					+ "return sqrt;\n"
					+ "case 10://COS\n"
					+ "return Math.cos(in1);\n"
					+ "case 11://SIN\n"
					+ "return Math.sin(in1);\n"
					+ "case 12://NOP\n"
					+ "return in1;\n"
					+ "case 13://CONST\n"
					+ "return 1;\n"
					+ "case 14://ABS\n"
					+ "return Math.abs(in1);\n"
					+ "case 15://MIN\n"
					+ "return Math.min(in1,in2);\n"
					+ "case 16://MAX\n"
					+ "return Math.max(in1,in2);\n"
					+ "case 17://LOG2\n"
					+ "return (Math.log(in1) / Math.log(2));\n"
					+ "case 18://ROUND\n"
					+ "return (Math.round(in1));\n"
					+ "case 19://FRAC\n"
					+ "return (in1%1);\n"
					+ "case 20://RECIPRICAL\n"
					+ "return (1/in1);\n"
					+ "case 21://RSQRT\n"
					+ "return (1/Math.sqrt(in1));\n"
					+ "default:\n"
					+ "throw new IllegalArgumentException(\"Function is not known\");\n"
					+ "}";
		}
		else{
			throw new Exception("unknown function Set to generate Code for");
		}

	MethodSpec function = MethodSpec.methodBuilder("function")
				.addModifiers(Modifier.PRIVATE, Modifier.STATIC)
				.returns(double.class)
				.addParameter(double.class, "in1")
				.addParameter(double.class, "in2")
				.addParameter(int.class, "functionNr")
				.addCode(codeBlock)
				.build();return function;
}}
