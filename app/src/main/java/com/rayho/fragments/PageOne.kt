package com.rayho.fragments

import com.rayho.customviewcollection.R

/**
 * Created by huangweihao on 2020/8/3.
 */
class PageOne : BaseLazyLoadFragment() {

    companion object{
        fun getInstance() = PageOne()
    }

    override val layoutId = R.layout.fragment_page_1


    override fun loadData() {

    }

}