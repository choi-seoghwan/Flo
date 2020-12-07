package com.example.flo.view.main

import com.example.flo.R
import com.example.flo.base.BaseKotlinFragment
import com.example.flo.databinding.FragmentBrowserBinding
import com.example.flo.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BrowserFragment : BaseKotlinFragment<FragmentBrowserBinding, MainViewModel>() {
    private val TAG = "BrowserFragment"

    override val layoutResourceId: Int
        get() = R.layout.fragment_browser

    override val viewModel: MainViewModel by sharedViewModel()

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {

    }

    companion object {
        @JvmStatic
        fun newInstance() = BrowserFragment()
    }
}