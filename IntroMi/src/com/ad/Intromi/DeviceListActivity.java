


package com.ad.Intromi;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.sax.TextElementListener;
import android.support.v4.app.FragmentActivity;
	


public class DeviceListActivity extends Activity{
	
	private  static boolean isBTRunning;
	private static int DELAYBT = 300000;
    // Debugging
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;
    public boolean found = false;
    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final int REQUEST_ENABLE_BT = 3;
    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private ArrayList<ItemDetails> results;
   
    private ItemListBaseAdapter lAdapter;
    private String card;
    private String holdMacs="macacdcdcdcdcdcdcdc";
    private ListView lv1;
	private String DisplayName ;
	private String MobileNumber;
	private String HomeNumber = "";
	private String WorkNumber = "";
	private String emailID ;
	private String company = "";
	private int mFoundDevice;


	int mStackLevel  = -1;
	private short mRssi =0;
	private Intent intent;
	private boolean mBTena;
	private LinearLayout layout ;
	private ProgressBar pBar;
	private HttpResponse response;
    private boolean firstTime = true;
	private BluetoothDevice device;
	private EditText inputSearch;
	private String fileName = "cards.bin";
	private String histoyFileName = "history.bin"; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

           
       
        // Setup the window
//       requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
       setContentView(R.layout.main);
       
       
       
mBTena = true ;




       
       pBar = (ProgressBar) findViewById(R.id.progressBarScan);
       pBar.setProgress(50);	
//       pb.setVisibility(View.GONE);
/*       
       if (savedInstanceState == null) {
           FragmentManager fragmentManager = getFragmentManager();
           // Or: FragmentManager fragmentManager = getSupportFragmentManager()
           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
           tt fragment = new tt();
 //          fragmentTransaction.replace((R.id.fragment_container, fragment);
           fragmentTransaction.commit();
       }
  
         
 layout = (LinearLayout) findViewById(R.id.linearLayout1);

 layout.setOnClickListener(new OnClickListener() {
	 
	 
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		 editCard();
	}
});
*/
//setUpActionBar();  	   
    
       
       
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy);
        
        getOverflowMenu();
       
       
 
        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Initialize the button to perform device discovery
//        Button scanButton = (Button) findViewById(R.id.se);
 //       scanButton.setOnClickListener(new OnClickListener() {
 //           public void onClick(View v) {
 //               doDiscovery();
 //              v.setVisibility(View.GONE);
 //           }
 //       });
        
        

      
      

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
 //    inputSearch = (EditText)findViewById(R.id.autoCompleteTextViewSearch)
        ;
        results = new ArrayList<ItemDetails>();
     	lv1 = (ListView) findViewById(R.id.listV_main); 
     	
     	
     	  lv1.setOnItemClickListener(new OnItemClickListener() {

  	        public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
  	                long arg3) {
  	        	
  	        	if (pos == 0)
  	        	{
  	        		 intent = new Intent(DeviceListActivity.this, CardDetails.class);
  	        	startActivity(intent);
  	        }
  	        	else {
  	      	ItemDetails  item= results.get(pos);
  		    intent = new Intent(DeviceListActivity.this, DetailActivity.class);
  			intent.putExtra("url", item.getName());
  			intent.putExtra("title", item.getPrice());
  			intent.putExtra("desc", item.getItemDescription());
  			intent.putExtra("head_line",item.getPrfessionalHeadLine());
  			intent.putExtra("site",item.getSite());
  			intent.putExtra("mission",item.getmission());
  			intent.putExtra("site", item.getSite());
  			intent.putExtra("file_name", fileName);
			
			intent.putExtra("name",item.getName());
			new MyAsyncTask1().execute(item.getImg());
  	        	}

 

  	        }
  	    });
     	  
   /*  	 	
     	 inputSearch.addTextChangedListener(new TextWatcher() {
     			
     			@Override
     			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
     				// When user changed the Text
     				System.out.println("Text was chnaged");	
     				setSearchResult(cs.toString());
         			
     			}
     			
     			@Override
     			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
     					int arg3) {
     				// TODO Auto-generated method stub
     				
     				
     			}
     			
     			@Override
     			public void afterTextChanged(Editable arg0) {
     				// TODO Auto-generated method stub	
     			
     			}
     		});
     		
     */	  
     	  
     	  
        
 //       registerForContextMenu(lv1);
     	lAdapter = new ItemListBaseAdapter(this,results);
     	
