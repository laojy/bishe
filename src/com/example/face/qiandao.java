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
import java.util.Calendar;
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
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class qiandao extends Activity {
	private boolean paozhao=true;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(!exit){
				exit=true;
				Toast.makeText(qiandao.this, "再按一次退出", Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						exit=false;
					}
					
				}, 2000);
				return false;
			}else{
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private boolean exit=false;
	private SurfaceView surface = null ;
	private Button paizhao=null;
	private Button shangchuan=null;
	private Button fanhui=null;
	private SurfaceHolder holder = null ;
	private Camera cam = null ;
	private boolean previewRunning =  true ;
	private Handler handler=null;
	 private Message msg=null;
	 private String apikey=null;
		private String apisecrect=null;
		private String groupid=null;
		private int x=0,y=0;
		private Date dt=null;
		private SimpleDateFormat   formatter=null;
	 String fileName =null; 
	private  File file = null;
	 private boolean shang=true;
	 ProgressDialog myDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);// 窗口设置为半透明
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 窗口去掉标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		   WindowManager.LayoutParams.FLAG_FULLSCREEN);// 窗口设置为全屏
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		super.setContentView(R.layout.qiandao);
		Bundle bun=getIntent().getExtras();
		apikey=bun.getString("apikey");
		apisecrect=bun.getString("apisecret");
		groupid=bun.getString("groupid");
		myDialog = new ProgressDialog(
				qiandao.this);// 创建ProgressDialog对象
		myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的样式为圆形样式
		myDialog.setTitle("提示");// 设置进度条的标题信息
		myDialog.setMessage("上传中,请稍等...");// 设置进度条的提示信息
		myDialog.setCancelable(false);
		handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
	            case 0:
	            	paozhao=true;
	            	Toast.makeText(qiandao.this,"检测不到人脸，请重新拍照",Toast.LENGTH_SHORT).show();
	                break;
	            case 1:
	            	Toast.makeText(qiandao.this,"上传成功！",Toast.LENGTH_SHORT).show();
	            	break;
	            case 2:
	            	myDialog.cancel();
	            	break;
	            default:
	                //do something
	                break;
			  }
			}
		};
		this.surface = (SurfaceView) super.findViewById(R.id.surfacech) ;
		this.paizhao=(Button)super.findViewById(R.id.paizhao);
		this.holder = this.surface.getHolder() ;
		this.holder.addCallback(new MySurfaceViewCallback()) ;
		this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS) ;	
		this.paizhao.setOnClickListener(new OnClickListenerImpl()) ;
		
		
	}
	private class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(qiandao.this.cam != null) {
				if(paozhao){
					paozhao=false;
					qiandao.this.cam.autoFocus(new AutoFocusCallbackImpl()) ;}
				
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
			qiandao.this.cam = Camera.open(0) ;	// 取得第一个摄像头
			qiandao.this.cam.setDisplayOrientation(90);
			Camera.Parameters parameters  = qiandao.this.cam.getParameters(); 
			parameters.set("orientation", "portrait");
		      // parameters.setRotation(180);
		     //parameters.setPreviewFrameRate(5) ;	// 一秒5帧
		     parameters.setPictureFormat(PixelFormat.JPEG) ;	// 图片形式
		     parameters.set("jpen-quality", 80) ;
			//MyCameraDemo.this.cam.setParameters(param);
			qiandao.this.cam.setParameters(parameters) ;
			//MyCameraDemo.this.cam.setParameters(param);
			
			try {
				qiandao.this.cam.setPreviewDisplay(qiandao.this.holder) ;
			} catch (IOException e) {
			}
			qiandao.this.cam.startPreview() ;	// 进行预览
			qiandao.this.previewRunning = true ;	// 已经开始预览
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if(qiandao.this.cam != null) {
				if(qiandao.this.previewRunning) {
					qiandao.this.cam.stopPreview() ;	// 停止预览
					qiandao.this.previewRunning = false ;
				}
				qiandao.this.cam.release() ;
			}
		}
		
	}
	
	private class AutoFocusCallbackImpl implements AutoFocusCallback {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			
				qiandao.this.cam.takePicture(sc, pc, jpgcall) ;
			
		}
		
	}
	
	private PictureCallback jpgcall = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {	// 保存图片的操作
			Bitmap bmp1 = BitmapFactory.decodeByteArray(data, 0, data.length);
			 Matrix matrix = new Matrix();
			 matrix.postRotate(90);
			 Bitmap bmp=Bitmap.createBitmap(bmp1, 0, 0,
                     bmp1.getWidth(), bmp1.getHeight(), matrix, true);
			//SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			//String date = sDateFormat.format(new java.util.Date());
			fileName=Environment.getExternalStorageDirectory()
					.toString()
					+ File.separator
					+ "ljynphoto"
					+ File.separator
					+"1.jpg";
			try{
			file=new File(fileName) ;
			}catch(Exception e){
				e.printStackTrace();
				Log.e("fail","创建失败");
			}
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs() ;	// 创建文件夹
			}
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();         
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
			    if( baos.toByteArray().length / 1024>1024) {
			        baos.reset();//重置baos即清空baos  
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
			    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
			    int be = 1;//be=1表示不缩放  
			    if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
			        be = (int) (newOpts.outWidth / ww);  
			    } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
			        be = (int) (newOpts.outHeight / hh);  
			    }  
			    Log.e("be",Integer.toString(be));
			    if (be <= 0)  
			        be = 1;  
			    newOpts.inSampleSize = be;//设置缩放比例  
			    //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
			    isBm = new ByteArrayInputStream(baos.toByteArray());  
			    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file)) ;
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos) ; // 向缓冲区之中压缩图片
				bos.flush() ;
				bos.close() ;
				y=1;	myDialog.show();
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						final String URL = "http://182.92.237.212:16667/v1/detection/detect";
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
							Log.e("detect",result);
							JSONArray ja=null;
							try {
								ja=new JSONArray(result);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(ja.length()==0){
								handler.sendEmptyMessage(2);
								handler.sendEmptyMessage(0);
							}else{
								JSONObject jo=null;
								try {
									jo=(JSONObject)ja.get(0);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								String faceid=null;
								try {
									faceid=jo.getString("id");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Log.e("faceid",faceid);
								handler.sendEmptyMessage(2);
								handler.sendEmptyMessage(1);
								Intent brow=new Intent();
								brow.putExtra("apikey", apikey);
								brow.putExtra("apisecret", apisecrect);
								brow.putExtra("faceid", faceid);
								brow.putExtra("groupid", groupid);
								brow.setClass(qiandao.this, checkin.class);
								startActivity(brow);
								qiandao.this.finish();
							}
							
						}catch(Exception e){
							e.printStackTrace();
							//handler.sendEmptyMessage(0);
						}
					}
					
				}).start();
			
				
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(qiandao.this,
						"拍照失败！", Toast.LENGTH_SHORT)
						.show();
				paozhao=true;
			}
		
			qiandao.this.cam.stopPreview();
			qiandao.this.cam.startPreview();
			x=1;
			
			
		}
		
	} ;
	
	private ShutterCallback sc = new ShutterCallback(){
		@Override
		public void onShutter() {
			// 按下快门之后进行的操作
		}
	} ;
	private PictureCallback pc = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
		}
		
	} ;
}
