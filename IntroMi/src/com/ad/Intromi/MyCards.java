package com.ad.Intromi;



import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.jar.Attributes.Name;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.ad.Intromi.R;



public class MyCards extends Activity {
    // Debugging
    private static final String TAG = "IntroMi";
    private static final boolean D = true;
    private ProgressBar pb;
    private TextView name;
    private TextView mobile; 
    private TextView email; 
    private ImageView photo;
  

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE card details+++ ");
        // Set up the window layout
        
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.my_cards_view);
          name = (TextView) findViewById(R.id.textViewNamePresent);
          mobile = (TextView) findViewById(R.id.textViewMobilePresent);
          email = (TextView)findViewById(R.id.textViewEmailPresent); 
          photo = (ImageView)findViewById(R.id.imageViewPhotoPresent);
        
       loadCard(); 
        
   //     final EditText site= (EditText)findViewById(R.id.siteEdit);
   //     Button btPickImage = (Button) findViewById(R.id.btImagepPick);
   //     Button saveRecords = (Button) findViewById(R.id.saveButton);
    }

    
    private  void  loadCard()
    {
    	final String filename = "card.bin";
    	 Profile p = new Profile(); 
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
    		    
    		     name.setText(p.getName());
    		     mobile.setText(p.getMobilePhoneNum());
    		     email.setText(p.getEmail()); 
    		     if ( p.getPicture() != null)
    		     photo.setImageBitmap(setImg(p.getPicture()));
    		    
    		   
    		  
    		    System.out.println(p.getEmail() + p.getName() + p.getMobilePhoneNum() + p.getPicture());
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
    		 
    	 
    
	
    @Override
    public void onBackPressed()
    {
    	 Intent  g = new Intent(this, SavedCardsList.class); 
         startActivity(g);
         super.onBackPressed();  // optional depending on your needs
    }




   
    
    

}
