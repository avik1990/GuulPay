package com.guulpay.guulex.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.guulpay.R
import com.guulpay.customUiComponents.QuicksandMediumTextview
import com.guulpay.customUiComponents.QuicksandRegularTextview
import com.guulpay.guulex.model.ForexResponse

class GuulexListAdapter(val context: Context, private val forexlist: ArrayList<ForexResponse.Forexlist>) : RecyclerView.Adapter<GuulexListAdapter.MyViewHolder>() {

    lateinit var rowView: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.guulex_list_row, parent, false)
        rowView = itemView
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return forexlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder?.tvCurrencyUnit?.text = forexlist.get(position).baseCurrency
        holder?.tvCurrencyFullform?.text = forexlist.get(position).country
        holder?.tvCurrencyValue?.text = forexlist.get(position).sellingRate
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCurrencyUnit: QuicksandMediumTextview
        var tvCurrencyFullform: QuicksandRegularTextview
        var tvCurrencyValue: QuicksandMediumTextview

        init {
            tvCurrencyUnit = itemView.findViewById(R.id.tvCurrencyUnit)
            tvCurrencyFullform = itemView.findViewById(R.id.tvCurrencyFullform)
            tvCurrencyValue = itemView.findViewById(R.id.tvCurrencyValue)
        }

    }

}
