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

public class ChAdapter extends BaseAdapter {
	private Context context;
    private ArrayList<chechstu> text;
	private LayoutInflater mInflater;
	private String apikey;
	private String apisecret;
	private Handler handler;
	public ChAdapter(Context context, ArrayList<chechstu> text,String apikey,String apisecret,Handler handler) {
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
			view=mInflater.inflate(R.layout.checkitem, null);}
			TextView tv=(TextView)view.findViewById(R.id.tvc);
			tv.setText(text.get(index).getName()+"\n"+"œ‡À∆∂»"+text.get(index).getScore());
			
			final Button btn1=(Button)view.findViewById(R.id.its);
			btn1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					new Thread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String url="http://182.92.237.212:16667/v1/attendance/checkin";
							try{
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("api_key", apikey));
								params.add(new BasicNameValuePair("api_secret", apisecret));
								params.add(new BasicNameValuePair("group_id", text.get(index).getGroupid()));
								params.add(new BasicNameValuePair("member_id", text.get(index).getMemberid()));
								HttpPost re = new HttpPost(url);
								re.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
								HttpResponse haha = new DefaultHttpClient().execute(re);
								String result=EntityUtils.toString(haha.getEntity(),"UTF_8").trim();
								Log.e("«©µΩ",result);
								JSONObject jo=new JSONObject(result);
								if(!"".equals(jo.optString("msg"))){
									handler.sendEmptyMessage(2);
								}else{
									handler.sendEmptyMessage(1);
								}
								Intent brow=new Intent();
								brow.putExtra("apikey", apikey);
								brow.putExtra("apisecret", apisecret);
								brow.putExtra("groupid", text.get(index).getGroupid());
								brow.setClass(context, qiandao.class);
								context.startActivity(brow);
								( (Activity) context).finish();
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						
					}).start();
				}
				
			});		
		return view;
	}

}
