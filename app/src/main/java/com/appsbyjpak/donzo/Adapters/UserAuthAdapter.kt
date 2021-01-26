package com.appsbyjpak.donzo.Adapters

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.appsbyjpak.donzo.Fragments.LoginTabFragment
import com.appsbyjpak.donzo.Fragments.SignupTabFragment

class UserAuthAdapter(private val context: Context, fm: FragmentManager, var totalTabs: Int): FragmentPagerAdapter(fm, totalTabs) {

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                LoginTabFragment()
            }
            1 -> {
                SignupTabFragment()
            }
            else -> throw IllegalStateException("position $position is invalid for this viewpager")
        }
    }

}