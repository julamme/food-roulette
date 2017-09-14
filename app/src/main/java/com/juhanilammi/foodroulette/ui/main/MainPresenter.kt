package com.juhanilammi.foodroulette.ui.main

import com.juhanilammi.foodroulette.ui.base.BaseActivity
import com.juhanilammi.foodroulette.ui.base.BasePresenter
import com.juhanilammi.foodroulette.ui.base.MvpPresenter
import com.juhanilammi.foodroulette.ui.base.MvpView

/**
 * Created by Laemmi on 27.7.2017.
 */
class MainPresenter(view : MainView): BasePresenter<MainView>() {
    val mMpvView = view
}