package com.RobinCompany.imageenc;
import java.io.File;
import java.util.List;
import images.ImageInfo;
import images.Image_item;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
public class PicSelectActivity extends ListActivity {
	private static final int REQUEST_CODE = 10;
	List<Image_item> images = new ImageInfo().getimage();
	private static final int Menu_Delete_Id=1;
	private int current_imageId;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_gallery);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		refereshdata();
		if(images.size()==0)
			Toast.makeText(this, "فایلی برای نمایش وجود ندارد", Toast.LENGTH_LONG).show();
		registerForContextMenu(getListView());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Image_item item = images.get(position);
		Intent intent = new Intent(this, Decryption.class);
		intent.putExtra("imageName", item.imageName);
		intent.putExtra("state", item.typeimage);
		intent.putExtra("imaddress", item.fulladress);
		startActivityForResult(intent, REQUEST_CODE);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}
//	@Override
//	protected void onResume() {
//		images = new ImageInfo().getimage();
//		refereshdata();
//		super.onResume();
//	}
	private void refereshdata() {
		ImageAdapter adapter = new ImageAdapter(this, R.layout.item_image, images);
		setListAdapter(adapter);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo ACI=(AdapterContextMenuInfo) menuInfo;
		current_imageId=(int)ACI.id;
		menu.add(0,Menu_Delete_Id,0,"حذف");
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId()==Menu_Delete_Id)
		{
			Image_item it =images.get(current_imageId);
			if(images.remove(it))
			{
				Toast.makeText(PicSelectActivity.this, "عکس با موفقیت حذف شد", Toast.LENGTH_LONG).show();
				File f=new File(it.fulladress+it.imageName);
				f.delete();
				refereshdata();
			}
		}
		return super.onContextItemSelected(item);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			String Address = data.getStringExtra("address");
			String Name = data.getStringExtra("filename");
			Image_item it=new Image_item(Name, Address, true);
			for(int ii=0;ii<images.size();ii++)
			{
				Image_item temp= images.get(ii);
				if(temp.imageName.equals(it.imageName)&&temp.fulladress.equals(it.fulladress))
				{
					images.remove(ii);
				}
			}
			images.add(it);
			refereshdata();
			
		}
	}
}

