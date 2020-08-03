package com.rayho.fragments

import com.rayho.customviewcollection.R

/**
 * Created by huangweihao on 2020/8/3.
 */
class PageThree : BaseLazyLoadFragment() {

    companion object{
        fun getInstance() = PageThree()
    }

    override val layoutId = R.layout.fragment_page_3


    override fun loadData() {
    }

}