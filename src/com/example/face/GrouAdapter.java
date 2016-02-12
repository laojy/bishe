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
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GrouAdapter extends BaseAdapter {
	private Context context;
    private ArrayList<group> text;
	private LayoutInflater mInflater;
	private String apikey;
	private String apisecret;
	private Handler handler;
	public GrouAdapter(Context context, ArrayList<group> text,String apikey,String apisecret,Handler handler) {
        this.context = context;
        this.text = text;
        mInflater=LayoutInflater.from(context);
        this.apikey=apikey;
        this.apisecret=apisecret;
        this.handler=handler;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return text.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return text.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final int index=arg0;
		View view=arg1;
		if(view==null){
			view=mInflater.inflate(R.layout.grouitem, null);}
			TextView tv=(TextView)view.findViewById(R.id.tvg);
			tv.setText(text.get(index).getName());
			final Button btn1=(Button)view.findViewById(R.id.grou);
			btn1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new Thread(new Runnable(){

						@Override
						public void run() {//进入每一个组
							// TODO Auto-generated method stub
							Intent brow=new Intent();
							brow.putExtra("apikey", apikey);
							brow.putExtra("apisecret", apisecret);
							brow.putExtra("groupid", text.get(index).getGroup_id());
							Log.e("gagroupid",text.get(index).getGroup_id());
							brow.setClass(context, checkstu.class);
							context.startActivity(brow);
							( (Activity) context).finish();
						}
						
					}).start();
				}
				
			});	
			final Button btn2=(Button)view.findViewById(R.id.delgrou);
			btn2.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try{
						new Thread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								try{
									List<NameValuePair> param = new ArrayList<NameValuePair>();
									param.add(new BasicNameValuePair("api_key", apikey));
									param.add(new BasicNameValuePair("api_secret", apisecret));
									param.add(new BasicNameValuePair("group_id", text.get(index).getGroup_id()));
									String URLmem="http://182.92.237.212:16667/v1/group/get_info";
									HttpPost reck = new HttpPost(URLmem);
									reck.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
									HttpResponse chr = new DefaultHttpClient().execute(reck);
									final String result2=EntityUtils.toString(chr.getEntity(),"UTF_8").trim();
									Log.e("deletecheck",result2);
									JSONObject temp=new JSONObject(result2);
									JSONArray tem=(JSONArray)temp.get("members");
									if(tem.length()==0){
										String urldg="http://182.92.237.212:16667/v1/group/delete";
										List<NameValuePair> params = new ArrayList<NameValuePair>();
										params.add(new BasicNameValuePair("api_key", apikey));
										params.add(new BasicNameValuePair("api_secret", apisecret));
										params.add(new BasicNameValuePair("group_id", text.get(index).getGroup_id()));
										HttpPost re = new HttpPost(urldg);
										re.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
										HttpResponse haha = new DefaultHttpClient().execute(re);
										String result=EntityUtils.toString(haha.getEntity(),"UTF_8").trim();
										Log.e("delg",result);
										JSONObject jo=new JSONObject(result);
										
										if(!"".equals(jo.optString("msg"))){
											//handler.sendEmptyMessage(3);
										}else{
											text.remove(index);
											handler.sendEmptyMessage(0);
											handler.sendEmptyMessage(1);
										}
									}else{
										handler.sendEmptyMessage(2);
									}
					
								}catch(Exception e){
									e.printStackTrace();
								}
							}
							
						}).start();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			});
		return view;
	}

}
