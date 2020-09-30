package com.tekzee.amiggos.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.kaopiz.kprogresshud.KProgressHUD
import com.shashank.sony.fancytoastlib.FancyToast


var progresshud: KProgressHUD? = null

fun Context.toast(message: String){
    FancyToast.makeText(this,message,FancyToast.LENGTH_LONG,FancyToast.DEFAULT,false).show()
}

fun Context.Successtoast(message: String){
    FancyToast.makeText(this,message,FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show()
}

fun Context.Errortoast(message: String){
    FancyToast.makeText(this,message,FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show()
}

fun Context.showProgressBar(){
   progresshud =KProgressHUD.create(this)
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setLabel("Please wait")
        .setCancellable(false)
       .show()
}

fun Context.hideProgressBar(){
    if(progresshud != null)
        progresshud?.dismiss()
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
}