package com.guulpay.kycverification

import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.GsonBuilder
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.activity.HomeActivity
import com.guulpay.fragments.BaseFragment
import com.guulpay.kycverification.model.KYCBaseResponse
import com.guulpay.kycverification.model.KYCResponse
import com.guulpay.others.*
import com.guulpay.others.encryption.AESCrypt
import com.guulpay.others.encryption.ObjectToJSonConvertor
import im.delight.android.webview.AdvancedWebView
import kotlinx.android.synthetic.main.about_us_fragment_layout.*
import org.jsoup.Jsoup
import android.content.Intent
import android.os.Handler


class KycFragment : BaseFragment(), View.OnClickListener, KycVerficationContract.View, AdvancedWebView.Listener {

    lateinit var aboutUsPresenter: KycPresenter
    var appdata: AppData? = null
    var encryptedstring: String? = ""
    var saltkey: String? = ""
    var loadurl: String? = ""
    var internalloadurl: String? = ""

    var mHtmlString: String? = null
    var responsecode: String? = null
    var responsemsg: String? = null

    companion object {
        const val CLASS_NAME = "KycFragment"
        fun newInstance(url: String): Fragment {
            val fragment = KycFragment()
            val bundle = Bundle()
            bundle.putString(Constants.Keys.KEY_URL, url)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.about_us_fragment_layout
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appdata = AppData(activity as HomeActivity, Constants.Keys._KeyCryptoPreference)
        aboutUsPresenter = KycPresenter(this)
    }

    override fun initListeners() {
        webview.setListener(activity, this)
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
        //fromWhichFragment = arguments!!.getString(Constants.Keys.KEY_FRAGMENT_NAME)
        var randomkey = Utils.generateRandom16bitKey()
        saltkey = ObjectToJSonConvertor.base64Encode(randomkey).toString().replace("\n", "")
        encryptedstring = AESCrypt.encrypt(appdata!!.user_id, randomkey)
        loadurl = arguments!!.getString(Constants.Keys.KEY_URL)

        Log.d("URLDATA", loadurl.toString())

        webview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                Log.d("LoadedUrl", url)
                internalloadurl = url
                if (url!!.contains("https://guulpay.indusnet.cloud/guulpay/guulpayapi/api/kyc?")) {
                    FetchKycResponseAsynTask().execute()
                }
                return true
            }
        }

        loader.show()
        webview.loadUrl(loadurl)
        FetchCountryCodesAsynTask().execute()

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


    override fun onPause() {
        super.onPause()
        try {
            webview.onPause()
            webview.clearCache(true)
        } catch (e: Exception) {
        }
    }

    //https://guulpay.indusnet.cloud/guulpay/guulpayapi/api/kyc

    inner class FetchCountryCodesAsynTask : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            loader.show()
        }

        override fun doInBackground(vararg p0: Void): String {
            extractDataFromURL()
            return ""
        }

        override fun onPostExecute(result: String?) {
            loader.hide()
            if (responsecode.equals("201")) {
                Utils.showToast(context, responsemsg!!)
                activity?.onBackPressed()
            }
        }
    }

    /*fun extractDataFromURL() {
        try {
            val doc = Jsoup.connect(url).ignoreContentType(true).get()
            // val content = doc.getElementById("\"data\":") //this is one of the divisions id (<div id=mp-tfa>...</div>)
            //val links = content.getElementsByTag("p") //this is a para inside divisino <div id=mp-tfa><p>...</p>/div>)
            mHtmlString = doc.toString()//now mHtmlString contains the text inside the para
            Log.d("mHtmlString", mHtmlString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

    fun extractDataFromURL() {
        try {
            val doc = Jsoup.connect(loadurl).ignoreContentType(true).get()
            mHtmlString = doc.toString()  //return the html strng
            Log.d("mHtmlString", Jsoup.parse(mHtmlString).text()) ///remove all the HTML tags
            val kYCBaseResponse = GsonBuilder().create().fromJson(Jsoup.parse(mHtmlString).text(), KYCBaseResponse::class.java) //convert response to JSON ready for parsing
            val decruptedjson = AESCrypt.decrypt(Base64.decode(kYCBaseResponse.response!!.salt, Base64.NO_WRAP), kYCBaseResponse.response!!.data)
            Log.d("decruptedjson", decruptedjson)
            val response = GsonBuilder().create().fromJson(decruptedjson, KYCResponse::class.java)
            responsecode = response.responseCode
            responsemsg = response.responseDetails
        } catch (e: Exception) {
            //  e.printStackTrace()
        }
    }


    inner class FetchKycResponseAsynTask : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            loader.show()
        }

        override fun doInBackground(vararg p0: Void): String {
            extractDataFromURL1()
            return ""
        }

        override fun onPostExecute(result: String?) {
            loader.hide()
            if (responsecode.equals("201")) {
                Utils.showToast(context, responsemsg!!)
                activity?.onBackPressed()
            } else if (responsecode.equals("200")) {
                Utils.showToast(context, responsemsg!!)
                activity?.onBackPressed()
            } else if (responsecode.equals("226")) {
                Utils.showToast(context, responsemsg!!)
                activity?.onBackPressed()
            }
        }
    }

    fun extractDataFromURL1() {
        try {
            val doc = Jsoup.connect(internalloadurl).ignoreContentType(true).get()
            mHtmlString = doc.toString()
            Log.d("mHtmlString", Jsoup.parse(mHtmlString).text())
            val kYCBaseResponse = GsonBuilder().create().fromJson(Jsoup.parse(mHtmlString).text(), KYCBaseResponse::class.java)
            val decruptedjson = AESCrypt.decrypt(Base64.decode(kYCBaseResponse.response!!.salt, Base64.NO_WRAP), kYCBaseResponse.response!!.data)
            Log.d("decruptedjson", decruptedjson)
            val response = GsonBuilder().create().fromJson(decruptedjson, KYCResponse::class.java)
            responsecode = response.responseCode
            responsemsg = response.responseDetails
            // Log.d("kYCResponse", kYCResponse.response!!.data + " " + kYCResponse.response!!.salt)
        } catch (e: Exception) {
            //e.printStackTrace()
        }
    }

    override fun onPageFinished(url: String?) {
        try {
            loader.hide()
            webview.visibility = View.VISIBLE
        } catch (e: Exception) {

        }
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDownloadRequested(url: String?, suggestedFilename: String?, mimeType: String?, contentLength: Long, contentDisposition: String?, userAgent: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onExternalPageRequest(url: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        webview.visibility = View.INVISIBLE
        loader.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        webview.onActivityResult(requestCode, resultCode, intent)
    }

    override fun globalLogout() {
        //appData!!.cleardata(activity!!, Constants.Keys._KeyCryptoPreference)
    }
}

