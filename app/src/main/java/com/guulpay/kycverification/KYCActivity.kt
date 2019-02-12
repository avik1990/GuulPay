package com.guulpay.kycverification

import android.content.Context
import android.content.Intent
import android.content.Intent.parseIntent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.guulpay.R
import com.guulpay.R.id.*
import com.guulpay.activity.BaseActivity
import com.guulpay.activity.HomeActivity
import com.guulpay.addBankAccount.AddBankAccountFragment
import com.guulpay.addMoney.addAmount.AddMoneyEnterAmountFragment
import com.guulpay.addMoney.completeYourPayment.CompleteYourPaymentFragment
import com.guulpay.addMoney.paymentStatus.AddMoneyPaymentStatusFragment
import com.guulpay.addReceiver.AddReceiverAccountFragment
import com.guulpay.addReceiver.reciever.RecieversFragment
import com.guulpay.basemodel.CurrenciesModel
import com.guulpay.changePasscode.ChangePasscodeFragment
import com.guulpay.contactus.ContactUsFragment
import com.guulpay.enterOtp.EnterOtpFragment
import com.guulpay.forgotPasscode.enterMobile.EnterMobileForgotPasscodeFragment
import com.guulpay.guulex.GuulexFragment
import com.guulpay.kycverification.KycFragment
import com.guulpay.kycverification.model.KYCBaseResponse
import com.guulpay.kycverification.model.KYCResponse
import com.guulpay.login.LoginActivity
import com.guulpay.mobilerecharge.MobileRechargeFragment
import com.guulpay.mobilerechargehistory.MobileRechargeHistFragment
import com.guulpay.myProfile.edit.EditProfileFragment
import com.guulpay.myProfile.view.ViewProfileFragment
import com.guulpay.myWallet.autoLoad.AutoLoadAmountFragment
import com.guulpay.notification.NotificationFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import com.guulpay.others.encryption.AESCrypt
import com.guulpay.others.encryption.ObjectToJSonConvertor
import com.guulpay.payments.send.paymentStatus.SendMoneyPaymentStatus
import com.guulpay.savedBanks.SavedBanksFragment
import com.guulpay.savedCards.SavedCardsFragment
import com.guulpay.termsAndConditions.TermsAndConditionsFragment
import com.guulpay.viewAllRecentTransactions.ViewAllRecentTransFragment
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.about_us_fragment_layout.*
import kotlinx.android.synthetic.main.common_toolbar_layout.*
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.myprofile_view_fragment.*
import org.jsoup.Jsoup
import java.io.IOException

class KYCActivity : BaseActivity(), View.OnClickListener, KycVerficationContract.View, AdvancedWebView.Listener {


    var appData: AppData? = null
    lateinit var aboutUsPresenter: KycPresenter
    var appdata: AppData? = null
    var encryptedstring: String? = ""
    var saltkey: String? = ""
    var loadurl: String? = ""
    var internalloadurl: String? = ""

    var mHtmlString: String? = null
    var responsecode: String? = null
    var responsemsg: String? = null
    var context: Context? = null

