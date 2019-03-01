package com.util;

import java.io.Serializable;

public class OBDServerModel implements Serializable {

	public String load_pct="", temp="", rpm="", vss="", iat="", maf="", throttlepos="", time="", obdid="", ownerid="", latsend="", lngsend="";
	public String drivername="", vehicleno="", imei="";
	String udate="";
	public String getUdate() {
		return udate;
	}

	public void setUdate(String udate) {
		this.udate = udate;
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

	public String getLoad_pct() {
		return load_pct;
	}

	public void setLoad_pct(String load_pct) {
		this.load_pct = load_pct;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getRpm() {
		return rpm;
	}

	public void setRpm(String rpm) {
		this.rpm = rpm;
	}

	public String getVss() {
		return vss;
	}

	public void setVss(String vss) {
		this.vss = vss;
	}

	public String getIat() {
		return iat;
	}

	public void setIat(String iat) {
		this.iat = iat;
	}

	public String getMaf() {
		return maf;
	}

	public void setMaf(String maf) {
		this.maf = maf;
	}

	public String getThrottlepos() {
		return throttlepos;
	}

	public void setThrottlepos(String throttlepos) {
		this.throttlepos = throttlepos;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getObdid() {
		return obdid;
	}

	public void setObdid(String obdid) {
		this.obdid = obdid;
	}

	public String getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
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
	
	
	
	
}
