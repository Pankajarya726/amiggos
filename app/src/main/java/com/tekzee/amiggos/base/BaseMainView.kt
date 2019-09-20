package com.tekzee.mallortaxi.base

interface BaseMainView{


    fun showProgressbar()
    fun hideProgressbar()
    fun hideKeyboard()

    fun checkInternet(): Boolean
    fun validateError(message: String)

}