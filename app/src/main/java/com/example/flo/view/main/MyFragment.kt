package com.example.flo.view.main

import com.example.flo.R
import com.example.flo.base.BaseKotlinFragment
import com.example.flo.databinding.FragmentMyBinding
import com.example.flo.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MyFragment : BaseKotlinFragment<FragmentMyBinding, MainViewModel>() {
    private val TAG = "MyFragment"

    override val layoutResourceId: Int
        get() = R.layout.fragment_my

    override val viewModel: MainViewModel by sharedViewModel()

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {

    }

    companion object {
        @JvmStatic
        fun newInstance() = MyFragment()
    }
}