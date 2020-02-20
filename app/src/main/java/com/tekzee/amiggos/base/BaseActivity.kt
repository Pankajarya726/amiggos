package com.tekzee.mallortaxi.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.tekzee.mallortaxi.util.NetWorkConection

abstract class BaseActivity : AppCompatActivity(),BaseMainView
{


    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
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