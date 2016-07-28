package com.demo.charile.dianping.dianping_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.demo.charile.dianping.dianping_client.utils.SharedUtils;
import com.maiziedu.dianping_client.R;

import java.util.Timer;
import java.util.TimerTask;
//��ʱ��ת����ʹ��handler
public class WelcomeStartAct extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
//		new Handler(new Handler.Callback() {
//			//������յ�����Ϣ�ķ���
//			@Override
//			public boolean handleMessage(Message msg) {
//				//ʵ��ҳ����ת
//				startActivity(new Intent(getApplicationContext(), MainActivity.class));
//				return false;
//			}
//		}).sendEmptyMessageDelayed(0, 3000);//��ʾ��ʱ������������ִ��
		//ʹ��java�еĶ�ʱ�����д���
		Timer timer = new Timer();
		timer.schedule(new Task(), 3000);//��ʱ����ʱִ������ķ���
	}
	class Task extends TimerTask{

		@Override
		public void run() {
			//ʵ��ҳ�����ת
			//���ǵ�һ������
			if (SharedUtils.getWelcomeBoolean(getBaseContext())) {
				startActivity(new Intent(getBaseContext(), MainActivity.class));
			}else{
				startActivity(new Intent(WelcomeStartAct.this, WelcomeGuideAct.class));
				//������ʼ�¼
				SharedUtils.putWelcomeBoolean(getBaseContext(), true);
			}
			finish();
		}
		
	}
}
