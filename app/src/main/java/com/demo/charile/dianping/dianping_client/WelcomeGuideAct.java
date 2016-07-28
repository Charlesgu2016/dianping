package com.demo.charile.dianping.dianping_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.demo.charile.dianping.R;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class WelcomeGuideAct extends Activity {
	@ViewInject(R.id.welcome_guide_btn)
	private Button btn;

	@ViewInject(R.id.welcome_pager)
	private ViewPager pager;

	private List<View> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		x.Ext.init(getApplication());
		initViewPager();
		setContentView(R.layout.welcome_guide);
	}
	@Event(value = R.id.welcome_guide_btn,type = View.OnClickListener.class)
	private void onClick(View view){
		//页面的跳转
		startActivity(new Intent(getBaseContext(), MainActivity.class));
		finish();
	}
	//初始化ViewPager的方法
	private void initViewPager(){
		list = new ArrayList<View>();
		ImageView iv = new ImageView(this);
		iv.setImageResource(R.drawable.guide_01);
		list.add(iv);
		ImageView iv1 = new ImageView(this);
		iv1.setImageResource(R.drawable.guide_02);
		list.add(iv1);
		ImageView iv2 = new ImageView(this);
		iv2.setImageResource(R.drawable.guide_03);
		list.add(iv2);
		pager.setAdapter(new MyPagerAdapter());
		//监听ViewPager滑动效果
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			//页卡被选中的方法
			@Override
			public void onPageSelected(int arg0) {
				//如果是第三个页面
				if (arg0==2) {
					btn.setVisibility(View.VISIBLE);
				}else{
					btn.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}

			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	}
	//定义ViewPager的适配器
	class MyPagerAdapter extends PagerAdapter{
		//计算需要多少item显示
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		//初始化item实例方法
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}
		//item销毁的方法
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);
			container.removeView(list.get(position));
		}

	}
}
