package com.guulpay.payments.scanQrCode

import android.content.Context
import android.os.Bundle
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface ScanQrContract {

    interface View: BaseView<Presenter> {
        fun goToSendFragment()
        fun goToEnterAmtPayFragment()
        fun getArgument(): Bundle?
        fun flashOn()
        fun flashOff()
        fun getContext() : Context
        fun handleProgressAlert(showingStatus:Boolean) // true --> show, false --> dismiss
    }
    interface Presenter : BasePresenter {
        fun afterScannedQr()
        fun handleCameraFlash()
    }
}