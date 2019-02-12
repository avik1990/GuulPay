package com.guulpay.payments.fetchContacts

import android.content.ContentResolver
import android.database.Cursor
import android.os.AsyncTask
import android.provider.ContactsContract

class GetContactsInteractorImpl(private val contentResolver: ContentResolver) : GetContactsInteractor {

    var list: ArrayList<ContactsModel>
    lateinit var phonesCursor: Cursor
    lateinit var onFinishedListener: GetContactsInteractor.OnFinishedListener

    init {
        list = ArrayList()
    }

    override fun getContactsList(onFinishedListener: GetContactsInteractor.OnFinishedListener) {

        this.onFinishedListener = onFinishedListener
        FetchContactsAsynTask().execute()
    }

    inner class FetchContactsAsynTask : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg p0: Void?): String {
            phonesCursor = contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null)
            while (phonesCursor.moveToNext()) {
                var name: String = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phoneNumber: String = phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                list.add(ContactsModel(name, phoneNumber.replace("\\s".toRegex(), "")))
            }
            phonesCursor.close()
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            onFinishedListener.onFinished(list)
        }

    }
}