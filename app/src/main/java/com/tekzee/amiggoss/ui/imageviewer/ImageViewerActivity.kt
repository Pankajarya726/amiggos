package com.tekzee.amiggoss.ui.imageviewer

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.ImageViewerActivityBinding
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.constant.ConstantLib

class ImageViewerActivity: BaseActivity() {

    private lateinit var binding: ImageViewerActivityBinding
    private var languageData: LanguageData? = null
    private var sharedPreference: SharedPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView(this, R.layout.image_viewer_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()

        Glide.with(applicationContext).load(intent.getStringExtra(ConstantLib.PROFILE_IMAGE))
            .placeholder(R.drawable.user).into(binding.imgProfile)
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klFriendProfileTitle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
}