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
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class checkgrou extends Activity {
	private ListView listview=null;
	private ArrayList<group>text=null;
	private GrouAdapter adapter;
	private Handler handler=null;
	private boolean exit=false;
	private Button addg=null;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(!exit){
				exit=true;
				Toast.makeText(checkgrou.this, "再按一次退出", Toast.LENGTH_SHORT).show();
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
			setContentView(R.layout.groulist);
			Bundle bun=getIntent().getExtras();
			final String apikey=bun.getString("apikey");
			final String apisecrect=bun.getString("apisecret");
			listview=(ListView)super.findViewById(R.id.lvg);
			text=new ArrayList<group>();
			
			addg=(Button)super.findViewById(R.id.addg);
			addg.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent brow=new Intent();
					brow.putExtra("apikey", apikey);
					brow.putExtra("apisecret", apisecrect);
					brow.setClass(checkgrou.this, addgroup.class);
					startActivity(brow);
					checkgrou.this.finish();
				}
				
			});
			
			handler=new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					switch(msg.what){
		            case 0:
		                adapter.notifyDataSetChanged(); 
		                listview.setAdapter(adapter);
		                break;
		            case 1:
		            	Toast.makeText(checkgrou.this,"删除成功",Toast.LENGTH_SHORT).show();
		            	break;
		            case 2:
		            	Toast.makeText(checkgrou.this,"还有组员，不能删除",Toast.LENGTH_SHORT).show();
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
						String URLmem="http://182.92.237.212:16667/v1/group/get_list";
						HttpPost re = new HttpPost(URLmem);
						re.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
						HttpResponse haha = new DefaultHttpClient().execute(re);
						final String result2=EntityUtils.toString(haha.getEntity(),"UTF_8").trim();
						Log.e("group",result2);
					    JSONArray tem=new JSONArray(result2);
					    for(int index=0;index<tem.length();index++){
					    	JSONObject te=(JSONObject)tem.get(index);
					    	group gr=new group();
					    	gr.setApi_key(apikey);
					    	gr.setApi_secret(apisecrect);
					    	gr.setName(te.getString("name"));
					    	gr.setGroup_id(te.getString("id"));
					    	//gr.setStatus(te.getString("status"));
					    	//Log.e("groupid",te.getString("id"));
					    	text.add(gr);
					    }
							
						handler.sendEmptyMessage(0);				
					}catch(Exception e){
						e.printStackTrace();
						handler.sendEmptyMessage(0);
					}
				}
				
			}).start();
			
			adapter = new GrouAdapter(checkgrou.this,text,apikey,apisecrect,handler);
			//listview.setSelection(listview.getCount()-1);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.denglu, menu);
			return true;
		}

	}
