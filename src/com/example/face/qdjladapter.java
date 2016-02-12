package com.example.face;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class qdjladapter extends BaseAdapter {
	private Context context;
    private ArrayList<String> qdjl=null;
    public qdjladapter(Context context, ArrayList<String> qdjl) {
		super();
		this.context = context;
		this.qdjl = qdjl;
		mInflater=LayoutInflater.from(context);
	}

	private LayoutInflater mInflater;
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return qdjl.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return qdjl.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view=arg1;
		if(view==null){
			view=mInflater.inflate(R.layout.qdjlitem,null);
		}
		if(qdjl.get(0)=="wu"){
			TextView qdjltv=(TextView)view.findViewById(R.id.qdjltv);
			Log.e("qdjl","null");
			qdjltv.setText("\t\t\t没有签到记录");
		}else{
		TextView qdjltv=(TextView)view.findViewById(R.id.qdjltv);
		qdjltv.setText(qdjl.get(arg0));}
		return view;
	}

}
