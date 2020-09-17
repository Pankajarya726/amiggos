package com.tekzee.amiggoss.ui.storieview

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bolaware.viewstimerstory.Momentz
import com.bolaware.viewstimerstory.MomentzCallback
import com.bolaware.viewstimerstory.MomentzView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.CommonResponse
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.StorieViewBinding
import com.tekzee.amiggoss.ui.home.model.Content
import com.tekzee.amiggoss.ui.home.model.StoriesData
import com.tekzee.amiggoss.ui.viewfriends.ViewFriendsActivity
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.cameranew.CameraActivity
import com.tekzee.amiggoss.ui.profiledetails.AProfileDetails
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib
import kotlinx.android.synthetic.main.storie_view.*


class StorieViewActivity: BaseActivity(), MomentzCallback, StorieViewPresenter.StorieViewMainView {
    private var mi: Momentz? = null
    private var languageData: LanguageData? = null
    var listOfViews = ArrayList<MomentzView>()
    var urlList = ArrayList<Content>()
    private var storieViewPresenterImplementation: StorieViewPresenterImplementation? = null
    private var data: StoriesData? = null
    private var storieId: String?= null
    private lateinit var binding : StorieViewBinding
    private var sharedPreferences: SharedPreference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.storie_view)
        data = intent.getSerializableExtra(ConstantLib.CONTENT) as StoriesData
        sharedPreferences = SharedPreference(this)
        storieViewPresenterImplementation = StorieViewPresenterImplementation(this,this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupStorieView()
        setupViews()
        setupClickListeners()

        if(intent.getStringExtra(ConstantLib.FROM).equals("NOTIFICATION",true)){
            binding.join.visibility = View.VISIBLE
        }else{
            binding.join.visibility = View.GONE
        }


        img_profile.setOnClickListener{
            if(data!!.userid!=sharedPreferences!!.getValueInt(ConstantLib.USER_ID)){
                val intent = Intent(this, AProfileDetails::class.java)
                intent.putExtra(ConstantLib.FRIEND_ID, data!!.userid.toString())
                intent.putExtra(ConstantLib.PROFILE_IMAGE,data!!.imageUrl)
                startActivity(intent)

            }

        }
    }

    private fun setupClickListeners() {
        binding.imgCancel.setOnClickListener{
            finish()
        }

        binding.imgDelete.setOnClickListener{
            mi!!.pause(false)
            val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            pDialog.titleText = languageData!!.klAlertMsgDeleteMemory
            pDialog.setCancelable(false)
            pDialog.setCancelButton(languageData!!.klCancel) {
                pDialog.dismiss()
                mi!!.resume()

            }
            pDialog.setConfirmButton(languageData!!.klDeleteTitle1) {
                pDialog.dismiss()
                callDeleteStorieApi()
            }
            pDialog.show()

        }

        binding.txtView.setOnClickListener{
            val intent = Intent(applicationContext, ViewFriendsActivity::class.java)
            intent.putExtra(ConstantLib.STORY,storieId)
            startActivity(intent)
        }

        binding.join.setOnClickListener{
            acceptOurStoryInvite()
        }
    }

    private fun acceptOurStoryInvite() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("sender_id", data!!.userid)
        input.addProperty("our_story_id", data!!.our_story_id)
        input.addProperty("notification_id", intent.getStringExtra(ConstantLib.NOTIFICAION_ID))
        storieViewPresenterImplementation!!.doAcceptOurStoryInvite(input, Utility.createHeaders(sharedPreferences))
    }

    private fun callDeleteStorieApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("file_id", "0")
        input.addProperty("story_id", storieId)
        storieViewPresenterImplementation!!.doCallDeleteStorie(input, Utility.createHeaders(sharedPreferences))
    }

    private fun setupViews() {
        Glide.with(applicationContext)
            .load(intent.getStringExtra(ConstantLib.PROFILE_IMAGE)).placeholder(R.drawable.user).into(img_profile)
        binding.txtName.text = intent.getStringExtra(ConstantLib.USER_NAME)
        binding.join.text = languageData!!.klJoin
        if(sharedPreferences!!.getValueInt(ConstantLib.USER_ID).toString().equals(intent.getStringExtra(
                ConstantLib.USER_ID))){
            binding.imgDelete.visibility = View.VISIBLE
        }else{
            binding.imgDelete.visibility = View.GONE
        }
    }

    private fun setupStorieView() {

        for(item in data!!.content){
            if(item.type.equals("image",true)){
                val internetLoadedImageView = ImageView(this)
                internetLoadedImageView.scaleType = ImageView.ScaleType.FIT_XY
                listOfViews.add(MomentzView(internetLoadedImageView,5))
                urlList.add(item)
            }else{
                val internetLoadedVideo = FullScreenVideoView(this)
                internetLoadedVideo.setVideoURI(Uri.parse(item.url))
                listOfViews.add(MomentzView(internetLoadedVideo,6))
                urlList.add(item)
            }
        }

         Momentz(this, listOfViews, container, this, R.drawable.white_drawable).start()

    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun done() {
        finish()
    }

    override fun onNextCalled(view: View, momentz: Momentz, index: Int) {
        try{
            mi = momentz
            if (view is VideoView) {
                momentz.pause(true)
                playVideo(view, index, momentz)
                binding.txtView.text = urlList[index].viewCount.toString()
                storieId = urlList[index].id.toString()
            } else if ((view is ImageView) && (view.drawable == null)) {
                momentz.pause(true)

                if(intent.getStringExtra(ConstantLib.FROM).equals("OURMEMORIES",true)){
                    Log.d("url ",urlList[index].apiUrl+"/"+sharedPreferences!!.getValueInt(ConstantLib.USER_ID)+"/"+intent.getStringExtra(ConstantLib.OURSTORYID));
                    Glide.with(view)
                        .load(urlList[index].apiUrl+"/"+sharedPreferences!!.getValueInt(ConstantLib.USER_ID)+"/"+intent.getStringExtra(ConstantLib.OURSTORYID)+"/"+urlList[index].id)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                //TODO: something on exception
                                return false
                            }
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                //do something when picture already loaded
                                momentz.resume()
                                return false
                            }
                        })
                        .into(view)
                }else{
                    Glide.with(view)
                        .load(urlList[index].apiUrl+"/"+sharedPreferences!!.getValueInt(ConstantLib.USER_ID)+"/"+urlList[index].id+"/0")
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                //TODO: something on exception
                                return false
                            }
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                //do something when picture already loaded
                                momentz.resume()
                                return false
                            }
                        })
                        .into(view)
                }


                binding.txtView.text = urlList[index].viewCount.toString()
                storieId = urlList[index].id.toString()


            }
        }catch (e: Exception){
            e.printStackTrace()
        }

    }


    fun playVideo(videoView: VideoView, index: Int, momentz: Momentz) {
        var str:String?=null
        if(intent.getStringExtra(ConstantLib.FROM).equals("OURMEMORIES",true)) {
            str = urlList[index].apiUrl+"/"+sharedPreferences!!.getValueInt(ConstantLib.USER_ID)+"/"+intent.getStringExtra(ConstantLib.OURSTORYID)+"/"+urlList[index].id
        }else{
            str = urlList[index].apiUrl+"/"+sharedPreferences!!.getValueInt(ConstantLib.USER_ID)+"/"+urlList[index].id+"/0"
        }

//        val str = urlList[index].url
        val videoUrl= java.net.URLDecoder.decode(str, "UTF-8");
        val uri = Uri.parse(videoUrl)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()

        videoView.setOnInfoListener(object : MediaPlayer.OnInfoListener {
            override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // Here the video starts
                    momentz.editDurationAndResume(index, (videoView.duration) / 1000)
                    return true
                }
                return false
            }
        })
    }

    override fun onDeleteStorieSuccess(responseData: CommonResponse?) {
            finish()
    }


    override fun onAcceptStoriesSuccess(responseData: CommonResponse?) {
        val intent = Intent(applicationContext, CameraActivity::class.java)
        intent.putExtra(ConstantLib.PROFILE_IMAGE,sharedPreferences!!.getValueString(ConstantLib.PROFILE_IMAGE))
        intent.putExtra(ConstantLib.FROM_ACTIVITY,"STORIEVIEWACTIVITY")
        intent.putExtra(ConstantLib.OURSTORYID,data!!.our_story_id.toString())
        Logger.d("inside StorieviewActivity"+data!!.our_story_id.toString());

        startActivity(intent)
    }



}