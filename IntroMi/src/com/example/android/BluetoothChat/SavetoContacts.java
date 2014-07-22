package com.example.android.BluetoothChat;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.widget.Toast;


public class SavetoContacts {


//members
	
        String DisplayName="aaaaaaaaaaaaaaaaaaaaa"; 
	    String MobileNumber;
	    String HomeNumber;
	    String WorkNumber ;
	    String emailID ;
	    String company ;
	    String jobTitle ;
	    Context context;

	    ArrayList<ContentProviderOperation> ops =  new ArrayList<ContentProviderOperation>();


public  void  setDisplayName (String DisplayName) {
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
}

	        private  void  setMobileNumber (String MobileNumber) {
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
	        }
	        
	        private  void  setHomeNumber (String HomeNumber) {
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

	        }
	        
	        
 //------------------------------------------------------ Work Numbers
	        private  void  setWorkNumber (String WorkNumber) {
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
	        }
	        
	        private  void  setEmailID(String emailID) {
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
	        }
	        
	        private  void  setOrg(String company, String jobTitle ) {
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
	        }
	        
	        public void create(Context context) {
	                            // Asking the Contact provider to create a new contact                  
	                            try 
	                            {
	                   
	                                context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
	                            } 
	                            catch (Exception e) 
	                            {               
	                                e.printStackTrace();
	                                Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
	                            }
	                            
	        }
	    
	}
