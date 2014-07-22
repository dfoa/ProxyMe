package com.example.android.BluetoothChat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract.Constants;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CardDetails extends Activity {
    // Debugging
    private static final String TAG = "IntroMi";
    private static final boolean D = true;
    private ProgressBar pb;
    private static int RESULT_LOAD_IMAGE = 1;
    private String img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE card details+++ ");
        // Set up the window layout
        
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.my_card);
     
        final EditText name = (EditText) findViewById(R.id.nameEditText);
        final EditText phone = (EditText) findViewById(R.id.phoneEditText);
        final EditText email = (EditText)findViewById(R.id.emailEditText);
        final EditText site= (EditText)findViewById(R.id.siteEditText);
        Button btPickImage = (Button) findViewById(R.id.btImagepPick);
        Button saveRecords = (Button) findViewById(R.id.saveButton);
        
        pb = (ProgressBar)findViewById(R.id.deataSentProgressBar);	
        pb.setVisibility(View.GONE);

        saveRecords.setOnClickListener(new OnClickListener()
        {
          public void onClick(View v)
          {
  
     

      
        
                /* This code get the values from edittexts  */
           String name_value = name.getText().toString();
           String phone_value = phone.getText().toString();
           String  email_value = email.getText().toString();
           String site_value = site.getText().toString();
           
           System.out.println("Thisis the data " + name_value +phone_value + email_value +site_value);
 //          BluetoothAdapter bluetoothDefaultAdapter = BluetoothAdapter.getDefaultAdapter();
  //         if ((bluetoothDefaultAdapter != null) && (bluetoothDefaultAdapter.isEnabled())){
          String mac = BluetoothAdapter.getDefaultAdapter().getAddress();
           System.out.println("This is the mac address:" + mac);
 //          }
           
           
           if (!isNetworkAvailable())
           {
           	   Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_SHORT).show();
          	finish();
           }
           else {
         
        //	   pb.setVisibility(View.VISIBLE);
        	   new MyAsyncTask().execute(mac,name_value,phone_value,email_value,site_value, img);		
               finish();
           }
        	
   
          }
        });
        
     
        
        btPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(v);
            }
        });
        
        
       
  
    
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
    
    
    
    private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
    	 
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
    	postData(params[0],params[1],params[2],params[3],params[4],params[5]);
		for (int i = 1; i<100;i++)
		{
			
			
			publishProgress(i);
		
			
		}
			
			
			return null;
		}
 
		protected void onPostExecute(Double result){
//			pb.setVisibility(View.GONE);
			
			
			Toast.makeText(getApplicationContext(), "card details saved sucessfully", Toast.LENGTH_LONG).show();
		}
		protected void onProgressUpdate(Integer... progress){
			
			super.onProgressUpdate(progress);
			//pb.setVisibility(View.VISIBLE);
		//	pb.setProgress(progress[0]);
		}
 
		public void postData(String mac ,String name_value,String phone_value,String email_value,String site_value,String  pickPath) {
			// Create a new HttpClient and Post Header
			
			String ba1 = null;;
			if (pickPath!=null)
			{
			Bitmap bitmapOrg = BitmapFactory.decodeFile(pickPath);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            //Resize the image
            double width = bitmapOrg.getWidth();
            double height = bitmapOrg.getHeight();
            double ratio = 400/width;
            int newheight = (int)(ratio*height);
            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 400, newheight, true);
            System.out.println("———-width" + width);
            System.out.println("———-height" + height);
            bitmapOrg.compress(Bitmap.CompressFormat.PNG, 95, bao);
            byte[] ba = bao.toByteArray();
            ba1 = Base64.encodeToString(ba, 0);
		
			}
			
             
           
             
			HttpClient httpclient = new DefaultHttpClient();
	//		HttpPost httppost = new HttpPost("http://192.168.50.5/cgi-bin/register.cgi");
			HttpPost httppost = new HttpPost("http://dfoa.ssh22.net/cgi-bin/register.cgi");
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    	nameValuePairs.add(new BasicNameValuePair("mac",mac));
	        nameValuePairs.add(new BasicNameValuePair("name",name_value));
	        nameValuePairs.add(new BasicNameValuePair("phone",phone_value ));
	        nameValuePairs.add(new BasicNameValuePair("email",email_value ));
	        nameValuePairs.add(new BasicNameValuePair("site",site_value ));
	        nameValuePairs.add(new BasicNameValuePair("pic",ba1));
			
			
 
			try {
				// Add your data
	//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//	nameValuePairs.add(new BasicNameValuePair("myHttpData", ));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				
				
				
				
				
                
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
 
	}
    
   public void pickImage(View v)
   {
    
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
    
   }
    
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
        
       if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
           Uri selectedImage = data.getData();
           String[] filePathColumn = { MediaStore.Images.Media.DATA };

           Cursor cursor = getContentResolver().query(selectedImage,
                   filePathColumn, null, null, null);
           cursor.moveToFirst();

           int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
           String picturePath = cursor.getString(columnIndex);
           cursor.close();
            
           ImageView imageView = (ImageView) findViewById(R.id.imgViewPhoto);
           imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
          img = picturePath;
       }
    
    
   } 
  
}
