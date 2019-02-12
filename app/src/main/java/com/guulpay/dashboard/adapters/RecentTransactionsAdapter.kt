package com.guulpay.dashboard.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.guulpay.R

class RecentTransactionsAdapter: RecyclerView.Adapter<RecentTransactionsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.recent_transactions_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return 5
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {

        }

    }

}
