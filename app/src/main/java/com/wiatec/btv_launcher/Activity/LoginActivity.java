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

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by patrick on 2016/12/29.
 */

public class LoginActivity extends BaseActivity<ILoginActivity, LoginPresenter> implements ILoginActivity {

    @BindView(R.id.et_key)
    EditText etKey;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

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

    @OnClick(R.id.bt_login)
    public void onClick() {
        String key = etKey.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(key) && ! TextUtils.isEmpty(password)){
            presenter.login(key ,password);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(LoginActivity.this , getString(R.string.error_input) , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void login(String result) {
        Logger.d(result);
        if ("ok".equals(result)) {
            progressBar.setVisibility(View.GONE);
            startActivity(new Intent(LoginActivity.this, PinentryActivity.class));
            finish();
        } else  if ("repeat".equals(result)){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this , getString(R.string.error_repeat) , Toast.LENGTH_LONG).show();
        }else  if ("error1".equals(result)){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this , getString(R.string.error_connect) , Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this , getString(R.string.error_key) , Toast.LENGTH_LONG).show();
        }
    }


}
