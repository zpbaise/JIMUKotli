package com.shiye.componentone

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.luojilab.component.componentlib.router.ui.UIRouter
import com.luojilab.router.facade.annotation.RouteNode
import com.shiye.baselibrary.RouterConst
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

@RouteNode(path = RouterConst.Path.PATH_COMP_ONE, desc = "组件1 activity")
class OneCompActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_one_comp)
        relativeLayout {
            val button = button {
                id = R.id.retrun_app_component_bt
                text = "返回app组件"
                textSize = px2sp(15)
                onClick {
                    UIRouter.getInstance()
                            .openUri(this@OneCompActivity,
                                    RouterConst.SCHEME + "://" + RouterConst.Host.HOST_APP + RouterConst.Path.PATH_APP, null)
                }
            }.lparams(width = matchParent, height = wrapContent) {
                topMargin = dip(20)
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL

                textView("scaleAnim") {
                    gravity = Gravity.CENTER
                    textSize = 20f
                    backgroundColorResource = R.color.color1
                }.lparams {

                    width = dip(0)
                    height = wrapContent
                    weight = 1f
                }

                textView("alphaAnim") {
                    gravity = Gravity.CENTER
                    textSize = 16f
                    backgroundColorResource = R.color.color2
                }.lparams {
                    width = dip(0)
                    height = wrapContent
                    weight = 1f
                }

                textView("translateAnim") {
                    backgroundColorResource = R.color.color3
                    gravity = Gravity.CENTER

                }.lparams {
                    width = dip(0)
                    height = wrapContent
                    weight = 1f
                }

                textView("rotateAnim") {
                    backgroundColorResource = R.color.color4
                    gravity = Gravity.CENTER
                }.lparams {
                    width = dip(0)
                    height = wrapContent
                    weight = 1f
                }

            }.lparams(width = matchParent, height = wrapContent) {
                topMargin = dip(20)
                below(button)
            }
        }
    }
}
