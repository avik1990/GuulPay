package com.guulpay.guulexgraph

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.guulpay.R
import com.guulpay.activity.HomeActivity
import com.guulpay.contactus.servicecall.ContactUsRepositoryProvider
import com.guulpay.fragments.BaseFragment
import com.guulpay.guulex.adapter.GuulexListAdapter
import com.guulpay.others.AppData
import com.guulpay.others.Constants
import com.guulpay.others.LoaderDialog
import com.guulpay.others.Utils
import kotlinx.android.synthetic.main.guulex_fragment_layout.*
import kotlinx.android.synthetic.main.guulex_graph_fragment_layout.*
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.LineChartView
import java.util.ArrayList

class GuulexGraphFragment : BaseFragment(), View.OnClickListener, GuulexGraphContract.View {
    
    lateinit var GuulexPresenter: GuulexGraphPresenter
    var appData: AppData? = null

    var data: LineChartData? = null
    var numberOfLines = 2
    val maxNumberOfLines = 4
    val numberOfPoints = 12

    var randomNumbersTab = Array(maxNumberOfLines) { FloatArray(numberOfPoints) }

    var hasAxes = true
    var hasAxesNames = false
    var hasLines = true
    var hasPoints = true
    var shape = ValueShape.CIRCLE
    var isFilled = false
    var hasLabels = false
    var isCubic = false
    var hasLabelForSelected = false
    var pointsHaveDifferentColor: Boolean = false
    var hasGradientToTransparent = false
    val days = arrayOf("Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun")


    companion object {
        const val CLASS_NAME = "GuulexGraphFragment"
        fun newInstance(): Fragment {
            return GuulexGraphFragment()
        }
    }

    val loader by lazy {
        LoaderDialog(context)
    }

    override fun getLayoutView(): Int {
        return R.layout.guulex_graph_fragment_layout
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        appData = AppData(context as HomeActivity, Constants.Keys._KeyCryptoPreference)
        GuulexPresenter = GuulexGraphPresenter(this, ContactUsRepositoryProvider.getAboutusContent(), appData!!)
        lineChartView!!.onValueTouchListener = ValueTouchListener()

        generateValues()

        generateData()

        // Disable viewport recalculations, see toggleCubic() method for more info.
        lineChartView!!.isViewportCalculationEnabled = false

        resetViewport()
    }

    fun generateValues() {
        for (i in 0 until maxNumberOfLines) {
            for (j in 0 until numberOfPoints) {
                randomNumbersTab[i][j] = Math.random().toFloat() * 100f
            }
        }
    }


    fun generateData() {
        var lines = ArrayList<Line>()
        for (i in 0 until numberOfLines) {

            val values = ArrayList<PointValue>()
            for (j in 0 until numberOfPoints) {
                values.add(PointValue(j.toFloat(), randomNumbersTab[i][j]))
            }

            val line = Line(values)
            line.color = ChartUtils.COLORS[i]
            line.shape = shape
            line.isCubic = isCubic
            line.isFilled = isFilled
            line.setHasLabels(hasLabels)
            line.setHasLabelsOnlyForSelected(hasLabelForSelected)
            line.setHasLines(hasLines)
            line.setHasPoints(hasPoints)
            if (pointsHaveDifferentColor) {
                line.pointColor = ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.size]
            }
            lines.add(line)
        }

        data = LineChartData(lines)

        if (hasAxes) {
            val axisX = Axis()
            val axisY = Axis().setHasLines(true)
            if (hasAxesNames) {
                axisX.name = "Axis X"
                axisY.name = "Axis Y"
            }
            data!!.axisXBottom = axisX
            data!!.axisYLeft = axisY
        } else {
            data!!.axisXBottom = null
            data!!.axisYLeft = null
        }

        data!!.baseValue = java.lang.Float.NEGATIVE_INFINITY
        lineChartView!!.lineChartData = data
    }

    private fun resetViewport() {
        // Reset viewport height range to (0,100)
        val v = Viewport(lineChartView!!.maximumViewport)
        v.bottom = 0f
        v.top = 100f
        v.left = 0f
        v.right = (numberOfPoints - 1).toFloat()
        lineChartView!!.maximumViewport = v
        lineChartView!!.currentViewport = v
    }

    override fun initListeners() {


    }

    override fun onClick(v: View?) {

    }

    override fun handleProgressAlert(showingStatus: Boolean) {
        if (showingStatus) {
            loader.show()
        } else {
            loader.hide()
        }
    }

    override fun enableButton() {
    }

    override fun disableButton() {
    }

    override fun goToNextPage() {
        Utils.showToast(context, "We will reach back to you shortly")
        activity?.onBackPressed()
    }

    override fun isFragmentAlive(): Boolean {
        return isAdded
    }

    override fun isActivityRunning(): Boolean {
        return (activity as HomeActivity).isActivityVisible
    }

    override fun setPresenter(presenter: GuulexGraphContract.Presenter) {
        GuulexPresenter = presenter as GuulexGraphPresenter
    }

    override fun isNetworkAvailable(): Boolean {
        return Utils.isConnectedToNetwork(context)
    }

    override fun showNetworkUnavailableMsg() {
        Utils.showSnackbar(llGullexGraphParent, getString(R.string.networkUnavailable), 3000)
    }

    override fun showSomeErrorOccurredMsg(msg: String) {
        Utils.showSnackbar(llGullexGraphParent, msg, 3000)
    }

    override fun fieldsValidationFailed(msg: String) {

    }

    inner class ValueTouchListener : LineChartOnValueSelectListener {

        override fun onValueSelected(lineIndex: Int, pointIndex: Int, value: PointValue) {
          //  Toast.makeText(activity, "Selected: $value", Toast.LENGTH_SHORT).show()
        }

        override fun onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    override fun globalLogout() {
        appData!!.cleardata(activity!!, Constants.Keys._KeyCryptoPreference)
    }
}