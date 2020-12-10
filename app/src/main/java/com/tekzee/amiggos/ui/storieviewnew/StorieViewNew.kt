package com.tekzee.amiggos.ui.storieviewnew

import android.animation.Animator
import android.animation.ValueAnimator
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.tekzee.amiggos.ui.storieviewnew.customview.StoryPagerAdapter
import com.tekzee.amiggos.ui.storieviewnew.data.Story
import com.tekzee.amiggos.ui.storieviewnew.data.StoryUser
import com.tekzee.amiggos.ui.storieviewnew.utils.CubeOutTransformer
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheUtil
import com.google.android.exoplayer2.util.Util
import com.tekzee.amiggos.ApplicationController
import com.tekzee.amiggos.R
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import com.tekzee.amiggos.ui.storieviewnew.screen.PageChangeListener
import com.tekzee.amiggos.ui.storieviewnew.screen.PageViewOperator
import com.tekzee.amiggos.ui.storieviewnew.screen.StoryDisplayFragment
import com.tekzee.amiggos.util.SharedPreference
import kotlinx.android.synthetic.main.storie_view_new_activity.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class StorieViewNew: AppCompatActivity(),
    PageViewOperator {

    private lateinit var sharedPreference: SharedPreference
    private lateinit var memorieData: MemorieResponse.Data.Memories
    private lateinit var pagerAdapter: StoryPagerAdapter
    private var currentPage: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.storie_view_new_activity)
        sharedPreference = SharedPreference(this)
        memorieData =
            intent.getSerializableExtra(ConstantLib.MEMORIE_DATA) as MemorieResponse.Data.Memories
        setupStorieData()
        setUpPager()
        if(intent.getStringExtra(ConstantLib.FROM).equals("OURMEMORIES")){
            sharedPreference.save(ConstantLib.FROM,ConstantLib.APPROVAL)
        }else{
            sharedPreference.save(ConstantLib.FROM,"")
        }
    }

    private fun setupStorieData(): ArrayList<StoryUser> {
        val storieUser = ArrayList<StoryUser>()
        val stories = ArrayList<Story>()
        var banners: java.util.ArrayList<MemorieResponse.Data.Memories.Memory.Tagged>
        for (i in memorieData.memory.indices) {
            if(memorieData.memory[i].storyFile!=null){
                banners = memorieData.memory[i].tagged as ArrayList<MemorieResponse.Data.Memories.Memory.Tagged>
                stories.add(Story(memorieData.memory[i],memorieData.memory[i].storyFile,banners,memorieData.our_story_id,intent.getStringExtra(ConstantLib.FROM)))
            }
        }
        storieUser.add(StoryUser(memorieData.name,memorieData.profile,stories))
        return storieUser
    }





    override fun backPageView() {
        if (viewPager.currentItem > 0) {
            try {
                fakeDrag(false)
            } catch (e: Exception) {
                //NO OP
            }
        }
    }

    override fun nextPageView() {
        if (viewPager.currentItem + 1 < viewPager.adapter?.count ?: 0) {
            try {
                fakeDrag(true)
            } catch (e: Exception) {
                //NO OP
            }
        } else {
           finish()
        }
    }

    private fun setUpPager() {
        val storyUserList = setupStorieData()
        preLoadStories(storyUserList)
        pagerAdapter = StoryPagerAdapter(
            supportFragmentManager,
            storyUserList
        )
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = currentPage
        viewPager.setPageTransformer(
            true,
            CubeOutTransformer()
        )
        viewPager.addOnPageChangeListener(object : PageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }

            override fun onPageScrollCanceled() {
                currentFragment()?.resumeCurrentStory()
            }
        })
    }

    private fun preLoadStories(storyUserList: ArrayList<StoryUser>) {
        val imageList = mutableListOf<String>()
        val videoList = mutableListOf<String>()

        storyUserList.forEach { storyUser ->
            storyUser.stories.forEach { story ->
                if (story.isVideo()) {
                    videoList.add(story.url)
                } else {
                    imageList.add(story.url)
                }
            }
        }
        preLoadVideos(videoList)
        preLoadImages(imageList)
    }

    private fun preLoadVideos(videoList: MutableList<String>) {
        videoList.map { data ->
            GlobalScope.async {
                val dataUri = Uri.parse(data)
                val dataSpec = DataSpec(dataUri, 0, 500 * 1024, null)
                val dataSource: DataSource =
                    DefaultDataSourceFactory(
                        applicationContext,
                        Util.getUserAgent(applicationContext, getString(R.string.app_name))
                    ).createDataSource()

                val listener =
                    CacheUtil.ProgressListener { requestLength: Long, bytesCached: Long, _: Long ->
                        val downloadPercentage = (bytesCached * 100.0
                                / requestLength)
                        Log.d("preLoadVideos", "downloadPercentage: $downloadPercentage")
                    }

                try {
                    CacheUtil.cache(
                        dataSpec,
                        ApplicationController.simpleCache,
                        CacheUtil.DEFAULT_CACHE_KEY_FACTORY,
                        dataSource,
                        listener,
                        null
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun preLoadImages(imageList: MutableList<String>) {
        imageList.forEach { imageStory ->
            Glide.with(this).load(imageStory).placeholder(R.drawable.noimage).preload()
        }
    }

    private fun currentFragment(): StoryDisplayFragment? {
        return pagerAdapter.findFragmentByPosition(viewPager, currentPage) as StoryDisplayFragment
    }

    /**
     * Change ViewPage sliding programmatically(not using reflection).
     * https://tech.dely.jp/entry/2018/12/13/110000
     * What for?
     * setCurrentItem(int, boolean) changes too fast. And it cannot set animation duration.
     */
    private var prevDragPosition = 0

    private fun fakeDrag(forward: Boolean) {
        if (prevDragPosition == 0 && viewPager.beginFakeDrag()) {
            ValueAnimator.ofInt(0, viewPager.width).apply {
                duration = 400L
                interpolator = FastOutSlowInInterpolator()
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        removeAllUpdateListeners()
                        if (viewPager.isFakeDragging) {
                            viewPager.endFakeDrag()
                        }
                        prevDragPosition = 0
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        removeAllUpdateListeners()
                        if (viewPager.isFakeDragging) {
                            viewPager.endFakeDrag()
                        }
                        prevDragPosition = 0
                    }

                    override fun onAnimationStart(p0: Animator?) {}
                })
                addUpdateListener {
                    if (!viewPager.isFakeDragging) return@addUpdateListener
                    val dragPosition: Int = it.animatedValue as Int
                    val dragOffset: Float =
                        ((dragPosition - prevDragPosition) * if (forward) -1 else 1).toFloat()
                    prevDragPosition = dragPosition
                    viewPager.fakeDragBy(dragOffset)
                }
            }.start()
        }
    }

    companion object {
        val progressState = SparseIntArray()
    }
}
