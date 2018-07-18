package com.shiye.jimukotlin.kotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import com.luojilab.component.componentlib.router.Router
import com.luojilab.component.componentlib.router.ui.UIRouter
import com.luojilab.router.facade.annotation.RouteNode
import com.shiye.baselibrary.RouterConst
import com.shiye.baselibrary.net.callback.CallBack
import com.shiye.baselibrary.net.ApiServiceFactory
import com.shiye.baselibrary.net.data.BaseResponse
import com.shiye.baselibrary.net.exception.ResponseException
import com.shiye.baselibrary.net.utils.parseResponseConstrInner
import com.shiye.baselibrary.net.utils.parseResponseConstrOuter
import com.shiye.baselibrary.net.utils.parseResponseConstrOuterList
import com.shiye.baselibrary.net.utils.parseResponseUnconstrOuterList
import com.shiye.jimukotlin.R
import com.shiye.jimukotlin.R.id.*
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import retrofit2.Response
import retrofit2.http.*

@RouteNode(path = RouterConst.Path.PATH_APP, desc = "app 组件 mainactivity")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val registerBt = findViewById<AppCompatButton>(register_bt)
        val unregisterBt = findViewById<AppCompatButton>(unregister_bt)
        val startBt = findViewById<AppCompatButton>(start_bt)
        registerBt.setOnClickListener { Router.registerComponent(RouterConst.CompName.NAME_COMP_ONE) }
        unregisterBt.setOnClickListener { Router.unregisterComponent(RouterConst.CompName.NAME_COMP_ONE) }
        startBt.setOnClickListener {
            UIRouter.getInstance().openUri(this,
                    RouterConst.SCHEME + "://" + RouterConst.Host.HOST_COMP_ONE + RouterConst.Path.PATH_COMP_ONE,
                    null)
        }

        /*val apiService = ApiServiceFactory.defaultCreateApiService(this, "http://www.meandy.com/",
                APIService::class.java)*/


        /*parseResponseUnconstrOuterList(this,apiService.getArray(),
                AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)),
                object : CallBack<BaseResponse<List<Any>>> {
                    override fun onBefore(disposable: Disposable) {
                    }

                    override fun onSucceed(result: BaseResponse<List<Any>>) {
                        AnkoLogger("AAA").error(">>>2-2 <<<"+result?.toString())
                    }

                    override fun onFailed(e: ResponseException) {
                        AnkoLogger("AAA").error(">>>2-2 error <<<"+e)
                    }

                    override fun onComplete() {
                    }
                }
                )*/

        /*parseResponseConstrOuterList(this,apiService.getArrayNull(2),
                AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)),
                object : CallBack<BaseResponse<List<ObjTestData>>> {
                    override fun onBefore(disposable: Disposable) {

                    }

                    override fun onSucceed(result: BaseResponse<List<ObjTestData>>) {
                        AnkoLogger("AAA").error(">>>3 <<<"+result?.toString())
                    }

                    override fun onFailed(e: ResponseException) {
                        AnkoLogger("AAA").error(">>>3 error <<<"+e)
                    }

                    override fun onComplete() {
                    }
                })*/
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

    public interface APIService {
        //        http://www.meandy.com/api/apidemo/query/pengzou/json/10/objtest/a
//        @FormUrlEncoded
//        @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
        @POST("/api/apidemo/query/pengzou/json/10/objtest/a") //这里的{id} 表示是一个变量
        fun getObject(/*@Field("type") type: Int*/): Observable<Response<BaseResponse<ObjTestData>>>

        @FormUrlEncoded
//        @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
        @POST("/api/apidemo/query/pengzou/json/10/objtest/b") //这里的{id} 表示是一个变量
        fun getObjectNull(@Field("type") type: Int): Observable<Response<BaseResponse<ObjTestData>>>

        //        @FormUrlEncoded
//        @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
        @POST("/api/apidemo/query/pengzou/json/10/arrayTest/a") //这里的{id} 表示是一个变量
        fun getArray(/*@Field("type") type: Int*/): Observable<Response<BaseResponse<List<Any>>>>

        @FormUrlEncoded
//        @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
        @POST("/api/apidemo/query/pengzou/json/10/arrayTest/b") //这里的{id} 表示是一个变量
        fun getArrayNull(@Field("type") type: Int): Observable<Response<BaseResponse<List<ObjTestData>>>>
    }
}
