package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.Result;
import com.wiatec.btv_launcher.bean.UserInfo;
import com.wiatec.btv_launcher.presenter.RegisterPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by patrick on 2017/3/10.
 */

public class RegisterActivity extends Base2Activity<IRegisterActivity, RegisterPresenter> implements IRegisterActivity {


    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password1)
    EditText etPassword1;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.sp_language)
    Spinner spLanguage;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String userName;
    private String password;
    private String password1;
    private String email;
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

    @OnClick(R.id.bt_register)
    public void onClick() {
        userName = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        password1 = etPassword1.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.username_input_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password_input_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password1)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password_input_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password_input_error), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(password1)) {
            Toast.makeText(RegisterActivity.this, getString(R.string.password_input_different), Toast.LENGTH_SHORT).show();
            return;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setPassword(password);
        userInfo.setEmail(email);
        presenter.register(userInfo, deviceInfo ,language);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void register(Result result) {
        progressBar.setVisibility(View.GONE);
        if (result.getCode() == Result.CODE_REGISTER_OK) {
            SPUtils.put(RegisterActivity.this, "userName", userName);
            Toast.makeText(Application.getContext(), getString(R.string.register_success), Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, result.getStatus(), Toast.LENGTH_LONG).show();
        }
    }
}
