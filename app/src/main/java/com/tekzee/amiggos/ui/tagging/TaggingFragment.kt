package com.tekzee.amiggosvenueapp.ui.tagging

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import androidx.work.WorkManager
import cn.pedant.SweetAlert.SweetAlertDialog
import co.lujun.androidtagview.TagView
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.otaliastudios.cameraview.BitmapCallback
import com.otaliastudios.cameraview.PictureResult
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialogExtended
import com.tekzee.amiggos.databinding.TaggingFragmentBinding
import com.tekzee.amiggos.services.UploadWorkService
import com.tekzee.amiggos.ui.postmemories.PostMemories
import com.tekzee.amiggos.ui.tagging.TaggingViewModelFactory
import com.tekzee.amiggos.util.*
import com.tekzee.amiggosvenueapp.ui.tagging.adapter.TaggingAdapter
import com.tekzee.amiggosvenueapp.ui.tagging.model.TaggingResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class TaggingFragment : AppCompatActivity(), TaggingEvent, TaggingClickListener, KodeinAware {

    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance<LanguageData>()
    val prefs: SharedPreference by instance<SharedPreference>()
    val factory: TaggingViewModelFactory by instance<TaggingViewModelFactory>()

    private var binding: TaggingFragmentBinding? = null
    private lateinit var viewModel: TaggingViewModel
    private var taglist = MutableLiveData<List<String>>()
    private var tagArraylist = ArrayList<String>()
    private var finaltaggedarray = ArrayList<TaggingResponse.Data.Search>()


    private lateinit var adapter: TaggingAdapter
    private var imageUri: Uri? = null
    private lateinit var mAppExcutor: AppExecutor
    private var mbitmap: Bitmap? = null

    companion object {
        private var picture: PictureResult? = null
        fun newInstance() = TaggingFragment()
        fun setPictureResult(result: PictureResult) {
            picture = result
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.tagging_fragment)
        mAppExcutor = AppExecutor()
        viewModel = ViewModelProvider(this, factory).get(TaggingViewModel::class.java)
        viewModel.taggingEvent = this
        setupLanguage()
        setupClickListener()
        callTaggingApi("")


        taglist.observe(this, Observer {
            binding!!.tagcontainerlayout.tags = it
        })


        val result: PictureResult? = picture
        if (result == null) {
            onBackPressed()
            return
        }


        try {
            result.toBitmap(
                BitmapCallback { bitmap: Bitmap? ->
                    binding!!.imgPicture.setImageBitmap(bitmap)
                    mbitmap = bitmap
                    getimageUri(bitmap)
                })


        } catch (e: UnsupportedOperationException) {
            binding!!.imgPicture.setImageDrawable(ColorDrawable(Color.GREEN))
        }
    }

    private fun getimageUri(bitmap: Bitmap?) {
        askPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) {
            imageUri = BitmapUtils.saveImageAndReturnUri(applicationContext, bitmap)

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
                    hideKeyboard(binding!!.tagSearch)
                    binding!!.recyclerViewTagging.visibility = View.GONE
                    binding!!.tagSearch.visibility = View.GONE
                    binding!!.go.visibility = View.VISIBLE
                    binding!!.bottomLayout.visibility = View.VISIBLE
                    binding!!.save.visibility = View.VISIBLE
                    return true
                }
                return false
            }

        })


        RxTextView.textChanges(binding!!.tagSearch).filter { it.length > 2 }
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
//                mIntent.putExtra(ConstantLib.FILEURI, imageUri)
//                mIntent.putExtra(ConstantLib.FROM, "IMAGE")
//                mIntent.putExtra(ConstantLib.TAGGED_ARRAY, getTaggedArrayJson(finaltaggedarray))
//                CreateMemorieService.enqueueWork(this, mIntent)
//                val intent = Intent(applicationContext, VenueDashboard::class.java)
//                startActivity(intent)


//                val mIntent = Intent(this, FileUploadService::class.java)
//                mIntent.putExtra(ConstantLib.FILEURI, imageUri)
//                mIntent.putExtra(ConstantLib.FROM, "IMAGE")
//                mIntent.putExtra(ConstantLib.TAGGED_ARRAY, getTaggedArrayJson(finaltaggedarray))
//                FileUploadService.enqueueWork(this, mIntent)
//                val intent = Intent(applicationContext, VenueDashboard::class.java)
//                startActivity(intent)
//                finishAffinity()

//                val data = Data.Builder().putString(ConstantLib.FILEURI, imageUri.toString())
//                    .putString(ConstantLib.FROM, "IMAGE")
//                    .putString(ConstantLib.TAGGED_ARRAY, getTaggedArrayJson(finaltaggedarray)).build()
//
//                val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
//
//
//                val oneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadWorkService::class.java).setInputData(
//                    data
//                ).setConstraints(constraints).addTag("Upload").build()
//                WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
//                val intent = Intent(applicationContext, HomeActivity::class.java)
//                startActivity(intent)
//                finishAffinity()

                val intent = Intent(applicationContext, PostMemories::class.java)
                intent.putExtra(ConstantLib.FILEURI, imageUri.toString())
                intent.putExtra(ConstantLib.TAGGED_ARRAY, getTaggedArrayJson(finaltaggedarray))
                intent.putExtra(ConstantLib.FROM, "IMAGE")
                startActivity(intent)



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
            askPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
                saveImageToDisk()
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

        binding!!.close.setOnClickListener {
            onBackPressed()
        }
    }


    private fun saveImageToDisk() {
        mAppExcutor.diskIO().execute {
            BitmapUtils.saveImage(this, mbitmap!!)
        }
        showImageSaveDialog()
    }

    private fun showImageSaveDialog() {

        val pDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        pDialog.titleText =languageConstant.imagesaved
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
        if(getCountCombination(finaltaggedarray)){
            Errortoast(languageConstant.max_venue_brand_limit);
        }else{
            tagArraylist.add(listItem.name)
            taglist.postValue(tagArraylist)
            finaltaggedarray.add(listItem)
        }
    }

    private fun getCountCombination(finaltaggedarray: java.util.ArrayList<TaggingResponse.Data.Search>): Boolean {
        var count:Int = 0
        for(items in finaltaggedarray){
            if(items.type.equals("2") || items.type.equals("3")){
                count++
            }
        }
        return count>1
    }


}
