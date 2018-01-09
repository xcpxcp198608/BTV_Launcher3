package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by patrick on 2016/12/29.
 */

public class LoginActivity extends Base2Activity<ILoginActivity, LoginPresenter>
        implements ILoginActivity, TextView.OnEditorActionListener {

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
        etPassword.setOnEditorActionListener(this);
        etEmail1.setOnEditorActionListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isRenter = (boolean) SPUtil.get(F.sp.is_renter ,true);
        if(!isRenter) {
            userName = (String) SPUtil.get(F.sp.username, "");
            etUserName.setText(userName);
            etUserName.setSelection(userName.length());
        }
    }

    private void doLogin(){
        userName = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(userName) && ! TextUtils.isEmpty(password)){
            AuthRegisterUserInfo authRegisterUserInfo = new AuthRegisterUserInfo();
            authRegisterUserInfo.setUsername(userName);
            authRegisterUserInfo.setPassword(password);
            authRegisterUserInfo.setMac((String) SPUtil.get(F.sp.ethernet_mac,""));
            presenter.login(authRegisterUserInfo);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(LoginActivity.this , getString(R.string.error_input) , Toast.LENGTH_LONG).show();
        }
    }

    private void doReset(){
        userName1 = etUserName1.getText().toString().trim();
        email1 = etEmail1.getText().toString().trim();
        if(!TextUtils.isEmpty(userName1) && ! TextUtils.isEmpty(email1)){
            AuthRegisterUserInfo authRegisterUserInfo = new AuthRegisterUserInfo();
            authRegisterUserInfo.setUsername(userName1);
            authRegisterUserInfo.setEmail(email1);
            presenter.resetPassword(authRegisterUserInfo);
            progressBar1.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(LoginActivity.this , getString(R.string.error_input) , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE){
            if(v.getId() == R.id.et_password){
                doLogin();
            }
            if(v.getId() == R.id.et_email1){
                doReset();
            }
        }
        return false;
    }

    @OnClick({R.id.bt_login, R.id.bt_renter, R.id.bt_create_account ,R.id.bt_forget_password , R.id.bt_reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                doLogin();
                break;
            case R.id.bt_renter:
                startActivity(new Intent(LoginActivity.this, RenterActivity.class));
                this.finish();
                break;
            case R.id.bt_create_account:
                startActivity(new Intent(this ,RegisterActivity.class));
                break;
            case R.id.bt_forget_password:
                String userName = (String) SPUtil.get(F.sp.username ,"");
                String token = (String) SPUtil.get(F.sp.token ,"");
                if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(token)) {
                    llLogin.setVisibility(View.GONE);
                    llResetPassword.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(LoginActivity.this , "please login first" , Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_reset:
                doReset();
                break;
        }
    }

    @Override
    public void login(ResultInfo<AuthRegisterUserInfo> resultInfo) {
        if(resultInfo ==null) return;
        Logger.d(resultInfo.toString());
        if (resultInfo.getCode() == 200) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(CommonApplication.context, "login success", Toast.LENGTH_LONG).show();
            AuthRegisterUserInfo authRegisterUserInfo = resultInfo.getData();
            SPUtil.put(F.sp.username, authRegisterUserInfo.getUsername());
            SPUtil.put(F.sp.token, authRegisterUserInfo.getToken());
            SPUtil.put(F.sp.last_name, authRegisterUserInfo.getLastName());
            SPUtil.put(F.sp.level, authRegisterUserInfo.getLevel() + "");
            SPUtil.put(F.sp.is_renter, false);
            finish();
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(CommonApplication.context, resultInfo.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void resetp(ResultInfo<AuthRegisterUserInfo> resultInfo) {
        progressBar1.setVisibility(View.GONE);
        if(resultInfo ==null) return;
        if(resultInfo.getCode() == 200){
            llResetPassword.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
        }
        Logger.d(resultInfo.toString());
        Toast.makeText(CommonApplication.context, resultInfo.getMessage(), Toast.LENGTH_LONG).show();
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
