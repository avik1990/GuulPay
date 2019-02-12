package com.guulpay.addReceiver.reciever

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.guulpay.R
import com.guulpay.customUiComponents.CustomSwipeOnTouch

class RecieverListAdapter: RecyclerView.Adapter<RecieverListAdapter.MyViewHolder>() {

    lateinit var rowView: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.getContext())
                .inflate(R.layout.recivers_row, parent, false)

        rowView = itemView
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /* Swipe listener to view Download option on Left swipe,
              * And, Hide on Right swipe */
        rowView.setOnTouchListener(object : CustomSwipeOnTouch() {
            override fun onClick() {

            }

            override fun onSwipeLeft() {
                holder.rlEditDel.visibility = View.VISIBLE
            }

            override fun onSwipeRight() {
                holder.rlEditDel.visibility = View.GONE
            }

        })
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var rlEditDel: RelativeLayout
        var imgvwBankIcon:ImageView

        init {
            rlEditDel = itemView.findViewById(R.id.rlEditDel)
            imgvwBankIcon = itemView.findViewById(R.id.imgvwBankIcon)
        }

    }

}
