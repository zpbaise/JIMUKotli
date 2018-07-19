package com.shiye.baselibrary.net.observer


import android.app.Activity
import android.os.SystemClock
import android.support.v4.app.FragmentManager
import android.view.View
import com.shiye.baselibrary.R
import com.shiye.baselibrary.net.callback.CallBack

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

import com.shiye.baselibrary.net.exception.handleException
import com.shiye.baselibrary.net.loadingView.LoadingDialog
import com.shiye.baselibrary.net.loadingView.LoadingFragment
import com.shiye.baselibrary.net.loadingView.SupportLoadingFragment

/**
 * Created by Administrator on 2018/1/25.
 * 网络请求结果的observer
 */

class ResponseObserver<T>(private var callback: CallBack<T>) : Observer<T> {
    //v4包的dialogfragment
    private var mSupportLoadingFragment: SupportLoadingFragment? = null
    //非support包的fragment
    private var mLoadingFragment: LoadingFragment? = null
    //support的FragmentManager
    private var mSupportFragmentManager: FragmentManager? = null
    //非support的FragmentManager
    private var mFragmentManager: android.app.FragmentManager? = null
    private var mLoadingView: View? = null

    private var mIsShowLoading = false

    private var mActivity: Activity? = null
    private var mLoadingDialog: LoadingDialog? = null

    /**
     * support的FragmentManager
     * @param supportFragmentManager 如果为null表示不显示加载动画
     * @param loadingView 自定义加载中显示的view，当supportFragmentManager参数不为空时，
     *                    loadingView为null表示使用默认的loadingView，否则显示自定义的loadingView
     * @param loadingviewCancelable 点击空白区域是否可以隐藏loadingView
     * @param callback 网络请求的结果回调
     */
    constructor(supportFragmentManager: FragmentManager? = null, loadingView: View? = null, loadingviewCancelable: Boolean = true, callback: CallBack<T>) : this(callback) {
        if (supportFragmentManager == null) {
            mIsShowLoading = false
        } else {
            mIsShowLoading = true
        }
        mSupportFragmentManager = supportFragmentManager
        mSupportLoadingFragment = (mSupportFragmentManager?.findFragmentByTag(SupportLoadingFragment::class.java.name) as? SupportLoadingFragment)
                ?: SupportLoadingFragment()
        mSupportLoadingFragment?.isCancelable = loadingviewCancelable
        mLoadingView = loadingView
        mLoadingView?.let {
            mSupportLoadingFragment?.setLoadingView(it)
        }
    }

    /**
     * 非support的FragmentManager
     * @param fragmentManager 如果为null表示不显示加载动画
     * @param loadingView 自定义加载中显示的view，当supportFragmentManager参数不为空时，
     *                    loadingView为null表示使用默认的loadingView，否则显示自定义的loadingView
     * @param loadingviewCancelable 点击空白区域是否可以隐藏loadingView
     * @param callback 网络请求的结果回调
     */
    constructor(fragmentManager: android.app.FragmentManager?, loadingView: View?, loadingviewCancelable: Boolean = true, callback: CallBack<T>) : this(callback) {
        if (fragmentManager == null) {
            mIsShowLoading = false
        } else {
            mIsShowLoading = true
        }
        mFragmentManager = fragmentManager
        mLoadingFragment = (mFragmentManager?.findFragmentByTag(LoadingFragment::class.java.name) as? LoadingFragment)
                ?: LoadingFragment()
        mLoadingFragment?.isCancelable = loadingviewCancelable
        mLoadingView = loadingView
        mLoadingView?.let {
            mLoadingFragment?.setLoadingView(it)
        }
    }

    constructor(activity: Activity?, loadingView: View?, loadingviewCancelable: Boolean = true, callback: CallBack<T>) : this(callback) {
        if (activity == null) {
            mIsShowLoading = false
        } else {
            mIsShowLoading = true
        }
        mActivity = activity
        mLoadingView = loadingView
        mLoadingDialog = activity?.let {
            LoadingDialog(it, R.style.Loading_Dialog)
                    .apply {
                        setCancelable(loadingviewCancelable)
                        mLoadingView?.let {
                            setLoadingView(it)
                        }
                    }
        }
    }

    override fun onSubscribe(d: Disposable) {
        if (mIsShowLoading) {
            mSupportLoadingFragment?.let {
                if (it.isAdded) {
                    it.dismiss()
                }
                it.show(mSupportFragmentManager, SupportLoadingFragment::class.java.name)
            }
            mLoadingFragment?.let {
                if (it.isAdded) {
                    it.dismiss()
                }
                it.show(mFragmentManager, SupportLoadingFragment::class.java.name)
            }
            mLoadingDialog?.let {
                if (!it.isShowing) {
                    it.show()
                }
            }
        }
        callback.onBefore(d)
    }

    override fun onNext(t: T) {
        callback.onSucceed(t)
    }

    override fun onError(e: Throwable) {
        if (mIsShowLoading) {
            mSupportLoadingFragment?.let {
                if (it.isAdded) {
                    it.dismiss()
                }
            }
            mLoadingFragment?.let {
                if (it.isAdded) {
                    it.dismiss()
                }
            }
            mLoadingDialog?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }
        }
        val responseException = handleException(e)
        callback.onFailed(responseException)
    }

    override fun onComplete() {
        if (mIsShowLoading) {
            mSupportLoadingFragment?.let {
                if (it.isAdded) {
                    it.dismiss()
                }
            }
            mLoadingFragment?.let {
                if (it.isAdded) {
                    it.dismiss()
                }
            }
            mLoadingDialog?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }
        }
        callback.onComplete()
    }
}
