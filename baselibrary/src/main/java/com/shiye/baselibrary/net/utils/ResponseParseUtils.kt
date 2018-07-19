@file:JvmName("ResponseParseUtils")

package com.shiye.baselibrary.net.utils

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.view.View
import com.shiye.baselibrary.R
import com.shiye.baselibrary.net.callback.CallBack
import com.shiye.baselibrary.net.observer.ResponseObserver
import com.shiye.baselibrary.net.data.IResponse
import com.shiye.baselibrary.net.data.IResponseData
import com.shiye.baselibrary.net.exception.*
import com.shiye.baselibrary.net.loadingView.LoadingDialog
import com.uber.autodispose.AutoDisposeConverter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.loading_dialog.view.*
import retrofit2.Response

/**
 * Created by issuser on 2018/7/9.
 * 解析网络返回数据的工具类
 * 默认response的json格式为
 * {
 *   "code":"",
 *   "message":"",
 *   "data":{}或"data":[]
 * }
 */

/**
 * 解析请求结果，无网络加载loading视图，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是[IResponseData]类型，
 * 得到的结果是[IResponse]<[IResponseData]>类型
 * @param context
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrOuter(context: Context,
                                                                               response: Observable<Response<outer>>,
                                                                               autoDisposable: AutoDisposeConverter<outer>,
                                                                               callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(callBack))
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是[IResponseData]类型，
 * 得到的结果是[IResponse]<[IResponseData]>类型
 * @param activity
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrOuter(activity: Activity,
                                                                               loadingviewCancelable: Boolean = true,
                                                                               response: Observable<Response<outer>>,
                                                                               autoDisposable: AutoDisposeConverter<outer>,
                                                                               callBack: CallBack<outer>): Unit? {
    return parseResponseConstrOuter(activity, null, loadingviewCancelable, response, autoDisposable, callBack)
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是[IResponseData]类型，
 * 得到的结果是[IResponse]<[IResponseData]>类型
 * @param activity
 * @param loadingView 网络加载loading的自定view
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrOuter(activity: Activity,
                                                                               loadingView: View?,
                                                                               loadingviewCancelable: Boolean = true,
                                                                               response: Observable<Response<outer>>,
                                                                               autoDisposable: AutoDisposeConverter<outer>,
                                                                               callBack: CallBack<outer>): Unit? {
    return checkResponse(activity.applicationContext, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(activity, loadingView, loadingviewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是[IResponseData]类型，
 * 得到的结果是[IResponse]<[IResponseData]>类型
 * @param context
 * @param supportFragmentManager support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrOuter(context: Context,
                                                                               supportFragmentManager: FragmentManager?,
                                                                               loadingView: View? = null,
                                                                               loadingViewCancelable: Boolean = true,
                                                                               response: Observable<Response<outer>>,
                                                                               autoDisposable: AutoDisposeConverter<outer>,
                                                                               callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(supportFragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是[IResponseData]类型，
 * 得到的结果是[IResponse]<[IResponseData]>类型
 * @param context
 * @param fragmentManager 非support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrOuter(context: Context,
                                                                               fragmentManager: android.app.FragmentManager?,
                                                                               loadingView: View? = null,
                                                                               loadingViewCancelable: Boolean = true,
                                                                               response: Observable<Response<outer>>,
                                                                               autoDisposable: AutoDisposeConverter<outer>,
                                                                               callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(fragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，无网络加载loading视图，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是List<[IResponseData]>类型，
 * 得到的结果是[IResponse]<List<[IResponseData]>>类型
 * @param context
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<List<inner>>> parseResponseConstrOuterList(context: Context,
                                                                                         response: Observable<Response<outer>>,
                                                                                         autoDisposable: AutoDisposeConverter<outer>,
                                                                                         callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(callBack))
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是List<[IResponseData]>类型，
 * 得到的结果是[IResponse]<List<[IResponseData]>>类型
 * @param activity
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrOuterList(activity: Activity,
                                                                                   loadingviewCancelable: Boolean = true,
                                                                                   response: Observable<Response<outer>>,
                                                                                   autoDisposable: AutoDisposeConverter<outer>,
                                                                                   callBack: CallBack<outer>): Unit? {
    return parseResponseConstrOuterList(activity, null, loadingviewCancelable, response, autoDisposable, callBack)
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是List<[IResponseData]>类型，
 * 得到的结果是[IResponse]<List<[IResponseData]>>类型
 * @param activity
 * @param loadingView 自定义loadingview视图
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrOuterList(activity: Activity,
                                                                                   loadingView: View?,
                                                                                   loadingviewCancelable: Boolean = true,
                                                                                   response: Observable<Response<outer>>,
                                                                                   autoDisposable: AutoDisposeConverter<outer>,
                                                                                   callBack: CallBack<outer>): Unit? {
    return checkResponse(activity.applicationContext, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(activity, loadingView, loadingviewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是List<[IResponseData]>类型，
 * 得到的结果是[IResponse]<List<[IResponseData]>>类型
 * @param context
 * @param supportFragmentManager support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<List<inner>>> parseResponseConstrOuterList(context: Context,
                                                                                         supportFragmentManager: FragmentManager?,
                                                                                         loadingView: View? = null,
                                                                                         loadingViewCancelable: Boolean = true,
                                                                                         response: Observable<Response<outer>>,
                                                                                         autoDisposable: AutoDisposeConverter<outer>,
                                                                                         callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(supportFragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是List<[IResponseData]>类型，
 * 得到的结果是[IResponse]<List<[IResponseData]>>类型
 * @param context
 * @param fragmentManager 非support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<List<inner>>> parseResponseConstrOuterList(context: Context,
                                                                                         fragmentManager: android.app.FragmentManager?,
                                                                                         loadingView: View? = null,
                                                                                         loadingViewCancelable: Boolean = true,
                                                                                         response: Observable<Response<outer>>,
                                                                                         autoDisposable: AutoDisposeConverter<outer>,
                                                                                         callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(fragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，无网络加载loading视图，response的json格式强制约束为默认格式，d
 * ata字段的类型有强制约束，必须是[IResponseData]类型，
 * 得到的结果是[IResponseData]类型
 * @param context
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrInner(context: Context,
                                                                               response: Observable<Response<outer>>,
                                                                               autoDisposable: AutoDisposeConverter<inner>,
                                                                               callBack: CallBack<inner>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, inner?> {
                override fun apply(t: outer): inner? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(callBack))
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，d
 * ata字段的类型有强制约束，必须是[IResponseData]类型，
 * 得到的结果是[IResponseData]类型
 * @param activity
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrInner(activity: Activity,
                                                                               loadingViewCancelable: Boolean = true,
                                                                               response: Observable<Response<outer>>,
                                                                               autoDisposable: AutoDisposeConverter<inner>,
                                                                               callBack: CallBack<inner>): Unit? {
    return parseResponseConstrInner(activity, null, loadingViewCancelable, response, autoDisposable, callBack)
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，d
 * ata字段的类型有强制约束，必须是[IResponseData]类型，
 * 得到的结果是[IResponseData]类型
 * @param activity
 * @param loadingView 自定义loadingview视图
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrInner(activity: Activity,
                                                                               loadingView: View?,
                                                                               loadingViewCancelable: Boolean = true,
                                                                               response: Observable<Response<outer>>,
                                                                               autoDisposable: AutoDisposeConverter<inner>,
                                                                               callBack: CallBack<inner>): Unit? {
    return checkResponse(activity.applicationContext, response)
            ?.map(object : Function<outer?, inner?> {
                override fun apply(t: outer): inner? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(activity, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，d
 * ata字段的类型有强制约束，必须是[IResponseData]类型，
 * 得到的结果是[IResponseData]类型
 * @param context
 * @param supportFragmentManager support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrInner(context: Context,
                                                                               supportFragmentManager: FragmentManager?,
                                                                               loadingView: View? = null,
                                                                               loadingViewCancelable: Boolean = true,
                                                                               response: Observable<Response<outer>>,
                                                                               autoDisposable: AutoDisposeConverter<inner>,
                                                                               callBack: CallBack<inner>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, inner?> {
                override fun apply(t: outer): inner? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(supportFragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，d
 * ata字段的类型有强制约束，必须是[IResponseData]类型，
 * 得到的结果是[IResponseData]类型
 * @param context
 * @param fragmentManager 非support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<inner>> parseResponseConstrInner(context: Context,
                                                                               fragmentManager: android.app.FragmentManager?,
                                                                               loadingView: View? = null,
                                                                               loadingViewCancelable: Boolean = true,
                                                                               response: Observable<Response<outer>>,
                                                                               autoDisposable: AutoDisposeConverter<inner>,
                                                                               callBack: CallBack<inner>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, inner?> {
                override fun apply(t: outer): inner? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(fragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，无网络加载loading视图，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是List<[IResponseData]>类型，
 * 得到的结果是List<[IResponseData]>类型
 * @param context
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<List<inner>>> parseResponseConstrInnerList(context: Context,
                                                                                         response: Observable<Response<outer>>,
                                                                                         autoDisposable: AutoDisposeConverter<List<inner>>,
                                                                                         callBack: CallBack<List<inner>>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, List<inner>?> {
                override fun apply(t: outer): List<inner>? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(callBack))
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是List<[IResponseData]>类型，
 * 得到的结果是List<[IResponseData]>类型
 * @param activity
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<List<inner>>> parseResponseConstrInnerList(activity: Activity,
                                                                                         loadingViewCancelable: Boolean = true,
                                                                                         response: Observable<Response<outer>>,
                                                                                         autoDisposable: AutoDisposeConverter<List<inner>>,
                                                                                         callBack: CallBack<List<inner>>): Unit? {
    return parseResponseConstrInnerList(activity, null, loadingViewCancelable, response, autoDisposable, callBack)
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是List<[IResponseData]>类型，
 * 得到的结果是List<[IResponseData]>类型
 * @param activity
 * @param loadingView 网络加载loading的自定view
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<List<inner>>> parseResponseConstrInnerList(activity: Activity,
                                                                                         loadingView: View?,
                                                                                         loadingViewCancelable: Boolean = true,
                                                                                         response: Observable<Response<outer>>,
                                                                                         autoDisposable: AutoDisposeConverter<List<inner>>,
                                                                                         callBack: CallBack<List<inner>>): Unit? {
    return checkResponse(activity.applicationContext, response)
            ?.map(object : Function<outer?, List<inner>?> {
                override fun apply(t: outer): List<inner>? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(activity, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是List<[IResponseData]>类型，
 * 得到的结果是List<[IResponseData]>类型
 * @param context
 * @param supportFragmentManager support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<List<inner>>> parseResponseConstrInnerList(context: Context,
                                                                                         supportFragmentManager: FragmentManager?,
                                                                                         loadingView: View? = null,
                                                                                         loadingViewCancelable: Boolean = true,
                                                                                         response: Observable<Response<outer>>,
                                                                                         autoDisposable: AutoDisposeConverter<List<inner>>,
                                                                                         callBack: CallBack<List<inner>>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, List<inner>?> {
                override fun apply(t: outer): List<inner>? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(supportFragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型有强制约束，必须是List<[IResponseData]>类型，
 * 得到的结果是List<[IResponseData]>类型
 * @param context
 * @param fragmentManager 非support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner : IResponseData, outer : IResponse<List<inner>>> parseResponseConstrInnerList(context: Context,
                                                                                         fragmentManager: android.app.FragmentManager?,
                                                                                         loadingView: View? = null,
                                                                                         loadingViewCancelable: Boolean = true,
                                                                                         response: Observable<Response<outer>>,
                                                                                         autoDisposable: AutoDisposeConverter<List<inner>>,
                                                                                         callBack: CallBack<List<inner>>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, List<inner>?> {
                override fun apply(t: outer): List<inner>? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(fragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，无网络加载loading视图，response的json格式强制约束为默认格式，
 * data字段的类型无约束，得到的结果是[IResponse]<[IResponseData]>类型
 * @param context
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<inner>> parseResponseUnconstrOuter(context: Context,
                                                                 response: Observable<Response<outer>>,
                                                                 autoDisposable: AutoDisposeConverter<outer>,
                                                                 callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(callBack))
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无约束，得到的结果是[IResponse]<[IResponseData]>类型
 * @param activity
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<inner>> parseResponseUnconstrOuter(activity: Activity,
                                                                 loadingViewCancelable: Boolean = true,
                                                                 response: Observable<Response<outer>>,
                                                                 autoDisposable: AutoDisposeConverter<outer>,
                                                                 callBack: CallBack<outer>): Unit? {
    return parseResponseUnconstrOuter(activity, null, loadingViewCancelable, response, autoDisposable, callBack)
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无约束，得到的结果是[IResponse]<[IResponseData]>类型
 * @param activity
 * @param loadingView 网络加载loading的自定view
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<inner>> parseResponseUnconstrOuter(activity: Activity,
                                                                 loadingView: View?,
                                                                 loadingViewCancelable: Boolean = true,
                                                                 response: Observable<Response<outer>>,
                                                                 autoDisposable: AutoDisposeConverter<outer>,
                                                                 callBack: CallBack<outer>): Unit? {
    return checkResponse(activity.applicationContext, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(activity, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无约束，得到的结果是[IResponse]<[IResponseData]>类型
 * @param context
 * @param supportFragmentManager support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<inner>> parseResponseUnconstrOuter(context: Context,
                                                                 supportFragmentManager: FragmentManager?,
                                                                 loadingView: View? = null,
                                                                 loadingViewCancelable: Boolean = true,
                                                                 response: Observable<Response<outer>>,
                                                                 autoDisposable: AutoDisposeConverter<outer>,
                                                                 callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(supportFragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无约束，得到的结果是[IResponse]<[IResponseData]>类型
 *  @param context
 * @param fragmentManager 非support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */

