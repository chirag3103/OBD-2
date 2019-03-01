package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public class DataImporter {
	public static void main(String[] args) {
		File f=new File("D:\\work\\setup\\Eclipse-JEE-Indigo-SR1-win32\\eclipse-jee-indigo-SR1-win32\\eclipse\\obd_11-3-16.txt");
		uploadData(f);
	}
	public static String uploadData(File f){
		
	StringBuffer sv=new StringBuffer();
		//Construct BufferedReader from InputStreamReader
		BufferedReader br=null;
		String line = null;
		ArrayList list=new ArrayList();
		try {
			int dup=0;
			int total=0;
			FileInputStream fis = new FileInputStream(f);
			 br = new BufferedReader(new InputStreamReader(fis));
			 br.readLine();	//Skip first line
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			String str[]=line.split(",");
			String imei=StringHelper.n2s(str[7]);
			if(imei.length()==0){
				continue;
			}
			OwnerInfoModel oi  = ConnectionManager.getOwnerId(str[7]);
			if(oi==null){
				String ownerId=ConnectionManager.getMaxValue("SELECT ownerId FROM vehicles where imei like '"+str[7]+"'");
				if(ownerId.length()>0){
					System.err.println("Owner ID "+ownerId+" for driver with IMEI does not exist ==>"+str[7]);
					sv.append("Owner ID "+ownerId+" for driver with IMEI does not exist ==>"+str[7]);
				}else{
					System.err.println("Driver Does not exists ==>"+str[7]);
					sv.append("Driver Does not exists ==>"+str[7]);
				}
				
				return sv.toString(); 
			}
			String timeString=str[10];
			long time= 0;
			String dt="";
			if(timeString.indexOf("-")!=-1){
				Date date=StringHelper.parseDate(timeString);
				time=date.getTime();
				dt=timeString;
			}else{
				time= StringHelper.n2long( str[10]);
			
				dt=StringHelper.getDBDate(time);
			}
			for (int i = 0; i < str.length; i++) {
				System.out.println(i+" "+str[i]);
			}
			String query = "INSERT INTO obdserver (load_pct, temp, rpm, vss, iat, maf, throttlepos, vehicleId, latsend, lngsend,timeLong,`time`)  values(?,?,?,?,?,?,?,?,?,?,?,?)";
			int i = ConnectionManager.executeUpdate(query, new Object[] { str[1],str[6], str[5], str[4], str[0], str[2], str[3], oi.getVehicleId(), StringHelper.n2d(str[8]), StringHelper.n2d(str[9]) ,StringHelper.n2long(time),dt});
	
			if(i>0){
				System.out.println("Data Inserted");
				
			}else{
				System.out.println("Data Duplicate");
				dup++;
			}
			total++;
			//System.out.println("Length"+str.length);
			}
		
			//	0	1		 2	 3			 4	 5	 6	  7	   8   9   10
			//	iat,load_pct,maf,throttlepos,vss,rpm,temp,imei,lat,lng,time
			
			br.close();
			sv.append("Records Inserted/Duplicate Records "+total+"/"+dup);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	return sv.toString();
	
	}
	
}
