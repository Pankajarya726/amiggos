package com.tekzee.mallortaxi.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.tekzee.mallortaxi.util.NetWorkConection

abstract class BaseFragment : Fragment(),BaseMainView {

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
    }

    override fun showProgressbar() {
        hideKeyboard()
        progressDialog.show()
    }

    override fun hideProgressbar() {
        progressDialog.dismiss()
    }

    override fun checkInternet(): Boolean {
        return NetWorkConection.isNEtworkConnected(activity!!.baseContext)
    }

    override fun hideKeyboard(){
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity!!.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}