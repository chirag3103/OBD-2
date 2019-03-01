package com.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import message.Sender;

import com.kmeans.AlgoKMeans;
import com.kmeans.Cluster;
import com.kmeans.ClusteringKMeans;

public class ConnectionManager extends DatabaseUtility {

	public static void main(String[] args) {
		getDBConnection();
		// DataImporter.uploadData();

	}

	public static ArrayList<BehaviourModel> getEfficiencyReport() {
		String q = "Select distinct date_format(`time`, '%b %d %Y') as dt,latsend,lngsend,vehicleid  from obdserver";
		List list = DatabaseUtility.getMapList(q);
		HashMap<String, String> map = new HashMap<String, String>();
		double prev = 0;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			HashMap object = (HashMap) iterator.next();
			double lat = StringHelper.n2d(object.get("latsend"));
			double lng = StringHelper.n2d(object.get("lngsend"));
			String dt = StringHelper.n2s(object.get("dt"));
			int vehicleid = StringHelper.n2i(object.get("vehicleid"));
			if (map.get(dt) != null) {
				String val[] = StringHelper.n2s(map.get(dt)).split(",");

				double plat = StringHelper.n2d(val[0]);
				double plng = StringHelper.n2d(val[1]);
				double distance = distance(lat, lng, plat, plng);
				double lastDistance = StringHelper.n2d(val[2]) + distance;
				map.put(dt, lat + "," + lng + "," + lastDistance + ","
						+ vehicleid);

			} else {
				map.put(dt, lat + "," + lng + ",0" + "," + vehicleid);
			}
		}
		System.out.println(map);

		String inefficient = "SELECT date_format(os.time, '%b %d %Y') as d, concat(count(*),',',avg(os.vss)) as inefficient_rides  FROM obdserver os "
				+ "	where !((os.rpm<'1000' and os.vss<'30') OR (os.rpm<'2000' and os.vss>='30' and os.vss<'60') OR (os.rpm<'3000' and os.vss>='60' and os.vss<'160') "
				+ "	) group by date_format(os.time, '%b %d %Y')";

		HashMap inefficientMap = getDBMap(inefficient);
		System.out.println(inefficientMap);
		String efficient = "SELECT date_format(os.time, '%b %d %Y') as d, concat(count(*),',',avg(os.vss))  as inefficient_rides FROM obdserver os "
				+ "	where ((os.rpm<'1000' and os.vss<'30') OR (os.rpm<'2000' and os.vss>='30' and os.vss<'60') OR (os.rpm<'3000' and os.vss>='60' and os.vss<'160') "
				+ "	) group by date_format(os.time, '%b %d %Y')";

