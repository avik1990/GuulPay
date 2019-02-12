package com.guulpay.mobilerechargehistory

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.guulpay.R
import com.guulpay.mobilerecharge.model.TransactionHistResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.mobile_recharge_hist_row.view.*

class MobileHistRechargeAdapter(private val context: Context, private val countryCodesItemSelect: MobileHistRechargeAdapter.oNRechargeListClick) : RecyclerView.Adapter<MobileHistRechargeAdapter.MyViewHolder>() {

    lateinit var rowView: View
    private var rechargelist: MutableList<TransactionHistResponse.Rechargelist> = ArrayList()

    fun setData(rechargelistall: List<TransactionHistResponse.Rechargelist>) {
        rechargelist!!.clear()
        rechargelist.addAll(rechargelistall)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.mobile_recharge_hist_row, parent, false)

        rowView = itemView
        return MyViewHolder(itemView)
    }

    /*var businessList = rechargelist
        @Synchronized set(value) {
            field = value
            notifyDataSetChanged()
        }*/


    override fun getItemCount(): Int {
        return rechargelist!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(rechargelist!![holder.adapterPosition])
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(rechargelist: TransactionHistResponse.Rechargelist) {
            itemView?.tvMobileNo?.text = rechargelist.mobileRechargePhoneNumber
            itemView?.tvRechargeDate?.text = "Last Recharge on " + rechargelist.createdAt
            try {
                val rechargeamount = "Amount: " + " <font color=#000000> " + rechargelist.currency_symbol +" </font>" + "" + rechargelist.mobileRechargeAmount
                //  getString(R.string.dontHaveAnAccount) + " <b><font color=#348C96> " + getString(R.string.signUp) + " </font></b>" + " here"
                itemView?.tvRechargeAmt?.setText(Html.fromHtml(rechargeamount))
            } catch (e: Exception) {
            }

            try {
                Picasso.get().load(rechargelist.logo)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(itemView?.imgvwBankIcon)
            } catch (e: Exception) {
                //  e.printStackTrace()
            }

            itemView?.btRepeat?.setOnClickListener {
                countryCodesItemSelect.gettransationDetails(adapterPosition)
            }
        }
    }

    interface oNRechargeListClick {
        fun gettransationDetails(pos: Int)
    }

}
