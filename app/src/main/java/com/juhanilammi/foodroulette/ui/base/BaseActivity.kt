package com.juhanilammi.foodroulette.ui.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Laemmi on 27.7.2017.
 */
public abstract class BaseActivity<in V : MvpView, T : MvpPresenter<V>>: AppCompatActivity(), BaseView {

    protected abstract var mPresenter: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}