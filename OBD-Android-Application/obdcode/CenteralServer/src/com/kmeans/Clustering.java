package com.kmeans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.util.DatabaseUtility;
import com.util.SpeedModel;

public class Clustering {
	public Clustering() {
	}

	public static ArrayList<Long> itime = new ArrayList<Long>();
	public static ArrayList<Long> speed = new ArrayList<Long>();// Main
																// array
	Connection con = null;
	ResultSet rs = null;

	public static void getdata() {
		String query = "SELECT o.vss as speed,o.`time`+0 as itime,o.obdid FROM `obdserver` o where o.vss > 10 and o.vehicleid in (SELECT vehicleid FROM `vehicles` v,`useraccounts` u  group by vehicleid) and date_format(o.`time`,'%c-%Y') like '%-%' group by o.obdid";
		List list = DatabaseUtility.getBeanList(SpeedModel.class, query);
		for (int i = 0; i < list.size(); i++) {
			SpeedModel rm = (SpeedModel) list.get(i);
			itime.add(rm.getItime());
			speed.add(rm.getSpeed());
			System.out.println("itime is " + rm.getItime() + " and "
					+ rm.getSpeed());
		}
	}

	public static HashMap cluster() {
		HashMap<String, ArrayList<Long>> hm = new HashMap<String, ArrayList<Long>>();
		long m1, m2, m3;
		int i;
		long d1 = 0;
		long d2 = 0;
		long d3 = 0;
		int a = 1;
		int b = 1;
		int c = 1;
		long c11[] = new long[40];
		long c12[] = new long[40];// Cluster 1
		long c21[] = new long[40];
		long c22[] = new long[40];// Cluster 2
		long c31[] = new long[40];
		long c32[] = new long[40];// Cluster 3

		c11[0] = itime.get(0);
		c12[0] = speed.get(0); // Randomly place one item in each cluster
		c21[0] = itime.get(1);
		c22[0] = speed.get(1);
		c31[0] = itime.get(2);
		c32[0] = speed.get(2);

		m1 = c12[0];
		m2 = c22[0];
		m3 = c32[0];// Initial Mean value of each cluster

		for (i = 3; i < 20; i++) {
			d1 = Math.abs(m1 - speed.get(i));
			d2 = Math.abs(m2 - speed.get(i));
			d3 = Math.abs(m3 - speed.get(i));

			if (d1 <= d2 && d1 <= d3) {
				c11[a] = itime.get(i);
				c12[a] = speed.get(i);
				m1 = (c12[a] + m1) / 2;
				a++;
			}

			if (d2 <= d1 && d2 <= d3) {
				c21[b] = itime.get(i);
				c22[b] = speed.get(i);
				m2 = (c22[b] + m2) / 2;
				b++;
			}

			if (d3 <= d1 && d3 <= d2) {
				c31[c] = itime.get(i);
				c32[c] = speed.get(i);
				m3 = (c32[c] + m3) / 2;
				c++;
			}

		}// end of for...

		System.out
				.println("\n Data is classified into 3 clusters as follows..");
		System.out.println("\nCluster 1");
		System.out.println("----------");
		System.out.println("\nTime Speed\n");
		ArrayList<Long> firstList = new ArrayList<Long>();
		for (i = 0; i < 40; i++) {
			if (c12[i] == 0) {
				break;
			}
			if (c12[i] > 20 && c12[i] <= 40) {
				firstList.add(c11[i]);
				System.out.print(c11[i]);
				System.out.print(" ");
				System.out.print(c12[i]);
				System.out.println("");
			}
			// System.out.print(c11[i]);
			// firstList.add(c11[i]);
			// System.out.print(" ");
			// System.out.print(c12[i]);
			// System.out.println("");
		}

		System.out.println("\nCluster 2");
		System.out.println("----------");
		System.out.println("\nTime Speed\n");
		ArrayList<Long> secondList = new ArrayList<Long>();
		for (i = 0; i < 40; i++) {
			if (c22[i] == 0) {
				break;
			}
			if (c22[i] >= 0 && c22[i] <= 20) {
				secondList.add(c21[i]);
				System.out.print(c21[i]);
				System.out.print(" ");
				System.out.print(c22[i]);
				System.out.println("");
			}
		}

		System.out.println("\nCluster 3");
		System.out.println("----------");
		System.out.println("\nTime Speed\n");
		ArrayList<Long> thirdList = new ArrayList<Long>();
		for (i = 0; i < 40; i++) {
			if (c32[i] == 0) {
				break;
			}
			if (c32[i] > 40) {
				thirdList.add(c31[i]);
				System.out.print(c31[i]);
				System.out.print(" ");
				System.out.print(c32[i]);
				System.out.println("");
			}
		}
		hm.put("clusterone", firstList);
		hm.put("clustertwo", secondList);
		hm.put("clusterthree", thirdList);
		return hm;
	}// end of function

