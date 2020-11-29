package com.example.flo.view.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.flo.R
import kotlinx.android.synthetic.main.layout_main_tab.view.*

class MainTabLayout : LinearLayout {

    constructor(paramContext: Context?) : super(paramContext) {
        binding()
    }

    constructor(paramContext: Context?, paramAttributeSet: AttributeSet?) : super(paramContext, paramAttributeSet) {
        binding()
    }

    constructor(paramContext: Context?, paramAttributeSet: AttributeSet?, paramInt: Int) : super(paramContext, paramAttributeSet, paramInt) {
        binding()
    }

    fun binding() {
        val viewDataBinding: ViewDataBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_main_tab, this as ViewGroup, true)
        mainTab_my_image.setImageResource(R.drawable.main_tab_my)
    }
}