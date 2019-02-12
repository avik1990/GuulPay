package com.guulpay.others

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.andreacioccarelli.cryptoprefs.CryptoPrefs
import com.guulpay.login.LoginActivity

/* All shared preference datas */
class AppData {
    val cryptoPrefs: CryptoPrefs
    val cryptoPrefsfcm: CryptoPrefs

    constructor(context: Context, secretKey: String) {
        cryptoPrefs = CryptoPrefs(context, Constants.Keys._CryptoPrefsFilename, secretKey, true)
        cryptoPrefsfcm = CryptoPrefs(context, Constants.Keys._CryptoPrefsFilenameToken, "fcmToken", true)
    }

    fun cleardata(context: Context, secretKey: String) {
        // val prefs = CryptoPrefs(context, Constants.Keys._CryptoPrefsFilename, Constants.Keys._KeyCryptoPreference, false)
        //  cryptoPrefs.remove(Constants.Keys._remember_token)
        cryptoPrefs.erase()
        RetrofitClient.clearRetrofitInstance()
        //  cryptoPrefs.apply()
    }

    init {

    }

    var isLoggedin: Boolean
        get() {
            return cryptoPrefs.getBoolean(Constants.Keys._KeyFirstTimeInstalled, false)
        }
        set(value) {
            cryptoPrefs.put(Constants.Keys._KeyFirstTimeInstalled, value)
        }

    var isFingerPrint: Boolean
        get() {
            return cryptoPrefs.getBoolean("isFingerPrint", true)
        }
        set(value) {
            cryptoPrefs.put("isFingerPrint", value)
        }

    var save_devicetoken: String
        get() {
            return cryptoPrefsfcm.getString("fcm_token", "")
        }
        set(value) {
            cryptoPrefsfcm.put("fcm_token", value)
        }


    /*var isFirstTimeInstalled: Boolean
        get() {
            return cryptoPrefs.getBoolean(Constants.Keys._KeyFirstTimeInstalled, false)
        }
        set(value) {
            cryptoPrefs.put(Constants.Keys._KeyFirstTimeInstalled, value)
        }*/
    var currency_symbol: String
        get() {
            return cryptoPrefs.getString("currency_symbol", "")
        }
        set(value) {
            cryptoPrefs.put("currency_symbol", value)
        }

    var register_country_code: String
        get() {
            return cryptoPrefs.getString(Constants.Keys._country_code, "")
        }
        set(value) {
            cryptoPrefs.put(Constants.Keys._country_code, value)
        }

    var register_device_token: String
        get() {
            return cryptoPrefs.getString(Constants.Keys._device_token, "")
        }
        set(value) {
            cryptoPrefs.put(Constants.Keys._device_token, value)
        }


    var register_device_type: String
        get() {
            return cryptoPrefs.getString(Constants.Keys._device_type, "")
        }
        set(value) {
            cryptoPrefs.put(Constants.Keys._device_type, value)
        }

    var register_password: String
        get() {
            return cryptoPrefs.getString(Constants.Keys._password, "")
        }
        set(value) {
            cryptoPrefs.put(Constants.Keys._password, value)
        }

    var register_phone: String
        get() {
            return cryptoPrefs.getString("register_phone", "")
        }
        set(value) {
            cryptoPrefs.put("register_phone", value)
        }
    var register_email: String
        get() {
            return cryptoPrefs.getString("email", "")
        }
        set(value) {
            cryptoPrefs.put("email", value)
        }
    var user_id: String
        get() {
            return cryptoPrefs.getString("user_id", "")
        }
        set(value) {
            cryptoPrefs.put("user_id", value)
        }

    var remember_token: String
        get() {
            Log.e("RememberToken", cryptoPrefs.getString(Constants.Keys._remember_token, ""))
            return cryptoPrefs.getString(Constants.Keys._remember_token, "")
        }
        set(value) {
            Log.e("RememberTokenSet", value)
            cryptoPrefs.remove(Constants.Keys._remember_token)
            cryptoPrefs.put(Constants.Keys._remember_token, value)
        }

    var register_countryName: String
        get() {
            return cryptoPrefs.getString("countryName", "")
        }
        set(value) {
            cryptoPrefs.put("countryName", value)
        }
    var register_currency_code: String
        get() {
            return cryptoPrefs.getString("currency_code", "")
        }
        set(value) {
            cryptoPrefs.put("currency_code", value)
        }

    var register_currency_name: String
        get() {
            return cryptoPrefs.getString("currency_name", "")
        }
        set(value) {
            cryptoPrefs.put("currency_name", value)
        }

    var register_wallet_id: String
        get() {
            return cryptoPrefs.getString("wallet_id", "")
        }
        set(value) {
            cryptoPrefs.put("wallet_id", value)
        }

    var register_wallet_currency_id: String
        get() {
            return cryptoPrefs.getString("wallet_currency_id", "")
        }
        set(value) {
            cryptoPrefs.put("wallet_currency_id", value)
        }

    var register_amount: String
        get() {
            return cryptoPrefs.getString("amount", "0.00")
        }
        set(value) {
            cryptoPrefs.put("amount", value)
        }
    var country_call_prifix: String
        get() {
            return cryptoPrefs.getString("country_call_prifix", "")
        }
        set(value) {
            cryptoPrefs.put("country_call_prifix", value)
        }

    var register_name: String
        get() {
            return cryptoPrefs.getString("register_name", "")
        }
        set(value) {
            cryptoPrefs.put("register_name", value)
        }

    var register_isEmailVerify: String
        get() {
            return cryptoPrefs.getString("register_isEmailVerify", "")
        }
        set(value) {
            cryptoPrefs.put("register_isEmailVerify", value)
        }

    var profile_image: String
        get() {
            return cryptoPrefs.getString("profile_image", "")
        }
        set(value) {
            cryptoPrefs.put("profile_image", value)
        }

    var register_isPhoneVerify: String
        get() {
            return cryptoPrefs.getString("register_isPhoneVerify", "")
        }
        set(value) {
            cryptoPrefs.put("register_isPhoneVerify", value)
        }

    var register_dateOfBirth: String
        get() {
            return cryptoPrefs.getString("register_dateOfBirth", "")
        }
        set(value) {
            cryptoPrefs.put("register_dateOfBirth", value)
        }

    var is_kyc_verified: String
        get() {
            return cryptoPrefs.getString("is_kyc_verified", "")
        }
        set(value) {
            cryptoPrefs.put("is_kyc_verified", value)
        }

}