		HashMap efficientMap = getDBMap(efficient);
		String q2 = "SELECT vehicleId,concat(drivername,'-', vehicleno) as v FROM vehicles v";
		HashMap vehicleMap = getDBMap(q2);
		System.out.println(efficientMap);
		Set<String> keys = map.keySet();
		ArrayList<BehaviourModel> arr = new ArrayList<BehaviourModel>();
		System.out.println("Date,Efficient,InEfficient,Distance,VehicleId");
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String countInefficient = StringHelper.n2s(inefficientMap.get(key));
			String countEfficient = StringHelper.n2s(efficientMap.get(key));
			int noOfEfficient = 0, noOfInefficient = 0;
			float avgSpeedEff = 0, avgSpeedInEff = 0;
			try {
				if (countEfficient.length() > 0) {
					noOfEfficient = StringHelper
							.n2i(countEfficient.split(",")[0]);
					avgSpeedEff = StringHelper
							.n2f(countEfficient.split(",")[1]);
				}
				if (countInefficient.length() > 0) {
					noOfInefficient = StringHelper.n2i(countInefficient
							.split(",")[0]);
					avgSpeedInEff = StringHelper.n2f(countInefficient
							.split(",")[1]);
					;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String val[] = StringHelper.n2s(map.get(key)).split(",");
			double lastDistance = StringHelper.n2d(val[2]);
			int vehicleId = StringHelper.n2i(val[3]);

			System.out
					.println(key
							+ ","
							+ ((float) noOfEfficient / (noOfEfficient + noOfInefficient))
							* 100
							+ ","
							+ ((float) noOfInefficient / (noOfEfficient + noOfInefficient))
							* 100 + "," + avgSpeedEff + "," + avgSpeedInEff
							+ "," + lastDistance + "," + vehicleId);
			BehaviourModel bm = new BehaviourModel();
			bm.setAvgSpeedEff((int) avgSpeedEff);
			bm.setAvgSpeedInEff((int) avgSpeedInEff);
			bm.setDistance((float) lastDistance);
			bm.setNoOfEfficient(noOfEfficient);
			bm.setNoOfEfficientPer((int) (((float) noOfEfficient / (noOfEfficient + noOfInefficient)) * 100));
			bm.setNoOfInefficientPer(100 - (int) (((float) noOfEfficient / (noOfEfficient + noOfInefficient)) * 100));
			bm.setVehicleId(vehicleId);
			bm.setDriverName(StringHelper.n2s(vehicleMap.get(vehicleId + "")));
			bm.setDate(key);
			if (bm.getDriverName().length() > 0)
				arr.add(bm);
		}
		int[][] cluster = new int[arr.size()][3];
		int i = 0;
		for (Iterator iterator = arr.iterator(); iterator.hasNext();) {
			BehaviourModel behaviourModel = (BehaviourModel) iterator.next();
			cluster[i][0] = i;
			cluster[i][1] = behaviourModel.getNoOfEfficientPer();
			cluster[i][2] = behaviourModel.getNoOfInefficientPer();
			i++;
		}
		int[] ars = ClusteringKMeans.calculateClusters(3, cluster, new int[][] {
				{ 80, 20 }, { 70, 30 }, { 60, 40 } });	// 0 1 2
		for (int j = 0; j < ars.length; j++) {
			BehaviourModel bm = arr.get(j);
			bm.setCluster(ars[j]);
		}

		return arr;
	}

