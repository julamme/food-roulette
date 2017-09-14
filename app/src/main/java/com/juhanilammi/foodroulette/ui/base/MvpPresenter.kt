package com.juhanilammi.foodroulette.ui.base

/**
 * Created by Laemmi on 27.7.2017.
 */
interface MvpPresenter<in V: MvpView> {

    fun attachView(view: V)

    fun detachView()
}