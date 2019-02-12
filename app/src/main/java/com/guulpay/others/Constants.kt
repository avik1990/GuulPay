package com.guulpay.others

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


object Constants {

    /* For all Keys Hashmap, CryptoPrefs, Intents, etc. */
    public object Keys {
        /* All preferences are stored in one file, changing the _key and _secretKey*/
        val _CryptoPrefsFilename = "_CryptoPrefsFilename"
        val _CryptoPrefsFilenameToken = "_CryptoPrefsFilenameToken"

        /* Required for finger print check */
        val _KEYSTORE_TYPE = "AndroidKeyStore"
        val _KEYSTORE_ALIASNAME = "_KEYSTORE_ALIASNAME"
        //val _SecretKeyFingerPrint = "_SecretKeyFingerPrint"
        /* To check whether the app is installed for the first time or not */
        // val _SecretKeyIsFirstTimeInstalled = "_SecretKeyIsFirstTimeInstalled"
        val _KeyFirstTimeInstalled = "_KeyFirstTimeInstalled"
        val _KeyCryptoPreference = "_cryptopreference"  /// change in the MyFirebaseInstanceService
        val _emailorphone = "emailorphone"
        val _remember_token = "remember_token"
        val _password = "password"
        val _device_type = "device_type"
        val _device_token = "device_token"
        val _country_code = "country_code"
        val _otp = "otp"
        val _user_id = "userid"
        val _isLoggedin = "_isLoggedin"


        /* Fragment name key. Required to redirect in specific fragment from HomeActivity/DashboardActivity */
        val KEY_FRAGMENT_NAME: String = "fragmentName"
        val KEY_URL: String = "keyurl_"
        val KEY_FROM_WHICH_FRAGMENT_CALLING: String = "fromWhichFragmentCalling"
        val KEY_FROM_AFTER_LOGINRESPONSE_203: String = "fromWhichFragmentCalling_203"
        val KEY_COUNTRY_ID: String = "countryid"

        /* Used in Enter OTP page, to show mobile number */
        val KEY_MOBILE_NUMBER = "keyMobileNumber"
        /* Tab position for : Send, Request and Pay money in payments */
        val KEY_TAB_POSITION: String = "tabPositon"
        val SEND_MONEY_TAB_POSITION = 0
        val REQUEST_MONEY_TAB_POSITION = 1
        val PAY_MONEY_TAB_POSITION = 2
        val DEVICE_TYPE = "android"

        /* Required to select country codes and return to the calling activity with result */
        val KEY_SELECTED_COUNTRY_CODE = "keySelectedCountryCode"
        val KEY_SELECTED_COUNTRY_CODEID = "keySelectedCountryCodeID"
        val KEY_SELECTED_COUNTRY_CURRENCYCODE = "keySelectedCountrycurrency_code"


        val KEY_SELECTED_PHONE_NUMBER = "keySelectedPhoneNumber"
        val KEY_SELECTED_BANK_CODE = "keySelectedBankCode"


        val KEY_SELECTED_OPERATOR_CODE = "keySelectedOperatorCode"
        val KEY_SELECTED_OPERATOR_NAME = "keySelectedOperatotName"


        /*               Rishabh 25/1/2019                             */
        val KEY_SELECTED_COUNTRY_CODE_NAME="keySelectedCountryCodeName"

        val GUULEX_BUY_TAB_POSITION = 0
        val GUULEX_SELL_TAB_POSITION = 1

    }

    object Values {
        //Hold signup data
        var SIGNUP_DATAS = ""
        /* Required to detect ScanQrFragment is opening from which purpose/screen */
        val TO_PAY_MONEY = "toPayMoneyScreen"
        val TO_SEND_MONEY = "toSendMoney"
        /* Max date for DOB --> 1st Jan 2002 */
        val MAX_YEAR = 2002
        val MAX_MONTH = 0
        val MAX_DAY = 1
    }

    /* For OnActivityResult */
    object RequestCodes {
        /* Required to select country codes and return to the calling activity with result */
        val REQUEST_PHONENO = 1097
        val REQUEST_CODE_OPERATOR_ID = 1098
        val REQUEST_CODE_COUNTRY = 123

        val REQUEST_CODE_COUNTRY_ID = 2344


        /*Edited 25/1/2019 rishabh*/
        val REQUEST_CODE_COUNTRY_NAME=456



        /* PermissionGen library request code for runtime permissions */
        val REQUEST_CODE_PERMISSIONS = 100
        /* Required to open Gallery */
        val REQUEST_CODE_GALLERY = 456
        /*Request bank code*/
        val REQUEST_CODE_BANK = 234

        val REQUEST_RECHARGEREPEAT = 1099

    }

    object Services {
        val BASE_URL = "https://guulpay.indusnet.cloud/guulpay/guulpayapi/api/"
        val BASE_URL_BACKEND = "https://guulpay.indusnet.cloud/guulpay/guulpaybackend/"

    }

    fun setTimeOutForDatapost(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
                .connectTimeout(100 * 10, TimeUnit.SECONDS)
                .readTimeout(100 * 10, TimeUnit.SECONDS)
                .writeTimeout(100 * 10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor { chain ->
                    val request = chain.request()
                            ?.newBuilder()
                            //?.addHeader("Content-Type", "application/json")
                            ?.addHeader("Content-Type", "application/json")
                            ?.build()
                    chain.proceed(request)
                }
                .addInterceptor(loggingInterceptor)
                .build()
    }






    fun setTimeOut(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
                .connectTimeout(100 * 10, TimeUnit.SECONDS)
                .readTimeout(100 * 10, TimeUnit.SECONDS)
                .writeTimeout(100 * 10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor { chain ->
                    val request = chain.request()
                            ?.newBuilder()
                            //?.addHeader("Content-Type", "application/json")
                            ?.addHeader("Content-Type", "application/json")
                            ?.build()
                    chain.proceed(request)
                }
                .addInterceptor(loggingInterceptor)
                .build()
    }

    fun setTimeOutAfterLogin(rember_token: String): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
                .connectTimeout(100 * 10, TimeUnit.SECONDS)
                .readTimeout(100 * 10, TimeUnit.SECONDS)
                .writeTimeout(100 * 10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor { chain ->
                    val request = chain.request()
                            ?.newBuilder()
                            ?.addHeader("Authorization", "bearer $rember_token")
                            ?.addHeader("Content-Type", "application/json")
                            ?.build()
                    chain.proceed(request)
                }
                .addInterceptor(loggingInterceptor)
                .build()
    }

    fun setTimeOutAfterLoginHeaderFormData(rember_token: String): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
                .connectTimeout(100 * 10, TimeUnit.SECONDS)
                .readTimeout(100 * 10, TimeUnit.SECONDS)
                .writeTimeout(100 * 10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .addInterceptor { chain ->
                    val request = chain.request()
                            ?.newBuilder()
                            ?.addHeader("Authorization", "bearer $rember_token")
                            ?.addHeader("Content-Type", "Form-data")
                            ?.build()
                    chain.proceed(request)
                }
                .addInterceptor(loggingInterceptor)
                .build()
    }
}