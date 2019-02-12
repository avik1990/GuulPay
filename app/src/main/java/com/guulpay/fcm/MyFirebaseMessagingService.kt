package com.guulpay.fcm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {
    var manager: NotificationManager? = null
    var myNotication: Notification? = null

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        val from = message!!.from
        val data = message.data
        var msg: String? = ""
        val imageurl = ""
        var title: String? = ""
        var subtitle: String? = ""
        var msg_flag: String? = ""
        val gson = Gson()


        try {
            if (data != null) {
                val jsonresponse = data["msg"].toString()
                val user = gson.fromJson(jsonresponse, NotificationModel::class.java)

                title = user.title
                subtitle = user.subtitle
                msg = user.msg
                msg_flag = user.type
            }
            if (imageurl.isEmpty() || imageurl == "") {
                generateNotification(this, msg!!, title!!, msg, msg_flag!!)
            } else {
                generateNotification(this, msg, imageurl, title, msg, msg_flag)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateNotification(mContext: Context, message: String, title: String, subtitle: String, flag: String) {
        setBigPictureStyleNotification(mContext, title, message, flag)
    }

    fun setBigPictureStyleNotification(mContext: Context, title: String, msg: String, flag: String) {
        val mBuilder = NotificationCompat.Builder(this)
        mBuilder.setTicker("Notification from #Evoire")
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
        mBuilder.setWhen(System.currentTimeMillis())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.color = 0xffea00
        }
        mBuilder.setAutoCancel(true)
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.resources, R.mipmap.ic_launcher))
        mBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(msg))
        mBuilder.setContentTitle(title)
        mBuilder.setContentText(msg)
        val resultIntent = Intent(this, DashboardHomeActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(DashboardHomeActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        mBuilder.setContentIntent(resultPendingIntent)
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        val random = Random()
        val m = random.nextInt(9999 - 1000) + 1000
        mNotificationManager!!.notify(m, mBuilder.build())
    }

    fun setBigPictureStyleNotification(mContext: Context, imageurl: String, title: String?, subtitle: String?, flag: String?): Notification {
        var remote_picture: Bitmap? = null

        val notiStyle = NotificationCompat.BigPictureStyle()
        notiStyle.setBigContentTitle(title)
        notiStyle.setSummaryText(subtitle)
        try {
            val bmURL = getBitmapFromURL(imageurl)
            remote_picture = bmURL
        } catch (e: Exception) {
            e.printStackTrace()
        }

        notiStyle.bigPicture(remote_picture)
        val resultIntent = Intent(this, DashboardHomeActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(DashboardHomeActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)


        return NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(NotificationCompat.BigTextStyle().bigText(subtitle))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(subtitle)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.resources, R.mipmap.ic_launcher))
                .setStyle(notiStyle).build()
    }


    fun getBitmapFromURL(src: String): Bitmap? {
        try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
            //   return myBitmap;
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    private fun generateNotification(mContext: Context, message: String?, imageurl: String, title: String?, subtitle: String?, flag: String?) {
        val `when` = System.currentTimeMillis()
        if (!imageurl.isEmpty()) {
            val mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(0, setBigPictureStyleNotification(mContext, imageurl, title, subtitle, flag))
        } else {
            val intent = Intent(mContext, DashboardHomeActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(mContext, 1, intent, 0)
            val builder = Notification.Builder(mContext)
            builder.setAutoCancel(true)
            builder.setContentTitle(title)
            builder.setContentText(message)
            builder.setSmallIcon(R.mipmap.ic_launcher)
            builder.setContentIntent(pendingIntent)
            builder.setWhen(`when`)
            builder.build()
            myNotication = builder.notification
            myNotication!!.defaults = myNotication!!.defaults or Notification.DEFAULT_SOUND
            myNotication!!.flags = myNotication!!.flags or Notification.FLAG_AUTO_CANCEL
            val random = Random()
            val m = random.nextInt(9999 - 1000) + 1000
            manager!!.notify(m, myNotication)
        }
    }
}
