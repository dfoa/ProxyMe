package com.ad.Intromi;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.ad.Intromi.R;

	public class Cosmo extends Activity  {
   

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.edit_card_alex);
	        Typeface tf = Typeface.createFromAsset(getAssets(),
	                "fonts/Lato-Black.ttf");
	        TextView tv01 = (TextView) findViewById(R.id.TextView01);
	        tv01.setTypeface(tf);
	        TextView tv04 = (TextView) findViewById(R.id.TextView04);
	        tv04.setTypeface(tf);
	        TextView tv1 = (TextView) findViewById(R.id.TextView1);
	        tv1.setTypeface(tf);
	        
	        TextView tv2 = (TextView) findViewById(R.id.textView2);
	        tv2.setTypeface(tf);
	        TextView tv3 = (TextView) findViewById(R.id.textView3);
	        tv3.setTypeface(tf);
	
	        
	}
	}
