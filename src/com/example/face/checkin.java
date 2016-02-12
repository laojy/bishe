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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class checkin extends Activity {
   private Handler handler=null;
   private ListView lvc=null;
   private ChAdapter adapter;
	private ArrayList<chechstu>text=null;
	private TextView tv=null;
	private Button bt=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checklist);
		Bundle bun=getIntent().getExtras();
		final String apikey=bun.getString("apikey");
		final String apisecrect=bun.getString("apisecret");
		final String faceid=bun.getString("faceid");
		final String groupid=bun.getString("groupid");
		Log.e("in_apikey",apikey);
		Log.e("in_apisecret",apisecrect);
		Log.e("in_faceid",faceid);
		Log.e("in_grouid",groupid);
		lvc=(ListView)super.findViewById(R.id.lvc);
		
		bt=(Button)super.findViewById(R.id.chck);
		bt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent brow=new Intent();
				brow.putExtra("apikey", apikey);
				brow.putExtra("apisecret", apisecrect);
				brow.putExtra("groupid", groupid);
				brow.setClass(checkin.this, qiandao.class);
				startActivity(brow);
				checkin.this.finish();
			}
			
		});
		text=new ArrayList<chechstu>();
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
	            case 0:
	                adapter.notifyDataSetChanged(); 
	                lvc.setAdapter(adapter);
	                if(text.isEmpty()){
	                	Toast.makeText(checkin.this, "没有相似的", Toast.LENGTH_LONG).show();
	                 }
	                break;
	            case 1:
	            	Toast.makeText(checkin.this, "签到成功！", Toast.LENGTH_LONG).show();
	            	break;
	            case 2:
	            	Toast.makeText(checkin.this, "签到失败！", Toast.LENGTH_LONG).show();
	            	break;
	            default:
	                //do something
	                break;
			  }
			}
		};
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("api_key", apikey));
					params.add(new BasicNameValuePair("api_secret", apisecrect));
					params.add(new BasicNameValuePair("face_id", faceid));
					params.add(new BasicNameValuePair("group_id", groupid));
					String URLmem="http://182.92.237.212:16667/v1/detection/recognize";
					HttpPost re = new HttpPost(URLmem);
					re.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					HttpResponse haha = new DefaultHttpClient().execute(re);
					final String result2=EntityUtils.toString(haha.getEntity(),"UTF_8").trim();
					Log.e("recognize",result2);
					JSONArray temp=new JSONArray(result2);
					for(int index=0;index<temp.length();index++){
						JSONObject tem=(JSONObject)temp.get(index);
					 // if(Float.parseFloat(tem.getString("score"))>0.8){
							chechstu ct=new chechstu();
							ct.setMemberid(tem.getString("id"));
							ct.setName(tem.getString("name"));
							ct.setScore(tem.getString("score"));
							ct.setGroupid(groupid);
							text.add(ct);
					//  }
						
					}
					handler.sendEmptyMessage(0);
				}catch(Exception e){
					e.printStackTrace();
					handler.sendEmptyMessage(0);
				}
			}
			
		}).start();
		adapter = new ChAdapter(checkin.this,text,apikey,apisecrect,handler);
		
	}

}
