package com.kmeans;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.util.DBUtils;
import com.util.SpeedModel;
import com.util.StringHelper;

/**
 * Example of how to use the KMEans algorithm, in source code.
 */
public class MainTestKMeans_saveToFile {

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		int k = 3;
		// Apply the algorithm
		AlgoKMeans algoKMeans = new AlgoKMeans(); // we request 3 clusters
		// for getting speed of vehicles from db
		List list = DBUtils.getBeanList(SpeedModel.class,
				"SELECT o.vss as speed FROM `obdserver` o");
		List<Cluster> clusters = algoKMeans.runAlgorithm(list,k);
		// driving styles
		// less efficient
		// efficient
		// rash
		System.out.println("Cluster: "
				+ algoKMeans.getSpeedCluster(clusters, "rash"));

	}
}
