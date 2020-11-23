package com.tekzee.amiggos.ui.cameranew

import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.iammert.library.cameravideobuttonlib.CameraVideoButton
import com.otaliastudios.cameraview.*
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.size.AspectRatio
import com.otaliastudios.cameraview.size.SizeSelectors
import com.tekzee.amiggos.R
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.NewCameraActivityBinding
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.ui.tagging.TaggingFragment
import com.tekzee.amiggos.ui.taggingvideo.TaggingVideoActivity
import java.io.File


class CameraActivity : AppCompatActivity(),
    CameraVideoButton.ActionListener {
    private lateinit var sharedPreferences: SharedPreference
    private var binding: NewCameraActivityBinding? = null
    private var mCaptureTime: Long = 0
    protected var cameraWidth = 1280
    protected var cameraHeight = 720
    protected var videoWidth = 578
    protected var videoHeight = 1152


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.new_camera_activity)
        sharedPreferences = SharedPreference(this)
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)
        setupView()
        setupClickListener()
    }


    private fun setupClickListener() {
        binding!!.imgCameraFront.setOnClickListener { v: View? ->
            binding!!.cameraFlipper.animate()
            binding!!.cameraFlipper.showPrevious()
            binding!!.camera.facing = Facing.FRONT
        }
        binding!!.imgCameraBack.setOnClickListener { v: View? ->
            binding!!.cameraFlipper.animate()
            binding!!.cameraFlipper.showNext()
            binding!!.camera.facing = Facing.BACK
        }
        binding!!.flashOn.setOnClickListener { v: View? ->
            binding!!.imgFlashControl.animate()
            binding!!.imgFlashControl.showPrevious()
            binding!!.camera.flash = Flash.ON
        }
        binding!!.flashOff.setOnClickListener { v: View? ->
            binding!!.imgFlashControl.animate()
            binding!!.imgFlashControl.showNext()
            binding!!.camera.flash = Flash.OFF
        }
        binding!!.imgBack.setOnClickListener { v: View? -> onBackPressed() }
    }



    private fun setupView() {

        binding!!.camera.setLifecycleOwner(this)
        val video_width = SizeSelectors.maxWidth(videoWidth)
        val video_height = SizeSelectors.maxHeight(videoHeight)
        val dimensions =
            SizeSelectors.and(video_width, video_height) // Matches sizes bigger than 1000x2000.
        val ratio =
            SizeSelectors.aspectRatio(AspectRatio.of(1, 1), 0f) // Matches 1:1 sizes.
        val result = SizeSelectors.or(
            SizeSelectors.and(ratio, dimensions),  // Try to match both constraints
            ratio,  // If none is found, at least try to match the aspect ratio
            SizeSelectors.biggest() // If none is found, take the biggest
        )

        val image_width = SizeSelectors.maxWidth(videoWidth)
        val image_height = SizeSelectors.maxHeight(videoHeight)
        val image_dimensions =
            SizeSelectors.and(image_width, image_height) // Matches sizes bigger than 1000x2000.
        val image_ratio =
            SizeSelectors.aspectRatio(AspectRatio.of(1, 1), 0f) // Matches 1:1 sizes.
        val image_result = SizeSelectors.or(
            SizeSelectors.and(image_ratio, image_dimensions),  // Try to match both constraints
            ratio,  // If none is found, at least try to match the aspect ratio
            SizeSelectors.biggest() // If none is found, take the biggest
        )

        binding!!.camera.setVideoSize(result)
        binding!!.camera.setPictureSize(image_result)
        binding!!.videobutton.setVideoDuration(5000)
        binding!!.videobutton.actionListener = this
        binding!!.camera.addCameraListener(Listener())
        binding!!.username.text = "@"+sharedPreferences.getValueString(ConstantLib.USER_NAME)

    }

    override fun onDurationTooShortError() {}
    override fun onEndRecord() {
        binding!!.videobutton.enableVideoRecording(false)
    }

    override fun onSingleTap() {
        capturePictureSnapshot()
    }

    override fun onStartRecord() {
        captureVideoSnapshot()
        binding!!.videobutton.enableVideoRecording(true)
    }

    private inner class Listener : CameraListener() {
        override fun onCameraOpened(options: CameraOptions) {}
        override fun onCameraError(exception: CameraException) {
            super.onCameraError(exception)
            Log.d(
                TAG,
                "Got CameraException #" + exception.reason
            )
        }

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            Log.e("Rotation----->",result.rotation.toString())
            if (binding!!.camera.isTakingVideo) {
                Log.d(
                    TAG,
                    "Captured while taking video. Size=" + result.size
                )
                return
            }


            // This can happen if picture was taken with a gesture.
            val callbackTime = System.currentTimeMillis()
            if (mCaptureTime == 0L) mCaptureTime = callbackTime - 300
            TaggingFragment.setPictureResult(result)
            mCaptureTime = 0
            val tagIntent = Intent(applicationContext, TaggingFragment::class.java)
            tagIntent.putExtra(ConstantLib.SENDER_ID,intent.getStringExtra(ConstantLib.SENDER_ID))
            tagIntent.putExtra(ConstantLib.FROM,intent.getStringExtra(ConstantLib.FROM))
            tagIntent.putExtra(ConstantLib.OURSTORYID,intent.getStringExtra(ConstantLib.OURSTORYID))
            startActivity(tagIntent)
        }

        override fun onVideoTaken(result: VideoResult) {
            TaggingVideoActivity.setVideoResult(result)
            val tagVideoIntent = Intent(applicationContext,TaggingVideoActivity::class.java)
            tagVideoIntent.putExtra(ConstantLib.SENDER_ID,intent.getStringExtra(ConstantLib.SENDER_ID))
            tagVideoIntent.putExtra(ConstantLib.FROM,intent.getStringExtra(ConstantLib.FROM))
            tagVideoIntent.putExtra(ConstantLib.OURSTORYID,intent.getStringExtra(ConstantLib.OURSTORYID))
            startActivity(tagVideoIntent)
            super.onVideoTaken(result)
        }

        override fun onVideoRecordingStart() {
            super.onVideoRecordingStart()
            Log.d(TAG, "onVideoRecordingStart!")
        }

        override fun onVideoRecordingEnd() {
            super.onVideoRecordingEnd()
            Log.d(TAG, "Video taken. Processing...")
            Log.d(TAG, "onVideoRecordingEnd!")
        }

        override fun onExposureCorrectionChanged(
            newValue: Float,
            bounds: FloatArray,
            fingers: Array<PointF>?
        ) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers)
            Log.d(TAG, "Exposure correction:$newValue")
        }

        override fun onZoomChanged(
            newValue: Float,
            bounds: FloatArray,
            fingers: Array<PointF>?
        ) {
            super.onZoomChanged(newValue, bounds, fingers)
            Log.d(TAG, "Zoom:$newValue")
        }
    }

    private fun capturePictureSnapshot() {
        if (binding!!.camera.isTakingPicture) return
        mCaptureTime = System.currentTimeMillis()
        Log.d(TAG, "Capturing picture snapshot...")
        binding!!.camera.takePictureSnapshot()
    }

    private fun captureVideoSnapshot() {
        if (binding!!.camera.isTakingVideo) {
            Log.e(TAG, "Already taking video.")
            return
        }
        Log.e(TAG, "Recording snapshot for 5 seconds...")
//        binding!!.camera.videoBitRate = 1
        binding!!.camera.takeVideoSnapshot(File(filesDir, "video.mp4"), 5000)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private val TAG = CameraActivity::class.java.simpleName
    }
}