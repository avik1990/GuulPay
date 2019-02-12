package com.guulpay.guulexfinal.GuulexFinalFragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.guulpay.R
import com.guulpay.countryCodes.CountryCodesListActivity
import com.guulpay.customUiComponents.CustomEditText
import com.guulpay.fragments.BaseFragment
import com.guulpay.guulexfinal.CurrencyCodePresenter
import com.guulpay.guulexfinal.CurrencyListContract
import com.guulpay.guulexfinal.GuulexProceedToPay.GuulexProceedToPayActivity
import com.guulpay.guulexfinal.GuulexfinalActivity
import com.guulpay.guulexfinal.guulexforexadapter.GuulexForExListAdapter
import com.guulpay.guulexfinal.guulexforexlist.model.ForExListResponse
import com.guulpay.guulexfinal.models.CalculateAmountResponse
import com.guulpay.guulexfinal.models.CurrencyListResponse
import com.guulpay.guulexfinal.servicecall.CurrencyListRepositoryProvider
import com.guulpay.login.LoginActivity
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.guulex_add_cash_layout.*
import kotlinx.android.synthetic.main.guulex_final_buy_fragment.*
import android.widget.EditText
import android.R.attr.data
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.guulpay.guulexfinal.models.CurrencyCalculateAmount
import com.guulpay.guulexfinal.models.ForexCartBuyResponse
import kotlinx.android.synthetic.main.enter_amount_layout.*
import kotlinx.android.synthetic.main.nfc_activity_main.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat
import org.json.JSONException




class GuulexFinalBuyFragment : BaseFragment(), View.OnClickListener, CurrencyListContract.View {


    var currencyCode: String? = ""
    var flagfrom: Int = 0
    var spinnerid: Int = 0
    var countryCodeID: String = ""
    var selectedItemText: String = " "
    var i: Int = 0
    lateinit var guulexForExListAdapter: GuulexForExListAdapter
    var arrayListItemSelected: ArrayList<String> = arrayListOf()
    var CurrencyAmountArrayList: ArrayList<String> = arrayListOf()
    var items: Int = 0
    var inflater: LayoutInflater? = null
    var view = null

    //Initialisation of the Model to calculate Amount for sending Spinner Item and Amount to the Model
    lateinit var currerncyAmountArrayList: ArrayList<CurrencyCalculateAmount>
    //ArrayList which contains all the Total Amounts which we get from the API
    lateinit var calAmountTotal:ArrayList<Double>
    //Variable which contains the addition of Total Amount
    var total:Double=0.0
    var flag=0

    lateinit var currencyListAdd: ArrayList<CurrencyListResponse.CurrencyList>
    lateinit var currencyListPresenter: CurrencyCodePresenter
    var appData: AppData? = null
    var obj = JSONArray()
    var forexDataJson= JSONObject()


    lateinit var forexBuyDataCart: ArrayList<CalculateAmountResponse>

    companion object {
        const val CLASS_NAME = "GuulexFinalBuyFragment"
        fun newInstance(): Fragment {
            return GuulexFinalBuyFragment()
        }
    }

    // Init Resources
    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appData = AppData(context as GuulexfinalActivity, Constants.Keys._KeyCryptoPreference)
        currencyListPresenter = CurrencyCodePresenter(this, appData!!, CurrencyListRepositoryProvider.getCurrencyRepository(appData!!.remember_token))
        // Inflating the Resource XML guulex_add_cash_layout for the First Time
        inflater = LayoutInflater.from(context)
        var view = inflater!!.inflate(R.layout.guulex_add_cash_layout, null)
        llGuulexAddCash.addView(view, items)

