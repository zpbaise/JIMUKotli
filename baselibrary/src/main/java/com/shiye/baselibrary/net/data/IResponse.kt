package com.shiye.baselibrary.net.data

/**
 * Created by issuser on 2018/7/9.
 * 服务器响应返回数据的根类型接口，
 * 用于添加公共字段，
 * 响应的数据类型都应该实现此字段
 *
 */
interface IResponse<T> {
    var code:String
    var message:String
    var data: T?
}