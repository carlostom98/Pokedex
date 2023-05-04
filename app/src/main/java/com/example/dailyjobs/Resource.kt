package com.example.dailyjobs

sealed class Resource<T> (val data:T?=null, message:String?=null){
    class Succes<T>(data: T?):Resource<T>(data)
    class Error<T>(message: String?,data: T?=null):Resource<T>(data, message)
}
