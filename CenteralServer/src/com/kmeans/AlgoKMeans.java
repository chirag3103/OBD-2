package com.kmeans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.sun.xml.internal.ws.message.StringHeader;
import com.util.DBUtils;
import com.util.SpeedModel;
import com.util.StringHelper;

/**
 * 
 * The K-means algorithm steps are (text from Wikipedia) : 1) Choose the number
 * of clusters,k. * 2) Randomly generate k clusters and determine the cluster
 * centers, or directly generate k random points as cluster centers. 3) Assign
 * each point to the nearest cluster center. 4)Recompute the new cluster
 * centers. 5) Repeat the two previous steps until some convergence criterion is
 * met (usually that the assignment hasn't changed).
 * 
 */

public class AlgoKMeans {

	// The list of clusters generated
	private List<Cluster> clusters = null;

	// A random number generator because K-Means is a randomized algorithm
	private final static Random random = new Random(System.currentTimeMillis());

	// For statistics
	private long startTimestamp; // the start time of the latest execution
	private long endTimestamp; // the end time of the latest execution
	private long iterationCount; // the number of iterations that was performed

	/**
	 * Default constructor
	 */
	public AlgoKMeans() {

	}

	/**
	 * Run the K-Means algorithm
	 * 
	 * @param inputFile
	 *            an input file path containing a list of vectors of double
	 *            values
	 * @param k
	 *            the parameter k
	 * @return a list of clusters (some of them may be empty)
	 * @throws IOException
	 *             exception if an error while writing the file occurs
	 */
	public List<Cluster> runAlgorithm(List<SpeedModel> list, int k)
			throws NumberFormatException, IOException {
		// record the start time
		startTimestamp = System.currentTimeMillis();
		// reset the number of iterations
		iterationCount = 0;

		// Structure to store the vectors from the file
		List<DoubleArray> vectors = new ArrayList<DoubleArray>();

		// variables to store the minimum and maximum values in vectors
		double minValue = Integer.MAX_VALUE;
		double maxValue = 0;

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			double[] vector = new double[1];
			SpeedModel sm = (SpeedModel) iterator.next();
			double value = StringHelper.n2d(sm.getSpeed());
			// check if it is the min or max
			if (value < minValue) {
				minValue = value;
			}
			if (value > maxValue) {
				maxValue = value;
			}
			// add the value to the current vector
			vector[0] = value;
			// add the vector to the list of vectors
			vectors.add(new DoubleArray(vector));
		}

		// Create a list of clusters
		clusters = new ArrayList<Cluster>();

		// Get the size of vectors
		int vectorsSize = vectors.get(0).data.length;

		// SPECIAL CASE: If only one vector
		if (vectors.size() == 1) {
			// Create a single cluster and return it
			DoubleArray vector = vectors.get(0);
			Cluster cluster = new Cluster(vectorsSize);
			cluster.addVector(vector);
			clusters.add(cluster);
			return clusters;
		}

		// (1) Randomly generate k empty clusters with a random mean (cluster
		// center)
		for (int i = 0; i < k; i++) {
			DoubleArray meanVector = generateRandomVector(minValue, maxValue,
					vectorsSize);
			Cluster cluster = new Cluster(vectorsSize);
			cluster.setMean(meanVector);
			clusters.add(cluster);
		}

		// (2) Repeat the two next steps until the assignment hasn't changed
		boolean changed;
		while (true) {
			iterationCount++;
			changed = false;
			// (2.1) Assign each point to the nearest cluster center.

			// / for each vector
			for (DoubleArray vector : vectors) {
				// find the nearest cluster and the cluster containing the item
				Cluster nearestCluster = null;
				Cluster containingCluster = null;
				double distanceToNearestCluster = Double.MAX_VALUE;

				// for each cluster
				for (Cluster cluster : clusters) {
					// calculate the distance of the cluster mean to the vector
					double distance = euclideanDistance(cluster.getmean(),
							vector);
					// if it is the smallest distance until now, record this
					// cluster
					// and the distance
					if (distance < distanceToNearestCluster) {
						nearestCluster = cluster;
						distanceToNearestCluster = distance;
					}
					// if the cluster contain the vector already,
					// remember that too!
					if (cluster.contains(vector)) {
						containingCluster = cluster;
					}
				}

				// if the nearest cluster is not the cluster containing
				// the vector
				if (containingCluster != nearestCluster) {
					// remove the vector from the containing cluster
					if (containingCluster != null) {
						containingCluster.remove(vector);
					}
					// add the vector to the nearest cluster
					nearestCluster.addVector(vector);
					changed = true;
				}
			}

			// check the memory usage
			MemoryLogger.getInstance().checkMemory();

			if (!changed) { // exit condition for main loop
				break;
			}

			// (2.2) Recompute the new cluster means
			for (Cluster cluster : clusters) {
				cluster.recomputeClusterMean();
			}
		}

