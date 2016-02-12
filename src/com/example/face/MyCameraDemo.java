package com.example.face;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.util.Log;
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

public class MyCameraDemo extends Activity {
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction() & MotionEvent.ACTION_MASK) {  
        case MotionEvent.ACTION_DOWN:  
            //Log.e("touch","down");
            break;  
        case MotionEvent.ACTION_UP:  
           //Log.e("touch","up");
            break;  
        case MotionEvent.ACTION_POINTER_UP:  
             
            break;  
        case MotionEvent.ACTION_POINTER_DOWN:  
        	olddist = spacing(event);
        	Log.e("pointer_down",Float.toString(olddist));
        	//Log.e("pointer_down","pointer_down");
            break;  
        case MotionEvent.ACTION_MOVE:
        	int point=event.getPointerCount();
        	if(point>=2){
        		newdist = spacing(event);
            	Log.e("pointer_move",Float.toString(newdist));
            	if(newdist>olddist+80){
            		if(zoom<maxzoom){
        				zoom+=1;
        				Log.e("move","add");
        				Camera.Parameters parameters  = MyCameraDemo.this.cam.getParameters();
        				parameters.setZoom(zoom);
        			
        				MyCameraDemo.this.cam.setParameters(parameters);
        			}
            	}
            	if(newdist<olddist-80){
            		if(zoom>0){
            			Log.e("move","sub");
        				zoom-=1;
        				Camera.Parameters parameters  = MyCameraDemo.this.cam.getParameters();
        				parameters.setZoom(zoom);
        				MyCameraDemo.this.cam.setParameters(parameters);
        			}
            	}
            	//Log.e("move","move");
        	}
        	
        	/**/
        	//Log.e("pointer_move","pointer_move");
        	break;
       } 
		return super.onTouchEvent(event);
	}
	private SurfaceView surface = null ;
	private Button but = null ;
	private SurfaceHolder holder = null ;
	private Camera cam = null ;
	private boolean previewRunning =  true ;
	private Handler handler=null;
	private String apikey=null;
	private String apisecrect=null;
	private String groupid=null;
	private String name=null;
	private String other=null;
	ProgressDialog myDialog;
	private int maxzoom,zoom=0;
	private float olddist,newdist;
	private Boolean paizhao=true;
	private float spacing(MotionEvent event) {  
	    float x = event.getX(0) - event.getX(1);  
	    float y = event.getY(0) - event.getY(1);  
	    return FloatMath.sqrt(x * x + y * y);  
	}  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);// 窗口设置为半透明
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 窗口去掉标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		   WindowManager.LayoutParams.FLAG_FULLSCREEN);// 窗口设置为全屏
		super.setContentView(R.layout.add);
		Bundle bun=getIntent().getExtras();
		apikey=bun.getString("apikey");
		apisecrect=bun.getString("apisecret");
		groupid=bun.getString("groupid");
		name=bun.getString("name");
		other=bun.getString("other");
		myDialog = new ProgressDialog(
				MyCameraDemo.this);// 创建ProgressDialog对象
		myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的样式为圆形样式
		myDialog.setTitle("提示");// 设置进度条的标题信息
		myDialog.setMessage("正在添加,请稍等...");// 设置进度条的提示信息
		myDialog.setCancelable(false);
		handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
	            case 0:
	            	Toast.makeText(MyCameraDemo.this,"添加成功！", Toast.LENGTH_SHORT).show();
	                break;
	            case 1:
	            	paizhao=true;
	            	Toast.makeText(MyCameraDemo.this,"添加失败！", Toast.LENGTH_SHORT).show();
	            	break;
	            case 2:
	            	paizhao=true;
	            	Toast.makeText(MyCameraDemo.this,"出错了！", Toast.LENGTH_SHORT).show();
	            	break;
	            case 3:
	            	myDialog.cancel();
					break;
	            default:
	                //do something
	                break;
			  }
			}
		};
		this.but = (Button) super.findViewById(R.id.but) ;
		this.surface = (SurfaceView) super.findViewById(R.id.surface) ;
		this.holder = this.surface.getHolder() ;
		this.holder.addCallback(new MySurfaceViewCallback()) ;
		this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS) ;	
		this.but.setOnClickListener(new OnClickListenerImpl()) ;
	}
	private class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(MyCameraDemo.this.cam != null) {
				if(paizhao){
					paizhao=false;
					MyCameraDemo.this.cam.autoFocus(new AutoFocusCallbackImpl()) ;}
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
			MyCameraDemo.this.cam = Camera.open(0) ;	// 取得第一个摄像头
			MyCameraDemo.this.cam.setDisplayOrientation(90);
			Camera.Parameters parameters  = MyCameraDemo.this.cam.getParameters(); 
			parameters.setRotation(90);
		     //parameters.setPreviewFrameRate(5) ;	// 一秒5帧
		     parameters.setPictureFormat(PixelFormat.JPEG) ;	// 图片形式
		     parameters.set("jpen-quality", 100) ;
		     maxzoom=parameters.getMaxZoom();
			//MyCameraDemo.this.cam.setParameters(param);
		     MyCameraDemo.this.cam.setParameters(parameters) ;
			try {
				MyCameraDemo.this.cam.setPreviewDisplay(MyCameraDemo.this.holder) ;
			} catch (IOException e) {
			}
			MyCameraDemo.this.cam.startPreview() ;	// 进行预览
			MyCameraDemo.this.previewRunning = true ;	// 已经开始预览
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if(MyCameraDemo.this.cam != null) {
				if(MyCameraDemo.this.previewRunning) {
					MyCameraDemo.this.cam.stopPreview() ;	// 停止预览
					MyCameraDemo.this.previewRunning = false ;
				}
				MyCameraDemo.this.cam.release() ;
			}
		}
		
	}
	
	private class AutoFocusCallbackImpl implements AutoFocusCallback {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			
				MyCameraDemo.this.cam.takePicture(sc, pc, jpgcall) ;
			
		}
		
	}
	
	private PictureCallback jpgcall = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {	// 保存图片的操作
			final String URL = "http://182.92.237.212:16667/v1/detection/detect";
			Bitmap bmp1 = BitmapFactory.decodeByteArray(data, 0, data.length);
			 Matrix matrix = new Matrix();
			 matrix.postRotate(90);
			 Bitmap bmp=Bitmap.createBitmap(bmp1, 0, 0,
                    bmp1.getWidth(), bmp1.getHeight(), matrix, true);
			final String fileName = Environment.getExternalStorageDirectory()
					.toString()
					+ File.separator
					+ "ljynphoto"
					+ File.separator
					+ name+other+ ".jpg";
			File file = new File(fileName) ;
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
				myDialog.show();
				new Thread(new Runnable(){

					@Override
					public void run() {
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
							String urlcre="http://182.92.237.212:16667/v1/member/create";
							/*List<NameValuePair> params = new ArrayList<NameValuePair>();
							params.add(new BasicNameValuePair("api_key", apikey));
							params.add(new BasicNameValuePair("api_secret", apisecrect));
							params.add(new BasicNameValuePair("face_id", faceid));
							params.add(new BasicNameValuePair("name", name));
							params.add(new BasicNameValuePair("remark", remark));
							HttpPost re = new HttpPost(urlcre);
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
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String result2=null;
							try {
								result2=EntityUtils.toString(haha.getEntity(),"UTF_8").trim() ;
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Log.e("create",result2);*/
							MultipartEntity entitycre = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
							entitycre.addPart("api_key", new StringBody(apikey, Charset.forName("UTF-8")));
							entitycre.addPart("api_secret", new StringBody(apisecrect, Charset.forName("UTF-8")));
							entitycre.addPart("img_file", new FileBody(new File(fileName)));
							entitycre.addPart("name", new StringBody(name, Charset.forName("UTF-8")));
							entitycre.addPart("remark", new StringBody(other, Charset.forName("UTF-8")));
							entitycre.addPart("face_id", new StringBody(faceid, Charset.forName("UTF-8")));
							HttpPost cremem=new HttpPost(urlcre);
							cremem.setEntity(entitycre);
							HttpResponse creresult=new DefaultHttpClient().execute(cremem,localContext);
							String result2=EntityUtils.toString(creresult.getEntity(),"UTF_8").trim() ;
							Log.e("cre_mem",result2);
							JSONObject jb=null;
							try {
								jb=new JSONObject(result2);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String memberid=null;
							try {
								memberid=jb.getString("id");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String urladd="http://182.92.237.212:16667/v1/group/add_mem";
							List<NameValuePair> param = new ArrayList<NameValuePair>();
							param.add(new BasicNameValuePair("api_key", apikey));
							param.add(new BasicNameValuePair("api_secret", apisecrect));
							param.add(new BasicNameValuePair("group_id", groupid));
							param.add(new BasicNameValuePair("member_id", memberid));
							param.add(new BasicNameValuePair("remark", other));
							HttpPost re1= new HttpPost(urladd);
							try {
								re1.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							HttpResponse haha1 = null;
							try {
								haha1 = new DefaultHttpClient().execute(re1);
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String result3=null;
							try {
								result3=EntityUtils.toString(haha1.getEntity(),"UTF_8").trim() ;
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Log.e("---add",result3);
							//JSONObject bj=null;
							/*try {
								bj=new JSONObject(result3);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(!"".equals(bj.optString("msg"))){
								handler.sendEmptyMessage(1);
							}else{*/
							    handler.sendEmptyMessage(3);
								handler.sendEmptyMessage(0);
								

									
										
										String urltrain="http://182.92.237.212:16667/v1/group/train";
										List<NameValuePair> pa = new ArrayList<NameValuePair>();
										pa.add(new BasicNameValuePair("api_key", apikey));
										pa.add(new BasicNameValuePair("api_secret", apisecrect));
										pa.add(new BasicNameValuePair("group_id", groupid));
										HttpPost train= new HttpPost(urltrain);
										try {
											train.setEntity(new UrlEncodedFormEntity(pa, HTTP.UTF_8));
										} catch (UnsupportedEncodingException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										try {
											new DefaultHttpClient().execute(train);
										} catch (ClientProtocolException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									
									
							
								Intent brow=new Intent();
								brow.putExtra("apikey", apikey);
								brow.putExtra("apisecret", apisecrect);
								brow.putExtra("groupid", groupid);
								brow.setClass(MyCameraDemo.this, checkstu.class);
								startActivity(brow);
								MyCameraDemo.this.finish();
						}catch(Exception e){
							handler.sendEmptyMessage(3);
							handler.sendEmptyMessage(1);
						}
						// TODO Auto-generated method stub
					
						
						
					}
					
				}).start();
			} catch (Exception e) {
				Toast.makeText(MyCameraDemo.this,
						"拍照失败！", Toast.LENGTH_SHORT)
						.show();
				paizhao=true;
			}
			MyCameraDemo.this.cam.stopPreview();
			MyCameraDemo.this.cam.startPreview() ;
			
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