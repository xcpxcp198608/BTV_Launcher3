package com.wiatec.btv_launcher.fragment;

import android.support.v4.app.Fragment;

import com.wiatec.btv_launcher.presenter.BasePresenter;

/**
 * Created by PX on 2016-11-14.
 */

public abstract class BaseFragment<V ,T extends BasePresenter<V>>  extends Fragment {
    protected T presenter;
    @Override
    public void onStart() {
        super.onStart();
        presenter = createPresenter();
        presenter.attachView((V)this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    protected abstract T createPresenter ();
}
