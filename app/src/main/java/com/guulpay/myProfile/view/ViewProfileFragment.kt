package com.guulpay.myProfile.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.View
import com.google.gson.GsonBuilder
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.customUiComponents.CommonDialog
import com.guulpay.customUiComponents.CustomDialogEmailVerify
import com.guulpay.fragments.BaseFragment
import com.guulpay.kycverification.KYCActivity
import com.guulpay.kycverification.model.KYCUrlGeneratorResponse
import com.guulpay.login.LoginActivity
import com.guulpay.myProfile.edit.EditProfileFragment
import com.guulpay.myProfile.model.KycVerificationResponse
import com.guulpay.myProfile.model.UserDetailsResponse
import com.guulpay.myProfile.servicecall.MyProfileRepository
import com.guulpay.myProfile.servicecall.MyProfileRepositoryProvider
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import com.guulpay.others.encryption.AESCrypt
import com.guulpay.others.encryption.ObjectToJSonConvertor
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.myprofile_edit_fragment.*
import kotlinx.android.synthetic.main.myprofile_view_fragment.*
import org.json.JSONObject
import java.net.URLDecoder

class ViewProfileFragment : BaseFragment(), ViewProfileContract.View, View.OnClickListener, CustomDialogEmailVerify.CommonDialogClickEmailListener,CommonDialog.CommonDialogClickListener {


    var appData: AppData? = null
    var viewProfilePresenter: ViewProfilePresenter? = null

    //For KYC
    var myProfileRepository1: MyProfileRepository? = null
    private var disposable1: Disposable? = null
    var isBank_: Boolean = false
    var isPassport_: Boolean = false
    var isDriving_: Boolean = false
    var isNational_: Boolean = false
    var saltkey: String? = ""



