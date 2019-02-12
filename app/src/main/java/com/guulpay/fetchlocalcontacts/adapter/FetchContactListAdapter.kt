package com.guulpay.fetchlocalcontacts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.guulpay.R
import com.guulpay.customUiComponents.QuicksandMediumTextview
import com.guulpay.customUiComponents.QuicksandRegularTextview
import com.guulpay.payments.fetchContacts.ContactsModel
import java.util.*
import kotlin.collections.ArrayList


class FetchContactListAdapter(private val context: Context, private val countryList: ArrayList<ContactsModel>,
                              private val countryCodesItemSelect: OnCountryCodesItemSelect) : BaseAdapter() {

    var countryCodesListForAdap: ArrayList<ContactsModel>
    lateinit var countryCodesModel: ContactsModel

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
        var rowView = LayoutInflater.from(context).inflate(R.layout.country_codelist_row, parent, false)
        countryCodesModel = getItem(pos)

        val tvCountryNames = rowView.findViewById(R.id.tvCountryNames) as QuicksandRegularTextview
        val tvCountryCode = rowView.findViewById(R.id.tvCountryCode) as QuicksandMediumTextview

        tvCountryCode.text = countryCodesModel.name
        tvCountryNames.text = countryCodesModel.number

        rowView.setOnClickListener {
            selectedPosition = pos
            /* val text = countryCodesListForAdap.get(selectedPosition).callprefix
             countryCodesItemSelect.getText(text!!,countryCodesListForAdap.get(selectedPosition).id!!)*/
        }
        return rowView
    }

    override fun getItem(pos: Int): ContactsModel {
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
                /*if (countryCodesModel.name!!.toLowerCase(Locale.getDefault()).contains(charText)) {
                    countryCodesListForAdap.add(countryCodesModel)
                } else if (countryCodesModel.callprefix!!.toLowerCase(Locale.getDefault()).contains(charText)) {
                    countryCodesListForAdap.add(countryCodesModel)
                }*/
            }
        }
        notifyDataSetChanged()
    }
}
