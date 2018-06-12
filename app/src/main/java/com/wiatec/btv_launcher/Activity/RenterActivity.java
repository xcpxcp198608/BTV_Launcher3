package com.wiatec.btv_launcher.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.px.common.constant.CommonApplication;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.EnumLevel;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.databinding.ActivityRenterBinding;
import com.wiatec.btv_launcher.presenter.RenterPresenter;


public class RenterActivity extends Base2Activity<IRenterActivity, RenterPresenter> implements
        IRenterActivity, View.OnClickListener {

    private ActivityRenterBinding binding;

    @Override
    protected RenterPresenter createPresenter() {
        return new RenterPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_renter);
        binding.btConfirm.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isRenter = (boolean) SPUtil.get(F.sp.is_renter ,true);
        if(isRenter){
            String key = (String) SPUtil.get(F.sp.username, "");
            binding.etKey.setText(key);
            binding.etKey.setSelection(key.length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
                String key = binding.etKey.getText().toString();
                if(TextUtils.isEmpty(key)){
                    Toast.makeText(CommonApplication.context, "input error", Toast.LENGTH_LONG).show();
                    return;
                }
                AuthRentUserInfo authRentUserInfo = new AuthRentUserInfo();
                authRentUserInfo.setClientKey(key);
                authRentUserInfo.setMac((String) SPUtil.get(F.sp.ethernet_mac,"1"));
                presenter.login(authRentUserInfo);
                binding.progressBar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLogin(ResultInfo<AuthRentUserInfo> resultInfo) {
        binding.progressBar.setVisibility(View.GONE);
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
        if(AuthRentUserInfo.CATEGORY_B1.equals(authRentUserInfo.getCategory())){
            level = EnumLevel.L2.getL();
        }else if(AuthRentUserInfo.CATEGORY_P1.equals(authRentUserInfo.getCategory())){
            level = EnumLevel.L4.getL();
        }else if(AuthRentUserInfo.CATEGORY_P2.equals(authRentUserInfo.getCategory())){
            level = EnumLevel.L4.getL();
        }
        SPUtil.put(F.sp.level, level);
        SPUtil.put(F.sp.is_renter, true);
        SPUtil.put(F.sp.rental_category, authRentUserInfo.getCategory());
        finish();
    }

}
