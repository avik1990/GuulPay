package com.guulpay.countryCodes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.guulpay.R
import com.guulpay.countryCodes.models.CountryWiseBankModel
import com.guulpay.customUiComponents.QuicksandMediumTextview
import com.guulpay.customUiComponents.QuicksandRegularTextview
import java.util.*
import kotlin.collections.ArrayList


class CountryWiseBankListAdapter(private val context: Context, private val bankList:ArrayList<CountryWiseBankModel>,
                                 private val countryCodesItemSelect: OnCountryCodesItemSelect) : BaseAdapter(){

    var countryCodesListForAdap: ArrayList<CountryWiseBankModel>
    lateinit var bankCodesModel:CountryWiseBankModel

    companion object {
        var selectedPosition: Int = -1
    }
    init {
        countryCodesListForAdap = ArrayList()
        countryCodesListForAdap.addAll(bankList)
    }

    interface OnCountryCodesItemSelect{
        fun getText(text:String)
    }

    override fun getView(pos: Int, v: View?, parent: ViewGroup?): View {
        var rowView = LayoutInflater.from(context).inflate(R.layout.countrywise_banklist_row, parent, false)
        bankCodesModel = getItem(pos)

        val tvCountryNames = rowView.findViewById(R.id.tvCountryNames) as QuicksandRegularTextview
        val tvCountryCode = rowView.findViewById(R.id.tvCountryCode) as QuicksandMediumTextview

        tvCountryCode.text = bankCodesModel.bankCode
        tvCountryNames.text = bankCodesModel.bankname

        rowView.setOnClickListener {
            selectedPosition = pos
            val text = countryCodesListForAdap.get(selectedPosition).bankname
            countryCodesItemSelect.getText(text)
        }
        return rowView
    }

    override fun getItem(pos: Int): CountryWiseBankModel {
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
            countryCodesListForAdap.addAll(bankList)
        } else {
            for (bankCodesModel in bankList) {
                if (bankCodesModel.bankname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    countryCodesListForAdap.add(bankCodesModel)
                }
                else if (bankCodesModel.bankname.toLowerCase(Locale.getDefault()).contains(charText)){
                    countryCodesListForAdap.add(bankCodesModel)
                }
            }
        }
        notifyDataSetChanged()
    }
}
