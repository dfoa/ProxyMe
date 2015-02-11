package com.ad.ProxyMe;

/*
 * This service requiered manifest permissions 
 * <manifest ... >
 * <uses-permission android:name="android.permission.BLUETOOTH" />
 *</manifest>
 */
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONStringer;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class DiscoveryService extends Service {

	/** indicates how to behave if the service is killed */
	int mStartMode;
	/** interface for clients that bind */
	//	IBinder mBinder;     
	/** indicates whether onRebind should be used */
	boolean mAllowRebind;
	/** print log  */
	static Boolean D = true;
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
	/** Send action to runnable  */
	private static int FIND_NEARBY = 1;
	//* Hold parsable object from main activity to Service */ 
	private ServiceArgument serviceArgument;

	/**Object that parameters from  the call activity  */

	private final IBinder mBinder = new LocalBinder();
	private String URL_STRING="http://31.168.241.149/cgi-bin/json.cgi";
	private  Utils utils = new Utils() ;
	/** Called when the service is being created. */
	// Random number generator
	private final Random mGenerator = new Random();
	private  Thread thread;

	@Override
	public void onCreate() {


		if (D) Log.d(TAG,"++ServiceonCreate++");

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		/*
		 * check if  bluetooth is supported on this device. 
		 */
		
		if (mBtAdapter == null) {
		    // Device does not support Bluetooth
			BroadcastErrors(Errors.ERR_BTV2_IS_NOT_SUPPORTED);
			onDestroy();
		}
		/*
		 * Check if Bluetooth is discoverable
		 */
		if(mBtAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
		{
			BroadcastErrors(Errors.ERR_BT_IS_NOT_DISCOVERABLE);
		}
		mSelfMac = mBtAdapter.getAddress();
		mArrayOfMacs = new ArrayList<String>();


		/*Thread for handling Bluetooth verion 2 scanning cycles
		 * 	   
		 */



		thread = new Thread("discover"){

			public void run(){
				

				//	while(!Thread.currentThread().isInterrupted()){
				isBTRunning = true;
				Looper.prepare(); 
				//               BluetoothAdapter mBluetoothAdapter =BluetoothAdapter.getDefaultAdapter();

				try { 
					 printToLog("BT Scanning started");

					while(isBTRunning)
					{ 

						if (!mBtAdapter.isEnabled())
						{
							mBtAdapter.enable();  


						}
						  printToLog("Register..."); 


						  printToLog("Start discovery");


						// mBluetoothAdapter.startDiscovery();

						mBtAdapter.startDiscovery();
						
						
						if (D) Log.d(TAG,"sleep for " + DELAYBT + " seconds");

						Thread.sleep(DELAYBT);
						mBtAdapter.cancelDiscovery();

						if (D) Log.d(TAG,"Unregister..."); 
						//                     unregisterReceiver(mReceiver);
					}
					Looper.loop();
				}
				catch (InterruptedException e) {
					Log.d(TAG, "BT Scanning stopped");
					Looper.myLooper().quit();
				}

			}     
			//	}
		}; thread.start();


		/*
		 * Check if Bluetooth is discoverable
		 */
		if(mBtAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
		{
			BroadcastErrors(Errors.ERR_BT_IS_NOT_DISCOVERABLE);
			printToLog("BT is not discoverable");
		}
		 
		
		// Register for broadcasts when a device is discovered
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);

		// Register for broadcasts when discovery has finished

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		this.registerReceiver(mReceiver, filter);
     
	}


	public class LocalBinder extends Binder {
		DiscoveryService getService() {
			// Return this instance of LocalService so clients can call public methods
			return DiscoveryService.this;
		}
	}

	/** A client is binding to the service with bindService() */
	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("-----In onBind command");
		return mBinder;
	}




	/** The service is starting, due to a call to startService() */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("In onstart command");
		 
		


		Bundle data = new Bundle();
		data = intent.getExtras();
		serviceArgument = data.getParcelable("args");
		StringBuilder stringBuilder = new StringBuilder();
		printToLog(stringBuilder.append("This is the bundle" +  serviceArgument.getCompanyId()).toString());
		
//		printToLog(stringBuilder.append("This is what i got from intent" +  "and this is the start ID " + startId + "and flag " + flags ).toString());
//		System.out.println("this is what i got from intent "  + "and this is the start ID " + startId + "and flag " + flags );
//		System.out.println("Thread state is "  + Thread.currentThread().getState().toString());

		return START_REDELIVER_INTENT;
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
	
		System.out.println("Service has been destroyed.");
		mBtAdapter.cancelDiscovery();
		System.out.println("Thread state is "  + thread.getState().toString());
		thread.interrupt();
		System.out.println("Thread state is "  + thread.getState().toString());

		String a = thread.getName();
		System.out.println("thread name " + a);
		//		thread.interrupt();
		//	System.out.println("Thread state is "  + Thread.currentThread().getState().toString());
		if (D)Log.d(TAG,"Unregister receiver..."); 
		unregisterReceiver(mReceiver);
		stopSelf();

	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery find a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				System.out.println("This is the current array" + mArrayOfMacs.toString());
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
				if (mArrayOfMacs.isEmpty()){
					System.out.println("AtrraY is Empty");
					//add mac address to the list
					mArrayOfMacs.add(mDevice.getAddress());

				}
				else {
					for(int i=0 ;i<mArrayOfMacs.size() && !mFound;i++) {

						System .out.println("item " +i+" inth array is" + mArrayOfMacs.get(i));
						if (mArrayOfMacs.get(i).equalsIgnoreCase(mDevice.getAddress())) {

							System.out.println("device " + mDevice.getAddress() + "is in the list");


							mFound = true;
							if (D) Log.v(TAG, "The device " + mDevice.getAddress() + " is already in the list -- ignore this mac");	
						}



					}


					//						System.out.println("update device  " + device.getAddress() +" with RSSI: "+mRssi );

				}

				if (!mFound ) {


					System.out.println("The device " + mDevice.getAddress() + " adding to array");
					System.out.println("This is the current array" + mArrayOfMacs.toString());
					if (mArrayOfMacs.size() >= 1) mArrayOfMacs.add(mDevice.getAddress());

					if (D) Log.v(TAG,"Going to update server with device " + mDevice.getAddress());

					/*
					 * update the server with
					 * selfmac:found mac:found time
					 */

					new QueryIdentityFromServer().execute(mSelfMac,mDevice.getAddress());

				}


				mFound = false;


				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

				if (D) Log.d(TAG, "Finished scan devices");
				isBTRunning =true;
			}

		}

	};


	/** method for test client binder */
	public ArrayList<String>  getWhiteListOfPhones() {

		return mArrayOfMacs;

	}

	private class QueryIdentityFromServer extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected  String doInBackground(String...string) {

			dd("dd");
			String s = null;
			System.out.println("This is s1 " + string[0]);
			System.out.println("This is s2 "+ string[1]);




			try {
				executeHttpPost(URL_STRING, utils.buildJson(string[0],string[1],System.currentTimeMillis()));


			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return s;
		}

		@Override
		protected void onPostExecute(String s) {

		}
	}


	public void dd(String a){


		System.out.println("ONLY A TEST");

	}
	public String executeHttpPost(String url, JSONStringer postParameters) throws Exception {
		//   public String executeHttpPost(String url) throws Exception {

		BufferedReader in = null;
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(url);
			String s = "found_nearby=";            
			StringEntity entity = new StringEntity(s+ postParameters.toString());                   
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-Type", "application/x-www-form-urlencoded");
			request.setEntity(entity);
			 try {
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent())); 
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();

			String result = sb.toString();
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * send broadcast to application with Error number 
	 * all errors  in Errors.java* 
	 * @param e as int
	 */
	public void  BroadcastErrors(int e) {

		Intent intent = new Intent("com.ad.proxymi.ERRORS");	
		intent.putExtra("Error", e);
		LocalBroadcastManager.getInstance(getApplicationContext());
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

		/*
	    Implement this on the reciever side

	         LocalBroadcastManager.getInstance(this).registerReceiver(

            mMessageReceiver, new IntentFilter("speedExceeded"));
}

private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

    @Override

    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        Double currentSpeed = intent.getDoubleExtra("currentSpeed", 20);

        Double currentLatitude = intent.getDoubleExtra("latitude", 0);

        Double currentLongitude = intent.getDoubleExtra("longitude", 0);

        //  ... react to local broadcast message

    }

};

		 */
	}


/**
 * Set log Enable/Disable
 * @return void
 */
	
    public void  setLog(boolean print){
    	
    	if (print) D = true;
    	else D = false;    	
    }
	
	public int getRandomNumber() {
		// TODO Auto-generated method stub
		return 222;
	}
	
	private void printToLog(String msg){

		if (D) Log.v(TAG, msg);
	}

}





