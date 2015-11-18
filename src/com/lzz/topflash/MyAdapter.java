package com.lzz.topflash;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private List<ApkEntity> list;
	private LayoutInflater inflater;
	public MyAdapter(Context context,List<ApkEntity> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	

	public void onDataChange(List<ApkEntity> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ApkEntity mApkEntity = list.get(position);
		ViewHolder holder;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.item, null);
			holder = new ViewHolder();
			holder.item_name = (TextView) convertView.findViewById(R.id.item_name);
			holder.item_info = (TextView) convertView.findViewById(R.id.item_info);
			holder.item_des = (TextView) convertView.findViewById(R.id.item_des);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item_name.setText(mApkEntity.getName());
		holder.item_info.setText(mApkEntity.getInfo());
		holder.item_des.setText(mApkEntity.getDes());
		return convertView;
	}
	class ViewHolder{
		TextView item_name;
		TextView item_info;
		TextView item_des;
	}
	

}
