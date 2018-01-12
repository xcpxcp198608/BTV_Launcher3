package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.databinding.ActivityMessageBinding;
import com.wiatec.btv_launcher.sql.MessageDao;
import com.px.common.utils.AppUtil;
import com.wiatec.btv_launcher.adapter.MessageAdapter;
import com.wiatec.btv_launcher.bean.MessageInfo;
import com.wiatec.btv_launcher.presenter.MessagePresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Message1Activity extends Base1Activity<IMessageActivity, MessagePresenter>
        implements IMessageActivity, View.OnClickListener {

    private ActivityMessageBinding binding;
    private MessageDao messageDao;
    private MessageAdapter messageAdapter;
    private View popView;
    private PopupWindow popupWindow;

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);
        messageDao = MessageDao.getInstance(Message1Activity.this);
        popView = getLayoutInflater().inflate(R.layout.message_popupwindow, null, false);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.item_btv_bg));
        }
        binding.ibtClean.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<MessageInfo>>() {
                    @Override
                    public List<MessageInfo> apply(String s) {
                        return messageDao.queryUnreadMessage();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MessageInfo>>() {
                    @Override
                    public void accept(final List<MessageInfo> messageInfos) {
                        messageAdapter = new MessageAdapter(Message1Activity.this, messageInfos);
                        binding.lvMessage.setAdapter(messageAdapter);
                        binding.lvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                MessageInfo messageInfo = messageInfos.get(position);
                                messageInfo.setIsRead("true");
                                messageDao.updateMessage(messageInfo);
                                showPopupwindow(view , messageInfo);
                            }
                        });
                    }
                });
    }

    @Override
    public void onClick(View view) {
        messageDao.setAllRead();
        onStart();
    }

    private void showPopupwindow (View view , final MessageInfo messageInfo){
        TextView tv_Title = popView.findViewById(R.id.tv_Popup_Title);
        TextView tv_Content = popView.findViewById(R.id.tv_Popup_Content);
        Button bt_Popup = popView.findViewById(R.id.bt_Popup);
        tv_Title.setText(messageInfo.getTitle());
        tv_Content.setText(messageInfo.getContent());
        String type = messageInfo.getType();
        if("link".equals(type)){
            bt_Popup.setVisibility(View.VISIBLE);
            bt_Popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMessageByBrowser(messageInfo.getLink());
                    popupWindow.dismiss();
                }
            });
        }else if ("app".equals(type)){
            bt_Popup.setVisibility(View.VISIBLE);
            bt_Popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AppUtil.isInstalled(messageInfo.getLink())){
                        AppUtil.launchApp(Message1Activity.this , messageInfo.getLink());
                    }
                    popupWindow.dismiss();
                }
            });
        }else {
            bt_Popup.setVisibility(View.GONE);
        }
        popupWindow.showAtLocation(view , Gravity.CENTER ,0,0);

    }

    private void showMessageByBrowser(String url){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(url)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if (popupWindow!= null && popupWindow.isShowing()){
                popupWindow.dismiss();
                messageAdapter.notifyDataSetChanged();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    
}
