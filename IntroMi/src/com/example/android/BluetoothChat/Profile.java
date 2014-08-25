package com.example.android.BluetoothChat;


import java.io.Serializable;

import android.graphics.Bitmap;

public class Profile implements Serializable {


  public Profile() {
 
//    this.myThread = new Thread();
    this.macHw=null;
    this.firstName = null;
	this.lastName = null;
	this.name = null;
	this.MobilePhoneNum = null;;
	this.homePhoneNum = null; 
	this.picture = null;
	this.email = null;
	this.homeAddress = null;
	this.workAddress = null;
	this.site = null;
	this.position = null;
	this.description = null;
    this.mission = null;
	this.company = null;
	this.option1 =null;
	this.option2 = null;
    
    
  }

  
		     
	  

 // @Override
 // public String toString() {
 //   return "Person [firstName=" + name + ", lastName=" + email
 //       + "]";
 // }
  
  //members
	private static final long serialVersionUID = 1111L;
	public String macHw;
	public String firstName;
	public String lastName;
	public String name;
	public String MobilePhoneNum;
	public String homePhoneNum; 
	public String picture;
	public String professionalHeadLine;
	public String email;
	public String homeAddress;
	public String workAddress;
	public String site;
	public String position;
	public String description;
	public String mission;
	public String company;
	public String option1;
	public String option2;

	
	
//example for transient
//transient private Thread myThread;
  
  

} 