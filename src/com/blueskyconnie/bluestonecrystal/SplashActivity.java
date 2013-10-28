package com.blueskyconnie.bluestonecrystal;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends Activity {

//	private static final int SPLASH_TIMEOUT = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_splash);

		// create an async task to load data
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		}, SPLASH_TIMEOUT);
		
		new LoadDataTask().execute();
	}
	

	private class LoadDataTask extends AsyncTask<String, Void, Void> {

		//private Context context;
		//private LoadDataTask(Context context) {
			//this.context = context;
		//}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			View splash_view = View.inflate(SplashActivity.this, R.layout.activity_splash, null);
			setContentView(splash_view);
		}

		@Override
		protected Void doInBackground(String... params) {
			 /* This is just a code that delays the thread execution 4 times, 
             * during 850 milliseconds and updates the current progress. This 
             * is where the code that is going to be executed on a background 
             * thread must be placed. 
             */  
            try  
            {  
                //Get the current thread's token  
                synchronized (this)  
                {  
                    //Initialize an integer (that will act as a counter) to zero  
                    int counter = 0;  
                    //While the counter is smaller than four  
                    while(counter <= 4)  
                    {  
                        //Wait 850 milliseconds  
                        this.wait(1000);  
                        //Increment the counter  
                        counter++;  
                    }  
                }  
            }  
            catch (InterruptedException e)  
            {  
                e.printStackTrace();  
            }  
            return null;  
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			// put product
			// 
			SplashActivity.this.startActivity(intent);
			finish();
		}
	}
	
}
