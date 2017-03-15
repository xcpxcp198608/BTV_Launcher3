package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.MessageDao;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.adapter.MessageAdapter;
import com.wiatec.btv_launcher.bean.MessageInfo;
import com.wiatec.btv_launcher.presenter.MessagePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by PX on 2016-11-14.
 */

public class MessageActivity extends Base1Activity<IMessageActivity, MessagePresenter> implements IMessageActivity {

    @BindView(R.id.lv_message)
    ListView lv_Message;
    @BindView(R.id.ibt_clean)
    ImageButton ibt_Clean;

    private MessageDao messageDao;
    private MessageAdapter messageAdapter;
    private View popView;
    private PopupWindow popupWindow;
    private boolean isPopup;

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        messageDao = MessageDao.getInstance(MessageActivity.this);
        popView = getLayoutInflater().inflate(R.layout.message_popupwindow, null, false);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.item_btv_bg));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<MessageInfo>>() {
                    @Override
                    public List<MessageInfo> call(String s) {
                        return messageDao.queryUnreadMessage();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<MessageInfo>>() {
                    @Override
                    public void call(final List<MessageInfo> messageInfos) {
                        messageAdapter = new MessageAdapter(MessageActivity.this, messageInfos);
                        lv_Message.setAdapter(messageAdapter);
                        lv_Message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    @OnClick(R.id.ibt_clean)
    public void onClick() {
        messageDao.setAllRead();
        onStart();
    }

    private void showPopupwindow (View view , final MessageInfo messageInfo){
        TextView tv_Title = (TextView) popView.findViewById(R.id.tv_Popup_Title);
        TextView tv_Content = (TextView) popView.findViewById(R.id.tv_Popup_Content);
        Button bt_Popup = (Button) popView.findViewById(R.id.bt_Popup);
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
                    if(ApkCheck.isApkInstalled(MessageActivity.this ,messageInfo.getLink())){
                        ApkLaunch.launchApkByPackageName(MessageActivity.this , messageInfo.getLink());
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
