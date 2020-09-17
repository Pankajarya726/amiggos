package com.tekzee.amiggoss.ui.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.databinding.WebviewActivityBinding
import com.tekzee.amiggoss.constant.ConstantLib
import com.orhanobut.logger.Logger


class WebViewActivity: AppCompatActivity(){


    lateinit var binding: WebviewActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.webview_activity)
        setupToolBar()
        setupWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webview.settings.javaScriptEnabled = true
        Logger.d(ConstantLib.PAGEURL);
        binding.webview.loadUrl(intent.getStringExtra(ConstantLib.PAGEURL)!!)
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }




}