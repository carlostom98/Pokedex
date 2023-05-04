package com.example.dailyjobs

sealed class Resource<T> (val data:T?=null, val message:String?=null){
    class Succes<T>(data: T?):Resource<T>(data)
    class Error<T>(data: T?=null,message: String?=null):Resource<T>(data, message)
}
