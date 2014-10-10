package com.ad.Intromi;

import java.util.ArrayList;

import android.R.color;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.ad.Intromi.R;

	public class Cosmo extends Activity  {
   

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.edit_card_alex);
			 Typeface  tf = Typeface.createFromAsset(getApplicationContext().getAssets(),
			             "fonts/Lato-Black.ttf");
				Typeface   tf1 = Typeface.createFromAsset(getApplicationContext().getAssets(),
			             "fonts/Lato-Regular.ttf");
	        
	        
	        TextView tv01 = (TextView) findViewById(R.id.nameCardPreview);
	        tv01.setTypeface(tf);
	        TextView tv04 = (TextView) findViewById(R.id.missionPreviewCard);
	        tv04.setTypeface(tf1);
	
	        
	        TextView tv2 = (TextView) findViewById(R.id.tvHeadLine);
	        tv2.setTypeface(tf1);
	        TextView tv3 = (TextView) findViewById(R.id.tvName);
	        tv3.setTypeface(tf1);
	        
	
	      
	
	}
	}
