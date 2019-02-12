package com.guulpay.guulexfinal.guulexforexadapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.guulpay.R
import com.guulpay.countryCodes.models.CountryWiseBankModel
import com.guulpay.guulexfinal.CurrencyListContract
import com.guulpay.guulexfinal.GuulexFinalFragments.GuulexFinalBuyFragment
import com.guulpay.guulexfinal.guulexforexlist.model.ForExListResponse
import com.guulpay.mobilerecharge.model.TransactionHistResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.forex_layout_adapter.view.*
import kotlinx.android.synthetic.main.mobile_recharge_hist_row.view.*

class GuulexForExListAdapter(private val context: Context,private val forExList: ArrayList<ForExListResponse.ForExList>): RecyclerView.Adapter<GuulexForExListAdapter.MyViewHolder>() {

    lateinit var rowView:View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.forex_layout_adapter, parent, false)

      //  rowView = itemView
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return forExList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      //  Log.v("Array", viewContract.getForExListArray())
        holder.bindView(forExList!![holder.adapterPosition])

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(forExList: ForExListResponse.ForExList) {
            itemView?.tvForExName?.text = forExList.pairCurrency
            itemView?.tvForExBuy?.text=forExList.buyingRate
            itemView?.tvForExSell?.text=forExList.sellingRate

        }

    }
}
