package com.example.flo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseKotlinFragment<T : ViewDataBinding, R : BaseKotlinViewModel> : Fragment() {

    lateinit var viewDataBinding: T

    abstract val layoutResourceId: Int
    abstract val viewModel: R

    // reset
    abstract fun initStartView()

    // Rx, databiding
    abstract fun initDataBinding()

    // Listener
    abstract fun initAfterBinding()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        initStartView()
        initDataBinding()
        initAfterBinding()
    }

}