package com.example.flo.view

import android.view.View
import androidx.lifecycle.Observer
import com.example.flo.R
import com.example.flo.base.BaseKotlinFragment
import com.example.flo.databinding.FragmentMainBinding
import com.example.flo.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : BaseKotlinFragment<FragmentMainBinding, MainViewModel>() {
    private val TAG = "MainFragment"

    override val layoutResourceId: Int
        get() = R.layout.fragment_main

    override val viewModel: MainViewModel by sharedViewModel()

    override fun initStartView() {
    }

    override fun initDataBinding() {
        viewModel.selectedTab.observe(this, Observer { tab ->
            when (tab) {
                "HOME" -> {
                    main_recommend_layout.visibility = View.VISIBLE
                    main_browser_layout.visibility = View.GONE
                    main_search_layout.visibility = View.GONE
                    main_my_layout.visibility = View.GONE
                }
                "BROWSER" -> {
                    main_recommend_layout.visibility = View.GONE
                    main_browser_layout.visibility = View.VISIBLE
                    main_search_layout.visibility = View.GONE
                    main_my_layout.visibility = View.GONE
                }
                "SEARCH" -> {
                    main_recommend_layout.visibility = View.GONE
                    main_browser_layout.visibility = View.GONE
                    main_search_layout.visibility = View.VISIBLE
                    main_my_layout.visibility = View.GONE
                }
                "MY" -> {
                    main_recommend_layout.visibility = View.GONE
                    main_browser_layout.visibility = View.GONE
                    main_search_layout.visibility = View.GONE
                    main_my_layout.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun initAfterBinding() {

    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}