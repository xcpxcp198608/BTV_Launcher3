package com.wiatec.btv_launcher.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.presenter.RenterPresenter;


import butterknife.BindView;
import butterknife.ButterKnife;

public class RenterActivity extends Base2Activity<IRenterActivity, RenterPresenter> implements
        IRenterActivity, View.OnClickListener {
    @BindView(R.id.et_key)
    EditText etKey;
    @BindView(R.id.bt_confirm)
    Button btConfirm;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected RenterPresenter createPresenter() {
        return new RenterPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renter);
        ButterKnife.bind(this);
        btConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                String key = etKey.getText().toString();
                if(TextUtils.isEmpty(key)){
                    Toast.makeText(Application.getContext(), "input error", Toast.LENGTH_LONG).show();
                    return;
                }
                AuthRentUserInfo authRentUserInfo = new AuthRentUserInfo();
                authRentUserInfo.setClientKey(key);
                authRentUserInfo.setMac((String) SPUtils.get(RenterActivity.this,"ethernetMac","1"));
                presenter.login(authRentUserInfo);
                progressBar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLogin(ResultInfo<AuthRentUserInfo> resultInfo) {
        progressBar.setVisibility(View.GONE);
        if(resultInfo == null){
            Toast.makeText(Application.getContext(), "communication error", Toast.LENGTH_LONG).show();
            return;
        }
        if(resultInfo.getCode() != ResultInfo.CODE_OK){
            Toast.makeText(Application.getContext(), resultInfo.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        AuthRentUserInfo authRentUserInfo = resultInfo.getData();
        SPUtils.put(RenterActivity.this,"userName", authRentUserInfo.getClientKey());
        SPUtils.put(RenterActivity.this,"token", authRentUserInfo.getClientKey());
        SPUtils.put(RenterActivity.this,"lastName", authRentUserInfo.getLastName());
        String level = "";
        if("B1".equals(authRentUserInfo.getCategory())){
            level = "2";
        }else if("P1".equals(authRentUserInfo.getCategory())){
            level = "4";
        }else if("P2".equals(authRentUserInfo.getCategory())){
            level = "4";
        }
        SPUtils.put(RenterActivity.this,"userLevel", level);
        SPUtils.put(RenterActivity.this,"isRenter", true);
        finish();
    }

}
