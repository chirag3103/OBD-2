package com.util;

public class ServerConstants {

	// SMS COMPORT Number
	public static int DRIVER_DISTANCE_CHECK = 50; // in KM



	public static final int ROLE_ADMIN = 1;
	public static final int ROLE_USER = 2;

	public static String db_driver = "com.mysql.jdbc.Driver";
//	public static String db_user = "root";
//	public static String db_pwd = "";
//	public static String db_url = "jdbc:mysql://localhost/obdserver";
	
	public static String db_user = "project";
	public static String db_pwd = "project";
	public static String db_url = "jdbc:mysql://node16984-env-9683988.ind-cloud.everdata.com/obdserver";
	
	// Application Header and Logo
	public static final String APPLICATION_HEAD = "On Board Diagnostics Web Server";
	public static final String APPLICATION_LOGO = "Monitoring Inefficient and Unsafe Driving Behaviour";
	public static final String APPLICATION_TITLE = "OBD";

	// Queries
	public static final String QUERY_VIEW_VEHICLES = "SELECT * FROM `vehicles` where ownerId = ?";
	public static final String QUERY_VEHICLE_REPORT_MONTHLY = "SELECT round(o.load_pct,3) as load_pct,round((o.rpm*0.10472*0.30),3) as speed,o.temp,o.rpm,o.iat,round(o.maf,3) as maf,round(o.throttlepos,3)as throttlepos,o.time FROM `obdserver` o where o.vehicleid like ? and date_format(o.time,'%c-%Y') like ?";
	public static final String QUERY_VEHICLE_REPORT_YEARLY = "SELECT round(o.load_pct,3) as load_pct,round((o.rpm*0.10472*0.30),3) as speed,o.temp,o.rpm,o.iat,round(o.maf,3) as maf,round(o.throttlepos,3)as throttlepos,o.time FROM `obdserver` o where o.vehicleid like ? and date_format(o.time,'%Y') like ?";
	public static final String QUERY_VEHICLES_DETAILS = "SELECT * FROM `vehicles` where vehicleId like ?";
	public static final String QUERY_VEHICLES_DETAILS2 = "SELECT * FROM `vehicles`";
	// public static final String QUERY_VEHICLES_DETAILS =
	// "SELECT * FROM `vehicles` ";
	// messages
	public static String MESSAGE_INVALID_USER_CREDENTIALS = "Login Failed ! Invalid User Credentials !";
	public static final String SMS_URL= "http://mobicomm.dove-sms.com/submitsms.jsp?user=Technow&key=b1ca1e9874XX&senderid=TWINGS&category=bulk&accusage=1&from=Technowings&senderid=ALERTS&mobile=";
}
