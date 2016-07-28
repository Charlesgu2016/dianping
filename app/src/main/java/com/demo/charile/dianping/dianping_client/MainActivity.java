package com.demo.charile.dianping.dianping_client;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.demo.charile.dianping.R;
import com.demo.charile.dianping.dianping_client.fragment.FragmentHome;
import com.demo.charile.dianping.dianping_client.fragment.FragmentMy;
import com.demo.charile.dianping.dianping_client.fragment.FragmentSearch;
import com.demo.charile.dianping.dianping_client.fragment.FragmentTuan;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener{
	@ViewInject(R.id.main_bottom_tabs)
	private RadioGroup group;
	@ViewInject(R.id.main_home)
	private RadioButton main_home;
	private FragmentManager fragmentManager;//管理fragment的类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		x.view().inject(this);
		//初始化FragmentManager
        fragmentManager = getSupportFragmentManager();
		//设置默认选中
        main_home.setChecked(true);
        group.setOnCheckedChangeListener(this);
		//切换不同的fragment
        changeFragment(new FragmentHome(), false);
    }
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.main_home://首页
				changeFragment(new FragmentHome(), true);
				break;
			case R.id.main_my://我的
				changeFragment(new FragmentMy(), true);
				break;
			case R.id.main_search://发现
				changeFragment(new FragmentSearch(), true);
				break;
			case R.id.main_tuan://团购
				changeFragment(new FragmentTuan(), true);
				break;

		default:
			break;
		}
		
	}
	//切换不同的fragment
	public void changeFragment(Fragment fragment,boolean isInit){
		//开启事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.main_content, fragment);
		if (!isInit) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
}
