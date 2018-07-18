package com.shiye.jimukotlin.kotlin

import android.util.Log
import com.luojilab.component.componentlib.applicationlike.IApplicationLike
import com.luojilab.component.componentlib.router.ui.UIRouter
import com.shiye.baselibrary.RouterConst

/**
 * Created by shiye on 2018/5/25.
 *
 */
class AppLike:IApplicationLike {

    override fun onCreate() {
        UIRouter.getInstance().registerUI(RouterConst.Host.HOST_APP)
    }
    override fun onStop() {
        UIRouter.getInstance().unregisterUI(RouterConst.Host.HOST_APP)
    }
}