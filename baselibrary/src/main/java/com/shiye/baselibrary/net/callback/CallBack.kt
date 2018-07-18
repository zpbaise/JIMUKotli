package com.shiye.baselibrary.net.callback

import com.shiye.baselibrary.net.exception.ResponseException

import io.reactivex.disposables.Disposable

/**
 * Created by Administrator on 2018/1/25.
 * 请求网络数据的回调接口
 */

interface CallBack<T> {

    /**
     * 该方法只会执行一次，且在网络数据返回之前执行
     * @param disposable 可以取消当前请求的对象
     * 如果在数据未返回之前，需要取消当前的网络请求可以如下方式取消请求
     * if (!disposable.isDisposed()){
     * disposable.dispose();
     * }
     * 如果当前网络请求还未执行完，但请求网络的activity或fragment因为某些原因销毁了，
     * 可以在onDestroy或onDestroyView()中使用disposable对象销毁当前未执行完的网络请求。
     * 如:
     * 在onBefore方法中将disposable赋值给一个成员变量this.disposable = disposable;
     * 在onDestroy方法中：
     * if (!disposable.isDisposed()){
     * disposable.dispose();
     * }
     */
    fun onBefore(disposable: Disposable)

    /**
     * 网络请求成功的回调
     * @param result 返回的数据
     */
    fun onSucceed(result: T)


    /**
     * 网络请求失败的回调
     * 和onComplete方法互斥，同一个请求只能执行其中的一个方法
     * @param e
     */
    fun onFailed(e: ResponseException)


    /**
     * 网络请求完成的回调
     * 和onFailed方法互斥，同一个请求只能执行其中的一个方法
     */
    fun onComplete()
}
