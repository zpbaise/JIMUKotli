package com.shiye.baselibrary.net.loadingView

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View

import com.shiye.baselibrary.R

/**
 * Created by issuser on 2018/7/19.
 */
class LoadingDialog @JvmOverloads constructor(context: Context, themeResId: Int = -1) : AlertDialog(context, themeResId) {
    private var mLoadingView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoadingView?.let { setContentView(it) }
        setContentView(R.layout.loading_dialog)
    }

    fun setLoadingView(loadingView: View?) {
        mLoadingView = loadingView
    }
}
