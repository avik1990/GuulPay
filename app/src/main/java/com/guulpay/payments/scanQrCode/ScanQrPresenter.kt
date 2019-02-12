package com.guulpay.payments.scanQrCode

import com.guulpay.R
import com.guulpay.others.Constants
import com.guulpay.others.Utils

class ScanQrPresenter(private val view: ScanQrContract.View) : ScanQrContract.Presenter{

    var isFlashOn = false

    override fun handleCameraFlash() {
        if (isFlashOn){
            Utils.makeFlashOff(view.getContext())
            view.flashOff()
        }
        else{
            Utils.makeFlashOn(view.getContext())
            view.flashOn()
        }
        isFlashOn = !isFlashOn
    }

    override fun start() {
        view.setPresenter(this)
    }

    override fun stop() {

    }

    override fun afterScannedQr() {
        val whichFragment = view.getArgument()?.getString(Constants.Keys.KEY_FRAGMENT_NAME)
        if (whichFragment.equals(Constants.Values.TO_PAY_MONEY)){
            view.goToEnterAmtPayFragment()
        }
        else{
            view.goToSendFragment()
        }
    }
}