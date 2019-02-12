package com.guulpay.helpSupport.menu

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.guulpay.R
import com.guulpay.activity.DashboardHomeActivity
import com.guulpay.customUiComponents.QuicksandMediumTextview
import com.guulpay.helpSupport.selectOrder.HelpSupportSelectOrderFragment
import com.guulpay.others.Utils

class HelpSupportMenuAdapter(private val context: DashboardHomeActivity?) : RecyclerView.Adapter<HelpSupportMenuAdapter.MyViewHolder>() {

    var helpSupportMenuList: ArrayList<HelpSupportMenuModel>

    init {
        helpSupportMenuList = ArrayList()
    }

    fun addItems(helpSupportMenuModel: HelpSupportMenuModel) {
        helpSupportMenuList.add(helpSupportMenuModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_support_menu_row, parent, false)

        itemView.setOnClickListener {
            Utils.replaceFragmentInActivityFadeAnimation(context?.supportFragmentManager, HelpSupportSelectOrderFragment.newInstance(),
                    R.id.flFragmentContainerDashboard, true, HelpSupportSelectOrderFragment.CLASS_NAME)
        }
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return helpSupportMenuList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Log.e("onBindViewHolder",helpSupportMenuList.size.toString())
        holder.tvTitle.text = helpSupportMenuList.get(position).title
        holder.imgvwIcon.background = helpSupportMenuList.get(position).drawableIcon
        holder.tvDes.text = helpSupportMenuList.get(position).description
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView
        var imgvwIcon: ImageView
        var tvDes: TextView

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            imgvwIcon = itemView.findViewById(R.id.imgvwIcon)
            tvDes = itemView.findViewById(R.id.tvDes)
        }

    }
}