package com.guulpay.transaction.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.customUiComponents.CustomSwipeOnTouch
import com.guulpay.customUiComponents.QuicksandMediumTextview
import com.guulpay.others.Constants
import com.guulpay.payments.send.paymentStatus.SendMoneyPaymentStatus

class TransactionHistoryAdapter(private val listener: TransactionHistoryListener) : RecyclerView.Adapter<TransactionHistoryAdapter.MyViewHolder>() {

    lateinit var rowView: View

    interface TransactionHistoryListener {
        fun onItemClick(pos: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.getContext())
                .inflate(R.layout.transactions_history_row, parent, false)

        rowView = itemView

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (position == 0) {
            holder.tvShowDateTransHistory.text = "31st Aug 2018"
            holder.tvShowDateTransHistory.visibility = View.VISIBLE
        } else if (position == 1) {
            holder.tvShowDateTransHistory.text = "28th Aug 2018"
            holder.tvShowDateTransHistory.visibility = View.VISIBLE
        } else if (position == 9) {
            holder.tvShowDateTransHistory.text = "22nd July 2018"
            holder.tvShowDateTransHistory.visibility = View.VISIBLE
        } else {
            holder.tvShowDateTransHistory.visibility = View.GONE
        }

        holder.setIsRecyclable(false)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var flDownloadTransHistory: FrameLayout
        var imgvwTransHistory: ImageView
        var tvTransHistoryDetails: TextView
        var tvTransHistoryId: TextView
        var tvShowDateTransHistory: TextView

        init {
            tvShowDateTransHistory = itemView.findViewById(R.id.tvShowDateTransHistory)
            flDownloadTransHistory = itemView.findViewById(R.id.flDownloadTransHistory)
            imgvwTransHistory = itemView.findViewById(R.id.imgvwTransHistory)
            tvTransHistoryDetails = itemView.findViewById(R.id.tvTransHistoryDetails)
            tvTransHistoryId = itemView.findViewById(R.id.tvTransHistoryId)

            //    itemView.setOnClickListener(this)

            /* Swipe listener to view Download option on Left swipe,
            * And, Hide on Right swipe */
            itemView.setOnTouchListener(object : CustomSwipeOnTouch() {
                override fun onClick() {
                    listener.onItemClick(adapterPosition)
                }

                override fun onSwipeLeft() {
                    flDownloadTransHistory.visibility = View.VISIBLE
                    imgvwTransHistory.visibility = View.GONE
                }

                override fun onSwipeRight() {
                    flDownloadTransHistory.visibility = View.GONE
                    imgvwTransHistory.visibility = View.VISIBLE
                }

            })
        }
    }
}