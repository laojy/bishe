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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addgroup extends Activity {
  private Button grouback=null;
  private Button addgroup=null;
  private EditText et=null;
  private EditText et1=null;
  private Handler handler=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addgrou);
		Bundle bun=getIntent().getExtras();
		final String apikey=bun.getString("apikey");
		final String apisecret=bun.getString("apisecret");
		grouback=(Button)super.findViewById(R.id.grouback);
		addgroup=(Button)super.findViewById(R.id.addgroup);
		et=(EditText)super.findViewById(R.id.groupname);
		et.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				et.setText("");
				return false;
			}
			
		});
		et1=(EditText)super.findViewById(R.id.groupremark);
		et1.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				et1.setText("");
				return false;
			}
			
		});
		handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
	            case 0:
	            	Toast.makeText(addgroup.this,"添加成功！", Toast.LENGTH_SHORT).show();
	                break;
	            case 1:
	            	Toast.makeText(addgroup.this,"添加失败！", Toast.LENGTH_SHORT).show();
	            	break;
	            default:
	                //do something
	                break;
			  }
			}
		};
		grouback.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent brow=new Intent();
				brow.putExtra("apikey", apikey);
				brow.putExtra("apisecret", apisecret);
				brow.setClass(addgroup.this, checkgrou.class);
				startActivity(brow);
				addgroup.this.finish();
			}
			
		});
		addgroup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String name=et.getText().toString();
				final String remark=et1.getText().toString();
				if(name.equals("name")||name.equals("")){
					Toast.makeText(addgroup.this, "请填写组名", Toast.LENGTH_SHORT).show();
				}else if(remark.equals("remark")||remark.equals("")){
					Toast.makeText(addgroup.this, "请填写备注", Toast.LENGTH_SHORT).show();
				}else{
					new Thread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try{
								String urlag="http://182.92.237.212:16667/v1/group/create";
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("api_key", apikey));
								params.add(new BasicNameValuePair("api_secret", apisecret));
								params.add(new BasicNameValuePair("name", name));
								params.add(new BasicNameValuePair("remark", remark));
								HttpPost re = new HttpPost(urlag);
								re.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
								HttpResponse haha = new DefaultHttpClient().execute(re);
								String result=EntityUtils.toString(haha.getEntity(),"UTF_8").trim();
								JSONObject jo=new JSONObject(result);
								if(!"".equals(jo.optString("msg"))){
									handler.sendEmptyMessage(1);
								}else{
									handler.sendEmptyMessage(0);
									Intent brow=new Intent();
									brow.putExtra("apikey", apikey);
									brow.putExtra("apisecret", apisecret);
									brow.setClass(addgroup.this, checkgrou.class);
									startActivity(brow);
									addgroup.this.finish();
								}
							}catch(Exception e){
								e.printStackTrace();
								handler.sendEmptyMessage(1);
							}
						}
						
					}).start();
				}
			}
			
		});
	}

}
