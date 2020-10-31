package com.tekzee.amiggos.ui.splash

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.SplashActivityBinding
//import com.tekzee.amiggos.ui.login.LoginActivity
import com.tekzee.amiggos.ui.splash.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.splash.model.ViewPageData
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.constant.ConstantLib




class SplashActivity : BaseActivity(), MediaPlayer.OnPreparedListener {


    lateinit var binding: SplashActivityBinding
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    lateinit var mediaPlayer: MediaPlayer
    private var muteStatus: Int = 1   // 1- unmute,2- mute

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            callNextView()
        } else {
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.splash_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupLanguage()
        setupVideoPlayer()
        setupViewPager()
        setupClickListener()
        initData()
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    private fun initData() {

    }

    private fun setupLanguage() {

        binding.getStarted.setText(languageData!!.klGetStarted)

    }

    private fun setupClickListener() {

        binding.imgLogo.setOnClickListener {
            if (mDelayHandler != null) {
                mDelayHandler!!.removeCallbacks(mRunnable)
            }
            callNextView()
        }

//        binding.getStarted.setOnClickListener{
//            sharedPreference!!.save(ConstantLib.ISAGREE,false)
//            sharedPreference!!.save(ConstantLib.SPREAD_CHECKBOX,false)
//            sharedPreference!!.save(ConstantLib.TURNTUP_CHECKBOX,false)
//            sharedPreference!!.save(ConstantLib.OPENMIDED_CHECKBOX,false)
//            val intent = Intent(applicationContext,LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

        binding.imgMute.setOnClickListener{
            mediaPlayer.setVolume(0f,0f)
            binding.imgUnmute.visibility = View.VISIBLE
            binding.imgMute.visibility = View.GONE
            muteStatus = 1
        }

        binding.imgUnmute.setOnClickListener{
            mediaPlayer.setVolume(10f,10f)
            binding.imgMute.visibility = View.VISIBLE
            binding.imgUnmute.visibility = View.GONE
            muteStatus = 2
        }

    }

    private fun callNextView() {
        binding.imgLogo.visibility = View.GONE
        binding.viewPager.visibility = View.VISIBLE
        binding.layoutbottom.visibility = View.VISIBLE
    }


    private fun setupViewPager() {

        var data: ArrayList<ViewPageData> = ArrayList()
        data.add(ViewPageData(languageData!!.klPageSubTitle1, languageData!!.klPagetitle1))
        data.add(ViewPageData(languageData!!.klPageSubtitle2, languageData!!.klPagetitle2))
        data.add(ViewPageData(languageData!!.klPageSubtitle3, languageData!!.klPagetitle3))
        val adapter = ViewPagerAdapter(data, applicationContext)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.setViewPager2(binding.viewPager)
    }

    private fun setupVideoPlayer() {

        //creating media controller
        val mediaController: MediaController = MediaController(this)
        mediaController.setAnchorView(binding.videoView)

        //specifying location of video
        val path = "android.resource://" + packageName + "/" + R.raw.amiggossplash_vedio
        binding.videoView.setMediaController(mediaController)
        binding.videoView.setVideoURI(Uri.parse(path));
        binding.videoView.requestFocus()
        binding.videoView.start()
        binding.videoView.setOnPreparedListener(this)
        binding.videoView.setMediaController(null)

    }

    override fun onPause() {
        super.onPause()
        binding.videoView.stopPlayback()
    }

    override fun onResume() {
        super.onResume()
        binding.videoView.start()

//        if(mediaPlayer != null){
//            if(muteStatus==1)
//                mediaPlayer.setVolume(10f,10f)
//            else
//                mediaPlayer.setVolume(0f,0f)
//        }

    }


    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer = mp!!
        mediaPlayer.isLooping = true
    }

    override fun validateError(message: String) {


    }


    override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }


}