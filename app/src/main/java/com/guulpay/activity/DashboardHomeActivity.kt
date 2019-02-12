package com.guulpay.activity

import android.content.Intent
import android.os.AsyncTask
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.guulpay.R
import com.guulpay.aboutus.AboutUsFragment
import com.guulpay.account.AccountFragment
import com.guulpay.basemodel.CurrenciesModel
import com.guulpay.contactus.ContactUsFragment
import com.guulpay.dashboard.DashboardMainContentFragment
import com.guulpay.dashboard.adapters.DrawerItemsAdapter
import com.guulpay.dashboard.pojo.DrawerItemsModel
import com.guulpay.helpSupport.menu.HelpSupportMenuFragment
import com.guulpay.helpSupport.selectOrder.HelpSupportSelectOrderFragment
import com.guulpay.helpSupport.submitReport.HelpSupportSubmitFragment
import com.guulpay.login.LoginActivity
import com.guulpay.more.MoreFragment
import com.guulpay.myProfile.view.ViewProfileFragment
import com.guulpay.myQrCode.MyQrCodeFragment
import com.guulpay.myWallet.MyWalletFragment
import com.guulpay.notification.NotificationFragment
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import com.guulpay.payments.PaymentActivity
import com.guulpay.transaction.MyTransactionFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottombar_layout.*
import kotlinx.android.synthetic.main.dashboard_home_activity.*
import kotlinx.android.synthetic.main.navigation_drawer.*
import kotlinx.android.synthetic.main.toolbar_dashboard_layout.*
import org.jetbrains.anko.alert
import java.io.IOException

/* This is the base activity for those fragments where the Top bar and the Bottom bar of the dashboard layout remains same
 * & Body changes accordingly */
class DashboardHomeActivity : BaseActivity(), View.OnClickListener, AdapterView.OnItemClickListener, DrawerLayout.DrawerListener {

    val TAG = "DashboardHomeActivity"
    lateinit var drawerItemsAdapter: DrawerItemsAdapter
    var appData: AppData? = null
    var currenciesModellist: ArrayList<CurrenciesModel> = ArrayList()

    private val loader by lazy {
        LoaderDialog(this)
    }

