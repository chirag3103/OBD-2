package com.util;

public class AndroidConstants {
	public static String MAIN_SERVER_IP = "env-9683988.ind-cloud.everdata.com";
	public static String MAIN_SERVER_PORT = "8080";

	public static String url() {
		return "http://" + AndroidConstants.MAIN_SERVER_IP + ":"
				+ AndroidConstants.MAIN_SERVER_PORT
				+ "/CenteralServer/pages/tiles/save.jsp";
	}
}