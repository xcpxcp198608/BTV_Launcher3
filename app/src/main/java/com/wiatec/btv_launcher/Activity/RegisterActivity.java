package com.wiatec.btv_launcher.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.px.common.utils.RegularUtil;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.databinding.ActivityRegisterBinding;
import com.wiatec.btv_launcher.presenter.RegisterPresenter;


public class RegisterActivity extends Base2Activity<IRegisterActivity, RegisterPresenter>
        implements IRegisterActivity, View.OnKeyListener, View.OnClickListener {

    private ActivityRegisterBinding binding;
    private String userName;
    private String firstName;
    private String lastName;
    private String language;

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.btRegister.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this ,
                android.R.layout.simple_spinner_item , getResources().getStringArray(R.array.languages1));
        binding.spLanguage.setAdapter(arrayAdapter);
        binding.spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        language = "English";
                        break;
                    case 1:
                        language = "Spanish";
                        break;
                    case 2:
                        language = "Chinese";
                        break;
                    case 3:
                        language = "French";
                        break;
                    case 4:
                        language = "Italian";
                        break;
                    case 5:
                        language = "German";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {
        userName = binding.etUsername.getText().toString().trim();
        firstName = binding.etFirstName.getText().toString().trim();
        lastName = binding.etLastName.getText().toString().trim();
        String password = binding.etPassword.getText().toString();
        String password1 = binding.etPassword1.getText().toString();
        String email = binding.etEmail.getText().toString();
        String email1 = binding.etEmail1.getText().toString();
        String phone = binding.etPhone.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.username_input_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.first_name_input_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.last_name_input_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.phone_input_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone.length() < 7){
            Toast.makeText(RegisterActivity.this, getString(R.string.phone_format_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.email_input_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!RegularUtil.validateEmail(email)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.email_format_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.equals(email1)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.email_different_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password_input_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password_format_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password1)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password_input_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(password1)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password_input_different), Toast.LENGTH_SHORT).show();
            return;
        }
        AuthRegisterUserInfo authRegisterUserInfo = new AuthRegisterUserInfo();
        authRegisterUserInfo.setUsername(userName);
        authRegisterUserInfo.setFirstName(firstName);
        authRegisterUserInfo.setLastName(lastName);
        authRegisterUserInfo.setPassword(password);
        authRegisterUserInfo.setEmail(email);
        authRegisterUserInfo.setPhone(phone);
        authRegisterUserInfo.setMac((String) SPUtil.get(F.sp.ethernet_mac,""));
        presenter.register(authRegisterUserInfo ,language);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void register(ResultInfo<AuthRegisterUserInfo> resultInfo) {
        binding.progressBar.setVisibility(View.GONE);
        if(resultInfo == null) return;
        Logger.d(resultInfo.toString());
        if (resultInfo.getCode() == 200) {
            SPUtil.put(F.sp.username, userName);
            SPUtil.put(F.sp.first_name, firstName);
            SPUtil.put(F.sp.last_name, lastName);
            SPUtil.put(F.sp.is_renter, false);
            Toast.makeText(CommonApplication.context, getString(R.string.register_success), Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, resultInfo.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
