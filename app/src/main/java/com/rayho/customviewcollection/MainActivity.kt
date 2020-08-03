package com.rayho.customviewcollection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.rayho.fragments.PageOne
import com.rayho.fragments.PageThree
import com.rayho.fragments.PageTwo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    private val fragments = arrayListOf<Fragment>()

    private val titles = arrayListOf<String>()

    private val adapter by lazy {
        MyViewPagerAdapter(supportFragmentManager)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initView()
    }

    private fun initData(){
        fragments.add(PageOne.getInstance())
        fragments.add(PageTwo.getInstance())
        fragments.add(PageThree.getInstance())

        titles.add("PAGE_ONE")
        titles.add("PAGE_TWO")
        titles.add("PAGE_THREE")
    }


    private fun initView(){
        viewpager.adapter = adapter
        tablayout.setupWithViewPager(viewpager)
    }


    private inner class MyViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager){

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }

    }
}
