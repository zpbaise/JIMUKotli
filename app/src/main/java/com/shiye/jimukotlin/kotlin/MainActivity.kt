package com.shiye.jimukotlin.kotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.luojilab.component.componentlib.router.Router
import com.luojilab.component.componentlib.router.ui.UIRouter
import com.luojilab.router.facade.annotation.RouteNode
import com.shiye.baselibrary.RouterConst
import com.shiye.jimukotlin.R

import kotlinx.android.synthetic.main.activity_main.*

@RouteNode(path = RouterConst.Path.PATH_APP,desc = "app 组件 mainactivity")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun registerCompOne(view:View){
        Router.registerComponent(RouterConst.CompName.NAME_COMP_ONE)
    }
    fun unregisterCompOne(view:View){
        Router.unregisterComponent(RouterConst.CompName.NAME_COMP_ONE)
    }
    fun startCompOne(view:View){
        UIRouter.getInstance().openUri(this,
                RouterConst.SCHEME+"://"+RouterConst.Host.HOST_COMP_ONE+RouterConst.Path.PATH_COMP_ONE,
                null)
    }
}
