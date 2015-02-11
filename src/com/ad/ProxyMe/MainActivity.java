package com.ad.ProxyMe;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.ad.ProxyMe.DiscoveryService.LocalBinder;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;

/*************************************************
**Only if using API 21
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
*/

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;





public class MainActivity extends Activity {

	private String host = "192.168.50.5";
	private boolean mDeviceExist = true;
	private boolean isBTRunning = false;
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	private Handler mHandler;
	private static final long SCAN_PERIOD = 5000;
	private static final int REQUEST_ENABLE_BT = 1;
	protected static final String TAG = "MainActivity";
	protected static final Boolean D = true;
	protected static final long DELAYBT = 60000;
	private ListView mListView;
	private ArrayAdapter<String> mNewDevicesArrayAdapter;
	private ArrayList<String> mDevicesArray;
	private String mFound;
	private String mFinished;
	public   BluetoothDevice  	 device ;   
	private static CustomHttpClient mCustomHttpClient;
    DiscoveryService mService;
    boolean mBound = false;
    private ServiceManager m;


    /** Defines callbacks for service binding, passed to bindService() */
////    private ServiceConnection mConnection = new ServiceConnection() {
////
////        @Override
////        public void onServiceConnected(ComponentName className,
////                IBinder service) {
////            // We've bound to LocalService, cast the IBinder and get LocalService instance
////        	System.out.println("Service is connected");
////            LocalBinder binder = (LocalBinder) service;
////            mService = binder.getService();
////            
////            new Thread(new Runnable() { 
////                public void run(){        
////                System.out.println("In thread");
////                for(;;) {
////            	int a = mService.getRandomNumber();
////        		System.out.println("get response from service " + a);
////        		try {
////					Thread.sleep(3000);
////				} catch (InterruptedException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////                }
////                }
////            }).start();
//
//            
//
//    		
//    		
//            mBound = true;
//            
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName arg0) {
//        	System.out.println("Service is disconnected");
//            mBound = false;
//        }
//    };
//
	
	
	  @Override
	    protected void onStart() {
	        super.onStart();
	     //   m.start();
	        // Bind to LocalService
//	        Intent intent = new Intent(this, DiscoveryService.class);
//	        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	    }

	    @Override
	    protected void onStop() {
	        super.onStop();
	        //stopService();
	        m.stop();
	        // Unbind from the servce
	        //if (mBound) {
//	            unbindService(mConnection);
	  //          mBound = false;
//	        }
	    }
	    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
 	 
	//
//		Register register = Register.getInstance(); 
//		register.withInfo(mBluetoothAdapter.getAddress(), "test1");

		/*Request bluetooth request - This should be implement in main activty	
 * 
 * 	

		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
 */			
//	    startService();

		
	m = new ServiceManager(getApplicationContext());
	 m.start();
		
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
	


	private void printToLog(String msg){

		if (D)
			Log.v(TAG, msg);
	}


//	private class GetUniqueIDFromServer extends AsyncTask<String, Integer, Integer>{
//
//
//
//		@Override
//		protected void onPreExecute(){
//			super.onPreExecute();
//
//		}
//		@Override
//		protected Integer doInBackground(String... params) {
////connect with server and bring information
//			String mResult;
//			//i = postData(params[0]);
//
//			//return i;
//			mResult = CustomHttpClient.executeHttpGet(host+"/device/"+device.getAddress());
//			
//		}
//
//		protected void onPostExecute(Integer result){
//
//			super.onPostExecute(result);
//			//		Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
//
//			if (result==1) {
//				//    	 pBar.setMessage("Server is not reachable..");
//
//				
//				System.out.println("Server is not reachable");
//			
//			
//			}
//
//                    
//
//
//			
//		
//				}
////			}
//		}
//		protected void onProgressUpdate(Integer... progress){
//
//
//		}
//
//
//			//			  httppost.addHeader("Accept-Encoding", "gzip");
//
//			try {
//				// Add your data
//				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//				nameValuePairs.add(new BasicNameValuePair("mac", mac));
//				nameValuePairs.add(new BasicNameValuePair("selfmac", myMac));
//
//				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//				httppost.addHeader("Accept-Encoding", "gzip");
//				// Execute HTTP Post Request
//				System.out.println("Print httppost");
//				response = httpclient.execute(httppost);
//
//				if (response!=null){
//					System.out.println("not Null");
//
//					int responseCode = response.getStatusLine().getStatusCode();
//					switch(responseCode)
//					{
//					case 200:
//
//						HttpEntity entity = response.getEntity();
//
//						InputStream instream = entity.getContent();
//						org.apache.http.Header contentEncoding = response.getFirstHeader("Content-Encoding");
//						if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
//							instream = new GZIPInputStream(instream);
//						}
//
//						if(entity != null)
//						{
//
//							card = convertStreamToString(instream);
//							//  card = EntityUtils.toString(entity);
//
//
//							//  System.out.println("This is cards details " + responseBody);
//
//						}
//						break;
//
//					case 500:
//						card=null;
//						break;
//
//					} 
//				}
//				else {
//					System.out.println("This is a mess");
//				}
//
//
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//			} catch (IOException e) {
//				//   System.out.println("server is not reachable");
//				//		    pBar.cancel();
//				return 1;
//
//
//
//				// TODO Auto-generated catch block
//			}
//			return 0;
//		}
//
//	}   



//}



}
