package com.demo.charile.dianping.dianping_client.fragment;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.maiziedu.dianping_client.AllCategoryActivity;
import com.maiziedu.dianping_client.CityActivity;
import com.maiziedu.dianping_client.R;
import com.maiziedu.dianping_client.utils.SharedUtils;
import com.maiziedu.utils.MyUtils;
//��ҳ
public class FragmentHome extends Fragment implements LocationListener{
	@ViewInject(R.id.index_top_city)
	private TextView topCity;
	private String cityName;//��ǰ�������
	private LocationManager locationManager;
	@ViewInject(R.id.home_nav_sort)
	private GridView navSort;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_index, null);
		ViewUtils.inject(this, view);
		//��ȡ��ݲ�����ʾ
		topCity.setText(SharedUtils.getCityName(getActivity()));
		navSort.setAdapter(new NavAdapter());
		return view;
	}
	public class NavAdapter extends BaseAdapter{
		//������Ҫ������������
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MyUtils.navsSort.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		//item��Ӧ��view�������Ⱦ
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//��convertViewΪnull��ʱ�򽲲���ת����View
			MyHolder myHolder = null;
			if (convertView==null) {
				myHolder = new MyHolder();
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_index_nav_item, null);
				ViewUtils.inject(myHolder, convertView);
				convertView.setTag(myHolder);//���ǩ
			}else{
				myHolder = (MyHolder) convertView.getTag();
			}
			//��ֵ
			myHolder.textView.setText(MyUtils.navsSort[position]);
			myHolder.imageView.setImageResource(MyUtils.navsSortImages[position]);
			//ѡ�е������ȫ���Ļ�
			if (position == MyUtils.navsSort.length-1) {
				myHolder.imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startActivity(new Intent(getActivity(), AllCategoryActivity.class));
					}
				});
			}
			return convertView;
		}
		
	}
	public class MyHolder{
		@ViewInject(R.id.home_nav_item_desc)
		public TextView textView;
		@ViewInject(R.id.home_nav_item_image)
		public ImageView imageView;
	}
	@OnClick({R.id.index_top_city,R.id.index_top_scan}) 
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.index_top_city://����
			//���з���ֵ����ת
			startActivityForResult(new Intent(getActivity(), CityActivity.class), 
					MyUtils.RequestCityCode);
			break;

		default:
			break;
		}
	}
	//���?��ֵ�Ľ��
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MyUtils.RequestCityCode && resultCode == Activity.RESULT_OK) {
			cityName = data.getStringExtra("cityName");
			topCity.setText(cityName);
		}else if (requestCode == MyUtils.RequestCaptureCode && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			Uri uri = Uri.parse(scanResult);
			Intent intent = new Intent(Intent.ACTION_VIEW , uri);
			startActivity(intent);
		}

		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//��鵱ǰ��gpsģ��
		checkGPSIsOpen();
	}
	//����Ƿ��GPS
	private void checkGPSIsOpen(){
		//��ȡ��ǰ��LocationManager����
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		boolean isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!isOpen) {
			//����gps������ҳ��
			Intent intent = new Intent();
			intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivityForResult(intent, 0);
		}
		//��ʼ��λ
		startLocation();
	}
	//ʹ��gps��λ�ķ���
	private void startLocation(){
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
	}
	//���ܲ��Ҵ�����Ϣ
	private Handler handler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what==1) {
				topCity.setText(cityName);
			}
			return false;
		}
	});
	//��ȡ��Ӧλ�õľ�γ�Ȳ��Ҷ�λ����
	private void updateWithNewLocation(Location location){
		double lat = 0.0,lng = 0.0;
		if (location!=null) {
			lat = location.getLatitude();
			lng = location.getLongitude();
			Log.i("TAG", "������"+lat+",γ���ǣ�"+lng);
		}else{
			cityName = "�޷���ȡ������Ϣ";
		}
		//ͨ��γ������ȡ��ַ�����ڵ�ַ���ж��������;�γ�Ⱦ�ȷ���йأ���ʵ���ж��������ķ�����2.���ڼ��϶�����������ֵ
		List<Address> list = null;
		Geocoder ge = new Geocoder(getActivity());
		try {
			list = ge.getFromLocation(lat, lng, 2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (list!=null && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				Address ad = list.get(i);
				cityName = ad.getLocality();//��ȡ����
			}
		}
		//���Ϳ���Ϣ
		handler.sendEmptyMessage(1);
	}
	//λ����Ϣ���ִ�еķ���
	@Override
	public void onLocationChanged(Location location) {
		//���µ�ǰ��λ����Ϣ
		updateWithNewLocation(location);
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		//�������
		SharedUtils.putCityName(getActivity(), cityName);
		//ֹͣ��λ
		stopLocation();
	}
	//ֹͣ��λ
	private  void stopLocation(){
		locationManager.removeUpdates(this);
	}
}
