package com.guulpay.kycverification

import io.reactivex.disposables.Disposable


class KycPresenter(private val view: KycVerficationContract.View) : KycVerficationContract.Presenter {

    private var disposable: Disposable? = null
    val TAG = "KycPresenter"


    override fun start() {
        view.setPresenter(this)
    }

    override fun stop() {
        disposable?.dispose()
        view.handleProgressAlert(false)
    }

}