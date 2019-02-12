package com.guulpay.transaction

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import kotlinx.android.synthetic.main.transaction_status_fragment.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue

class TransactionStatusFragment:BaseFragment(), View.OnClickListener {

    val pieChartItemsCount = 2
    companion object {
        const val CLASS_NAME = "TransactionStatusFragment"
        fun newInstance(): Fragment {
            return TransactionStatusFragment()
        }
    }
    override fun getLayoutView(): Int {
        return R.layout.transaction_status_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        val values = ArrayList<SliceValue>()
        for (i in 0..pieChartItemsCount-1) {
            val sliceValue = SliceValue((100*(i+2)).toFloat(), activity?.resources!!.getIntArray(R.array.pieChartItems).get(i))
            sliceValue.sliceSpacing = 10
            values.add(sliceValue)
        }
        val data = PieChartData(values)
        data.setHasLabels(true)
        data.setHasLabelsOnlyForSelected(false)
        data.setHasLabelsOutside(false)
        data.setValueLabelsTextColor(Color.BLACK)
        data.isValueLabelBackgroundEnabled = false
        data.setHasCenterCircle(true)
        data.setCenterCircleScale(0.4F)
        data.slicesSpacing = 2

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
                lineraColorIndicator.visibility = View.VISIBLE
                imgvwShowChart.background = resources.getDrawable(R.drawable.ic_list_icon)
            } else {
                lineraColorIndicator.visibility = View.GONE
                vwSeparator.visibility = View.GONE
                flPieChart.visibility = View.GONE
                imgvwShowChart.background = resources.getDrawable(R.drawable.ic_chart_icon)
            }
        }
    }
}