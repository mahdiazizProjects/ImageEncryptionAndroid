package com.RobinCompany.imageenc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Switch;
import android.widget.Toast;

public class Main extends Activity {
	Switch sw;
	static ImageView imview1;
	ImageView mix;
	Bitmap bitmap;
	private static int RESULT_LOAD_IMAGE=1;
	static String encryptedfilename;
	static String file_path;
	int width;
	int count=0;
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		sw=(Switch) findViewById(R.id.switch1);
		sw.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					// Do your stuff here
					break;
				case MotionEvent.ACTION_MOVE:

					break;
				case MotionEvent.ACTION_UP:
					if(sw.isChecked()==true)
					{
						ImageView mix=(ImageView) findViewById(R.id.mixitimv);
						mix.setImageDrawable(getResources().getDrawable(R.drawable.mixit));
					}
					else if(sw.isChecked()==false)
					{
						ImageView mix=(ImageView) findViewById(R.id.mixitimv);
						mix.setImageDrawable(getResources().getDrawable(R.drawable.back));
					}
					break;
				}
				return false;
			}
		});
		imview1=(ImageView) findViewById(R.id.imageView1);
		imview1.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				//				Intent i = new Intent(
				//						Intent.ACTION_PICK,
				//						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				//				startActivityForResult(i, RESULT_LOAD_IMAGE);
				int action = arg1.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					imview1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.clickc));
					break;
				}
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(intent, RESULT_LOAD_IMAGE);
				return false;
				//		         Intent intent = new Intent();
				//		         intent.setAction(Intent.ACTION_PICK);
				//		         Uri startDir = Uri.fromFile(new File("/sdcard"));
				//		         startActivityForResult(intent, RESULT_LOAD_IMAGE);
				//		         return false;
			}
		});
		mix=(ImageView)findViewById(R.id.mixitimv);
		mix.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View arg0){
				if(bitmap==null)
				{
					Toast t=Toast.makeText(Main.this, "عکس انتخاب کنید", Toast.LENGTH_LONG);
					t.show();
					return;
				}
				EditText et=(EditText) findViewById(R.id.editText1);
				final char[]cc=et.getText().toString().toCharArray();
				if(cc.length==0)
				{
					Toast.makeText(Main.this, "لطفا رمز عبور را وارد کنید", Toast.LENGTH_LONG).show();
					return;
				}
				if(cc.length<=2)
				{
					Toast.makeText(Main.this, "رمز عبور باید حداقل سه حرف باشد.", Toast.LENGTH_LONG).show();
					return;
				}
				progressBar = new ProgressDialog(arg0.getContext());
				progressBar.setCancelable(true);
				progressBar.setTitle("لطفا چند لحظه صبر کنید.");
				progressBar.setMessage("در حال انجام فرآیند رمزنگاری.....");
				progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressBar.setProgress(0);
				progressBar.setMax(100);
				progressBar.show();

				//reset progress bar status
				progressBarStatus = 0;
				Thread t=new Thread(new Runnable() {
					public void run() {
						boolean switch_checked= sw.isChecked();
						int host_width=bitmap.getWidth();
						int host_height=bitmap.getHeight();
						int[] pixels=new int[host_height*host_width];
						bitmap.getPixels(pixels, 0, host_width, 0, 0, host_width, host_height);
						////////////////////cleaning the memory
						//						int width_image=bitmap.getWidth();
						//						int height_image=bitmap.getHeight();
						bitmap.recycle();
						bitmap=null;
						////////////////////////////////////////
						encryption enc=new encryption();
						int block_size=pixels.length/3000+1;
						int number_block=pixels.length/block_size;
						double lamda1 = -1;
						double lamda2 = -1;
						double lamda3 = -1;
						double Gamma = -1;
						double Xzero=0;
						double Yzero = 0;
						double Zzero = 0;
						double M = -1;
						if (cc.length >= 3)
						{
							enc.restrict(-1, 2);
							Xzero = enc.key_maker( cc[0]);
							Yzero = enc.key_maker( cc[1]);
							Zzero = enc.key_maker( cc[2]);
							if (cc.length > 4)
							{
								enc.restrict(2.6, 0.5);
								lamda1 = enc.key_maker( cc[3]);
								if (cc.length > 5)
								{
									lamda2 = enc.key_maker(cc[4]);
									if (cc.length > 6)
									{
										lamda3 = enc.key_maker( cc[5]);
										if (cc.length > 7)
										{
											enc.restrict(2.9, 0.2);
											Gamma = enc.key_maker( cc[6]);
											if (cc.length >= 8)
											{
												enc.restrict(0.2, 0.1);
												M = enc.key_maker(cc[7]);
											}
											else
											{
												M = 0.25;
											}
										}
										else
										{
											Gamma = 3.05; M = 0.25;
										}
									}
									else
									{
										lamda3 = 2.87; Gamma = 3.05; M = 0.25;
									}
								}
								else
								{
									lamda2 = 3.01; lamda3 = 2.87; Gamma = 3.05; M = 0.25;
								}
							}
							else
							{
								lamda1 = 2.98; lamda2 = 3.01; lamda3 = 2.87; Gamma = 3.05; M = 0.25;
							}
						}
						// block
						//////////////
						enc=new encryption(lamda1, lamda2, lamda3, Xzero, Yzero, Zzero, Gamma, M);
						int[]result_pixels=new int[pixels.length];
						String filestate="";
						double iterator=100.0/block_size;
						double sum=0;
						while (progressBarStatus < 100) {
							enc.domains[0] = -2.0;
							for (int i = 1; i < 65536; i++)
							{
								enc.domains[i] = enc.domains[i - 1] + enc.domain;
							}
							int index = 0;
							///////////////////////////////////////////////////////////////Color///////////////
							double Xnegative1 = Xzero;
							double Ynegative1 = Yzero;
							double Znegative1 = Zzero;
							////////////////////////////////////////////////////////////////////////////////////
							double X = (lamda1 * Xnegative1) - (Math.pow(Xnegative1, 3)) - (Gamma * Math.pow(Math.abs(Ynegative1), M) * Xnegative1);
							double Y = (lamda2 * Ynegative1) - (Math.pow(Ynegative1, 3)) - (Gamma * Math.pow(Math.abs(Znegative1), M) * Ynegative1);
							double Z = (lamda3 * Znegative1) - (Math.pow(Znegative1, 3)) - (Gamma * Math.pow(Math.abs(Xnegative1), M) * Znegative1);
							double select = enc.turning(Xnegative1, Ynegative1, Znegative1);
							Xnegative1 = X; Ynegative1 = Y; Znegative1 = Z;
							for (int ii = 0; ii < enc.domains.length - 1; ii++)
								if (enc.domains[ii] <= select && enc.domains[ii + 1] > select)
								{
									index = ii;
									break;
								}
							Random rr = new Random(index);
							int[] source = enc.sq_rand_gen(number_block, rr);
							///////////////////////////////////////////////////////////////Color///////////////
							X = (lamda1 * Xnegative1) - (Math.pow(Xnegative1, 3)) - (Gamma * Math.pow(Math.abs(Ynegative1), M) * Xnegative1);
							Y = (lamda2 * Ynegative1) - (Math.pow(Ynegative1, 3)) - (Gamma * Math.pow(Math.abs(Znegative1), M) * Ynegative1);
							Z = (lamda3 * Znegative1) - (Math.pow(Znegative1, 3)) - (Gamma * Math.pow(Math.abs(Xnegative1), M) * Znegative1);
							select = enc.turning(Xnegative1, Ynegative1, Znegative1);
							Xnegative1 = X; Ynegative1 = Y; Znegative1 = Z;
							for (int ii = 0; ii < enc.domains.length - 1; ii++)
								if (enc.domains[ii] <= select && enc.domains[ii + 1] > select)
								{
									index = ii;
									break;
								}
							rr = new Random(index);
							int[] destination = enc.sq_rand_gen(number_block, rr);
							for (int kk = 0; kk < destination.length; kk++)
							{
								int dest_block_num=destination[kk];
								int source_block_num=source[kk];
								for(int ll=0;ll<block_size;ll++)
								{
									if(switch_checked==false)
									{
										int index_pixel_source=(source_block_num)*block_size+ll;
										int index_pixel_dest=(dest_block_num)*block_size+ll;
										result_pixels[index_pixel_dest]=pixels[index_pixel_source];
									}
									else
									{
										int index_pixel_source=(dest_block_num)*block_size+ll;
										int index_pixel_dest=(source_block_num)*block_size+ll;
										result_pixels[index_pixel_dest]=pixels[index_pixel_source];
									}
								}
								try {
									Thread.sleep(1);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								sum+=iterator;
								progressBarStatus=(int) Math.floor(sum);
								if((progressBarStatus%10)==0)
								{	// Update the progress bar
									progressBarHandler.post(new Runnable() {
										public void run() {
											progressBar.setProgress(progressBarStatus);
										}
									});
								}
							}

							// ok, file is downloaded,
							if (progressBarStatus >= 100) {

								// sleep 2 seconds, so that you can see the 100%
								try {
									Thread.sleep(2);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								// close the progress bar dialog
								progressBar.dismiss();
							}
						}
						if(switch_checked==false)
						{
							filestate="encrypted_";
						}
						else
						{
							filestate="decrypted_";
						}
						bitmap=Bitmap.createBitmap(host_width,host_height,Bitmap.Config.ARGB_8888);
						bitmap.setPixels(result_pixels, 0, host_width, 0, 0, host_width,host_height);
						result_pixels=null;
						pixels=null;
						Random rr=new Random();
						String[]temp=encryptedfilename.split("[.]");
						String filename=filestate+temp[0]+String.valueOf(rr.nextInt(100));
						if(storeImage(bitmap,filename))
						{ 
							pixels=null;
							result_pixels=null;
							new Thread()
							{
								public void run()
								{
									Main.this.runOnUiThread(new Runnable() {
										public void run()
										{
											//Do your UI operations like dialog opening or Toast here
											Toast t;
											if(sw.isChecked()==false)
											{
												t = Toast.makeText(Main.this, "عمل رمز نگاری انجام شد.", 
														Toast.LENGTH_LONG);
											}
											else
											{
												t = Toast.makeText(Main.this, "عمل رمزگشایی انجام شد.", 
														Toast.LENGTH_LONG);	
											}
											t.show();
											//							int sdk = android.os.Build.VERSION.SDK_INT;
											//							if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
											//								Drawable d = new BitmapDrawable(getResources(),bitmap);
											//								//imview1.setBackgroundDrawable(d);
											//							} else {
											//								Drawable d = new BitmapDrawable(getResources(),bitmap);
											////								imview1.setBackground(d);
											//							}
											imview1.setImageBitmap(bitmap);
											//											if(bitmap!=null)
											//											{
											//												bitmap.recycle();
											////												bitmap=null;
											//											}
										}
									});
								}
							}.start();
						}
					}
				});
				t.start();
				t.interrupt();
			}
		});
		if(bitmap!=null)
			bitmap.recycle();
		mix.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int action = arg1.getAction();
				if(sw.isChecked()==false)
				{
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						mix.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mixitc));
						break;
					case MotionEvent.ACTION_UP:
						mix.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mixit));
					}
				}
				else
				{
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						mix.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.backc));
						break;
					case MotionEvent.ACTION_UP:
						mix.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.back));
					}
				}
				return false;
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK)
		{
			// We need to recyle unused bitmaps
			try
			{
				if (bitmap != null) {
					bitmap.recycle();
				}
				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inJustDecodeBounds=true;
				InputStream stream = getContentResolver().openInputStream(
						data.getData());
				BitmapFactory.decodeStream(stream,null,options);
				//				BitmapFactory.decodeFile(file_path,options);
				int IMAGE_MAX_SIZE=1000;
				int scale = 1;
				if (options.outHeight > IMAGE_MAX_SIZE || options.outWidth > IMAGE_MAX_SIZE) {
					scale = (int)Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / 
							(double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
				}
				BitmapFactory.Options options2=new BitmapFactory.Options();
				options2.inSampleSize = scale; 
				stream = getContentResolver().openInputStream(
						data.getData());
				bitmap = BitmapFactory.decodeStream(stream,null,options2);
				stream.close();
				//			 Toast.makeText(this,"File Name & PATH are:"+filename, Toast.LENGTH_LONG).show();
				if(count==0)
					width=imview1.getWidth();
				count++;
				final double viewWidthToBitmapWidthRatio = (double)width / (double)bitmap.getWidth();
				if(bitmap.getWidth()>bitmap.getHeight())
					imview1.getLayoutParams().height = (int) (bitmap.getHeight() * viewWidthToBitmapWidthRatio);
				else
					imview1.getLayoutParams().width = (int) (bitmap.getHeight() * viewWidthToBitmapWidthRatio);
				//				int sdk = android.os.Build.VERSION.SDK_INT;
				//				if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				//					Drawable d = new BitmapDrawable(getResources(),bitmap);
				//					//imview1.setBackgroundDrawable(d);
				//				} else {
				//					Drawable d = new BitmapDrawable(getResources(),bitmap);
				//					imview1.setBackground(d);
				//				}
				imview1.setImageBitmap(bitmap);
				//////////////////////////////finding the name of filename for encryption process
				Uri uri = data.getData();
				encryptedfilename=getFileNameByUri(Main.this, uri);
				uri=null;
				///////////////////////////////////////////////////////
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch (OutOfMemoryError e) {
				Toast.makeText(Main.this, "اندازه عکس بیشتر از حجم حافظه موجود است.", Toast.LENGTH_LONG).show();
			}
			//			finally
			//			{
			//				AlertDialog.Builder builder = new AlertDialog.Builder(this);
			//
			//				builder.setTitle("اخطار");
			//				builder.setMessage("آیا میخواهید عکس کوچک شود؟");
			//
			//				builder.setPositiveButton("بلی", new DialogInterface.OnClickListener() {
			//
			//					public void onClick(DialogInterface dialog, int which) {
			//
			//						BitmapFactory.Options options = new BitmapFactory.Options();
			//						options.inSampleSize = 2;
			//						bitmap = BitmapFactory.decodeStream(stream,options);
			//						stream.close();
			//					}
			//				});
			//
			//				builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
			//
			//					@Override
			//					public void onClick(DialogInterface dialog, int which) {
			//						// I do not need any action here you might
			//						bitmap.recycle();
			//						bitmap=null;
			//						Toast.makeText(Main.this, "لطفا عکس دیگری انتخاب نمایید", Toast.LENGTH_LONG).show();
			//					}
			//				});
			//
			//				AlertDialog alert = builder.create();
			//				alert.show();
			//			}
		}
		else
		imview1.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.click));
		super.onActivityResult(requestCode, resultCode, data);
	}
	public static String getFileNameByUri(Context context, Uri uri)
	{
		String fileName="unknown";//default fileName
		Uri filePathUri = uri;
		if (uri.getScheme().toString().compareTo("content")==0)
		{      
			Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
			if (cursor.moveToFirst())
			{
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
				filePathUri = Uri.parse(cursor.getString(column_index));
				fileName = filePathUri.getLastPathSegment().toString();
				file_path=filePathUri.getPath();
			}
		}
		else if (uri.getScheme().compareTo("file")==0)
		{
			fileName = filePathUri.getLastPathSegment().toString();
		}
		else
		{
			fileName = fileName+"_"+filePathUri.getLastPathSegment();
		}
		return fileName;
	}
	private boolean storeImage(Bitmap imageData, String filename) {
		//get path to external storage (SD card)
		String dir="/ImageEnc/";
		String iconsStoragePath = Environment.getExternalStorageDirectory() + dir;
		File sdIconStorageDir = new File(iconsStoragePath);

		//create storage directories, if they don't exist
		if(!sdIconStorageDir.exists())
			sdIconStorageDir.mkdirs();

		try {
			//		if(!check_external_storage()){
			//			return false;
			//		}
			String filePath = sdIconStorageDir.toString() +"/" +filename+".png";
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
			//choose another format if PNG doesn't suit you
			imageData.compress(CompressFormat.PNG, 100, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return false;
		}

		return true;
	}

}