package com.guulpay.countryCodes.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.guulpay.R
import com.guulpay.countryCodes.models.CountryCodesModel
import com.guulpay.customUiComponents.QuicksandMediumTextview
import com.guulpay.customUiComponents.QuicksandRegularTextview
import java.util.*
import kotlin.collections.ArrayList


class CountryListAdapter(private val context: Context, private val countryList: ArrayList<CountryCodesModel.Prefixlist>,
                         private val countryCodesItemSelect: OnCountryCodesItemSelect) : BaseAdapter() {

    var countryCodesListForAdap: ArrayList<CountryCodesModel.Prefixlist>
    lateinit var countryCodesModel: CountryCodesModel.Prefixlist

    companion object {
        var selectedPosition: Int = -1
    }

    init {
        countryCodesListForAdap = ArrayList()
        countryCodesListForAdap.addAll(countryList)
    }

    interface OnCountryCodesItemSelect {

        /*Edited 25/1/2019 rishabh*/
        fun getText(text: String, country_code: String, currency_code: String,countryCodeName:String)

    }

    override fun getView(pos: Int, v: View?, parent: ViewGroup?): View {
        var rowView = LayoutInflater.from(context).inflate(R.layout.country_codelist_row, parent, false)
        countryCodesModel = getItem(pos)

        val tvCountryNames = rowView.findViewById(R.id.tvCountryNames) as QuicksandRegularTextview
        val tvCountryCode = rowView.findViewById(R.id.tvCountryCode) as QuicksandMediumTextview

        tvCountryCode.text = countryCodesModel.callprefix
        tvCountryNames.text = countryCodesModel.name

        rowView.setOnClickListener {
            selectedPosition = pos
            val text = countryCodesListForAdap.get(selectedPosition).callprefix
            Log.v("CountryName",countryCodesListForAdap.get(selectedPosition).name)



            /*Edited 25/1/2019 rishabh*/
            countryCodesItemSelect.getText(text!!, countryCodesListForAdap.get(selectedPosition).id!!,countryCodesListForAdap.get(selectedPosition).currency_code!!,countryCodesListForAdap.get(selectedPosition).name!!)

        }
        return rowView
    }

    override fun getItem(pos: Int): CountryCodesModel.Prefixlist {
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
            for (countryCodesModel in countryList) {
                if (countryCodesModel.name!!.toLowerCase(Locale.getDefault()).contains(charText)) {
                    countryCodesListForAdap.add(countryCodesModel)
                } else if (countryCodesModel.callprefix!!.toLowerCase(Locale.getDefault()).contains(charText)) {
                    countryCodesListForAdap.add(countryCodesModel)
                }
            }
        }
        notifyDataSetChanged()
    }
}
