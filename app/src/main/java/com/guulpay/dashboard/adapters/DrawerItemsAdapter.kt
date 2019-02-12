package com.guulpay.dashboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.guulpay.R
import com.guulpay.customUiComponents.QuicksandMediumTextview
import com.guulpay.dashboard.pojo.DrawerItemsModel

class DrawerItemsAdapter(context: Context) : BaseAdapter() {

    var drawerItemsList: ArrayList<DrawerItemsModel>
    var context: Context

    init {
        this.context = context
        drawerItemsList = ArrayList()
    }

    fun addItems(drawerItemsModel: DrawerItemsModel) {
        drawerItemsList.add(drawerItemsModel)
    }

    override fun getView(pos: Int, rowView: View?, parent: ViewGroup?): View {

        var rowView = LayoutInflater.from(context).inflate(R.layout.drawer_item_row, parent, false)
        var drawerItemsModel = getItem(pos)
        var tvDrawerRow = rowView.findViewById(R.id.tvDrawerRow) as TextView
        var imgvwDrawerRow = rowView.findViewById(R.id.imgvwDrawerRow) as ImageView
        tvDrawerRow.text = drawerItemsModel.itemText
        imgvwDrawerRow.background = context.resources.getDrawable(drawerItemsModel.drawerIcon)

        return rowView
    }

    override fun getItem(pos: Int): DrawerItemsModel {
        return drawerItemsList.get(pos)
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getCount(): Int {
        return drawerItemsList.size
    }

}
