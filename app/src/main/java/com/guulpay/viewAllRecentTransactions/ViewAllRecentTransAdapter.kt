package com.guulpay.viewAllRecentTransactions

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.guulpay.R
import com.guulpay.customUiComponents.QuicksandMediumTextview

class ViewAllRecentTransAdapter: RecyclerView.Adapter<ViewAllRecentTransAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.getContext())
                .inflate(R.layout.viewall_recent_transactions_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == 0){
            holder.tvShowDateTransHistory.text = "31st Aug 2018"
            holder.tvShowDateTransHistory.visibility = View.VISIBLE
        }
        else  if (position == 1){
            holder.tvShowDateTransHistory.text = "28th Aug 2018"
            holder.tvShowDateTransHistory.visibility = View.VISIBLE
        }
        else  if (position == 9){
            holder.tvShowDateTransHistory.text = "28th July 2018"
            holder.tvShowDateTransHistory.visibility = View.VISIBLE
        }
        else{
            holder.tvShowDateTransHistory.visibility = View.GONE
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgvwTransHistory: ImageView
        var tvTransHistoryDetails: TextView
        var tvTransHistoryId: TextView
        var tvShowDateTransHistory: TextView

        init {
            tvShowDateTransHistory = itemView.findViewById(R.id.tvShowDateTransHistory)
            imgvwTransHistory = itemView.findViewById(R.id.imgvwTransHistory)
            tvTransHistoryDetails = itemView.findViewById(R.id.tvTransHistoryDetails)
            tvTransHistoryId = itemView.findViewById(R.id.tvTransHistoryId)

        }

    }

}