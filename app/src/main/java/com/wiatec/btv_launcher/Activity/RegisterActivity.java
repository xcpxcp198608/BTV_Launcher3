package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.AuthRegisterUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.presenter.RegisterPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by patrick on 2017/3/10.
 */

public class RegisterActivity extends Base2Activity<IRegisterActivity, RegisterPresenter>
        implements IRegisterActivity, View.OnKeyListener {


    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_last_name)
    EditText etLastName;
    @BindView(R.id.et_nick_name)
    EditText etNickName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password1)
    EditText etPassword1;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_email1)
    EditText etEmail1;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.sp_language)
    Spinner spLanguage;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String userName;
    private String firstName;
    private String lastName;
    private String nickName;
    private String password;
    private String password1;
    private String email;
    private String email1;
    private String phone;
    private String language;

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this ,
                android.R.layout.simple_spinner_item , getResources().getStringArray(R.array.languages1));
        spLanguage.setAdapter(arrayAdapter);
        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    @OnClick(R.id.bt_register)
    public void onClick() {
        userName = etUsername.getText().toString().trim();
        firstName = etFirstName.getText().toString().trim();
        lastName = etLastName.getText().toString().trim();
        nickName = etNickName.getText().toString().trim();
        password = etPassword.getText().toString();
        password1 = etPassword1.getText().toString();
        email = etEmail.getText().toString();
        email1 = etEmail1.getText().toString();
        phone = etPhone.getText().toString();
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
//        if (TextUtils.isEmpty(nickName)) {
//            Toast.makeText(RegisterActivity.this, getString(R.string.nick_name_input_error), Toast.LENGTH_SHORT).show();
//            return;
//        }
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
//        if (!RegularUtil.validateEmail(email)) {
//            Toast.makeText(RegisterActivity.this, getString(R.string.email_format_error), Toast.LENGTH_SHORT).show();
//            return;
//        }
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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void register(ResultInfo<AuthRegisterUserInfo> resultInfo) {
        progressBar.setVisibility(View.GONE);
        if(resultInfo == null) return;
        Logger.d(resultInfo.toString());
        if (resultInfo.getCode() == 200) {
            SPUtil.put(F.sp.username, userName);
            SPUtil.put(F.sp.first_name, firstName);
            SPUtil.put(F.sp.last_name, lastName);
            Toast.makeText(CommonApplication.context, getString(R.string.register_success), Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, resultInfo.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
