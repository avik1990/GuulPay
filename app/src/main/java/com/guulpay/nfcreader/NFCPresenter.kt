package com.guulpay.nfcreader

import com.guulpay.others.Constants

class NFCPresenter(private val view: NFCContract.View) : NFCContract.Presenter{

    var isFlashOn = false

    override fun start() {
        view.setPresenter(this)
    }

    override fun stop() {

    }

    override fun afterScannedNFC() {
        val whichFragment = view.getArgument()?.getString(Constants.Keys.KEY_FRAGMENT_NAME)
        if (whichFragment.equals(Constants.Values.TO_PAY_MONEY)){
           // view.goToEnterAmtPayFragment()
        }
        else{
           // view.goToSendFragment()
        }
    }
}