	// IN KM
	private static double distance(double lat1, double lon1, double lat2,
			double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public static HashMap getDBMap(String query) {

		String success = "";
		Connection conn = null;
		ResultSet rs = null;
		HashMap map = new HashMap();
		try {

			conn = ConnectionManager.getDBConnection();
			rs = conn.createStatement().executeQuery(query);
			System.out.println("Executing " + query);
			while (rs.next()) {
				String key = rs.getString(1);
				String value = rs.getString(2);
				map.put(key, value);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public static void closeConnection(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static String getCombo2(String sql) {
		List list = getMapList(sql);
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			HashMap record = (HashMap) iterator.next();
			String key = StringHelper.n2s(record.get("vid"));
			String value = StringHelper.n2s(record.get("name"));
			sb.append("<option value='" + key + "'>" + value + "</option>");

		}
		return sb.toString();
	}

	public static int index = 0;

	public static String getVehicleList(HashMap param) {
		String str = "";
		String uid = StringHelper.n2s(param.get("userid"));
		String query = "SELECT role FROM `useraccounts` where userid like '"
				+ uid + "'";
		int role = DBUtils.getMaxValue(query);
		if (role == ServerConstants.ROLE_ADMIN) {
			query = "SELECT * FROM `vehicles` ";
		} else {
			query = "SELECT * FROM `vehicles` where ownerid like " + uid;
		}
		System.out.println("Query: " + query);
		return getComboList(query);
	}

	public static String getComboList(String query) {
		String ret = "";
		List stateList = DatabaseUtility.getBeanList(VehicleModel.class, query);
		System.out.println("List in getcombolist " + stateList);
		String s = "Select vehicle No";
		ret += "<OPTION value=''>" + s + "</OPTION>";
		ret += "<option value='all' >All</option>";
		for (int i = 0; stateList.size() > 0 && i < stateList.size(); i++) {
			VehicleModel bm = (VehicleModel) stateList.get(i);
			ret += "<OPTION value='" + bm.getVehicleId() + "'>"
					+ bm.getVehicleno() + "</OPTION>";
		}
		return ret;
	}

	public static OBDModel getOBDList() {
		String query = "SELECT * FROM obd  order by `time` LIMIT " + index
				+ ",1";
		index++;
		System.out.println(query);
		List beans = getBeanList(OBDModel.class, query);
		System.out.println("Beans Size " + beans.size());
		OBDModel o = null;
		if (beans.size() > 0) {
			o = (OBDModel) beans.get(0);
		}
		return o;

	}

	public static List getAllOwners() {
		String query = "SELECT * FROM useraccounts u,vehicles v where v.ownerId=u.userid and u.role="
				+ ServerConstants.ROLE_USER;
		System.out.println(query);
		List lst = getBeanList(OwnerInfoModel.class, query);
		System.out.println("Size " + lst.size());
		return lst;
	}

	public static List getAllOwners(String userId) {
		String query = "SELECT * FROM useraccounts u,vehicles v where v.ownerId=u.userid and u.role="
				+ ServerConstants.ROLE_USER + " and u.userid=" + userId;
		System.out.println(query);
		List lst = getBeanList(OwnerInfoModel.class, query);
		System.out.println("Size " + lst.size());
		return lst;
	}

	public static List getAllUsers() {
		String query = "SELECT * from useraccounts where role="
				+ ServerConstants.ROLE_USER;
		System.out.println(query);
		List lst = getBeanList(UserAccountModel.class, query);
		System.out.println("Size " + lst.size());
		return lst;
	}

	public static AlertModel getAlertInfo(String ownerid, String type) {
		String query = "";
		AlertModel am = null;
		query = "SELECT * FROM alerts a where ownerid='" + ownerid
				+ "' and alerttype like '" + type + "'";
		System.out.println(query);
		List lst = getBeanList(AlertModel.class, query);
		if (lst.size() > 0) {
			am = (AlertModel) lst.get(0);
		}
		return am;
	}

	public static OwnerInfoModel getOwnerInfo(String ownerid) {
		String query = "";
		OwnerInfoModel oi = null;
		query = "SELECT * FROM useraccounts o where userId='" + ownerid + "'";
		System.out.println(query);
		List lst = getBeanList(OwnerInfoModel.class, query);
		if (lst.size() > 0) {
			oi = (OwnerInfoModel) lst.get(0);
		}
		return oi;
	}

	// for getting the vehicle report
	public static List<OBDModel> getVehicleReport(String vehicleno,
			String month, String year) {
		List<OBDModel> list = new ArrayList<OBDModel>();
		System.out.println("Vehicleno: " + vehicleno + " Month: " + month
				+ " Year: " + year);
		if (vehicleno.equalsIgnoreCase("all")) {
			vehicleno = "%";
		}
		if (month.equalsIgnoreCase("all")) {
			month = "%";
		}
		if (year.equalsIgnoreCase("all")) {
			year = "%";
		}
		String query = "";
		if (month != "" && year != "" && vehicleno != "") {
			query = "SELECT round(o.load_pct,3) as load_pct,round((o.rpm*0.10472*0.30),3) as speed,o.vss,o.temp,o.rpm,o.iat,round(o.maf,3) as maf,round(o.throttlepos,3)as throttlepos,o.time FROM `obdserver` o where o.vehicleid like '"
					+ vehicleno
					+ "' and date_format(o.time,'%c-%Y') like '"
					+ month + "-" + year + "'";
			System.out.println("Query: " + query);
			list = DatabaseUtility.getBeanList(com.util.OBDModel.class, query);

		} else if (vehicleno != "" && year != "") {
			query = "SELECT round(o.load_pct,3) as load_pct,round((o.rpm*0.10472*0.30),3) as speed,o.vss,o.temp,o.rpm,o.iat,round(o.maf,3) as maf,round(o.throttlepos,3)as throttlepos,o.time FROM `obdserver` o where o.vehicleid like '"
					+ vehicleno
					+ "' and date_format(o.time,'%Y') like '"
					+ year + "'";
			System.out.println("Query: " + query);
			list = DatabaseUtility.getBeanList(com.util.OBDModel.class, query);
		}
		return list;
	}

	public static String checkSpeedAlert(String ownerid, String vss) {
		String b = "";
		String type = "S";
		String sql = "Select * from alerts o where o.ownerid like '" + ownerid
				+ "' and o.alerttype = '" + type
				+ "' and  cast(o.val as unsigned)  < cast('" + vss
				+ "' as unsigned)";
		List lst = getMapList(sql);
		if (lst.size() > 0) {
			String speed = StringHelper.n2s(((HashMap) lst.get(0)).get("val"));

			b = speed;
		} else {
			return "";
		}
		return b;
	}

	public static boolean checkPositionAlert(String ownerid, String lat,
			String lng) {
		boolean b = false;
		String type = "P";
		String[] arr = Geocoder.getLocation("" + lat + "," + lng);
		String currentLoacation = arr[0] + " " + arr[1];
		System.out.println("currentLoacation " + currentLoacation);
		AlertModel am = ConnectionManager.getAlertInfo(ownerid, type);
		if(am==null){
			return false;
		}
		String defaultplace = am.getVal();
		HashMap map = Geocoder.getDIstanceTimeDetails(defaultplace,
				currentLoacation);
		String i = (String) map.get("DISTANCE");
		System.out.println("Distance " + i);
		double range = ServerConstants.DRIVER_DISTANCE_CHECK;
		Scanner st = new Scanner(i);
		while (!st.hasNextDouble()) {
			st.next();
		}
		double distanceBetween = st.nextDouble();
		System.out.println(distanceBetween);
		if (range < distanceBetween) {
			b = true;
		} else {
			b = false;
		}
		return b;
	}

	public static boolean delVehicle(String ownerid, String vehicleno) {
		boolean b = false;
		String query = "DELETE FROM ownertable WHERE ownerid=? and vehicleno = ?";
		System.out.println(query);
		int i = executeUpdate(query, new Object[] { ownerid, vehicleno });
		if (i > 0) {
			b = true;
		}
		return b;
	}

	public static boolean deleteDriver(String vehicleid) {
		boolean b = false;
		String query = "Delete from vehicles where vehicleId=" + vehicleid;
		System.out.println(query);
		int i = executeUpdate(query);
		if (i > 0) {
			b = true;
		}

		query = "DELETE FROM alerts WHERE vehicleId=?";
		i = executeUpdate(query, new Object[] { vehicleid });
		if (i > 0) {
			b = true;
		}

		return b;
	}

	public static boolean delOwner(String userid) {
		boolean b = false;
		String query = "DELETE FROM useraccounts WHERE userid=?";
		System.out.println(query);
		int i = executeUpdate(query, new Object[] { userid });
		if (i > 0) {
			b = true;
		}

		query = "DELETE FROM vehicles WHERE ownerId=?";
		i = executeUpdate(query, new Object[] { userid });
		if (i > 0) {
			b = true;
		}

		return b;
	}

	public static HashMap map = new HashMap();

	// (load_pct, iat, maf, throttlepos, vss, rpm, temp, latsend, lngsend,oi);
	public static boolean setOBDList(String load_pct, String iat, String maf,
			String throttlepos, String vss, String rpm, String temp,
			String latsend, String lngsend, final OwnerInfoModel oi) {

		final boolean b = true;
		final String ownername = oi.getFullname();
		final String vehicleno = oi.getVehicleno();
		final String alertCheck = ConnectionManager.checkSpeedAlert(
				oi.getOwnerId(), vss);
		final boolean alertPosition = ConnectionManager.checkPositionAlert(
				oi.getOwnerId(), latsend, lngsend);
		final String[] arr = Geocoder.getLocation("" + latsend + "," + lngsend);
		final String location = arr[0] + " " + arr[1];
		new Thread() {
			public void run() {
				try {
					if (alertCheck.length() > 0) {
						String msg = "Hello " + ownername + " , Your Vehicle "
								+ vehicleno + " Has Crossed Speed Limit of "
								+ alertCheck + " Km/Hr. Current Position: "
								+ location;
						System.out.println("SMS To Send " + msg);
						String sms[] = { oi.getPhoneno() };
						for (int i = 0; i < sms.length; i++) {
							System.out.println("Sending SMS to " + sms[i]);
							System.out.println(msg);
							if (map.get(sms[i] + "_" + msg) == null) {
								map.put(sms[i] + "_" + msg, 1);

								message.Sender sender = new Sender(sms[i], msg);
								try {
									sender.send();

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								System.out
										.println(">>>>>>>>>>>>>>>>>>>>>> SAME MESSAGE HAS BEEN SENT >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
							}

						}
					}

					String msg2 = "Hello " + ownername + " " + vehicleno
							+ " Has Crossed Area,Current Address-" + location;
					if (alertPosition) {
						String sms[] = { oi.getPhoneno() };
						for (int i = 0; i < sms.length; i++) {
							System.out.println("Sending SMS to " + sms[i]);
							System.out.println(msg2);
							if (map.get(sms[i] + "_" + msg2) == null) {
								map.put(sms[i] + "_" + msg2, 1);

								message.Sender sender = new Sender(sms[i], msg2);
								try {
									sender.send();

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

								}
							} else {
								System.out
										.println(">>>>>>>>>>>>>>>>>>>>>> SAME MESSAGE HAS BEEN SENT >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

		// String query =
		// "INSERT INTO obdserver (load_pct, temp, rpm, vss, iat, maf, throttlepos, vehicleId, latsend, lngsend)  values(?,?,?,?,?,?,?,?,?,?)";
		// System.out.println(query);
		// int i = executeUpdate(query, new Object[] { load_pct,temp, rpm, vss,
		// iat, maf, throttlepos, oi.getVehicleId(), latsend, lngsend});
		// if (i >= 0) {
		// b = true;
		// }

		return b;

	}

	public static boolean setAlert(String ownerid, String type, String val,
			String vehicleId) {
		boolean b = false;

		String q = "Delete from alerts where vehicleId ="+vehicleId+" and  ownerid=" + ownerid
				+ " and alerttype like '" + type + "'";
		executeUpdate(q);
		String query = "INSERT INTO alerts (alerttype, val, ownerid,vehicleId)  values(?,?,?,?)";
		System.out.println(query);
		int i = executeUpdate(query, new Object[] { type, val, ownerid,
				vehicleId });
		if (i >= 0) {
			b = true;
		}
		return b;
	}

	public static boolean addOwner(String ownerid, String drivername,
			String vno, String imei) {
		boolean b = false;

		String query = "INSERT INTO obdserver.vehicles (drivername, vehicleno, imei, ownerId)  values(?,?,?,?)";
		System.out.println(query);
		int i = executeUpdate(query, new Object[] { drivername, vno, imei,
				ownerid });
		if (i >= 0) {
			b = true;
		}
		return b;
	}

	public static boolean createNewUser(HashMap parameters) {
		String fname = StringHelper.n2s(parameters.get("fname"));
		String phoneNo = StringHelper.n2s(parameters.get("phoneNo"));
		String login = StringHelper.n2s(parameters.get("login"));
		String pass = StringHelper.n2s(parameters.get("pass"));

		String q = "insert into useraccounts (login, pass, role, fullname, phoneno)"
				+ " values(?,?,?,?,?)";
		int rows = executeUpdate(q, login, pass, 2, fname, phoneNo);
		if (rows > 0)
			return true;
		else
			return false;

	}

	public static boolean duplicateCheck(String ownerid, String val) {
		boolean b = false;
		String sql = "Select * from alerts where ownerid='" + ownerid
				+ "' and ";
		List lst = getMapList(sql);
		if (lst.size() > 0) {
			b = true;
		}
		return b;
	}

	public static List getOBDAllList(String vehicleNo) {
		String query = "";
		if (vehicleNo.length() > 0)
			query = "SELECT * FROM obd o,adminvehicles a where o.vehicleNo=a.vehicleNo and a.vehicleno like '%"
					+ vehicleNo + "%' order by `time` Limit 1,1000";
		else
			query = "SELECT * FROM obd o,adminvehicles a where o.vehicleNo =a.vehicleNo order by `time` Limit 1,1000";
		System.out.println(query);
		List beans = getBeanList(OBDModel.class, query);
		return beans;
	}

	public static List getAlertList(String vehicleid) {
		String query = "";
		query = "SELECT * FROM alerts where vehicleId=" + vehicleid;
		System.out.println(query);
		List beans = getBeanList(AlertModel.class, query);
		return beans;
	}

	public static List getDriverList(String userId) {
		String query = "";
		query = "SELECT v.* FROM vehicles v where v.ownerId=" + userId;
		System.out.println(query);
		List beans = getBeanList(VehicleModel.class, query);
		return beans;
	}

	public static List getVehicleData() {
		List l = getBeanList(
				VehicleModel.class,
				"SELECT *,date_format(`time`, '%d-%b-%y %h:%i:%s %p') as `time` FROM getvechiledata");
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			try {
				VehicleModel vehicle = (VehicleModel) iterator.next();
				String lat = vehicle.getLatsend();
				String lng = vehicle.getLngsend();
				String[] a = Geocoder.getLocation(lat + "," + lng);
				vehicle.setAddress(a[0]);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		return l;
	}

	public static String getVehicleData(List l, String vechile) {
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			try {
				VehicleModel vehicle = (VehicleModel) iterator.next();
				if (vehicle.getVehicleId().equalsIgnoreCase(vechile)) {
					return vehicle.getAddress();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		return "";
	}

	public static String getVehicleTime(List l, String vechile) {
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			try {
				VehicleModel vehicle = (VehicleModel) iterator.next();
				if (vehicle.getVehicleId().equalsIgnoreCase(vechile)) {
					return vehicle.getTime();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		return "";
	}

	public static OwnerInfoModel getOwnerId(String imei) {
		String query = "";
		OwnerInfoModel oi = null;
		query = "SELECT * FROM vehicles v,useraccounts u where v.ownerId=u.userId and v.imei='"
				+ imei + "'";
		System.out.println(query);
		List lst = getBeanList(OwnerInfoModel.class, query);
		if (lst.size() > 0) {
			oi = (OwnerInfoModel) lst.get(0);
		}
		return oi;
	}

	public static List validateUser(String username, String pwd) {
		String query = "Select * from useraccounts a where a.login like '"
				+ username + "' and a.pass like '" + pwd + "' ";
		return getBeanList(UserAccountModel.class, query);
	}

	public static boolean dataExists(String query) {
		boolean success = false;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getDBConnection();
			rs = conn.createStatement().executeQuery(query);
			System.out.println("Executing " + query);
			if (rs.next()) {
				success = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public static String getDomain() {
		String qu = "SELECT * FROM ownertable";
		return getDropDownList(qu);
	}

	public static String getDropDownList(String query) {
		String ret = "";
		//
		// System.out.println(query);
		// List domainList = DBUtils.getBeanList(OwnerInfoModel.class, query);
		// for (int i = 0; domainList.size() > 0 && i < domainList.size(); i++)
		// {
		// OwnerInfoModel dm = (OwnerInfoModel) domainList.get(i);
		// ret += "<OPTION value='" + dm.getOwnerid()+ "'>" + dm.getOwnername()
		// + "</OPTION>";
		// }
		System.out.println("ret  " + ret);
		return ret;
	}

	public static String getMaxValue(String query) {

		String success = "";
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getDBConnection();
			rs = conn.createStatement().executeQuery(query);
			System.out.println("Executing " + query);
			if (rs.next()) {
				success = rs.getString(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public static String getDBCombo(String query) {

		String success = "";
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getDBConnection();
			rs = conn.createStatement().executeQuery(query);
			System.out.println("Executing " + query);
			while (rs.next()) {
				String key = rs.getString(1);
				String value = rs.getString(2);
				success += "<option value='" + key + "'>" + value + "</option>";

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	// for displaying the driver report
	public static List getDriverReport(String month, String year,
			String severe, String userid) throws NumberFormatException,
			IOException {
		ArrayList<Long> time = new ArrayList<Long>();
		ArrayList<Long> speed = new ArrayList<Long>();
		List<SpeedModel> list = new ArrayList<SpeedModel>();
		if (month.equalsIgnoreCase("all")) {
			month = "%";
		}
		if (year.equalsIgnoreCase("all")) {
			year = "%";
		}
		String query = "";

		if (year != "" && month == "") {
			// yearly vehicle details
			query = "SELECT o.vss as speed FROM `obdserver` o where o.vehicleid in (SELECT vehicleid FROM `vehicles` v,`useraccounts` u  group by vehicleid) and date_format(o.`time`,'%c-%Y') like '"
					+ year + "' group by o.obdid";
			System.out.println("Query is " + query);
			list = DatabaseUtility.getBeanList(SpeedModel.class, query);
		} else if (year != "" && month != "") {
			// monthly and yearly vehicle details
			query = "SELECT o.vss as speed FROM `obdserver` o where o.vehicleid in (SELECT vehicleid FROM `vehicles` v,`useraccounts` u  group by vehicleid) and date_format(o.`time`,'%c-%Y') like '"
					+ month + "-" + year + "' group by o.obdid";
			System.out.println("Query is " + query);
			list = DatabaseUtility.getBeanList(SpeedModel.class, query);
		}
		List returnList = new ArrayList<MasterModel>();
		if (list.size() > 0) {
			// Apply the algorithm
			// No. of clusters
			int minrpm = 0;
			int maxrpm = 0;
			int minspeed = 0;
			int maxspeed = 0;
			int k = 3;
			AlgoKMeans algoKMeans = new AlgoKMeans(); // we request 3 clusters
			List<Cluster> clusters = algoKMeans.runAlgorithm(list, k);
			// driving styles
			// less efficient
			// efficient
			// rash
			String str = algoKMeans.getSpeedCluster(clusters, severe);

			if (severe.equalsIgnoreCase("less efficient")) {
				minspeed = 0;
				maxspeed = 30;
				minrpm = 0;
				maxrpm = 1000;
			} else if (severe.equalsIgnoreCase("efficient")) {
				minspeed = 30;
				maxspeed = 70;
				minrpm = 1000;
				maxrpm = 2000;
			} else if (severe.equalsIgnoreCase("rash")) {
				minspeed = 70;
				maxspeed = 160;
				minrpm = 2000;
				maxrpm = 3000;
			}

			if (str.length() > 0) {
				System.out.println("Cluster: " + str);
				returnList = getVehicleDetail(str, minrpm, maxrpm, minspeed,
						maxspeed);
				System.out.println("Cluster1=====================>"
						+ returnList);
				return returnList;
			}
		}
		System.out.println("Cluster2=====================>" + returnList);
		return returnList;
	}

	// for getting the list of vehicle report as vehicle number, driver name
	public static List getVehicleDetail(String str, int minrpm, int maxrpm,
			int minspeed, int maxspeed) {
		List returnList = new ArrayList<MasterModel>();
		String query = "SELECT o.vehicleid,v.vehicleno,v.drivername FROM `obdserver` o,`vehicles` v where o.`vss` in ("
				+ str
				+ ") and o.rpm>'"
				+ minrpm
				+ "'and o.rpm<'"
				+ maxrpm
				+ "' and o.vss>'"
				+ minspeed
				+ "' and o.vss<'"
				+ maxspeed
				+ "'and o.vehicleid=v.vehicleid group by vehicleid;";
		System.out.println("Query is " + query);
		List list = DatabaseUtility.getBeanList(MasterModel.class, query);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			MasterModel object = (MasterModel) iterator.next();
			returnList.add(object);
			System.out
					.println("Return List in get vehicle details ============> "
							+ returnList);

		}
		return returnList;
	}

}