    companion object {
        const val CLASS_NAME = "ViewProfileFragment"
        fun newInstance(): Fragment {
            return ViewProfileFragment()
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.myprofile_view_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

        appData = AppData(activity as HomeActivity, Constants.Keys._KeyCryptoPreference)
        viewProfilePresenter = ViewProfilePresenter(this, appData!!, MyProfileRepositoryProvider.getUserDetsils(appData!!.remember_token))
        viewProfilePresenter?.callUserDetailsApi(appData!!)
        myProfileRepository1 = MyProfileRepositoryProvider.getUserDetsils(appData!!.remember_token)
        // viewProfilePresenter!!.showUserDetails()

        tvPhoneMyProfile.text="+"+appData!!.country_call_prifix+" "+appData!!.register_phone
    }

    override fun initListeners() {
        imgvwVerifyStateEmail.setOnClickListener(this)
        imgvwEditProfile.setOnClickListener(this)
        llKycVerify.setOnClickListener(this)
        imgvwVerifykyc.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == imgvwEditProfile) {
            Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, EditProfileFragment.newInstance(),
                    R.id.flFragmentContainerHome, true, EditProfileFragment.CLASS_NAME)
        } else if (v == imgvwVerifyStateEmail) {
            CustomDialogEmailVerify.getInstance("Email Verification!!", "Do you want to verify your Email?", "Cancel", "Ok", this).show(activity?.fragmentManager, "")
        }
        //KYC Verification Code
        else if(v == llKycVerify || v == imgvwVerifykyc){
            if (!isNetworkAvailable()) {
                showNetworkUnavailableMsg()
                return
            }
            checKYCMethod()
        }

    }

    //Check KYC Method Edit by Rishabh 05/02/2019

    private fun checKYCMethod() {
        loader.show()
        disposable1 = myProfileRepository1!!.callKYCMethodinViewProfile(appData!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loader.hide()
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("Json_Stirng_Kyc", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            Utils.showSnackbar(llProfileedit, obj.get("responseDetails") as String, 3000)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val kycVerificationResponse = GsonBuilder().create().fromJson(json, KycVerificationResponse::class.java)
                            Log.v("kyc_response_method",kycVerificationResponse.toString())
                             Log.d("RequestString_kyc", ObjectToJSonConvertor.getRequestJson(kycVerificationResponse))
                            if (kycVerificationResponse.methods!!.size > 0) {
                                //  passport","bank","national_id
                                if (kycVerificationResponse.methods.toString().contains("passport")) {
                                    isPassport_ = true
                                }
                                if (kycVerificationResponse.methods.toString().contains("bank")) {
                                    isBank_ = true
                                }
                                if (kycVerificationResponse.methods.toString().contains("national_id")) {
                                    isNational_ = true
                                }
                                if (kycVerificationResponse.methods.toString().contains("drivers_license")) {
                                    isDriving_ = true
                                }

                                CommonDialog.getInstance("Choose a Verification Method", "", "Cancel", "Ok", false,this, isBank = isBank_, isDriving = isDriving_, isNational = isNational_, isPassport = isPassport_).show(activity?.fragmentManager, "")
                            }
                        } else if (obj.get("responseCode").toString().equals("401")) {
                            Utils.showSnackbar(llProfileedit, obj.get("responseDetails") as String, 3000)
                        } else if (obj.get("responseCode").toString().equals("701")) {
                            globalLogout()
                        }
                    }
                }, {
                    if ((activity as HomeActivity).isActivityVisible) {
                        loader.hide()
                        Utils.showSnackbar(llProfileedit, "Something went wrong", 3000)
                    }
                })
    }



    override fun onRightClick(pos: Int) {
        var randomkey = Utils.generateRandom16bitKey()
        saltkey = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        Log.v("position",pos.toString())
        when (pos) {
            0 -> {
                /*encryptedstring = URLEncoder.encode(AESCrypt.encrypt(appData!!.user_id + "&tupas", randomkey))
                loadurl = Constants.Services.BASE_URL_BACKEND + "kyc-verification?salt=$saltkey&data=$encryptedstring"*/
                getSignigetUrl("0", "tupas")
                /*Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, KycFragment.newInstance(loadurl!!),
                        R.id.flFragmentContainerHome, true, KycFragment.CLASS_NAME)*/
                //  Utils.openUrl(loadurl!!, context!!)

            }
            1 -> {
                /* encryptedstring = URLEncoder.encode(AESCrypt.encrypt(appData!!.user_id + "&scid-passport-selfie", randomkey))
                 loadurl = Constants.Services.BASE_URL_BACKEND + "kyc-verification?salt=$saltkey&data=$encryptedstring"*/
                /* Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, KycFragment.newInstance(loadurl!!),
                         R.id.flFragmentContainerHome, true, KycFragment.CLASS_NAME)*/
                // Utils.openUrl(loadurl!!, context!!)
                getSignigetUrl("1", "scid-passport-selfie")


            }
            2 -> {
                /* encryptedstring = URLEncoder.encode(AESCrypt.encrypt(appData!!.user_id + "&scid-dl-selfie", randomkey), "utf-8")
                 loadurl = Constants.Services.BASE_URL_BACKEND + "kyc-verification?salt=$saltkey&data=$encryptedstring"*/
                /*Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, KycFragment.newInstance(loadurl!!),
                        R.id.flFragmentContainerHome, true, KycFragment.CLASS_NAME)*/

                getSignigetUrl("2", "scid-dl-selfie")

                //Utils.openUrl(loadurl!!, context!!)
            }
            3 -> {
                /* encryptedstring = URLEncoder.encode(AESCrypt.encrypt(appData!!.user_id + "&scid-ic-selfie", randomkey))
                 loadurl = Constants.Services.BASE_URL_BACKEND + "kyc-verification?salt=$saltkey&data=$encryptedstring"*/
                /*Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, KycFragment.newInstance(loadurl!!),
                        R.id.flFragmentContainerHome, true, KycFragment.CLASS_NAME)*/
                getSignigetUrl("3", "scid-ic-selfie")

                //Utils.openUrl(loadurl!!, context!!)
            }
        }
    }


    fun getSignigetUrl(index: String, signicate_method: String) {
        loader.show()
        disposable1 = myProfileRepository1!!.callGenerateUrlApi(signicate_method, appData!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loader.hide()
                    if (!it.response.data.isEmpty()) {
                        val json = AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data)
                        Log.d("Json_Stirng_v_profile", json)
                        val obj = JSONObject(AESCrypt.decrypt(Base64.decode(it.response.salt, Base64.NO_WRAP), it.response.data))
                        if (obj.get("responseCode").toString().equals("201")) {
                            Utils.showSnackbar(llProfileedit, obj.get("responseDetails") as String, 3000)
                        } else if (obj.get("responseCode").toString().equals("200")) {
                            val kYCUrlGeneratorResponse = GsonBuilder().create().fromJson(json, KYCUrlGeneratorResponse::class.java)

                            Log.v("redirectUrl",kYCUrlGeneratorResponse.redirectUrl)

                            redirecturl(index, kYCUrlGeneratorResponse.redirectUrl)

                        } else if (obj.get("responseCode").toString().equals("401")) {
                            //view.showSomeErrorOccurredMsg(obj.get("responseDetails") as String)
                            Utils.showSnackbar(llProfileedit, obj.get("responseDetails") as String, 3000)
                        } else if (obj.get("responseCode").toString().equals("701")) {
                            (activity as HomeActivity).globalLogout()
                        }
                    }
                }, {
                    if ((activity as HomeActivity).isActivityVisible) {
                        loader.hide()
                        Utils.showSnackbar(llProfileedit, "Something went wrong", 3000)
                    }
                })
    }

    private fun redirecturl(index: String, redirectUrl: String?) {
        /*when {
            index.equals("0") -> Utils.replaceFragmentInActivityFadeAnimation(activity?.supportFragmentManager, KycFragment.newInstance(URLDecoder.decode(redirectUrl, "UTF-8")!!), R.id.flFragmentContainerHome, true, KycFragment.CLASS_NAME)
            index.equals("1") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
            index.equals("2") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
            index.equals("3") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
        }*/

        when {
            index.equals("0") -> {
                val intent = Intent(context, KYCActivity::class.java)
                Log.v("decodes_Url",URLDecoder.decode(redirectUrl, "UTF-8"))
                intent.putExtra(Constants.Keys.KEY_URL, URLDecoder.decode(redirectUrl, "UTF-8"))
                startActivity(intent)
            }
            index.equals("1") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
            index.equals("2") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
            index.equals("3") -> Utils.openUrl(URLDecoder.decode(redirectUrl, "UTF-8"), context!!)
        }

    }
