package com.appsbyjpak.donzo.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.appsbyjpak.donzo.Adapters.UserAuthAdapter
import com.appsbyjpak.donzo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class UserAuthActivity : AppCompatActivity() {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var google: FloatingActionButton
    lateinit var fb: FloatingActionButton
    var v = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_auth)

        tabLayout = findViewById(R.id.user_auth_tab_layout)
        viewPager = findViewById(R.id.user_auth_view_pager)
        google = findViewById(R.id.fab_google)
        fb = findViewById(R.id.fab_fb)

        tabLayout.addTab(tabLayout.newTab().setText("Login"))
        tabLayout.addTab(tabLayout.newTab().setText("Signup"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = UserAuthAdapter(this, supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        fb.translationY = 300f
        google.translationY = 300f
        tabLayout.translationY = 300f

        fb.alpha = v
        google.alpha = v
        tabLayout.alpha = v

        fb.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(400).start()
        google.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(600).start()
        tabLayout.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(100).start()
    }
}