        // Adding the Base Symbol to the Total Amount and Total Amount with Advance Payment
        tvCurrencySymbol.setText(appData!!.currency_symbol)
        tvCurrencySymbolBottom.setText(appData!!.currency_symbol)
    }


    override fun initListeners() {
        //API call for the Foreign Exchange Data
        currencyListPresenter?.callForExApi(appData!!.register_currency_code)
        //API Call for the CurrencyList
        currencyListPresenter?.callCurrencyListApi(appData!!.register_currency_code)

        tvspinnerCountryCode.setOnClickListener(this)

        //Add Cash Listener On Click
        tvAddCash.setOnClickListener(this)

        //Initialing the ArrayList which will contain all the Calculated Amount
        calAmountTotal = ArrayList<Double>()

        //Calculate Amount On Click
        tvCalculateAmount.setOnClickListener(this)

        //Adding the First Value from the Edit Text of Guulex Amount to the Array List CurrencyAmountArrayList
        currerncyAmountArrayList = ArrayList<CurrencyCalculateAmount>()
        var currencyCalculateAmount = CurrencyCalculateAmount()
        currencyCalculateAmount.currencyAmount = ""
        currencyCalculateAmount.listItemSelected = ""
        currerncyAmountArrayList.add(currencyCalculateAmount)



        //On Text Change Listener of the First Edit Text of Guulex Amount
        edtAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                tvCalculateAmount.isClickable=true
                currerncyAmountArrayList[0].currencyAmount = s.toString()
 //               Log.v("currencyat0",currerncyAmountArrayList[0].currencyAmount)
//                if(currerncyAmountArrayList[0].currencyAmount==null){
//                    tvCalculateAmount.isClickable=false
//                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        val adapter_spinner_paymentType = ArrayAdapter.createFromResource(context,
                R.array.paymentType, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter_spinner_paymentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinnerSelectType.adapter = adapter_spinner_paymentType


        val adapter_spinner_currency = ArrayAdapter.createFromResource(context,
                R.array.currencyType, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter_spinner_currency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinnerSelectCurrency.adapter = adapter_spinner_currency

        //Proceed to the Guulex Advance Page
        btProceed.setOnClickListener(this)

        //Making the Calculate Amount Button Clickable False for Validation
        //Important
       // tvCalculateAmount.isClickable=false

        //

        forexBuyDataCart=ArrayList<CalculateAmountResponse>()
    }


    // Currency API List View for the Drop Down Spinner of Currency List
    override fun getCurrencyCodeArray(currencyList: ArrayList<CurrencyListResponse.CurrencyList>) {
        //Populating the same spinner to the other Spinner after Add Cash Button
        currencyListAdd = currencyList
        // Spinner Id Getting Changed Dynamically
        spinnerSelectCurrency.id = items
        val arraysize = currencyList!!.size + 1
        val countryarray = Array(arraysize) { "it = $it" }
        //Adding the default value
        countryarray[0] = "Select Currency"
        for (i in currencyList.indices) {
            countryarray[i + 1] = currencyList[i].country!!

        }
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, countryarray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        Log.v("idofspinnerabove", spinnerid.toString())
        Log.v("CurrencyList", currencyList.toString())
        spinnerSelectCurrency.adapter = adapter

        //............spinner item selected listener........
        spinnerSelectCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                var amount = if (pos != 0) {
                    //Adding +1 value
                    selectedItemText = currencyList[pos - 1].currencykey.toString()
                    Utils.showToast(context, selectedItemText)

                    //Adding the First Spinner Value to the Array List for the Calculate Amount API
                    currerncyAmountArrayList[0].listItemSelected = selectedItemText
                    selectedItemText
                } else {
                    ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }

        }

    }


    override fun getLayoutView(): Int {
        return R.layout.guulex_final_buy_fragment
    }

    // All the On Click
    override fun onClick(v: View?) {
      //  obj=JSONArray()
        // Proceed Button Function
        if (v == btProceed) {
            Log.v("data_forex",forexBuyDataCart.size.toString())
           // obj=JSONArray()
            //forexBuyDataCart=ArrayList<CalculateAmountResponse>()
           // for(i in 0 until forexBuyDataCart.size) {
              //  Log.v("forexArrayist", forexBuyDataCart[i].originalRate)

               // var forexBuyDataCartProceed = CalculateAmountResponse()
               // forexBuyDataCartProceed.finalAmountCommissionRate

                try {
                    for (j in 0 until forexBuyDataCart.size) {
                        // 1st object
                        Log.v("forexArrayist098",forexBuyDataCart[j].finalAmountNormalRate.toString())
                        Log.v("forexArrayist098",forexBuyDataCart[j].originalRate)
                        Log.v("forexArrayist098",forexBuyDataCart[j].rateWithAdminPercent.toString())
                        Log.v("forexArrayist098",forexBuyDataCart[j].finalAmountCommissionRate.toString())
                        Log.v("forexArrayist098",currerncyAmountArrayList[j].currencyAmount.toString())
                        Log.v("forexArrayist098",currerncyAmountArrayList[j].listItemSelected.toString())

                        forexDataJson= JSONObject()
                        forexDataJson.put("forex_transaction_type_id","1")
                        forexDataJson.put("booking_currency",currerncyAmountArrayList[j].listItemSelected)
                        forexDataJson.put("original_rate",forexBuyDataCart[j].originalRate)
                        forexDataJson.put("rate_with_admin_percent",forexBuyDataCart[j].rateWithAdminPercent)
                        forexDataJson.put("final_amount_normal_rate",forexBuyDataCart[j].finalAmountNormalRate)
                        forexDataJson.put("final_amount_commission_rate",forexBuyDataCart[j].finalAmountCommissionRate)
                        forexDataJson.put("amount",currerncyAmountArrayList[j].currencyAmount)
                        obj.put(forexDataJson)

                    }
                    Log.v("JsonArrayData",obj.toString())
//                    //val data = ArrayList()
//
//                    val jsonArray = jsonObject.getJSONArray("data")
//
//                    for (i in 0 until jsonArray.length()) {
//                        data.add(Model(/*fill your data*/))
//                    }
                } catch (e1: JSONException) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace()
                }
            //    currencyListPresenter?.callForexCartBuyApi(0,appData!!.user_id, appData!!.register_currency_code,appData!!.register_country_code,obj)

           // }
           //


            val intent = Intent(activity, GuulexProceedToPayActivity::class.java)
            startActivity(intent)
        }
        //Spinner Selection On Click Function
        else if (v == tvspinnerCountryCode) {
            // tvCountryCode.visibility=GONE
            val intent = Intent(activity, CountryCodesListActivity::class.java)
            startActivityForResult(intent, Constants.RequestCodes.REQUEST_CODE_COUNTRY)
        }
        //Add Cash On Click Function
        else if (v == tvAddCash) {

                var currencyCalculateAmount = CurrencyCalculateAmount()
                currerncyAmountArrayList.add(currencyCalculateAmount)

                var edtAmountValue: String = edtAmount.text.toString()

                CurrencyAmountArrayList.add(edtAmountValue)
                arrayListItemSelected.add(selectedItemText)

                var CurrencyCodeArrayList: ArrayList<String> = arrayListOf()

                for (i in CurrencyCodeArrayList.indices) {
                    CurrencyCodeArrayList.add(selectedItemText)
                    Log.v("currencyCodeListItem", CurrencyCodeArrayList[i].toString())
                }

                items++
                var view = inflater!!.inflate(R.layout.guulex_add_cash_layout, null)

                //creating new object of Spinner from the dynamic IDs for getting the data
                //Important for getting data inside new Object

                var edGuulexAmountId = view.findViewById<CustomEditText>(R.id.edtAmount)
                var spinner = view.findViewById<Spinner>(R.id.spinnerSelectCurrency)
                // Assigning Dynamic IDs of the Spinner
                spinner.id = items
                // Assigning Dynamic IDs of the Edit Text
                edGuulexAmountId.id = items
                // Setting Calculate Amount Button Clickable to False
                tvCalculateAmount.isClickable=true
//                if(v==tvCalculateAmount){
//                    Utils.showToast(context,"Please FillUp the Am")
//                }
                // On Text Change Listener for the Dynamic Generated Edit Text
                edGuulexAmountId.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {
                        // Check for the Values inside the Dynamic generated Edit Text for the Validation
                        if(CurrencyAmountArrayList.size>0){
                            // if the Edit Text Contains the value then make the Calculate Amount Clickable
                            tvCalculateAmount.isClickable=true
                            Log.v("textId-->", edGuulexAmountId.id.toString())
                            // Adding the Dynamic generated Edit Text Value to the Array List for Calculate Amount
                            CurrencyAmountArrayList.add(s.toString())
                            Log.v("CurrencyAmount", CurrencyAmountArrayList.toString())
                            Log.v("edTextChangeValueafter", s.toString())
                            currerncyAmountArrayList[edGuulexAmountId.id].currencyAmount = s.toString()
                        }
                    }

                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        Log.v("onChanged", s.toString())

                    }
                })


                Log.v("GuulexAmountEdvalue", edGuulexAmountId.text.toString())

                Log.v("GuulexAmountEd", edGuulexAmountId.toString())
                Log.v("Currencyitem", spinner.id.toString())

                val arraysize = currencyListAdd!!.size + 1

                val countryarray = Array(arraysize) { "it = $it" }

                countryarray[0] = "Select Currency"


                for (i in currencyListAdd.indices) {
                    countryarray[i + 1] = currencyListAdd[i].country!!

                }
                Log.v("CountryArray", countryarray.size.toString())
                val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, countryarray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter

                llGuulexAddCash.addView(view)
                // On Click of the Dynamic Generated Spinner
                //............spinner item selected listener........
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                        var amount = if (pos != 0) {
                            //Adding +1 value
                            selectedItemText = currencyListAdd[pos - 1].currencykey.toString()
                            Utils.showToast(context, selectedItemText)
                            // Adding the Value of the Dynamic Generated Spinner to the Array List for Calculate Amount
                            currerncyAmountArrayList[spinner.id].listItemSelected = selectedItemText

                            selectedItemText
                        } else {
                            ""
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        //Another interface callback
                    }

                }


        }
        // On click of the Calculated Amount Button Function
        else if (v == tvCalculateAmount) {
            forexDataJson= JSONObject()
           // obj=JSONArray()
           // obj.remove()
            for(j in 0 until currerncyAmountArrayList.size){
                Log.v("amountCurrent1", currerncyAmountArrayList[j].currencyAmount + " " + currerncyAmountArrayList[j].listItemSelected)
                if(currerncyAmountArrayList[j].currencyAmount == "" || currerncyAmountArrayList[j].listItemSelected == ""){
                    flag=1
                    Utils.showSnackbar(svguulexbuyfragment, "Please Select Currency or Fill Up Currency Amount", 10000)
                }
                else{
                    flag=0
                }
            }
            if(flag ==0) {
                for (i in 0 until currerncyAmountArrayList.size) {

                        Log.v("testCalAPI", "inside Else")
                        currencyListPresenter?.callCalculateAmountApi(appData!!.register_currency_code, currerncyAmountArrayList[i].listItemSelected, currerncyAmountArrayList[i].currencyAmount, "0")
                    // Important
                        // Clear the Array List which contains the value of all the Edit Text generated
                        // This will help in calling the API in Loops
                        calAmountTotal.clear()
                        total = 0.0
                        flag = 1
                    }

            }
            }

    }


    override fun getForExListArray(forExList: ArrayList<ForExListResponse.ForExList>) {
        if (forExList != null) {
            dummy_layout_forex1.visibility = View.GONE
            main_forex.visibility = View.VISIBLE
            rvForExList.visibility = View.VISIBLE
            rvForExList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            rvForExList.itemAnimator = DefaultItemAnimator()
            svguulexbuyfragment.smoothScrollTo(0, 0)
        } else {
            dummy_layout_forex1.visibility = View.VISIBLE

        }
        guulexForExListAdapter = GuulexForExListAdapter(context!!, forExList)
//        rvForExList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        rvForExList.itemAnimator = DefaultItemAnimator()
        rvForExList.adapter = guulexForExListAdapter
    }


    override fun getCalCulatedAmount(calAmount: CalculateAmountResponse) {

       // forexBuyDataCart.clear()
        Log.v("totalAmount",calAmount.finalAmountCommissionRate.toString())


        var forexBuyDataCart1 = CalculateAmountResponse()
        forexBuyDataCart1.originalRate = calAmount.originalRate
        forexBuyDataCart1.finalAmountCommissionRate = calAmount.finalAmountCommissionRate
        forexBuyDataCart1.rateWithAdminPercent=calAmount.rateWithAdminPercent
        forexBuyDataCart1.finalAmountNormalRate= calAmount.finalAmountNormalRate
        Log.v("size_array_list",forexBuyDataCart.size.toString())


        forexBuyDataCart.add(forexBuyDataCart1)



        for(i in 0 until forexBuyDataCart.size) {
            Log.v("forexArrayist123", forexBuyDataCart[i].originalRate)
            Log.v("forexArrayist123",forexBuyDataCart[i].finalAmountCommissionRate.toString())
            Log.v("forexArrayist123",forexBuyDataCart[i].finalAmountNormalRate.toString())
            Log.v("forexArrayist123",forexBuyDataCart[i].rateWithAdminPercent.toString())


        }

        total=0.0
        handleProgressAlert(true)
        calAmountTotal.add(calAmount.finalAmountCommissionRate!!)
        Log.v("callAmountTotal",calAmountTotal.toString())
        for( i in 0 until calAmountTotal.size){
            Log.v("Cal_Amount_size",calAmountTotal.size.toString())
            total= total +calAmountTotal[i]
            Log.v("totalfinalAmount",total.toString())
            val df = DecimalFormat("#.###")
            df.roundingMode = RoundingMode.CEILING

            val totalWithTwoDecimals=df.format(total)
            handleProgressAlert(false)
            tvAmountTotal.text=totalWithTwoDecimals.toString()

            var percentAdd =total+(total*2)/100
            Log.v("percentVal",percentAdd.toString())

            val df1 = DecimalFormat("#.###")
            df1.roundingMode = RoundingMode.CEILING

            val totalWithTwoDecimalsPercent=df.format(percentAdd)
            tvAmountTotalWithPercentAdd.text=totalWithTwoDecimalsPercent.toString()
        }


    }



    override fun goToNextPage() {
        //TODO: implement later
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return isAdded
    }

    override fun setPresenter(presenter: CurrencyListContract.Presenter) {
        currencyListPresenter = presenter as CurrencyCodePresenter


    }

    private val loader by lazy {
        LoaderDialog(context)
    }

    //Handler
    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }


    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        //TODO: implement later
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        //TODO: implement later
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.RequestCodes.REQUEST_CODE_COUNTRY) {
            flagfrom = Constants.RequestCodes.REQUEST_CODE_COUNTRY
            try {
                val countryCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE)
                //countryCode?.let { tvspinnerCountryCode.text = countryCode }
                countryCodeID = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODEID)!!
                countryCode?.let { Log.d("CountryCodeID", countryCodeID) }

                currencyCode = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CURRENCYCODE)!!
                currencyCode?.let { Log.d("currencyCode", currencyCode) }

                //Added Rishabh 28/1/2019 */

                val countryCodeName = data?.getStringExtra(Constants.Keys.KEY_SELECTED_COUNTRY_CODE_NAME)
                countryCodeName?.let { tvspinnerCountryCode.text = countryCodeName }
            } catch (e: Exception) { }
        }
    }

    override fun globalLogout() {
        appData!!.cleardata(context!!, Constants.Keys._KeyCryptoPreference)
        val intent = Intent(context, LoginActivity::class.java)
        activity!!.startActivity(intent)
        activity!!.finishAffinity()
    }

}


