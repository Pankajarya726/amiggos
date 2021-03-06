package com.tekzee.amiggos.base.repository

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.kaopiz.kprogresshud.KProgressHUD
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.util.NetWorkConection


abstract class BaseFragmentActivity : AppCompatActivity(), BaseMainView
{
    lateinit var hud: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hud = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(false)



    }

    override fun showProgressbar() {
        hideKeyboard()
        hud.show()
    }

    override fun hideProgressbar() {
        hud.dismiss()
    }

    override fun checkInternet(): Boolean {
        return NetWorkConection.isNEtworkConnected(this)
    }

    override fun hideKeyboard(){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }



}