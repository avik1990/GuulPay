package com.guulpay.payments

import android.content.Intent
import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.guulpay.R
import com.guulpay.activity.BaseActivity
import com.guulpay.customUiComponents.QuicksandRegularTextview
import com.guulpay.login.LoginActivity
import com.guulpay.nfcreader.NFCFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.Utils
import com.guulpay.payments.pay.PayViaQrNFCFragment
import com.guulpay.payments.request.enterAmount.EnterAmountRequestMoneyFragment
import com.guulpay.payments.send.ViaPhoneQrFragment
import kotlinx.android.synthetic.main.common_toolbar_layout.*
import kotlinx.android.synthetic.main.payment_activity.*
import java.util.concurrent.atomic.AtomicBoolean


class PaymentActivity : BaseActivity(), View.OnClickListener, TabLayout.OnTabSelectedListener {
    override fun globalLogout() {
        appData!!.cleardata(applicationContext, Constants.Keys._KeyCryptoPreference)
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
    var appData: AppData? = null

    override fun onBackPressed() {
        Utils.hideKeyboard(this)
        super.onBackPressed()
    }

    override fun onClick(v: View?) {
        if (v == imgvwCommonToolbar) {
            onBackPressed()
        }
    }

    override fun initPermissions() {
        Utils.checkPermissions(this)
    }

    override fun initResources() {
        appData= AppData(this, Constants.Keys._KeyCryptoPreference)
        Utils.hideKeyboardOnOutsideTouch(flPaymentContainer, this)
        tvCommonToolbar.setCustomFont(this, getString(R.string.quicksandMedium), true)
        tvCommonToolbar.text = getString(R.string.payments)
        /* Adding Tabs */
        tabLayoutPayment.addTab(tabLayoutPayment.newTab())
        tabLayoutPayment.addTab(tabLayoutPayment.newTab())
        tabLayoutPayment.addTab(tabLayoutPayment.newTab())

        /* set up ViewPager */
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, tabLayoutPayment.tabCount)
        viewPagerPayment.adapter = viewPagerAdapter
        tabLayoutPayment.setupWithViewPager(viewPagerPayment)
        /* Custom view is inflated to make the text of Tab left-aligned */
        for (i in 0..tabLayoutPayment.tabCount - 1) {
            tabLayoutPayment.getTabAt(i)?.setCustomView(getCustomViewTab(i))
        }

        /* Parse to get tab position */
        parseIntent(intent)
    }

    private fun getCustomViewTab(pos: Int): View {
        val tabInflater = LayoutInflater.from(this).inflate(R.layout.custom_tabitems, null) as LinearLayout
        val tvTabContent = tabInflater.findViewById(R.id.tvTabContent) as QuicksandRegularTextview
        tvTabContent.text = resources.getStringArray(R.array.tabItemsPayments)[pos]
        tvTabContent.setTextColor(Color.BLACK)
        tabLayoutPayment.getTabAt(pos)?.customView = tvTabContent

        return tvTabContent
    }

    override fun initListeners() {
        imgvwCommonToolbar.setOnClickListener(this)
        tabLayoutPayment.addOnTabSelectedListener(this)
    }

    override fun getLayout(): Int {
        return R.layout.payment_activity
    }

    fun parseIntent(intent: Intent?) {
        if (null != intent) {
            if (intent.hasExtra(Constants.Keys.KEY_TAB_POSITION)) {
                var tabPosition = intent.getIntExtra(Constants.Keys.KEY_TAB_POSITION, 0)

                when (tabPosition) {
                    0 -> {
                        tabLayoutPayment.getTabAt(tabPosition)?.select()
                        //   viewPagerPayment.currentItem = tabPosition
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, ViaPhoneQrFragment.newInstance(),
                                R.id.flPaymentContainer, false, ViaPhoneQrFragment.CLASS_NAME)
                    }
                    1 -> {
                        tabLayoutPayment.getTabAt(tabPosition)?.select()
                        //   viewPagerPayment.currentItem = tabPosition
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, EnterAmountRequestMoneyFragment.newInstance(),
                                R.id.flPaymentContainer, false, EnterAmountRequestMoneyFragment.CLASS_NAME)
                    }
                    2 -> {
                        tabLayoutPayment.getTabAt(tabPosition)?.select()
                        //   viewPagerPayment.currentItem = tabPosition
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, PayViaQrNFCFragment.newInstance(),
                                R.id.flPaymentContainer, false, PayViaQrNFCFragment.CLASS_NAME)
                    }
                }
            }
        }
    }

    /* TabLayout.OnTabSelectedListener */
    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        //   viewPagerPayment.currentItem = tab?.position!!



        if (tab?.position == 0) {
            Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, ViaPhoneQrFragment.newInstance(),
                    R.id.flPaymentContainer, false, ViaPhoneQrFragment.CLASS_NAME)
        } else if (tab?.position == 1) {
            Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, EnterAmountRequestMoneyFragment.newInstance(),
                    R.id.flPaymentContainer, false, EnterAmountRequestMoneyFragment.CLASS_NAME)
        } else if (tab?.position == 2) {
            Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, PayViaQrNFCFragment.newInstance(),
                    R.id.flPaymentContainer, false, PayViaQrNFCFragment.CLASS_NAME)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (supportFragmentManager.findFragmentById(R.id.flPaymentContainer) is NFCFragment) {
            val my = getNFCFragment() as NFCFragment
            // Pass intent or its data to the fragment's method
            if (intent != null) {
                //my.resolveIntent(intent)
                setIntent(intent)
                my.resolveIntent(intent)
            }
        }
    }

    fun getNFCFragment(): Fragment {
        return supportFragmentManager.findFragmentById(R.id.flPaymentContainer)
    }
}