package com.guulpay.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.Log
import android.view.View
import com.google.gson.GsonBuilder
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.activity.HomeActivity
import com.guulpay.contactus.ContactUSContract
import com.guulpay.customUiComponents.EndlessRecyclerViewScrollListener
import com.guulpay.fragments.BaseFragment
import com.guulpay.guulexfinal.servicecall.CurrencyListRepository
import com.guulpay.mobilerechargehistory.MobileHistRechargeAdapter
import com.guulpay.notification.model.NotificationResponse
import com.guulpay.notification.servicecall.NotificationRepository
import com.guulpay.notification.servicecall.NotificationRepositoryProvider
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.myprofile_edit_fragment.*
import kotlinx.android.synthetic.main.notification_fragment.*
import kotlinx.android.synthetic.main.recharge_history_fragment_layout.*
import org.json.JSONObject

class NotificationFragment : BaseFragment(),NotificationContract.View, NotificationAdapter.oNNotificationListClick
{


    //private val currencyListRepository: NotificationRepository
    lateinit var notificationAdapter: NotificationAdapter
    lateinit var notificationRepository: NotificationRepository
    var appData: AppData? = null
    private var disposable: Disposable? = null
    private var notificationlist: MutableList<NotificationResponse.NotificationList> = ArrayList()
    var OFFSET_COUNT = 10
    private var scrollListener:EndlessRecyclerViewScrollListener?= null
    var mLayoutManager: LinearLayoutManager? = null


    companion object {
        const val CLASS_NAME = "NotificationFragment"
        fun newInstance(): Fragment {
            return NotificationFragment()
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.notification_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        Log.v("NotificationCheck","Inside notification Fragment")
        appData = AppData(context as HomeActivity, Constants.Keys._KeyCryptoPreference)
        notificationRepository = NotificationRepositoryProvider.callNotificationApi(appData!!.remember_token)
        recyclerVwNotificationFrag.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerVwNotificationFrag.layoutManager = mLayoutManager
        recyclerVwNotificationFrag.itemAnimator = DefaultItemAnimator()
        callPagination()
        callMobilerechargelistApi(appData!!, "scroll")
//        if(notificationlist.size>0){
//            callPagination()
//        }
        notificationAdapter = NotificationAdapter(context!!, this)
        recyclerVwNotificationFrag.adapter = notificationAdapter

/*

        notificationAdapter = NotificationAdapter(context!!, this)
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerVwNotificationFrag.layoutManager = mLayoutManager
        recyclerVwNotificationFrag.itemAnimator = DefaultItemAnimator()
        recyclerVwNotificationFrag.adapter = notificationAdapter
        notificationRepository.callNotificationData()*/
    }


    override fun gettransationDetails(pos: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initListeners() {

    }




    fun callPagination() {
        scrollListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                callMobilerechargelistApi(appData!!, "scroll")
                Log.d("BeforePageNo===", "sdsfdsfsd")
            }
        }
        recyclerVwNotificationFrag.addOnScrollListener(scrollListener)
    }


    fun callMobilerechargelistApi(appData: AppData, flag: String) {
        if (!isNetworkAvailable()) {
            showNetworkUnavailableMsg()
            return
        }
        handleProgressAlert(true)
        Log.d("ArraySize", "" + notificationlist!!.size)
        disposable = notificationRepository.callNotificationData(notificationlist!!.size, OFFSET_COUNT, appData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    handleProgressAlert(false)
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        Log.v("json_noti",json)
                        if (obj.get("responseCode").toString().equals("201")) {
                            showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        } else if (obj.get("responseCode").toString().equals("200")) {


                            val notificationResponse = GsonBuilder().create().fromJson(json, NotificationResponse::class.java)
                            notificationlist.addAll(notificationResponse.notificationList!!)
                            onNotificationlistFetched(notificationlist!!)
                            Log.d("ArraySizeAfter", "" + notificationlist!!.size)
                        } else if (obj.get("responseCode").toString().equals("401")) {
                            showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                        }
                    }

                }, {
                    if (isActivityRunning()) {
                        handleProgressAlert(false)
                        if (isNetworkAvailable())
                            showSomeErrorOccurredMsg("Something went wrong")
                        else showNetworkUnavailableMsg()
                    }
                })
    }

    private fun onNotificationlistFetched(list: MutableList<NotificationResponse.NotificationList>) {
        notificationlist = list

        Log.v("List_data",notificationlist.size.toString())
        if (list.isNotEmpty()) {
            notificationAdapter.setData(list)
        }

    }


    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }

    override fun fieldsValidationFailed(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disableButton() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enableButton() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun globalLogout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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


    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llProfileedit, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llProfileedit, msg, 3000)
    }

    override fun setPresenter(presenter: NotificationContract.Presenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}