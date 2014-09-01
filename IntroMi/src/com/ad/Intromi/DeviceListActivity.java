

package com.ad.Intromi;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceActivity.Header;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.FragmentActivity;
import com.ad.Intromi.R;


public class DeviceListActivity extends FragmentActivity{
    // Debugging
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;
    public boolean found = false;
    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private static final int REQUEST_ENABLE_BT = 3;
    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
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
	private String jobTitle = "";
    ActionBar.Tab tab1, tab2, tab3;
	Fragment fragmentTab1 = new FragmentTab1();
	Fragment fragmentTab2 = new FragmentTab2();
	Fragment fragmentTab3 = new FragmentTab3();
	int mStackLevel  = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
       
        // Setup the window
//       requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
 //       getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
       setContentView(R.layout.main);
/*       
       if (savedInstanceState == null) {
           FragmentManager fragmentManager = getFragmentManager();
           // Or: FragmentManager fragmentManager = getSupportFragmentManager()
           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
           tt fragment = new tt();
 //          fragmentTransaction.replace((R.id.fragment_container, fragment);
           fragmentTransaction.commit();
       }
  
         */
       setUpActionBar();  
    	   
    
       
       
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
        results = new ArrayList<ItemDetails>();
     	lv1 = (ListView) findViewById(R.id.listV_main); 
     	
     	
     	  lv1.setOnItemClickListener(new OnItemClickListener() {

  	        public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
  	                long arg3) {
  	      	ItemDetails  item= results.get(pos);
  			Intent intent = new Intent(DeviceListActivity.this, DetailActivity.class);
  			intent.putExtra("url", item.getName());
  			intent.putExtra("title", item.getPrice());
  			intent.putExtra("desc", item.getItemDescription());
  			intent.putExtra("head_line",item.getPrfessionalHeadLine());
  			intent.putExtra("site",item.getSite());
  			intent.putExtra("mission",item.getmission());
			intent.putExtra("photo", getPhotoStringFromBitmap(item.getImg()));
			intent.putExtra("name",item.getName());
			
  			
  			
  			startActivity(intent);
  	             Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
/*
 * 
 *     	item_details.setName(name);
    	item_details.setItemDescription(phone);
    	item_details.setPrice(email);
    	item_details.setSite(site);
    	item_details.setImg(img);
    	results.add(item_details);
 */

  	        }
  	    });
        
 //       registerForContextMenu(lv1);
     	lAdapter = new ItemListBaseAdapter(this,results);
	   
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
  
      
        // Find and set up the ListView for newly discovered devices
 //       ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
 //       newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
//        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
       
        
        

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        
        // Get a set of currently paired devices
 //       Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        



        
        
        
        // If there are paired devices, add each one to the ArrayAdapter
//        if (pairedDevices.size() > 0) {
//            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
//            for (BluetoothDevice device : pairedDevices) {
//                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//              System.out.println("These are paired devices\n" + device.getName() + "\n" + device.getAddress());
        }
//        } else {
      //      String noDevices = getResources().getText(R.string.none_paired).toString();
       //     mPairedDevicesArrayAdapter.add(noDevices);
//             System.out.println("No paired devices");
  
 //       }
    
//    }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
      
       if (!mBtAdapter .isEnabled()) {
           Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
           startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        }
  //     mBtAdapter.setName("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
//        if (mBtAdapter != null) {
//            mBtAdapter.cancelDiscovery();
//            Thread.interrupted();
//        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
   
