package com.juhanilammi.foodroulette.ui.base

open class BasePresenter<V : MvpView> : MvpPresenter<V> {
    protected var mView: V? = null

    override fun attachView(view: V) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }


}