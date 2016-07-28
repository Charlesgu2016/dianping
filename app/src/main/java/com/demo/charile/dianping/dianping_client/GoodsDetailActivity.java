package com.demo.charile.dianping.dianping_client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.charile.dianping.entity.Goods;
import com.demo.charile.dianping.entity.Shop;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.maiziedu.dianping_client.R;
import com.maiziedu.entity.Goods;
import com.maiziedu.entity.Shop;
import com.squareup.picasso.Picasso;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class GoodsDetailActivity extends Activity {
	@@ViewInject(R.id.goods_image)
	private ImageView goods_image;
	@ViewInject(R.id.goods_title)
	private TextView goods_title;
	@ViewInject(R.id.goods_desc)
	private TextView goods_desc;
	@ViewInject(R.id.shop_title)
	private TextView shop_title;
	@ViewInject(R.id.shop_phone)
	private TextView shop_phone;
	@ViewInject(R.id.goods_price)
	private TextView goods_price;
	@ViewInject(R.id.goods_old_price)
	private TextView goods_old_price;
	@ViewInject(R.id.tv_more_details_web_view)
	private WebView tv_more_details_web_view;
	@ViewInject(R.id.wv_gn_warm_prompt)
	private WebView wv_gn_warm_prompt;
	private Goods goods;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuan_goods_detail);
		x.view().inject(this);
		// TextView�������л���Ч��
		goods_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		// ����ҳ����Ӧ��Ļ
		WebSettings webSettings = tv_more_details_web_view.getSettings();
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		WebSettings webSettings1 = wv_gn_warm_prompt.getSettings();
		webSettings1.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			goods = (Goods) bundle.get("goods");
		}
		if (goods != null) {
			// ����ҳ�������е�����
			updateTitleImage();
			updateGoodsInfo();
			updateShopInfo();
			updateMoreDetails();
		}

	}
	@Event(value = {R.id.shop_call,R.id.goods_detail_goback},type = View.OnClickListener.class)
	private void onClick(View v){
		switch (v.getId()) {
		case R.id.shop_call:
			Intent callin = new Intent(Intent.ACTION_DIAL);
			callin.setData(Uri.parse("tel:"+goods.getShop().getTel()));
			startActivity(callin);
			break;
		case R.id.goods_detail_goback:
			finish();
		default:
			break;
		}
		
	}

	// ������Ʒ�ı���ͼƬ
	public void updateTitleImage() {
		Picasso.with(this).load(goods.getImgUrl())
				.placeholder(R.drawable.ic_empty_dish).into(goods_image);
	}
	//��Ʒ�ı��⣬��ʾ����Ǯ����
	public void updateGoodsInfo() {
		goods_title.setText(goods.getSortTitle());
		goods_desc.setText(goods.getTip());
		goods_price.setText("��"+goods.getPrice());
		goods_old_price.setText("��"+goods.getValue());
	}

	public void updateShopInfo() {
		Shop shop = goods.getShop();
		shop_title.setText(shop.getName());
		shop_phone.setText(shop.getTel());
	}
	public void updateMoreDetails() {
		String[] data = htmlSub(goods.getDetail());
		tv_more_details_web_view.loadDataWithBaseURL("", data[1], "text/html", "utf-8", "");
		wv_gn_warm_prompt.loadDataWithBaseURL("", data[0], "text/html", "utf-8", "");
	}
	/*
	 * <div class="prodetail-sp"><h4 style="background:#ff6600">
	 * ���������顿</h4><p class="ti">ƾ����ȯ���������һ�10��Լ2.5����Ʒˮ�����ѡ1��/8���±��һ�ݣ��г���238Ԫ�������Ź�����<strong class="f18" style="color:#ff6600;">98</strong>Ԫ��</p><p>ע��1����453.59237��</p><p style="text-align:center"><a style="color:#00ccff" href="http://beijing.lashou.com/deal/7260985.html" target="_new"><img src="http://img.lashou.com/wg/beijing/201209/21/7260985.jpg" border="0" /></a></p><h4 style="background:#ff6600">
	 * ����ܰ��ʾ��</h4><ul><li><p>�����Ź��������͵�ַΪ�������廷�ڵ�����û�����������ͣ� &nbsp; &nbsp; </p></li><li><p>����ȯ��2012��9��25�գ��ܶ�����Ч�� &nbsp; &nbsp; </p></li><li><p>����ȯ��Ч�ڽ�ֹ��������2014��07��31�գ�2013��2��9-15�� ������ʹ�ã� &nbsp; &nbsp; </p></li><li><p>Ӫҵʱ����ַ���� &nbsp; &nbsp; </p></li><li><p><span style="color:#ff6600;">����������ǰ1��ԤԼ�������̼���9��25�տ�ʼ����ԤԼ���绰���ַ����Ԥ��ʱ���֪���ĵ�����ƣ����ṩ����ȯ�ź����룻 </span> &nbsp; &nbsp;</p></li><li><p><span style="color:#ff6600;">������Ԥ��ʱ��ϸ˵���ջ��˵���ϵ��ʽ���ջ���ַ��������Ա��������ʱ����ǰ������ϵ��ϣ�����ܱ����ͻ������ֻ�ͨ���糬��Լ��ʱ���޷���ϵ��������Ա���������ͣ��ɴ˲�����������ջ������ге��� &nbsp;</span> &nbsp; &nbsp; </p></li><li><p>�����Ϊ�ͻ���������������1����ʳ���꣬����Ʒ0-10��ɱ���24Сʱ��5��ʳ����ѣ� &nbsp; &nbsp; </p></li><li><p>ÿ������ȯ��������һ�����⣬ÿ�����⸽�͵�����5�ף� &nbsp; &nbsp; </p></li><li><p>���ڲ�Ʒ�������ԣ�����һ���������������ⲻ���˻��� &nbsp; &nbsp; </p></li><li><p>����ȯ�����֡������㣬������������Ż�ͬʱ���á� </p></li></ul><h4 style="background:#ff6600">
	 * ����Ʒչʾ��</h4><p><strong>���ſ�</strong></p><p class="tc"><img src="http://img.lashou.com/wg/beijing/201209/21/ayk1.jpg" /></p><p><strong>��ɫ����</strong></p><p class="tc"><img src="http://img.lashou.com/wg/beijing/201209/24/fsjr1.jpg" /></p><p><strong>���Ҿ���</strong></p><p class="tc"><img src="http://img.lashou.com/wg/beijing/201209/24/htjd1.jpg" /></p><p><strong>��Ʒ�ջ�</strong></p><p class="tc"><img src="http://img.lashou.com/wg/beijing/201209/24/jpyh1.jpg" /></p><p><strong>������</strong></p><p class="tc"><img src="http://img.lashou.com/wg/beijing/201209/24/kxj1.jpg" /></p><p><strong>������</strong></p><p class="tc"><img src="http://img.lashou.com/wg/beijing/201209/24/mym1.jpg" /></p><p><strong>ʢ֮��</strong></p><p class="tc"><img src="http://img.lashou.com/wg/beijing/201209/24/szq1.jpg" /></p><p><strong>��Ũ</strong></p><p class="tc"><img src="http://img.lashou.com/wg/beijing/201209/24/yn1.jpg" /></p><p><strong>�±�� </strong>�ܶ�/��ݮ/��ݮ/������</p><p class="tc"><img src="http://img.lashou.com/wg/beijing/201209/24//ybdg1.jpg" /></p><h4 style="background:#ff6600">����������</h4><p class="ti">������������Ҫ�Ծ�Ӫ��ʽ�����ʽ���Ϊ�������Ʒ�ַ��࣬��Ҫ�л�ɫ���ͼ�������ࡣ���ڻ��ɸ�ݹ˿���Ҫ�������յ��⣬��ʽ������ӱ���۸���?��˾��������һֱ����ԡ���Ч�ʡ��߱�׼����Ʒ�ʡ�Ϊ��ҵ������ּ��������Ϊ���������ṩ�������Ĳ�Ʒ�����</p></div>
	 */
	//�������html���͵��ַ�  �ֱ��ȡ ��������  ��ܰ��ʾ  ��Ʒչʾ
	public String[] htmlSub(String html){
		char[] str = html.toCharArray();
		int len = str.length;
		Log.i("TAG", "������"+len);
		int n = 0;
		String[] data = new String[3];
		int oneIndex = 0;
		int secIndex = 1;
		int ThiIndex = 2;
		for (int i = 0; i < len; i++) {
			if (str[i]=='��') {
				n++;
				if (n ==1) oneIndex=i; 
				if (n ==2) secIndex=i; 
				if (n ==3) ThiIndex=i; 
			}
		}
		if (oneIndex>0 && secIndex >1 && ThiIndex>2) {
			data[0] = html.substring(oneIndex, secIndex);
			data[1] = html.substring(secIndex, ThiIndex);
			data[2] = html.substring(ThiIndex, html.length()-6);
		}
		return data;
	}

}
