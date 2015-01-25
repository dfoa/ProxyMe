package com.ad.ProxyMe;



import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONStringer;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.IBinder;
import android.os.Looper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DiscoveryService extends Service {

	/** indicates how to behave if the service is killed */
	int mStartMode;
	/** interface for clients that bind */
	IBinder mBinder;     
	/** indicates whether onRebind should be used */
	boolean mAllowRebind;
	/** print log  */
	final static Boolean D = true;
	/** scan duration */
	static  int DELAYBT = 60000;
	/**run scan or not */
	boolean isBTRunning =  false;
	/** TAG description*/
	String TAG = "DiscoveryService";
	/** Array to hold macs that was found, kind of black list in order not to use again */
	private ArrayList<String> mArrayOfMacs;
	/** get local Bluetooth adapter */
	private BluetoothAdapter mBtAdapter ;
	/** get remote bluetooth properties */
	private  BluetoothDevice mDevice;
	/** self mac ID  */
	private String mSelfMac;	 
	/** Yes/No device in array list  */
	private boolean mFound = false ;
	/** Called when the service is being created. */
	
	@Override
	public void onCreate() {



		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		this.registerReceiver(mReceiver, filter);

		if (D) Log.d(TAG,"++ServiceonCreate++");

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		mSelfMac = mBtAdapter.getAddress();
		mArrayOfMacs = new ArrayList<String>();

		/*Thread for handling Bluetooth verion 2 scanning cycles
		 * 	   
		 */
		new Thread(new Runnable() { 
			public void run(){

				{
					while(!Thread.currentThread().isInterrupted()){
						
						
						Looper.prepare(); 
				          if (mBtAdapter.isDiscovering())
                          	//Adapter is currently doing scan action so we wait until finish
				        	  isBTRunning = false;
				          else 
				        	  isBTRunning = true;
						
						try {     

							while(isBTRunning)
							{ 

								if (!mBtAdapter.isEnabled())
								{
									mBtAdapter.enable();  

								}

								if (D)  Log.d(TAG, "BT Scanning started"); 
                      
								mBtAdapter.startDiscovery();
						
								Log.d(TAG,"sleep for " + DELAYBT + " seconds");

										Thread.sleep(DELAYBT);
										mBtAdapter.cancelDiscovery();
										if (D)  Log.d(TAG, "BT Scanning stopped"); 
							
							}
							Looper.loop();
						}
						catch (InterruptedException e) {
							Log.d(TAG, "BT Scanning stopped");
							Looper.myLooper().quit();
						}

					}     

				}
			}
		}).start();


	}

	/** The service is starting, due to a call to startService() */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		System.out.println("In service onStartCommand");



		return mStartMode;

	}

	/** A client is binding to the service with bindService() */
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("-----In onBind command");
		return mBinder;
	}

	/** Called when all clients have unbound with unbindService() */
	@Override
	public boolean onUnbind(Intent intent) {
		return mAllowRebind;
	}

	/** Called when a client is binding to the service with bindService()*/
	@Override
	public void onRebind(Intent intent) {

	}

	/** Called when The service is no longer used and is being destroyed */
	@Override
	public void onDestroy() {

		super.onDestroy();
		if (D)Log.d(TAG,"Unregister receiver..."); 
		unregisterReceiver(mReceiver);
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery find a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent

				//IntroMi

				mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				if (D) Log.d(TAG,"Found device "+ mDevice.getAddress());
				// if (device.getName().equalsIgnoreCase("IntroMi")) {
				//future implementation	
				//mRssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);

				// If it's already paired or already listed, skip it, because it's has been listed already
				//           System.out.println("This is the device name" + device.getName());
				//				           System.out.println("This is the length of the name :\n" +  mDevice.getName().length()
				//            showToast("Device name is\n " + device.getName() +"length:\n" + device.getName().length());

			


					//check if array first is first used
					if (mArrayOfMacs.isEmpty())
						//					//add mac address to the list
						mArrayOfMacs.add(mDevice.getAddress());
					else 
						if (mArrayOfMacs.equals(mDevice.getAddress()))
							mFound = true;

					{

						if (D) Log.v(TAG, "The device " + mDevice.getAddress() + " is already in the list -- ignore this mac");		 
						//						System.out.println("update device  " + device.getAddress() +" with RSSI: "+mRssi );

					}

					if (!mFound ) {
						mArrayOfMacs.add(mDevice.getAddress());

						if (D) Log.v(TAG,"Going to update server with device " + mDevice.getAddress());
                        
						//					new MyAsyncTask().execute(device.getAddress());
						/*
						 * update the server with
						 * selfmac:found mac:found time
						 */
						
					
						
						new Thread(new MyRunnable(mSelfMac,mDevice.getAddress())) {
							
//							long time = System.currentTimeMillis();
//							String foundMac = mDevice.getAddress();
//							public void run() {
//
//
//								System.out.println("In runnable");
//								String URL_STRING="http://192.168.50.5/cgi-bin/json.cgi";
//
//								try {
//									CustomHttpClient.executeHttpPost(URL_STRING,buildJson(mSelfMac, foundMac, time));
//								} catch (Exception e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//
//
//							}
						}.start();
					}
						
						
					
				
					

				
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                 if (D) Log.d(TAG, "Finished scan devices");
			}

		}

	};
	
	
	private JSONStringer buildJson(String selfMac,String foundMac,long time){
		
	  JSONStringer 	mJSONStringer = new JSONStringer();
		try {
			mJSONStringer.object();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			mJSONStringer.key("self_mac");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mJSONStringer.value(selfMac);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mJSONStringer.key("found_mac");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mJSONStringer.value(foundMac);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			mJSONStringer.key("time");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mJSONStringer.value(time);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			mJSONStringer.endObject();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return mJSONStringer;
		
		
		
		
	}
	
}



