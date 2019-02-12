package com.guulpay.nfcreader

import android.content.Context
import android.os.Bundle
import com.guulpay.others.BasePresenter
import com.guulpay.others.BaseView

interface NFCContract {

    interface View: BaseView<Presenter> {
        //fun goToSendFragment()
        //fun goToEnterAmtPayFragment()
        fun getArgument(): Bundle?
        fun getContext() : Context
        fun handleProgressAlert(showingStatus:Boolean) // true --> show, false --> dismiss
    }
    interface Presenter : BasePresenter {
        fun afterScannedNFC()
    }
}