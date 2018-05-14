package com.RobinCompany.imageenc;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class HotoUse extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.howtouse);
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	 @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if(item.getItemId()==android.R.id.home)
				finish();
			return super.onOptionsItemSelected(item);
		}
}