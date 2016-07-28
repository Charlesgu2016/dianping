package com.demo.charile.dianping.dianping_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.charile.dianping.R;
import com.demo.charile.dianping.consts.CONSTS;
import com.demo.charile.dianping.entity.City;
import com.demo.charile.dianping.entity.ResponseObject;
import com.demo.charile.dianping.view.SiderBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.home_city_list)
public class CityActivity extends Activity implements SiderBar.OnTouchingLetterChangedListener {

    private List<City> cityList;

    @ViewInject(R.id.city_list)
    private ListView listDatas;

    @ViewInject(R.id.city_side_bar)
    private SiderBar siderBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        x.Ext.init(getApplication());

        View view = LayoutInflater.from(this).inflate(R.layout.home_city_search, null);

        listDatas.addHeaderView(view);
        //执行异步任务

        siderBar.setOnTouchingLetterChangedListener(this);//给自定义ViewSiderBar设置touch监听

        LoadCityData();

    }

    private void LoadCityData() {

        RequestParams params =  new RequestParams(CONSTS.City_Data_URI);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ResponseObject<List<City>> object = gson.fromJson(result,new TypeToken<ResponseObject<List<City>>>(){}.getType());
                cityList = object.getDatas();
                MyAdapter adapter = new MyAdapter(cityList);
                listDatas.setAdapter(adapter);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getApplication(), "onErrorMsg" + ex.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    @Event(value = {R.id.index_city_back, R.id.index_city_flushcity}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.index_city_back://返回
                finish();
                break;
            case R.id.index_city_flushcity://刷新
                LoadCityData();
                break;

            default:
                break;
        }
    }

    @Event(value = {R.id.city_list}, type = ListView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view,
                             int position, long id) {

        Intent intent = new Intent();
        TextView textView = (TextView) view.findViewById(R.id.city_list_item_name);
        intent.putExtra("cityName", textView.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    //使用异步任务获取城市的json串



    private StringBuffer buffer = new StringBuffer();//用来第一次保存首字母的索引
    private List<String> firdtList = new ArrayList<String>();//用来保存索引值对象的城市名称

    //适配器
    public class MyAdapter extends BaseAdapter {
        private List<City> listCityDatas;

        public MyAdapter(List<City> listCityDatas) {
            this.listCityDatas = listCityDatas;
        }

        @Override
        public int getCount() {
            return listCityDatas.size();//返回集合的长度
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
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_city_list_item, null);
                x.view().inject(holder, convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            //数据显示的处理
            City city = listCityDatas.get(position);
            String sort = city.getSortKey();
            String name = city.getName();
            //索引不存在
            if (buffer.indexOf(sort) == -1) {
                buffer.append(sort);
                firdtList.add(name);
            }
            if (firdtList.contains(name)) {
                holder.keySort.setText(sort);
                holder.keySort.setVisibility(View.VISIBLE);//包含对应的城市就让索引显示
            } else {
                holder.keySort.setVisibility(View.GONE);
            }
            holder.cityName.setText(name);
            return convertView;
        }

    }

    public class Holder {
        @ViewInject(R.id.city_list_item_sort)
        public TextView keySort;
        @ViewInject(R.id.city_list_item_name)
        public TextView cityName;
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        //找到listView中显示的索引位置
        listDatas.setSelection(findIndex(cityList, s));
    }

    //根据s找到对应的s的位置
    public int findIndex(List<City> list, String s) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                City city = list.get(i);
                //根据city中的sortKey进行比较
                if (s.equals(city.getSortKey())) {
                    return i;
                }
            }
        } else {
            Toast.makeText(this, "暂无信息", Toast.LENGTH_SHORT).show();
        }
        return -1;
    }
}
