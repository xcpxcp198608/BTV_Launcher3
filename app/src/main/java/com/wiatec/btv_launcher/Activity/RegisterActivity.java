package com.wiatec.btv_launcher.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.UserInfo;
import com.wiatec.btv_launcher.databinding.ActivityRegisterBinding;
import com.wiatec.btv_launcher.presenter.RegisterPresenter;

/**
 * Created by patrick on 2017/3/10.
 */

public class RegisterActivity extends BaseActivity<IRegisterActivity , RegisterPresenter> implements IRegisterActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_register);
        binding.setOnEvent(new OnEventListener());
    }

    public class OnEventListener{
        public void onClick(View view){
            switch (view.getId()){
                case R.id.bt_register:
                    String userName = binding.etUsername.getText().toString().trim();
                    String password = binding.etPassword.getText().toString().trim();
                    String password1 = binding.etPassword1.getText().toString().trim();
                    String email = binding.etEmail.getText().toString().trim();
                    if(TextUtils.isEmpty(userName)){
                        Toast.makeText(RegisterActivity.this , getString(R.string.username_input_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(password)){
                        Toast.makeText(RegisterActivity.this , getString(R.string.password_input_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(password1)){
                        Toast.makeText(RegisterActivity.this , getString(R.string.password_input_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(email)){
                        Toast.makeText(RegisterActivity.this , getString(R.string.password_input_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(! password.equals(password1)){
                        Toast.makeText(RegisterActivity.this , getString(R.string.password_input_different), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserName(userName);
                    userInfo.setPassword(password);
                    userInfo.setEmail(email);
                    presenter.register(userInfo , deviceInfo);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void register(Result result) {
        Logger.d(result.toString());
    }
}
