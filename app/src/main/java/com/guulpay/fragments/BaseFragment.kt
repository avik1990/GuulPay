package com.guulpay.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initResources(view, savedInstanceState)
        initListeners()
    }

    abstract fun getLayoutView(): Int
    abstract fun initResources(view: View, savedInstanceState: Bundle?)
    abstract fun initListeners()
    //abstract fun start()
}
