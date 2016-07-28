package com.demo.charile.dianping.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.maiziedu.dianping_client.R;
//���ƶ�Ӧ��Ӣ����ĸ
public class SiderBar extends View {
	//XML�ļ������ؼ�����ʱ����
	public SiderBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	//new ����ʱ����
	public SiderBar(Context context) {
		super(context);
	}
	private Paint  paint = new Paint();//����
	// 26����ĸ
	public static String[] sideBar = { "����","A", "B", "C", "D", "E", "F", "G", "H", "I",
					"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
					"W", "X", "Y", "Z" };
	private OnTouchingLetterChangedListener letterChangedListener;
	private int choose;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(Color.GRAY);//���û��ʻ�ɫ
		paint.setTypeface(Typeface.DEFAULT_BOLD);//����������ʽ����
		paint.setTextSize(20);
		//��ȡ�Զ���View�Ŀ�͸�
		int height = getHeight();
		int width = getWidth();
		//�趨ÿһ����ĸ���ڿؼ��ĸ߶�
		int each_height = height/sideBar.length;
		//��ÿһ����ĸ���Ƴ���
		for (int i = 0; i < sideBar.length; i++) {
			//��������������x���ƫ����
			float x =  width/2-paint.measureText(sideBar[i])/2;
			//��������������Y���ƫ����
			float y = (1+i)*each_height;
			canvas.drawText(sideBar[i], x, y, paint);
		}
	}
	//��������¼�
	public interface OnTouchingLetterChangedListener{
		public void onTouchingLetterChanged(String s);//��ݻ���λ�õ�������������
	}
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener){
		this.letterChangedListener = onTouchingLetterChangedListener;
	}
	//�ַ���Ӧ��touch����
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();//��ȡ��Ӧ�Ķ���
		final float y = event.getY();//�����y���
		final OnTouchingLetterChangedListener listener = letterChangedListener;
		final int c = (int)(y/getHeight()*sideBar.length);//��ȡ���y�������ռ�ܸ߶ȵı���*����ĳ��Ⱦ��ǵ��������е������ĸ����
		switch (action) {
		case MotionEvent.ACTION_UP://̧��
			setBackgroundResource(android.R.color.transparent);
			invalidate();
			break;

		default:
			setBackgroundResource(R.drawable.sidebar_background);
			if (c>0&&c<sideBar.length) {
				if (listener!=null) {
					listener.onTouchingLetterChanged(sideBar[c]);
				}
				choose = c;
				invalidate();
			}
			break;
		}
		return true;
		
	}
}
