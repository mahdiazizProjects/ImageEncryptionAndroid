package images;

public class Image_item {
	public String imageName;
	public boolean typeimage;
	public String fulladress;
	public Image_item(String filename,String fullAdress, boolean typeimage) {
		this.imageName = filename;
		this.typeimage = typeimage;
		this.fulladress=fullAdress;
	}

	@Override
	public String toString() {
		return imageName;
	}
}
