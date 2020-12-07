package com.example.flo.view.main

import com.example.flo.R
import com.example.flo.base.BaseKotlinFragment
import com.example.flo.databinding.FragmentRecommendBinding
import com.example.flo.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RecommendFragment : BaseKotlinFragment<FragmentRecommendBinding, MainViewModel>() {
    private val TAG = "RecommendFragment"

    override val layoutResourceId: Int
        get() = R.layout.fragment_recommend

    override val viewModel: MainViewModel by sharedViewModel()

    override fun initStartView() {
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {

    }

    companion object {
        @JvmStatic
        fun newInstance() = RecommendFragment()
    }
}