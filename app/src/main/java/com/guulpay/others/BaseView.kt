package com.guulpay.others

interface BaseView<T> {
    fun globalLogout()
    fun goToNextPage()
    fun isFragmentAlive():Boolean
    fun isActivityRunning(): Boolean
    fun setPresenter(presenter: T)
    fun isNetworkAvailable():Boolean
    fun showNetworkUnavailableMsg()
    fun showSomeErrorOccurredMsg(msg:String)
}