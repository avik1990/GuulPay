package com.guulpay.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.guulpay.R;
import com.guulpay.activity.DashboardHomeActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Random;


public class GCMPushReceiverService extends FirebaseMessagingService {
    static NotificationManager manager;
    static Notification myNotication;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        String from = message.getFrom();
        Map data = message.getData();
        String msg = "";
        String imageurl = "";
        String title = "";
        String subtitle = "";
        String msg_flag = "";
        Gson gson = new Gson();


        try {
            if (data != null) {
                String jsonresponse = data.get("msg").toString();
                NotificationModel user = gson.fromJson(jsonresponse, NotificationModel.class);

                title = user.getTitle();
                subtitle = user.getSubtitle();
                msg = user.getMsg();
                msg_flag = user.getType();
            }
            if (imageurl.isEmpty() || imageurl.equals("")) {
                generateNotification(this, msg, title, msg, msg_flag);
            } else {
                generateNotification(this, msg, imageurl, title, msg, msg_flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateNotification(Context mContext, String message, String title, String subtitle, String flag) {
        setBigPictureStyleNotification(mContext, title, message, flag);
    }

    public void setBigPictureStyleNotification(Context mContext, String title, String msg, String flag) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setTicker("Notification from #Evoire");
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setColor(0xffea00);
        }
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setLargeIcon((BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher)));
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(msg);
        Intent resultIntent = new Intent(this, DashboardHomeActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DashboardHomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        mNotificationManager.notify(m, mBuilder.build());
    }

    public Notification setBigPictureStyleNotification(Context mContext, String imageurl, String title, String subtitle, String flag) {
        Bitmap remote_picture = null;

        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle(title);
        notiStyle.setSummaryText(subtitle);
        try {
            Bitmap bmURL = getBitmapFromURL(imageurl);
            remote_picture = bmURL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        notiStyle.bigPicture(remote_picture);
        Intent resultIntent = new Intent(this, DashboardHomeActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DashboardHomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(subtitle))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(subtitle)
                .setLargeIcon((BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher)))
                .setStyle(notiStyle).build();
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
            //   return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void generateNotification(Context mContext, String message, String imageurl, String title, String subtitle, String flag) {
        //Eutils.setNotificationFlag(mContext, flag.trim());
        long when = System.currentTimeMillis();
        if (!imageurl.isEmpty()) {
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, setBigPictureStyleNotification(mContext, imageurl, title, subtitle, flag));
        } else {
            Intent intent = new Intent(mContext, DashboardHomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, intent, 0);
            Notification.Builder builder = new Notification.Builder(mContext);
            builder.setAutoCancel(true);
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(pendingIntent);
            builder.setWhen(when);
            builder.build();
            myNotication = builder.getNotification();
            myNotication.defaults |= Notification.DEFAULT_SOUND;
            myNotication.flags |= Notification.FLAG_AUTO_CANCEL;
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            manager.notify(m, myNotication);
        }
    }
}
