package com.RobinCompany.imageenc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class AboutUs extends Activity {
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.about_us);
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        ImageView fb=(ImageView) findViewById(R.id.fa);
	        fb.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com"));
					startActivity(i); 
					
				}
			});
	        ImageView gm=(ImageView) findViewById(R.id.gm);
	        gm.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gmail.com"));
					startActivity(i); 
					
				}
			});
	        ImageView gp=(ImageView) findViewById(R.id.gp);
	        gp.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gplus.com"));
					startActivity(i); 
					
				}
			});
	        ImageView tw=(ImageView) findViewById(R.id.tw);
	        tw.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com"));
					startActivity(i); 
					
				}
			});
	        
	    }
	 @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if(item.getItemId()==android.R.id.home)
				finish();
			return super.onOptionsItemSelected(item);
		}
}
