package com.RobinCompany.imageenc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashsc extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
         
// METHOD 1     
         
         /****** Create Thread that will sleep for 1 seconds *************/        
        Thread background = new Thread() {
            public void run() {
                 
                try {
                    // Thread will sleep for 1 seconds
                    sleep(2000);
                     
                    // After 1 second redirect to another intent
                    Intent i=new Intent(getBaseContext(),Home.class);
                    startActivity(i);
                     
                    //Remove activity
                    finish();
                     
                } catch (Exception e) {
                 
                }
            }
        };
         
        // start thread
        background.start();
    }
     
    @Override
    protected void onDestroy() {
        super.onDestroy();
         
    }
}