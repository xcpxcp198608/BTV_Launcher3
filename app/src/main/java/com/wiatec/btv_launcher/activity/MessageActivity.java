package com.wiatec.btv_launcher.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.MessageDao;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.adapter.MessageAdapter;
import com.wiatec.btv_launcher.bean.MessageInfo;
import com.wiatec.btv_launcher.custom_view.DividerItemDecoration;
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

public class MessageActivity extends BaseActivity<IMessageActivity, MessagePresenter> implements IMessageActivity {

    @BindView(R.id.lv_message)
    ListView lv_Message;
    @BindView(R.id.ibt_clean)
    ImageButton ibt_Clean;

    private MessageDao messageDao;
    private MessageAdapter messageAdapter;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                                showMessageByBrowser(messageInfo.getLink());
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

    private void showMessageByBrowser(String url){
        startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(url)));
    }
}
