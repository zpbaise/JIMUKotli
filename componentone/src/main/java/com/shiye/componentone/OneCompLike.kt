package com.shiye.componentone

import android.util.Log
import com.luojilab.component.componentlib.applicationlike.IApplicationLike
import com.luojilab.component.componentlib.router.ui.UIRouter
import com.shiye.baselibrary.RouterConst

/**
 * Created by shiye on 2018/5/25.
 *
 */
class OneCompLike:IApplicationLike {
    override fun onCreate() {
        UIRouter.getInstance().registerUI(RouterConst.Host.HOST_COMP_ONE)
        Log.e("component","register component one")
    }

    override fun onStop() {
        UIRouter.getInstance().unregisterUI(RouterConst.Host.HOST_COMP_ONE)
    }
}