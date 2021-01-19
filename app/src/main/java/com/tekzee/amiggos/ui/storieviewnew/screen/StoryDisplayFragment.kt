package com.tekzee.amiggos.ui.storieviewnew.screen

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.nicolettilu.scrolldowntosearchrecyclerview.utils.hide
import com.tekzee.amiggos.ApplicationController
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.enums.FriendsAction
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import com.tekzee.amiggos.ui.storieview.StorieEvent
import com.tekzee.amiggos.ui.storieviewnew.*
import com.tekzee.amiggos.ui.storieviewnew.customview.StoriesProgressView
import com.tekzee.amiggos.ui.storieviewnew.data.Story
import com.tekzee.amiggos.ui.storieviewnew.data.StoryUser
import com.tekzee.amiggos.ui.storieviewnew.utils.OnSwipeTouchListener
import com.tekzee.amiggos.ui.storieviewnew.utils.show
import com.tekzee.amiggos.ui.viewfriends.ViewFriendsActivity
import com.tekzee.amiggos.util.*
import kotlinx.android.synthetic.main.fragment_story_display.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class StoryDisplayFragment : Fragment(),
    StoriesProgressView.StoriesListener, BannerClickListener, KodeinAware, StorieEvent {
    private var storieId: String? = null
    private var fileid: String? = null
    val prefs: SharedPreference by instance<SharedPreference>()
    override val kodein: Kodein by closestKodein()
    private val position: Int by
    lazy { arguments?.getInt(EXTRA_POSITION) ?: 0 }

    private val storyUser: StoryUser by
    lazy {
        (arguments?.getParcelable<StoryUser>(
            EXTRA_STORY_USER
        ) as StoryUser)
    }

    private val stories: ArrayList<Story> by
    lazy { storyUser.stories }

    private var simpleExoPlayer: SimpleExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private var pageViewOperator: PageViewOperator? = null
    private var counter = 0
    private var pressTime = 0L
    private var limit = 500L
    private var onResumeCalled = false
    private var onVideoPrepared = false
    private var adapter: BannerAdapter? = null
    val languageConstant: LanguageData by instance<LanguageData>()
    private lateinit var viewModel: StorieViewModel
    val factory: StorieViewModelFactory by instance<StorieViewModelFactory>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, factory).get(StorieViewModel::class.java)
        viewModel.storieEvent = this

        storyDisplayVideo.useController = false
        updateStory()
        setUpUi()
        setupAdapter()
        setupLanguge()
        img_cancel.setOnClickListener {
            requireActivity().onBackPressed()
        }

        txt_view.setOnClickListener {
            val intent = Intent(requireActivity(), ViewFriendsActivity::class.java)
            intent.putExtra(ConstantLib.STORY, storieId)
            intent.putExtra(ConstantLib.FILEID, fileid)
            startActivity(intent)
        }

    }


    private fun setupLanguge() {
        if (prefs.getValueString(ConstantLib.FROM).equals(ConstantLib.APPROVAL, true)) {
            layout_approve_decline.visibility = View.GONE
        } else {
            layout_approve_decline.visibility = View.GONE
        }
        txt_accept.text = languageConstant.approve
        txt_decline.text = languageConstant.decline
    }

    private fun setupAdapter() {
        adapter = BannerAdapter(this)
        banner_recyclerview.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        banner_recyclerview.adapter = adapter
        if(stories.size>0){
            stories[0].banners
            observeList(stories[0].banners)
        }

    }

    private fun observeList(tagged: List<MemorieResponse.Data.Memories.Memory.Tagged>) {
        if (tagged.isNotEmpty() && adapter != null) {
            banner_recyclerview.visibility = View.VISIBLE
            adapter!!.submitList(tagged)
        } else {
            banner_recyclerview.visibility = View.GONE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.pageViewOperator = context as PageViewOperator
    }

    override fun onStart() {
        super.onStart()
//        counter = restorePosition()
    }

    override fun onResume() {
        super.onResume()
        if(stories.size>0){
            onResumeCalled = true
            if (stories[counter].isVideo() && !onVideoPrepared) {
                simpleExoPlayer?.playWhenReady = false
                return
            }

            simpleExoPlayer?.seekTo(5)
            simpleExoPlayer?.playWhenReady = true
            if (counter == 0) {
                storiesProgressView?.startStories()
            } else {
                // restart animation
                counter = StorieViewNew.progressState.get(arguments?.getInt(EXTRA_POSITION) ?: 0)
                storiesProgressView?.startStories(counter)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.playWhenReady = false
        storiesProgressView?.abandon()
    }

    override fun onComplete() {
        simpleExoPlayer?.release()
        pageViewOperator?.nextPageView()
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        --counter
        savePosition(counter)
        updateStory()
    }

    override fun onNext() {
        if (stories.size <= counter + 1) {
            return
        }
        ++counter
        savePosition(counter)
        updateStory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        simpleExoPlayer?.release()
    }

    private fun updateStory() {
        if(stories.size>0){
            observeList(stories[counter].banners)
            Log.e("Url---->", stories[counter].toString())
            if (stories[counter].from.equals(ConstantLib.OURMEMORIES)) {
                fileid = stories[counter].storyData.id.toString()
                storieId = stories[counter].ourstorieid
            } else {
                fileid = ""
                storieId = stories[counter].storyData.id.toString()
            }


            if (stories[counter].storyData.user_id == prefs.getValueInt(ConstantLib.USER_ID)) {
                txt_view.visibility = View.VISIBLE
            } else {
                txt_view.visibility = View.GONE
            }
            txt_view.text = stories[counter].storyData.viewCount.toString()

            Glide.with(this).load(stories[counter].storyData.profile).circleCrop()
                .placeholder(R.drawable.user).into(storyDisplayProfilePicture)
            storyDisplayNick.text = stories[counter].storyData.name





            simpleExoPlayer?.stop()
            if (stories[counter].isVideo()) {
                storyDisplayVideo.show()
                storyDisplayImage.hide()
                storyDisplayVideoProgress.show()
                initializePlayer()
            } else {
                storyDisplayVideo.hide()
                storyDisplayVideoProgress.hide()
                storyDisplayImage.show()
                Glide.with(this).load(stories[counter].url).placeholder(R.drawable.noimage)
                    .into(storyDisplayImage)
            }

            val cal: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
                timeInMillis = 0
            }
            storyDisplayTime.text = DateFormat.format("MM-dd-yyyy HH:mm:ss", cal).toString()

            if (prefs.getValueInt(ConstantLib.USER_ID) == stories[counter].storyData.user_id && prefs.getValueString(ConstantLib.USER_TYPE) == stories[counter].storyData.story_user_type.toString()) {
                img_delete.visibility = View.VISIBLE
            } else {
                img_delete.visibility = View.GONE
            }
        }





        txt_accept.setOnClickListener {
            viewModel.callAcceptDeclineApi(stories[counter].storyData.id.toString(), 1)
        }


        txt_decline.setOnClickListener {
            viewModel.callAcceptDeclineApi(stories[counter].storyData.id.toString(), 2)
        }


        img_delete.setOnClickListener {
            viewModel.callDeleteApi(stories[counter].storyData.id.toString(), counter)
        }
    }

    private fun initializePlayer() {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext())
        } else {
            simpleExoPlayer?.release()
            simpleExoPlayer = null
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext())
        }

        mediaDataSourceFactory = CacheDataSourceFactory(
            ApplicationController.simpleCache,
            DefaultHttpDataSourceFactory(
                Util.getUserAgent(
                    context,
                    Util.getUserAgent(requireContext(), getString(R.string.app_name))
                )
            )
        )
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
            Uri.parse(stories[counter].url)
        )
        simpleExoPlayer?.prepare(mediaSource, false, false)
        if (onResumeCalled) {
            simpleExoPlayer?.playWhenReady = true
        }

        storyDisplayVideo.setShutterBackgroundColor(Color.BLACK)
        storyDisplayVideo.player = simpleExoPlayer

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException?) {
                super.onPlayerError(error)
                storyDisplayVideoProgress.hide()
                if (counter == stories.size.minus(1)) {
                    pageViewOperator?.nextPageView()
                } else {
                    storiesProgressView?.skip()
                }
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                super.onLoadingChanged(isLoading)
                if (isLoading) {
                    storyDisplayVideoProgress.show()
                    pressTime = System.currentTimeMillis()
                    pauseCurrentStory()
                } else {
                    storyDisplayVideoProgress.hide()
                    storiesProgressView?.getProgressWithIndex(counter)
                        ?.setDuration(simpleExoPlayer?.duration ?: 8000L)
                    onVideoPrepared = true
                    resumeCurrentStory()
                }
            }
        })
    }

    private fun setUpUi() {
        val touchListener = object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeTop() {
                // Toast.makeText(activity, "onSwipeTop", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeBottom() {
                // Toast.makeText(activity, "onSwipeBottom", Toast.LENGTH_LONG).show()
            }

            override fun onClick(view: View) {
                when (view) {
                    next -> {
                        if (counter == stories.size - 1) {
                            pageViewOperator?.nextPageView()
                        } else {
                            storiesProgressView?.skip()
                        }
                    }
                    previous -> {
                        if (counter == 0) {
                            pageViewOperator?.backPageView()
                        } else {
                            storiesProgressView?.reverse()
                        }
                    }
                }
            }

            override fun onLongClick() {
                hideStoryOverlay()
            }

            override fun onTouchView(view: View, event: MotionEvent): Boolean {
                super.onTouchView(view, event)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressTime = System.currentTimeMillis()
                        pauseCurrentStory()
                        return false
                    }
                    MotionEvent.ACTION_UP -> {
                        showStoryOverlay()
                        resumeCurrentStory()
                        return limit < System.currentTimeMillis() - pressTime
                    }
                }
                return false
            }
        }
        previous.setOnTouchListener(touchListener)
        next.setOnTouchListener(touchListener)

        storiesProgressView?.setStoriesCountDebug(
            stories.size, position = arguments?.getInt(EXTRA_POSITION) ?: -1
        )
        storiesProgressView?.setAllStoryDuration(4000L)
        storiesProgressView?.setStoriesListener(this)


    }

    private fun showStoryOverlay() {
        if (storyOverlay == null || storyOverlay.alpha != 0F) return

        storyOverlay.animate()
            .setDuration(100)
            .alpha(1F)
            .start()
    }

    private fun hideStoryOverlay() {
        if (storyOverlay == null || storyOverlay.alpha != 1F) return

        storyOverlay.animate()
            .setDuration(200)
            .alpha(0F)
            .start()
    }

    private fun savePosition(pos: Int) {
        StorieViewNew.progressState.put(position, pos)
    }

    private fun restorePosition(): Int {
        return StorieViewNew.progressState.get(position)
    }

    fun pauseCurrentStory() {
        simpleExoPlayer?.playWhenReady = false
        storiesProgressView?.pause()
    }

    fun resumeCurrentStory() {
        if (onResumeCalled) {
            simpleExoPlayer?.playWhenReady = true
            showStoryOverlay()
            storiesProgressView?.resume()
        }
    }

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_STORY_USER = "EXTRA_STORY_USER"
        fun newInstance(position: Int, story: StoryUser): StoryDisplayFragment {
            return StoryDisplayFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putParcelable(EXTRA_STORY_USER, story)
                }
            }
        }
    }

    override fun onItemClicked(
        position: Int,
        listItem: MemorieResponse.Data.Memories.Memory.Tagged
    ) {


        callBannercountApi(listItem,stories[counter].storyData.featured_brand_id.toString())


    }

    private fun callBannercountApi(
        listItem: MemorieResponse.Data.Memories.Memory.Tagged,
        featured_brand_id: String
    ) {
        pauseCurrentStory()
        viewModel.callBannerCountApi(stories[counter].storyData.id.toString(),listItem,featured_brand_id)
    }

    override fun onAcceptDeclineCalled() {
        requireActivity().showProgressBar()
    }

    override fun onAcceptDeclineResponse(message: String) {
        requireActivity().hideProgressBar()
        requireActivity().toast(message)
        requireActivity().onBackPressed()
    }

    override fun onDeleteResponse(message: String, counter: Int) {
        requireActivity().hideProgressBar()
//        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
//        val intent = Intent(requireContext(), AHomeScreen::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            action = FriendsAction.SHOW_MY_MEMORY.action
//        }
//        requireActivity().startActivity(intent)
//        storiesProgressView?.skip()


//        StorieViewNew.memorieData!!.memory.removeAt(counter)
//        if(StorieViewNew.memorieData!!.memory.size>0){
//            val intent = Intent(requireContext(), StorieViewNew::class.java)
//            intent.putExtra(ConstantLib.MEMORIE_DATA, StorieViewNew.memorieData)
//            intent.putExtra(ConstantLib.FROM, ConstantLib.MEMORIES)
//            intent.putExtra(ConstantLib.BACKFROM, ConstantLib.DISPLAYFRAGMENT)
//            prefs.save(ConstantLib.TYPEFROM, ConstantLib.MEMORIES)
//            intent.putExtra(ConstantLib.BACKFROM, "")
//            intent.putExtra(ConstantLib.DELETED_POSITION, counter+1)
//            startActivity(intent)
//            requireActivity().finish()
//        }else{
//            requireActivity().onBackPressed()
//        }

        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        val intent = Intent(requireContext(), AHomeScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = FriendsAction.SHOW_MY_MEMORY.action
        }
        requireActivity().startActivity(intent)
    }

    override fun onFailure(message: String) {
        resumeCurrentStory()
        requireActivity().hideProgressBar()
        requireActivity().Errortoast(message)
    }

    override fun sessionExpired(message: String) {
        requireActivity().Errortoast(message)
    }

    override fun onBannerCountSuccess(
        message: String,
        listItem: MemorieResponse.Data.Memories.Memory.Tagged
    ) {
        resumeCurrentStory()
        requireActivity().hideProgressBar()
        try {
            var websiteUrl = ""
            if (listItem.website.contains("http://")) {
                websiteUrl = listItem.website
            } else {
                websiteUrl = "http://" + listItem.website
            }
            val openUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(openUrlIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}