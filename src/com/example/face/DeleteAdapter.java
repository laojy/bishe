package com.example.face;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
public class DeleteAdapter extends BaseAdapter implements Filterable{
	private Context context;
    private ArrayList<stu> text;
	private LayoutInflater mInflater;
	private Handler handler;
	private String apikey;
	private String apisecret;
	private PersonFilter filter;
	AlertDialog adg;
	private void del(final int index) {
		try{
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String urlremovemem="http://182.92.237.212:16667/v1/group/remove_mem";
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("api_key", apikey));
					params.add(new BasicNameValuePair("api_secret", apisecret));
					params.add(new BasicNameValuePair("group_id", text.get(index).getGroupid()));
					params.add(new BasicNameValuePair("member_id", text.get(index).getMemberid()));
					HttpPost re = new HttpPost(urlremovemem);
					try {
						re.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					HttpResponse haha = null;
					try {
						haha = new DefaultHttpClient().execute(re);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String result2=null;
					try {
						result2=EntityUtils.toString(haha.getEntity(),"UTF_8").trim();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Log.e("remove_result",result2);
   					String urldelmem="http://182.92.237.212:16667/v1/member/delete";
					List<NameValuePair> param = new ArrayList<NameValuePair>();
					param.add(new BasicNameValuePair("api_key", apikey));
					param.add(new BasicNameValuePair("api_secret", apisecret));
					param.add(new BasicNameValuePair("member_id", text.get(index).getMemberid()));
					HttpPost delmem= new HttpPost(urldelmem);
					try {
						delmem.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					HttpResponse del = null;
					try {
						del = new DefaultHttpClient().execute(delmem);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String result=null;
					try {
					   result=EntityUtils.toString(del.getEntity(),"UTF_8").trim();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.e("del_result",result);
					JSONObject jo=null;
					try {
						jo=new JSONObject(result);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					String urltrain="http://182.92.237.212:16667/v1/group/train";
					List<NameValuePair> pa = new ArrayList<NameValuePair>();
					pa.add(new BasicNameValuePair("api_key", apikey));
					pa.add(new BasicNameValuePair("api_secret", apisecret));
					pa.add(new BasicNameValuePair("group_id", text.get(index).getGroupid()));
					HttpPost train= new HttpPost(urltrain);
					try {
						train.setEntity(new UrlEncodedFormEntity(pa, HTTP.UTF_8));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						new DefaultHttpClient().execute(train);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					if(!"".equals(jo.optString("msg"))){
						handler.sendEmptyMessage(2);
					}else{
						text.remove(index);
						handler.sendEmptyMessage(0);
						handler.sendEmptyMessage(1);
					}
					
				}
				
			}).start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public DeleteAdapter(Context context, ArrayList<stu> text,Handler handler,String apikey,String apisecret) {
        this.context = context;
        this.text = text;
        mInflater=LayoutInflater.from(context);
        this.handler=handler;
        this.apikey=apikey;
        this.apisecret=apisecret;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return text.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return text.get(arg0).getMemberid();
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
			view=mInflater.inflate(R.layout.stuitem, null);}
			TextView tv=(TextView)view.findViewById(R.id.tvs);
			tv.setText(text.get(index).getName());
			final ImageButton btn1=(ImageButton)view.findViewById(R.id.ds);
			btn1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					 new AlertDialog.Builder(context).setTitle("提示")
					  
				     .setMessage("真的要删除吗？")
				  
				     .setPositiveButton("取消",new DialogInterface.OnClickListener() {
				  
				          
				  
				         @Override  
				  
				         public void onClick(DialogInterface dialog, int which) {
				  
				             // TODO Auto-generated method stub  
				  
				        	dialog.dismiss(); 
				  
				         }  
				  
				     }).setNegativeButton("确定",new DialogInterface.OnClickListener() {
				  
				          
				  
				         @Override  
				  
				         public void onClick(DialogInterface dialog, int which) {
				  
				             // TODO Auto-generated method stub  
				  
				             del(index);
				         }  
				  
				     }).show();//在按键响应事件中显示此对话框  
				  
				  
				      
				  
				 }  
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
				

			
				});
				
		
			
			
		
		return view;
	}
	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		if(filter==null){
			filter = new PersonFilter(text);
		}
		return filter;
	}
private class PersonFilter extends Filter {  
        
        private ArrayList<stu> original;  
          
        public PersonFilter(ArrayList<stu> list) {  
            this.original = list;  
        }  

        @Override  
        protected FilterResults performFiltering(CharSequence constraint) {  
            FilterResults results = new FilterResults();  
            if (constraint == null || constraint.length() == 0) {  
                results.values = original;  
                results.count = original.size();  
            } else {  
                ArrayList<stu> mList = new ArrayList<stu>();  
                for (stu p: original) {  
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))  
                         {  
                        mList.add(p);  
                    }  
                }  
                results.values = mList;  
                results.count = mList.size();  
            }  
            return results;  
        }  

        @Override  
        protected void publishResults(CharSequence constraint,  
                FilterResults results) {  
            text = (ArrayList<stu>)results.values;  
            //notifyDataSetChanged(); 
            handler.sendEmptyMessage(0);
        }  
          
    }  

}
