package com.bigbang.advancedfragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class HomeViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {

        return when(position){
            0 -> HomeFragment()
            1 -> ProfileFragment()
            else -> SettingsFragment()
        }

        /*return if (position == 1)
            HomeFragment()
        else if (position == 2)
            ProfileFragment()
        else
            SettingsFragment()*/

    }
    override fun getCount(): Int = 3
}