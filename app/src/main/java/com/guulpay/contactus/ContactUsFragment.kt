package com.guulpay.contactus

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.contactus.servicecall.ContactUsRepositoryProvider
import com.guulpay.customUiComponents.CustomTextWatcher
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.contact_us_fragment_layout.*

class ContactUsFragment : BaseFragment(), View.OnClickListener, ContactUSContract.View {


    lateinit var contactUsPresenter: ContactUsPresenter
    var appData: AppData? = null
    var isTyping = false

    companion object {
        const val CLASS_NAME = "ContactUsFragment"
        fun newInstance(): Fragment {
            return ContactUsFragment()
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.contact_us_fragment_layout
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appData = AppData(context as DashboardHomeActivity, Constants.Keys._KeyCryptoPreference)
        contactUsPresenter = ContactUsPresenter(this, ContactUsRepositoryProvider.getAboutusContent(), appData!!)
        btSubmit.setOnClickListener(this)

        etName.setText(appData!!.register_name)
        etEmail.setText(appData!!.register_email)
        etMobile.setText(appData!!.register_phone)
    }

    override fun initListeners() {

        CustomTextWatcher(etName, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                contactUsPresenter.checkFieldsValidation(etName.text.toString(), etEmail.text.toString(), etMessage.text.toString())
            }
        })
        CustomTextWatcher(etEmail, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                contactUsPresenter.checkFieldsValidation(etName.text.toString(), etEmail.text.toString(), etMessage.text.toString())
            }
        })
        CustomTextWatcher(etMessage, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                isTyping = true
                contactUsPresenter.checkFieldsValidation(etName.text.toString(), etEmail.text.toString(), etMessage.text.toString())
            }
        })

    }

    override fun onClick(v: View?) {
        if (v == btSubmit) {
            contactUsPresenter.callContactUsAPi(etName.text.toString(), etEmail.text.toString(), etMessage.text.toString())
        }
    }

    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }

    override fun enableButton() {
        btSubmit.isEnabled = true
        btSubmit.alpha = 1.0F
    }

    override fun disableButton() {
        btSubmit.isEnabled = false
        btSubmit.alpha = 0.5F
    }

    override fun goToNextPage() {
        showAlert()
    }

    fun showAlert() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setMessage("Thank you for writing us. We'll get back to you soon")
        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.dismiss()
            activity?.onBackPressed()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as DashboardHomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: ContactUSContract.Presenter) {
        contactUsPresenter = presenter as ContactUsPresenter
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llContactusParent, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llContactusParent, msg, 3000)
    }

    override fun fieldsValidationFailed(msg: String) {
        if (!isTyping)
            Utils.showSnackbar(llContactusParent, msg, 3000)
    }

    override fun globalLogout() {
        appData!!.cleardata(activity!!, Constants.Keys._KeyCryptoPreference)
    }

}