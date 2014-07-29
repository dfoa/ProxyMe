/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.BluetoothChat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class DeviceListActivity extends Activity {
    // Debugging
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;
    public boolean found = false;
    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter mBluetoothAdapter = null;
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
	private String HomeNumber = "1111";
	private String WorkNumber = "2222";
	private String emailID ;
	private String company = "bad";
	private String jobTitle = "abcd";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        setUpActionBar();
        // Setup the window
 //      requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
 //       getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.main);
    
       getOverflowMenu();
       
       
       
       // Get local Bluetooth adapter
       mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

       // If the adapter is null, then Bluetooth is not supported
       if (mBluetoothAdapter == null) {
           Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
           finish();
           return;
       }
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
        
        registerForContextMenu(lv1);
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
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        



        
        
        
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
//            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
//                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
              System.out.println("These are paired devices\n" + device.getName() + "\n" + device.getAddress());
        }
        } else {
      //      String noDevices = getResources().getText(R.string.none_paired).toString();
       //     mPairedDevicesArrayAdapter.add(noDevices);
             System.out.println("No paired devices");
  
        }
    
    }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
 //        setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
 //       findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }
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
                
                if (device.getBondState() != BluetoothDevice.BOND_BONDED  ) {
                	
                	 if (holdMacs.contains(device.getAddress()))
          					 {
          				 
          				System.out.println("The device is already in the list");		 
          			 found = true;
            			      
         				 }
                	
                	
            	for (pos = 0 ; pos< mNewDevicesArrayAdapter.getCount();pos++)
            	{
              	
            		 System.out.println("Thsissssss" +mNewDevicesArrayAdapter.getItem(pos));
            		
            		if (mNewDevicesArrayAdapter.getItem(pos).contains(device.getAddress()))
                				 {
              			 System.out.println("The device is already in the list");
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
                doDiscovery();
            	
               
            	}
           
            	 return true;
                
            case R.id.discoverable:
            	 showToast("Discoverable");
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
                
            case R.id.my_card:
           	
             //open card details
               cardActions();
               return true;
               
            case R.id.edit:
               	
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
    public void showToast(String msg){
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
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
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
			  System.out.println("This is cards details " + responseBody);
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

     	 System.out.println("This is the card I am going to  bring information for" + card);
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
			
			 System.out.println("This is the card I am going to  bring information for" + card);
			if (card!=null) {
			  String arr[] = card.split("&");
			
			 
			  String name = arr[1].toString().replace("name=" , " ");
			  String phone = arr[2].toString().replace("phone=" , " ");
			  String email = arr[3].toString().replace("email=" , " ");
			  String site = arr[4].toString().replace ("site=" , " ");
			  String pic = arr[5].toString().replace ("pic=" , " ");
			  
			   
			  card = name + "\n" + phone +  "\n" +email + "\n"  +site;
			  
///////////////////////////////////////////////////
		        
			  GetSearchResults(name,phone,email,site,pic);




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
	//	  httppost.addHeader("Accept-Encoding", "gzip");
 
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("mac", mac));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				int responseCode = response.getStatusLine().getStatusCode();
				switch(responseCode)
				{
				    case 200:
				        HttpEntity entity = response.getEntity();
				 if(entity != null)
				 {
			     
				  card = EntityUtils.toString(entity);
				  

			
			  
				  
				  
				 
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
        }
    }
    
    private void GetSearchResults(String name,String phone,String email,String site,String pic){
      
    	//update global variables
    	DisplayName = name;
    	emailID = email;
    	MobileNumber = phone;
    	ItemDetails item_details = new ItemDetails();
    	item_details.setName(name);
    	item_details.setItemDescription(phone);
    	item_details.setPrice(email);
    	item_details.setSite(site);
    	item_details.setImg(pic);
    	results.add(item_details);
        
    	lv1.setAdapter(lAdapter);
    	lAdapter.notifyDataSetChanged();
        
    	
	//     ArrayList<ItemDetails> image_details = GetSearchResults();
	        
 
	        
	        
		
	        
	        
	     
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
          super.onCreateContextMenu(menu, v, menuInfo);
          if (v.getId()==R.id.listV_main) {
              MenuInflater inflater = getMenuInflater();
              inflater.inflate(R.menu.menu_list, menu);
          }
    }
    
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
          AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
          switch(item.getItemId()) {
             case R.id.save_contacts:
             // add stuff here
            //	saveToContacts();
   ////
       
       

                 ArrayList<ContentProviderOperation> ops = 
                     new ArrayList<ContentProviderOperation>();

                 ops.add(ContentProviderOperation.newInsert(
                     ContactsContract.RawContacts.CONTENT_URI)
                     .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                     .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                     .build()
                 );

                 //------------------------------------------------------ Names
                 if(DisplayName != null)
                 {           
                     ops.add(ContentProviderOperation.newInsert(
                         ContactsContract.Data.CONTENT_URI)              
                         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                         .withValue(ContactsContract.Data.MIMETYPE,
                             ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                         .withValue(
                             ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,     
                             DisplayName).build()
                     );
                 } 

                 //------------------------------------------------------ Mobile Number                      
                 if(MobileNumber != null)
                 {
                     ops.add(ContentProviderOperation.
                         newInsert(ContactsContract.Data.CONTENT_URI)
                         .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                         .withValue(ContactsContract.Data.MIMETYPE,
                         ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                         .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                         .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, 
                         ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                         .build()
                     );
                 }

                                     //------------------------------------------------------ Home Numbers
                                     if(HomeNumber != null)
                                     {
                                         ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                                 .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                                 .withValue(ContactsContract.Data.MIMETYPE,
                                                         ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                                 .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
                                                 .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, 
                                                         ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                                                 .build());
                                     }

                                     //------------------------------------------------------ Work Numbers
                                     if(WorkNumber != null)
                                     {
                                         ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                                 .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                                 .withValue(ContactsContract.Data.MIMETYPE,
                                                         ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                                 .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
                                                 .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, 
                                                         ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                                                 .build());
                                     }

                                     //------------------------------------------------------ Email
                                     if(emailID != null)
                                     {
                                          ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                                     .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                                     .withValue(ContactsContract.Data.MIMETYPE,
                                                             ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                                                     .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                                                     .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                                                     .build());
                                     }

                                     //------------------------------------------------------ Organization
                                     if(!company.equals("") && !jobTitle.equals(""))
                                     {
                                         ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                                 .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                                 .withValue(ContactsContract.Data.MIMETYPE,
                                                         ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                                                 .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                                                 .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                                                 .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                                                 .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                                                 .build());
                                     }

                                     // Asking the Contact provider to create a new contact                  
                                     try 
                                     {
                                         getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                                         
                                         Toast.makeText(this, "Card was saved", Toast.LENGTH_LONG).show();  
                                     } 
                                     catch (Exception e) 
                                     {               
                                         e.printStackTrace();
                                         Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                     }

            	 
            	 
            	 
            	 
  /////
            	 
            	 
            	 return true;
              case R.id.edit:
                // edit stuff here
                    return true;
              default:
                    return super.onContextItemSelected(item);
          }
    }
 
  private void   saveToContacts ()
  {
	  SavetoContacts contact = new SavetoContacts();
	  contact.DisplayName = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	 
	  contact.create(getApplicationContext());
  }
    
}
    

