package com.demo.charile.dianping.dianping_client.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.maiziedu.consts.CONSTS;
import com.maiziedu.dianping_client.GoodsDetailActivity;
import com.maiziedu.dianping_client.R;
import com.maiziedu.entity.Goods;
import com.maiziedu.entity.ResponseObject;
import com.squareup.picasso.Picasso;

//�Ź�
public class FragmentTuan extends Fragment {
	@ViewInject(R.id.index_listGoods)
	private PullToRefreshListView listGoods;
	//����Ʒ�б�����ʱ����ʾ����
	@OnItemClick(R.id.index_listGoods)
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
		intent.putExtra("goods", listDatas.get(position-1));
		startActivity(intent);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tuan_index, null);
		ViewUtils.inject(this, view);
		//������Ʒ����Ϣ�б�����
		listGoods.setMode(Mode.BOTH);//֧������Ҳ֧������
		listGoods.setScrollingWhileRefreshingEnabled(true);//������ʱ�򲻼������
		listGoods.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//����ˢ�� y<0
				loadDatas(listGoods.getScrollY()<0);
				
			}
		});
		//�״�����ҳ���ʱ��Ҫ�Զ��������
		new Handler(new  Handler.Callback() {
			
			@Override
			public boolean handleMessage(Message arg0) {
				listGoods.setRefreshing();
				return true;
			}
		}).sendEmptyMessageDelayed(0, 300);
		return view;
	}

	private int page, size = 10, count;// ��ʼ�����
	private MyAdapter adapter;
	private List<Goods> listDatas;

	// �������
	public void loadDatas(final boolean reflush) {
		if (reflush) {
			page = 1;
		}else{
			page++;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("page", page + "");
		params.addQueryStringParameter("size", size + "");
		// ʹ��Xutils����е����HTTP�����װ�õķ���
		new HttpUtils().send(HttpMethod.GET, CONSTS.Goods_Datas_URL, params,
				new RequestCallBack<String>() {
					// ����ʧ�ܵ�ʱ��ִ�еķ���
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						listGoods.onRefreshComplete();//ֹͣˢ��
						Toast.makeText(getActivity(), arg1, Toast.LENGTH_SHORT)
								.show();
					}

					// �ɹ����󷵻صķ���������Ϊ���ص���ݣ������һ��ResponseInfo���������������һϵ�еĲ����ַ��õ���ʱ����д���
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						listGoods.onRefreshComplete();//ֹͣˢ��
						Gson gson = new Gson();
						ResponseObject<List<Goods>> object = gson.fromJson(
								arg0.result,
								new TypeToken<ResponseObject<List<Goods>>>() {
								}.getType());
						// ��ȡ���ݵĶ����з�װ������
						page = object.getPage();
						size = object.getSize();
						count = object.getCount();
//						listDatas = object.getDatas();
////						Log.i("TAG", "�������------------" + listDatas.size());
////						Log.i("TAG", "page = " + page + " , size = " + size
////								+ " , count = " + count);
//						adapter = new MyAdapter();
//						listGoods.setAdapter(adapter);
						//�ж�������ˢ�»�����������
						if (reflush) {//����ˢ��
							listDatas = object.getDatas();
							adapter = new MyAdapter();
							listGoods.setAdapter(adapter);
						}else{//���ظ��
							listDatas.addAll(object.getDatas());
							adapter.notifyDataSetChanged();
						}
						
						if (count==page) {//û�и��������ʾ
							listGoods.setMode(Mode.PULL_FROM_START);//ֻ��ˢ��
						}
						
					}

				});
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listDatas.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		// ��Ⱦÿһ��item��Ӧ����ͼ
		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			MyHolder holder = null;
			if (convertView == null) {
				holder = new MyHolder();
				// ӳ�����ͼ
				convertView = LayoutInflater.from(arg2.getContext()).inflate(
						R.layout.tuan_goods_list_item, null);
				ViewUtils.inject(holder, convertView);
				convertView.setTag(holder);
			} else {
				holder = (MyHolder) convertView.getTag();
			}
			// ��ȡ��Ӧ����������
			Goods goods = listDatas.get(arg0);
			//picasso���������ͼƬ�ĳ���oom�Լ�ͼƬ��λ
			Picasso.with(arg2.getContext()).load(goods.getImgUrl()).placeholder(R.drawable.ic_empty_dish).into(holder.image);
			
			
			StringBuffer sbf = new StringBuffer("��"+goods.getValue());
			//����л���
			SpannableString spannable = new SpannableString(sbf);
			spannable.setSpan(new StrikethroughSpan(), 0, sbf.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
			holder.value.setText(spannable);
			holder.count.setText(goods.getBought() + "��");
			holder.price.setText("��" + goods.getPrice());
			holder.title.setText(goods.getSortTitle());
			holder.titleContent.setText(goods.getTitle());
			return convertView;
		}

	}

	// ��ǩ��
	public class MyHolder {
		@ViewInject(R.id.index_gl_item_image)
		public ImageView image;
		@ViewInject(R.id.index_gl_item_title)
		public TextView title;
		@ViewInject(R.id.index_gl_item_titlecontent)
		public TextView titleContent;
		@ViewInject(R.id.index_gl_item_price)
		public TextView price;
		@ViewInject(R.id.index_gl_item_value)
		public TextView value;
		@ViewInject(R.id.index_gl_item_count)
		public TextView count;
	}
}