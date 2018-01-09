package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.px.common.utils.CommonApplication;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
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
    protected void onStart() {
        super.onStart();
        boolean isRenter = (boolean) SPUtil.get(F.sp.is_renter ,true);
        if(isRenter){
            String key = (String) SPUtil.get(F.sp.username, "");
            etKey.setText(key);
            etKey.setSelection(key.length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                String key = etKey.getText().toString();
                if(TextUtils.isEmpty(key)){
                    Toast.makeText(CommonApplication.context, "input error", Toast.LENGTH_LONG).show();
                    return;
                }
                AuthRentUserInfo authRentUserInfo = new AuthRentUserInfo();
                authRentUserInfo.setClientKey(key);
                authRentUserInfo.setMac((String) SPUtil.get(F.sp.ethernet_mac,"1"));
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
            Toast.makeText(CommonApplication.context, "communication error", Toast.LENGTH_LONG).show();
            return;
        }
        if(resultInfo.getCode() != 200){
            Toast.makeText(CommonApplication.context, resultInfo.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        AuthRentUserInfo authRentUserInfo = resultInfo.getData();
        SPUtil.put(F.sp.username, authRentUserInfo.getClientKey());
        SPUtil.put(F.sp.token, authRentUserInfo.getClientKey());
        SPUtil.put(F.sp.last_name, authRentUserInfo.getLastName());
        String level = "";
        if("B1".equals(authRentUserInfo.getCategory())){
            level = "2";
        }else if("P1".equals(authRentUserInfo.getCategory())){
            level = "4";
        }else if("P2".equals(authRentUserInfo.getCategory())){
            level = "4";
        }
        SPUtil.put(F.sp.level, level);
        SPUtil.put(F.sp.is_renter, true);
        SPUtil.put(F.sp.rental_category, authRentUserInfo.getCategory());
        finish();
    }

}
