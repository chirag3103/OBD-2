package com.obd.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.obd.reader.activity.R;
import com.util.AndroidConstants;
import com.util.StringHelper;

public class WelcomeActivity extends CommonActivity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		toast("Make sure that IMEI is added in " + getIMEI() + " DB");
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}
		turnGPSOn();

		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		go(MainActivity.class);
		// checkConnectivity();
	}

	private void turnGPSOn() {
		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (!provider.contains("gps")) { // if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			sendBroadcast(poke);
		}
	}

	public void checkConnectivity() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SharedPreferences s = PreferenceManager
						.getDefaultSharedPreferences(WelcomeActivity.this);

				boolean success = checkConnectivityServer();
				if (success) {
					SharedPreferences.Editor editor = s.edit();
					editor.putString("MAIN_SERVER_IP",
							AndroidConstants.MAIN_SERVER_IP + "");
					editor.putString("MAIN_SERVER_PORT",
							AndroidConstants.MAIN_SERVER_PORT + "");
					editor.commit();
					go(MainActivity.class);

				} else {

					String MAIN_SERVER_IP = s.getString("MAIN_SERVER_IP",
							AndroidConstants.MAIN_SERVER_IP);
					String MAIN_SERVER_PORT = s.getString("MAIN_SERVER_PORT",
							AndroidConstants.MAIN_SERVER_PORT);
					if (!MAIN_SERVER_IP
							.equalsIgnoreCase(AndroidConstants.MAIN_SERVER_IP)
							|| !MAIN_SERVER_PORT
									.equalsIgnoreCase(AndroidConstants.MAIN_SERVER_PORT)) {
						success = checkConnectivityServer(MAIN_SERVER_IP,
								StringHelper.n2i(MAIN_SERVER_PORT));
						if (success) {
							AndroidConstants.MAIN_SERVER_IP = MAIN_SERVER_IP;
							AndroidConstants.MAIN_SERVER_PORT = MAIN_SERVER_PORT;
							go(MainActivity.class);
						} else {
							System.out.println("Redirecting to Config 1");
							go(ConfigTabActivity.class);
						}
					} else {
						System.out.println("Redirecting to Config 2");
						go(ConfigTabActivity.class);
					}
				}
			}
		});

	}

	private void checkUserDetails() {

	}

}
