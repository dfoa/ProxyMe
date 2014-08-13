package com.example.android.BluetoothChat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;
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
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.MediaStore;
import android.provider.SyncStateContract.Constants;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView.BufferType;
import android.widget.Toast;


public class CardDetails extends Activity {
    // Debugging
    private static final String TAG = "IntroMi";
    private static final boolean D = true;
    private ProgressBar pb;
    private static int RESULT_LOAD_IMAGE = 1;
    private String img;
    private String filename = "card.bin";
    private EditText name;
    private EditText mobile;
    private EditText email;
    private EditText site;
    private String name_value ;
    private String phone_value;
    private String  email_value;
    private String site_value; 
    private ImageView imageView;
    private  Profile p;
    private  boolean editProfile = false;
//Linkedin
    
    private LinkedInOAuthService oAuthService;
    private LinkedInApiClientFactory factory;
    private LinkedInRequestToken liToken;
    private LinkedInApiClient client;


    
   
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE card details+++ ");
        // Set up the window layout
        
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.my_card);
       
        name = (EditText) findViewById(R.id.nameEditText);
        mobile = (EditText) findViewById(R.id.phoneEditText);
        email = (EditText)findViewById(R.id.emailEditText);
        site= (EditText)findViewById(R.id.siteEditText);
        imageView = (ImageView) findViewById(R.id.imgViewPhoto);
        Button btPickImage = (Button) findViewById(R.id.btImagepPick);
        Button saveRecords = (Button) findViewById(R.id.saveButton);
        ImageButton imageButton = (ImageButton)findViewById(R.id.imageView1);

        
        
        pb = (ProgressBar)findViewById(R.id.deataSentProgressBar);	
        pb.setVisibility(View.GONE);
        
      
       
  //check if card already exist , just load the details 
        System.out.println("checking if file exist");
        File file = getBaseContext().getFileStreamPath(filename);
        if(file.exists()) {
   	    	//read the card and show  details
   	     if(D) Log.e(TAG, "+++ Load card details");
   	      
   
      
   	     
   	     
   	  editProfile = true;   
   	  System.out.println("loading card");
   	     loadCard();
   	     
   	     
        }  
        
        imageButton.setOnClickListener(new OnClickListener()
        {
          public void onClick(View v)
          {
        	  System.out.println("Image button linkedin  was pressed");
     
              /* This code get the values from edittexts  */
              name_value  = name.getText().toString();
              System.out.println("this is the name" + name_value);
              phone_value = mobile.getText().toString();
              email_value = email.getText().toString();
              site_value  = site.getText().toString();
             
           
           if (!isNetworkAvailable())
           {
           	   Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_SHORT).show();
          	finish();
           }
           else {
        	   
        
         
  //      	   pb.setVisibility(View.VISIBLE);
        	   
        //	   GoToLD(); 
        	
        	   oAuthService = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(Constants.CONSUMER_KEY,Constants.CONSUMER_SECRET);
        	   factory = LinkedInApiClientFactory.newInstance(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
        	           liToken = oAuthService.getOAuthRequestToken(Constants.OAUTH_CALLBACK_URL);
        	           Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(liToken.getAuthorizationUrl()));
        	           startActivity(i);
   
              // finish();
           }
        	
   
          }
        });
        
   	 

        saveRecords.setOnClickListener(new OnClickListener()
        {
          public void onClick(View v)
          {
        	  System.out.println("button save card was pressed");
     
              /* This code get the values from edittexts  */
              name_value  = name.getText().toString();
              System.out.println("this is the name" + name_value);
              phone_value = mobile.getText().toString();
              email_value = email.getText().toString();
              site_value  = site.getText().toString();
             
              
           
           System.out.println("Thisis the data " + name_value +phone_value + email_value +site_value);
 //          BluetoothAdapter bluetoothDefaultAdapter = BluetoothAdapter.getDefaultAdapter();
  //         if ((bluetoothDefaultAdapter != null) &&+ (bluetoothDefaultAdapter.isEnabled())){
          String mac = BluetoothAdapter.getDefaultAdapter().getAddress();
           System.out.println("This is the mac address:" + mac);
 //          }
 //check if card already exist ,if yes  show the information , if no, create new card
           
           //check if card exist 
          

   	    
   	       

          
           
           
           if (!isNetworkAvailable())
           {
           	   Toast.makeText(getApplicationContext(), "No network connection", Toast.LENGTH_SHORT).show();
          	finish();
           }
           else {
        	   
        
         
        	   pb.setVisibility(View.VISIBLE);
        	   
        	
 //       	   new MyAsyncTask().execute(mac,name_value,phone_value,email_value,site_value,img);
               if (createCard(mac,name_value,phone_value,email_value,site_value,img))
            	   Toast.makeText(getApplicationContext(), "Card details saved sucessfully", Toast.LENGTH_LONG).show();
               else
            	   Toast.makeText(getApplicationContext(), "Problem saving card", Toast.LENGTH_LONG).show();  
        	   //upload  profile to server
               Profile profile = loadCard();
        	   new UploadProfileAsyncTask().execute(profile.macHw,profile.name,profile.MobilePhoneNum,profile.email,profile.site,profile.picture);
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
        
        
       
  
    
  //   }
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
    
    
 /*   
    private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
    	 
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
//create new profile card
		if 	(createCard(params[0],params[1],params[2],params[3],params[4],params[5])){
	
			System.out.println("card was created");
//			Toast.makeText(getApplicationContext(), "card details saved sucessfully", Toast.LENGTH_LONG).show();
			
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
 

 
	}
	*/
    
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
           Bitmap b = decodeSampledBitmapFromPath(picturePath,100,100);
           imageView.setImageBitmap(b);
           img = picturePath;

          
          
       }
       
 
    
   } 
 
 private  boolean createCard(String mac ,String name,String mobilePhone,String emailAddress,String siteAddress,String  location){
	 
//Create object and fill with data
	 Profile p = new Profile();  
     p.macHw = mac;
     p.name =  name;
     p.MobilePhoneNum = mobilePhone;
     p.email = emailAddress;
     p.site = siteAddress;
 //    p.picture=location;
     if (editProfile){
    	 Bitmap bm=((BitmapDrawable)imageView.getDrawable()).getBitmap() ; //change to image if profile in edit mode  
    	 p.picture =  getPhotoStringFromBitmap(bm);
     }
	 
  
     
     else {
    	 p.picture = getPhoto(location);
     }
     if (saveCard(p)){
  //now save  profile to web
    	 return true; 
     }
  
    else {
    	return false;
    
    }
 }
 
 public  boolean saveCard(Profile p){
	 


	    // save the object to file
	    FileOutputStream fos = null;
	    ObjectOutputStream out = null;
	    try {
	//	      Toast.makeText(this, "going to open file and write...." , Toast.LENGTH_SHORT).show();
	      fos = openFileOutput(filename,Context.MODE_PRIVATE);
//	      Toast.makeText(this, "opened file...." , Toast.LENGTH_SHORT).show();
   out = new ObjectOutputStream(fos);
	      out.writeObject(p);
        
	      out.close();
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      Toast.makeText(this, "Cant open file and write" , Toast.LENGTH_SHORT).show();
	    }
	 
	 
	 
	 return true;
 }
 
 public String getPhoto(String location){
	 
	 String ba1 = null;;
		if (location!=null)
		{
		Bitmap bitmapOrg = BitmapFactory.decodeFile(location);
     ByteArrayOutputStream bao = new ByteArrayOutputStream();
     //Resize the image
     double width = bitmapOrg.getWidth();
     double height = bitmapOrg.getHeight();
     double ratio = 400/width;
//     int newheight = (int)(ratio*height);
//     bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 400, newheight, true);
     System.out.println("———-width" + width);
     System.out.println("———-height" + height);
     bitmapOrg.compress(Bitmap.CompressFormat.PNG, 95, bao);
     byte[] ba = bao.toByteArray();
     ba1 = Base64.encodeToString(ba, 0);
       	return ba1;
		}
		else {
			return null;
		}
		
 
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
 
 private  Profile  loadCard()
 {
 	final String filename = "card.bin";
 	  p = new Profile(); 
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
 		    System.out.println("test");
 		  
 		     name.setText(p.name);
 		     mobile.setText(p.MobilePhoneNum);
 		     email.setText(p.email);
 		     site.setText(p.site);
 //		     decode p.picture from BASE_64 to bitmap 
             
 		    //Bitmap b = decodeSampledBitmapFromPath(p.picture,100,100);
 		    if ( p.picture != null) {
   		
 		      Bitmap b = setImg(p.picture);
 		     imageView.setImageBitmap(b);
 	//	    img = p.picture;
 	//	     WebView.setImageBitmap(setImg(p.picture));
 		    }
 		   
 		    
 		    System.out.println(p.email + p.name + p.MobilePhoneNum);
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
 public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
         int reqHeight) {

     final BitmapFactory.Options options = new BitmapFactory.Options();
     options.inJustDecodeBounds = true;
     BitmapFactory.decodeFile(path, options);

     options.inSampleSize = calculateInSampleSize(options, reqWidth,
             reqHeight);

     // Decode bitmap with inSampleSize set
     options.inJustDecodeBounds = false;
     Bitmap bmp = BitmapFactory.decodeFile(path, options);
     return bmp;
     }

 public static int calculateInSampleSize(BitmapFactory.Options options,
         int reqWidth, int reqHeight) {

     final int height = options.outHeight;
     final int width = options.outWidth;
     int inSampleSize = 1;

     if (height > reqHeight || width > reqWidth) {
         if (width > height) {
             inSampleSize = Math.round((float) height / (float) reqHeight);
         } else {
             inSampleSize = Math.round((float) width / (float) reqWidth);
          }
      }
      return inSampleSize;
     }
 
 
 private class UploadProfileAsyncTask extends AsyncTask<String, Integer, Double>{
 	 
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub

			postData(params[0],params[1],params[2],params[3],params[4],params[5]);
	
			
			
			return null;
		}

		protected void onPostExecute(Double result){
//			pb.setVisibility(View.GONE);
			
			
//			Toast.makeText(getApplicationContext(), "card details saved sucessfully", Toast.LENGTH_LONG).show();
		}
		protected void onProgressUpdate(Integer... progress){
			
			super.onProgressUpdate(progress);
			//pb.setVisibility(View.VISIBLE);
		//	pb.setProgress(progress[0]);
		}

		public void postData(String mac ,String name_value,String phone_value,String email_value,String site_value,String  pickPath) {
			// Create a new HttpClient and Post Header
			
          
        
            if(D) Log.i(TAG, "in posdate send to server");
			HttpClient httpclient = new DefaultHttpClient();
	//		HttpPost httppost = new HttpPost("http://192.168.50.5/cgi-bin/register.cgi");
			HttpPost httppost = new HttpPost("http://dfoa.ssh22.net/cgi-bin/register.cgi");
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    	nameValuePairs.add(new BasicNameValuePair("mac",mac));
	        nameValuePairs.add(new BasicNameValuePair("name",name_value));
	        nameValuePairs.add(new BasicNameValuePair("phone",phone_value ));
	        nameValuePairs.add(new BasicNameValuePair("email",email_value ));
	        nameValuePairs.add(new BasicNameValuePair("site",site_value ));
	        nameValuePairs.add(new BasicNameValuePair("pic",pickPath));
			
			

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

private void GoToLD() { 
 oAuthService = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(Constants.CONSUMER_KEY,Constants.CONSUMER_SECRET);
 factory = LinkedInApiClientFactory.newInstance(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
         liToken = oAuthService.getOAuthRequestToken(Constants.OAUTH_CALLBACK_URL);
         Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(liToken.getAuthorizationUrl()));
         startActivity(i);
}
 
 public class Constants {

     public static final String CONSUMER_KEY = "776b50zwhoried"; // your KEY
     public static final String CONSUMER_SECRET = "Cq8tW1l0B16KwaV2"; // your SECRET
     public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
         public static final String OAUTH_CALLBACK_HOST = "litestcalback";
         public static final String OAUTH_CALLBACK_URL =
                 OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
     }	  
 
 @Override
 protected void onNewIntent(Intent intent) {
         super.onNewIntent(intent);

         String verifier = intent.getData().getQueryParameter("oauth_verifier");

         LinkedInAccessToken accessToken = oAuthService.getOAuthAccessToken(liToken, verifier);
                 client = factory.createLinkedInApiClient(accessToken);
         // Now you can access login person profile details...

         Person profile = client.getProfileForCurrentUser(EnumSet.of(ProfileField.ID, ProfileField.FIRST_NAME,   ProfileField.LAST_NAME, ProfileField.HEADLINE,
                         ProfileField.SUMMARY,ProfileField.PICTURE_URL,ProfileField.PHONE_NUMBERS));
 System.out.println("going tp print ptofile");
         System.out.println("First Name -" +  profile.getFirstName());
         System.out.println("Last Name  - "+ profile.getLastName());
         System.out.println("Head Line  -" + profile.getHeadline());
         System.out.println("Industry -" + profile.getIndustry());
         System.out.println("Telephone -" + profile.getPhoneNumbers());
         System.out.println("summery  -" + profile.getSummary());
         System.out.println("summery  -" + profile.getPictureUrl());

       }

 
  }
 

 

