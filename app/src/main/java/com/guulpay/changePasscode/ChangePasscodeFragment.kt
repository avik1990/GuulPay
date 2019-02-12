package com.guulpay.changePasscode

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.Log
import android.view.View
import com.google.gson.GsonBuilder
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.changePasscode.model.ChangePasscodeResponseModel
import com.guulpay.changePasscode.servicecall.ChangePasscodeRepositoryProvider
import com.guulpay.changePasscode.servicecall.ChangepasscodeRepository
import com.guulpay.customUiComponents.PasscodeHandler
import com.guulpay.fragments.BaseFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import com.guulpay.others.encryption.AESCrypt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.change_passcode.*
import kotlinx.android.synthetic.main.enter_passcode_layout.*
import kotlinx.android.synthetic.main.new_passcode_layout.*
import org.json.JSONObject

class ChangePasscodeFragment : BaseFragment(), View.OnClickListener {

    var isPasscodeVisible: Boolean = false
    var isConfirmPasscodeVisible = false
    var ChangepasscodeRepository: ChangepasscodeRepository? = null
    var appData: AppData? = null
    var current_passcode: String = ""
    var new_passcode: String = ""
    private var disposable: Disposable? = null


    private val loader by lazy {
        LoaderDialog(context)
    }

    companion object {
        const val CLASS_NAME = "ChangePasscodeFragment"
        fun newInstance(): Fragment {
            return ChangePasscodeFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.change_passcode
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appData = AppData(context as HomeActivity, Constants.Keys._KeyCryptoPreference)
        etNewPasscode1.isEnabled = false
        etNewPasscode2.isEnabled = false
        etNewPasscode3.isEnabled = false
        etNewPasscode4.isEnabled = false
        btSubmitResetPasscode.isEnabled = false
        btSubmitResetPasscode.alpha = 0.0F
        ChangepasscodeRepository = ChangePasscodeRepositoryProvider.getChangePasscode(appData!!.remember_token)
    }

    override fun initListeners() {
        imgvwShowHidePwd.setOnClickListener(this)
        imgvwShowHideNewPwd.setOnClickListener(this)

        PasscodeHandler(etPasscode1, etPasscode2, etPasscode3, etPasscode4, object : PasscodeHandler.PasscodeListener {
            override fun onResult(status: Boolean) {
                if (status) {
                    etNewPasscode1.isEnabled = true
                    etNewPasscode2.isEnabled = true
                    etNewPasscode3.isEnabled = true
                    etNewPasscode4.isEnabled = true
                    btSubmitResetPasscode.isEnabled = true
                    btSubmitResetPasscode.alpha = 0.0F

                    if (!Utils.isConnectedToNetwork(context)) {
                        Utils.showSnackbar(llChangepasscode, getString(R.string.networkUnavailable), 3000)
                        return
                    }

                    PasscodeHandler(etNewPasscode1, etNewPasscode2, etNewPasscode3, etNewPasscode4, object : PasscodeHandler.PasscodeListener {
                        override fun onResult(status: Boolean) {
                            if (status) {
                                current_passcode = etPasscode1.text.toString() + "" + etPasscode2.text.toString() + "" + etPasscode3.text.toString() + "" + etPasscode4.text.toString()
                                new_passcode = etNewPasscode1.text.toString() + "" + etNewPasscode2.text.toString() + "" + etNewPasscode3.text.toString() + "" + etNewPasscode4.text.toString()
                                loader.show()
                                disposable = ChangepasscodeRepository!!.callChangepasscodeApi(current_passcode, new_passcode, appData!!)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({
                                            loader.hide()
                                            if (!it.response.data.isEmpty()) {
                                                val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                                                Log.d("Json_Stirng", json)
                                                val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                                                if (obj.get("responseCode").toString().equals("201")) {
                                                    Utils.showSnackbar(llChangepasscode, obj.get("responseDetails") as String, 3000)
                                                } else if (obj.get("responseCode").toString().equals("200")) {
                                                    val changePasscodeResponseModel = GsonBuilder().create().fromJson(json, ChangePasscodeResponseModel::class.java)
                                                    try {
                                                        Utils.showToast(context, changePasscodeResponseModel.responseDetails!!)
                                                    } catch (e: Exception) {
                                                    }
                                                    activity?.onBackPressed()
                                                } else if (obj.get("responseCode").toString().equals("401")) {
                                                    //view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                                                    Utils.showSnackbar(llChangepasscode, obj.get("responseDetails") as String, 3000)
                                                } else if (obj.get("responseCode").toString().equals("701")) {
                                                    //view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                                                    appData!!.cleardata(context!!, Constants.Keys._KeyCryptoPreference)

                                                }
                                            }
                                        }, {
                                            if ((activity as HomeActivity).isActivityVisible) {
                                                loader.hide()
                                                Utils.showSnackbar(llChangepasscode, "Something went wrong", 3000)
                                            }
                                        })
                                // Utils.showToast(context, "Hello")
                            }
                        }

                    })
                } else {
                    /*etConfirmPasscode1.isEnabled = false
                    etConfirmPasscode2.isEnabled = false
                    etConfirmPasscode3.isEnabled = false
                    etConfirmPasscode4.isEnabled = false
                    btSubmitResetPasscode.isEnabled = false
                    btSubmitResetPasscode.alpha = 0.5F*/
                }
            }

        })
    }

    override fun onClick(v: View?) {
        if (v == imgvwShowHidePwd) {
            if (isPasscodeVisible) {
                etPasscode1.transformationMethod = PasswordTransformationMethod()
                etPasscode2.transformationMethod = PasswordTransformationMethod()
                etPasscode3.transformationMethod = PasswordTransformationMethod()
                etPasscode4.transformationMethod = PasswordTransformationMethod()
                imgvwShowHidePwd.background = activity?.getDrawable(R.drawable.hide_icon)
            } else {
                etPasscode1.transformationMethod = null
                etPasscode2.transformationMethod = null
                etPasscode3.transformationMethod = null
                etPasscode4.transformationMethod = null
                imgvwShowHidePwd.background = activity?.getDrawable(R.drawable.view_icon)
            }
            isPasscodeVisible = !isPasscodeVisible
        } else if (v == imgvwShowHideNewPwd) {
            if (isConfirmPasscodeVisible) {
                etNewPasscode1.transformationMethod = PasswordTransformationMethod()
                etNewPasscode2.transformationMethod = PasswordTransformationMethod()
                etNewPasscode3.transformationMethod = PasswordTransformationMethod()
                etNewPasscode4.transformationMethod = PasswordTransformationMethod()
                imgvwShowHidePwd.background = activity?.getDrawable(R.drawable.hide_icon)
            } else {
                etNewPasscode1.transformationMethod = null
                etNewPasscode2.transformationMethod = null
                etNewPasscode3.transformationMethod = null
                etNewPasscode4.transformationMethod = null
                imgvwShowHidePwd.background = activity?.getDrawable(R.drawable.view_icon)
            }
            isConfirmPasscodeVisible = !isConfirmPasscodeVisible
        }
    }

}