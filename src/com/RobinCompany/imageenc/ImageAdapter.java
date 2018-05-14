package com.RobinCompany.imageenc;
import java.util.List;
import images.Image_item;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class ImageAdapter extends ArrayAdapter<Image_item> {
	private Context context;
	private List<Image_item> objects;
	public ImageAdapter(Context context, int resource,
			List<Image_item> objects) {
		super(context, resource, objects);
		this.context=context;
		this.objects=objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Image_item Imit = objects.get(position);	
		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.item_image, null);
		ImageView image = (ImageView) view.findViewById(R.id.itemimage);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeFile(Imit.fulladress+Imit.imageName,options);
		int scale = 1;
		int IMAGE_MAX_SIZE=80;
		if (options.outHeight > IMAGE_MAX_SIZE || options.outWidth > IMAGE_MAX_SIZE) {
			scale = (int)Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / 
					(double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
		}
		BitmapFactory.Options options2=new BitmapFactory.Options();
		options2.inSampleSize=scale;
//		float pixels=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMatrices());
		image.setImageBitmap(BitmapFactory.decodeFile(Imit.fulladress+Imit.imageName,options2));
		TextView tv = (TextView) view.findViewById(R.id.gallitem);
		tv.setText(Imit.imageName);
		return view;
	}

}
