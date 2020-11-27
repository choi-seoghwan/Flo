package com.example.flo.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar

abstract class BaseKotlinActivity<T : ViewDataBinding, R : BaseKotlinViewModel> : AppCompatActivity() {

    lateinit var viewDataBinding: T
    lateinit var binding: T

    abstract val layoutResourceId: Int
    abstract val viewModel: R

    /**
     * 뷰나 액티비티의 속성 등을 초기화.
     */
    abstract fun initStartView()

    /**
     * 데이터 바인딩 및 rxjava 설정.
     */
    abstract fun initDataBinding()

    /**
     * 클릭 리스너
     */
    abstract fun initAfterBinding()

    private var isSetBackButtonValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(layoutInflater,layoutResourceId,null,false)
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)
        snackbarObserving()
        initStartView()
        initDataBinding()
        initAfterBinding()
    }

    private fun snackbarObserving() {
        viewModel.observeSnackbarMessage(this) {
            Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
        }
        viewModel.observeSnackbarMessageStr(this){
            Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_LONG).show()
        }
    }
}