		// check memory usage
		MemoryLogger.getInstance().checkMemory();

		// record end time
		endTimestamp = System.currentTimeMillis();
		System.out.println("Time+++++++"+(endTimestamp-startTimestamp));

		// return the clusters
		return clusters;
	}

	// for getting speed clusters
	public static String getSpeedCluster(List<Cluster> clusters, String style) {

		double minSpeed = 0, maxSpeed = 0;
		if (style.equalsIgnoreCase("less efficient")) {
			minSpeed = 0.0;
			maxSpeed = 30.0;
		} else if (style.equalsIgnoreCase("efficient")) {
			minSpeed = 30.0;
			maxSpeed = 60.0;
		} else if (style.equalsIgnoreCase("rash")) {
			minSpeed = 60.0;
			maxSpeed = 300.0;
		}
		System.out.println("Style: " + style);
		for (Iterator iterator = clusters.iterator(); iterator.hasNext();) {
			Cluster cluster = (Cluster) iterator.next();
			int size = cluster.getVectors().size();
			double minValue = Integer.MAX_VALUE;
			double maxValue = 0;
			String s = "";
			for (int j = 0; j < size; j++) {
				double value = StringHelper.n2d(cluster.getVectors().get(j));
				if (value < minValue) {
					minValue = value;
				}
				if (value > maxValue) {
					maxValue = value;
				}
				// for getting the cluster values
				s = s + "," + StringHelper.n2s(cluster.getVectors().get(j));
			}
			if (s.length() > 0) {
				s = s.substring(1);
				if (maxValue >= minSpeed && maxValue < maxSpeed) {
					System.out.println("Return String"+s);
					return s;
				}
			}
		}
		return "";
	}

	/**
	 * Generate a random vector.
	 * 
	 * @param minValue
	 *            the minimum value allowed
	 * @param maxValue
	 *            the maximum value allowed
	 * @param vectorsSize
	 *            the desired vector size
	 * @return the random vector
	 */
	private DoubleArray generateRandomVector(double minValue, double maxValue,
			int vectorsSize) {
		// create a new vector
		double[] vector = new double[vectorsSize];
		// for each position generate a random number
		for (int i = 0; i < vectorsSize; i++) {
			vector[i] = (random.nextDouble() * (maxValue - minValue))
					+ minValue;
		}
		// return the vector
		return new DoubleArray(vector);
	}

	/**
	 * Calculate the eucledian distance between two vectors of doubles.
	 * 
	 * @param vector1
	 *            the first vector
	 * @param vector2
	 *            the second vector
	 * @return the distance
	 */
	private double euclideanDistance(DoubleArray vector1, DoubleArray vector2) {
		double sum = 0;
		try {
			for (int i = 0; i < vector1.data.length; i++) {
				sum += Math.pow(vector1.data[i] - vector2.data[i], 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Math.sqrt(sum);
	}

	/**
	 * Print statistics of the latest execution to System.out.
	 */
	public void printStatistics() {
		System.out.println("========== KMEANS - STATS ============");
		System.out.println(" Total time ~: " + (endTimestamp - startTimestamp)
				+ " ms");
		System.out.println(" Max memory:"
				+ MemoryLogger.getInstance().getMaxMemory() + " mb ");
		System.out.println(" Iteration count: " + iterationCount);
		System.out.println("=====================================");
	}

	/**
	 * Save the clusters to an output file
	 * 
	 * @param output
	 *            the output file path
	 * @throws IOException
	 *             exception if there is some writing error.
	 */
	public void saveToFile(String output) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		// for each cluster
		for (int i = 0; i < clusters.size(); i++) {
			// if the cluster is not empty
			if (clusters.get(i).getVectors().size() >= 1) {
				// write the cluster
				writer.write(clusters.get(i).toString());
				// if not the last cluster, add a line return
				if (i < clusters.size() - 1) {
					writer.newLine();
				}
			}
		}
		// close the file
		writer.close();
	}
}
