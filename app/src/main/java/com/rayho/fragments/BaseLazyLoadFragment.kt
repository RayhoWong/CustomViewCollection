package com.rayho.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by Rayho on 2020/8/3.
 * 懒加载基类
 */
abstract class BaseLazyLoadFragment : Fragment() {

    //当前页面是否可见
    private var isUiVisible = false

    //标志view控件是否已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，
    // 在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    private var isViewCreated = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = null
        if (layoutId != 0) {
            view = inflater.inflate(layoutId, container, false)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewCreated = true
        lazyLoad()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
        isUiVisible = false
    }

    /**
     * 返回子类的布局id
     * @return
     */
    abstract val layoutId: Int

    /**
     *
     * 通过判断setUserVisibleHint的isVisibleToUser，判断界面是否可见
     * @param isVisibleToUser
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isUiVisible = true
            lazyLoad()
        } else {
            isUiVisible = false
        }
    }

    private fun lazyLoad() { //只有页面可见并且view初始化完成 才可以加载数据
        if (isUiVisible && isViewCreated) {
            loadData()
            isViewCreated = false
            isUiVisible = false
        } else {
            return
        }
    }

    abstract fun loadData()
}