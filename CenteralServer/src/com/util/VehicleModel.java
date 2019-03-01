package com.util;

import java.io.Serializable;

public class VehicleModel implements Serializable {
	public String vehicleId, drivername, vehicleno, imei, ownerId;
	public String latsend, lngsend, time;
	String address="";
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLatsend() {
		return latsend;
	}

	public void setLatsend(String latsend) {
		this.latsend = latsend;
	}

	public String getLngsend() {
		return lngsend;
	}

	public void setLngsend(String lngsend) {
		this.lngsend = lngsend;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getDrivername() {
		return drivername;
	}

	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}

	public String getVehicleno() {
		return vehicleno;
	}

	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	
}
