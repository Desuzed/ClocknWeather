package com.desuzed.clocknweather.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.desuzed.clocknweather.ui.ClockFragment.Companion.newInstance
import com.desuzed.clocknweather.R
import com.desuzed.clocknweather.databinding.ActivityMainBinding
import com.desuzed.clocknweather.util.adapters.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainActivity : AppCompatActivity() {
    private var viewPager: ViewPager2? = null
    private var tabLayout: TabLayout? = null
    private lateinit var tabNames: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        hideStatusBar()
        initTabs()
    }

    private val fragmentsList: List<Fragment>
        private get() {
            val fragments: MutableList<Fragment> = ArrayList()
            val clockFragment = newInstance()
            val weatherFragment = WeatherFragment.newInstance()
            fragments.add(clockFragment)
            fragments.add(weatherFragment)
            return fragments
        }

    private fun bind() {
        val amb = ActivityMainBinding.inflate(
            layoutInflater
        )
        val view: View = amb.root
        setContentView(view)
        viewPager = amb.viewPager
        tabLayout = amb.tabLayout
//        setContentView(R.layout.activity_main)
//        tabLayout = findViewById(R.id.tabLayout)
//        viewPager = findViewById(R.id.viewPager)
    }

    private fun hideStatusBar() {
        val decorView = window.decorView
        // Hide the status bar.
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        //        ActionBar actionBar = getActionBar();
//        actionBar.hide();
    }

    private fun initTabs() {
        tabNames =
            arrayOf(resources.getString(R.string.clock), resources.getString(R.string.weather))
        val adapter = PagerAdapter(this, fragmentsList)
        viewPager!!.adapter = adapter
        TabLayoutMediator(
            tabLayout!!,
            viewPager!!
        ) { tab: TabLayout.Tab, position: Int -> tab.text = tabNames[position] }.attach()
    }
}