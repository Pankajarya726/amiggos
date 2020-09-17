package com.tekzee.amiggoss.ui.camera.postimagecaptured

import android.content.*
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.orhanobut.logger.Logger
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.PostImageCaptureBinding
import com.tekzee.amiggoss.ui.ourmemories.OurMemoriesActivity
import com.tekzee.amiggoss.ui.postmemories.PostMemories
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.constant.ConstantLib
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class PostImageCapturedActivity : BaseActivity() {

    private lateinit var binding: PostImageCaptureBinding
    private var languageData: LanguageData? = null
    private var sharedPreference: SharedPreference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.post_image_capture)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
            binding.capturedVideo.visibility = View.VISIBLE
            binding.imgCapture.visibility = View.GONE
            val bitmap = intent.getStringExtra("BitmapImage")
            playVideo(bitmap!!)

        } else {
            binding.capturedVideo.visibility = View.GONE
            binding.imgCapture.visibility = View.VISIBLE
            val bitmap = intent.getStringExtra("BitmapImage")!! as Uri
            binding.imgCapture.setImageURI(bitmap)
        }

        setupClickListener()



    }

    private fun setupClickListener() {
        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgDownload.setOnClickListener {
            if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
                val bitmap = intent.getStringExtra("BitmapImage")
                saveVideo(bitmap!!)
            } else {
                val imageUri: Uri = intent.getStringExtra("BitmapImage") as Uri
                val contentResolver = contentResolver
                saveImageToGallery(contentResolver,imageUri.path)
            }
        }

        binding.imgGo.setOnClickListener {

            if (intent.getStringExtra(ConstantLib.FROM_ACTIVITY).equals(
                    "STORIEVIEWACTIVITY",
                    true
                )
            ) {
                if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
                    val imageUri = intent.getStringExtra("BitmapImage")
                    val intentActivity = Intent(applicationContext, OurMemoriesActivity::class.java)
                    intentActivity.putExtra(ConstantLib.FILEURI, imageUri)
                    Logger.d("inside PostCaptureActivity"+intent.getStringExtra(ConstantLib.OURSTORYID));
                    intentActivity.putExtra(ConstantLib.OURSTORYID,intent.getStringExtra(ConstantLib.OURSTORYID));
                    intentActivity.putExtra(ConstantLib.FROM, "VIDEO")
                    startActivity(intentActivity)
                } else {
                    val imageUri: Uri = intent.getStringExtra("BitmapImage") as Uri
                    val intentActivity = Intent(applicationContext, OurMemoriesActivity::class.java)
                    Logger.d("inside PostCaptureActivity"+intent.getStringExtra(ConstantLib.OURSTORYID));
                    intentActivity.putExtra(ConstantLib.OURSTORYID,intent.getStringExtra(ConstantLib.OURSTORYID));
                    intentActivity.putExtra(ConstantLib.FILEURI, imageUri)
                    intentActivity.putExtra(ConstantLib.FROM, "IMAGE")
                    startActivity(intentActivity)
                }

            } else {


                if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
                    val imageUri = intent.getStringExtra("BitmapImage")
                    val intent = Intent(applicationContext, PostMemories::class.java)
                    intent.putExtra(ConstantLib.FILEURI, imageUri)
                    intent.putExtra(ConstantLib.FROM, "VIDEO")
                    startActivity(intent)
                } else {
                    val imageUri: Uri = intent.getStringExtra("BitmapImage") as Uri
                    val intent = Intent(applicationContext, PostMemories::class.java)
                    intent.putExtra(ConstantLib.FILEURI, imageUri)
                    intent.putExtra(ConstantLib.FROM, "IMAGE")
                    startActivity(intent)
                }
            }
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

    private fun playVideo(bitmap: String) {
        val video = Uri.parse(bitmap)
        binding.capturedVideo.setVideoURI(video)
        binding.capturedVideo.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp ->
            mp.isLooping = true
        })
        binding.capturedVideo.start()

    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }





    private fun galleryAddPic(context: Context, imagePath: String) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(imagePath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        context.sendBroadcast(mediaScanIntent)
    }


    fun saveImageToGallery(cr: ContentResolver, imagePath: String?) {
        val title = "Saved From Glance"
        val values = ContentValues()

        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
        values.put(MediaStore.Images.Media.DESCRIPTION, title)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        // Add the date meta data to ensure the image is added at the front of the gallery
        val millis = System.currentTimeMillis()
        values.put(MediaStore.Images.Media.DATE_ADDED, millis / 1000L)
        values.put(MediaStore.Images.Media.DATE_MODIFIED, millis / 1000L)
        values.put(MediaStore.Images.Media.DATE_TAKEN, millis)
        var url: Uri? = null
        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (imagePath != null) {
                val BUFFER_SIZE = 1024
                val fileStream = FileInputStream(imagePath)
                try {
                    val imageOut = cr.openOutputStream(url!!)
                    try {
                        val buffer = ByteArray(BUFFER_SIZE)
                        while (true) {
                            val numBytesRead = fileStream.read(buffer)
                            if (numBytesRead <= 0) {
                                break
                            }
                            imageOut!!.write(buffer, 0, numBytesRead)
                        }
                    } finally {
                        imageOut!!.close()
                    }
                } finally {
                    fileStream.close()
                }
                val id = ContentUris.parseId(url!!)
                // Wait until MINI_KIND thumbnail is generated.
                val miniThumb = MediaStore.Images.Thumbnails.getThumbnail(
                    cr,
                    id,
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    null
                )
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50f, 50f, MediaStore.Images.Thumbnails.MICRO_KIND)

                showImageSaveDialog()

            } else {
                cr.delete(url!!, null, null)
            }
        } catch (e: java.lang.Exception) {
            if (url != null) {
                cr.delete(url, null, null)
            }
        }
    }

    private fun showImageSaveDialog() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = "Image have been saved to gallery"
        pDialog.setCancelable(false)
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
        }
        pDialog.show()
    }

    private fun storeThumbnail(
        cr: ContentResolver,
        source: Bitmap,
        id: Long,
        width: Float,
        height: Float,
        kind: Int
    ): Bitmap? { // create the matrix to scale it
        val matrix = Matrix()
        val scaleX = width / source.width
        val scaleY = height / source.height
        matrix.setScale(scaleX, scaleY)
        val thumb = Bitmap.createBitmap(
            source, 0, 0,
            source.width,
            source.height, matrix,
            true
        )
        val values = ContentValues(4)
        values.put(MediaStore.Images.Thumbnails.KIND, kind)
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, id.toInt())
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.height)
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.width)
        val url =
            cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values)
        return try {
            val thumbOut = cr.openOutputStream(url!!)
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut)
            thumbOut!!.close()
            thumb
        } catch (ex: FileNotFoundException) {
            null
        } catch (ex: IOException) {
            null
        }


    }


}