package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.databinding.ActivityLoginBinding;
import com.wiatec.btv_launcher.presenter.LoginPresenter;

public class LoginActivity extends Base2Activity<ILoginActivity, LoginPresenter>
        implements ILoginActivity, TextView.OnEditorActionListener, View.OnClickListener {

    private ActivityLoginBinding binding;
    private String userName;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.etPassword.setOnEditorActionListener(this);
        binding.etEmail1.setOnEditorActionListener(this);
        binding.btCreateAccount.setOnClickListener(this);
        binding.btForgetPassword.setOnClickListener(this);
        binding.btLogin.setOnClickListener(this);
        binding.btRenter.setOnClickListener(this);
        binding.btReset.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isRenter = (boolean) SPUtil.get(F.sp.is_renter ,true);
        if(!isRenter) {
            userName = (String) SPUtil.get(F.sp.username, "");
            binding.etUsername.setText(userName);
            binding.etUsername.setSelection(userName.length());
        }
    }

    private void doLogin(){
        userName = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(userName) && ! TextUtils.isEmpty(password)){
            AuthRegisterUserInfo authRegisterUserInfo = new AuthRegisterUserInfo();
            authRegisterUserInfo.setUsername(userName);
            authRegisterUserInfo.setPassword(password);
            authRegisterUserInfo.setMac((String) SPUtil.get(F.sp.ethernet_mac,""));
            presenter.login(authRegisterUserInfo);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(LoginActivity.this , getString(R.string.error_input) , Toast.LENGTH_LONG).show();
        }
    }

    private void doReset(){
        String userName1 = binding.etUsername1.getText().toString().trim();
        String email1 = binding.etEmail1.getText().toString().trim();
        if(!TextUtils.isEmpty(userName1) && ! TextUtils.isEmpty(email1)){
            AuthRegisterUserInfo authRegisterUserInfo = new AuthRegisterUserInfo();
            authRegisterUserInfo.setUsername(userName1);
            authRegisterUserInfo.setEmail(email1);
            presenter.resetPassword(authRegisterUserInfo);
            binding.progressBar1.setVisibility(View.VISIBLE);
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
                binding.llLogin.setVisibility(View.GONE);
                binding.llResetPassword.setVisibility(View.VISIBLE);
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
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(CommonApplication.context, "login success", Toast.LENGTH_LONG).show();
            AuthRegisterUserInfo authRegisterUserInfo = resultInfo.getData();
            SPUtil.put(F.sp.username, authRegisterUserInfo.getUsername());
            SPUtil.put(F.sp.token, authRegisterUserInfo.getToken());
            SPUtil.put(F.sp.last_name, authRegisterUserInfo.getLastName());
            SPUtil.put(F.sp.level, authRegisterUserInfo.getLevel() + "");
            SPUtil.put(F.sp.is_renter, false);
            finish();
        } else {
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(CommonApplication.context, resultInfo.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void resetp(ResultInfo<AuthRegisterUserInfo> resultInfo) {
        binding.progressBar1.setVisibility(View.GONE);
        if(resultInfo ==null) return;
        if(resultInfo.getCode() == 200){
            binding.llResetPassword.setVisibility(View.GONE);
            binding.llLogin.setVisibility(View.VISIBLE);
        }
        Logger.d(resultInfo.toString());
        Toast.makeText(CommonApplication.context, resultInfo.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if(binding.llResetPassword.getVisibility() == View.VISIBLE){
                binding.llResetPassword.setVisibility(View.GONE);
                binding.llLogin.setVisibility(View.VISIBLE);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