//***************************************************KYC Verification END*************************************************/



    override fun showUserDetails(userDetailsResponse: UserDetailsResponse.User?) {
        tvNameMyProfileText.text = userDetailsResponse!!.name
       // tvPhoneMyProfile.text = userDetailsResponse!!.phone
        tvEmailMyProfile.text = userDetailsResponse!!.email

        if (userDetailsResponse.isPhoneVerify.toString().isEmpty() || userDetailsResponse.isPhoneVerify.toString().equals("0")) {
            imgvwVerifyStatePhone.background = ContextCompat.getDrawable(activity as HomeActivity, R.drawable.ic_not_verified)
            tvisPhoneverified.text = "Not Verified"
        } else {
            imgvwVerifyStatePhone.background = ContextCompat.getDrawable(activity as HomeActivity, R.drawable.ic_verified_icon)
            tvisPhoneverified.text = "Verified"
        }

        if (userDetailsResponse.isEmailVerify.toString().isEmpty() || userDetailsResponse.isEmailVerify.toString().equals("0")) {
            imgvwVerifyStateEmail.background = ContextCompat.getDrawable(activity as HomeActivity, R.drawable.ic_not_verified)
            tvisEmailverified.text = "Not Verified"
            imgvwVerifyStateEmail.isClickable = true
            imgvwVerifyStateEmail.isEnabled = true
        } else {
            imgvwVerifyStateEmail.background = ContextCompat.getDrawable(activity as HomeActivity, R.drawable.ic_verified_icon)
            tvisEmailverified.text = "Verified"
            imgvwVerifyStateEmail.isClickable = false
            imgvwVerifyStateEmail.isEnabled = false
        }


        if (userDetailsResponse.is_kyc_verified.toString().isEmpty() || userDetailsResponse.is_kyc_verified.toString().equals("0")) {
            imgvwVerifykyc.background = ContextCompat.getDrawable(activity as HomeActivity, R.drawable.ic_not_verified)
            tvisKycVerified.text = "Not Verified"
            tvAddKYC.text="Complete Your KYC Verification"
        } else {
            imgvwVerifykyc.background = ContextCompat.getDrawable(activity as HomeActivity, R.drawable.ic_verified_icon)
            tvisKycVerified.text = "Verified"
            tvAddKYC.visibility=View.GONE
//            llKycVerify.isClickable=false
//            imgvwVerifykyc.isClickable=false

        }


        if (userDetailsResponse.dateOfBirth.toString().isEmpty()) {
            tvBrithday.visibility = View.GONE
        } else {
            tvBrithday.visibility = View.VISIBLE
            tvDobMyProfile.text = Utils.getFormasssttedDate(userDetailsResponse.dateOfBirth!!)
        }
        try {
            Picasso.get().load(userDetailsResponse.mainimage!!.toString())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(ivProfileImage)
        } catch (e: Exception) {
            e.printStackTrace()
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as HomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: ViewProfileContract.Presenter) {
        viewProfilePresenter = presenter as ViewProfilePresenter
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)

    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llviewprofile, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llviewprofile, msg, 3000)
    }



    override fun globalLogout() {
        appData!!.cleardata(context!!, Constants.Keys._KeyCryptoPreference)
        val intent = Intent(context, LoginActivity::class.java)
        activity!!.startActivity(intent)
        activity!!.finishAffinity()
    }

    override fun onLeftClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRightClick() {
        val textEmail: String = tvEmailMyProfile.text as String
        Log.v("emaii_user", textEmail);
        viewProfilePresenter?.callUSerEmailValidate(appData!!, appData!!.register_email)
    }

    override fun showToast(msg: String) {
        Utils.showToast(context, msg)
    }
}