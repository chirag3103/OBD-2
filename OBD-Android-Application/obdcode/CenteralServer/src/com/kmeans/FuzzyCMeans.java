/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmeans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.util.StringHelper;

/**
 *
 * @author technowings-pc
 */
public class FuzzyCMeans {

    public static final int MAX_DATA_POINTS = 10000;
    public static final int MAX_CLUSTER = 100;
    public static final int MAX_DATA_DIMENSION = 5;
    public static int num_data_points;
    public static int num_clusters;
    public static int num_dimensions;
    public static double low_high[][] = new double[MAX_DATA_DIMENSION][2];
    public static double degree_of_memb[][] = new double[MAX_DATA_POINTS][MAX_CLUSTER];
    public static double epsilon;
    public static double fuzziness;
    public static double data_point[][] = new double[MAX_DATA_POINTS][MAX_DATA_DIMENSION];
    public static double cluster_centre[][] = new double[MAX_CLUSTER][MAX_DATA_DIMENSION];
//num_data_points num_clusters num_dimensions fuzziness epsilon

   public static double[] readFile(String fname){
       try {
           File f = new File(fname);
           System.out.println(" Absolute Path " + f.getCanonicalPath());
           StringBuffer sb = FileHelper.getFileContent(f.getAbsolutePath());
           String[] tokens = sb.toString().split("\\s+");
           double[] a = new double[tokens.length];
           for (int i = 0; i < tokens.length; i++) {
               String string = tokens[i];
               a[i] = StringHelper.n2d(string);
           }
           return a;
       } catch (IOException ex) {
           ex.printStackTrace();
       }
       return null;
       
   }
   public static  int init(String fname) {
        int i, j, r, rval;
        double[] fileData = readFile(fname);
        double s;
        num_data_points = (int) fileData[0];
        num_clusters = (int) fileData[1];
        num_dimensions = (int) fileData[2];
        fuzziness = fileData[3];
        epsilon = fileData[4];

        if (num_clusters > MAX_CLUSTER) {
            System.out.println("Number of clusters should be < " + MAX_CLUSTER + "\n");
            return -1;
        }
        if (num_data_points > MAX_DATA_POINTS) {
            System.out.println("Number of data points should be < " + MAX_DATA_POINTS + "\n");
            return -1;
        }
        if (num_dimensions > MAX_DATA_DIMENSION) {
            System.out.println("Number of dimensions should be >= 1.0 and < %d" + MAX_DATA_DIMENSION + "\n");
            return -1;
        }

        if (fuzziness <= 1.0) {
            System.out.println("Fuzzyness coefficient should be > 1.0\n");
            return -1;
        }

        if (epsilon <= 0.0 || epsilon > 1.0) {
            System.out.println("Termination criterion should be > 0.0 and <= 1.0\n");
            return -1;
        }
        // Fill Data Points From File
        for (i = 0; i < num_data_points; i++) {
            for (j = 0; j < num_dimensions; j++) {
                data_point[i][j] = fileData[5 + i * num_dimensions + j];
                if (data_point[i][j] < low_high[j][0]) {
                    low_high[j][0] = data_point[i][j];
                }
                if (data_point[i][j] > low_high[j][1]) {
                    low_high[j][1] = data_point[i][j];
                }
            }
        }
        for (i = 0; i < num_data_points; i++) {
            s = 0.0;
            r = 100;
            for (j = 1; j < num_clusters; j++) {
                //   rval = rand() % (r + 1);
                rval = (int) ((Math.random() * 32767) % (r + 1));
                r -= rval;
                degree_of_memb[i][j] = rval / 100.0;
                s += degree_of_memb[i][j];
            }
            degree_of_memb[i][0] = 1.0 - s;
        }

        return 0;

    }

  public static   int calculate_centre_vectors() {
        int i, j, k;
        double numerator, denominator;
        double t[][]=new double[MAX_DATA_POINTS][MAX_CLUSTER];
    for (i = 0; i < num_data_points; i++) {
            for (j = 0; j < num_clusters; j++) {
                t[i][j] =Math.pow(degree_of_memb[i][j], fuzziness);
            }
        }
        for (j = 0; j < num_clusters; j++) {
            for (k = 0; k < num_dimensions; k++) {
                numerator = 0.0;
                denominator = 0.0;
                for (i = 0; i < num_data_points; i++) {
                    numerator += t[i][j] * data_point[i][k];
                    denominator += t[i][j];
                }
                cluster_centre[j][k] = numerator / denominator;
            }
        }
        return 0;
    }

