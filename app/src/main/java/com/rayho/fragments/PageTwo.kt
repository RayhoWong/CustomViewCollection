package com.rayho.fragments

import com.rayho.customviewcollection.R

/**
 * Created by huangweihao on 2020/8/3.
 */
class PageTwo : BaseLazyLoadFragment() {

    companion object{
        fun getInstance() = PageTwo()
    }

    override val layoutId = R.layout.fragment_page_2


    override fun loadData() {
    }

}