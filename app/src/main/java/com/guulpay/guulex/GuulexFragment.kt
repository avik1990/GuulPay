package com.guulpay.guulex

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.fragments.BaseFragment
import com.guulpay.guulex.adapter.GuulexListAdapter
import com.guulpay.guulex.model.ForexResponse
import com.guulpay.guulex.servicecall.ForexTransactionRepositoryProvider
import com.guulpay.guulexgraph.GuulexGraphFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.guulex_fragment_layout.*

class GuulexFragment : BaseFragment(), View.OnClickListener, GuulexContract.View {

    lateinit var guulexListAdapter: GuulexListAdapter
    lateinit var GuulexPresenter: GuulexPresenter
    var appData: AppData? = null
    var isTyping = false

    companion object {
        const val CLASS_NAME = "GuulexFragment"
        fun newInstance(): Fragment {
            return GuulexFragment()
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.guulex_fragment_layout
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appData = AppData(context as HomeActivity, Constants.Keys._KeyCryptoPreference)
        GuulexPresenter = GuulexPresenter(this, ForexTransactionRepositoryProvider.getForexTransactionRepository(appData!!.remember_token), appData!!)
        GuulexPresenter.getForexData()
    }

    override fun inflateData(list: ArrayList<ForexResponse.Forexlist>) {
        guulexListAdapter = GuulexListAdapter(context!!, list)
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerViewGullex.layoutManager = mLayoutManager
        recyclerViewGullex.itemAnimator = DefaultItemAnimator()
        recyclerViewGullex.adapter = guulexListAdapter
    }

    override fun initListeners() {
        fabGraph.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == fabGraph) {
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, GuulexGraphFragment.newInstance(),
                    R.id.flFragmentContainerHome, true, GuulexGraphFragment.CLASS_NAME)
        }
    }

    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }

    override fun goToNextPage() {
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as HomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: GuulexContract.Presenter) {
        GuulexPresenter = presenter as GuulexPresenter
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llGullexParent, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llGullexParent, msg, 3000)
    }

    override fun fieldsValidationFailed(msg: String) {
        if (!isTyping)
            Utils.showSnackbar(llGullexParent, msg, 3000)
    }

    override fun globalLogout() {
        appData!!.cleardata(activity!!, Constants.Keys._KeyCryptoPreference)
    }
}