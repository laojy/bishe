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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Zhuce extends Activity {
    private EditText etus=null;
    private EditText etpw=null;
    private EditText etma=null;
    private EditText etni=null;
    private Button btnreg=null;
    private Button btnch=null;
    private Button again=null;
    private Handler handler=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhuce);
		etus=(EditText)super.findViewById(R.id.usrname1);
		etpw=(EditText)super.findViewById(R.id.password1);
		etma=(EditText)super.findViewById(R.id.mail);
		etni=(EditText)super.findViewById(R.id.nickname);
		btnreg=(Button)super.findViewById(R.id.reg);
		btnch=(Button)super.findViewById(R.id.reflash);
		again=(Button)super.findViewById(R.id.again);
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
	            case 0:
	                Toast.makeText(Zhuce.this,"注册失败！", Toast.LENGTH_SHORT).show();
	                break;
	            case 1:
	                Toast.makeText(Zhuce.this,"注册成功，请十五分钟内进行邮件验证！", Toast.LENGTH_SHORT).show();
	                break;
	            case 2:
	                Toast.makeText(Zhuce.this,"出错了！", Toast.LENGTH_SHORT).show();
	                break;
	            case 3:
	                Toast.makeText(Zhuce.this,"邮件发送失败！", Toast.LENGTH_SHORT).show();
	                break;
	            case 4:
	                Toast.makeText(Zhuce.this,"邮件发送成功，请注意查收！", Toast.LENGTH_SHORT).show();
	                break;
	            default:
	                //do something
	                break;
			  }
			}
		};
		 btnch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				etus.setText("");
				etpw.setText("");
				etma.setText("");
				etni.setText("");
			}
		  }
		);
		 btnreg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String usrname=etus.getText().toString();
				final String password=etpw.getText().toString();
				final String mail=etma.getText().toString();
				final String nickname=etni.getText().toString();
				final String URL="http://182.92.237.212:16667/v1/user/signin";
				if(usrname.equals("")){
					Toast.makeText(Zhuce.this, "请填写用户名！", Toast.LENGTH_SHORT).show();
				}else if(password.length()<8){
					Toast.makeText(Zhuce.this, "密码最少为八位！", Toast.LENGTH_SHORT).show();
				}else if(mail.equals("")){
					Toast.makeText(Zhuce.this, "请填写邮箱！", Toast.LENGTH_SHORT).show();
				}else if(nickname.equals("")){
					Toast.makeText(Zhuce.this, "请填写昵称！", Toast.LENGTH_SHORT).show();
				}else{
					new Thread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try{
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("username", usrname));
								params.add(new BasicNameValuePair("password", password));
								params.add(new BasicNameValuePair("mail", mail));
								params.add(new BasicNameValuePair("nickname", nickname));
								HttpPost re = new HttpPost(URL);
								re.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
								HttpResponse haha = new DefaultHttpClient().execute(re);
								String result2=EntityUtils.toString(haha.getEntity(),"UTF_8").trim();
								Log.e("error",result2);
								JSONObject zhuce=new JSONObject(result2);
								if(!"".equals(zhuce.optString("msg"))){
									handler.sendEmptyMessage(0);
								}else{
									handler.sendEmptyMessage(1);
									Intent intent = new Intent();
									intent.setClass(Zhuce.this, Denglu.class);
									startActivity(intent);
									Zhuce.this.finish();
								}
							}catch(Exception e){
								e.printStackTrace();
								handler.sendEmptyMessage(2);
							}
						}
						
					}).start();
				}
			}
			 
		 });
		 again.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String usrname=etus.getText().toString();
				final String mail=etma.getText().toString();
				final String password=etpw.getText().toString();
				final String URL="http://182.92.237.212:16667/v1/user/resend_auth_mail";
				if(usrname.equals("")){
					Toast.makeText(Zhuce.this, "请填写用户名！", Toast.LENGTH_SHORT).show();
				}else if(password.equals("")){
					Toast.makeText(Zhuce.this,"密码不能为空！", Toast.LENGTH_SHORT).show();
				}else if(password.length()<8){
					Toast.makeText(Zhuce.this,"密码最少为八位！", Toast.LENGTH_SHORT).show();
				}else{
					new Thread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try{
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("username", usrname));
								//params.add(new BasicNameValuePair("mail", mail));
								params.add(new BasicNameValuePair("password", password));
								HttpPost re = new HttpPost(URL);
								re.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
								Log.e("haha","right");
								HttpResponse haha = new DefaultHttpClient().execute(re);
								String result2=EntityUtils.toString(haha.getEntity(),"UTF_8").trim();
								Log.e("error",result2);
								JSONObject zhuce=new JSONObject(result2);
								if(!"".equals(zhuce.optString("msg"))){
									handler.sendEmptyMessage(3);
								}else{
									handler.sendEmptyMessage(4);
									Intent intent = new Intent();
									intent.setClass(Zhuce.this, Denglu.class);
									startActivity(intent);
									Zhuce.this.finish();
								}
							}catch(Exception e){
								e.printStackTrace();
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