    private val loader by lazy {
        LoaderDialog(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun initPermissions() {
        Utils.checkPermissions(this)
    }

    override fun initResources() {
        context = this
        appData = AppData(this@KYCActivity, Constants.Keys._KeyCryptoPreference)
        aboutUsPresenter = KycPresenter(this)
        tvCommonToolbar.setText("KYC Verification")
        imgvwCommonToolbar.setOnClickListener(this)
        webview.setListener(context as KYCActivity, this)
        webview.setGeolocationEnabled(false)
        webview.setMixedContentAllowed(true)
        webview.setCookiesEnabled(true)
        webview.setThirdPartyCookiesEnabled(true)
        webview.settings.javaScriptEnabled = true

        webview.getSettings().setLoadsImagesAutomatically(true)
        webview.getSettings().setAllowUniversalAccessFromFileURLs(true)
        webview.getSettings().setDisplayZoomControls(true)
        webview.clearHistory()
        webview.clearCache(true)
        loadurl = intent.getStringExtra(Constants.Keys.KEY_URL)

        Log.d("URLDATA", loadurl.toString())

        webview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                view?.loadUrl(url)
                Log.d("LoadedUrl123", url)
                internalloadurl = url
                return true
            }

            //Very Important Added 7/02/2019 Rishabh

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url!!.contains("https://guulpay.indusnet.cloud/guulpay/guulpayapi/api/kyc?")) {
                    FetchKycResponseAsynTask().execute()
//                    imgvwVerifykyc.background = ContextCompat.getDrawable(context as KYCActivity, R.drawable.ic_verified_icon)
//                    //imgvwVerifykyc.drawable(R.drawable.ic_verified_icon)
//                    tvisKycVerified.text = "Verified"
//                    tvAddKYC.visibility=View.GONE
//                    llKycVerify.isClickable=false
//                    imgvwVerifykyc.isClickable=false

                }
            }
        }



        loader.show()
        webview.loadUrl(loadurl)
        //FetchCountryCodesAsynTask().execute()
       //FetchKycResponseAsynTask().execute()
    }

    override fun initListeners() {
        //imgvwCommonToolbar.setOnClickListener(this)
    }

    override fun getLayout(): Int {
        return R.layout.kyc_webview_layout
    }

    override fun onClick(v: View?) {
        if (v == imgvwCommonToolbar) {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun globalLogout() {
        appData!!.cleardata(applicationContext, Constants.Keys._KeyCryptoPreference)
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    inner class FetchCountryCodesAsynTask : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            loader.show()
        }

        override fun doInBackground(vararg p0: Void): String {
            extractDataFromURL()
            Log.v("background","Background Task running")
            return ""
        }

        override fun onPostExecute(result: String?) {
            loader.hide()
            if (responsecode.equals("201")) {
                Utils.showToast(context, responsemsg!!)
                onBackPressed()
            }
        }
    }

    inner class FetchKycResponseAsynTask : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            loader.show()
        }

        override fun doInBackground(vararg p0: Void): String {
            extractDataFromURL1()
            Log.v("background1","Background Task running")
            return ""
        }

        override fun onPostExecute(result: String?) {
            loader.hide()
            if (responsecode.equals("201")) {
                Utils.showToast(context, responsemsg!!)
                onBackPressed()
            } else if (responsecode.equals("200")) {
                Utils.showToast(context, responsemsg!!)
                onBackPressed()
            } else if (responsecode.equals("226")) {
                Utils.showToast(context, responsemsg!!)
                onBackPressed()
            }
            else if (responsecode.equals("401")) {
                Utils.showToast(context, responsemsg!!)
                onBackPressed()
            }
        }
    }

    fun extractDataFromURL1() {
        try {
            val doc = Jsoup.connect(internalloadurl).ignoreContentType(true).get()
            mHtmlString = doc.toString()
            Log.d("LoadedUrl", Jsoup.parse(mHtmlString).text())
            val kYCBaseResponse = GsonBuilder().create().fromJson(Jsoup.parse(mHtmlString).text(), KYCBaseResponse::class.java)
            val decruptedjson = AESCrypt.decrypt(Base64.decode(kYCBaseResponse.response!!.salt, Base64.NO_WRAP), kYCBaseResponse.response!!.data)
            Log.d("decruptedjson_url1", decruptedjson)
            val response = GsonBuilder().create().fromJson(decruptedjson, KYCResponse::class.java)
            responsecode = response.responseCode
            responsemsg = response.responseDetails
            // Log.d("kYCResponse", kYCResponse.response!!.data + " " + kYCResponse.response!!.salt)
        } catch (e: Exception) {
            //e.printStackTrace()
        }
    }

    fun extractDataFromURL() {
        try {
            val doc = Jsoup.connect(loadurl).ignoreContentType(true).get()
            mHtmlString = doc.toString()  //return the html strng
            Log.d("mHtmlString_url", Jsoup.parse(mHtmlString).text()) ///remove all the HTML tags
            val kYCBaseResponse = GsonBuilder().create().fromJson(Jsoup.parse(mHtmlString).text(), KYCBaseResponse::class.java) //convert response to JSON ready for parsing
            val decruptedjson = AESCrypt.decrypt(Base64.decode(kYCBaseResponse.response!!.salt, Base64.NO_WRAP), kYCBaseResponse.response!!.data)
            Log.d("decruptedjson_url", decruptedjson)
            val response = GsonBuilder().create().fromJson(decruptedjson, KYCResponse::class.java)
            responsecode = response.responseCode
            responsemsg = response.responseDetails
          //  Log.v("response_data",response.to)
        } catch (e: Exception) {
            //  e.printStackTrace()
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isActivityRunning(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPresenter(presenter: KycVerficationContract.Presenter) {
        aboutUsPresenter = presenter as KycPresenter
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

    override fun onPageFinished(url: String?) {
        try {
            loader.hide()
            webview.visibility = View.VISIBLE
        } catch (e: Exception) {

        }
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        Log.d("Error", "" + errorCode)
    }

    override fun onDownloadRequested(url: String?, suggestedFilename: String?, mimeType: String?, contentLength: Long, contentDisposition: String?, userAgent: String?) {
        Log.d("Error", "" + url)
    }

    override fun onExternalPageRequest(url: String?) {
        Log.d("Error", "" + url)
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        webview.visibility = View.INVISIBLE
        loader.show()
    }


}