fun <inner, outer : IResponse<inner>> parseResponseUnconstrOuter(context: Context,
                                                                 fragmentManager: android.app.FragmentManager?,
                                                                 loadingView: View? = null,
                                                                 loadingViewCancelable: Boolean = true,
                                                                 response: Observable<Response<outer>>,
                                                                 autoDisposable: AutoDisposeConverter<outer>,
                                                                 callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(fragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，无网络加载loading视图，response的json格式强制约束为默认格式，
 * data字段的类型无约束，得到的结果是[IResponse]<List<[IResponseData]>>类型
 * @param context
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<List<inner>>> parseResponseUnconstrOuterList(context: Context,
                                                                           response: Observable<Response<outer>>,
                                                                           autoDisposable: AutoDisposeConverter<outer>,
                                                                           callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(callBack))
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无约束，得到的结果是[IResponse]<List<[IResponseData]>>类型
 * @param activity
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<List<inner>>> parseResponseUnconstrOuterList(activity: Activity,
                                                                           loadingViewCancelable: Boolean = true,
                                                                           response: Observable<Response<outer>>,
                                                                           autoDisposable: AutoDisposeConverter<outer>,
                                                                           callBack: CallBack<outer>): Unit? {
    return parseResponseUnconstrOuterList(activity, null, loadingViewCancelable, response, autoDisposable, callBack)
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无约束，得到的结果是[IResponse]<List<[IResponseData]>>类型
 * @param activity
 * @param loadingView 网络加载loading的自定view
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<List<inner>>> parseResponseUnconstrOuterList(activity: Activity,
                                                                           loadingView: View?,
                                                                           loadingViewCancelable: Boolean = true,
                                                                           response: Observable<Response<outer>>,
                                                                           autoDisposable: AutoDisposeConverter<outer>,
                                                                           callBack: CallBack<outer>): Unit? {
    return checkResponse(activity.applicationContext, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(activity, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无约束，得到的结果是[IResponse]<List<[IResponseData]>>类型
 * @param context
 * @param supportFragmentManager support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<List<inner>>> parseResponseUnconstrOuterList(context: Context,
                                                                           supportFragmentManager: FragmentManager?,
                                                                           loadingView: View? = null,
                                                                           loadingViewCancelable: Boolean = true,
                                                                           response: Observable<Response<outer>>,
                                                                           autoDisposable: AutoDisposeConverter<outer>,
                                                                           callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(supportFragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无约束，得到的结果是[IResponse]<List<[IResponseData]>>类型
 * @param context
 * @param fragmentManager 非support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<List<inner>>> parseResponseUnconstrOuterList(context: Context,
                                                                           fragmentManager: android.app.FragmentManager?,
                                                                           loadingView: View? = null,
                                                                           loadingViewCancelable: Boolean = true,
                                                                           response: Observable<Response<outer>>,
                                                                           autoDisposable: AutoDisposeConverter<outer>,
                                                                           callBack: CallBack<outer>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(fragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，无网络加载loading视图，response的json格式强制约束为默认格式，
 * data字段的类型无强制约束，得到的结果是[IResponseData]类型
 * @param context
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<inner>> parseResponseUnconstrInner(context: Context,
                                                                 response: Observable<Response<outer>>,
                                                                 autoDisposable: AutoDisposeConverter<inner>,
                                                                 callBack: CallBack<inner>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, inner?> {
                override fun apply(t: outer): inner? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(callBack))
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无强制约束，得到的结果是[IResponseData]类型
 * @param activity
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<inner>> parseResponseUnconstrInner(activity: Activity,
                                                                 loadingViewCancelable: Boolean = true,
                                                                 response: Observable<Response<outer>>,
                                                                 autoDisposable: AutoDisposeConverter<inner>,
                                                                 callBack: CallBack<inner>): Unit? {
    return parseResponseUnconstrInner(activity, null, loadingViewCancelable, response, autoDisposable, callBack)
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无强制约束，得到的结果是[IResponseData]类型
 * @param activity
 * @param loadingView 网络加载loading的自定view
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<inner>> parseResponseUnconstrInner(activity: Activity,
                                                                 loadingView: View?,
                                                                 loadingViewCancelable: Boolean = true,
                                                                 response: Observable<Response<outer>>,
                                                                 autoDisposable: AutoDisposeConverter<inner>,
                                                                 callBack: CallBack<inner>): Unit? {
    return checkResponse(activity.applicationContext, response)
            ?.map(object : Function<outer?, inner?> {
                override fun apply(t: outer): inner? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(activity, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无强制约束，得到的结果是[IResponseData]类型
 * @param context
 * @param supportFragmentManager support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<inner>> parseResponseUnconstrInner(context: Context,
                                                                 supportFragmentManager: FragmentManager?,
                                                                 loadingView: View? = null,
                                                                 loadingViewCancelable: Boolean = true,
                                                                 response: Observable<Response<outer>>,
                                                                 autoDisposable: AutoDisposeConverter<inner>,
                                                                 callBack: CallBack<inner>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, inner?> {
                override fun apply(t: outer): inner? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(supportFragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无强制约束，得到的结果是[IResponseData]类型
 * @param context
 * @param fragmentManager 非support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<inner>> parseResponseUnconstrInner(context: Context,
                                                                 fragmentManager: android.app.FragmentManager?,
                                                                 loadingView: View? = null,
                                                                 loadingViewCancelable: Boolean = true,
                                                                 response: Observable<Response<outer>>,
                                                                 autoDisposable: AutoDisposeConverter<inner>,
                                                                 callBack: CallBack<inner>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, inner?> {
                override fun apply(t: outer): inner? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(fragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，无网络加载loading视图，response的json格式强制约束为默认格式，
 * data字段的类型无强制约束，得到的结果是List<[IResponseData]>类型
 * @param context
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<List<inner>>> parseResponseUnconstrInnerList(context: Context,
                                                                           response: Observable<Response<outer>>,
                                                                           autoDisposable: AutoDisposeConverter<List<inner>>,
                                                                           callBack: CallBack<List<inner>>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, List<inner>?> {
                override fun apply(t: outer): List<inner>? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(callBack))
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无强制约束，得到的结果是List<[IResponseData]>类型
 * @param activity
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<List<inner>>> parseResponseUnconstrInnerList(activity: Activity,
                                                                           loadingViewCancelable: Boolean = true,
                                                                           response: Observable<Response<outer>>,
                                                                           autoDisposable: AutoDisposeConverter<List<inner>>,
                                                                           callBack: CallBack<List<inner>>): Unit? {
    return parseResponseUnconstrInnerList(activity, null, loadingViewCancelable, response, autoDisposable, callBack)
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无强制约束，得到的结果是List<[IResponseData]>类型
 * @param activity
 * @param loadingView 网络加载loading的自定view
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<List<inner>>> parseResponseUnconstrInnerList(activity: Activity,
                                                                           loadingView: View?,
                                                                           loadingViewCancelable: Boolean = true,
                                                                           response: Observable<Response<outer>>,
                                                                           autoDisposable: AutoDisposeConverter<List<inner>>,
                                                                           callBack: CallBack<List<inner>>): Unit? {
    return checkResponse(activity.applicationContext, response)
            ?.map(object : Function<outer?, List<inner>?> {
                override fun apply(t: outer): List<inner>? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(activity, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无强制约束，得到的结果是List<[IResponseData]>类型
 * @param context
 * @param supportFragmentManager support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<List<inner>>> parseResponseUnconstrInnerList(context: Context,
                                                                           supportFragmentManager: FragmentManager?,
                                                                           loadingView: View? = null,
                                                                           loadingViewCancelable: Boolean = true,
                                                                           response: Observable<Response<outer>>,
                                                                           autoDisposable: AutoDisposeConverter<List<inner>>,
                                                                           callBack: CallBack<List<inner>>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, List<inner>?> {
                override fun apply(t: outer): List<inner>? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(supportFragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式强制约束为默认格式，
 * data字段的类型无强制约束，得到的结果是List<[IResponseData]>类型
 * @param context
 * @param fragmentManager 非support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <inner, outer : IResponse<List<inner>>> parseResponseUnconstrInnerList(context: Context,
                                                                           fragmentManager: android.app.FragmentManager?,
                                                                           loadingView: View? = null,
                                                                           loadingViewCancelable: Boolean = true,
                                                                           response: Observable<Response<outer>>,
                                                                           autoDisposable: AutoDisposeConverter<List<inner>>,
                                                                           callBack: CallBack<List<inner>>): Unit? {
    return checkResponse(context, response)
            ?.map(object : Function<outer?, List<inner>?> {
                override fun apply(t: outer): List<inner>? {
                    return t.data
                }
            })
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(fragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，无网络加载loading视图，response的json格式无强制约束，得到的结果是T类型
 * @param context
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <T> parseResponse(context: Context,
                      response: Observable<Response<T>>,
                      autoDisposable: AutoDisposeConverter<T>,
                      callBack: CallBack<T>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(callBack))
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式无强制约束，得到的结果是T类型
 * @param activity
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <T> parseResponse(activity: Activity,
                      loadingViewCancelable: Boolean = true,
                      response: Observable<Response<T>>,
                      autoDisposable: AutoDisposeConverter<T>,
                      callBack: CallBack<T>): Unit? {
    return parseResponse(activity, null, loadingViewCancelable, response, autoDisposable, callBack)
}

/**
 * 兼容低版本,低版本使用[Activity]的时候，此方法会提供一个网络加载的loading视图，此视图是一个Dialog
 * 解析请求结果，response的json格式无强制约束，得到的结果是T类型
 * @param activity
 * @param loadingView 网络加载loading的自定view
 * @param loadingviewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <T> parseResponse(activity: Activity,
                      loadingView: View?,
                      loadingViewCancelable: Boolean = true,
                      response: Observable<Response<T>>,
                      autoDisposable: AutoDisposeConverter<T>,
                      callBack: CallBack<T>): Unit? {
    return checkResponse(activity.applicationContext, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(activity, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式无强制约束，得到的结果是T类型
 * @param context
 * @param supportFragmentManager support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <T> parseResponse(context: Context,
                      supportFragmentManager: FragmentManager?,
                      loadingView: View? = null,
                      loadingViewCancelable: Boolean = true,
                      response: Observable<Response<T>>,
                      autoDisposable: AutoDisposeConverter<T>,
                      callBack: CallBack<T>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(supportFragmentManager, loadingView, loadingViewCancelable, callBack))
}

/**
 * 解析请求结果，response的json格式无强制约束，得到的结果是T类型
 * @param context
 * @param fragmentManager 非support包的FragmentManager，为null表示不显示网络加载loading
 * @param loadingView 自定义的网络加载loading，当supportFragmentManager不为null是，loadingView为null表示使用默认的加载loading
 * @param loadingViewCancelable 点击边缘之外时，网络加载loading是否可以隐藏。
 *                             true，点击是可以隐藏；false，点击是不可隐藏，且按back键也无法回退
 * @param response 将要执行的网络请求
 * @param autoDisposable AutoDispose框架管理Rxjava生命周期的对象，只需要传递
 *                       AutoDispose.<T>autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
 * @param callBack 网络请求的回调
 */
fun <T> parseResponse(context: Context,
                      fragmentManager: android.app.FragmentManager?,
                      loadingView: View? = null,
                      loadingViewCancelable: Boolean = true,
                      response: Observable<Response<T>>,
                      autoDisposable: AutoDisposeConverter<T>,
                      callBack: CallBack<T>): Unit? {
    return checkResponse(context, response)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.newThread())
            ?.`as`(autoDisposable)
            ?.subscribe(ResponseObserver(fragmentManager, loadingView, loadingViewCancelable, callBack))

}

/**
 * 对网络请求进行过滤，如果设备无网络连接或http响应码不在[200..300)范围之内，则抛出异常，返回有效的网络请求
 */
private fun <T> checkResponse(context: Context,
                              response: Observable<Response<T>>): Observable<T>? {
    return Observable.just(isConnected(context.applicationContext))
            .flatMap {
                if (!it) {
                    throw ResponseException(RuntimeException(MSG_NETWORK_UNCONN), CODE_NETWORK_UNCONN, MSG_NETWORK_UNCONN)
                }
                response
            }
            .map(object : Function<Response<T>, T> {
                override fun apply(t: Response<T>): T? {
                    if (!t.isSuccessful) {
                        throw ResponseException(RuntimeException(MSG_HTTP_EXCEPTION + "response code ${t.code()}"),
                                CODE_HTTP_EXCEPTION,
                                MSG_HTTP_EXCEPTION + "response code ${t.code()}")
                    }
                    return t.body()
                }
            })
}