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

import com.maiziedu.dianping_client.R;

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
		setContentView(R.layout.welcome_guide);
		x.view().inject(this);
		initViewPager();
	}
	@Event(value = R.id.welcome_guide_btn,type = View.OnClickListener.class)
	private void click(View view){
		//ҳ�����ת
		startActivity(new Intent(getBaseContext(), MainActivity.class));
		finish();
	}
	//��ʼ��ViewPager�ķ���
	public void initViewPager(){
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
		//����ViewPager����Ч��
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			//ҳ����ѡ�еķ���
			@Override
			public void onPageSelected(int arg0) {
				//����ǵ����ҳ��
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
	//����ViewPager��������
	class MyPagerAdapter extends PagerAdapter{
		//������Ҫ����item��ʾ
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		//��ʼ��itemʵ��
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}
		//item��ٵķ���
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);
			container.removeView(list.get(position));
		}
		
	}
}
