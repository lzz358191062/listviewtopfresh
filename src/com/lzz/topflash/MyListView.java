package com.lzz.topflash;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyListView extends ListView implements OnScrollListener{

	
	private static final String TAG = "lzz";
	View header;
	int headerHeigth;
	public MyListView(Context context) {
		super(context);
		initView(context);
	}
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.header, null);
		measureView(header);
		headerHeigth = header.getMeasuredHeight();
		Log.d(TAG, "headerHeigth = "+headerHeigth);
		setTopPadding(-headerHeigth);
		
		this.addHeaderView(header);
		this.setOnScrollListener(this);
	}
	private void setTopPadding(int headerHeigth) {
		header.setPadding(header.getPaddingLeft(), headerHeigth, header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}
	/**
	 * 测量布局大小并告知父布局
	 * @param view
	 */
	public void measureView(View view){
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if(p==null){
			p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		}
		Log.d(TAG, "p.width = "+p.width);
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		Log.d(TAG, "width = "+width);
		int heigth ;
		int temp = p.height;
		if(temp>0){
			heigth = MeasureSpec.makeMeasureSpec(temp, MeasureSpec.EXACTLY);
		}else{
			heigth =  MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		Log.d(TAG, "heigth = "+heigth);
		view.measure(width, heigth);
	}
	private int myScrollState;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.myScrollState = scrollState;
		Log.d(TAG, "onScrollStateChanged  scrollState = "+scrollState);
	}
	private int firstVisibleItem;
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.firstVisibleItem = firstVisibleItem;
	}
	
	private int state;
	private final int NONE = 0;
	private final int PULL = 1;
	private final int RELEASE = 2;
	private final int REFRESHING = 3;
	private int startY;
	private boolean isRemark;
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.d(TAG, "onTouchEvent ACTION_DOWN");
			if(firstVisibleItem==0){
				startY = (int) ev.getY();
				isRemark = true;
			}
			break;
		case MotionEvent.ACTION_UP:
			Log.d(TAG, "onTouchEvent ACTION_UP");
			if(state==RELEASE){
				state = REFRESHING;
				refreshViewByState();
				iRefresh.onFresh();
			}else if(state==PULL){
				state = NONE;
				isRemark = false;
				refreshViewByState();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d(TAG, "onTouchEvent ACTION_MOVE");
			onMove(ev);
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	private void onMove(MotionEvent ev) {
		if(!isRemark){
			return;
		}
		int tempY = (int) ev.getY();
		int space = tempY - startY;
		int topPadding = space - headerHeigth;
		switch (state) {
		case NONE:
			Log.d(TAG, "onMove NONE");
			if(space > 0){
				state = PULL;
				refreshViewByState();
			}
			break;
		case PULL:
			Log.d(TAG, "onMove PULL space="+space);
			Log.d(TAG, "onMove PULL headerHeigth="+headerHeigth);
			Log.d(TAG, "onMove PULL scrollState="+myScrollState);
			setTopPadding(topPadding);
			if(space > headerHeigth + 30
					&& myScrollState == SCROLL_STATE_TOUCH_SCROLL){
				state = RELEASE;
				refreshViewByState();
				Log.d(TAG, "onMove PULL in");
			}
			break;
		case RELEASE:
			Log.d(TAG, "onMove RELEASE");
			setTopPadding(topPadding);
			if(space < headerHeigth+30){
				state = PULL;
				refreshViewByState();
			}else if (space <= 0){
				state = NONE;
				isRemark = false;
				refreshViewByState();
			}
			break;

		default:
			break;
		}
		
		
		
	}
	public void refreshComplete(){
		state = NONE;
		isRemark = false;
		refreshViewByState();
		TextView time = (TextView) header.findViewById(R.id.refresh_time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月DD日  hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String date_time = format.format(date);
		time.setText(date_time);
	}
	
	public void refreshViewByState(){
		TextView tv = (TextView) header.findViewById(R.id.tip);
		ImageView img =  (ImageView) header.findViewById(R.id.img_refresh);
		ProgressBar progress = (ProgressBar) header.findViewById(R.id.img_progress);
		
		RotateAnimation anim1 = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		RotateAnimation anim2 = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim2.setDuration(500);
		anim2.setFillAfter(true);
		
		switch (state) {
		case NONE:
			img.clearAnimation();
			setTopPadding(-headerHeigth);
			break;
		case PULL:
			img.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tv.setText("下拉可以刷新！");
			img.clearAnimation();
			img.setAnimation(anim2);
			break;
		case RELEASE:
			img.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tv.setText("松开可以刷新！");
			img.clearAnimation();
			img.setAnimation(anim1);
			break;
		case REFRESHING:
			setTopPadding(50);
			img.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			tv.setText("正在刷新！");
			img.clearAnimation();
			break;

		default:
			break;
		}
		
		
		
	}
	
	
	
	
	public interface IRefresh{
		public void onFresh();
	}
	
	public IRefresh iRefresh;
	public void setInterface(IRefresh iRefresh){
		this.iRefresh = iRefresh;
	}
	
	
	
	
	
	

	

	

}
