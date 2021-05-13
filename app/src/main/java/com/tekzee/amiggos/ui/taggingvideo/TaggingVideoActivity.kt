package com.tekzee.amiggos.ui.taggingvideo

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.MediaController
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import co.lujun.androidtagview.TagView
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.bumptech.glide.Glide
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.size.AspectRatio
import com.rw.keyboardlistener.KeyboardUtils
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialogExtended
import com.tekzee.amiggos.databinding.TaggingVideoFragmentBinding
import com.tekzee.amiggos.ui.ourmemories.InviteFriendAfterCreateMemory
import com.tekzee.amiggos.ui.postmemories.PostMemories
import com.tekzee.amiggos.util.*
import com.tekzee.amiggosvenueapp.ui.tagging.TaggingClickListener
import com.tekzee.amiggosvenueapp.ui.tagging.TaggingEvent
import com.tekzee.amiggosvenueapp.ui.tagging.TaggingRecyclerviewClickListener
import com.tekzee.amiggosvenueapp.ui.tagging.adapter.TaggingAdapter
import com.tekzee.amiggosvenueapp.ui.tagging.adapter.TaggingRecyclerAdapter
import com.tekzee.amiggosvenueapp.ui.tagging.model.TaggingResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class TaggingVideoActivity : AppCompatActivity(), TaggingEvent, TaggingClickListener, KodeinAware,
    TaggingRecyclerviewClickListener {

    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance<LanguageData>()
    val prefs: SharedPreference by instance<SharedPreference>()
    val factory: TaggingVideoViewModelFactory by instance<TaggingVideoViewModelFactory>()

    private var binding: TaggingVideoFragmentBinding? = null
    private lateinit var viewModel: TaggingVideoViewModel
    private var taglist = MutableLiveData<List<String>>()
    private var tagArraylist = ArrayList<String>()
    private var finaltaggedarray = ArrayList<TaggingResponse.Data.Search>()
    var filename: String = "";

    private lateinit var adapter: TaggingAdapter
    private var imageUri: Uri? = null
    private lateinit var mAppExcutor: AppExecutor
    private var mbitmap: Bitmap? = null
    private lateinit var adapterTaggingRecycler:TaggingRecyclerAdapter

    companion object {
        fun newInstance() = TaggingVideoActivity()
        private var videoResult: VideoResult? = null

        @JvmStatic
        fun setVideoResult(result: VideoResult?) {
            videoResult = result
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.tagging_video_fragment)
        mAppExcutor = AppExecutor()
        viewModel = ViewModelProvider(this, factory).get(TaggingVideoViewModel::class.java)
        viewModel.taggingEvent = this
        setupLanguage()
        setupClickListener()

        callTaggingApi("")
        setupTaggingReyclerViewAdapter()

        taglist.observe(this, Observer {
            binding!!.tagcontainerlayout.setTags(it)
        })
        setupVideoView()
        if(intent.getStringExtra(ConstantLib.FROM_ACTIVITY)!!.equals(ConstantLib.OURSTORYINVITE)){
            binding!!.touchText.visibility = View.GONE
        }else{
            binding!!.touchText.visibility = View.VISIBLE
        }

        KeyboardUtils.addKeyboardToggleListener(
            this
        ) { isVisible ->
            if (!isVisible) {
                setvisibilityofViews()
            }
        }
    }
    private fun setupTaggingReyclerViewAdapter() {
        adapterTaggingRecycler = TaggingRecyclerAdapter(this)
        val chipsLayoutManager =
            ChipsLayoutManager.newBuilder(this)
                .setChildGravity(Gravity.TOP)
                .setScrollingEnabled(true)
                .setGravityResolver { Gravity.START }
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT) // whether strategy is applied to last row. FALSE by default
                .withLastRow(true)
                .build()
        binding!!.taggingrecyclerview.addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.chipmargin),resources.getDimensionPixelOffset(R.dimen.chipmargin)))
        binding!!.taggingrecyclerview.layoutManager = chipsLayoutManager
        binding!!.taggingrecyclerview.adapter = adapterTaggingRecycler
    }
    private fun setupVideoView() {
        val result = videoResult
        if (result == null) {
            finish()
            return
        }

        askPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) {
            val ratio =
                AspectRatio.of(result.size)
            val controller = MediaController(this)
            controller.setAnchorView(binding!!.videoview)
            controller.setMediaPlayer(binding!!.videoview)
            binding!!.videoview.setVideoURI(Uri.fromFile(result.file))
            filename = result.file.absolutePath
        }.onDeclined { e ->
            if (e.hasDenied()) {
                AlertDialog.Builder(this)
                    .setMessage("Please provide storage permission")
                    .setPositiveButton(
                        "yes"
                    ) { dialog: DialogInterface?, which: Int -> e.askAgain() } // ask again
                    .setNegativeButton(
                        "no"
                    ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                    .show()
            }

            if (e.hasForeverDenied()) {
                e.goToSettings()
            }
        }



        binding!!.videoview.setOnPreparedListener { mp ->
            mp.isLooping = true
            val lp = binding!!.videoview.layoutParams
            val videoWidth = mp.videoWidth.toFloat()
            val videoHeight = mp.videoHeight.toFloat()
            val viewWidth = binding!!.videoview.getWidth().toFloat()
            lp.height = (viewWidth * (videoHeight / videoWidth)).toInt()
            binding!!.videoview.layoutParams = lp
            playVideo()
            if (result.isSnapshot) {
                // Log the real size for debugging reason.
                Log.e(
                    "VideoPreview",
                    "The video full size is " + videoWidth + "x" + videoHeight
                )
            }
        }
    }

    private fun setupLanguage() {
        binding!!.tagSearch.hint = languageConstant.klSearchTitle
    }

    @SuppressLint("CheckResult")
    private fun setupClickListener() {
        binding!!.touchText.setOnClickListener {
            binding!!.tagSearch.visibility = View.VISIBLE
            binding!!.tagSearch.isFocusableInTouchMode = true;
            binding!!.tagSearch.isFocusable = true;
            binding!!.tagSearch.requestFocus()
            binding!!.recyclerViewTagging.visibility = View.VISIBLE
            binding!!.go.visibility = View.GONE
            binding!!.bottomLayout.visibility = View.GONE
            binding!!.save.visibility = View.GONE
            Glide.with(this).load(R.drawable.text).placeholder(R.drawable.noimage).into(binding!!.touchText)
            showKeyboard(binding!!.tagSearch)
        }


        binding!!.tagcontainerlayout.setOnTagClickListener(object : TagView.OnTagClickListener {
            override fun onSelectedTagDrag(position: Int, text: String?) {

            }

            override fun onTagLongClick(position: Int, text: String?) {

            }

            override fun onTagClick(position: Int, text: String?) {

            }

            override fun onTagCrossClick(position: Int) {
                tagArraylist.removeAt(position)
                finaltaggedarray.removeAt(position)
                taglist.postValue(tagArraylist)
            }

        })


        binding!!.tagSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setvisibilityofViews()

                    return true
                }
                return false
            }

        })


        RxTextView.textChanges(binding!!.tagSearch).filter { it.isNotEmpty() }
            .debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it.isEmpty()) {
                    callTaggingApi("")
                } else {
                    callTaggingApi(it.toString())
                }
            }

        binding!!.go.setOnClickListener {
            askPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
//                val mIntent = Intent(this, CreateMemorieService::class.java)
//                mIntent.putExtra(ConstantLib.FILEURI, filename)
//                mIntent.putExtra(ConstantLib.FROM, "VIDEO")
//                mIntent.putExtra(ConstantLib.TAGGED_ARRAY, getTaggedArrayJson(finaltaggedarray))
//                CreateMemorieService.enqueueWork(this, mIntent)
//                val intent = Intent(applicationContext, VenueDashboard::class.java)
//                startActivity(intent)
//                finishAffinity()




//                val desFile = saveVideoFile(filename)
//                VideoCompressor.start(
//                    filename,
//                    desFile!!.path,
//                    object : CompressionListener {
//                        override fun onProgress(percent: Float) {
//                            // Update UI with progress value
//                            runOnUiThread {
//                                binding!!.saveProgressBar.visibility = View.VISIBLE
//                                binding!!.go.visibility = View.GONE
//                            }
//                        }
//
//                        override fun onStart() {
//                            runOnUiThread {
//                                binding!!.saveProgressBar.visibility = View.GONE
//                                binding!!.go.visibility = View.VISIBLE
//                            }
//                        }
//
//                        override fun onSuccess() {
//
//                            runOnUiThread {
//                                binding!!.saveProgressBar.visibility = View.GONE
//                                binding!!.go.visibility = View.VISIBLE
//                            }
//
//                            if(intent.getStringExtra(ConstantLib.FROM_ACTIVITY).equals(ConstantLib.OURSTORYINVITE)){
//                                val inviteFriendAfterCreateMemoryIntent = Intent(applicationContext, InviteFriendAfterCreateMemory::class.java)
//                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.FILEURI, desFile.path)
//                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.TAGGED_ARRAY,  getTaggedArrayJson(finaltaggedarray))
//                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.SENDER_ID, intent.getStringExtra(ConstantLib.SENDER_ID))
//                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.OURSTORYID, intent.getStringExtra(ConstantLib.OURSTORYID))
//                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.FROM, "VIDEO")
//                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.FROM_ACTIVITY, intent.getStringExtra(ConstantLib.FROM_ACTIVITY))
//                                startActivity(inviteFriendAfterCreateMemoryIntent)
//                            }else{
//                                val intentPostMemory = Intent(applicationContext, PostMemories::class.java)
//                                intentPostMemory.putExtra(ConstantLib.FILEURI, desFile.path)
//                                intentPostMemory.putExtra(ConstantLib.TAGGED_ARRAY, getTaggedArrayJson(finaltaggedarray))
//                                intentPostMemory.putExtra(ConstantLib.SENDER_ID, intent.getStringExtra(ConstantLib.SENDER_ID))
//                                intentPostMemory.putExtra(ConstantLib.OURSTORYID, intent.getStringExtra(ConstantLib.OURSTORYID))
//                                intentPostMemory.putExtra(ConstantLib.FROM_ACTIVITY, intent.getStringExtra(ConstantLib.FROM_ACTIVITY))
//                                intentPostMemory.putExtra(ConstantLib.FROM, "VIDEO")
//                                startActivity(intentPostMemory)
//                            }
//
//
//                        }
//
//                        override fun onFailure(failureMessage: String) {
//                            runOnUiThread {
//                                binding!!.saveProgressBar.visibility = View.VISIBLE
//                                binding!!.go.visibility = View.GONE
//                            }
//                        }
//
//                        override fun onCancelled() {
//                            runOnUiThread {
//                                binding!!.saveProgressBar.visibility = View.GONE
//                                binding!!.go.visibility = View.VISIBLE
//                            }
//                        }
//
//                    },
//                    VideoQuality.MEDIUM,
//                    isMinBitRateEnabled = false,
//                    keepOriginalResolution = true
//                )


                val desFile = saveVideoFile(filename)
                VideoCompressor.start(
                    filename,
                    desFile!!.path,
                    object : CompressionListener {
                        override fun onProgress(percent: Float) {
                            // Update UI with progress value
                            runOnUiThread {
                                binding!!.saveProgressBar.visibility = View.VISIBLE
                                binding!!.go.visibility = View.GONE
                            }
                        }

                        override fun onStart() {
                            runOnUiThread {
                                binding!!.saveProgressBar.visibility = View.GONE
                                binding!!.go.visibility = View.VISIBLE
                            }
                        }

                        override fun onSuccess() {

                            runOnUiThread {
                                binding!!.saveProgressBar.visibility = View.GONE
                                binding!!.go.visibility = View.VISIBLE
                            }

                            if(intent.getStringExtra(ConstantLib.FROM_ACTIVITY).equals(ConstantLib.OURSTORYINVITE)){
                                val inviteFriendAfterCreateMemoryIntent = Intent(applicationContext, InviteFriendAfterCreateMemory::class.java)
                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.FILEURI, desFile.path)
                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.TAGGED_ARRAY,  getTaggedArrayJson(finaltaggedarray))
                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.SENDER_ID, intent.getStringExtra(ConstantLib.SENDER_ID))
                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.OURSTORYID, intent.getStringExtra(ConstantLib.OURSTORYID))
                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.FROM, "VIDEO")
                                inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.FROM_ACTIVITY, intent.getStringExtra(ConstantLib.FROM_ACTIVITY))
                                startActivity(inviteFriendAfterCreateMemoryIntent)
                            }else{
                                val intentPostMemory = Intent(applicationContext, PostMemories::class.java)
                                intentPostMemory.putExtra(ConstantLib.FILEURI, desFile.path)
                                intentPostMemory.putExtra(ConstantLib.TAGGED_ARRAY, getTaggedArrayJson(finaltaggedarray))
                                intentPostMemory.putExtra(ConstantLib.SENDER_ID, intent.getStringExtra(ConstantLib.SENDER_ID))
                                intentPostMemory.putExtra(ConstantLib.OURSTORYID, intent.getStringExtra(ConstantLib.OURSTORYID))
                                intentPostMemory.putExtra(ConstantLib.FROM_ACTIVITY, intent.getStringExtra(ConstantLib.FROM_ACTIVITY))
                                intentPostMemory.putExtra(ConstantLib.FROM, "VIDEO")
                                startActivity(intentPostMemory)
                            }



                        }

                        override fun onFailure(failureMessage: String) {
                            runOnUiThread {
                                binding!!.saveProgressBar.visibility = View.VISIBLE
                                binding!!.go.visibility = View.GONE
                            }
                        }

                        override fun onCancelled() {
                            runOnUiThread {
                                binding!!.saveProgressBar.visibility = View.GONE
                                binding!!.go.visibility = View.VISIBLE
                            }
                        }

                    },
                    VideoQuality.MEDIUM,
                    isMinBitRateEnabled = false,
                    keepOriginalResolution = true
                )


            }.onDeclined { e ->
                if (e.hasDenied()) {

                    val dialog: BottomDialogExtended =
                        BottomDialogExtended.newInstance(
                            languageConstant.storagepermission,
                            arrayOf(languageConstant.yes)
                        )
                    dialog.show(supportFragmentManager, "dialog")
                    dialog.setListener { position ->
                        dialog.dismiss()
                        e.askAgain()
                    }
                }

                if (e.hasForeverDenied()) {
                    e.goToSettings()
                }
            }

        }

        binding!!.save.setOnClickListener {
            saveVideo(filename)

        }


        binding!!.close.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setvisibilityofViews() {
        hideKeyboard(binding!!.tagSearch)
        binding!!.recyclerViewTagging.visibility = View.GONE
        binding!!.tagSearch.visibility = View.GONE
        binding!!.go.visibility = View.VISIBLE
        binding!!.bottomLayout.visibility = View.VISIBLE
        binding!!.save.visibility = View.VISIBLE
        Glide.with(this).load(R.drawable.ic_t).placeholder(R.drawable.noimage).into(binding!!.touchText)
    }


    private fun saveVideoFile(filePath: String?): File? {
        filePath?.let {
            val videoFile = File(filePath)
            val videoFileName = "${System.currentTimeMillis()}_${videoFile.name}"
            val folderName = Environment.DIRECTORY_MOVIES+"/AmiggosMovies"
            if (Build.VERSION.SDK_INT >= 29) {

                val values = ContentValues().apply {

                    put(
                        MediaStore.Images.Media.DISPLAY_NAME,
                        videoFileName
                    )
                    put(MediaStore.Images.Media.MIME_TYPE, "video/mp4")
                    put(MediaStore.Images.Media.RELATIVE_PATH, folderName)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val collection =
                    MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

                val fileUri = applicationContext.contentResolver.insert(collection, values)

                fileUri?.let {
                    application.contentResolver.openFileDescriptor(fileUri, "w").use { descriptor ->
                        descriptor?.let {
                            FileOutputStream(descriptor.fileDescriptor).use { out ->
                                FileInputStream(videoFile).use { inputStream ->
                                    val buf = ByteArray(4096)
                                    while (true) {
                                        val sz = inputStream.read(buf)
                                        if (sz <= 0) break
                                        out.write(buf, 0, sz)
                                    }
                                }
                            }
                        }
                    }

                    values.clear()
                    values.put(MediaStore.Video.Media.IS_PENDING, 0)
                    applicationContext.contentResolver.update(fileUri, values, null, null)

                    return File(getMediaPath(applicationContext, fileUri))
                }
            } else {
                val downloadsPath =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val desFile = File(downloadsPath, videoFileName)

                if (desFile.exists())
                    desFile.delete()

                try {
                    desFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return desFile
            }
        }
        return null
    }


    fun getMediaPath(context: Context, uri: Uri): String {

        val resolver = context.contentResolver
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        var cursor: Cursor? = null
        try {
            cursor = resolver.query(uri, projection, null, null, null)
            return if (cursor != null) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(columnIndex)

            } else ""

        } catch (e: Exception) {
            resolver.let {
                val filePath = (context.applicationInfo.dataDir + File.separator
                        + System.currentTimeMillis())
                val file = File(filePath)

                resolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        val buf = ByteArray(4096)
                        var len: Int
                        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(
                            buf,
                            0,
                            len
                        )
                    }
                }
                return file.absolutePath
            }
        } finally {
            cursor?.close()
        }
    }


    private fun saveVideo(defaultVideo: String) {
        askPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) {
            val timeStamp = SimpleDateFormat("ddMMyyHHmm").format(Date())
            val VideoFile = "ammigos-$timeStamp.mp4"
            val sdCard =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                    .toString()
            val dir = File(sdCard + "/ammigos/")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val from = File(defaultVideo)
            val to = File(dir, VideoFile)

            val inStream = FileInputStream(from)
            val outStream = FileOutputStream(to)
            val buf = ByteArray(1024)

            var len: Int = 0;

            while (inStream.read(buf).also { len = it } > 0) {
                outStream.write(buf, 0, len)

            }

            inStream.close()
            outStream.close()
            showVideoSaveDialog()
        }.onDeclined { e ->
            if (e.hasDenied()) {

                val dialog: BottomDialogExtended =
                    BottomDialogExtended.newInstance(
                        languageConstant.storagepermission,
                        arrayOf(languageConstant.yes)
                    )
                dialog.show(supportFragmentManager, "dialog")
                dialog.setListener { position ->
                    dialog.dismiss()
                    e.askAgain()
                }

            }

            if (e.hasForeverDenied()) {
                e.goToSettings()
            }
        }


    }

    private fun showVideoSaveDialog() {

        val pDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        pDialog.titleText =languageConstant.videosaved
        pDialog.setCancelable(false)
        pDialog.setConfirmButton(languageConstant.klOk) {
            pDialog.dismiss()
        }
        pDialog.show()
    }

    private fun getTaggedArrayJson(finaltaggedarray: ArrayList<TaggingResponse.Data.Search>): String? {
        val jsonArray = JsonArray()
        for (items in finaltaggedarray) {
            val json = JsonObject()
            json.addProperty("id", items.id)
            json.addProperty("name", items.name)
            json.addProperty("type", items.type)
            jsonArray.add(json)
        }
        return jsonArray.toString()
    }


    private fun callTaggingApi(searchtext: String) {
        Coroutines.main {
            viewModel.callTaggingApi(searchtext)
        }
    }

    override fun onStarted() {
        binding!!.progressCircular.visibility = View.VISIBLE
    }

    override fun onLoaded() {
        setupAdapter()
        observeList()
    }

    private fun observeList() {
        viewModel.taggingList.observe(this, Observer { listitem ->
            val newlist = ArrayList<TaggingResponse.Data.Search>()
            newlist.addAll(listitem)
            adapter.submitList(newlist)
            binding!!.progressCircular.visibility = View.GONE
        })
    }

    private fun setupAdapter() {
        adapter = TaggingAdapter(this)
        binding!!.recyclerViewTagging.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding!!.recyclerViewTagging.adapter = adapter
    }

    override fun onFailure(message: String) {
        binding!!.progressCircular.visibility = View.GONE
    }

    override fun sessionExpired(message: String) {
        Errortoast(message)
//        Utility.logoutUser(prefs, this)
    }

    override fun onItemClicked(
        position: Int,
        listItem: TaggingResponse.Data.Search
    ) {

        if(listItem.type !="3" && getCountCombination(finaltaggedarray)){
            Errortoast(languageConstant.max_venue_brand_limit);
        }else{
            binding!!.tagSearch.setText("")
            tagArraylist.add(listItem.name)
            taglist.postValue(tagArraylist)
            finaltaggedarray.add(listItem)
            adapterTaggingRecycler.submitList(finaltaggedarray)
            adapterTaggingRecycler.notifyDataSetChanged()
        }

    }


    fun playVideo() {
        if (!binding!!.videoview.isPlaying) {
            binding!!.videoview.start()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            setVideoResult(null)
        }
    }


    private fun getCountCombination(finaltaggedarray: java.util.ArrayList<TaggingResponse.Data.Search>): Boolean {
        var count:Int = 0
        for(items in finaltaggedarray){
            if(items.type == "1" || items.type == "2"){
                count++
            }
        }
        return count>1
    }

    override fun onItemCloseClicked(position: Int, listItem: TaggingResponse.Data.Search) {
        if(finaltaggedarray.size>0){
            finaltaggedarray.removeAt(position)
            adapterTaggingRecycler.submitList(finaltaggedarray)
            adapterTaggingRecycler.notifyItemRemoved(position)
            adapterTaggingRecycler.notifyItemRangeChanged(position,adapterTaggingRecycler.itemCount -position)
        }
    }

}
