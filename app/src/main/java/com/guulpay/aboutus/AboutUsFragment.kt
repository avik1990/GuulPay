package com.guulpay.aboutus

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.aboutus.servicecall.AboutUsRepositoryProvider
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.about_us_fragment_layout.*

class AboutUsFragment : BaseFragment(), View.OnClickListener, AboutUSContract.View {


    lateinit var aboutUsPresenter: AboutUsPresenter

    companion object {
        const val CLASS_NAME = "AboutUsFragment"
        fun newInstance(): Fragment {
            return AboutUsFragment()
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.about_us_fragment_layout
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        aboutUsPresenter = AboutUsPresenter(this, AboutUsRepositoryProvider.getAboutusContent())

    }

    override fun initListeners() {
        webview.getSettings().javaScriptEnabled = true
        webview.isHorizontalScrollBarEnabled = false
    }

    override fun onClick(v: View?) {
    }

    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }

    override fun goToNextPage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as DashboardHomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: AboutUSContract.Presenter) {
        aboutUsPresenter = presenter as AboutUsPresenter
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llAboutUs, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llAboutUs, msg, 3000)
    }

    override fun setAboutusdata(htmlcontent: String) {
        webview.loadData(htmlcontent, "text/html; charset=utf-8", "UTF-8");
    }

    override fun onResume() {
        super.onResume()
        aboutUsPresenter.callAboutUs()
    }


    override fun onPause() {
        super.onPause()
        webview.onPause()
    }

    override fun globalLogout() {
        (activity as DashboardHomeActivity).globalLogout()
    }


}