    override fun onBackPressed() {
        Utils.hideKeyboard(this)
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawer(Gravity.LEFT)
            } else if (!(supportFragmentManager.findFragmentById(R.id.flFragmentContainerDashboard) is DashboardMainContentFragment)) {
                /* Move to home screen on back press, if on different fragment */
                tvHomeBottomLayout.performClick()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun initPermissions() {
        Utils.checkPermissions(this)
    }

    override fun onClick(v: View?) {
        if (v == imgvwNotification) {
            val intent = Intent(this@DashboardHomeActivity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, NotificationFragment.CLASS_NAME)
            startActivity(intent)
        } else if (v == imgvwNavMenu) {
            drawerLayout.openDrawer(Gravity.LEFT)
        } else if (v == imgvwNavDrawerRightArrow) {
            drawerLayout.closeDrawers()
            val intent = Intent(this@DashboardHomeActivity, HomeActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_FRAGMENT_NAME, ViewProfileFragment.CLASS_NAME)
            startActivity(intent)
        } else if (v == llDrawerHeader) {
            imgvwNavDrawerRightArrow.performClick()
        } else if (v == imgvwProfPicDrawer) {
            imgvwNavDrawerRightArrow.performClick()
        }
        /* Bottom bar icons click */
        else if (v == tvHomeBottomLayout) {
            /* If not in DashboardMainContentFragment, then only redirect */
            if (!(supportFragmentManager.findFragmentById(R.id.flFragmentContainerDashboard) is DashboardMainContentFragment)) {
                setUpScreenUiForFragment(DashboardMainContentFragment.CLASS_NAME)
                Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, DashboardMainContentFragment.newInstance(),
                        R.id.flFragmentContainerDashboard, false, DashboardMainContentFragment.CLASS_NAME)
            }
        } else if (v == tvWalletBottomLayout) {
            setUpScreenUiForFragment(MyWalletFragment.CLASS_NAME)
            Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, MyWalletFragment.newInstance(),
                    R.id.flFragmentContainerDashboard, false, MyWalletFragment.CLASS_NAME)
        } else if (v == tvMoreBottomLayout) {
            setUpScreenUiForFragment(MoreFragment.CLASS_NAME)
            Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, MoreFragment.newInstance(),
                    R.id.flFragmentContainerDashboard, false, MoreFragment.CLASS_NAME)
        } else if (v == tvTransBottomLayout) {
            setUpScreenUiForFragment(MyTransactionFragment.CLASS_NAME)
            Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, MyTransactionFragment.newInstance(),
                    R.id.flFragmentContainerDashboard, false, MyTransactionFragment.CLASS_NAME)
        } else if (v == imgvwPaymentMiddleBottomBar) {
            val intent = Intent(this@DashboardHomeActivity, PaymentActivity::class.java)
            intent.putExtra(Constants.Keys.KEY_TAB_POSITION, Constants.Keys.PAY_MONEY_TAB_POSITION)
            startActivity(intent)
        }
    }

    private fun deselectAllBottomBarIcon() {
        tvHomeBottomLayout.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_deselect, 0, 0)
        tvWalletBottomLayout.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_my_wallet_deselect, 0, 0)
        tvMoreBottomLayout.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_more_icon_deselect, 0, 0)
        tvTransBottomLayout.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_transaction_deselect, 0, 0)
    }

    override fun onResume() {
        super.onResume()
        tvMobileCountryCode.text="+" +appData!!.country_call_prifix
        tvUserNameNavDrawer.text = appData!!.register_name
        tvUserMobileNo.text = appData!!.register_phone
        tvWalletAmt.text = appData!!.currency_symbol + " " + appData!!.register_amount
        Log.v("user_imagedb",appData!!.profile_image)

        try {
            Picasso.get().load(appData!!.profile_image!!)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(imgvwProfPicDrawer)
        } catch (e: Exception) {
            //  e.printStackTrace()
        }
    }

    override fun initResources() {
        appData = AppData(this@DashboardHomeActivity, Constants.Keys._KeyCryptoPreference)
        //tvUserNameNavDrawer.setCustomFont(this, getString(R.string.quicksandMedium), true)
        tvCommonToolbarDashboardHome.setCustomFont(this, getString(R.string.quicksandMedium), true)
        //Utils.hideKeyboard(this)
        Utils.hideKeyboardOnOutsideTouch(flFragmentContainerDashboard, this)
        parseIntent(intent)

        /* Init drawer items to adapter from string array */
        drawerItemsAdapter = DrawerItemsAdapter(this)
        drawerItemsAdapter.addItems(DrawerItemsModel(R.drawable.ic_home_screen, resources.getStringArray(R.array.navDrawerItems)[0]))
        drawerItemsAdapter.addItems(DrawerItemsModel(R.drawable.ic_my_account, resources.getStringArray(R.array.navDrawerItems)[1]))
        drawerItemsAdapter.addItems(DrawerItemsModel(R.drawable.ic_my_qr_code, resources.getStringArray(R.array.navDrawerItems)[2]))
        drawerItemsAdapter.addItems(DrawerItemsModel(R.drawable.ic_settings, resources.getStringArray(R.array.navDrawerItems)[3]))
        drawerItemsAdapter.addItems(DrawerItemsModel(R.drawable.ic_about_guulpay, resources.getStringArray(R.array.navDrawerItems)[4]))
        drawerItemsAdapter.addItems(DrawerItemsModel(R.drawable.ic_help_and_support, resources.getStringArray(R.array.navDrawerItems)[5]))
        drawerItemsAdapter.addItems(DrawerItemsModel(R.drawable.ic_contact_us_sidemenu, resources.getStringArray(R.array.navDrawerItems)[6]))
        drawerItemsAdapter.addItems(DrawerItemsModel(R.drawable.ic_logout, resources.getStringArray(R.array.navDrawerItems)[7]))

        lvDrawerItem.adapter = drawerItemsAdapter
    }

    override fun initListeners() {
        imgvwNotification.setOnClickListener(this)

        /* Navigation Drawer related items click */
        imgvwNavMenu.setOnClickListener(this)
        lvDrawerItem.setOnItemClickListener(this)
        drawerLayout.addDrawerListener(this)
        imgvwNavDrawerRightArrow.setOnClickListener(this)
        llDrawerHeader.setOnClickListener(this)
        imgvwProfPicDrawer.setOnClickListener(this)

        /* Bottom bar icons click */
        tvHomeBottomLayout.setOnClickListener(this)
        tvWalletBottomLayout.setOnClickListener(this)
        tvMoreBottomLayout.setOnClickListener(this)
        tvTransBottomLayout.setOnClickListener(this)
        imgvwPaymentMiddleBottomBar.setOnClickListener(this)
    }

    override fun getLayout(): Int {
        return R.layout.dashboard_home_activity
    }

    /**
     * This method set the Activity UI according to the fragment, for example it changes toolbar icon's visibility according to fragment loaded
     */
    fun setUpScreenUiForFragment(fragName: String) {
        when (fragName) {
            MyQrCodeFragment.CLASS_NAME -> {
                tvCommonToolbarDashboardHome.visibility = View.VISIBLE
                tvCommonToolbarDashboardHome.text = resources.getStringArray(R.array.navDrawerItems)[2]
                imgvwGuulpayLogo.visibility = View.GONE
                hideBottomBar()
            }
            HelpSupportMenuFragment.CLASS_NAME -> {
                tvCommonToolbarDashboardHome.visibility = View.VISIBLE
                tvCommonToolbarDashboardHome.text = resources.getString(R.string.helpSupport)
                imgvwGuulpayLogo.visibility = View.GONE
                hideBottomBar()
            }
            HelpSupportSubmitFragment.CLASS_NAME -> {
                tvCommonToolbarDashboardHome.visibility = View.VISIBLE
                tvCommonToolbarDashboardHome.text = resources.getString(R.string.helpSupport)
                imgvwGuulpayLogo.visibility = View.GONE
                hideBottomBar()
            }
            HelpSupportSelectOrderFragment.CLASS_NAME -> {
                tvCommonToolbarDashboardHome.visibility = View.VISIBLE
                tvCommonToolbarDashboardHome.text = resources.getString(R.string.helpSupport)
                imgvwGuulpayLogo.visibility = View.GONE
                hideBottomBar()
            }
            AccountFragment.CLASS_NAME -> {
                imgvwGuulpayLogo.visibility = View.GONE
                tvCommonToolbarDashboardHome.visibility = View.VISIBLE
                tvCommonToolbarDashboardHome.text = resources.getString(R.string.account)
                hideBottomBar()
            }
            DashboardMainContentFragment.CLASS_NAME -> {
                imgvwGuulpayLogo.visibility = View.VISIBLE
                tvCommonToolbarDashboardHome.visibility = View.GONE
                deselectAllBottomBarIcon()
                showBottomBar()
                tvHomeBottomLayout.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_select, 0, 0)
            }
            MyWalletFragment.CLASS_NAME -> {
                imgvwGuulpayLogo.visibility = View.GONE
                tvCommonToolbarDashboardHome.visibility = View.VISIBLE
                tvCommonToolbarDashboardHome.text = resources.getString(R.string.wallet)
                deselectAllBottomBarIcon()
                showBottomBar()
                tvWalletBottomLayout.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_my_wallet_select, 0, 0)
            }
            MoreFragment.CLASS_NAME -> {
                imgvwGuulpayLogo.visibility = View.GONE
                tvCommonToolbarDashboardHome.visibility = View.VISIBLE
                tvCommonToolbarDashboardHome.text = resources.getString(R.string.more)
                deselectAllBottomBarIcon()
                showBottomBar()
                tvMoreBottomLayout.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_more_icon_select, 0, 0)
            }
            MyTransactionFragment.CLASS_NAME -> {
                imgvwGuulpayLogo.visibility = View.GONE
                tvCommonToolbarDashboardHome.visibility = View.VISIBLE
                tvCommonToolbarDashboardHome.text = resources.getString(R.string.mytransactions)
                deselectAllBottomBarIcon()
                showBottomBar()
                tvTransBottomLayout.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_transaction_select, 0, 0)
            }
            AboutUsFragment.CLASS_NAME -> {
                imgvwGuulpayLogo.visibility = View.GONE
                tvCommonToolbarDashboardHome.visibility = View.VISIBLE
                tvCommonToolbarDashboardHome.text = resources.getString(R.string.aboutus)
                deselectAllBottomBarIcon()
                //showBottomBar()
                hideBottomBar()
                //tvTransBottomLayout.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_transaction_select, 0, 0)
            }

            ContactUsFragment.CLASS_NAME -> {
                tvCommonToolbarDashboardHome.visibility = View.VISIBLE
                tvCommonToolbarDashboardHome.text = resources.getString(R.string.contact_us)
                imgvwGuulpayLogo.visibility = View.GONE
                hideBottomBar()
            }
        }
    }

    /**
     * Parse intent value received either in onCreate and load the required fragment
     */
    fun parseIntent(intent: Intent?) {
        if (null != intent) {
            if (intent.hasExtra(Constants.Keys.KEY_FRAGMENT_NAME)) {
                val fragName = intent.getStringExtra(Constants.Keys.KEY_FRAGMENT_NAME)
                setUpScreenUiForFragment(fragName)
                when (fragName) {
                    DashboardMainContentFragment.CLASS_NAME -> {
                        Utils.addFragmentInActivityFadeAnimation(supportFragmentManager, DashboardMainContentFragment.newInstance(),
                                R.id.flFragmentContainerDashboard, false, DashboardMainContentFragment.CLASS_NAME)
                    }
                }
            }
        }
    }

    /* Handle drawer items on click */
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> {
                drawerLayout.closeDrawers()
                if (!(supportFragmentManager.findFragmentById(R.id.flFragmentContainerDashboard) is DashboardMainContentFragment)) {
                    setUpScreenUiForFragment(DashboardMainContentFragment.CLASS_NAME)
                    Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, DashboardMainContentFragment.newInstance(),
                            R.id.flFragmentContainerDashboard, false, DashboardMainContentFragment.CLASS_NAME)
                }

            }
            1 -> {
                drawerLayout.closeDrawers()
                if (!(supportFragmentManager.findFragmentById(R.id.flFragmentContainerDashboard) is AccountFragment)) {
                    setUpScreenUiForFragment(AccountFragment.CLASS_NAME)
                    Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, AccountFragment.newInstance(),
                            R.id.flFragmentContainerDashboard, false, AccountFragment.CLASS_NAME)
                }
            }
            2 -> {
                drawerLayout.closeDrawers()
                if (!(supportFragmentManager.findFragmentById(R.id.flFragmentContainerDashboard) is MyQrCodeFragment)) {
                    setUpScreenUiForFragment(MyQrCodeFragment.CLASS_NAME)
                    Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, MyQrCodeFragment.newInstance(),
                            R.id.flFragmentContainerDashboard, false, MyQrCodeFragment.CLASS_NAME)
                }
            }
            3 -> {
                drawerLayout.closeDrawers()
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
                // AboutUsFragment
                /* if (!(supportFragmentManager.findFragmentById(R.id.flFragmentContainerDashboard) is AboutUsFragment)) {
                     setUpScreenUiForFragment(AboutUsFragment.CLASS_NAME)
                     Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, AboutUsFragment.newInstance(), R.id.flFragmentContainerDashboard, false, AboutUsFragment.CLASS_NAME)
                 }*/
                //KToast.normalToast(this, "  Coming Soon  ", Gravity.CENTER, KToast.LENGTH_LONG)
                // Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()

            }
            4 -> {
                drawerLayout.closeDrawers()
                if (!(supportFragmentManager.findFragmentById(R.id.flFragmentContainerDashboard) is AboutUsFragment)) {
                    setUpScreenUiForFragment(AboutUsFragment.CLASS_NAME)
                    Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, AboutUsFragment.newInstance(), R.id.flFragmentContainerDashboard, false, AboutUsFragment.CLASS_NAME)
                }
            }
            5 -> {
                drawerLayout.closeDrawers()
                if (!(supportFragmentManager.findFragmentById(R.id.flFragmentContainerDashboard) is HelpSupportMenuFragment)) {
                    setUpScreenUiForFragment(HelpSupportMenuFragment.CLASS_NAME)
                    Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, HelpSupportMenuFragment.newInstance(),
                            R.id.flFragmentContainerDashboard, false, HelpSupportMenuFragment.CLASS_NAME)
                }
            }
            6 -> {
                drawerLayout.closeDrawers()
                if (!(supportFragmentManager.findFragmentById(R.id.flFragmentContainerDashboard) is ContactUsFragment)) {
                    setUpScreenUiForFragment(ContactUsFragment.CLASS_NAME)
                    Utils.replaceFragmentInActivityFadeAnimation(supportFragmentManager, ContactUsFragment.newInstance(), R.id.flFragmentContainerDashboard, false, ContactUsFragment.CLASS_NAME)
                }
            }
            7 -> {
                drawerLayout.closeDrawers()
                showConfirmAlert()
            }
            else -> {
            }
        }
    }


    /* Confirm alert dialog before Logout */
    private fun showConfirmAlert() {
        alert(R.string.areYouSure, R.string.logout) {
            isCancelable = false
            negativeButton(R.string.no) {
                it.dismiss()
            }
            positiveButton(R.string.yes) {
                appData!!.cleardata(applicationContext, Constants.Keys._KeyCryptoPreference)
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }?.show()
    }

    private fun hideBottomBar() {
        rlBottomBar.visibility = View.GONE
        flScanImageBottomBar.visibility = View.GONE
    }

    private fun showBottomBar() {
        rlBottomBar.visibility = View.VISIBLE
        flScanImageBottomBar.visibility = View.VISIBLE
    }


    /* DrawerLayout.DrawerListener callabck methods  */
    override fun onDrawerStateChanged(newState: Int) {
        Log.e(TAG, "onDrawerStateChanged")
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        Log.e(TAG, "onDrawerSlide")
    }

    override fun onDrawerClosed(drawerView: View) {
        Log.e(TAG, "onDrawerClosed")
    }

    override fun onDrawerOpened(drawerView: View) {
        tvVersionNavDrawer.text = "v" + Utils.getAppVersionName(this)
        Log.e(TAG, "onDrawerOpened")
    }


    private fun loadJSONFromAsset(): String? {
        var json: String? = ""
        try {
            val inputStream = getAssets().open(resources.getString(R.string.currenciesPath))
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            //showSomeErrorOccurredMsg(ex.toString())
        }

        return json
    }


    inner class FetchCountryCodesAsynTask : AsyncTask<Void, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            loader.show()
        }

        override fun doInBackground(vararg p0: Void): String {
            val listType = object : TypeToken<ArrayList<CurrenciesModel>>() {}.type
            currenciesModellist = GsonBuilder().create().fromJson(loadJSONFromAsset(), listType)
            currenciesModellist =
                    return ""
        }

        override fun onPostExecute(result: String?) {
            if (currenciesModellist.size > 0) {
                for (i in currenciesModellist.indices) {
                    if (currenciesModellist.get(i).cc.equals(appData!!.register_currency_code)) {
                        appData!!.currency_symbol = currenciesModellist.get(i).symbol.toString()
                        tvWalletAmt.text = appData!!.currency_symbol + " " + appData!!.register_amount
                        Log.d("Currency", appData!!.currency_symbol)
                        break
                    }
                }
            }
            loader.hide()
        }

    }

    override fun globalLogout() {
        appData!!.cleardata(applicationContext, Constants.Keys._KeyCryptoPreference)
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}



