package dataIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import cartesianNetwork.Individual;

/**
 * for saving and loading Indiciduals if the CGP-net
 * 
 * @author Piepe
 *
 */
public class SaveAndLoadIndividuals {
	
	/**
	 * saves Individual as a txt file
	 * @param indiv the given Individual
	 * @param path The path under which the Individual should be saved
	 * @param fileName the Name of the file without endings
	 */
	public static void saveIndividual(Individual indiv, String path, String fileName) {
		try {
			FileOutputStream f = new FileOutputStream(new File(path + fileName + ".txt"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			o.writeObject(indiv);

			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
			e.printStackTrace();
		}
	}

	/**
	 * saves Individuals as a txt file
	 * @param indiv the given Individuals
	 * @param path The path under which the Individuals should be saved
	 * @param fileName the Name of the file without endings
	 */
	public static void saveIndividual(ArrayList<Individual> indivs, String path, String fileName) {
		for (int i = 0; i < indivs.size(); i++) {
			saveIndividual(indivs.get(i), path, fileName + "_" + i);
		}
	}

	/**
	 * Saves the given Text under the given Path and Name
	 * @param text The text that should be saved
	 * @param path The path under which it should be saved
	 * @param fileName The name of the file
	 */
	public static void saveStatistics(String text, String path, String fileName) {
		try {
			FileOutputStream f = new FileOutputStream(new File(path + fileName + ".txt"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			o.writeObject(text);

			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
			e.printStackTrace();
		}
	}

	/**
	 * read a file that contains an individual
	 * 
	 * @param filePath
	 *            the Path of the file + the file name + file ending
	 * @return the Individual read from the file
	 */
	public static Individual readIndividual(String filePath) {
		Individual indiv = null;
		try {
			FileInputStream fi = new FileInputStream(new File(filePath));
			ObjectInputStream oi = new ObjectInputStream(fi);

			indiv = (Individual) oi.readObject();

			oi.close();
			fi.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return indiv;
	}
	
	/**
	 * saves new Individual only if Fitness is smaller or equal than the fitness
	 * of the already saved one
	 * @param indiv  The new Individual
	 * @param path The Path where the best Individual is saved / should be saved
	 * @param fileName  the name of the file it should check and overwrite if necessary
	 * @throws Exception if reading or saving goes wrong
	 */
	public static void saveBestIndividual(Individual indiv, String path, String fileName) throws Exception {
		try {

			Individual best = readIndividual(path + "" + fileName + ".txt");
			if (best == null) {
				saveIndividual(indiv, path, fileName);
				System.out.println("saved Individual with Fitness" + indiv.getFitness());
				return;
			}
			if (indiv.getFitness() <= best.getFitness()) {
				saveIndividual(indiv, path, fileName);
				System.out.println("saved Individual with Fitness" + indiv.getFitness());
			} else {
				System.out.println("saved Individual has Fitness: " + best.getFitness());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			saveIndividual(indiv, path, fileName);
			System.out.println("saved Individual with Fitness" + indiv.getFitness());
		}
	}
}
