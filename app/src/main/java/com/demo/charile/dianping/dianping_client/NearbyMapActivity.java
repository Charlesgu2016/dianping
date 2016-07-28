package com.demo.charile.dianping.dianping_client;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.demo.charile.dianping.entity.Goods;
import com.demo.charile.dianping.entity.Shop;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.maiziedu.consts.CONSTS;
import com.maiziedu.dianping_client.R;
import com.maiziedu.entity.Goods;
import com.maiziedu.entity.ResponseObject;
import com.maiziedu.entity.Shop;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

//���ֵ�ͼ����ʽ
public class NearbyMapActivity extends Activity implements LocationSource,
		AMapLocationListener, OnMarkerClickListener, OnMapLoadedListener,
		OnInfoWindowClickListener, InfoWindowAdapter {
	@ViewInject(R.id.search_mymap)
	private MapView mapView;
	private AMap aMap;
	private double longitude = 116.367612;// ����
	private double latitude = 40.075483;// γ��
	private List<Goods> listDatas;// ��Ʒ��Ϣ

	private LocationManagerProxy mAMapLocManager = null;
	private OnLocationChangedListener mListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_map_act);
		x.view().inject(this);
		mapView.onCreate(savedInstanceState);
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setLocationSource(this);// �����˶�λ�ļ���
			aMap.setMyLocationEnabled(true);// ��ʾ��λ�㲢�ҿ��Դ�����λ��Ĭ����false
			aMap.setOnMapLoadedListener(this);// ����aMap���سɹ��¼��ļ���
			aMap.setOnMarkerClickListener(this);// ���õ��marker�¼��ļ�����
			aMap.setInfoWindowAdapter(this);// �����Զ����InfoWindow��ʽ
			aMap.setOnInfoWindowClickListener(this);// ���õ��infoWindow���¼�������
		}
	}

	@Event(value = { R.id.search_back, R.id.search_refresh },type = View.OnClickListener.class)
	private void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_back:// ���ذ�ť
			finish();
			break;
		case R.id.search_refresh:// ˢ�¼������
			loadData(String.valueOf(latitude), String.valueOf(longitude),
					"1000");

			break;

		default:
			break;
		}
	}

	// ���ն�λ�ĵ�ַ�������뾶�����ܱߵ����
	private void loadData(String lat, String lon, String radius) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("lat", lat);
		params.addQueryStringParameter("lon", lon);
		params.addQueryStringParameter("radius", radius);
		new HttpUtils().send(HttpMethod.GET, CONSTS.Goods_NearBy_URI, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(NearbyMapActivity.this, "��ݼ���ʧ��������",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// ������˵ķ��ص�json��ݽ��н���
						ResponseObject<List<Goods>> object = new GsonBuilder()
								.create()
								.fromJson(
										arg0.result,
										new TypeToken<ResponseObject<List<Goods>>>() {
										}.getType());
						// Log.i("TAG", object.getDatas()+"");
						object.getDatas();

						// ���õ�ͼ������
						/*
						 * new LatLng(latitude, longitude) �Ե�ǰλ��Ϊ���� 16 ���ż���
						 * ��ͼ���ż���Ϊ4-20��
						 * ���ż���ϵ�ʱ������Կ���������ĵ�ͼ�����ż����ʱ������Բ鿴��������ϸ�ĵ�ͼ�� 0
						 * Ĭ������£���ͼ�ķ���Ϊ0�ȣ���Ļ���Ϸ�ָ�򱱷���������ʱ����ת��ͼʱ��
						 * ��ͼ��������Ļ���Ϸ��ļнǶ���Ϊ��ͼ����
						 * ����Χ�Ǵ�0�ȵ�360�ȡ����磬һ��90�ȵĲ�ѯ����ڵ�ͼ�ϵ����ϵķ���ָ���� 30
						 * ��ͼ��Ƿ�ΧΪ0-45�ȡ�
						 * 
						 * ����ο���http://lbs.amap.com/api/android-sdk/guide/camera/
						 */
						aMap.animateCamera(CameraUpdateFactory
								.newCameraPosition(new CameraPosition(
										new LatLng(latitude, longitude), 16, 0,
										30)));
						// ��ǵ���ͼ��
						addMarker(listDatas);
					}
				});
	}

	// ����ݱ�ǵ���ͼ��
	public void addMarker(List<Goods> list) {
		// ������Ƕ���
		MarkerOptions markerOptions;
		for (Goods goods : list) {
			Shop shop = goods.getShop();
			markerOptions = new MarkerOptions();
			// ���õ�ǰ��markerOptions����ľ�γ��
			markerOptions.position(new LatLng(
					Double.parseDouble(shop.getLat()), Double.parseDouble(shop
							.getLon())));
			// ���ÿһ��ͼ����ʾ��Ϣ ��ʾ����Ϊ��������Լ���ǰ����Ʒ��Ǯ
			markerOptions.title(shop.getName()).snippet("��" + goods.getPrice());
			// ��ͬ���͵���Ʒ���ò�ͬ������ͼ��
			if (goods.getCategoryId().equals("3")) {
				markerOptions.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_landmark_chi));
			} else if (goods.getCategoryId().equals("5")) {
				markerOptions.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_landmark_movie));
			} else if (goods.getCategoryId().equals("8")) {
				markerOptions.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_landmark_hotel));
			} else if (goods.getCategoryId().equals("6")) {
				markerOptions.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_landmark_life));
			} else if (goods.getCategoryId().equals("4")) {
				markerOptions.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_landmark_wan));
			} else {
				markerOptions.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_landmark_default));
			}
			// �ڵ�ͼ����ʾ���е�ͼ��
			aMap.addMarker(markerOptions).setObject(goods);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		if (mAMapLocManager == null) {
			mListener = listener;
			mAMapLocManager = LocationManagerProxy.getInstance(this);
			mAMapLocManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 5000, 10, this);
		}
	}

	@Override
	public void deactivate() {

	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			// longitude = location.getLongitude();
			// latitude = location.getLatitude();
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			mListener.onLocationChanged(location);// ��ʾϵͳ��С����
			Log.i("TAG", "��ǰ�ľ��Ⱥ�γ���ǣ�" + longitude + "," + latitude);
			//������Ϣ
			loadData(latitude+"", longitude+"", "1000");
			mAMapLocManager.removeUpdates(this);
		}
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	//����ʾ�Ĵ�����е����ʱ��
	@Override
	public void onInfoWindowClick(Marker arg0) {
		//��ȡ�̵�����
		String shopName = arg0.getTitle();
		//�����������ҵ���Ӧ����Ʒ
		Goods goods = getGoodsByShopName(shopName);
		if (goods!=null) {
			//��ת������ҳ��
			Intent intent = new Intent(this, GoodsDetailActivity.class);
			intent.putExtra("goods", goods);
			startActivity(intent);
		}
	}
	//����̵���������ȡ��ǰ����Ʒ��Ϣ
	public Goods getGoodsByShopName(String shopName){
		for(Goods goods: listDatas){//������Ʒ���Ͻ������̵�ƥ��
			if (goods.getShop().getName().equals(shopName)) {
				return goods;
			}
		}
		return null;
	}
	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
