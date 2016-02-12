package com.example.face;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addinfo extends Activity {
  String name=null;
  String company=null;
  String number=null;
  String title=null;
  String other=null;
  String apikey=null;
  String apisecret=null;
  String groupid=null;
  EditText etna=null;
  EditText etc=null;
  EditText etnu=null;
  EditText etti=null;
  Button btn=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addinfo);
		Bundle bun=getIntent().getExtras();
		apikey=bun.getString("apikey");
		apisecret=bun.getString("apisecret");
		groupid=bun.getString("groupid");
		etna=(EditText)super.findViewById(R.id.aietn);
		etc=(EditText)super.findViewById(R.id.aietc);
		etnu=(EditText)super.findViewById(R.id.aietnu);
		etti=(EditText)super.findViewById(R.id.aietti);
		btn=(Button)super.findViewById(R.id.aib);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				name=etna.getText().toString();
				company=etc.getText().toString();
				number=etnu.getText().toString();
				title=etti.getText().toString();
				if(company.equals("")){
					Toast.makeText(addinfo.this,"«ÎÃÓ–¥π´Àæ", Toast.LENGTH_SHORT).show();
				}else if(name.equals("")){
					Toast.makeText(addinfo.this,"«ÎÃÓ–¥–’√˚", Toast.LENGTH_SHORT).show();
				}else if(number.equals("")){
					Toast.makeText(addinfo.this,"«ÎÃÓ–¥π§∫≈", Toast.LENGTH_SHORT).show();
				}else if(title.equals("")){
					Toast.makeText(addinfo.this,"«ÎÃÓ–¥÷∞Œª", Toast.LENGTH_SHORT).show();
				}else{
					JSONObject jo=new JSONObject();
					try {
						jo.put("company", company);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						jo.put("number", number);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						jo.put("title", title);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					other=jo.toString();
					Intent brow=new Intent();
					brow.putExtra("apikey", apikey);
					brow.putExtra("apisecret", apisecret);
					brow.putExtra("groupid", groupid);
					brow.putExtra("name", name);
					brow.putExtra("other", other);
					brow.setClass(addinfo.this, MyCameraDemo.class);
					startActivity(brow);
				}
				
			}
			
		});
	}

}
