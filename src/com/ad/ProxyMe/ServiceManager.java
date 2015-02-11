package com.ad.ProxyMe;


import com.ad.ProxyMe.DiscoveryService.LocalBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceManager {
private DiscoveryService mService;
private boolean mBound = false;
private  Context context;

	public ServiceManager(Context c)
	{
	this.context = c;
	 System.out.println("In seviceManager");

	}
public  void  start (){	
        System.out.println("going to start the service");
        Intent intent = new Intent(this.context, DiscoveryService.class);
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);      
    }
 
    public void stop() {
    
        // Unbind from the service
        if (mBound) {
            context.unbindService(mConnection);
            mBound = false;
        }
    }



    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
        	
        	
        	System.out.println("Service is connected");
         	
        	// We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
//            mService.setLog(true);

 /*           
            new Thread(new Runnable() { 
              public void run(){        
              System.out.println("In thread");
              for(;;) {
          	int a = mService.getRandomNumber();
      		System.out.println("get response from service " + a);
      		try {
      			
					Thread.sleep(3000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              }
              }
          }).start();
*/
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        	System.out.println("Service is disconnected");
            mBound = false;
        }
    };


}
