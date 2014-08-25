package com.example.android.BluetoothChat;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;

public class DetailActivity extends Activity {
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	private ProgressBar pbar;
	private TextView tvMobilePhone , tvemail,tvSite,tvMission,tvHeadline,tvName;
	String test;
	private ImageView imgView;
	Bitmap bm;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_card_details);

//		pbar = (ProgressBar) findViewById(R.id.pbar);
		tvMobilePhone = (TextView) findViewById(R.id.tvPhone);
		tvemail = (TextView) findViewById(R.id.tvEmail);
		tvHeadline = (TextView) findViewById(R.id.tvHeadLine);
		tvMission = (TextView) findViewById(R.id.tvMission);
		tvName = (TextView) findViewById(R.id.tvName);
		
	//	test  =  (TextView) findViewById(R.id.Tex);
		imgView = (ImageView) findViewById(R.id.imgViewPhoto);
		
		Bundle b = getIntent().getExtras();

		String email = b.getString("title");
		String mobilePhone = b.getString("desc");
		String photo = b.getString("photo");
		String head_line = b.getString("head_line");
		String mission = b.getString(";mission");
		String name = b.getString("name");
		
		tvName.setText(name);
		tvMobilePhone.setText(email);
	    tvemail.setText(mobilePhone);
	    tvHeadline.setText(head_line);
	    tvMission.setText(mission);
	    imgView.setImageBitmap(StringToBm(photo));
		




//		String url = b.getString("url");
//		loadImageFromURL(url);

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
