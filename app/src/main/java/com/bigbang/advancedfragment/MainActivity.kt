package com.bigbang.advancedfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {
    //To use a ViewPager you need a ViewPagerAdapter
    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeViewPagerAdapter = HomeViewPagerAdapter(supportFragmentManager)
        main_viewpager.adapter = homeViewPagerAdapter
        main_viewpager.addOnPageChangeListener(this)

        bottom_navigation_view.setOnNavigationItemSelectedListener { menuItem ->

            when(menuItem.itemId){
                R.id.home_menu_item -> main_viewpager.currentItem = 0
                R.id.profile_menu_item -> main_viewpager.currentItem = 1
                R.id.settings_menu_item -> main_viewpager.currentItem = 2
            }

            true
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
//        Do nothing
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//        Do nothing
    }

    override fun onPageSelected(position: Int) {
        when(position){
            0 -> bottom_navigation_view.selectedItemId = R.id.home_menu_item
            1 -> bottom_navigation_view.selectedItemId = R.id.profile_menu_item
            2 -> bottom_navigation_view.selectedItemId = R.id.settings_menu_item
        }
    }
}
