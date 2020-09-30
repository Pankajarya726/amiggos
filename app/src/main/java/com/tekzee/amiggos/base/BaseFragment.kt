package com.tekzee.mallortaxi.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.kaopiz.kprogresshud.KProgressHUD
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.util.NetWorkConection

abstract class BaseFragment : Fragment(), BaseMainView {

    lateinit var hud: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hud = KProgressHUD.create(activity)
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
        return NetWorkConection.isNEtworkConnected(activity!!.baseContext)
    }

    override fun hideKeyboard(){
        if(activity!=null){
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity!!.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }

}