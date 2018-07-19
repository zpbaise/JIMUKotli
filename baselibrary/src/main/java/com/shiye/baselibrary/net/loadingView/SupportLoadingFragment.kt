package com.shiye.baselibrary.net.loadingView

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.shiye.baselibrary.R


/**
 * Created by issuser on 2018/7/9.
 * 网络请求的Loading动画
 */

class SupportLoadingFragment : DialogFragment() {
    private var mLoadingView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Loading_Dialog)
    }

    override fun onStart() {
        super.onStart()
        //设置宽度顶满屏幕,无左右留白
        val dm = DisplayMetrics()
        activity?.let { it.windowManager.defaultDisplay.getMetrics(dm) }
        dialog.window?.let { it.setLayout(dm.widthPixels, it.attributes.height) }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mLoadingView == null) {
            mLoadingView = inflater.inflate(R.layout.loading_dialog, container, false)
        }
        return mLoadingView
    }

    fun setLoadingView(loadingView: View?) {
        mLoadingView = loadingView
    }
}
