package com.dev.baqari.pagerindicator

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val list = ArrayList<Fragment>()
        list.add(DummyFragment(Color.RED))
        list.add(DummyFragment(Color.GREEN))
        list.add(DummyFragment(Color.BLUE))
        list.add(DummyFragment(Color.YELLOW))
        list.add(DummyFragment(Color.MAGENTA))
        val adapter = RegistrationPagerAdapter(supportFragmentManager, list)
        viewPager.adapter = adapter

        complete_indicator.setViewPager(viewPager)
        complete_indicator.setOnItemClickListener(object : OnItemClickListener {
            override fun item(id: Int) {
                viewPager.currentItem = (id - 1)
            }
        })
    }
}
