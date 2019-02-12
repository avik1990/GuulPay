package com.guulpay.others

import android.content.Context
import com.andreacioccarelli.cryptoprefs.CryptoPrefs

/* All shared preference datas */
class SaveDeviceToken {
    val cryptoPrefs: CryptoPrefs

    constructor(context: Context) {
        cryptoPrefs = CryptoPrefs(context, Constants.Keys._CryptoPrefsFilenameToken, "DeviceToken", true)
    }

    init {

    }


}

