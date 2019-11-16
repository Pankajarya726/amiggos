package com.tekzee.amiggos.ui.camera.postimagecaptured

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.PostImageCaptureBinding
import com.tekzee.amiggos.ui.ourmemories.OurMemoriesActivity
import com.tekzee.amiggos.ui.postmemories.PostMemories
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxiclient.constant.ConstantLib
import java.io.*
import java.text.SimpleDateFormat
import java.util.*




class PostImageCapturedActivity : BaseActivity() {

    private lateinit var binding: PostImageCaptureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.post_image_capture)

        if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
            binding.capturedVideo.visibility = View.VISIBLE
            binding.imgCapture.visibility = View.GONE
            val bitmap = intent.getStringExtra("BitmapImage")
            playVideo(bitmap)

        } else {
            binding.capturedVideo.visibility = View.GONE
            binding.imgCapture.visibility = View.VISIBLE
            val bitmap = intent.getParcelableExtra("BitmapImage") as Uri
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
                saveVideo(bitmap)
            } else {
                val imageUri: Uri = intent.getParcelableExtra("BitmapImage") as Uri
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                    this.contentResolver,
                    Uri.fromFile(File(imageUri.path))
                )
//                saveImage(bitmap)
                SaveImageNew(bitmap)

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
                    val imageUri: Uri = intent.getParcelableExtra("BitmapImage") as Uri
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
                    val imageUri: Uri = intent.getParcelableExtra("BitmapImage") as Uri
                    val intent = Intent(applicationContext, PostMemories::class.java)
                    intent.putExtra(ConstantLib.FILEURI, imageUri)
                    intent.putExtra(ConstantLib.FROM, "IMAGE")
                    startActivity(intent)
                }
            }
        }
    }



    private fun saveImage(bitmap: Bitmap) {
        val wrapper = ContextWrapper(applicationContext);
        val filePath = wrapper.getExternalFilesDir("Ammigos")
        if (!filePath!!.exists()) {
            val reponse = filePath.mkdirs()
            Logger.d("Directory Created---->" + reponse);
        }
        val timeStamp = SimpleDateFormat("ddMMyyHHmm").format(Date())
        val ImageFile = "ammigos-$timeStamp.jpg"
        val file = File(filePath, ImageFile)
        if (file.exists()) {
            Logger.d("file exists")
        } else {
            Logger.d("file not exists")
            file.createNewFile()
        }

        try {

            val outStream = FileOutputStream(file.absolutePath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
            Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_LONG).show()
        } catch (e: FileNotFoundException) {
            Log.e("", "File not found: " + e.message)
            Toast.makeText(this, "Error saving!", Toast.LENGTH_LONG).show()

        }

        MediaScannerConnection.scanFile(
            this, arrayOf(file.toString()), null
        ) { path, uri ->
            Log.i("ExternalStorage", "Scanned $path:")
            Log.i("ExternalStorage", "-> uri=$uri")
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
        val len: Int
        len = inStream.read(buf)
        while ((len) > 0) {
            outStream.write(buf, 0, len)
        }

        inStream.close()
        outStream.close()
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

//
//    @Throws(IOException::class)
//    fun saveMedia(v: View) {
//        if (!binding.capturedVideo.isShown()) {
//            Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show()
//            val sdCard = Environment.getExternalStorageDirectory()
//            var dir = File(sdCard.absolutePath + "/Opendp")
//            if (!dir.exists()) {
//                dir.mkdirs()
//            }
//
//            val timeStamp = SimpleDateFormat("ddMMyyHHmm").format(Date())
//            val ImageFile = "opendp-$timeStamp.jpg" //".png";
//            val file = File(dir, ImageFile)
//
//            try {
//                val fos = FileOutputStream(file)
////                stickerView.createBitmap().compress(Bitmap.CompressFormat.PNG, 90, fos)
////                refreshGallery(file)
//                Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show()
//            } catch (e: FileNotFoundException) {
//                Toast.makeText(this, "Error saving!", Toast.LENGTH_LONG).show()
//                Log.d("", "File not found: " + e.message)
//            }
//
//        } else {
//            if (defaultVideo != null) {
//
//
//                val timeStamp = SimpleDateFormat("ddMMyyHHmm").format(Date())
//                val VideoFile = "opendp-$timeStamp.mp4"
//
//
//                val from = File(defaultVideo)
//                val to = File(dir, VideoFile)
//
//                val `in` = FileInputStream(from)
//                val out = FileOutputStream(to)
//
//                val buf = ByteArray(1024)
//                var len: Int
//                while ((len = `in`.read(buf)) > 0) {
//                    out.write(buf, 0, len)
//                }
//                `in`.close()
//                out.close()
//                refreshGallery(to)
//                Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this, "Error saving!", Toast.LENGTH_LONG).show()
//            }
//        }
//    }



    private fun SaveImageNew(bitmap: Bitmap): String? {
        var savedImagePath: String? = null
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())

        val imageFileName = "JPEG_$timeStamp.jpg"

        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Amiggos"
        )
        var success = true
        if (!storageDir.exists()) {
            if ( ! storageDir.mkdirs() ) {
                Log.e("TAG", "Failed to create directory");
                return null
            }
            success = storageDir.mkdirs()
        }

        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath;
            try {
                val fOut = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
                Logger.d("Image Saved Successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                Logger.d(e.localizedMessage)
            }

            galleryAddPic(this, savedImagePath);

        }
        return savedImagePath
    }


    private fun galleryAddPic(context: Context, imagePath: String) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(imagePath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        context.sendBroadcast(mediaScanIntent)
    }


}