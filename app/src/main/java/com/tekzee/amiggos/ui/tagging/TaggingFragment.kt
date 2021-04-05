package com.tekzee.amiggos.ui.tagging

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import co.lujun.androidtagview.TagView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.bumptech.glide.Glide
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.otaliastudios.cameraview.BitmapCallback
import com.otaliastudios.cameraview.PictureResult
import com.rw.keyboardlistener.KeyboardUtils
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialogExtended
import com.tekzee.amiggos.databinding.TaggingFragmentBinding
import com.tekzee.amiggos.ui.ourmemories.InviteFriendAfterCreateMemory
import com.tekzee.amiggos.ui.postmemories.PostMemories
import com.tekzee.amiggos.util.*
import com.tekzee.amiggosvenueapp.ui.tagging.TaggingClickListener
import com.tekzee.amiggosvenueapp.ui.tagging.TaggingEvent
import com.tekzee.amiggosvenueapp.ui.tagging.TaggingRecyclerviewClickListener
import com.tekzee.amiggosvenueapp.ui.tagging.TaggingViewModel
import com.tekzee.amiggosvenueapp.ui.tagging.adapter.TaggingAdapter
import com.tekzee.amiggosvenueapp.ui.tagging.adapter.TaggingRecyclerAdapter
import com.tekzee.amiggosvenueapp.ui.tagging.model.TaggingResponse
import com.vipul.hp_hp.library.Layout_to_Image
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.io.File
import java.util.concurrent.TimeUnit


class TaggingFragment : AppCompatActivity(), TaggingEvent, TaggingClickListener, KodeinAware,
    TaggingRecyclerviewClickListener {



    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance<LanguageData>()
    val prefs: SharedPreference by instance<SharedPreference>()
    val factory: TaggingViewModelFactory by instance<TaggingViewModelFactory>()

    private var binding: TaggingFragmentBinding? = null
    private lateinit var viewModel: TaggingViewModel
    private var taglist = MutableLiveData<List<String>>()
    private var tagArraylist = ArrayList<String>()
    private var finaltaggedarray = ArrayList<TaggingResponse.Data.Search>()
    private lateinit var adapterTaggingRecycler:TaggingRecyclerAdapter


    private lateinit var adapter: TaggingAdapter
    private var imageUri: Uri? = null
    private lateinit var mAppExcutor: AppExecutor
    private var mbitmap: Bitmap? = null

    companion object {
        private var picture: PictureResult? = null
        var imagefile: File?=null
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
        setupTaggingReyclerViewAdapter()
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
                   /* var layout_to_image = Layout_to_Image(this, binding!!.watermark)
                    var b:Bitmap=layout_to_image.convert_layout();
                    val finalbitmap: Bitmap = addWatermark(
                        b,
                        bitmap!!,
                        "@Himanshu Verma \n Amiggos"
                    )*/
                    binding!!.imgPicture.setImageBitmap(bitmap)
                    mbitmap = bitmap
                    getimageUri(bitmap)
                })


        } catch (e: UnsupportedOperationException) {
            binding!!.imgPicture.setImageDrawable(ColorDrawable(Color.GREEN))
        }

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

