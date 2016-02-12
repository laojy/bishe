package com.example.face;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class meminfo extends Activity {
  private ImageView iv=null;
  private TextView tvname=null;
  private TextView tvcompany=null;
  private TextView tvnumber=null;
  private TextView tvtitle=null;
  private TextView tvcreate=null;
  private TextView tvupdate=null;
  private ListView listview=null;
  private TextView tvqdjl=null;
  private ArrayList<String> qdjl=null;
  private qdjladapter adapter=null;
  private Button btnmdf=null;
  private Handler handler=null;
  private Bitmap bmp=null;
  private String name,remark,photourl,create,update,groupid;
  private String apikey,apisecret,memberid,startat,endat;
  private String[] cnu=null;
  private int index=0,year,month,day;;
  private JSONObject joresult=null;
  @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent brow=new Intent();
			brow.putExtra("apikey", apikey);
			brow.putExtra("apisecret", apisecret);
			brow.putExtra("groupid", groupid);
			brow.putExtra("index", index);
			brow.setClass(meminfo.this, checkstu.class);
			startActivity(brow);
			meminfo.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meminfo);
		Bundle bun=getIntent().getExtras();
		apikey=bun.getString("apikey");
		apisecret=bun.getString("apisecret");
		memberid=bun.getString("memberid");
		groupid=bun.getString("groupid");
		index=bun.getInt("index");
		iv=(ImageView)super.findViewById(R.id.meminfoiv);
		tvname=(TextView)super.findViewById(R.id.memtv1);
		tvcompany=(TextView)super.findViewById(R.id.memtv2);
		tvnumber=(TextView)super.findViewById(R.id.memtv5);
		tvtitle=(TextView)super.findViewById(R.id.memtv6);
		tvcreate=(TextView)super.findViewById(R.id.memtv3);
		tvupdate=(TextView)super.findViewById(R.id.memtv4);
		tvqdjl=(TextView)super.findViewById(R.id.memtv7);
		listview=(ListView)super.findViewById(R.id.qdjllv);
		qdjl=new ArrayList<String>();
		Calendar myCalendar = Calendar.getInstance(Locale.CHINA);
		year=myCalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month=myCalendar.get(Calendar.MONTH)+1;//获取Calendar对象中的月，0表示1月，1表示2月....
        day=myCalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
       startat=Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(1);
        endat=Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
		btnmdf=(Button)super.findViewById(R.id.btnmdf);
		btnmdf.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent brow=new Intent();
				brow.putExtra("apikey", apikey);
				brow.putExtra("apisecret", apisecret);
				brow.putExtra("memberid", memberid);
				brow.putExtra("groupid", groupid);
				brow.putExtra("name", name);
				brow.putExtra("remark", remark);
				brow.setClass(meminfo.this, mdfphoto.class);
				startActivity(brow);
				meminfo.this.finish();
			}
			
		});
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 0:
					try {
						joresult=new JSONObject(remark);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(joresult==null){
						cnu=remark.split("-");
						if(cnu.length>1){
							tvname.setText("name:"+"\t\t\t\t\t"+name);
							tvcompany.setText("company:"+"\t\t\t"+cnu[0]);
							tvnumber.setText("number:"+"\t\t\t\t"+cnu[1]);
							tvtitle.setText("create_at:"+"\t\t"+create);
							tvcreate.setText("update_at:"+"\t"+update);}
							else{
						     tvname.setText("name:"+"\t\t\t\t\t"+name);
							 tvcompany.setText("remark:"+"\t\t\t\t"+cnu[0]);
							 tvnumber.setText("create_at:"+"\t\t"+create);
							 tvtitle.setText("update_at:"+"\t"+update);
							}
					}else{
						String company=null;
						try {
							company=joresult.getString("company");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String number=null;
						try {
							number=joresult.getString("number");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String title=null;
						try {
							title=joresult.getString("title");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						tvname.setText("姓名："+"\t\t\t\t\t"+name);
						tvcompany.setText("公司："+"\t\t\t\t\t"+company);
						tvnumber.setText("编号："+"\t\t\t\t\t"+number);
						tvtitle.setText("职位："+"\t\t\t\t\t"+title);
						tvcreate.setText("创建于："+"\t\t"+create);
						tvupdate.setText("更新于："+"\t\t"+update);
						tvqdjl.setText("签到记录:");
						}
					break;
				case 1:
					iv.setImageResource(R.drawable.notface);
					break;
				case 2:
					iv.setImageBitmap(bmp);
					break;
				case 3:
					adapter.notifyDataSetChanged();
					listview.setAdapter(adapter);
					break;
	            default:
	                //do something
	                break;
			  }
				super.handleMessage(msg);
			}
			
		};
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					String urlget="http://182.92.237.212:16667/v1/member/get_info";
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("api_key", apikey));
					params.add(new BasicNameValuePair("api_secret", apisecret));
					params.add(new BasicNameValuePair("member_id", memberid));
					HttpPost memre = new HttpPost(urlget);
					memre.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					HttpResponse memres = new DefaultHttpClient().execute(memre);
					String meminfo=EntityUtils.toString(memres.getEntity(),"UTF_8").trim();
					//Log.e("meminfo",meminfo);
					//Log.e("api",apikey+" "+apisecret+" "+memberid);
					JSONObject memjo=new JSONObject(meminfo);
					name=memjo.getString("name");
					remark=memjo.getString("remark");
					create=memjo.getString("create_at");
					update=memjo.getString("update_at");
				    photourl=memjo.getString("photo");
				    
				    
						Log.e("photourl",photourl);
						URL url=new URL(photourl);
		                   URLConnection con = url.openConnection();
							handler.sendEmptyMessage(0);
							InputStream is = con.getInputStream();
							byte[] buffer = new byte[1024];  
					        int len = 0;  
					        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
					        while((len = is.read(buffer)) != -1) {  
					            bos.write(buffer, 0, len);  
					        }  
					        bos.close();  
							byte[] be=bos.toByteArray();
							bmp=BitmapFactory.decodeByteArray(be, 0, be.length);
									    
				    handler.sendEmptyMessage(2);
					handler.sendEmptyMessage(0);
					String atURL="http://182.92.237.212:16667/v1/attendance/get_info";
					List<NameValuePair> atparams = new ArrayList<NameValuePair>();
					atparams.add(new BasicNameValuePair("api_key", apikey));
					atparams.add(new BasicNameValuePair("api_secret", apisecret));
					atparams.add(new BasicNameValuePair("group_id", groupid));
					atparams.add(new BasicNameValuePair("member_id", memberid));
					atparams.add(new BasicNameValuePair("start_at", startat));
					atparams.add(new BasicNameValuePair("end_at", endat));
					Log.e("start_at",startat);
			        Log.e("end_at",endat);
					HttpPost atre = new HttpPost(atURL);
					atre.setEntity(new UrlEncodedFormEntity(atparams, HTTP.UTF_8));
					HttpResponse athaha = new DefaultHttpClient().execute(atre);
					String atresult=EntityUtils.toString(athaha.getEntity(),"UTF_8").trim();
					Log.e("result",atresult);
					JSONObject atjo=new JSONObject(atresult);
					JSONArray atja=atjo.getJSONArray("attendance");
					for(int i=0;i<atja.length();i++){
						JSONObject attjo=atja.getJSONObject(i);
						qdjl.add(attjo.getString("date"));
					}
					if(qdjl.isEmpty()){
						qdjl.add("wu");
					}
					handler.sendEmptyMessage(3);
				}catch(Exception e){
					e.printStackTrace();
					handler.sendEmptyMessage(0);
					handler.sendEmptyMessage(1);
				}
			}
			
		}).start();
		
		
		adapter=new qdjladapter(meminfo.this,qdjl);
		
	}

}
