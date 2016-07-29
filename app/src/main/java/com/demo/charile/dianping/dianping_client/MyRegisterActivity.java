package com.demo.charile.dianping.dianping_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.charile.dianping.R;
import com.demo.charile.dianping.consts.CONSTS;
import com.demo.charile.dianping.utils.MyUtils;
import com.demo.charile.dianping.utils.SharedUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Charile on 16/7/29.
 */
@ContentView(R.layout.my_register_act)
public class MyRegisterActivity extends Activity {
    //添加对象
    @ViewInject(R.id.register_back)
    private ImageView registerBack;

    @ViewInject(R.id.my_register_phone)
    private EditText registerPhone;

    @ViewInject(R.id.my_register_get_check_pass)
    private EditText registerCheckPass;

    @ViewInject(R.id.register_get_check_pass)
    private Button registerCheck;

    @ViewInject(R.id.my_register_get_pass)
    private EditText registerPass;

    @ViewInject(R.id.register_btn)
    private Button registerBtn;

    @ViewInject(R.id.register_info)
    private CheckBox registerInfo;

    private EventHandler eh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);
        x.Ext.init(getApplication());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    public class CountDown extends CountDownTimer {

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            //更新倒计时
            registerCheck.setText(l / 1000 + "秒后重新发送");

        }

        @Override
        public void onFinish() {
            registerCheck.setBackgroundResource(R.drawable.mini_block_item_normal);
            registerCheck.setText("重新发送");
            registerCheck.setClickable(true);
        }
    }

    @Event(value = {R.id.register_back, R.id.register_info, R.id.register_btn, R.id.register_get_check_pass}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_back:
                finish();
                break;
            case R.id.register_info:
                if (registerInfo.isChecked()) {
                    registerInfo.setChecked(false);
                    registerBtn.setClickable(false);
                } else {
                    registerInfo.setChecked(true);
                    registerBtn.setClickable(true);
                }
                break;
            case R.id.register_btn:
                //提交注册
                SMSSDK.submitVerificationCode("86", registerPhone.getText().toString(), registerPass.getText().toString());

                register();
                break;
            case R.id.register_get_check_pass:
                if (registerPhone.getText().toString().trim().length() > 0) {
                    registerCheck.setClickable(false);
                    registerCheck.setBackgroundResource(R.drawable.mini_orange_bg);
                    CountDown countDown = new CountDown(60000, 1000);
                    countDown.start();
                    sendSMSRandom();

                } else {
                    Toast.makeText(this, "请填写手机号", Toast.LENGTH_SHORT).show();
                }

                //获取验证码
                break;
            default:
                break;
        }
    }

    private void sendSMSRandom() {
        //初始化
        SMSSDK.initSDK(this, "158f4bf83656c", "87e2d110cfdce9b04448ebb21cdf1fdf");
        eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

        String phoneName = registerPhone.getText().toString();

        SMSSDK.getVerificationCode("86", phoneName);

    }

    private void register() {

        final String phone = registerPhone.getText().toString().trim();
        final String pass = registerPass.getText().toString().trim();

        if (phone.length() > 0 && pass.length() > 0) {

            RequestParams params = new RequestParams(CONSTS.USER_LOGIN);
            params.addQueryStringParameter("flag", "register");
            params.addQueryStringParameter("username", phone);
            params.addQueryStringParameter("password", pass);

            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Toast.makeText(getApplication(), "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.putExtra("loginName", phone);
                    setResult(MyUtils.RequestLoginCode, intent);
                    SharedUtils.putUserName(getApplication(), phone);
                    finish();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Toast.makeText(getApplication(), "注册失败", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

        }


    }
}
