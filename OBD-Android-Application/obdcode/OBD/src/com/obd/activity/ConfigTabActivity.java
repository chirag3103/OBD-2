package com.obd.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.obd.reader.activity.R;
import com.util.StringHelper;

public class ConfigTabActivity extends CommonActivity {

	public static String TAG = "WelcomeActivity";
	EditText ipaddress = null, port = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config);
		TabHost tabs = (TabHost) findViewById(R.id.tabHost);

		tabs.setup();

		TabHost.TabSpec spec1 = tabs.newTabSpec("tag1");

		spec1.setContent(R.id.tab1);

		spec1.setIndicator(
				"",
				getResources().getDrawable(
						R.drawable.settings));

		tabs.addTab(spec1);
		
		
		// Server Settings
		ipaddress = (EditText) findViewById(R.id.editText1);
		port = (EditText) findViewById(R.id.editText2);
//		ipaddress.setText(AndroidConstants.MAIN_SERVER_IP);
//		port.setText(AndroidConstants.MAIN_SERVER_PORT);
		 SharedPreferences s=PreferenceManager.getDefaultSharedPreferences(ConfigTabActivity.this);
		 ipaddress.setText(s.getString("MAIN_SERVER_IP", com.util.AndroidConstants.MAIN_SERVER_IP +""));
		 port.setText(s.getString("MAIN_SERVER_PORT", com.util.AndroidConstants.MAIN_SERVER_PORT+""));
    	  
	}

	

	public void toast(String message) {
		Toast t = Toast.makeText(ConfigTabActivity.this, message,
				Toast.LENGTH_LONG);
		t.show();
	}
	
	
	ProgressDialog progressDialog=null;
	public void fnConfig(View v) {
		if (v.getId() == R.id.button1) {
			String newIp = ipaddress.getText().toString().trim();
			int newPort = StringHelper.n2i(port.getText().toString()) ;
			CheckConnectivityAsyncTask ct=new CheckConnectivityAsyncTask();
			ct.execute(new String[] { newIp,newPort+"","UpdateIp" });
	
		}else if (v.getId() == R.id.buttonCheckConnectivity) {
			CheckConnectivityAsyncTask ct=new CheckConnectivityAsyncTask();
			ct.execute(new String[] { "www.google.com","80" });
		}
		
		
		
		
	}

}
