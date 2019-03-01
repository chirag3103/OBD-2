package com.util;

import java.io.Serializable;

public class OBDModel implements Serializable {
	public static final long serialVersionUID = 1L;
	String load_pct, temp, rpm, vss, iat, maf, throttlepos, time, obdid;
	String vehicleno,speed;

	public String getLoad_pct() {
		return load_pct;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
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

	public String getVehicleno() {
		return vehicleno;
	}

	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
