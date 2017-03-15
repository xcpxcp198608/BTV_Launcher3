package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.UserInfo;
import com.wiatec.btv_launcher.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by patrick on 2016/12/29.
 */

public class LoginActivity extends Base1Activity<ILoginActivity, LoginPresenter> implements ILoginActivity {

    @BindView(R.id.et_username)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.bt_create_account)
    Button btCreateAccount;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private String userName;
    private String password;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        userName = (String) SPUtils.get(LoginActivity.this , "userName" ,"");
        etUserName.setText(userName);
    }

    @OnClick({R.id.bt_login, R.id.bt_create_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                userName = etUserName.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if(!TextUtils.isEmpty(userName) && ! TextUtils.isEmpty(password)){
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserName(userName);
                    userInfo.setPassword(password);
                    presenter.login(userInfo , deviceInfo);
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(LoginActivity.this , getString(R.string.error_input) , Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_create_account:
                startActivity(new Intent(this ,RegisterActivity.class));
                break;
        }
    }

    @Override
    public void login(Result result) {
        Logger.d(result.toString());
        int code = result.getCode();
        if (code == Result.CODE_OK) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(Application.getContext(), "login success", Toast.LENGTH_LONG).show();
            SPUtils.put(LoginActivity.this,"userName", userName);
            SPUtils.put(LoginActivity.this,"currentLoginCount", result.getCount());
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(Application.getContext(), result.getStatus(), Toast.LENGTH_LONG).show();
        }
    }

}
