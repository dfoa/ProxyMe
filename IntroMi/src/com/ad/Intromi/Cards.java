package com.ad.Intromi;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.content.ContextWrapper;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;


public class Cards {

private static  String  TAG ="IntroMi/ItemListBasAdapter";	
private static String fileName = "cards.bin";
private static boolean D = true; 
public SavedCards mSaveCards ;

protected Context context;
  public Cards(Context c) {
	  mSaveCards  =  new SavedCards();
	  this.context = c;
			// TODO Auto-generated constructor stub   
  }

 
    
  public boolean chekIfFileExist()
  {
	   
  if (D) Log.v (TAG,"checking if file exist");
   File file = context.getFileStreamPath(fileName);
    if(file.exists()) {
	    	//read the card and show  details
	   if(D) Log.v(TAG, "+++ Load saved cards");
       return true;
    }
  return false;
 }
	  
  
 public    synchronized void  saveToFile(Profile profile)
 {
   	  
	 File file = context.getFileStreamPath(fileName);
	
	    if(file.exists()) {
		    	//read the card and show  details
			   if(D) Log.v(TAG, "+++ Load saved cards");	
	
	 mSaveCards = loadCards();
	    }
	 mSaveCards.addProfile(profile);
	 
	 FileOutputStream fos = null;
	 ObjectOutputStream out = null;
	   
	    try {
	      fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
          out = new ObjectOutputStream(fos);
	      out.writeObject(mSaveCards);     
	      out.close();
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      Toast.makeText(context, "Cant open file and write" , Toast.LENGTH_SHORT).show();
	    }
	 
 }
 
	    
  public  SavedCards loadCards()
  
 { 
	  if (chekIfFileExist()) {
	  
	  FileInputStream fis = null;
	 	 ObjectInputStream in = null; 

	 	 try {
	 		      fis = context.openFileInput(fileName);
	 		      in = new ObjectInputStream(fis);
	 		      mSaveCards = (SavedCards) in.readObject();
	 		      in.close();
	 		    } catch (Exception ex) {
	 		      ex.printStackTrace();
	 		      Toast.makeText(context, "cant open file to read" , Toast.LENGTH_SHORT).show();
	 		    }
	 		   
	 		    
	 //		    System.out.println(p.email + p.name + p.MobilePhoneNum + p.professionalHeadLine);
	 	 System.out.println(mSaveCards.profileArrayList.get(0).getName());
	            	  
	 
 }
		return(mSaveCards);	
	  
 }
	
}



 