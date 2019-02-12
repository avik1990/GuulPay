package com.guulpay.payments.fetchContacts

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.guulpay.R
import com.guulpay.customUiComponents.QuicksandMediumTextview
import com.guulpay.customUiComponents.QuicksandRegularTextview
import java.util.*
import kotlin.collections.ArrayList


class ContactListAdapter(private val context: Context?, private val list: ArrayList<ContactsModel>, private val listener: OnContactListItemSelect) :
        BaseAdapter() {

    lateinit var contactsModel: ContactsModel
    var contactListForAdap: ArrayList<ContactsModel>
    var hashSet: HashSet<ContactsModel>
    var phoneUtil: PhoneNumberUtil? = null

    companion object {
        var selectedPosition: Int = -1
    }

    init {
        phoneUtil = PhoneNumberUtil.getInstance()
        contactListForAdap = ArrayList()
        hashSet = HashSet(list)
        contactListForAdap.addAll(hashSet)
        Collections.sort(contactListForAdap)
        Log.e("ContactListAdapter", hashSet.size.toString() + "\n" + contactListForAdap.size)
    }

    interface OnContactListItemSelect {
        fun getNumber(text: String)
    }

    // Filter Class
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        contactListForAdap.clear()
        if (charText.length == 0) {
            hashSet.clear()
            hashSet.addAll(list)
            contactListForAdap.clear()
            contactListForAdap.addAll(hashSet)
            Collections.sort(contactListForAdap)
            //Log.e("filter", contactListForAdap.size.toString() + "\n" + hashSet.size)
        } else {
            for (countryCodesModel in hashSet) {
                if (countryCodesModel.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    contactListForAdap.add(countryCodesModel)
                } else if (countryCodesModel.number.toLowerCase(Locale.getDefault()).contains(charText)) {
                    contactListForAdap.add(countryCodesModel)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun getView(pos: Int, rowView: View?, viewgroup: ViewGroup?): View {
        var rowView = LayoutInflater.from(context).inflate(R.layout.contact_list_row, viewgroup, false)
        contactsModel = getItem(pos)

        val tvLetters = rowView.findViewById(R.id.tvLetters) as QuicksandMediumTextview
        val tvContactName = rowView.findViewById(R.id.tvContactName) as QuicksandMediumTextview
        val tvContactNumber = rowView.findViewById(R.id.tvContactNumber) as QuicksandRegularTextview

        tvContactName?.text = contactListForAdap[pos].name
        if (contactListForAdap[pos].number.contains("+")) {
            val numberProto = phoneUtil!!.parse(contactListForAdap[pos].number, "")
            tvContactNumber?.text = numberProto.nationalNumber.toString()
        } else {
            tvContactNumber?.text = contactListForAdap[pos].number
        }
        tvLetters?.text = contactListForAdap.get(pos).name.substring(0, 1)

        rowView.setOnClickListener {
            selectedPosition = pos
            Log.e("ContactListAdapter", selectedPosition.toString() + "\n" + contactListForAdap.size)
            val text = contactListForAdap[selectedPosition].number
            listener.getNumber(text)
        }

        return rowView
    }

    override fun getItem(pos: Int): ContactsModel {
        return contactListForAdap.get(pos)
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getCount(): Int {
        return contactListForAdap.size
    }
}

