package com.ad.ProxyMe;


import com.ad.ProxyMe.DiscoveryService.LocalBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ServiceManager {

	
	private static DiscoveryService mService = new DiscoveryService();
	private static boolean mBound = false;
	private static Context context;
	private static ServiceManager   _instance;


	private ServiceManager(Context c){

		ServiceManager.context = c;
	}

	public synchronized  static ServiceManager getInstance(Context c)
	{

		if (_instance == null)
		{
			_instance = new ServiceManager(c);
		}
		return _instance;

	}

	public  void  start (){	

		System.out.println("going to start the service");
		ServiceArgument  parameters  =  new ServiceArgument("Fiix","http://192.168.50.5", "80");
		Intent intent = new Intent(context, DiscoveryService.class);
		intent.putExtra("args",parameters);
		context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE); 

	}

	public  void stop() {

		// Unbind from the service
		if (mBound) {
			context.unbindService(mConnection);
			mBound = false;
		}
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private static ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className,
				IBinder service) {


			System.out.println("Service is connected");

			// We've bound to LocalService, cast the IBinder and get LocalService instance
			LocalBinder binder = (LocalBinder) service;
			mService = binder.getService();
			

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

      public static void setLog(boolean yesNo) {
	
         mService.setLog(yesNo);
      }
}
