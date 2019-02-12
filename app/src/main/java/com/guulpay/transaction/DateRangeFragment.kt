package com.guulpay.transaction

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment

import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import kotlinx.android.synthetic.main.date_range_transhistory_fragment.*
import android.R.attr.data
import android.util.Log
import android.view.View
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.model.SliceValue



class DateRangeFragment : BaseFragment(), View.OnClickListener {

    /*lateinit var pieData: PieData
    lateinit var pieDataSet: PieDataSet
    lateinit var entryList: ArrayList<Entry>
    lateinit var xValsList: ArrayList<String>*/
    val pieChartItemsCount = 2

    companion object {
        const val CLASS_NAME = "DateRangeFragment"
        fun newInstance(): Fragment {
            return DateRangeFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun getLayoutView(): Int {
        return R.layout.date_range_transhistory_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {

        val values = ArrayList<SliceValue>()
        for (i in 0..pieChartItemsCount-1) {
            val sliceValue = SliceValue((100*(i+1)).toFloat(), activity?.resources!!.getIntArray(R.array.pieChartItems).get(i))
            sliceValue.sliceSpacing = 10
            values.add(sliceValue)
        }
        val data = PieChartData(values)
        data.setHasLabels(true)
        data.setHasLabelsOnlyForSelected(false)
        data.setHasLabelsOutside(false)
        data.setValueLabelsTextColor(Color.WHITE)
        data.isValueLabelBackgroundEnabled = false
        data.setHasCenterCircle(true)
        data.setCenterCircleScale(0.4F)
        data.slicesSpacing = 2

       /* pieChart.isInteractive = false
        pieChart.isZoomEnabled = false
        pieChart.isScrollEnabled = false*/

        pieChart.pieChartData = data

    }

    override fun initListeners() {
        imgvwShowChart.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v == imgvwShowChart) {
            if (flPieChart.visibility == View.GONE) {
           //     llChartLabels.visibility = View.VISIBLE
                vwSeparator.visibility = View.VISIBLE
                flPieChart.visibility = View.VISIBLE
                imgvwShowChart.background = resources.getDrawable(R.drawable.ic_list_icon)
            } else {
                llChartLabels.visibility = View.GONE
                vwSeparator.visibility = View.GONE
                flPieChart.visibility = View.GONE
                imgvwShowChart.background = resources.getDrawable(R.drawable.ic_chart_icon)
            }
        }
    }

}