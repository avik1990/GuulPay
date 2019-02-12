package com.guulpay.addMoney.completeYourPayment

import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.guulpay.R
import com.guulpay.customUiComponents.CustomEditText
import com.guulpay.customUiComponents.CustomTextWatcher
import com.guulpay.customUiComponents.QuicksandMediumTextview

class SavedCardsAddmoneyAdapter : RecyclerView.Adapter<SavedCardsAddmoneyAdapter.MyViewHolder>() {

    companion object {
        var selectedPosition = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.getContext())
                .inflate(R.layout.saved_cardslist_addmoney_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (position == selectedPosition) {
            expandCardLayout(holder)
        } else {
            collapseCardLayout(holder)
        }

        CustomTextWatcher(holder.etCvvno, object : CustomTextWatcher.TextWatcherListener {
            override fun onType(text: String, length: Int) {
                if (length>=3){
                    holder.tvPayNow.isEnabled = true
                    holder.tvPayNow.alpha = 1.0F
                }
                else{
                    holder.tvPayNow.isEnabled = false
                    holder.tvPayNow.alpha = 0.5F
                }
            }
        })


        holder.radioButton.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    private fun expandCardLayout(holder: SavedCardsAddmoneyAdapter.MyViewHolder) {
        holder.radioButton.isChecked = true
        holder.tvDeleteCard.visibility = View.VISIBLE
        holder.llCvv.visibility = View.VISIBLE
        holder.tvPayNow.visibility = View.VISIBLE
    }

    private fun collapseCardLayout(holder: SavedCardsAddmoneyAdapter.MyViewHolder) {
        holder.radioButton.isChecked = false
        holder.tvDeleteCard.visibility = View.GONE
        holder.llCvv.visibility = View.GONE
        holder.tvPayNow.visibility = View.GONE
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var radioButton: AppCompatRadioButton
        var llCvv: LinearLayout
        var tvPayNow: QuicksandMediumTextview
        var tvDeleteCard: QuicksandMediumTextview
        var etCvvno: CustomEditText

        init {
            radioButton = itemView.findViewById(R.id.radioBtSavedCardsRow)
            llCvv = itemView.findViewById(R.id.llCvvSavedCardsRow)
            tvPayNow = itemView.findViewById(R.id.tvPayNowFromSavedCrdRow)
            tvDeleteCard = itemView.findViewById(R.id.tvDelSavedCardsRow)
            etCvvno = itemView.findViewById(R.id.etCvvno)
        }

    }
}