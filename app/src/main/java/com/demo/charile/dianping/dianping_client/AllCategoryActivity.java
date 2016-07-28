package com.demo.charile.dianping.dianping_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.charile.dianping.entity.ResponseObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.maiziedu.consts.CONSTS;
import com.maiziedu.dianping_client.R;
import com.maiziedu.entity.Category;
import com.maiziedu.entity.ResponseObject;
import com.maiziedu.utils.MyUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.experimental.categories.Category;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

public class AllCategoryActivity extends Activity {
	@ViewInject(R.id.home_nav_all_categray)
	private ListView categoryList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ???????
		setContentView(R.layout.home_index_nav_all);
		x.view().inject(this);
		// ????
		// categoryList.setAdapter(new MyAdapter());
		// ??????????
		new CategoryDataTask().execute();
		
	}

	// ??????????????
	public class CategoryDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			//restructuring with xutils3 or okHttp

//			HttpClient client = new DefaultHttpClient();
//			HttpPost post = new HttpPost(CONSTS.Category_Data_URI);
//			try {
//				HttpResponse response = client.execute(post);
//				if (response.getStatusLine().getStatusCode() == 200) {
//					String jsonString = EntityUtils.toString(response
//							.getEntity());
//					// ????????????
//					paseCategoryDataJson(jsonString);
//				}
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			//????
			MyAdapter adapter = new MyAdapter();
			categoryList.setAdapter(adapter);
		}
	}


	//restructring with GSON

//	// ??????????
//	public void paseCategoryDataJson(String json) {
//		Gson gson = new Gson();
//		ResponseObject<List<Category>> responseObject = gson.fromJson(json,
//				new TypeToken<ResponseObject<List<Category>>>() {
//				}.getType());
//		List<Category> datas = responseObject.getDatas();
//		//????????
//		for (Category category : datas) {
//			int position = Integer.parseInt(category.getCategoryId());
//			//???????§Ö?categoryId???????????¡À?categoryNumber????????›¥???
//			MyUtils.allCategoryNumber[position-1] = category.getCategoryNumber();
//		}
//	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MyUtils.allCategray.length;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyHolder myHolder = null;
			if (convertView == null) {
				myHolder = new MyHolder();
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.home_index_nav_all_item, null);
				x.view().inject(myHolder, convertView);
				convertView.setTag(myHolder);// ????
			} else {
				myHolder = (MyHolder) convertView.getTag();
			}
			// ???
			myHolder.textDesc.setText(MyUtils.allCategray[position]);
			myHolder.imageView
					.setImageResource(MyUtils.allCategrayImages[position]);
			myHolder.textNumber.setText(MyUtils.allCategoryNumber[position]+"");
			return convertView;
		}
	}

	public class MyHolder {
		@ViewInject(R.id.home_nav_all_item_number)
		public TextView textNumber;
		@ViewInject(R.id.home_nav_all_item_desc)
		public TextView textDesc;
		@ViewInject(R.id.home_nav_all_item_image)
		public ImageView imageView;
	}

	// ????????
	@Event(value = R.id.home_nav_all_back,type = View.OnClickListener.class)
	private void onClick(View view) {
		switch (view.getId()) {
		case R.id.home_nav_all_back:// ???????
			finish();
			break;

		default:
			break;
		}
	}
}