   public static  double get_norm(int i, int j) {
        int k;
        double sum = 0.0;
        for (k = 0; k < num_dimensions; k++) {
            sum += Math.pow(data_point[i][k] - cluster_centre[j][k], 2);
        }
        return Math.sqrt(sum);
    }

 public static    double get_new_value(int i, int j) {
        int k;
        double t, p, sum;
        sum = 0.0;
        p = 2 / (fuzziness - 1);
        for (k = 0; k < num_clusters; k++) {
            t = get_norm(i, j) / get_norm(i, k);
            t = Math.pow(t, p);
            sum += t;
        }
        return 1.0 / sum;
    }

   public static  double update_degree_of_membership() {
        int i, j;
        double new_uij;
        double max_diff = 0.0, diff;
        for (j = 0; j < num_clusters; j++) {
            for (i = 0; i < num_data_points; i++) {
                new_uij = get_new_value(i, j);
                diff = new_uij - degree_of_memb[i][j];
                if (diff > max_diff) {
                    max_diff = diff;
                }
                degree_of_memb[i][j] = new_uij;
            }
        }
        return max_diff;
    }

    public static int fcm(String fname) {
        double max_diff;
        init(fname);
        do {
            calculate_centre_vectors();
            max_diff = update_degree_of_membership();
        } while (max_diff > epsilon);
        return 0;
    }

//    int gnuplot_membership_matrix() {
//        int i, j, cluster;
//        char fname[]=new char[100];
//    double highest;
//    FILE * f[MAX_CLUSTER];
//        if (num_dimensions != 2) {
//            System.out.println("Plotting the cluster only works when the\n");
//            System.out.println("number of dimensions is two. This will create\n");
//            System.out.println("a two-dimensional plot of the cluster points.\n");
//            exit(1);
//        }
//        for (j = 0; j < num_clusters; j++) {
//            System.out.println(fname, "cluster.%d", j);
//            if ((f[j] = fopen(fname, "w")) == NULL) {
//                System.out.println("Could not create %s\n", fname);
//                for (i = 0; i < j; i++) {
//                    fclose(f[i]);
//                    sSystem.out.println(fname, "cluster.%d", i);
//                    remove(fname);
//                }
//                return -1;
//            }
//            fSystem.out.println(f[j], "#Data points for cluster: %d\n", j);
//        }
//        for (i = 0; i < num_data_points; i++) {
//            cluster = 0;
//            highest = 0.0;
//            for (j = 0; j < num_clusters; j++) {
//                if (degree_of_memb[i][j] > highest) {
//                    highest = degree_of_memb[i][j];
//                    cluster = j;
//                }
//            }
//            fSystem.out.println(f[cluster], "%lf %lf\n", data_point[i][0], data_point[i][1]);
//        }
//        for (j = 0; j < num_clusters; j++) {
//            fclose(f[j]);
//        }
//        if ((f[0] = fopen("gnuplot.script", "w")) == NULL) {
//            System.out.println("Could not create gnuplot.script.\n");
//            for (i = 0; i < j; i++) {
//                fclose(f[i]);
//                sSystem.out.println(fname, "cluster.%d", i);
//                remove(fname);
//            }
//            return -1;
//        }
//        fSystem.out.println(f[0], "set terminal png medium\n");
//        fSystem.out.println(f[0], "set output \"cluster_plot.png\"\n");
//        fSystem.out.println(f[0], "set title \"FCM clustering\"\n");
//        fSystem.out.println(f[0], "set xlabel \"x-coordinate\"\n");
//        fSystem.out.println(f[0], "set ylabel \"y-coordinate\"\n");
//        fSystem.out.println(f[0], "set xrange [%lf : %lf]\n", low_high[0][0], low_high[0][1]);
//        fSystem.out.println(f[0], "set yrange [%lf : %lf]\n", low_high[1][0], low_high[1][1]);
//        fSystem.out.println(f[0],
//                "plot 'cluster.0' using 1:2 with points pt 7 ps 1 lc 1 notitle");
//        for (j = 1; j < num_clusters; j++) {
//            sSystem.out.println(fname, "cluster.%d", j);
//            fSystem.out.println(f[0],
//                    ",\\\n'%s' using 1:2 with points  pt 7 ps 1 lc %d notitle",
//                    fname, j + 1);
//        }
//        fSystem.out.println(f[0], "\n");
//        fclose(f[0]);
//        return 0;
//    }

