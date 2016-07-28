package com.demo.charile.dianping.dianping_client;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.demo.charile.dianping.dianping_client.fragment.FragmentHome;
import com.demo.charile.dianping.dianping_client.fragment.FragmentMy;
import com.demo.charile.dianping.dianping_client.fragment.FragmentSearch;
import com.demo.charile.dianping.dianping_client.fragment.FragmentTuan;
import com.maiziedu.dianping_client.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener{
	@ViewInject(R.id.main_bottom_tabs)
	private RadioGroup group;
	@ViewInject(R.id.main_home)
	private RadioButton main_home;
	private FragmentManager fragmentManager;//����fragment����
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		x.view().inject(this);
        //��ʼ��FragmentManager
        fragmentManager = getSupportFragmentManager();
        //����Ĭ��ѡ��
        main_home.setChecked(true);
        group.setOnCheckedChangeListener(this);
        //�л���ͬ��fragment
        changeFragment(new FragmentHome(), false);
    }
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.main_home://��ҳ
				changeFragment(new FragmentHome(), true);
				break;
			case R.id.main_my://�ҵ�
				changeFragment(new FragmentMy(), true);
				break;
			case R.id.main_search://����
				changeFragment(new FragmentSearch(), true);
				break;
			case R.id.main_tuan://�Ź�
				changeFragment(new FragmentTuan(), true);
				break;

		default:
			break;
		}
		
	}
	//�л���ͬ��fragment
	public void changeFragment(Fragment fragment,boolean isInit){
		//��������
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.main_content, fragment);
		if (!isInit) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
}
