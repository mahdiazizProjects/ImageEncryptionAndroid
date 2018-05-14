package images;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class ImageInfo {
	private List<Image_item> Images = new ArrayList<Image_item>();
	public List<Image_item> getimage() {
		return Images;
	}
	public ImageInfo() {
		
		String path = Environment.getExternalStorageDirectory().toString()+"/ImageEnc/";
		File f = new File(path);        
		File file[] = f.listFiles();
		if(file.length>0)
		for (int i=0; i < file.length; i++)
		{
			String temp=file[i].getName();
			boolean enc=false;
		if(temp.charAt(0)=='E'||temp.charAt(0)=='e')
			enc=true;
			addItem(new Image_item(temp,path,enc));
		}
	}
	private void addItem(Image_item item) {
		Images.add(item);
	}
}
