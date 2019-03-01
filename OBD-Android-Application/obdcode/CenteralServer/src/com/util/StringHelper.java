package com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 
 * @author user
 */
public class StringHelper {

	public static StringBuffer readURLContent(String url) {
		System.out.println("URL " + url);
		StringBuffer json = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new URL(url).openStream()));
			String line;
			while ((line = in.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	public static double round(double d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Double.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	public static String n2s(Object d) {
		String dual = "";
		if (d == null) {
			dual = "";
		} else
			dual = d.toString().trim();

		return dual;
	}
	public static long n2long(Object d) {
		long dual = 0;
		if (d == null) {
			dual = 0;
		} else
			dual = new Long(d.toString().trim()).longValue();

		return dual;
	}
	public static ArrayList n2l(Object d) {
		ArrayList dual = null;
		if (d == null) {
			dual = new ArrayList();
		} else
			dual = (ArrayList) d;

		return dual;
	}

	public static String nullObjectToStringEmpty(Object d) {
		String dual = "";
		if (d == null) {
			dual = "";
		} else
			dual = d.toString().trim();

		return dual;
	}

	public static float nullObjectToFloatEmpty(Object d) {
		float i = 0;
		if (d != null) {
			String dual = d.toString().trim();
			try {
				i = new Float(dual).floatValue();
			} catch (Exception e) {
				System.out.println("Unable to find float value");
			}
		}
		return i;
	}
	public static float n2f(Object d) {
		float i = 0;
		if (d != null) {
			String dual = d.toString().trim();
			try {
				i = new Float(dual).floatValue();
			} catch (Exception e) {
				System.out.println("Unable to find float value");
			}
		}
		return i;
	}
	public static double n2d(Object d) {
		double i = 0;
		if (d != null) {
			String dual = d.toString().trim();
			try {
				i = new Double(dual).doubleValue();
			} catch (Exception e) {
				System.out.println("Unable to find double value");
			}
		}
		return i;
	}

	public static HashMap displayRequest(ServletRequest request) {

		Enumeration paraNames = request.getParameterNames();

		System.out.println(" ------------------------------");

		System.out.println("parameters:");

		HashMap parameters = new HashMap();

		String pname;

		String pvalue;

		while (paraNames.hasMoreElements()) {

			pname = (String) paraNames.nextElement();

			pvalue = request.getParameter(pname);

			System.out.println(pname + " = " + pvalue + "");

			parameters.put(pname, pvalue);
		}

		System.out.println(" ------------------------------");
		return parameters;
	}

	public static int nullObjectToIntegerEmpty(Object d) {
		int i = 0;
		if (d != null) {
			String dual = d.toString().trim();
			try {
				i = new Integer(dual).intValue();
			} catch (Exception e) {
				System.out.println("Unable to find integer value");
			}
		}
		return i;
	}
	public static int n2i(Object d) {
		int i = 0;
		if (d != null) {
			String dual = d.toString().trim();
			try {
				i = new Integer(dual).intValue();
			} catch (Exception e) {
				System.out.println("Unable to find integer value");
			}
		}
		return i;
	}
	public static String result[][] = new String[500][];
	public static int count = -1;

	public static boolean checkConnectivityServer(String ip, int port) {
		boolean success = false;
		try {
			Socket soc = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(ip, port);
			soc.connect(socketAddress, 3000);
			System.out.println(socketAddress.toString());
			success = true;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(" Connecting to server " + success);
		return success;

	}

	public static void main(String args[]) {
		connect2Server("http://192.168.0.102:9988/?method%3Dpath%26value%3DD%3A%2FBmonitor+to+dasda+sir%2F");
		// checkConnectivityServer("ADMIN-0D61A3371", 9988);

		// String s=null;
		// s=StringHelper.n2s(s);
		// s=StringHelper.n2s(s);
		// s=StringHelper.n2s(s);
		// s=StringHelper.n2s(s);
		// s=StringHelper.n2s(s);
		// s=StringHelper.n2s(s);
		//
		// System.out.println(s.length());
		//
		// URL u;
		// try {
		// u = new
		// URL("http://192.168.0.101:9988/?method%3Dpath%26value%3DD%3A%2FBmonitor+to+asd+sir%2F");
		// Scanner scanner=new Scanner(u.openStream());
		// while(scanner.hasNext()){
		// String row=scanner.nextLine();
		//
		// String cols[] = row.split(",");
		// for(int i=0;i<cols.length&&cols[i]!=null;i++){
		// cols[i]=cols[i].trim(); System.out.println(cols[i]);}
		// result[++count] = cols;
		//
		// }
		// scanner.close();
		// u=null;
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		//
		//

	}
	public static String getDBDate(long time) {
		Date d=new Date(time);
		//2013-04-05 15:03:23
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String f=dateFormat.format(d);		
		return f;
	}
	public static Date parseDate(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date f=null;
		try {
			f = dateFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}

	public static void connect2Server(String url) {
		System.out.println(new Date());

		URL u;
		try {

			for (int i = 0; i < result.length; i++) {
				result[i] = null;
			}
			u = new URL(url);
			URLConnection uc = u.openConnection();
			uc.setConnectTimeout(5000);

			Scanner scanner = new Scanner(uc.getInputStream());

			while (scanner.hasNext()) {
				String row = StringHelper.n2s(scanner.nextLine());
				if (row.length() > 0) {

					String cols[] = row.split(",");
					for (int i = 0; i < cols.length && cols[i] != null; i++) {
						cols[i] = cols[i].trim();
					}

				}
			}
			scanner.close();
			u = null;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(new Date());

	}
	public static HashMap parseMultipartRequest(HttpServletRequest request) {

		System.out.println("Multipart Parser Start");
		
		HashMap param = new HashMap();
		
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		try {
			String s = URLDecoder.decode(request.getQueryString());
			System.out.println("URL "+s);
			String[] keyval = s.split("&");
			for (int i = 0; i < keyval.length; i++) {
				String kbv = keyval[i];
				String[] tok = kbv.split("=");  
				param.put(tok[0], tok[1]);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(isMultipart);
		String inputFile = "", outputFile = "";
		if (!isMultipart) {
			System.out.println("File Not Uploaded");
		} else {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items = null;
			String uid = "", desc = "";
			try {
				items = upload.parseRequest(request);
				System.out.println("Multipart items: " + items);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				// textbox checkbox
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = StringHelper.n2s(item.getString());
					param.put(name, value);

				} else {
					// file
					String itemName = item.getName();
					param.put(item.getFieldName(), item.getName());
					try {
						param.put(item.getFieldName() + "FILE",
								item.getInputStream());
						param.put(item.getFieldName() +"ITEM", item);

						param.put(item.getFieldName() + "_FILE_CTYPE",
								item.getContentType());
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
		System.out.println("PARAMETER HASHMAP "+param);
		return param;

	}
}
