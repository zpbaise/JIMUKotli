package com.shiye.componentone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.luojilab.component.componentlib.router.ui.UIRouter
import com.luojilab.router.facade.annotation.RouteNode
import com.shiye.baselibrary.RouterConst

@RouteNode(path = RouterConst.Path.PATH_COMP_ONE,desc = "组件1 activity")
class OneCompActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_comp)
    }
    fun retrunAppComp(view:View){
        UIRouter.getInstance()
                .openUri(this@OneCompActivity,
                        RouterConst.SCHEME+"://" + RouterConst.Host.HOST_APP + RouterConst.Path.PATH_APP, null)

    }
}
