package com.ad.Intromi;

import java.io.FileNotFoundException;
import java.net.URI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;
import android.net.Uri;


import com.ad.Intromi.R.id;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;

public class DetailActivity extends Activity {
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private static Boolean  D = true;
    private static String TAG = "IntroMi/detailedActivity";
	private ProgressBar pbar;
	private TextView tvMobilePhone , tvemail,tvSite,tvMission,tvHeadline,tvName;
	
	String test;
	private ImageView imgView;
	private ImageView imagePhone;
	private ImageView imageEmail ;
	private ImageView btSaveToFavourites;
	private ImageView imgLeftBrackets ,	imgRightBrackets;
	private  Context c;
    private  String name;
    private String email;
    private String mission;
    private String head_line;
    private String photo;

	
	
	Bitmap bm;

	
	@Override
	protected  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_card);
	    
		pbar = (ProgressBar) findViewById(R.id.pbar);
		tvMobilePhone = (TextView) findViewById(R.id.tvPhone);
		tvemail = (TextView) findViewById(R.id.tvEmail);
		tvHeadline = (TextView) findViewById(R.id.tvHeadLine);
		tvMission = (TextView) findViewById(R.id.tvMission);
		tvName = (TextView) findViewById(R.id.tvName);
		imgLeftBrackets  = (ImageView)findViewById(R.id.leftBrackets);
		imgRightBrackets = (ImageView)findViewById(R.id.rightBrackets);


	    Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/Lato-Black.ttf");
	    Typeface tf1 = Typeface.createFromAsset(getAssets(),
                "fonts/Lato-Regular.ttf");

        tvName.setTypeface(tf);
       
        tvMission.setTypeface(tf);
        tvHeadline.setTypeface(tf1);
        tvMobilePhone.setTypeface(tf1);
        tvemail.setTypeface(tf1);
   //     tvLeftBrackets.setTypeface(tf);
   //     tvRightBrackets.setTypeface(tf);
        
        
        
		
		
		
	//	test  =  (TextView) findViewById(R.id.Tex);
		imgView = (ImageView) findViewById(R.id.photo);
		imagePhone = (ImageView) findViewById(R.id.imagePhone);
		imageEmail= (ImageView) findViewById(R.id.imageEmail);
		btSaveToFavourites = (ImageView)findViewById(R.id.btSaveToFavourites);
		Bundle b = getIntent().getExtras();

		 email = b.getString("title");
		final String mobilePhone = b.getString("desc");
		photo = b.getString("photo");
		head_line = b.getString("head_line");
	    mission = b.getString("mission");
		name = b.getString("name");
		tvName.setText(name);
		tvMobilePhone.setText(mobilePhone);
	    tvemail.setText(email);
	    if (head_line!=null)
	    tvHeadline.setText(head_line);
	    tvMission.setText(mission);
	    imgView.setImageBitmap(StringToBm(photo));
	    pbar.setVisibility(View.INVISIBLE);
	    this.c = getApplicationContext();

        
		




//		String url = b.getString("url");
//		loadImageFromURL(url);
	    btSaveToFavourites.setOnClickListener(new OnClickListener()
	     {
	       public void onClick(View v)
	       {
	     	  if (D) Log.v(TAG, "Image button  btSaveToFavorits  was pressed");
	     	
	          Cards cards = new Cards(c);
	             Profile p = new Profile();
	             p.setName(name);
	             p.setEmail(email);
	             p.setMission(mission);
	             p.setProfessionalHeadLine(head_line);
	             p.setPicture(photo);
	             p.setMobilePhoneNum(mobilePhone);
	             
	             cards.saveToFile(p);
	          

	   

	         //   finish();
	        }
	     	

	       
	     });
		 

	    
	    
		 imagePhone.setOnClickListener(new OnClickListener()
	     {
	       public void onClick(View v)
	       {
	     	  if (D) Log.v(TAG, "Imagephone pressed no need to send intent to dialer");
	     	 tvMobilePhone.getText().toString();
	     	 Intent intent = new Intent(Intent.ACTION_DIAL);
	     	intent.setData(Uri.parse("tel:"+mobilePhone));
	     	startActivity(intent); 

	        

	     	

	       }
	     });

		 
		 imageEmail.setOnClickListener(new OnClickListener()
	     {
	       public void onClick(View v)
	       {
	     	  if (D) Log.v(TAG, "ImageEmail  pressed no need to send intent to default mail application");
	     	 tvemail.getText().toString();
	     	Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+email));
//	     	emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email Subject");

	     	startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
         
	        

	     	

	       }
	     });
	}

	private void loadImageFromURL(String url) {
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.profile)
				.showImageForEmptyUrl(R.drawable.profile).cacheInMemory()
				.cacheOnDisc().build();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		imageLoader.displayImage(url, imgView, options,
				new ImageLoadingListener() {
					@Override
					public void onLoadingComplete() {
						pbar.setVisibility(View.INVISIBLE);

					}

					@Override
					public void onLoadingFailed() {

						pbar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onLoadingStarted() {
						pbar.setVisibility(View.VISIBLE);
					}
				});

	}
	
	 public Bitmap StringToBm(String img) {
			//decode from base_64 to real PNG format
	             Bitmap bm;
					
					byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
					Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
				//	im.setImageBitmap(decodedByte);
					bm = decodedByte;
					return bm;
	 }
	 
	 
	 

					
}
