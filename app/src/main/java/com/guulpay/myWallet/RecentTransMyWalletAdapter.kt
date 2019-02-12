package com.guulpay.myWallet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.guulpay.R
import com.guulpay.customUiComponents.QuicksandMediumTextview

class RecentTransMyWalletAdapter : RecyclerView.Adapter<RecentTransMyWalletAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.getContext()).inflate(R.layout.recent_trans_mywallet_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        when (position) {
            0 -> {
                holder.tvShowDate.text = "1st Oct, 2018"
                holder.tvShowDate.visibility = View.VISIBLE
            }
            1 -> {
                holder.tvShowDate.text = "28th Aug, 2018"
                holder.tvShowDate.visibility = View.VISIBLE
            }
            4 -> {
                holder.tvShowDate.text = "22nd July, 2018"
                holder.tvShowDate.visibility = View.VISIBLE
            }
            else -> holder.tvShowDate.visibility = View.GONE
        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvShowDate: QuicksandMediumTextview

        init {
            tvShowDate = itemView.findViewById(R.id.tvShowDate)
        }

    }

}