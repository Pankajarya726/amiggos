//package com.tekzee.amiggos.ui.videoplayer
//
//import android.content.res.Configuration
//import android.os.Bundle
//import android.widget.Toast
//import com.khizar1556.mkvideoplayer.MKPlayer
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.ui.imagepanaroma.model.ImageVideoData
//import com.tekzee.amiggos.base.BaseActivity
//import com.tekzee.amiggos.constant.ConstantLib
//
//
//
//class MyPlayerActivity : BaseActivity() {
//
//
//    private lateinit var dataItem: ImageVideoData
//    private var player: MKPlayer? = null
//    override fun validateError(message: String) {
//        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_myplayer)
//        dataItem = intent.getSerializableExtra(ConstantLib.IMAGE_DATA) as ImageVideoData
//        player = MKPlayer(this)
//        player!!.play(dataItem.url);
//
//    }
//
//
//    override fun onPause() {
//        super.onPause()
//        if (player != null) {
//            player!!.onPause()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (player != null) {
//            player!!.onResume()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (player!= null) {
//            player!!.onDestroy()
//        }
//    }
//
//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        if (player != null) {
//            player!!.onConfigurationChanged(newConfig)
//        }
//    }
//
//    override fun onBackPressed() {
//        if (player != null && player!!.onBackPressed()) {
//            return
//        }
//        super.onBackPressed()
//    }
//
//}