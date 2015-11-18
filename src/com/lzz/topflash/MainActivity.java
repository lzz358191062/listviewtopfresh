package com.lzz.topflash;

import java.util.ArrayList;
import java.util.List;

import com.lzz.topflash.MyListView.IRefresh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

public class MainActivity extends Activity implements IRefresh{

	private MyListView mListView;
	private MyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getData();
		ShowView();
	}
	private void ShowView() {
		if(mListView==null){
			mListView = (MyListView) findViewById(R.id.listview);
			mListView.setInterface(this);
			adapter = new MyAdapter(this, list);
			mListView.setAdapter(adapter);
		}else{
			adapter.onDataChange(list);
		}
	}
	
	
	List<ApkEntity> list = new ArrayList<ApkEntity>();
	private void getData() {
		for(int i=0;i<10;i++){
			ApkEntity entity = new ApkEntity();
			entity.setName("��"+i+"������");
			entity.setInfo("��"+i+"����Ϣ");
			entity.setDes("��"+i+"������");
			list.add(entity);
		}
	}
	private void getFreshData() {
		for(int i=0;i<2;i++){
			ApkEntity entity = new ApkEntity();
			entity.setName("ˢ�µ�"+i+"������");
			entity.setInfo("ˢ�µ�"+i+"����Ϣ");
			entity.setDes("ˢ�µ�"+i+"������");
			list.add(0,entity);
		}
	}
	@Override
	public void onFresh() {
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getFreshData();
				ShowView();
				mListView.refreshComplete();
			}
		}, 2000);
	}

}
