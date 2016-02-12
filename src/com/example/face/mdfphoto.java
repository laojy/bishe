package com.example.face;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class mdfphoto extends Activity {
	private SurfaceView surface = null ;
	private Button paizhao=null;
	private SurfaceHolder holder = null ;
	private Camera cam = null ;
	private boolean previewRunning =  true ;
	private Handler handler=null;
	 private String apikey=null;
		private String apisecrect=null;
		private String memberid=null;
		private String groupid=null;
		private String name=null;
		private String remark=null;
		private String faceid=null;
		private Date dt=null;
		private SimpleDateFormat   formatter=null;
	 String fileName =null; 
	private  File file = null;
	private  boolean paozhao1=true;
	 ProgressDialog myDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);// ��������Ϊ��͸��
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ����ȥ������
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		   WindowManager.LayoutParams.FLAG_FULLSCREEN);// ��������Ϊȫ��
		super.setContentView(R.layout.mdfphoto);
		Bundle bun=getIntent().getExtras();
		apikey=bun.getString("apikey");
		apisecrect=bun.getString("apisecret");
		memberid=bun.getString("memberid");
		groupid=bun.getString("groupid");
		name=bun.getString("name");
		remark=bun.getString("remark");
		myDialog = new ProgressDialog(
				mdfphoto.this);// ����ProgressDialog����
		myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���ý���������ʽΪԲ����ʽ
		myDialog.setTitle("��ʾ");// ���ý������ı�����Ϣ
		myDialog.setMessage("�ϴ���,���Ե�...");// ���ý���������ʾ��Ϣ
		myDialog.setCancelable(false);
		handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
	            case 0:
	            	paozhao1=true;
	            	Toast.makeText(mdfphoto.this,"����ʧ��",Toast.LENGTH_SHORT).show();
	                break;
	            case 1:
	            	Toast.makeText(mdfphoto.this,"���³ɹ�",Toast.LENGTH_SHORT).show();
	            	break;
	            case 2:
	            	myDialog.cancel();
	            	break;
	            case 3:
	            	paozhao1=true;
	            	Toast.makeText(mdfphoto.this,"��ⲻ������������������",Toast.LENGTH_SHORT).show();
	            	break;
	            default:
	                //do something
	                break;
			  }
			}
		};
		this.surface = (SurfaceView) super.findViewById(R.id.mdfsv) ;
		this.paizhao=(Button)super.findViewById(R.id.mdfpaizhao);
		this.holder = this.surface.getHolder() ;
		this.holder.addCallback(new MySurfaceViewCallback()) ;
		this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS) ;	
		this.paizhao.setOnClickListener(new OnClickListenerImpl()) ;
		
	}
	private class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(mdfphoto.this.cam != null) {
				if(paozhao1){
					paozhao1=false;
				mdfphoto.this.cam.autoFocus(new AutoFocusCallbackImpl());}
				
			}
		}
		
	}
	
	private class MySurfaceViewCallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mdfphoto.this.cam = Camera.open(0) ;	// ȡ�õ�һ������ͷ
			mdfphoto.this.cam.setDisplayOrientation(90);
			Camera.Parameters parameters  = mdfphoto.this.cam.getParameters(); 
			//parameters.setRotation(270);
		     //parameters.setPreviewFrameRate(5) ;	// һ��5֡
		     parameters.setPictureFormat(PixelFormat.JPEG) ;	// ͼƬ��ʽ
		     parameters.set("jpen-quality", 80) ;
			//MyCameraDemo.this.cam.setParameters(param);
		     mdfphoto.this.cam.setParameters(parameters) ;
			//MyCameraDemo.this.cam.setParameters(param);
			
			try {
				mdfphoto.this.cam.setPreviewDisplay(mdfphoto.this.holder) ;
			} catch (IOException e) {
			}
			mdfphoto.this.cam.startPreview() ;	// ����Ԥ��
			mdfphoto.this.previewRunning = true ;	// �Ѿ���ʼԤ��
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if(mdfphoto.this.cam != null) {
				if(mdfphoto.this.previewRunning) {
					mdfphoto.this.cam.stopPreview() ;	// ֹͣԤ��
					mdfphoto.this.previewRunning = false ;
				}
				mdfphoto.this.cam.release() ;
			}
		}
		
	}
	
	private class AutoFocusCallbackImpl implements AutoFocusCallback {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
		
				mdfphoto.this.cam.takePicture(sc, pc, jpgcall) ;
			
		}
		
	}
	
	private PictureCallback jpgcall = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {	// ����ͼƬ�Ĳ���
			Bitmap bmp1 = BitmapFactory.decodeByteArray(data, 0, data.length);
			 Matrix matrix = new Matrix();
			 matrix.postRotate(90);
			 Bitmap bmp=Bitmap.createBitmap(bmp1, 0, 0,
                    bmp1.getWidth(), bmp1.getHeight(), matrix, true);
			fileName=Environment.getExternalStorageDirectory()
					.toString()
					+ File.separator
					+ "ljynphoto"
					+ File.separator
					+name+remark+"1.jpg";
			try{
			file=new File(fileName) ;
			}catch(Exception e){
				e.printStackTrace();
				Log.e("fail","����ʧ��");
			}
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs() ;	// �����ļ���
			}
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();         
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
			    if( baos.toByteArray().length / 1024>1024) {
			        baos.reset();//����baos�����baos  
			        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			    }  
			    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
			    BitmapFactory.Options newOpts = new BitmapFactory.Options();     
			    newOpts.inJustDecodeBounds = true;  
			    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
			    newOpts.inJustDecodeBounds = false;  
			    int w = newOpts.outWidth;
			    int h = newOpts.outHeight;  
			    Log.e("w",Integer.toString(w));
			    Log.e("h",Integer.toString(h));
			    float hh = 400f; 
			    float ww = 400f;  
			    //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��  
			    int be = 1;//be=1��ʾ������  
			    if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����  
			        be = (int) (newOpts.outWidth / ww);  
			    } else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����  
			        be = (int) (newOpts.outHeight / hh);  
			    }  
			    Log.e("be",Integer.toString(be));
			    if (be <= 0)  
			        be = 1;  
			    newOpts.inSampleSize = be;//�������ű���  
			    //���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��  
			    isBm = new ByteArrayInputStream(baos.toByteArray());  
			    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file)) ;
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos) ; // �򻺳���֮��ѹ��ͼƬ
				bos.flush() ;
				bos.close() ;
		        myDialog.show();
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						final String detecturl="http://182.92.237.212:16667/v1/detection/detect";
						MultipartEntity detectentity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
						try{
							detectentity.addPart("api_key", new StringBody(apikey, Charset.forName("UTF-8")));
							detectentity.addPart("api_secret", new StringBody(apisecrect, Charset.forName("UTF-8")));
							detectentity.addPart("img_file", new FileBody(new File(fileName)));
							HttpPost detectrequest = new HttpPost(detecturl);
							detectrequest.setEntity(detectentity);
							HttpContext detectlocalContext = new BasicHttpContext();
							HttpResponse detectresponse=new DefaultHttpClient().execute(detectrequest,detectlocalContext);
							String detectresult=EntityUtils.toString(detectresponse.getEntity(),"UTF_8").trim() ;
							Log.e("******detectresult------",detectresult);
							JSONArray detectjo=new JSONArray(detectresult);
							JSONObject jo=null;
							try {
								jo=(JSONObject)detectjo.get(0);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								faceid=jo.getString("id");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							final String seturl="http://182.92.237.212:16667/v1/member/set_info";
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("api_key", apikey));
							params.add(new BasicNameValuePair("api_secret", apisecrect));
							params.add(new BasicNameValuePair("face_id", faceid));
							params.add(new BasicNameValuePair("name", name));
							params.add(new BasicNameValuePair("remark", remark));
							params.add(new BasicNameValuePair("member_id", memberid));
							HttpPost re = new HttpPost(seturl);
							try {
								re.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							HttpResponse haha = null;
							try {
								haha = new DefaultHttpClient().execute(re);
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}catch(Exception e){
							e.printStackTrace();
						}
						
						if(faceid!=null){
							final String URL = "http://182.92.237.212:16667/v1/member/set_photo";
							try{
								MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
								try {
									entity.addPart("api_key", new StringBody(apikey, Charset.forName("UTF-8")));
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								try {
									entity.addPart("api_secret", new StringBody(apisecrect, Charset.forName("UTF-8")));
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								entity.addPart("member_id", new StringBody(memberid, Charset.forName("UTF-8")));
								entity.addPart("img_file", new FileBody(new File(fileName)));
								HttpPost request = new HttpPost(URL);
								request.setEntity(entity);
								HttpContext localContext = new BasicHttpContext();
								HttpResponse response=null;
								try {
									response = new DefaultHttpClient().execute(request,localContext);
								} catch (ClientProtocolException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								String result=null;
								try {
								  result=EntityUtils.toString(response.getEntity(),"UTF_8").trim() ;
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Log.e("setphoto",result);
								JSONObject josp=new JSONObject(result);
								if(!"".equals(josp.optString("msg"))){
									handler.sendEmptyMessage(2);
									handler.sendEmptyMessage(0);
								}else{
									handler.sendEmptyMessage(2);
									handler.sendEmptyMessage(1);
									Intent brow=new Intent();
									brow.putExtra("apikey", apikey);
									brow.putExtra("apisecret", apisecrect);
									brow.putExtra("groupid", groupid);
									brow.setClass(mdfphoto.this, checkstu.class);
									startActivity(brow);
									mdfphoto.this.finish();
								}
								
							}catch(Exception e){
								e.printStackTrace();
								handler.sendEmptyMessage(2);
								handler.sendEmptyMessage(0);
							}
						}else{
							handler.sendEmptyMessage(2);
							handler.sendEmptyMessage(3);
						}
					}
					
				}).start();
			
				
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(mdfphoto.this,
						"����ʧ�ܣ�", Toast.LENGTH_SHORT)
						.show();
				paozhao1=true;
			}
		
			mdfphoto.this.cam.stopPreview();
			mdfphoto.this.cam.startPreview();
			
		}
		
	} ;
	
	private ShutterCallback sc = new ShutterCallback(){
		@Override
		public void onShutter() {
			// ���¿���֮����еĲ���
		}
	} ;
	private PictureCallback pc = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
		}
		
	} ;
}

