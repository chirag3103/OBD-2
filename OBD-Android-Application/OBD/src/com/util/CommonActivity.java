package com.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.obd.activity.WelcomeActivity;
import com.util.AndroidConstants;
import com.util.StringHelper;

@SuppressLint("NewApi")
public class CommonActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		   android.os.StrictMode.ThreadPolicy tp = android.os.StrictMode.ThreadPolicy.LAX;
			android.os.StrictMode.setThreadPolicy(tp);
		
	}

	public InputStream getFile(String strName)
    {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
   
        return istr;
    }
	public static void CancelNotification(Context ctx, int notifyId) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) ctx
				.getSystemService(ns);
		nMgr.cancel(notifyId);
	}

	public void go(Class c) {
		Intent intent = new Intent(CommonActivity.this, c);
		startActivity(intent);
		finish();
	}

	public boolean checkConnectivityServer() {
		boolean success = checkConnectivityServer(
				AndroidConstants.MAIN_SERVER_IP,
				StringHelper.n2i(AndroidConstants.MAIN_SERVER_PORT));
		return success;

	}

	public static boolean checkConnectivityServer(String ip, int port) {
		boolean success = false;
		try {
			System.out.println("Checking Connectivity With " + ip + " " + port);
			Socket soc = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(ip, port);
			soc.connect(socketAddress, 3000);
			success = true;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(" Connecting to server " + success);
		return success;

	}

	ProgressDialog progressDialog = null;
	AlertDialog alertDialog = null;

	class CheckConnectivityAsyncTask extends AsyncTask<String, String, String> {
		String message = "";
		String title = "";
		String action = "";

		@Override
		protected void onPreExecute() {
			System.out.println("In Aysnc");
			progressDialog = ProgressDialog.show(CommonActivity.this,
					"Please Wait", "Loading....", true);
			alertDialog = new AlertDialog.Builder(CommonActivity.this).create();
		}

		@Override
		protected String doInBackground(String... params) {
			String ip = params[0];
			int port = StringHelper.n2i(params[1]);
			boolean success = checkConnectivityServer(ip, port);

			if (success) {

				title = "Success";
				if (params.length > 2 && params[2].equalsIgnoreCase("UpdateIp")) {
					action = "1";
					message = "Connection established with the Main Server.";
					AndroidConstants.MAIN_SERVER_IP = ip;
					AndroidConstants.MAIN_SERVER_PORT = port + "";
				} else {
					message = "Internet Connection Successful!";
				}
			} else {
				action = "";
				message = "Error Connecting to Server http://" + ip + ":"
						+ port;
				title = "Connectivity Error";
			}

			return success + "";
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			alertDialog.setTitle(title);
			alertDialog.setMessage(message);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					alertDialog.hide();
					if (action.length() > 0) {

						Intent main = new Intent(CommonActivity.this,
								WelcomeActivity.class);
						startActivity(main);
						finish();

					}

				}
			});
			alertDialog.show();

		};

	}

	public void toast(String message) {
		Toast t = Toast.makeText(CommonActivity.this, message, 1000);
		t.show();
	}

	public void finished() {
		try {
			System.runFinalizersOnExit(true);
			finish();
			super.finish();
			super.onDestroy();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			android.os.Process.killProcess(android.os.Process.myPid());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@JavascriptInterface
	public String getIMEI() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		System.out.println("Device IMEI is " + imei);
		return imei;

	}

}
