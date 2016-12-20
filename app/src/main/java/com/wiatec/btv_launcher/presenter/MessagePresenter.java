package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Activity.IMessageActivity;

/**
 * Created by PX on 2016-11-14.
 */

public class MessagePresenter extends BasePresenter<IMessageActivity> {

    private IMessageActivity iMessageActivity;


    public MessagePresenter(IMessageActivity iMessageActivity) {
        this.iMessageActivity = iMessageActivity;

    }

    public void loadMessage (){

    }
}
