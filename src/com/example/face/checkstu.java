package com.example.face;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class checkstu extends Activity  {
	private ListView listview=null;
	private ArrayList<stu>text=null;
	private DeleteAdapter adapter;
	private Handler handler=null;
	private Button btb=null;
	private Button adds=null;
	private Button btc=null;
	private EditText sv=null;
	private int index=0;
	private boolean exit=false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(!exit){
				exit=true;
				Toast.makeText(checkstu.this, "再按一次退出", Toast.LENGTH_SHORT).show();
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
			setContentView(R.layout.stulist);
			initActionbar(); 
			Bundle bun=getIntent().getExtras();
			final String apikey=bun.getString("apikey");
			final String apisecrect=bun.getString("apisecret");
			final String groupid=bun.getString("groupid");
			index=bun.getInt("index");
			Log.e("index",Integer.toString(index));
			listview=(ListView)super.findViewById(R.id.lvs);
			listview.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					String memberid = (String) arg0.getItemAtPosition(arg2);
					Intent brow=new Intent();
					brow.putExtra("apikey", apikey);
					brow.putExtra("apisecret", apisecrect);
					brow.putExtra("memberid", memberid);
					brow.putExtra("groupid", groupid);
					brow.putExtra("index", arg2);
					brow.setClass(checkstu.this, meminfo.class);
					sv.setText("");
					startActivity(brow);
					checkstu.this.finish();
				}
			});
			btb=(Button)super.findViewById(R.id.back1);
			adds=(Button)super.findViewById(R.id.adds);
			adds.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent brow=new Intent();
					brow.putExtra("apikey", apikey);
					brow.putExtra("apisecret", apisecrect);
					brow.putExtra("groupid", groupid);
					brow.setClass(checkstu.this, addinfo.class);
					startActivity(brow);
				}
				
			});
			btc=(Button)super.findViewById(R.id.check);
			btc.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent brow=new Intent();
					brow.putExtra("apikey", apikey);
					brow.putExtra("apisecret", apisecrect);
					brow.putExtra("groupid", groupid);
					brow.setClass(checkstu.this, qiandao.class);
					startActivity(brow);
					checkstu.this.finish();
				}
				
			});
			btb.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent brow=new Intent();
					brow.putExtra("apikey", apikey);
					brow.putExtra("apisecret", apisecrect);
					brow.setClass(checkstu.this, checkgrou.class);
					startActivity(brow);
					checkstu.this.finish();
				}
				
			});
			text=new ArrayList<stu>();
			handler=new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					switch(msg.what){
		            case 0:
		                adapter.notifyDataSetChanged(); 
		                listview.setAdapter(adapter);
		                if(index!=0){
		        			listview.setSelection(index);}
		                break;
		            case 1:
		            	Toast.makeText(checkstu.this,"删除成功!",Toast.LENGTH_SHORT).show();
		            	break;
		            case 2:
		            	Toast.makeText(checkstu.this,"删除失败!",Toast.LENGTH_SHORT).show();
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
						params.add(new BasicNameValuePair("group_id", groupid));
						String URLmem="http://182.92.237.212:16667/v1/group/get_info";
						HttpPost re = new HttpPost(URLmem);
						re.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
						HttpResponse haha = new DefaultHttpClient().execute(re);
						final String result2=EntityUtils.toString(haha.getEntity(),"UTF_8").trim();
						Log.e("re",result2);
						JSONObject temp=new JSONObject(result2);
						JSONArray tem=(JSONArray)temp.get("members");
						for(int index=0;index<tem.length();index++){
							JSONObject te=(JSONObject)tem.get(index);
							stu st=new stu();
							st.setApikey(apikey);
							st.setApisecret(apisecrect);
							st.setName(te.getString("name"));
							st.setGroupid(groupid);
							st.setMemberid(te.getString("id"));
							text.add(st);
						}
						Collections.sort(text, new SortByName());
						handler.sendEmptyMessage(0);
					}catch(Exception e){
						e.printStackTrace();
						handler.sendEmptyMessage(0);
					}
				}
				
			}).start();
			
			adapter = new DeleteAdapter(checkstu.this,text,handler,apikey,apisecrect);
			
			 
		}
		class SortByName implements Comparator{

			@Override
			public int compare(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				stu st1=(stu)arg0;
				stu st2=(stu)arg1;
				java.util.Comparator cmp = java.text.Collator.getInstance(java.util.Locale.CHINA);
				return cmp.compare(st1.getName(), st2.getName());
				
				
				
			}
			
		}

		 public void initActionbar() {  
		      
		        getActionBar().setDisplayShowHomeEnabled(false);  
		        getActionBar().setDisplayShowTitleEnabled(false);  
		        getActionBar().setDisplayShowCustomEnabled(true);  
		        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		        View mTitleView = mInflater.inflate(R.layout.custom_action_bar_layout,  
		                null);  
		        getActionBar().setCustomView(  
		                mTitleView,  
		                new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,  
		                        LayoutParams.WRAP_CONTENT));  
		        sv = (EditText) mTitleView.findViewById(R.id.search_view);
		        sv.addTextChangedListener(filterTextWatcher);
		        sv.clearFocus();
		    }
		 private TextWatcher filterTextWatcher = new TextWatcher() {  
			  
		        @Override  
		        public void afterTextChanged(Editable s) {  
		  
		        }  
		  
		        @Override  
		        public void beforeTextChanged(CharSequence s, int start, int count,  
		                int after) {  
		  
		        }  
		  
		        @Override  
		        public void onTextChanged(CharSequence s, int start, int before,  
		                int count) {
		        	
		            adapter.getFilter().filter(s); 
		        	
		        }  
		  
		    };  
		 
		 
		

	}