//        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        
        


        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
 
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        
        this.registerReceiver(mReceiver, filter);
       
       //put my profile at the head of the list  
       //this function already knows how to insert values ans update adapter.
           
    //check if user has a card
       //startDiscovery(); 
        //check if card already exist , just load the details 
        System.out.println("checking if file exist");
    	final String filename = "card.bin";
        File file = getBaseContext().getFileStreamPath(filename);
        if(file.exists()) {
   	    	//read the card and show  details
   	     if(D) Log.e(TAG, "+++ Load self card");
   	      
   
      
   	     
   	     
   	 
   	  System.out.println("loading card");
   	    Profile profile = new Profile() ;
   	    profile = loadCard();
   	    
   	    
   	    
   	  GetSearchResults(profile.getMacHw() ,profile.getName(),profile.getMobilePhoneNum(),profile.getEmail(),profile.getSite(),profile.getProfessionalHeadLine() ,profile.getMission(),profile.getPicture() );
   	    
   	    
   	     
   	     
        }
       
        else  {
        	System.out.println("Self profile does not exist");
        	 GetSearchResults("ss" ,"New user ?" ,"ssd","sdsd","sdsds","Click here" ,"To Complete your profile", null);
        }
        

        // Get the local Bluetooth adapter
 if (mBTena)       mBtAdapter = BluetoothAdapter.getDefaultAdapter();


 if (mBTena){
	 
	
	
	 new Thread(new Runnable() { 
         public void run(){
         
             {
                System.out.println("Into thread");
               isBTRunning = true;
               Looper.prepare(); 
               BluetoothAdapter mBluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
             
               try { 
                   Log.d(TAG, "BT Scanning started");

                   while(isBTRunning)
                   { 
                
                   if (!mBluetoothAdapter.isEnabled())
                   {
                           mBluetoothAdapter.enable();  
                           
                                
                   }
                   Log.d(TAG,"Register..."); 
      
                   showToast("Start discovery");
                   Log.d(TAG,"Start discovery");
                   
                // mBluetoothAdapter.startDiscovery();
                  startDiscovery();
                   Log.d(TAG,"sleep for " + DELAYBT + " seconds");
                   
                   Thread.sleep(DELAYBT);
                   
                   Log.d(TAG,"Unregister..."); 
//                     unregisterReceiver(mReceiver);
                }
                   Looper.loop();
               }
               catch (InterruptedException e) {
                   Log.d(TAG, "BT Scanning stopped");
                       Looper.myLooper().quit();
               }

             }     

         }
    
 }).start();
	 
	
	 
	 /////
 }
	 
	
	  
     
        
        
 
        }


    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
   if (mBTena)  {  
       if (!mBtAdapter .isEnabled()) {
//           Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//           startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
           mBtAdapter.enable();
        // Otherwise, setup the chat session
        }
 //     mBtAdapter.setName("IntroMi");
      ensureDiscoverable();
     // startDiscovery();
      
    }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
            Thread.interrupted();
        }
        

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
   

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
   
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
             int pos ;
             System.out.println("In broadcast reciev");
             found = false;
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            	System.out.println("device found");
                // Get the BluetoothDevice object from the Intent
            	
            	
            	//IntroMi
         	
            	 device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
             //   System.out.println("This is the name of the device " +device.getName());
                // if (device.getName().equalsIgnoreCase("IntroMi")) {
            	//  mRssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                 
                if (D) Log.v(TAG, "rssi:"+ mRssi);
                // If it's already paired or already listed, skip it, because it's been listed already
     //           System.out.println("This is the device name" + device.getName());
     //           System.out.println("This is the length of the name :\n" +  device.getName().length());
    //            showToast("Device name is\n " + device.getName() +"length:\n" + device.getName().length());
               
                if (device.getBondState() != BluetoothDevice.BOND_BONDED  ) {
                	
                	 if (holdMacs.contains(device.getAddress()))
          					 {
          				 
          				if (D) Log.v(TAG, "The device" + device.getAddress() +"is already in the list");		 
          			 found = true;
            			      
         				 }
                	
                	
            	for (pos = 0 ; pos< mNewDevicesArrayAdapter.getCount();pos++)
            	{
              	
            		 System.out.println("Thsissssss" +mNewDevicesArrayAdapter.getItem(pos));
            		
            		if (mNewDevicesArrayAdapter.getItem(pos).contains(device.getAddress()))
                				 {
              			 if (D) Log.v(TAG,"The device" + device.getAddress() +"is already in the list");
              			 found = true;
                			      
             				 }
            			
            		
                	
             	
            	}
            	if (!found ) {
            		
            	holdMacs = holdMacs+device.getAddress()+ "=";	
            	//	 mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            		if (D) Log.v(TAG,"found device and going to look for information Asynctask with " + device.getAddress());
           //Save this card to history file
            	
            		new MyAsyncTask().execute(device.getAddress());
 /*           		 new Thread(new Runnable() { 
            	         public void run(){
            	         
            	             {
            	            	 int count =0;
            	                System.out.println("Into thread");
            	              
            	               Looper.prepare(); 
            	               
            	             
            	               try { 
            	                   Log.d(TAG, "BT Scanning started");

            	                   while(true &&count<20)
            	                   { 
            	                	  ++count; 
            	                 		new MyAsyncTask().execute(device.getAddress());
            	                   Log.d(TAG,"Register..."); 
            	      
            	       
            	              
            	                   Log.d(TAG,"sleep for " + DELAYBT + " seconds");
            	                   
            	                   Thread.sleep(2000);
            	                   
            	                   Log.d(TAG,"Unregister..."); 

            	                   }
            	            //       Looper.loop();
            	               }
            	               catch (InterruptedException e) {
            	                   Log.d(TAG, "BT Scanning stopped");
            	                       Looper.myLooper().quit();
            	               }

            	             }  

            	         }
            	    
            	 }).start();	
            		
      */
          
            
            		
            		
            	
            	}
                }
            	
            	   
           //     }
                 else {
                	  if (D) Log.v(TAG,"found blletooth device but dont have IntoMI application"); 
                		 
                 }
                
            // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
         //       setProgressBarIndeterminateVisibility(false);
                //showToast("finish discovery...");
            	
                if (D) Log.v(TAG,"finish discovery");
                pBar.setVisibility(View.GONE);       
        //        if(null!=pBar && pBar.isShowing()){
                
 //             	  pBar.dismiss();
 //               }
             
           setTitle("Below cards were found");
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            
            }
            
        }
    
    };
    
    

    ////
    
    // inflate for action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	  MenuInflater inflater = getMenuInflater();
          inflater.inflate(R.menu.main, menu);
   
          return super.onCreateOptionsMenu(menu);
    }
 
    // handle click events for action bar items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        switch (item.getItemId()) {
 
 
            case R.id.secure_connect_scan:
            	
            	//check id network connection
            	if (!isNetworkAvailable())
                   {
                   	   Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_SHORT).show();
                  
                   	
                   }
            	else {
                showToast("Searching cards");
 //               new Thread(new Runnable() { 
 //                   public void run(){

                    	 


 //                   }
 //           }).start();

           
               
            //    startService(new Intent(this, DiscoveryService.class));
            	
                if (mBtAdapter.isDiscovering()) {
                    mBtAdapter.cancelDiscovery();
                    System.out.println("discovery still running, cannot start ...");

                }
                else

                // Request discover from BluetoothAdapter
                	startDiscovery();
                
              
            	 
              
            	}
            	return true;
