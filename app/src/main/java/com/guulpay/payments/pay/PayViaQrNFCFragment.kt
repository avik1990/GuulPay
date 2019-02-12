package com.guulpay.payments.pay

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import com.guulpay.R
import com.guulpay.activity.NFCReaderActivity
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.Constants
import com.guulpay.others.Utils
import com.guulpay.payments.scanQrCode.ScanQrFragment
import kotlinx.android.synthetic.main.pay_via_qr_nfc_fragment.*

class PayViaQrNFCFragment : BaseFragment(), View.OnClickListener {

    var isViaNfcChecked = true
    private var mNfcAdapter: NfcAdapter? = null

    companion object {
        const val CLASS_NAME = "PayViaQrNFCFragment"
        fun newInstance(): Fragment {
            return PayViaQrNFCFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.pay_via_qr_nfc_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

    }

    override fun initListeners() {
        tvNfcPay.setOnClickListener(this)
        tvScanQrPay.setOnClickListener(this)
        radioBtViaPhQr.setOnClickListener(this)
        radioBtViaNFC.setOnClickListener(this)
        btNextPayMoney.setOnClickListener(this)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(activity)
    }

    override fun onClick(v: View?) {
        if (v == btNextPayMoney) {
            if (isViaNfcChecked) {
                if (mNfcAdapter == null) {
                    Toast.makeText(activity, getString(R.string.nfc_notavailable), Toast.LENGTH_LONG).show()
                    /*Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, NFCFragment.newInstance(Constants.Values.TO_PAY_MONEY),
                            R.id.flPaymentContainer, false, NFCFragment.CLASS_NAME)*/
                    return
                } else {
                    /*  Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, NFCFragment.newInstance(Constants.Values.TO_PAY_MONEY),
                              R.id.flPaymentContainer, false, NFCFragment.CLASS_NAME)*/
                    val i = Intent(activity, NFCReaderActivity::class.java)
                    startActivity(i)
                }
            } else {
                Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, ScanQrFragment.newInstance(Constants.Values.TO_PAY_MONEY),
                        R.id.flPaymentContainer, false, ScanQrFragment.CLASS_NAME)
            }
        } else if (v == radioBtViaPhQr) {
            isViaNfcChecked = false
            radioBtViaPhQr.isChecked = true
            radioBtViaNFC.isChecked = false
        } else if (v == radioBtViaNFC) {
            isViaNfcChecked = true
            radioBtViaPhQr.isChecked = false
            radioBtViaNFC.isChecked = true
        } else if (v == tvNfcPay) {
            radioBtViaNFC.performClick()
        } else if (v == tvScanQrPay) {
            radioBtViaPhQr.performClick()
        }
    }
}