package com.tekzee.amiggos.ui.cameranew

import android.content.Intent
import android.gesture.Gesture
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.jackandphantom.instagramvideobutton.InstagramVideoButton
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
import com.tekzee.amiggos.util.MoveViewTouchListener
import java.io.File


class CameraActivity : AppCompatActivity()
/*InstagramVideoButton.ActionListener*/ {
    private lateinit var sharedPreferences: SharedPreference
    private var binding: NewCameraActivityBinding? = null
    private var mCaptureTime: Long = 0
    protected var videoWidth = 578
    protected var videoHeight = 1152


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.new_camera_activity)
        sharedPreferences = SharedPreference(this)
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)
        setupView()
        setupClickListener()

        NotificationManagerCompat.from(applicationContext!!).cancel(
            intent!!.getIntExtra(
                ConstantLib.EXTRA_NOTIFICATION_ID,
                0
            )
        )
//        binding!!.watermark.setOnTouchListener(MoveViewTouchListener(binding!!.watermark))
    }


    private fun setupClickListener() {


        binding!!.imgCameraFront.setOnClickListener { v: View? ->
            checkFlashAvailability()
            binding!!.cameraFlipper.animate()
            binding!!.cameraFlipper.showPrevious()
            binding!!.camera.facing = Facing.FRONT
        }
        binding!!.imgCameraBack.setOnClickListener { v: View? ->
            checkFlashAvailability()
            binding!!.cameraFlipper.animate()
            binding!!.cameraFlipper.showNext()
            binding!!.camera.facing = Facing.BACK
        }
        binding!!.flashOn.setOnClickListener { v: View? ->
            binding!!.imgFlashControl.animate()
            binding!!.imgFlashControl.showPrevious()
            binding!!.camera.flash = Flash.TORCH
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


        binding!!.videobutton.enablePhotoTaking(true)
        binding!!.videobutton.enableVideoRecording(true)
        binding!!.videobutton.setVideoDuration(5000L)
        binding!!.videobutton.setMinimumVideoDuration(0)


//        binding!!.videobutton.actionListener = this


        binding!!.videobutton.actionListener = object : InstagramVideoButton.ActionListener {
            override fun onStartRecord() {
                Log.e("MY TAG", "CALL the on start record ")
                captureVideoSnapshot()
                binding!!.videobutton.enableVideoRecording(true)

            }

            override fun onEndRecord() {
                binding!!.camera.stopVideo()
                Log.e("MY TAG", "CALL the on end record ")
            }

            override fun onSingleTap() {
                Log.e("MY TAG", "CALL the on single tap record ")
                capturePictureSnapshot()
            }

            override fun onDurationTooShortError() {
                Log.e("MY TAG", "CALL the on on duration record ")

            }

            override fun onCancelled() {
                Log.e("MY TAG", "CALL the on on cancel record ")
                binding!!.camera.stopVideo()
            }


        }




        binding!!.camera.addCameraListener(Listener())
        binding!!.username.text = "@" + sharedPreferences.getValueString(ConstantLib.USER_NAME)

    }

    fun checkFlashAvailability() {
        val isFlashSupported: List<Flash> =
            binding!!.camera.cameraOptions!!.supportedFlash.toList()
        Log.e("supportedFlash", isFlashSupported.toString())
        for (item in isFlashSupported) {
            if (item == Flash.TORCH || item == Flash.ON) {
                binding!!.flashOn.visibility = View.GONE
                binding!!.flashOff.visibility = View.GONE
                break
            } else {
                binding!!.flashOn.visibility = View.VISIBLE
            }
        }
    }

//    override fun onCancelled() {
//        Log.e("MY TAG", "CALL the on on cancel record ")
//        binding!!.camera.stopVideo()
//    }
//
//    override fun onDurationTooShortError() {
//        Log.e("MY TAG", "CALL the on on duration record ")
//    }
//    override fun onEndRecord() {
//        Log.e("MY TAG", "CALL the on end record ")
//    }
//
//    override fun onSingleTap() {
//        Log.e("MY TAG", "CALL the on single tap record ")
//        capturePictureSnapshot()
//    }
//
//    override fun onStartRecord() {
//        Log.e("MY TAG", "CALL the on start record ")
//        captureVideoSnapshot()
//        binding!!.videobutton.enableVideoRecording(true)
//    }

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
            Log.e("Rotation----->", result.rotation.toString())
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
            tagIntent.putExtra(ConstantLib.SENDER_ID, intent.getStringExtra(ConstantLib.SENDER_ID))
            tagIntent.putExtra(
                ConstantLib.FROM_ACTIVITY,
                intent.getStringExtra(ConstantLib.FROM_ACTIVITY)
            )
            tagIntent.putExtra(
                ConstantLib.OURSTORYID,
                intent.getStringExtra(ConstantLib.OURSTORYID)
            )
            startActivity(tagIntent)
        }

        override fun onVideoTaken(result: VideoResult) {
            TaggingVideoActivity.setVideoResult(result)
            val tagVideoIntent = Intent(applicationContext, TaggingVideoActivity::class.java)
            tagVideoIntent.putExtra(
                ConstantLib.SENDER_ID,
                intent.getStringExtra(ConstantLib.SENDER_ID)
            )
            tagVideoIntent.putExtra(
                ConstantLib.FROM_ACTIVITY,
                intent.getStringExtra(ConstantLib.FROM_ACTIVITY)
            )
            tagVideoIntent.putExtra(
                ConstantLib.OURSTORYID,
                intent.getStringExtra(ConstantLib.OURSTORYID)
            )
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