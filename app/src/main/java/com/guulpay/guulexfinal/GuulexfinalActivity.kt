package com.guulpay.guulexfinal

import android.content.Intent
import android.graphics.Color
import android.support.design.widget.TabLayout

import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.guulpay.R
import com.guulpay.activity.BaseActivity
import com.guulpay.customUiComponents.QuicksandRegularTextview
import com.guulpay.guulexfinal.GuulexFinalFragments.GuulexFinalBuyFragment
import com.guulpay.guulexfinal.GuulexFinalFragments.GuulexFinalSellFragment
import com.guulpay.nfcreader.NFCFragment
import com.guulpay.others.Constants
import com.guulpay.others.Utils

import kotlinx.android.synthetic.main.activity_guulexfinal.*
import kotlinx.android.synthetic.main.common_toolbar_layout.*


class GuulexfinalActivity  : BaseActivity(), View.OnClickListener, TabLayout.OnTabSelectedListener {

    override fun onBackPressed() {
        Utils.hideKeyboard(this)
        super.onBackPressed()
    }


    override fun initResources() {
        Utils.hideKeyboardOnOutsideTouch(framelayout_guulex_bottom_tab_area, this)
        tvCommonToolbar.setCustomFont(this, getString(R.string.quicksandMedium), true)
        tvCommonToolbar.text = getString(R.string.title_guulex_activity_final)

        //if (savedInstanceState == null) {
            tabs_guulex_final_activity.addTab(tabs_guulex_final_activity.newTab())
            tabs_guulex_final_activity.addTab(tabs_guulex_final_activity.newTab())
      //  }
        val viewPagerGuulexFinalAdapter = ViewPagerGuulexFinalAdapter(supportFragmentManager, tabs_guulex_final_activity.tabCount)
        viewPagerTabsGuulexFinal.adapter = viewPagerGuulexFinalAdapter
        tabs_guulex_final_activity.setupWithViewPager(viewPagerTabsGuulexFinal)


        /* Custom view is inflated to make the text of Tab left-aligned */
        for (i in 0..tabs_guulex_final_activity.tabCount - 1) {
            tabs_guulex_final_activity.getTabAt(i)?.setCustomView(getCustomViewTab(i))
        }

        /* Parse to get tab position */
        parseIntent(intent)

    }

    private fun getCustomViewTab(pos: Int): View {
        val tabInflater = LayoutInflater.from(this).inflate(R.layout.custom_tabitems, null) as LinearLayout
        val tvTabContent = tabInflater.findViewById(R.id.tvTabContent) as QuicksandRegularTextview
        tvTabContent.text = resources.getStringArray(R.array.tabItemsGuulexFinal)[pos]
        tvTabContent.setTextColor(Color.BLACK)
        tabs_guulex_final_activity.getTabAt(pos)?.setCustomView(tvTabContent)

        return tvTabContent
    }


    fun parseIntent(intent: Intent?) {
        if (null != intent) {
            if (intent.hasExtra(Constants.Keys.KEY_TAB_POSITION)) {
                var tabPosition = intent.getIntExtra(Constants.Keys.KEY_TAB_POSITION, 0)

                when (tabPosition) {
                    0 -> {
                        tabs_guulex_final_activity.getTabAt(tabPosition)?.select()
                        //   viewPagerPayment.currentItem = tabPosition
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, GuulexFinalBuyFragment.newInstance(),
                                R.id.framelayout_guulex_bottom_tab_area, false, GuulexFinalBuyFragment.CLASS_NAME)
                    }
                    1 -> {
                        tabs_guulex_final_activity.getTabAt(tabPosition)?.select()
                        //   viewPagerPayment.currentItem = tabPosition
                        Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, GuulexFinalSellFragment.newInstance(),
                                R.id.framelayout_guulex_bottom_tab_area, false, GuulexFinalSellFragment.CLASS_NAME)
                    }

                }
            }
        }
    }



    override fun initListeners() {
        imgvwCommonToolbar.setOnClickListener(this)
        tabs_guulex_final_activity.addOnTabSelectedListener(this)

    }

    override fun getLayout(): Int {
       return R.layout.activity_guulexfinal
    }

    //Checking Permissions
    override fun initPermissions() {
        Utils.checkPermissions(this)

    }

    override fun onClick(v: View?) {
        if (v == imgvwCommonToolbar) {
            onBackPressed()
        }

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab?.position == 0) {
            Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, GuulexFinalBuyFragment.newInstance(),
                    R.id.framelayout_guulex_bottom_tab_area, false, GuulexFinalBuyFragment.CLASS_NAME)

        } else if (tab?.position == 1) {
            Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, GuulexFinalSellFragment.newInstance(),
                    R.id.framelayout_guulex_bottom_tab_area, false, GuulexFinalSellFragment.CLASS_NAME)
        }
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (supportFragmentManager.findFragmentById(R.id.framelayout_guulex_bottom_tab_area) is GuulexFinalBuyFragment) {
          //  val my = getNFCFragment() as NFCFragment
            // Pass intent or its data to the fragment's method
            if (intent != null) {
                //my.resolveIntent(intent)
                setIntent(intent)
               // my.resolveIntent(intent)
            }
        }
    }


    override fun globalLogout() {

    }

}
