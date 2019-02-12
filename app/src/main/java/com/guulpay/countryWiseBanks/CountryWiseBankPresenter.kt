package com.guulpay.countryWiseBanks

import android.content.Context
import com.guulpay.countryCodes.models.CountryWiseBankModel


class CountryWiseBankPresenter(private val context: Context, private val view: CountryWiseBanksContract.View) : CountryWiseBanksContract.Presenter {
    var jsonAsString: String? = ""
    var countryCodesList: ArrayList<CountryWiseBankModel> = ArrayList()
    // var fetchCountryCodesAsyncTask: CountryCodesPresenter.FetchCountryCodesAsynTask
    init {
        //   fetchCountryCodesAsyncTask = CountryCodesPresenter.FetchCountryCodesAsynTask()
    }

    override fun fetchcountryBanks() {
        //fetchCountryCodesAsyncTask.execute()
        countryCodesList.clear()
        countryCodesList.add(CountryWiseBankModel("OP Corporate Bank PLC", "123", ""))
        countryCodesList.add(CountryWiseBankModel("Nordea Bank", "123", ""))
        countryCodesList.add(CountryWiseBankModel("Danske Bank", "123", ""))
        countryCodesList.add(CountryWiseBankModel("Evli Bank PLC", "123", ""))
        countryCodesList.add(CountryWiseBankModel("Aktia Savings Bank", "123", ""))
        countryCodesList.add(CountryWiseBankModel("Bank of Aland", "123", ""))
        countryCodesList.add(CountryWiseBankModel("Alexander Corporate Finance Oy", "123", ""))
        countryCodesList.add(CountryWiseBankModel("Carnegie Investment Bank AB", "123", ""))
        countryCodesList.add(CountryWiseBankModel("BNP Paribas Fortis", "123", ""))
        countryCodesList.add(CountryWiseBankModel("POP Bank Group", "123", ""))
        view.countryBankcsFetched(countryCodesList)
    }

    override fun start() {
        view.setPresenter(this)
        //jsonAsString = loadJSONFromAsset()
    }

    override fun stop() {
        /*if (!fetchCountryCodesAsyncTask.isCancelled)
            fetchCountryCodesAsyncTask.cancel(true)*/
    }

    /*private fun loadJSONFromAsset(): String? {
        var json: String? = ""
        try {
            val inputStream = context.getAssets().open(context.resources.getString(R.string.countryCodesPath))
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            view.showSomeErrorOccurredMsg(ex.toString())
        }

        return json
    }*/

    /* inner class FetchCountryCodesAsynTask : AsyncTask<Void, Void, String>() {
         override fun doInBackground(vararg p0: Void): String {
             val listType = object : TypeToken<ArrayList<CountryCodesModel>>() {}.type
             countryCodesList = GsonBuilder().create().fromJson(loadJSONFromAsset(), listType)
             return ""
         }

         override fun onPostExecute(result: String?) {
             view.countryCodesFetched(countryCodesList)
             Log.e("fetchCountryCodes", countryCodesList.size.toString())
         }

     }*/
}
