package com.ad.ProxyMe;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;

/*************************************************
 **Only if using API 21
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
 */

public class MainActivity extends Activity {

	protected static final String TAG = "MainActivity";
	protected static final Boolean D = true;
	protected static final long DELAYBT = 60000;
	public   BluetoothDevice  	 device ;   
	DiscoveryService mService;
	boolean mBound = false;
	private ServiceManager m;
	private static Context mContext;
	@Override
	protected void onStart() {
		super.onStart();
		
	}

	@Override
	protected void onStop() {
		super.onStop();
  
//To stop the service when application is stopped		
//		m.stop();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
  
		
		
//To stop the service when application is destroyed.		
//		m.stop();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		 mContext = getApplicationContext();
		

		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("com.ad.proxymi.ACTION_ERRORS"));
//		m = ServiceManager.getInstance(getApplicationContext());
//		m.start();
	    String uniqueId = "FIIX_UniqueID";
		 Register r = Register.getInstance();
		 System.out.println("Going to register the user ");
	     r.doRegistration(getApplicationContext(), uniqueId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_list, menu);
		return true;
	}


	// Method to start the service
	/*	public void startService() {

         ServiceArgument  parameters  =  new ServiceArgument("Fiix","http://192.168.50.5", "80");
 		 Intent  intent = new Intent(getApplicationContext(),DiscoveryService.class);
 		 intent.putExtra("args",parameters);
		 startService(intent);



	}
	 */
	// Method to stop the service
	public void stopService() {
		stopService(new Intent(getBaseContext(), DiscoveryService.class));
	}



	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

		@Override

		public void onReceive(Context context, Intent intent) {


			switch (intent.getIntExtra("Error", -1)) {
			case Errors.ERR_BT_IS_NOT_DISCOVERABLE:
			{
				Log.v("BT is not discaoverable", null);
				break;
			}
			case Errors.ERR_BTV2_IS_NOT_SUPPORTED:
			{
				Log.v("BT is not supported", null);
				break;
			}


			}

		}

	};    


	 public static Context getContext() {
	        return mContext;
	    }


	 
}