/*
    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
*/
    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
             int pos ;
             found = false;
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            	System.out.println("device found");
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired or already listed, skip it, because it's been listed already
     //           System.out.println("This is the device name" + device.getName());
     //           System.out.println("This is the length of the name :\n" +  device.getName().length());
    //            showToast("Device name is\n " + device.getName() +"length:\n" + device.getName().length());
                
                if (device.getBondState() != BluetoothDevice.BOND_BONDED  ) {
                	
                	 if (holdMacs.contains(device.getAddress()))
          					 {
          				 
          				System.out.println("The device" + device.getAddress() +"is already in the list");		 
          			 found = true;
            			      
         				 }
                	
                	
            	for (pos = 0 ; pos< mNewDevicesArrayAdapter.getCount();pos++)
            	{
              	
            		 System.out.println("Thsissssss" +mNewDevicesArrayAdapter.getItem(pos));
            		
            		if (mNewDevicesArrayAdapter.getItem(pos).contains(device.getAddress()))
                				 {
              			 System.out.println("The device" + device.getAddress() +"is already in the list");
              			 found = true;
                			      
             				 }
            			
            		
                	
             	
            	}
            	if (!found ) {
            		
            	holdMacs = holdMacs+device.getAddress()+ "=";	
            	//	 mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            		System.out.println("found device and going to look for information Asynctask with " + device.getAddress());
            		new MyAsyncTask().execute(device.getAddress());
            
            		
            		
            	
            	}
            	
            	
            	   
            	
                }
            // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                showToast("finish discovery...");
                System.out.println("finish discovery");
                
             
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

           
               
                startService(new Intent(this, DiscoveryService.class));
            	         	
           
            	 return true;
              
            	}
            case R.id.discoverable:
            	 showToast("Discoverable");
                // Ensure this device is discoverable by others
                ensureDiscoverable();
               return true;
                
            case R.id.settings:
           	
             //open card details
            	//call Settings intent
            	   Intent  settings = new Intent(this, Settings.class); 
                   startActivity(settings);
                
               return true;
       
               
               case R.id.future_card:
 
               Intent  s = new Intent(this, Cosmo.class); 
               startActivity(s);
            
           return true;
           
                case R.id.my_card:
                //open card details
                  editCard();
                  return true;
                  
                case R.id.saved_cards:
                    //open card details
                	  Intent  g = new Intent(this, Cosmo.class); 
                      startActivity(g);
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
    
    private void cardActions() {
        if(D) Log.d(TAG, "card actions");
        
//       Intent  intent_card = new Intent(this, CardDetails.class);
        Intent  intent_card = new Intent(this, MyCards.class); 
        startActivity(intent_card);
     
        
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
    
    
    private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
    	 
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			postData(params[0]);
			return null;
		}
 
		protected void onPostExecute(Double result){
//			pb.setVisibility(View.GONE);
	//		Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
			
	//	 System.out.println("This is the card I am going to  bring information for" + card);
			if (card!=null) {
			  String arr[] = card.split("&");
			
			  String mac =      arr[0].toString().replace("mac=" , ""        );
			  String name =     arr[1].toString().replace("name=" , " "      );
			  String phone =    arr[2].toString().replace("phone=" , " "     );
			  String email =    arr[3].toString().replace("email=" , " "     );
			  String site =     arr[4].toString().replace ("site=" , " "     );
			  String head_line =arr[5].toString().replace ("head_line=" , " ");
			  String mission =  arr[6].toString().replace ("mission=" , " "  );
			  String pic =      arr[7].toString().replace ("pic=" , " "      );
			  
			   
			  card = name + "\n" + phone +  "\n" +email + "\n"  + site +"\n" + head_line +"\n" + mission + "\n";
			  
///////////////////////////////////////////////////
		        
			  GetSearchResults(mac,name,phone,email,site,head_line,mission,pic);




////////////////////////////
	
			 
			 
			 
//		     	mNewDevicesArrayAdapter.add(card);
//			mNewDevicesArrayAdapter.notifyDataSetChanged();
		}
		}
		protected void onProgressUpdate(Integer... progress){
//			pb.setProgress(progress[0]);
		}
 
		public void postData(String mac) {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			
		//	HttpPost httppost = new HttpPost("http://192.168.50.5/cgi-bin/get_card.cgi");
			HttpPost httppost = new HttpPost("http://dfoa.ssh22.net/cgi-bin/get_card.cgi");
//		  httppost.addHeader("Accept-Encoding", "gzip");
 
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("mac", mac));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.addHeader("Accept-Encoding", "gzip");
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

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
 
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
 
	}
    
  
    
    
    private void setUpActionBar() {
        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            
 /*         
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            
            tab1 = actionBar.newTab().setText("1");
            tab2 = actionBar.newTab().setText("2");
            tab3 = actionBar.newTab().setText("3");
            
            tab1.setTabListener(new MyTabListener(fragmentTab1));
            tab2.setTabListener(new MyTabListener(fragmentTab2));
            tab3.setTabListener(new MyTabListener(fragmentTab3));
            
            actionBar.addTab(tab1);
            actionBar.addTab(tab2);
            actionBar.addTab(tab3);
        
   */
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
    

 
  private void   saveToContacts ()
  {
	  SavetoContacts contact = new SavetoContacts();
	  contact.DisplayName = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	 
	  contact.create(getApplicationContext());
  }
  
 
   
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
		 
		 String ba1 = null;;
			if (bm!=null)
			{
			Bitmap bitmapOrg = bm;
	     ByteArrayOutputStream bao = new ByteArrayOutputStream();
	     //Resize the image
	  //   double width = bitmapOrg.getWidth();
	  //   double height = bitmapOrg.getHeight();
	  //   double ratio = 400/width;
	  //   int newheight = (int)(ratio*height);
	  //   bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 400, newheight, true);
	  //   System.out.println("———-width" + width);
	  //   System.out.println("———-height" + height);
	     bitmapOrg.compress(Bitmap.CompressFormat.PNG, 95, bao);
	     byte[] ba = bao.toByteArray();
	     ba1 = Base64.encodeToString(ba, 0);
	       	return ba1;
			}
			else {
				return null;
			}
			
	 
	 }

	
  public class FragmentTab1 extends Fragment {
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	                           Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.tab, container, false);
		TextView textview = (TextView) view.findViewById(R.id.tabtextview);
		textview.setText("tab1");
		return view;
	  }
	}
  
  public class FragmentTab2 extends Fragment {
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	                           Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.tab, container, false);
		TextView textview = (TextView) view.findViewById(R.id.tabtextview);
		textview.setText(R.string.email);
		return view;
	  }
	}
  
  public class FragmentTab3 extends Fragment {
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, 
	                           Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.tab, container, false);
		TextView textview = (TextView) view.findViewById(R.id.tabtextview);
		textview.setText(R.string.head_line);
		return view;
	  }
	}
  
  
  public class MyTabListener implements ActionBar.TabListener {
		Fragment fragment;
		
		public MyTabListener(Fragment fragment) {
			this.fragment = fragment;
		}
		
	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, fragment);
		}
		
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}
		
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// nothing done here
		}
	}
  
  public class tt extends Fragment {
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        return inflater.inflate(R.layout.tab , container, false);
	    }
	}

}
    

