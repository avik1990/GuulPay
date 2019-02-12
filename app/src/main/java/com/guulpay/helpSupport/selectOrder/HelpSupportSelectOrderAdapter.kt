package com.guulpay.helpSupport.selectOrder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.customUiComponents.QuicksandMediumTextview
import com.guulpay.helpSupport.submitReport.HelpSupportSubmitFragment
import com.guulpay.others.Utils

class HelpSupportSelectOrderAdapter(private val context: DashboardHomeActivity?): RecyclerView.Adapter<HelpSupportSelectOrderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_support_selectorder_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {

        holder.tvSelectOrder.setOnClickListener {
            Utils.replaceFragmentInActivityFadeAnimation(context?.supportFragmentManager, HelpSupportSubmitFragment.newInstance(),
                    R.id.flFragmentContainerDashboard, true, HelpSupportSubmitFragment.CLASS_NAME)
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvSelectOrder: TextView
        init {
            tvSelectOrder = itemView.findViewById(R.id.tvSelectOrder)
        }

    }
}