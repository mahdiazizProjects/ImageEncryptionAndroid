package com.RobinCompany.imageenc;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class Decryption extends Activity {
	static ImageView imview1;
	Bitmap bitmap;
	static String encryptedfilename;
	static String file_path;
	int width;
	int count=0;
	Switch sw;
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	static ImageView mixbut;
	private Handler progressBarHandler = new Handler();
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		imview1=(ImageView) findViewById(R.id.imageView1);
		Intent intent=getIntent();
		sw=(Switch) findViewById(R.id.switch1);
		boolean imstate=intent.getBooleanExtra("state", false);
		file_path=intent.getStringExtra("imaddress");
		encryptedfilename=intent.getStringExtra("imageName");//it is given for removing "encrypted"
		String fulladdress=file_path+encryptedfilename;
		if(encryptedfilename.contains("encrypted")||encryptedfilename.contains("decrypted"))
			encryptedfilename=encryptedfilename.substring(10);
		sw.setChecked(imstate);
		sw.setEnabled(false);
		bitmap=BitmapFactory.decodeFile(fulladdress);
		imview1.setImageBitmap(bitmap);
	    mixbut=(ImageView) findViewById(R.id.mixitimv);
		if(imstate==true)
		{
			mixbut.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.back));
		}
		mixbut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0){
				EditText et=(EditText) findViewById(R.id.editText1);
				final char[]cc=et.getText().toString().toCharArray();
				if(cc.length==0)
				{
					Toast.makeText(Decryption.this, "لطفا رمز عبور را وارد کنید", Toast.LENGTH_LONG).show();
					return;
				}
				if(cc.length<=2)
				{
					Toast.makeText(Decryption.this, "رمز عبور باید حداقل سه حرف باشد.", Toast.LENGTH_LONG).show();
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
						boolean switch_checked=sw.isChecked();
						int host_width=bitmap.getWidth();
						int host_height=bitmap.getHeight();
						int[] pixels=new int[host_height*host_width];
						bitmap.getPixels(pixels, 0, host_width, 0, 0, host_width, host_height);
						////////////////////cleaning the memory
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
						bitmap=Bitmap.createBitmap(host_width,host_height,Bitmap.Config.ARGB_8888);
						bitmap.setPixels(result_pixels, 0, host_width, 0, 0, host_width,host_height);
						result_pixels=null;
						pixels=null;
						if(!switch_checked)
							encryptedfilename="encrypted_"+encryptedfilename;
						if(storeImage(bitmap,file_path+encryptedfilename))
						{ 
							pixels=null;
							result_pixels=null;
							new Thread()
							{
								public void run()
								{
									Decryption.this.runOnUiThread(new Runnable() {
										public void run()
										{
											//Do your UI operations like dialog opening or Toast here
											Toast t;
											if(sw.isChecked()==false)
											{
												t = Toast.makeText(Decryption.this, "عمل رمز نگاری انجام شد.", 
														Toast.LENGTH_LONG);
											}
											else
											{
												t = Toast.makeText(Decryption.this, "عمل رمزگشایی انجام شد.", 
														Toast.LENGTH_LONG);	
											}
											t.show();
											imview1.setImageBitmap(bitmap);
											Intent intent = new Intent();
											intent.putExtra("address", file_path);
											intent.putExtra("filename", encryptedfilename);
//											intent.putExtra("action", "add");
											setResult(RESULT_OK, intent);
											finish();
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
		mixbut.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				int action = arg1.getAction();
				if(sw.isChecked()==false)
				{
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						mixbut.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mixitc));
						break;
					case MotionEvent.ACTION_UP:
						mixbut.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mixit));
					}
				}
				else
				{
					switch (action) {
					case MotionEvent.ACTION_DOWN:
						mixbut.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.backc));
						break;
					case MotionEvent.ACTION_UP:
						mixbut.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.back));
					}
				}
				return false;
			}
		});
	}

		private boolean storeImage(Bitmap imageData, String fullname) {

			try {
				FileOutputStream fileOutputStream = new FileOutputStream(fullname);

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
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if(item.getItemId()==android.R.id.home)
				finish();
			return super.onOptionsItemSelected(item);
		}

	}
