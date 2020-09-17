package com.tekzee.amiggoss.cameranew

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.orhanobut.logger.Logger
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.size.AspectRatio
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.ActivityVideoPreviewBinding
import com.tekzee.amiggoss.ui.ourmemories.OurMemoriesActivity
import com.tekzee.amiggoss.ui.postmemories.PostMemories
import com.tekzee.amiggoss.util.AppExecutor
import com.tekzee.amiggoss.util.BitmapUtils
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.constant.ConstantLib
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class VideoPreviewActivity : AppCompatActivity() {
    private var binding: ActivityVideoPreviewBinding? = null
    private var videoView: VideoView? = null
    private var languageData: LanguageData? = null
    private lateinit var mAppExcutor: AppExecutor
    private var sharedPreference: SharedPreference? = null
    var filename:String ="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_video_preview)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        mAppExcutor = AppExecutor()
        setupClickListener()

        val result = videoResult
        if (result == null) {
            finish()
            return
        }
        videoView = findViewById(R.id.video)
        videoView!!.setOnClickListener(View.OnClickListener { playVideo() })
        val ratio =
            AspectRatio.of(result.size)
        val controller = MediaController(this)
        controller.setAnchorView(videoView)
        controller.setMediaPlayer(videoView)
             videoView!!.setVideoURI(Uri.fromFile(result.file))
        filename = result.file.absolutePath

        videoView!!.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp ->
            mp.isLooping = true
            val lp = videoView!!.getLayoutParams()
            val videoWidth = mp.videoWidth.toFloat()
            val videoHeight = mp.videoHeight.toFloat()
            val viewWidth = videoView!!.getWidth().toFloat()
            lp.height = (viewWidth * (videoHeight / videoWidth)).toInt()
            videoView!!.layoutParams = lp
            playVideo()
            if (result.isSnapshot) {
                // Log the real size for debugging reason.
                Log.e(
                    "VideoPreview",
                    "The video full size is " + videoWidth + "x" + videoHeight
                )
            }
        })
    }


    private fun saveVideoToDisk() {

        mAppExcutor.diskIO().execute {
            BitmapUtils.saveVideo(this, filename)
        }
        showVideoSaveDialog()
    }


    private fun setupClickListener() {
        binding!!.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding!!.imgDownload.setOnClickListener {

            saveVideo(filename)
        }

        binding!!.imgGo.setOnClickListener {

            if (intent.getStringExtra(ConstantLib.FROM_ACTIVITY).equals(
                    "STORIEVIEWACTIVITY",
                    true
                )
            ) {
                val imageUri = filename
                val intentActivity = Intent(applicationContext, OurMemoriesActivity::class.java)
                intentActivity.putExtra(ConstantLib.FILEURI, imageUri)
                Logger.d("inside PostCaptureActivity"+intent.getStringExtra(ConstantLib.OURSTORYID));
                intentActivity.putExtra(ConstantLib.OURSTORYID,intent.getStringExtra(ConstantLib.OURSTORYID));
                intentActivity.putExtra(ConstantLib.FROM, "VIDEO")
                startActivity(intentActivity)

            } else {
                val imageUri = filename
                val intent = Intent(applicationContext, PostMemories::class.java)
                intent.putExtra(ConstantLib.FILEURI, imageUri)
                intent.putExtra(ConstantLib.FROM, "VIDEO")
                startActivity(intent)
            }
        }
    }

    fun playVideo() {
        if (!videoView!!.isPlaying) {
            videoView!!.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            setVideoResult(null)
        }
    }

    companion object {
        private var videoResult: VideoResult? = null
        @JvmStatic
        fun setVideoResult(result: VideoResult?) {
            videoResult = result
        }
    }


    private fun saveVideo(defaultVideo: String) {
        val timeStamp = SimpleDateFormat("ddMMyyHHmm").format(Date())
        val VideoFile = "ammigos-$timeStamp.mp4"
        val sdCard =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val dir = File(sdCard + "/ammigos/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val from = File(defaultVideo)
        val to = File(dir, VideoFile)

        val inStream = FileInputStream(from)
        val outStream = FileOutputStream(to)
        val buf = ByteArray(1024)

        var len: Int=0;
//        len = inStream.read(buf)
//        while ((len) > 0) {
//            outStream.write(buf, 0, len)
//            Log.d("writing video","----------"+len)
//        }

        while (inStream.read(buf).also({ len = it }) > 0) {
            outStream.write(buf, 0, len)
            Log.d("writing video","----------"+len)
        }

        inStream.close()
        outStream.close()
        showVideoSaveDialog()
    }

    private fun showVideoSaveDialog() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = "video have been saved to gallery"
        pDialog.setCancelable(false)
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
        }
        pDialog.show()
    }

}