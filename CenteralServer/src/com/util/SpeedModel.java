package com.util;

import java.io.Serializable;

public class SpeedModel implements Serializable {
	long speed, itime;
	String obdid;
	long avgspeed, vehicleid, userid;

	public long getAvgspeed() {
		return avgspeed;
	}

	public void setAvgspeed(long avgspeed) {
		this.avgspeed = avgspeed;
	}

	public long getVehicleid() {
		return vehicleid;
	}

	public void setVehicleid(long vehicleid) {
		this.vehicleid = vehicleid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getSpeed() {
		return speed;
	}

	public void setSpeed(long speed) {
		this.speed = speed;
	}

	public long getItime() {
		return itime;
	}

	public void setItime(long itime) {
		this.itime = itime;
	}

	public String getObdid() {
		return obdid;
	}

	public void setObdid(String obdid) {
		this.obdid = obdid;
	}

}
