package com.guulpay.payments.send

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.view.Window
import com.guulpay.R
import com.guulpay.customUiComponents.CustomButton
import com.guulpay.customUiComponents.QuicksandMediumTextview
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Utils
import com.guulpay.payments.send.enterAmount.EnterAmountSendMoneyFragment
import kotlinx.android.synthetic.main.via_phone_qr_fragment.*

class ViaPhoneQrFragment:BaseFragment(), View.OnClickListener {

    var isViaPhQrInfo = true

    companion object {
        const val CLASS_NAME = "ViaPhoneQrFragment"
        fun newInstance(): Fragment {
            return ViaPhoneQrFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.via_phone_qr_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
    }

    override fun initListeners() {
        imgvwViaIntMoneyTransfer.setOnClickListener(this)
        imgvwViaPhQrInfo.setOnClickListener(this)
        tvViaIntMoneyTransferSend.setOnClickListener(this)
        tvViaPhQrSend.setOnClickListener(this)
        radioBtViaIntrnlMoneyTransfer.setOnClickListener(this)
        radioBtViaPhQr.setOnClickListener(this)
        btNextSendMoney.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == radioBtViaIntrnlMoneyTransfer){
            radioBtViaIntrnlMoneyTransfer.isChecked = true
            radioBtViaPhQr.isChecked = false
        }
        else if (v == radioBtViaPhQr){
            radioBtViaIntrnlMoneyTransfer.isChecked = false
            radioBtViaPhQr.isChecked = true
        }
        else if (v == btNextSendMoney){
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, EnterAmountSendMoneyFragment.newInstance(),
                    R.id.flPaymentContainer, false, EnterAmountSendMoneyFragment.CLASS_NAME)
        }
        else if (v == imgvwViaIntMoneyTransfer){
            isViaPhQrInfo = false
            showInfoDialog()
        }
        else if (v == imgvwViaPhQrInfo){
            isViaPhQrInfo = true
            showInfoDialog()
        }
        else if (v == tvViaPhQrSend){
            radioBtViaPhQr.performClick()
        }
        else if (v == tvViaIntMoneyTransferSend){
            radioBtViaIntrnlMoneyTransfer.performClick()
        }
    }

    private fun showInfoDialog() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.confirm_dialog_sendmoney)
        dialog.setCancelable(false)

        val tvContentDesDialog = dialog.findViewById(R.id.tvContentDesDialog) as QuicksandMediumTextview
        val btOkDialog = dialog.findViewById(R.id.btOkDialog) as CustomButton
        if (isViaPhQrInfo) tvContentDesDialog.text = activity?.getString(R.string.sendMoneyViaPhoneQr)
        else tvContentDesDialog.text = activity?.getString(R.string.sendMoneyViaIntMoneyTransfer)

        btOkDialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
}