//        binding!!.taggingrecyclerview.setOnTouchListener(MoveViewTouchListener(binding!!.taggingrecyclerview))

                binding!!.watermark.setOnTouchListener(MoveViewTouchListener(binding!!.watermark))
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
        binding!!.taggingrecyclerview.addItemDecoration(
            SpacingItemDecoration(
                resources.getDimensionPixelOffset(
                    R.dimen.chipmargin
                ), resources.getDimensionPixelOffset(R.dimen.chipmargin)
            )
        )
        binding!!.taggingrecyclerview.layoutManager = chipsLayoutManager
        binding!!.taggingrecyclerview.adapter = adapterTaggingRecycler
    }

    private fun getimageUri(bitmap: Bitmap?) {
        askPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) {
            imagefile = BitmapUtils.saveImageAndReturnFile(applicationContext, bitmap)
            getimageUrifrombitmap(Compressor.getDefault(this).compressToBitmap(imagefile))
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

    private fun getimageUrifrombitmap(bitmap: Bitmap?) {
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

                if(intent.getStringExtra(ConstantLib.FROM_ACTIVITY).equals(ConstantLib.OURSTORYINVITE)){
//                    val imageUri = intent.getStringExtra(ConstantLib.FILEURI)
                    val inviteFriendAfterCreateMemoryIntent = Intent(
                        applicationContext,
                        InviteFriendAfterCreateMemory::class.java
                    )
                    inviteFriendAfterCreateMemoryIntent.putExtra(
                        ConstantLib.FILEURI,
                        imageUri.toString()
                    )
                    inviteFriendAfterCreateMemoryIntent.putExtra(
                        ConstantLib.TAGGED_ARRAY, getTaggedArrayJson(
                            finaltaggedarray
                        )
                    )
                    inviteFriendAfterCreateMemoryIntent.putExtra(
                        ConstantLib.OURSTORYID, intent.getStringExtra(
                            ConstantLib.OURSTORYID
                        )
                    )
                    inviteFriendAfterCreateMemoryIntent.putExtra(ConstantLib.FROM, "IMAGE")
                    startActivity(inviteFriendAfterCreateMemoryIntent)
                }else {
                    val intentPostMemory = Intent(applicationContext, PostMemories::class.java)
                    intentPostMemory.putExtra(ConstantLib.FILEURI, imageUri.toString())
                    intentPostMemory.putExtra(
                        ConstantLib.TAGGED_ARRAY,
                        getTaggedArrayJson(finaltaggedarray)
                    )
                    intentPostMemory.putExtra(
                        ConstantLib.SENDER_ID,
                        intent.getStringExtra(ConstantLib.SENDER_ID)
                    )
                    intentPostMemory.putExtra(
                        ConstantLib.OURSTORYID,
                        intent.getStringExtra(ConstantLib.OURSTORYID)
                    )
                    intentPostMemory.putExtra(
                        ConstantLib.FROM_ACTIVITY,
                        intent.getStringExtra(ConstantLib.FROM_ACTIVITY)
                    )
                    intentPostMemory.putExtra(ConstantLib.FROM, "IMAGE")
                    startActivity(intentPostMemory)
                }
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

    private fun setvisibilityofViews() {
        hideKeyboard(binding!!.tagSearch)
        binding!!.recyclerViewTagging.visibility = View.GONE
        binding!!.tagSearch.visibility = View.GONE
        binding!!.go.visibility = View.VISIBLE
        Glide.with(this).load(R.drawable.ic_t).placeholder(R.drawable.noimage).into(binding!!.touchText)
        binding!!.bottomLayout.visibility = View.VISIBLE
        binding!!.save.visibility = View.VISIBLE
    }


    private fun saveImageToDisk() {
        mAppExcutor.diskIO().execute {
            BitmapUtils.saveImage(this, mbitmap!!)
        }
        showImageSaveDialog()
    }

    private fun showImageSaveDialog() {

        val pDialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        pDialog.titleText = languageConstant.imagesaved
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
        if (listItem.type !="3" && getCountCombination(finaltaggedarray)) {
            Errortoast(languageConstant.max_venue_brand_limit);
        } else {
            binding!!.tagSearch.setText("")
            tagArraylist.add(listItem.name)
            taglist.postValue(tagArraylist)
            finaltaggedarray.add(listItem)
            adapterTaggingRecycler.submitList(finaltaggedarray)
            adapterTaggingRecycler.notifyDataSetChanged()
        }
    }

    private fun getCountCombination(finaltaggedarray: java.util.ArrayList<TaggingResponse.Data.Search>): Boolean {
        var count: Int = 0
        for (items in finaltaggedarray) {
            if (items.type == "1" || items.type == "2") {
                count++
            }
        }
        return count > 1
    }


    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onItemCloseClicked(position: Int, listItem: TaggingResponse.Data.Search) {
        if(finaltaggedarray.size>0){
            finaltaggedarray.removeAt(position)
            adapterTaggingRecycler.submitList(finaltaggedarray)
            adapterTaggingRecycler.notifyItemRemoved(position)
            adapterTaggingRecycler.notifyItemRangeChanged(
                position,
                adapterTaggingRecycler.itemCount - position
            )
        }

    }

}
