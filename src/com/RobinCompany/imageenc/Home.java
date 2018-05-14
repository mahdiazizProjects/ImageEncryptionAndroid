package com.RobinCompany.imageenc;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
public class Home extends Activity {

	ImageView aboutus;
	ImageView homepage;
	ImageView howtouseb;
	ImageView gall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_home);
        aboutus=(ImageView) findViewById(R.id.ab);
        aboutus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent in=new Intent(Home.this, AboutUs.class);
			        startActivity(in);
				
			}
		});
        aboutus.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int action = arg1.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					aboutus.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.aboutc));
					break;
				case MotionEvent.ACTION_UP:
					aboutus.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.about));
			}
				return false;
			}
		});
        homepage=(ImageView) findViewById(R.id.st);
        homepage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 Intent in=new Intent(Home.this, Main.class);
			        startActivity(in);
				
			}
		});
        homepage.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int action = arg1.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					homepage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.startc));
					break;
				case MotionEvent.ACTION_UP:
					homepage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.start));
			}
				return false;
			}
		});
        howtouseb=(ImageView) findViewById(R.id.us);
        howtouseb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 Intent in=new Intent(Home.this, HotoUse.class);
			        startActivity(in);
				
			}
		});
        howtouseb.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int action = arg1.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					howtouseb.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.usec));
					break;
				case MotionEvent.ACTION_UP:
					howtouseb.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.use));
			}
				return false;
			}
		});
        gall=(ImageView) findViewById(R.id.gal);
        gall.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 Intent in=new Intent(Home.this, PicSelectActivity.class);
			        startActivity(in);
				
			}
		});
        gall.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int action = arg1.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					gall.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.galleryc));
					break;
				case MotionEvent.ACTION_UP:
					gall.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.gallery));
			}
				return false;
			}
		});
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("خروج از برنامه عکس مخفی");
            builder.setMessage("آیا میخواهید از برنامه عکس مخفی خارج شوید؟");

            builder.setPositiveButton("بلی", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                	finish();
                    System.exit(0);
                }

            });
            builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // I do not need any action here you might
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();

        }
        return super.onKeyDown(keyCode, event);
    }
}
