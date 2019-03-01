package com.obd.activity;

import static com.obd.activity.ConfigActivity.getGpsDistanceUpdatePeriod;
import static com.obd.activity.ConfigActivity.getGpsUpdatePeriod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.inject.Inject;
import com.obd.commands.ObdCommand;
import com.obd.commands.SpeedCommand;
import com.obd.commands.engine.RPMCommand;
import com.obd.commands.engine.RuntimeCommand;
import com.obd.enums.AvailableCommandNames;
import com.obd.reader.activity.R;
import com.obd.reader.config.ObdConfig;
import com.obd.reader.io.AbstractGatewayService;
import com.obd.reader.io.LogCSVWriter;
import com.obd.reader.io.MockObdGatewayService;
import com.obd.reader.io.ObdCommandJob;
import com.obd.reader.io.ObdGatewayService;
import com.obd.reader.io.ObdProgressListener;
import com.obd.reader.net.ObdReading;
import com.obd.reader.trips.TripLog;
import com.obd.reader.trips.TripRecord;
import com.util.GPSTracker;
import com.util.HttpView;
import com.util.OBDModel;
import com.util.StringHelper;

@SuppressLint("NewApi")
public class MainActivity extends RoboActivity implements ObdProgressListener,
		LocationListener, GpsStatus.Listener {

	private static final String TAG = MainActivity.class.getName();
	private static final int NO_BLUETOOTH_ID = 0;
	private static final int BLUETOOTH_DISABLED = 1;
	private static final int START_LIVE_DATA = 2;
	private static final int STOP_LIVE_DATA = 3;
	private static final int SETTINGS = 4;
	private static final int SETTINGS_IP = 44;
	private static final int GET_DTC = 5;
	private static final int TABLE_ROW_MARGIN = 7;
	private static final int NO_ORIENTATION_SENSOR = 8;
	private static final int NO_GPS_SUPPORT = 9;
	private static final int TRIPS_LIST = 10;
	private static final int SAVE_TRIP_NOT_AVAILABLE = 11;
	private static boolean bluetoothDefaultIsEnable = false;

	static {
		// RoboGuice.setUseAnnotationDatabases(false);
	}

	public Map<String, String> commandResult = new HashMap<String, String>();
	public String oldSpeed = "";  
	boolean mGpsIsStarted = false;
	private LocationManager mLocService;
	private LocationProvider mLocProvider;
	private LogCSVWriter myCSVWriter;
	private Location mLastLocation;
	// / the trip log
	private TripLog triplog;
	private TripRecord currentTrip;

	private Context context;
	@InjectView(R.id.compass_text)
	private TextView compass;
	private final SensorEventListener orientListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent event) {
			float x = event.values[0];
			String dir = "";
			if (x >= 337.5 || x < 22.5) {
				dir = "N";
			} else if (x >= 22.5 && x < 67.5) {
				dir = "NE";
			} else if (x >= 67.5 && x < 112.5) {
				dir = "E";
			} else if (x >= 112.5 && x < 157.5) {
				dir = "SE";
			} else if (x >= 157.5 && x < 202.5) {
				dir = "S";
			} else if (x >= 202.5 && x < 247.5) {
				dir = "SW";
			} else if (x >= 247.5 && x < 292.5) {
				dir = "W";
			} else if (x >= 292.5 && x < 337.5) {
				dir = "NW";
			}
			updateTextView(compass, dir);
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// do nothing
		}
	};
	@InjectView(R.id.BT_STATUS)
	private TextView btStatusTextView;
	@InjectView(R.id.OBD_STATUS)
	private TextView obdStatusTextView;
	@InjectView(R.id.GPS_POS)
	private TextView gpsStatusTextView;
	@InjectView(R.id.vehicle_view)
	private LinearLayout vv;
	@InjectView(R.id.data_table)
	private TableLayout tl;
	@Inject
	private SensorManager sensorManager;
	@Inject
	private PowerManager powerManager;
	@Inject
	private SharedPreferences prefs;
	private boolean isServiceBound;
	private AbstractGatewayService service;
	private final Runnable mQueueCommands = new Runnable() {
		public void run() {
			if (service != null && service.isRunning() && service.queueEmpty()) {
				queueCommands();

				double lat = 0;
				double lon = 0;
				double alt = 0;
				final int posLen = 7;
				if (mGpsIsStarted && mLastLocation != null) {
					lat = mLastLocation.getLatitude();
					lon = mLastLocation.getLongitude();
					alt = mLastLocation.getAltitude();
					System.out.println("LAT" + lat + "LONG" + lon);
					// Toast.makeText(MainActivity.this, StringHelper.n2s(lat)
					// + " " + StringHelper.n2s(lon), 100);
					try {
						StringBuffer sb = new StringBuffer();
						sb.append("Lat: ");
						sb.append(String.valueOf(mLastLocation.getLatitude())
								.substring(0, posLen));
						sb.append(" Lon: ");
						sb.append(String.valueOf(mLastLocation.getLongitude())
								.substring(0, posLen));
						sb.append(" Alt: ");
						sb.append(String.valueOf(mLastLocation.getAltitude())
								.substring(0, posLen));
						gpsStatusTextView.setText(sb.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// if (false) {
				// // Upload the current reading by http
				// final String vin = "UNDEFINED_VIN";
				// Map<String, String> temp = new HashMap<String, String>();
				// temp.putAll(commandResult);
				// ObdReading reading = new ObdReading(lat, lon, alt,
				// System.currentTimeMillis(), vin, temp);
				// new UploadAsyncTask().execute(reading);
				//
				// } else if (false) {
				// // Write the current reading to CSV
				// final String vin = prefs.getString(
				// ConfigActivity.VEHICLE_ID_KEY, "UNDEFINED_VIN");
				// Map<String, String> temp = new HashMap<String, String>();
				// temp.putAll(commandResult);
				// ObdReading reading = new ObdReading(lat, lon, alt,
				// System.currentTimeMillis(), vin, temp);
				//
				// System.out.println("data String is+++++++++++++++++++"
				// + reading);
				// myCSVWriter.writeLineCSV(reading);
				// }
				commandResult.clear();
			}
			// run again in period defined in preferences
			new Handler().postDelayed(mQueueCommands,
					ConfigActivity.getObdUpdatePeriod(prefs));
		}
	};
	private Sensor orientSensor = null;
	private PowerManager.WakeLock wakeLock = null;
	private boolean preRequisites = true;
	private ServiceConnection serviceConn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			Log.d(TAG, className.toString() + " service is bound");
			isServiceBound = true;
			service = ((AbstractGatewayService.AbstractGatewayServiceBinder) binder)
					.getService();
			service.setContext(MainActivity.this);
			Log.d(TAG, "Starting live data");
			try {
				service.startService();
				if (preRequisites)
					btStatusTextView
							.setText(getString(R.string.status_bluetooth_connected));
			} catch (IOException ioe) {
				Log.e(TAG, "Failure Starting live data");
				btStatusTextView
						.setText(getString(R.string.status_bluetooth_error_connecting));
				doUnbindService();
			}
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			return super.clone();
		}

		// This method is *only* called when the connection to the service is
		// lost unexpectedly
		// and *not* when the client unbinds
		// (http://developer.android.com/guide/components/bound-services.html)
		// So the isServiceBound attribute should also be set to false when we
		// unbind from the service.
		@Override
		public void onServiceDisconnected(ComponentName className) {
			Log.d(TAG, className.toString() + " service is unbound");
			isServiceBound = false;
		}
	};

	public static String LookUpCommand(String txt) {
		for (AvailableCommandNames item : AvailableCommandNames.values()) {
			if (item.getValue().equals(txt))
				return item.name();
		}
		return txt;
	}

	public void updateTextView(final TextView view, final String txt) {
		new Handler().post(new Runnable() {
			public void run() {
				view.setText(txt);
			}
		});
	}

	GPSTracker g = null;

	public String[] getCurrentLocation() {
		String str[] = new String[2];

		if (g != null && g.canGetLocation) {
			Location l = g.getLocation();
			if (l != null) {
				str[0] = StringHelper.n2s(l.getLatitude());
				str[1] = StringHelper.n2s(l.getLongitude());
				System.out.println("LAT" + str[0] + "LONG" + str[1]);
				// Toast.makeText(MainActivity.this, str[0] + "and " + str[1],
				// 100)
				// .show();
			} else {
				// Toast.makeText(MainActivity.this,
				// "Unable to get the location at this moment!", 100)
				// .show();
			}

		}
		return str;
	}

	public void stateUpdate(final ObdCommandJob job) {

		final String cmdName = job.getCommand().getName();
		String cmdResult = "";
		final String cmdID = LookUpCommand(cmdName);

		if (job.getState().equals(
				ObdCommandJob.ObdCommandJobState.EXECUTION_ERROR)) {
			cmdResult = job.getCommand().getResult();
			if (cmdResult != null) {
				obdStatusTextView.setText(cmdResult.toLowerCase());
			}
		} else if (job.getState().equals(
				ObdCommandJob.ObdCommandJobState.NOT_SUPPORTED)) {
			cmdResult = getString(R.string.status_obd_no_support);
		} else {
			cmdResult = job.getCommand().getFormattedResult();
			obdStatusTextView.setText(getString(R.string.status_obd_data));
		}

		if (vv.findViewWithTag(cmdID) != null) {
			TextView existingTV = (TextView) vv.findViewWithTag(cmdID);
			existingTV.setText(cmdResult);
		} else
			addTableRow(cmdID, cmdName, cmdResult);
		commandResult.put(cmdID, cmdResult);
		// updateTripStatistic(job, cmdID);
	}

	private boolean gpsInit() {
		mLocService = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (mLocService != null) {
			mLocProvider = mLocService
					.getProvider(LocationManager.GPS_PROVIDER);
			if (mLocProvider != null) {
				mLocService.addGpsStatusListener(this);
				if (mLocService.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					System.out.println("In ready function");
					gpsStatusTextView.setText("Ready".toString());
					return true;
				}
			}
		}
		gpsStatusTextView.setText("not available");
		showDialog(NO_GPS_SUPPORT);
		Log.e(TAG, "Unable to get GPS PROVIDER");
		// todo disable gps controls into Preferences
		return false;
	}

	private void updateTripStatistic(final ObdCommandJob job, final String cmdID) {
		final OBDModel om = new OBDModel();
		// Double x = 888.00;
		// Double y = 888.00;
		Double z = 14.00;

		if (currentTrip != null) {
			// for speed
			// if (cmdID.equals(AvailableCommandNames.SPEED.toString())) {
			SpeedCommand command = (SpeedCommand) job.getCommand();
			currentTrip.setSpeedMax(command.getMetricSpeed());

			om.setVss(command.getMetricSpeed());
			// } else if (cmdID
			// .equals(AvailableCommandNames.ENGINE_RPM.toString())) {
			// for RPM
			RPMCommand command2 = (RPMCommand) job.getCommand();
			currentTrip.setEngineRpmMax(command2.getRPM());
			om.setRpm(command2.getRPM());
			// } else if (cmdID.endsWith(AvailableCommandNames.ENGINE_RUNTIME
			// .toString())) {
			RuntimeCommand command3 = (RuntimeCommand) job.getCommand();
			currentTrip.setEngineRuntime(command3.getFormattedResult());
			om.setThrottlepos(StringHelper.nullObjectToFloatEmpty(command3
					.getFormattedResult()));
			// }
			// om.setIat(x);
			// om.setLoad_pct(y);
			// om.setMaf(x);
			om.setObdid(z);
			om.setTime(z);
			new Thread() {
				public void run() {
					sendData(om);
				};

			}.start();

		}
	}

	public String getIMEI() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		System.out.println("Device IMEI is " + imei);
		return imei;

	}

	public void sendData(OBDModel om) {
		File sdCard = Environment.getExternalStorageDirectory();
		File dir = new File(sdCard.getAbsolutePath() + "/OBD");
		File file = null;
		if (!(dir.exists())) {
			dir.mkdirs();

		}

		// om.setIat(29f);
		// om.setRpm(1144);
		// om.setMaf(45f);
		// om.setLoad_pct(4.5f);
		// om.setVss(50);
		// om.setTemp(45f);
		// om.setThrottlepos(14.1566);

		// double iat = ConfigActivity.getEngineDisplacement(prefs);
		// double maf = ConfigActivity.getVolumetricEfficieny(prefs);
		// double Throttlepos = ConfigActivity.getMaxFuelEconomy(prefs);
		// double load_pct = ConfigActivity.getObdUpdatePeriod(prefs);
		// om.setIat(iat);
		// om.setMaf(maf);
		// om.setLoad_pct(load_pct);
		// om.setThrottlepos(Throttlepos);
		HashMap param = new HashMap();
		String str[] = new String[2];

		// load_pct, temp, rpm, vss, iat, maf, throttlepos, time, obdid,
		// vehicleId, latsend, lngsend
		param.put("method", "send");
		try {
			param.put("iat", StringHelper.n2s(om.getIat()));
			param.put("maf", StringHelper.n2s(om.getMaf()));
			param.put("throttlepos", StringHelper.n2s(om.getThrottlepos()));
			param.put("load_pct", StringHelper.n2s(om.getLoad_pct()));
			param.put("vss", StringHelper.n2s(om.getVss()));
			param.put("rpm", StringHelper.n2s(om.getRpm()));
			param.put("temp", StringHelper.n2s(om.getTemp()));
			param.put("imei", imei);
			param.put("time", System.currentTimeMillis());
			str = getCurrentLocation();
			param.put("latsend", str[0]);
			param.put("lngsend", str[1]);

			// TODO: handle exception

			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yy");
			file = new File(dir, "obd_" + sdf.format(new Date()) + ".txt");
			String mydate = java.text.DateFormat.getDateTimeInstance().format(
					Calendar.getInstance().getTime());
			String st = mydate.replaceAll(",", "");
			param.put("ctime", st);
			OutputStream os = new FileOutputStream(file, true);
			String msg = StringHelper.n2s(param.get("iat")) + ","
					+ StringHelper.n2s(param.get("load_pct")) + ","
					+ StringHelper.n2s(param.get("maf")) + ","
					+ StringHelper.n2s(param.get("throttlepos")) + ","
					+ StringHelper.n2s(param.get("vss")) + ","
					+ StringHelper.n2s(param.get("rpm")) + ","
					+ StringHelper.n2s(param.get("temp")) + ","
					+ StringHelper.n2s(param.get("imei")) + ","
					+ StringHelper.n2s(param.get("latsend")) + ","
					+ StringHelper.n2s(param.get("lngsend")) + ","
					+ StringHelper.n2s(param.get("time")) + "\n";

			System.out.println("MSG " + msg);
			Log.d(TAG,
					"throttle,vss,rpm "
							+ StringHelper.n2s(param.get("throttlepos")) + ","
							+ StringHelper.n2s(param.get("vss")) + ","
							+ StringHelper.n2s(param.get("rpm")));
			os.write(msg.getBytes());
			os.close();
			if (isInternet)

				HttpView.createURL(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isInternetAvailable() {
		try {
			InetAddress ipAddr = InetAddress.getByName("google.com"); // You can
																		// replace
																		// it
																		// with
																		// your
																		// name

			if (ipAddr.equals("")) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			return false;
		}

	}

	String imei = "";
	boolean isInternet = false;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		android.os.StrictMode.ThreadPolicy tp = android.os.StrictMode.ThreadPolicy.LAX;
		android.os.StrictMode.setThreadPolicy(tp);
		final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter != null)
			bluetoothDefaultIsEnable = btAdapter.isEnabled();
		System.out.println("in oncreate method");
		setContentView(R.layout.main);
		OBDModel om = new OBDModel();
		sendData(om);
		imei = getIMEI();
		// get Orientation sensor
		List<Sensor> sensors = sensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);
		if (sensors.size() > 0)
			orientSensor = sensors.get(0);
		else
			showDialog(NO_ORIENTATION_SENSOR);
		isInternet = isInternetAvailable();
		context = this.getApplicationContext();
		// create a log instance for use by this application
		triplog = TripLog.getInstance(context);
		g = new GPSTracker(MainActivity.this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "Entered onStart...");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mLocService != null) {
			mLocService.removeGpsStatusListener(this);
			mLocService.removeUpdates(this);
		}

		releaseWakeLockIfHeld();
		if (isServiceBound) {
			doUnbindService();
		}

		endTrip();

		final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter != null && btAdapter.isEnabled()
				&& !bluetoothDefaultIsEnable)
			btAdapter.disable();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "Pausing..");
		releaseWakeLockIfHeld();
	}

	/**
	 * If lock is held, release. Lock will be held when the service is running.
	 */
	private void releaseWakeLockIfHeld() {
		if (wakeLock.isHeld())
			wakeLock.release();
	}

	protected void onResume() {
		super.onResume();
		Log.d(TAG, "Resuming..");
		sensorManager.registerListener(orientListener, orientSensor,
				SensorManager.SENSOR_DELAY_UI);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"ObdReader");

		// get Bluetooth device
		final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

		preRequisites = btAdapter != null && btAdapter.isEnabled();
		if (!preRequisites
				&& prefs.getBoolean(ConfigActivity.ENABLE_BT_KEY, false)) {
			preRequisites = btAdapter.enable();
		}

		gpsInit();

		if (!preRequisites) {
			showDialog(BLUETOOTH_DISABLED);
			btStatusTextView.setText("disabled");
		} else {
			btStatusTextView.setText("ready");
		}
	}

	private void updateConfig() {
		startActivity(new Intent(this, ConfigActivity.class));
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, START_LIVE_DATA, 0,
				getString(R.string.menu_start_live_data));
		menu.add(0, STOP_LIVE_DATA, 0, getString(R.string.menu_stop_live_data));
		// menu.add(0, GET_DTC, 0, getString(R.string.menu_get_dtc));
		// menu.add(0, TRIPS_LIST, 0, getString(R.string.menu_trip_list));
		menu.add(0, SETTINGS, 0, getString(R.string.menu_settings));
		menu.add(0, SETTINGS_IP, 0, "Change Server");
		return true;
	}

	// private void staticCommand() {
	// Intent commandIntent = new Intent(this, ObdReaderCommandActivity.class);
	// startActivity(commandIntent);
	// }

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case START_LIVE_DATA:
			startLiveData();
			return true;
		case STOP_LIVE_DATA:
			stopLiveData();
			return true;
		case SETTINGS:
			updateConfig();
			return true;
			// case GET_DTC:
			// getTroubleCodes();
			// return true;
		case SETTINGS_IP:
			startActivity(new Intent(this, ConfigTabActivity.class));
			return true;
			// case TRIPS_LIST:
			// startActivity(new Intent(this, TripListActivity.class));
			// return true;
			// case COMMAND_ACTIVITY:
			// staticCommand();
			// return true;
		}
		return false;
	}

	private void getTroubleCodes() {
		startActivity(new Intent(this, TroubleCodesActivity.class));
	}

	private void startLiveData() {
		Log.d(TAG, "Starting live data..");

		tl.removeAllViews(); // start fresh
		doBindService();

		currentTrip = triplog.startTrip();
		if (currentTrip == null)
			showDialog(SAVE_TRIP_NOT_AVAILABLE);

		// start command execution
		new Handler().post(mQueueCommands);

		if (prefs.getBoolean(ConfigActivity.ENABLE_GPS_KEY, false))
			gpsStart();
		else
			gpsStatusTextView.setText(getString(R.string.status_gps_not_used));

		// screen won't turn off until wakeLock.release()
		wakeLock.acquire();

		// if (prefs.getBoolean(ConfigActivity.ENABLE_FULL_LOGGING_KEY, false))
		// {
		//
		// // Create the CSV Logger
		// long mils = System.currentTimeMillis();
		// SimpleDateFormat sdf = new SimpleDateFormat("_dd_MM_yyyy_HH_mm_ss");
		//
		// myCSVWriter = new LogCSVWriter("Log"
		// + sdf.format(new Date(mils)).toString() + ".csv",
		// prefs.getString(ConfigActivity.DIRECTORY_FULL_LOGGING_KEY,
		// getString(R.string.default_dirname_full_logging)));
		// }
	}

	private void stopLiveData() {
		Log.d(TAG, "Stopping live data..");

		gpsStop();

		doUnbindService();
		endTrip();

		releaseWakeLockIfHeld();

		if (myCSVWriter != null) {
			myCSVWriter.closeLogCSVWriter();
		}
	}

	protected void endTrip() {
		if (currentTrip != null) {
			currentTrip.setEndDate(new Date());
			triplog.updateRecord(currentTrip);
		}
	}

	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		switch (id) {
		case NO_BLUETOOTH_ID:
			build.setMessage(getString(R.string.text_no_bluetooth_id));
			return build.create();
		case BLUETOOTH_DISABLED:
			build.setMessage(getString(R.string.text_bluetooth_disabled));
			return build.create();
		case NO_ORIENTATION_SENSOR:
			build.setMessage(getString(R.string.text_no_orientation_sensor));
			return build.create();
		case NO_GPS_SUPPORT:
			build.setMessage(getString(R.string.text_no_gps_support));
			return build.create();
		case SAVE_TRIP_NOT_AVAILABLE:
			build.setMessage(getString(R.string.text_save_trip_not_available));
			return build.create();
		}
		return null;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem startItem = menu.findItem(START_LIVE_DATA);
		MenuItem stopItem = menu.findItem(STOP_LIVE_DATA);
		MenuItem settingsItem = menu.findItem(SETTINGS);
		// MenuItem getDTCItem = menu.findItem(GET_DTC);

		if (service != null && service.isRunning()) {
			// getDTCItem.setEnabled(false);
			startItem.setEnabled(false);
			stopItem.setEnabled(true);
			settingsItem.setEnabled(false);
		} else {
			// getDTCItem.setEnabled(true);
			stopItem.setEnabled(false);
			startItem.setEnabled(true);
			settingsItem.setEnabled(true);
		}

		return true;
	}

	private void addTableRow(String id, String key, String val) {

		TableRow tr = new TableRow(this);
		MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(TABLE_ROW_MARGIN, TABLE_ROW_MARGIN, TABLE_ROW_MARGIN,
				TABLE_ROW_MARGIN);
		tr.setLayoutParams(params);

		TextView name = new TextView(this);
		name.setGravity(Gravity.RIGHT);
		name.setText(key + ": ");
		TextView value = new TextView(this);
		value.setGravity(Gravity.LEFT);
		value.setText(val);
		value.setTag(id);
		tr.addView(name);
		tr.addView(value);
		tl.addView(tr, params);
	}

	/**
     *
     */
	private void queueCommands() {
		if (isServiceBound) {
			for (ObdCommand Command : ObdConfig.getCommands()) {
				if (prefs.getBoolean(Command.getName(), true))
					service.queueJob(new ObdCommandJob(Command));
			}
		}
	}

	private void doBindService() {
		if (!isServiceBound) {
			Log.d(TAG, "Binding OBD service..");
			if (preRequisites) {
				btStatusTextView
						.setText(getString(R.string.status_bluetooth_connecting));
				Intent serviceIntent = new Intent(this, ObdGatewayService.class);
				bindService(serviceIntent, serviceConn,
						Context.BIND_AUTO_CREATE);
			} else {
				btStatusTextView
						.setText(getString(R.string.status_bluetooth_disabled));
				Intent serviceIntent = new Intent(this,
						MockObdGatewayService.class);
				bindService(serviceIntent, serviceConn,
						Context.BIND_AUTO_CREATE);
			}
		}
	}

	private void doUnbindService() {
		if (isServiceBound) {
			if (service.isRunning()) {
				service.stopService();
				if (preRequisites)
					btStatusTextView
							.setText(getString(R.string.status_bluetooth_ok));
			}
			Log.d(TAG, "Unbinding OBD service..");
			unbindService(serviceConn);
			isServiceBound = false;
			obdStatusTextView
					.setText(getString(R.string.status_obd_disconnected));
		}
	}

	public void onLocationChanged(Location location) {
		mLastLocation = location;
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onProviderDisabled(String provider) {
	}

	public void onGpsStatusChanged(int event) {

		switch (event) {
		case GpsStatus.GPS_EVENT_STARTED:
			gpsStatusTextView.setText(getString(R.string.status_gps_started));
			break;
		case GpsStatus.GPS_EVENT_STOPPED:
			gpsStatusTextView.setText(getString(R.string.status_gps_stopped));
			break;
		case GpsStatus.GPS_EVENT_FIRST_FIX:
			gpsStatusTextView.setText(getString(R.string.status_gps_fix));
			break;
		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			break;
		}
	}

	private synchronized void gpsStart() {
		if (!mGpsIsStarted && mLocProvider != null && mLocService != null
				&& mLocService.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mLocService.requestLocationUpdates(mLocProvider.getName(),
					getGpsUpdatePeriod(prefs),
					getGpsDistanceUpdatePeriod(prefs), this);
			mGpsIsStarted = true;
		} else if (mGpsIsStarted && mLocProvider != null && mLocService != null) {
		} else {
			gpsStatusTextView
					.setText(getString(R.string.status_gps_no_support));
		}
	}

	private synchronized void gpsStop() {
		if (mGpsIsStarted) {
			mLocService.removeUpdates(this);
			mGpsIsStarted = false;
			gpsStatusTextView.setText(getString(R.string.status_gps_stopped));
		}
	}

	/**
	 * Uploading asynchronous task
	 */
	private class UploadAsyncTask extends AsyncTask<ObdReading, Void, Void> {

		@Override
		protected Void doInBackground(ObdReading... readings) {
			Log.d(TAG, "Uploading " + readings.length + " readings..");
			// instantiate reading service client
			// final String endpoint = prefs.getString(
			// ConfigActivity.UPLOAD_URL_KEY, "");
			// RestAdapter restAdapter = new
			// RestAdapter.Builder().setEndpoint(endpoint).build();
			// RestAdapter restAdapter = new RestAdapter.Builder().set;
			// ObdService service = restAdapter.create(ObdService.class);
			// // upload readings
			// for (ObdReading reading : readings) {
			// try {
			// Response response = service.uploadReading(reading);
			// assert response.getStatus() == 200;
			// } catch (RetrofitError re) {
			// Log.e(TAG, re.toString());
			// }
			//
			// }
			// Log.d(TAG, "Done");
			return null;
		}

	}
}
