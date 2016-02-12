package com.example.face;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Denglu extends  Activity {
    private EditText usrname=null;
    private EditText password=null;
    private Button btnlog=null;
    private Button btnreg=null;
    private Handler handler=null;
    private boolean exit=false;
    private ProgressDialog myDialog;
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(!exit){
				exit=true;
				Toast.makeText(Denglu.this, "再按一次退出", Toast.LENGTH_SHORT).show();
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.denglu);
		//setTitle("");
		usrname=(EditText)super.findViewById(R.id.usrname);
		password=(EditText)super.findViewById(R.id.password);
		myDialog = new ProgressDialog(
				Denglu.this);// 创建ProgressDialog对象
		myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的样式为圆形样式
		myDialog.setTitle("提示");// 设置进度条的标题信息
		myDialog.setMessage("登录中,请稍候...");// 设置进度条的提示信息
		myDialog.setCancelable(false);
		btnlog=(Button)super.findViewById(R.id.btnlog);
		btnreg=(Button)super.findViewById(R.id.btnreg);
		btnreg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Denglu.this, Zhuce.class);
				startActivity(intent);
				//Denglu.this.finish();
			}
			
		});
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
	            case 0:
	            	Toast.makeText(Denglu.this,"登录失败！", Toast.LENGTH_SHORT).show();
	                break;
	            case 1:
	            	Toast.makeText(Denglu.this,"您还未通过邮件验证！", Toast.LENGTH_SHORT).show();
	            	break;
	            case 2:
	            	Toast.makeText(Denglu.this,"出错了！", Toast.LENGTH_SHORT).show();
	            	break;
	            case 3:
	            	Toast.makeText(Denglu.this,"登录成功！", Toast.LENGTH_SHORT).show();
	            	break;
	            case 4:
	            	myDialog.cancel();
	                break;
	            default:
	                //do something
	                break;
			  }
			}
		};
		btnlog.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String name=usrname.getText().toString();
				final String pw=password.getText().toString();
				if(name.equals("")||name.equals("username")){
					Toast.makeText(Denglu.this,"请填写用户名！", Toast.LENGTH_SHORT).show();
				}else if(pw.equals("")){
					Toast.makeText(Denglu.this,"密码不能为空！", Toast.LENGTH_SHORT).show();
				}else if(pw.length()<8){
					Toast.makeText(Denglu.this,"密码最少为八位！", Toast.LENGTH_SHORT).show();
				}else{
					myDialog.show();
					new Thread(new Runnable(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String URL = "http://182.92.237.212:16667/v1/user/get_info";
							try{
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("username", name));
								params.add(new BasicNameValuePair("password", pw));
								HttpPost re = new HttpPost(URL);
								re.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
								HttpResponse haha = new DefaultHttpClient().execute(re);
								String result=EntityUtils.toString(haha.getEntity(),"UTF_8").trim();
								Log.e("result",result);
								JSONObject jo=new JSONObject(result);
								//Log.e("test", "hello world");
								if(!"".equals(jo.optString("msg"))){
									handler.sendEmptyMessage(0);
									handler.sendEmptyMessage(4);
								}else{
									if(0==Integer.parseInt(jo.getString("auth"))){
										handler.sendEmptyMessage(1);
										handler.sendEmptyMessage(4);
									}else{
										handler.sendEmptyMessage(4);
										//Log.e("haha","success");
										String apikey=jo.getString("api_key");
										String apisecret=jo.getString("api_secret");
										Intent brow=new Intent();
										brow.putExtra("apikey", apikey);
										brow.putExtra("apisecret", apisecret);
										brow.setClass(Denglu.this, checkgrou.class);
										startActivity(brow);
										Denglu.this.finish();
									}
								}
							}catch(Exception e){
								    e.printStackTrace();
								    handler.sendEmptyMessage(4);
								    handler.sendEmptyMessage(2);
							}
						}
						
					}).start();
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.denglu, menu);
		return true;
	}

}