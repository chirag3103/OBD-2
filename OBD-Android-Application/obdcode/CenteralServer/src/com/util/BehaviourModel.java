package com.util;

import java.io.Serializable;

public class BehaviourModel implements Serializable {
	public int noOfEfficient=0,noOfInefficient=0;
	public int avgSpeedEff=0,avgSpeedInEff=0;
	public float distance;
	public int vehicleId;
	public String driverName="";
	public int noOfEfficientPer=0,noOfInefficientPer=0;
	public String date="";
	public int cluster=0;
	public int getCluster() {
		return cluster;
	}
	public void setCluster(int cluster) {
		this.cluster = cluster;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getNoOfEfficient() {
		return noOfEfficient;
	}
	public void setNoOfEfficient(int noOfEfficient) {
		this.noOfEfficient = noOfEfficient;
	}
	public int getNoOfInefficient() {
		return noOfInefficient;
	}
	public void setNoOfInefficient(int noOfInefficient) {
		this.noOfInefficient = noOfInefficient;
	}
	public int getAvgSpeedEff() {
		return avgSpeedEff;
	}
	public void setAvgSpeedEff(int avgSpeedEff) {
		this.avgSpeedEff = avgSpeedEff;
	}
	public int getAvgSpeedInEff() {
		return avgSpeedInEff;
	}
	public void setAvgSpeedInEff(int avgSpeedInEff) {
		this.avgSpeedInEff = avgSpeedInEff;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public int getNoOfEfficientPer() {
		return noOfEfficientPer;
	}
	public void setNoOfEfficientPer(int noOfEfficientPer) {
		this.noOfEfficientPer = noOfEfficientPer;
	}
	public int getNoOfInefficientPer() {
		return noOfInefficientPer;
	}
	public void setNoOfInefficientPer(int noOfInefficientPer) {
		this.noOfInefficientPer = noOfInefficientPer;
	}
	
}
