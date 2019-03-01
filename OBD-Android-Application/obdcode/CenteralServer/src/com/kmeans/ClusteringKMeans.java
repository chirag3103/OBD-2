package com.kmeans;

public class ClusteringKMeans {
	public static void main(String[] args) {
		int[] ars = calculateClusters(3, new int[][] { { 1, 1, 17 },
				{ 2, 2, 10 }, { 3, 15, 15 }, { 4, 1, 18 }, { 5, 30, 17 },
				{ 6, 1, 7 }, { 7, 1, 7 }, { 8, 2, 13 }, { 9, 1, 10 } },
				new int[][] { { 5, 10 }, { 20, 15 }, { 30, 20 } });
		for (int i = 0; i < ars.length; i++) {
			System.out.println(ars[i]);

		}
	}

	public static int[] calculateClusters(int iterations, int[][] values,
			int[][] clusters) {
		int[] elementClusters = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			float distance = 0;
			float[] distances = new float[clusters.length];
			int k = 0;
			for (int j2 = 0; j2 < clusters.length; j2++) {
				distance = 0;
				for (int j3 = 0; j3 < clusters[j2].length; j3++) {
					distance += (values[i][j3 + 1] - clusters[j2][j3])
							* (values[i][j3 + 1] - clusters[j2][j3]);
				}
				distances[k] = (float) Math.sqrt(distance);
				k++;
			}
			elementClusters[i] = getMinimumDistance(distances);
		}
		for (int i = 0; i < elementClusters.length; i++) {
			System.out.println(elementClusters[i]);

		}
		return elementClusters;

	}

	public static int getMinimumDistance(float[] distances) {
		float min = 99999999;
		int cluster = -1;
		for (int i = 0; i < distances.length; i++) {
			if (distances[i] < min) {
				min = distances[i];
				cluster = i;
			}
		}
		System.out.println("Distance is " + min + " " + cluster);
		return cluster;
	}

	public static float findEucleadeanDistance(int x, int y) {
		double d = Math.sqrt((x - y) * (x - y));
		return (float) d;

	}
}
