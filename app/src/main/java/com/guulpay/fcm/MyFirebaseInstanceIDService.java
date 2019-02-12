package com.guulpay.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.guulpay.others.AppData;

/**
 * Created by indranil on 2/3/18.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        saveRegistrationToken(refreshedToken);
    }

    private void saveRegistrationToken(String refreshedToken) {
        AppData appData=new AppData(getApplicationContext(),"_cryptopreference");
        appData.setSave_devicetoken(refreshedToken);
    }
}
