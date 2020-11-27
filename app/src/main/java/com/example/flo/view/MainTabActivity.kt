package com.example.flo.view

import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.example.flo.R
import com.example.flo.base.BaseKotlinActivity
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.view.ui.MainTabListener
import com.example.flo.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main_tab.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainTabActivity : BaseKotlinActivity<ActivityMainBinding, MainViewModel>(), MainTabListener {
    override val layoutResourceId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()

    private val TAG = "MainActivity"

    override fun initStartView() {
        // 각각 Fragment를 나눠서 만들어야 하지만, 현재 구현하고 싶은 주 기능이 아니기 때문에 남긴다.
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(main_screen.id, MainFragment.newInstance()).commit()

        viewModel.selectTab("HOME")
    }

    override fun initDataBinding() {
        viewModel.selectedTab.observe(this, Observer {
            it.let {tab ->
                when(tab){
                    "HOME" -> landingHome()
                    "BROWSER" -> landingBrowser()
                    "SEARCH" -> landingSearch()
                    "MY" -> landingMy()
                }
            }
        })
    }

    override fun initAfterBinding() {
        mainTab_home_layout.setOnClickListener {
            viewModel.selectTab("HOME")
        }
        mainTab_browser_layout.setOnClickListener {
            viewModel.selectTab("BROWSER")
        }
        mainTab_search_layout.setOnClickListener {
            viewModel.selectTab("SEARCH")
        }
        mainTab_my_layout.setOnClickListener {
            viewModel.selectTab("MY")
        }
    }

    override fun landingHome() {
        clearAllTab()
        mainTab_home_image.isSelected = true
        mainTab_home_text.setTextColor(resources.getColor(R.color.use_tab_color))
    }

    override fun landingBrowser() {
        clearAllTab()
        mainTab_browser_image.isSelected = true
        mainTab_browser_text.setTextColor(resources.getColor(R.color.use_tab_color))
    }

    override fun landingSearch() {
        clearAllTab()
        mainTab_search_image.isSelected = true
        mainTab_search_text.setTextColor(resources.getColor(R.color.use_tab_color))
    }

    override fun landingMy() {
        clearAllTab()
        mainTab_my_image.isSelected = true
        mainTab_my_text.setTextColor(resources.getColor(R.color.use_tab_color))
    }

    private fun clearAllTab(){
        mainTab_home_image.isSelected = false
        mainTab_browser_image.isSelected = false
        mainTab_search_image.isSelected = false
        mainTab_my_image.isSelected = false
        mainTab_home_text.setTextColor(resources.getColor(R.color.not_use_tab_color))
        mainTab_browser_text.setTextColor(resources.getColor(R.color.not_use_tab_color))
        mainTab_search_text.setTextColor(resources.getColor(R.color.not_use_tab_color))
        mainTab_my_text.setTextColor(resources.getColor(R.color.not_use_tab_color))
    }
}
