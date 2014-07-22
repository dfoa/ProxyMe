package com.example.android.BluetoothChat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.widget.ImageView;

public class ItemDetails {
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	
	
	public Bitmap getImg() {
		
		return bm;
	}
	public void setImg(String img) {
//decode from base_64 to real PNG format

		
		byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
	//	im.setImageBitmap(decodedByte);
		bm = decodedByte;
	    	
		
	}
	
	//private ImageView im = new ImageView(null);
	private Bitmap bm; 
	private String name ;
	private String itemDescription;
	private String price;
	private String site;

	
}
