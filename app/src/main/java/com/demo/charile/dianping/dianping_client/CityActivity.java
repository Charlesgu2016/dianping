package com.demo.charile.dianping.dianping_client;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.charile.dianping.entity.City;
import com.demo.charile.dianping.entity.ResponseObject;
import com.demo.charile.dianping.view.SiderBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maiziedu.dianping_client.R;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends Activity implements SiderBar.OnTouchingLetterChangedListener{

	@ViewInject(R.id.city_list)
	private ListView listDatas;
	private List<City> cityList;
	@ViewInject(R.id.city_side_bar)
	private SiderBar siderBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_city_list);
		x.view().inject(this);

		View view = LayoutInflater.from(this).inflate(R.layout.home_city_search,
				null);
		listDatas.addHeaderView(view);
		//ִ���첽����
		new CityDataTask().execute();
		siderBar.setOnTouchingLetterChangedListener(this);//���Զ���ViewSiderBar����touch����
	}
	@Event(value = {R.id.index_city_back,R.id.index_city_flushcity},type = View.OnClickListener.class)
	private void onClick(View view){
		switch (view.getId()) {
		case R.id.index_city_back://����
			finish();
			break;
		case R.id.index_city_flushcity://ˢ��
			new CityDataTask().execute();
			break;

		default:
			break;
		}
	}
	@Event(value = {R.id.city_list},type = View.OnClickListener.class)
	private void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		Intent intent = new Intent();
		TextView textView = (TextView) view.findViewById(R.id.city_list_item_name);
		intent.putExtra("cityName", textView.getText().toString());
		setResult(RESULT_OK, intent);
		finish();
	}	
	//ʹ���첽�����ȡ���е�json��
	public class CityDataTask extends AsyncTask<Void, Void, List<City>>{
		@Override
		protected List<City> doInBackground(Void... params) {

			//restructuring with okhttp or xutils

//			HttpClient client = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost(CONSTS.City_Data_URI);
//			try {
//				HttpResponse httpResponse = client.execute(httpPost);
//				if (httpResponse.getStatusLine().getStatusCode()==200) {
//					String jsonString = EntityUtils.toString(httpResponse.getEntity());
//					return parseCityDatasJson(jsonString);
//				}
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return null;
		}
		@Override
		protected void onPostExecute(List<City> result) {
			super.onPostExecute(result);
			cityList = result;
			//������ʾ
			MyAdapter adapter = new MyAdapter(cityList);
			listDatas.setAdapter(adapter);
		}
	}
	//����������ݵ�json
	public List<City> parseCityDatasJson(String json){
		Gson gson = new Gson();
		ResponseObject<List<City>> responseObject = gson.fromJson(json, new TypeToken<ResponseObject<List<City>>>(){}.getType());
		return responseObject.getDatas();
	}
	private StringBuffer buffer = new StringBuffer();//������һ�α�������ĸ������
	private List<String> firdtList = new ArrayList<String>();//������������ֵ����ĳ������
	//������
	public class MyAdapter extends BaseAdapter{
		private List<City> listCityDatas;
		
		public MyAdapter(List<City> listCityDatas) {
			this.listCityDatas = listCityDatas;
		}

		@Override
		public int getCount() {
			return listCityDatas.size();//���ؼ��ϵĳ���
		}

		@Override
		public Object getItem(int position) {
			return listCityDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView==null) {
				holder = new Holder();
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_city_list_item, null);
				x.view().inject(holder, convertView);
				convertView.setTag(holder);
			}else{
				holder = (Holder) convertView.getTag();
			}
			//�����ʾ�Ĵ���
			City city = listCityDatas.get(position);
			String sort = city.getSortKey();
			String name = city.getName();
			//�������
			if (buffer.indexOf(sort)==-1) {
				buffer.append(sort);
				firdtList.add(name);
			}
			if (firdtList.contains(name)) {
				holder.keySort.setText(sort);
				holder.keySort.setVisibility(View.VISIBLE);//���Ӧ�ĳ��о���������ʾ
			}else{
				holder.keySort.setVisibility(View.GONE);
			}
			holder.cityName.setText(name);
			return convertView;
		}
		
	}
	public class Holder{
		@ViewInject(R.id.city_list_item_sort)
		public TextView keySort;
		@ViewInject(R.id.city_list_item_name)
		public TextView cityName;
	}
	@Override
	public void onTouchingLetterChanged(String s) {
		//�ҵ�listView����ʾ������λ��
		listDatas.setSelection(findIndex(cityList, s));
	}
	//���s�ҵ���Ӧ��s��λ��
	public int findIndex(List<City> list,String s){
		if (list!=null) {
			for (int i = 0; i < list.size(); i++) {
				City city = list.get(i);
				//���city�е�sortKey���бȽ�
				if (s.equals(city.getSortKey())) {
					return i;
				}
			}
		}else{
			Toast.makeText(this, "������Ϣ", Toast.LENGTH_SHORT).show();
		}
		return -1;
	}
}

