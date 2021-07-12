package helperClasses;

/**
 * a Picture of a number (0-9)
 * with a given classification of the number
 * @author Björn Piepenbrink
 *
 */
public class Digit {

	private int classification;
	private int[][] picture;
	
	/**
	 * @param classification the number on the given Picture
	 * @param picture a Picture of a number (0-9) - should be a square
	 * @throws IllegalArgumentException if classification is not in the range 0-9 or if the given Picture is not a square
	 */
	public Digit(int classification, int[][] picture){
		setClassification(classification);
		setPicture(picture);
	}
	
	public int getClassification() {
		return classification;
	}
	public void setClassification(int classification){
		if(classification <0 || classification >=10) throw new IllegalArgumentException();
		this.classification = classification;
	}
	
	public int[][] getPicture() {
		return picture;
	}
	public void setPicture(int[][] picture){
		if(picture.length!=picture[0].length)throw new IllegalArgumentException();
		this.picture = picture;
	}
	
	public String toString(){
		String toReturn="";
		toReturn += "+++\n";
		toReturn += "Number: "+classification+"\n";
		for(int i=0; i< picture.length; i++){
			for(int j= 0; j < picture[0].length; j++){
				toReturn += picture[i][j]+"\t";
			}
			toReturn += "\n";
		}
		toReturn += "+++\n";
		return toReturn;
	}

	public int[] toIntArray(){
		int[] array = new int[picture.length*picture.length];
		int a = 0;
		for(int i=0; i< picture.length; i++){
			for(int j= 0; j < picture[0].length; j++){
				array[a]=picture[i][j];
				a++;
			}
		}
		return array;
	}
	
	/**
	 * reduces size of picture to a quarter
	 * one pixel = arithmetic average of 4 pixels
	 */
	public void makeSmaller(){
		int[][] pic = new int[picture.length/2][picture[0].length/2];
		for(int i=0;i<picture.length/2;i++){
			for(int j=0;j<picture[0].length/2;j++){
				pic[i][j] = calcAverage(picture[i*2][j*2],
						picture[i*2+1][j*2], picture[i*2][j*2+1], picture[i*2+1][j*2+1]);
			}
		}
		this.picture = pic;
	}
	
	/**
	 * makes the image binary
	 * @param treshold if value is higher than 1 , else 0
	 */
	public void makeBinary(int treshold){
		int[][] pic = new int[picture.length][picture[0].length];
		for(int i=0;i<picture.length;i++){
			for(int j=0;j<picture[0].length;j++){
				if(picture[i][j] >= treshold){
					pic[i][j] = 1;
				}
				else{
					pic[i][j] = 0;
				}
			}
		}
		this.picture = pic;
	}
	
	private int calcAverage(int v1, int v2, int v3, int v4){
		int sum = v1+v2+v3+v4;
		return (sum/4);
	}
}