    void print_data_points(String fileName) {
    int i, j;
      double[] fileData = readFile(fileName);
        double s;
        num_data_points = (int) fileData[0];
        num_clusters = (int) fileData[1];
        num_dimensions = (int) fileData[2];
        fuzziness = fileData[3];
        epsilon = fileData[4];

      
        
      for (i = 0; i < num_data_points; i++) {
            for (j = 0; j < num_dimensions; j++) {
                data_point[i][j] = fileData[5 + i * num_dimensions + j];
                System.out.println("data_point["+i+"]["+j+"] "+data_point[i][j]);
            }
        }
    }

   public static void print_membership_matrix(String fname) {
    int i, j;

        
        for (i = 0; i < num_data_points; i++) {
          
            for (j = 0; j < num_clusters; j++) {
               System.out.println(degree_of_memb[i][j]);
            }
            System.out.println();
        }
      
    }
   public static void main(String[] args) {
		int numbOfClusters=3;
		int[][] datapoints= new int[][] { {1, 1, 17 },
				{ 2,3, 10 }, {3,15, 15 }, { 4, 1, 18 }, { 5, 30, 17 },
				{ 6, 1, 7 }, { 7, 1, 7 }, { 8, 2, 13 }, { 9, 1, 10 } };
	   try {
		applyCMeans(numbOfClusters, datapoints);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public static double[][] applyCMeans(int numbOfClusters,int[][] datapoints) throws Exception{
	
	
//	int numbOfClusters=3;
//	int[][] datapoints= new int[][] { { 1, 17 },
//			{ 2, 10 }, {  15, 15 }, { 4, 1, 18 }, { 5, 30, 17 },
//			{ 6, 1, 7 }, { 7, 1, 7 }, { 8, 2, 13 }, { 9, 1, 10 } };
	File f=new File("cmeansinput.txt");
	System.out.println(f.getCanonicalPath());
	
	FileOutputStream fos=new FileOutputStream(f);
	int NO_OF_DATAPOINTS=datapoints.length;
	int NUMBER_CLUSTERS=numbOfClusters;
	int INPUT_DIMENSION=datapoints[0].length;
	fos.write((NO_OF_DATAPOINTS+" "+NUMBER_CLUSTERS+" "+INPUT_DIMENSION+"\n").getBytes());
	fos.write("2.0 0.00005\n".getBytes());
	for (int i = 0; i < datapoints.length; i++) {
		for (int j = 0; j < datapoints[i].length; j++) {
			fos.write((datapoints[i][j]+" ").getBytes());	
		}
		
	}
	fos.close();
	parseCMeansInputFile(new String[]{f.getAbsolutePath()});
	return degree_of_memb;		// Degree of membership of each point so for 3 input and 3 cluster 
}
    public static void parseCMeansInputFile(String[] args) {


        System.out.println("------------------------------------------------------------------------\n");
//        if (args.length != 2) {
//            System.out.println("USAGE: fcm <input file>\n");
//            System.exit(1);
//        }
        args=new String[]{args[0]};
        fcm(args[0]);
        System.out.println("Number of data points: "+ num_data_points+"\n");
        System.out.println("Number of clusters: "+ num_clusters+"\n");
        System.out.println("Number of data-point dimensions: \n"+ num_dimensions+"\n");
        System.out.println("Accuracy margin: "+ epsilon+"\n");
        print_membership_matrix("");
//        print_membership_matrix("membership.matrix");
//        gnuplot_membership_matrix();
        System.out.println("------------------------------------------------------------------------\n");
        System.out.println("The program run was successful...\n");
        System.out.println("Storing membership matrix in file 'membership.matrix'\n\n");
        System.out.println("If the points are on a plane (2 dimensions)\n");
        System.out.println("the gnuplot script was generated in file 'gnuplot.script', and\n");
        System.out.println("the gnuplot data in files cluster.[0]... \n\n");
        System.out.println("Process 'gnuplot.script' to generate graph: 'cluster_plot.png'\n\n");
        System.out.println("NOTE: While generating the gnuplot data, for each of the data points\n");
        System.out.println("the corresponding cluster is the one which has the highest\n");
        System.out.println("degree-of-membership as found in 'membership.matrix'.\n");
        System.out.println("------------------------------------------------------------------------\n");
      
    }
}
