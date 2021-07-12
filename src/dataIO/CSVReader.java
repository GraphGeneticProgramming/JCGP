package dataIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import helperClasses.Digit;

/**
 *  For reading the MNIST-Data in CSV-format
 *  
 *  the MNIST-Data in CSV format was downloaded at: 
 *  https://pjreddie.com/projects/mnist-in-csv/
 *  
 * @author Björn Piepenbrink
 *
 */
public class CSVReader {  
    
	/**
	 * Method for getting the LearnSet of MNIST
	 * @param path The Path where the Set is saved
	 * @return a List of Digits
	 */
	static public Digit[] getLearnSet(String path){
    	return readCVS(false,path);
    }
    
	/**
	 * Method for getting the TestSet of MNIST
	 * @param path The Path where the Set is saved
	 * @return a List of Digits
	 */
    static public Digit[] getTestSet(String path){
    	return readCVS(true,path);
    }
	
    
    /**
     * Method for reading the CVS-MNIST data
     * @param test true: read Test-Set
     * 				false: read Learn-Set
     * @param path The Path where the Set is saved
     * @returna Digit Array of all the read Digits
     */
    static public Digit[] readCVS(boolean test,String path){
    	String csvFile = path;
    	if(test){
    		csvFile += "/mnist_test.csv";
    	}
    	else{
    		csvFile += "/mnist_train.csv";
    	}
        String line = "";
        // use comma as separator
        String cvsSplitBy = ",";
        String[] seperatedString;
        Digit[] digits = new Digit[60000];
        if(test){
        	digits = new Digit[10000];
        }
        int digitnr=0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	//per Line one Digit
            while ((line = br.readLine()) != null) {
                seperatedString = line.split(cvsSplitBy);
                int column=0;
                int row=0;
                int[][] pic = new int[28][28];
                for(int i = 1; i<seperatedString.length; i++){
                	pic[row][column] = Integer.parseInt(seperatedString[i]);
                		
                	column++;
                	if(column==28){
                		column=0;
                		row++;
                	}
                }
                int classification = Integer.parseInt(seperatedString[0]);
                Digit digit = new Digit(classification,pic);
                digits[digitnr] = digit;
                digitnr++;
                //System.out.println("Ein Digit hinzugefügt."+digitnr);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //digits enthält jetzt alle Lernbeispiele
        return digits;
    }

}