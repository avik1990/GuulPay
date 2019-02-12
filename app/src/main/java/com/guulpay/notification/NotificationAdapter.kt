package com.guulpay.notification

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.guulpay.R
import com.guulpay.mobilerecharge.model.TransactionHistResponse
import com.guulpay.mobilerechargehistory.MobileHistRechargeAdapter
import com.guulpay.notification.model.NotificationResponse


class NotificationAdapter(private val context: Context, private val notificationAdapter: NotificationAdapter.oNNotificationListClick) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {

    private var notificationList: MutableList<NotificationResponse.NotificationList> = ArrayList()


    fun setData(notificationListall: List<NotificationResponse.NotificationList>) {
        notificationList!!.clear()
        notificationList.addAll(notificationListall)
        Log.v("notification_list",notificationList.size.toString())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.getContext())
                .inflate(R.layout.notification_row, parent, false)

        return MyViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Log.v("position",position.toString())
        //Adding Data to View of Recycler View
        holder.tvContent.text = notificationList[position].content
        holder.tvDate.text = notificationList[position].createdAt


    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvContent: TextView
        var tvDate: TextView

        init {
            //Initialising the View with IDs
            tvContent= itemView.findViewById(R.id.tvContentNotification)
            tvDate=itemView.findViewById(R.id.tvDateNotification)

        }

    }

    interface oNNotificationListClick {
        fun gettransationDetails(pos: Int)
    }

}
