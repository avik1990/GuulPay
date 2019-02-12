package com.guulpay.operatorList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.guulpay.R
import com.guulpay.customUiComponents.CircleImageView
import com.guulpay.customUiComponents.QuicksandMediumTextview
import com.guulpay.operatorList.models.OperatorModel
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class OperatorListAdapter(private val context: Context, private val countryList: ArrayList<OperatorModel.Operatorlist>, private val countryCodesItemSelect: OnCountryCodesItemSelect) : BaseAdapter() {

    var countryCodesListForAdap: ArrayList<OperatorModel.Operatorlist>
    lateinit var OperatorModel: OperatorModel.Operatorlist

    companion object {
        var selectedPosition: Int = -1
    }

    init {
        countryCodesListForAdap = ArrayList()
        countryCodesListForAdap.addAll(countryList)
    }

    interface OnCountryCodesItemSelect {
        fun getText(text: String, country_code: String)
    }

    override fun getView(pos: Int, v: View?, parent: ViewGroup?): View {
        var rowView = LayoutInflater.from(context).inflate(R.layout.operator_list_row, parent, false)
        OperatorModel = getItem(pos)

        val tvCountryNames = rowView.findViewById(R.id.tvCountryNames) as QuicksandMediumTextview
        val imgvwBankIcon = rowView.findViewById(R.id.imgvwBankIcon) as CircleImageView

        if(!OperatorModel.operator_logo.isNullOrBlank()) {
            Picasso.get().load(OperatorModel.operator_logo)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(imgvwBankIcon)
        }


        tvCountryNames.text = OperatorModel.operator

        rowView.setOnClickListener {
            selectedPosition = pos
            val text = countryCodesListForAdap.get(selectedPosition).operator
            countryCodesItemSelect.getText(text!!, countryCodesListForAdap.get(selectedPosition).operatorId!!)
        }
        return rowView
    }

    override fun getItem(pos: Int): OperatorModel.Operatorlist {
        return countryCodesListForAdap.get(pos)
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getCount(): Int {
        return countryCodesListForAdap.size
    }

    // Filter Class
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        countryCodesListForAdap.clear()
        if (charText.length == 0) {
            countryCodesListForAdap.addAll(countryList)
        } else {
            for (OperatorModel in countryList) {
                if (OperatorModel.operator!!.toLowerCase(Locale.getDefault()).contains(charText)) {
                    countryCodesListForAdap.add(OperatorModel)

                }
            }
            notifyDataSetChanged()
        }
    }
}