//            case R.id.discoverable:
//            	 showToast("Discoverable");
                // Ensure this device is discoverable by others
//                ensureDiscoverable();
//               return true;
                
//            case R.id.settings:
           	
             //open card details
            	//call Settings intent
//            	   Intent  settings = new Intent(this, Settings.class); 
//                   startActivity(settings);
                
//               return true;
       
               
 //              case R.id.future_card:
 //
 //              Intent  s = new Intent(this, Cosmo.class); 
 //              startActivity(s);
            
 //          return true;
           

                  
                case R.id.saved_cards:
                    //open card details
                	  Intent  g = new Intent(this, SavedCardsList.class); 
                      startActivity(g);
                      return true;   
                  
                case R.id.my_card:
                //open card details
                  editCard();
                  return true;
               
                
           default:
                return super.onOptionsItemSelected(item);          
                
           
        
              
      
        }
    
    }
    // put the other two menu on the three dots (overflow)
    private void getOverflowMenu() {
 
        try {
 
           ViewConfiguration config = ViewConfiguration.get(this);
           java.lang.reflect.Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
           if(menuKeyField != null) {
               menuKeyField.setAccessible(true);
               menuKeyField.setBoolean(config, false);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
 
     }
 
    // so that we know something was triggered
    public  void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    //////
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
 
            	
            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    
    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBtAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,0);
            startActivity(discoverableIntent);
          
        }
    }
    

    
   private void  editCard() {
	 
       if(D) Log.d(TAG, "edit card");
       
     Intent  intent_card = new Intent(this, CardDetails.class);
//      Intent  intent_card = new Intent(this, MyCards.class); 
      startActivity(intent_card);
	   
   }
    
    
    
    public void  retrievCardDetails(String mac) throws IOException {
        

    	
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	nameValuePairs.add(new BasicNameValuePair("mac",mac));

        
//      String URL_STRING="http://192.168.0.151:8080/map/jsp/passwordget.jsp";
        String URL_STRING="http://192.168.50.5/cgi-bin/get_card.cgi";

        final HttpClient hc = new DefaultHttpClient();
        final HttpPost post = new HttpPost(URL_STRING);
        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
        
        new Thread(new Runnable() {
            public void run() {
        
        try {
			 HttpResponse rp = hc.execute(post);
		
			int responseCode = rp.getStatusLine().getStatusCode();
			switch(responseCode)
			{
			    case 200:
			        HttpEntity entity = rp.getEntity();
			 if(entity != null)
			 {
			  String responseBody = EntityUtils.toString(entity);
//			  System.out.println("This is cards details " + responseBody);
		       show_card_results(responseBody);	             
			        }
			        break;
			} 
        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
       
         
        
        
            }
        }).start();
	
		
		
       
    } 
    
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) 
          getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    
    
  //Body of your click handler

    } 
    
    
    public void show_card_results(String result)
    {
    	card = result;

     //	 System.out.println("This is the card I am going to  bring information for" + card);
     	mNewDevicesArrayAdapter.add(result);
    }
    
    
    private class MyAsyncTask extends AsyncTask<String, Integer, Integer>{
    	 
  
    	
    	@Override
    	protected void onPreExecute(){
    		super.onPreExecute();
    		//pBar.setMessage("Found Card " + mFoundDevice + "  Loading profile");
    	
    		++mFoundDevice;
    	
    	}
		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			int  i = 1;
			i = postData(params[0]);
			
			return i;
		}
 
		protected void onPostExecute(Integer result){
			
             super.onPostExecute(result);
	//		Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
			
             if (result==1) {
        //    	 pBar.setMessage("Server is not reachable..");
                 System.out.println("Server is not reachable");
                 mBtAdapter.cancelDiscovery();
                 holdMacs = "a";
             }
             
             
             
             
	//	 System.out.println("This is the card I am going to  bring information for" + card);
			if (card!=null) {
				
			 Profile p = new Profile();
			  String arr[] = card.split("&");
			  
			
			  String mac =      arr[0].toString().replace("mac=" , ""        );
			  String name =     arr[1].toString().replace("name=" , ""      );
			  String phone =    arr[2].toString().replace("phone=" , ""     );
			  String email =    arr[3].toString().replace("email=" , ""     );
			  String site =     arr[4].toString().replace ("site=" , ""     );
			  String head_line =arr[5].toString().replace ("head_line=" , "");
			  String mission =  arr[6].toString().replace ("mission=" , ""  );
			  String pic =      arr[7].toString().replace ("pic=" , ""      );
			  
			  p.setMacHw(arr[0].toString().replace("mac=" , ""        ));
			  p.setName(arr[1].toString().replace("name=" , ""        ));
			  p.setMobilePhoneNum(arr[2].toString().replace("phone=" , ""     ));
			  p.setEmail(arr[3].toString().replace("email=" , ""     ));
			  p.setSite(arr[4].toString().replace ("site=" , ""     ));
			  p.setProfessionalHeadLine(arr[5].toString().replace ("head_line=" , ""));
			  p.setMission(arr[6].toString().replace ("mission=" , ""  ));
			  p.setPicture(arr[7].toString().replace ("pic=" , ""      ));
			  
			  card = name + "\n" + phone +  "\n" +email + "\n"  + site +"\n" + head_line +"\n" + mission + "\n";
	         
			  SaveToHistory(p);
		//	  pBar.setMessage("Scanning please wait....");
///////////////////////////////////////////////////
		        
			  System.out.println("This is the mission:"+mission);
			  GetSearchResults(mac,name,phone,email,site,head_line,mission,pic);

    
              
			  


////////////////////////////
	
			 
			 
			 
//		     	mNewDevicesArrayAdapter.add(card);
//			mNewDevicesArrayAdapter.notifyDataSetChanged();
		}
		}
		protected void onProgressUpdate(Integer... progress){
			
	
		}
 
		public int postData(String mac) {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			
	//HttpPost httppost = new HttpPost("http://192.168.50.5/cgi-bin/get_card.cgi");
			HttpPost httppost = new HttpPost("http://dfoa.ssh22.net/cgi-bin/get_card.cgi");
			
//		  httppost.addHeader("Accept-Encoding", "gzip");
 
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("mac", mac));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.addHeader("Accept-Encoding", "gzip");
				// Execute HTTP Post Request
				System.out.println("Print httppost");
				response = httpclient.execute(httppost);
				
				if (response!=null){
				 System.out.println("not Null");

				int responseCode = response.getStatusLine().getStatusCode();
				switch(responseCode)
				{
				    case 200:
				       
				   	HttpEntity entity = response.getEntity();

				        InputStream instream = entity.getContent();
				        org.apache.http.Header contentEncoding = response.getFirstHeader("Content-Encoding");
				        if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				            instream = new GZIPInputStream(instream);
				        }
				        
				        if(entity != null)
				 {
			     
				       	card = convertStreamToString(instream);
				//  card = EntityUtils.toString(entity);
				  

			
			  
				  
				  
				 
				//  System.out.println("This is cards details " + responseBody);
			      	             
				        }
				        break;
				        
				    case 500:
				    	card=null;
				   break;
				      
				} 
				}
				else {
					System.out.println("This is a mess");
				}
        				
				
				
				
				} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
			 //   System.out.println("server is not reachable");
	//		    pBar.cancel();
			    return 1;
			    
			
				
				// TODO Auto-generated catch block
			}
		return 0;
		}
		
	}
    
  
    
    
    private void setUpActionBar() {
        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            

        }
        }
    
    
    private void GetSearchResults(String mac,String name,String phone,String email,String site,String head_line, String mission , String pic ){
      
    	//update global variables
    	
    	DisplayName = name;
    	String img = pic;
    	String macHw = mac.toString();
    	String url  = "http://dfoa.ssh22.net/photo/photo/" + macHw + ".png";
    	emailID = email;
    	MobileNumber = phone;
    
    	
    	ItemDetails item_details = new ItemDetails();
    	item_details.setName(name);
    	item_details.setItemDescription(phone);
    	item_details.setPrice(email);
    	item_details.setSite(site);
    	item_details.setImg(img);
    	item_details.setProfessionlaHeadLine(head_line);
    	
    	item_details.setMission(mission);
    	item_details.setmRssi(String.valueOf(mRssi));
    	
    	results.add(item_details);
    	
    	
        
    	lv1.setAdapter(lAdapter);
    	lAdapter.notifyDataSetChanged();
       
    	
	//     ArrayList<ItemDetails> image_details = GetSearchResults();
	        
 
	        
	        
		
	        
	        
	     
    }

    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
          super.onCreateContextMenu(menu, v, menuInfo);
          if (v.getId()==R.id.listV_main) {
              MenuInflater inflater = getMenuInflater();
              inflater.inflate(R.menu.menu_list, menu);
          }
    }
    */
    


  
 
   
  private static String convertStreamToString(InputStream is) {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    try {
	      while ((line = reader.readLine()) != null) {
	        sb.append(line + "\n");
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    } finally {
	      try {
	        is.close();
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	    return sb.toString();
	  }
 
  public String getPhotoStringFromBitmap(Bitmap bm){
		 System.out.println("reach here  inside getPhotoStringFromBitmap");
		 String ba1 = null;;
			if (bm!=null)
			{
			Bitmap bitmapOrg = bm;
	     ByteArrayOutputStream bao = new ByteArrayOutputStream();
	     //Resize the image
	     double width = bitmapOrg.getWidth();
	     double height = bitmapOrg.getHeight();
	     double ratio = 200/width;
	     int newheight = (int)(ratio*height);
	     bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 200, newheight, true);
	     System.out.println("———-width" + width);
	     System.out.println("———-height" + height);
	     bitmapOrg.compress(Bitmap.CompressFormat.PNG, 95, bao);
	     byte[] ba = bao.toByteArray();
	     ba1 = Base64.encodeToString(ba, 0);

	       	return ba1;
			}
			else {
				 System.out.println("return null");
				return null;
			}
			
	 
	 }


  

  
 
  


  
  private class MyAsyncTask1 extends AsyncTask<Bitmap, Void, String>{
 	 
	  
  	
  	@Override
  	protected void onPreExecute(){
  		super.onPreExecute();
  		//pBar.setMessage("Found Card " + mFoundDevice + "  Loading profile");
  	
  		
  	
  	}
		@Override
		protected String  doInBackground(Bitmap... params) {
			// TODO Auto-generated method stub
		
		return   getPhotoStringFromBitmap(params[0]);
		
		}

		protected void onPostExecute(String result){
			
         //  super.execute(result);
	//		Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
			
	//	 System.out.println("This is the card I am going to  bring information for" + card);
			
			intent.putExtra("photo", result);
			
			
			
			
  			
  			
  			startActivity(intent);
           
		}
		protected void onProgressUpdate(Integer... progress){
			
	
	

	}
  }
  private void startDiscovery(){
	  
	  
	  mBtAdapter.startDiscovery();
        
	   pBar.setVisibility(View.VISIBLE);
//      pBar = new ProgressDialog(DeviceListActivity.this);
  
//		pBar.setMessage("Scanning please wait....");
		
		
//		pBar.show();        	
		 mFoundDevice =1; 
  }

  private  Profile  loadCard()
  {
  	final String filename = "card.bin";
  	 Profile  p = new Profile(); 
       FileInputStream fis = null;
  	 ObjectInputStream in = null; 
  		    try {
  		      fis = openFileInput(filename);
  		      in = new ObjectInputStream(fis);
  		      p = (Profile) in.readObject();
  		      in.close();
  		    } catch (Exception ex) {
  		      ex.printStackTrace();
  		      Toast.makeText(this, "cant open file to read" , Toast.LENGTH_SHORT).show();
  		    }

  		     
  //		     decode p.picture from BASE_64 to bitmap 
              
  		    //Bitmap b = decodeSampledBitmapFromPath(p.picture,100,100);
  		    if ( p.getPicture() != null) {
    		
  		      Bitmap b = setImg(p.getPicture());
  		 //    imageView.setImageBitmap(b);
  	//	    img = p.picture;
  	//	     WebView.setImageBitmap(setImg(p.picture));
  		    }
  		   
  		    
  		    System.out.println(p.getEmail() + p.getEmail() + p.getMobilePhoneNum() + p.getProfessionalHeadLine());
             return(p);		  
    }
  
  public Bitmap setImg(String img) {
		//decode from base_64 to real PNG format
           Bitmap bm;
				
				byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
				Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
			//	im.setImageBitmap(decodedByte);
				bm = decodedByte;
				return bm;
				
			    
			    	
		
	}
  

public   void setSearchResult(String str) {
  CardsListBaseAdapter    mAdapter = new CardsListBaseAdapter(this, null);
   ArrayList<ItemDetails> results1 = new ArrayList<ItemDetails>();
//   ItemDetails temp = new ItemDetails();
     for (ItemDetails temp  : results) {
    	  System.out.println("This is temp: " + temp.toString());
         if (temp.getPrfessionalHeadLine().contains(str)){
        System.out.println("match-- this is head line :" +temp.getPrfessionalHeadLine() + "and String is :" + str );
     
       
       results1.add(temp);
 //    lAdapter.notifyDataSetChanged();
  
      
    
        
         
  //  
  }
  

	
   

  
  }
    
 //    results = results1;
  
  //   lAdapter.notifyDataSetChanged();
}   


private void SaveToHistory (Profile p){
	
    Context c = getApplicationContext();
    SavedCards mHistoryCards = new SavedCards();
    boolean flag = true;
    boolean  reachedMaxProfiles = false;
    int maxProfiles = 50; 		
     Cards cards = new Cards(c,histoyFileName);
     //load list from file;
       mHistoryCards = cards.loadCards();
      if (mHistoryCards !=null) {
    	 
    	  if (mHistoryCards.profileArrayList.size() == maxProfiles-1)    	
    	  reachedMaxProfiles = true;
    	  {
    		  
    		  if (D)  Log.v(TAG,"History file reached 50  profiles, need to rotate");
    		   
    	  }
    		 
	 for (int ind =0 ;ind <mHistoryCards.profileArrayList.size() && flag;ind++) {
		 System.out.println("This is the mac i am looking" + p.getMacHw() + "and this is mac in the file "+ mHistoryCards.profileArrayList.get(ind).getMacHw() );
    if    (mHistoryCards.profileArrayList.get(ind).getMacHw().contentEquals(p.getMacHw())){
//That means that this card already exist in history card so we can ignore, 
    	
    if (D)  Log.v(TAG,"The card with Mac address" + p.getMacHw() + "already exist in history fileis " );
   	  flag=false;
    	 
    	 
     //  mHistoryCards.profileArrayList.add(p);
     //  cards.saveToFileBulk(mHistoryCards);
   
   	  
 
  
    }
    
  
	 }
	 
	  
	  if (flag  && reachedMaxProfiles)   mHistoryCards = cards.saveToFile(p,true);


	      
		 
	

}
      else    	 mHistoryCards = cards.saveToFile(p,false);
}
}

