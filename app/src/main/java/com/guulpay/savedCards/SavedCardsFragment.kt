package com.guulpay.savedCards

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.guulpay.R
import com.guulpay.fragments.BaseFragment
import kotlinx.android.synthetic.main.saved_cardslist_fragment.*

class SavedCardsFragment:BaseFragment() {

    lateinit var savedCardsAdapter: SavedCardsAdapter

    companion object {
        const val CLASS_NAME = "SavedCardsFragment"
        fun newInstance(): Fragment {
            return SavedCardsFragment()
        }
    }

    override fun getLayoutView(): Int {
        return R.layout.saved_cardslist_fragment
    }

    override fun initResources(view: View, savedInstanceState: Bundle?) {
        /* Saved banks list */
        savedCardsAdapter = SavedCardsAdapter()
        val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerVwSavedCards.layoutManager = mLayoutManager
        recyclerVwSavedCards.itemAnimator = DefaultItemAnimator()
        recyclerVwSavedCards.adapter = savedCardsAdapter
    }

    override fun initListeners() {

    }
}