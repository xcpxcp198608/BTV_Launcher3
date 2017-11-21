package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.User1Info;
import com.wiatec.btv_launcher.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by patrick on 2016/12/29.
 */

public class LoginActivity extends Base2Activity<ILoginActivity, LoginPresenter> implements ILoginActivity {

    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.et_username)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.ll_reset_password)
    LinearLayout llResetPassword;
    @BindView(R.id.et_username1)
    EditText etUserName1;
    @BindView(R.id.et_email1)
    EditText etEmail1;
    @BindView(R.id.bt_reset)
    Button btReset;
    @BindView(R.id.bt_create_account)
    Button btCreateAccount;
    @BindView(R.id.bt_renter)
    Button btRenter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;


    private String userName;
    private String password;
    private String userName1;
    private String email1;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userName = (String) SPUtils.get(LoginActivity.this , "userName" ,"");
        etUserName.setText(userName);
        etUserName.setSelection(userName.length());
    }

    @OnClick({R.id.bt_login, R.id.bt_renter, R.id.bt_create_account ,R.id.bt_forget_password , R.id.bt_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                userName = etUserName.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if(!TextUtils.isEmpty(userName) && ! TextUtils.isEmpty(password)){
                    User1Info user1Info = new User1Info();
                    user1Info.setUserName(userName);
                    user1Info.setPassword(password);
                    user1Info.setMac((String) SPUtils.get(LoginActivity.this,"mac",""));
                    user1Info.setEthernetMac((String) SPUtils.get(LoginActivity.this,"ethernetMac",""));
                    user1Info.setCountry((String) SPUtils.get(LoginActivity.this,"country",""));
                    user1Info.setRegion((String) SPUtils.get(LoginActivity.this,"region",""));
                    user1Info.setCity((String) SPUtils.get(LoginActivity.this,"city",""));
                    user1Info.setTimeZone((String) SPUtils.get(LoginActivity.this,"timeZone",""));
                    presenter.login(user1Info);
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(LoginActivity.this , getString(R.string.error_input) , Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_renter:
                startActivity(new Intent(LoginActivity.this, RenterActivity.class));
                this.finish();
                break;
            case R.id.bt_create_account:
                startActivity(new Intent(this ,RegisterActivity.class));
                break;
            case R.id.bt_forget_password:
                llLogin.setVisibility(View.GONE);
                llResetPassword.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_reset:
                userName1 = etUserName1.getText().toString().trim();
                email1 = etEmail1.getText().toString().trim();
                if(!TextUtils.isEmpty(userName1) && ! TextUtils.isEmpty(email1)){
                    User1Info user1Info = new User1Info();
                    user1Info.setUserName(userName1);
                    user1Info.setEmail(email1);
                    presenter.resetPassword(user1Info);
                    progressBar1.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(LoginActivity.this , getString(R.string.error_input) , Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void login(Result result) {
        Logger.d(result.toString());
        int code = result.getCode();
        if (code == Result.CODE_LOGIN_SUCCESS) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(Application.getContext(), "login success", Toast.LENGTH_LONG).show();
            SPUtils.put(LoginActivity.this,"userName", userName);
            SPUtils.put(LoginActivity.this,"currentLoginCount", result.getLoginCount());
            SPUtils.put(LoginActivity.this,"token", result.getToken());
            SPUtils.put(LoginActivity.this,"lastName", result.getExtra());
            SPUtils.put(LoginActivity.this,"userLevel", result.getUserLevel()+"");
            finish();
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(Application.getContext(), result.getStatus(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void resetp(Result result) {
        progressBar1.setVisibility(View.GONE);
        if(result == null){
            return;
        }
        Toast.makeText(Application.getContext(), result.getStatus(), Toast.LENGTH_LONG).show();
        llResetPassword.setVisibility(View.GONE);
        llLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if(llResetPassword.getVisibility() == View.VISIBLE){
                llResetPassword.setVisibility(View.GONE);
                llLogin.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