	public static HashMap cluster(ArrayList<Long> itime, ArrayList<Long> speed) {
		HashMap<String, ArrayList<Long>> hm = new HashMap<String, ArrayList<Long>>();
		long m1, m2, m3;
		int i;
		long d1 = 0;
		long d2 = 0;
		long d3 = 0;
		int a = 1;
		int b = 1;
		int c = 1;
		long c11[] = new long[40];
		long c12[] = new long[40];// Cluster 1
		long c21[] = new long[40];
		long c22[] = new long[40];// Cluster 2
		long c31[] = new long[40];
		long c32[] = new long[40];// Cluster 3

		c11[0] = itime.get(0);
		c12[0] = speed.get(0); // Randomly place one item in each cluster
		c21[0] = itime.get(1);
		c22[0] = speed.get(1);
		c31[0] = itime.get(2);
		c32[0] = speed.get(2);

		m1 = c12[0];
		m2 = c22[0];
		m3 = c32[0];// Initial Mean value of each cluster

		for (i = 3; i < 20; i++) {
			d1 = Math.abs(m1 - speed.get(i));
			d2 = Math.abs(m2 - speed.get(i));
			d3 = Math.abs(m3 - speed.get(i));

			if (d1 <= d2 && d1 <= d3) {
				c11[a] = itime.get(i);
				c12[a] = speed.get(i);
				m1 = (c12[a] + m1) / 2;
				a++;
			}

			if (d2 <= d1 && d2 <= d3) {
				c21[b] = itime.get(i);
				c22[b] = speed.get(i);
				m2 = (c22[b] + m2) / 2;
				b++;
			}

			if (d3 <= d1 && d3 <= d2) {
				c31[c] = itime.get(i);
				c32[c] = speed.get(i);
				m3 = (c32[c] + m3) / 2;
				c++;
			}

		}// end of for...

		System.out
				.println("\n Data is classified into 3 clusters as follows..");
		System.out.println("\nCluster 1");
		System.out.println("----------");
		System.out.println("\nTime Speed\n");
		ArrayList<Long> firstList = new ArrayList<Long>();
		for (i = 0; i < 40; i++) {
			if (c12[i] == 0) {
				break;
			}
			if (c12[i] > 20 && c12[i] <= 40) {
				firstList.add(c11[i]);
				System.out.print(c11[i]);
				System.out.print(" ");
				System.out.print(c12[i]);
				System.out.println("");
			}
			// System.out.print(c11[i]);
			// firstList.add(c11[i]);
			// System.out.print(" ");
			// System.out.print(c12[i]);
			// System.out.println("");
		}

		System.out.println("\nCluster 2");
		System.out.println("----------");
		System.out.println("\nTime Speed\n");
		ArrayList<Long> secondList = new ArrayList<Long>();
		for (i = 0; i < 40; i++) {
			if (c22[i] == 0) {
				break;
			}
			if (c22[i] >= 0 && c22[i] <= 20) {
				secondList.add(c21[i]);
				System.out.print(c21[i]);
				System.out.print(" ");
				System.out.print(c22[i]);
				System.out.println("");
			}
		}

		System.out.println("\nCluster 3");
		System.out.println("----------");
		System.out.println("\nTime Speed\n");
		ArrayList<Long> thirdList = new ArrayList<Long>();
		for (i = 0; i < 40; i++) {
			if (c32[i] == 0) {
				break;
			}
			if (c32[i] > 40) {
				thirdList.add(c31[i]);
				System.out.print(c31[i]);
				System.out.print(" ");
				System.out.print(c32[i]);
				System.out.println("");
			}
		}
		hm.put("clusterone", firstList);
		hm.put("clustertwo", secondList);
		hm.put("clusterthree", thirdList);
		return hm;
	}// end of function

	public static void main(String args[]) {
		Clustering t = new Clustering();
		t.getdata();
		t.cluster();
	}// end of main

}// end of class
