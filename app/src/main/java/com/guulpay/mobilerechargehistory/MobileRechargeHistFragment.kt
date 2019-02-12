package com.guulpay.mobilerechargehistory

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.activity.HomeActivity
import com.guulpay.customUiComponents.EndlessRecyclerViewScrollListener
import com.guulpay.fragments.BaseFragment
import com.guulpay.mobilerecharge.MobileRechargeContract
import com.guulpay.mobilerecharge.MobileRechargeFragment
import com.guulpay.mobilerecharge.model.TransactionHistResponse
import com.guulpay.mobilerecharge.servicecall.MobileRechargeRepositoryProvider
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.recharge_history_fragment_layout.*


class MobileRechargeHistFragment : BaseFragment(), View.OnClickListener, MobileRechargeContract.TransactionContract.View, MobileHistRechargeAdapter.oNRechargeListClick {
    private var rechargelist: MutableList<TransactionHistResponse.Rechargelist> = ArrayList()

    lateinit var MobileRechargeAdapter: MobileHistRechargeAdapter
    var mobileRechargePresenter: TransactionPresenter? = null
    var appData: AppData? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    var mLayoutManager: LinearLayoutManager? = null

    companion object {
        const val CLASS_NAME = "MobileRechargeHistFragment"
        fun newInstance(fromWhichFragment: String): Fragment {
            val bundle = Bundle()
            bundle.putString(Constants.Keys.KEY_FRAGMENT_NAME, fromWhichFragment)
            val fragment = MobileRechargeHistFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.recharge_history_fragment_layout
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appData = AppData(context!!, Constants.Keys._KeyCryptoPreference)
        TransactionPresenter(this, MobileRechargeRepositoryProvider.getRechargeRepository(appData!!.remember_token), appData!!).start()
        recyclerVwSavedBankList.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerVwSavedBankList.layoutManager = mLayoutManager
        recyclerVwSavedBankList.itemAnimator = DefaultItemAnimator()
        callPagination()
       // mobileRechargePresenter!!.callMobilerechargelistApi(appData!!, "scroll")
        MobileRechargeAdapter = MobileHistRechargeAdapter(context!!, this)
        recyclerVwSavedBankList.adapter = MobileRechargeAdapter


    }

    override fun initListeners() {
    }

    override fun onClick(v: View?) {
        /* if (v == imgvwAddBank){
             (activity as HomeActivity).setUpScreenUiForFragment(AddBankAccountFragment.CLASS_NAME)
             Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, AddBankAccountFragment.newInstance(CLASS_NAME),
                     R.id.flFragmentContainerHome, true, AddBankAccountFragment.CLASS_NAME)
         }*/
    }

    override fun onMobilerechargelistFetched(list: MutableList<TransactionHistResponse.Rechargelist>) {
        rechargelist = list
        if (list.isNotEmpty()) {
            MobileRechargeAdapter.setData(list)
        }
    }

    override fun callPagination() {
        scrollListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                mobileRechargePresenter!!.callMobilerechargelistApi(appData!!, "scroll")
                Log.d("BeforePageNo===", "sdsfdsfsd")
            }
        }
        recyclerVwSavedBankList.addOnScrollListener(scrollListener)
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
        return (activity as HomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: MobileRechargeContract.TransactionContract.Presenter) {
        mobileRechargePresenter = presenter as TransactionPresenter
        mobileRechargePresenter!!.callMobilerechargelistApi(appData!!, "nonscroll")
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(lnTranshist, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(lnTranshist, msg, 3000)
    }

    override fun gettransationDetails(pos: Int) {
        val intent = Intent(context, MobileRechargeFragment::class.java)
        intent.putExtra("phoneno", rechargelist[pos].mobileRechargePhoneNumber!!)
        intent.putExtra("callprefix", rechargelist[pos].mobileRechargeCallPrifix!!)
        intent.putExtra("rechargeamt", rechargelist[pos].mobileRechargeAmount!!)

        setTargetFragment(MobileRechargeFragment.newInstance(), Constants.RequestCodes.REQUEST_RECHARGEREPEAT)
        targetFragment!!.onActivityResult(targetRequestCode, RESULT_OK, intent)
        activity?.supportFragmentManager!!.popBackStack()
/*
        Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, MobileRechargeHistFragment.newInstance(MobileRechargeFragment.CLASS_NAME),
                R.id.flFragmentContainerHome, true, MobileRechargeHistFragment.CLASS_NAME)
*/
    }

    override fun globalLogout() {
        appData!!.cleardata(activity!!, Constants.Keys._KeyCryptoPreference)
    }
}