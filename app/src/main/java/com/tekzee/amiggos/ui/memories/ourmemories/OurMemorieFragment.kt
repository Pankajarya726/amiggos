package com.tekzee.amiggos.ui.memories.ourmemories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.camera.CameraPreview
import com.tekzee.amiggos.ui.home.StorieClickListener
import com.tekzee.amiggos.ui.home.adapter.PaginationScrollListener
import com.tekzee.amiggos.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggos.ui.home.model.NearestClub
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.adapter.FirstFragmentAdapter
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.model.NearByV2Response
import com.tekzee.amiggos.ui.memories.AMemoriesFragment
import com.tekzee.amiggos.ui.memories.ourmemories.adapter.MemorieAdapter
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView


class OurMemorieFragment : BaseFragment(), OurMemoriePresenter.OurMemoriePresenterMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, FirstFragmentAdapter.HomeItemClick {


    private var addMemories: ConstraintLayout? =null
    //adapters
    private var myStoriesAdapter: MemorieAdapter? = null
    private var myStoriesListData = ArrayList<StoriesData>()
    private var reyclerview: RecyclerView? =null
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 0
    private var venueListData = ArrayList<NearestClub>()

    private var languageData: LanguageData? = null
    private val mLoadingData = NearByV2Response.Data.NearestFreind(loadingStatus = true)
    private var sharedPreference: SharedPreference? = null
    private var onlineFriendPageNo = 0
    private var ourMemoriePresenterImplementation: OurMemoriePresenterImplementation? = null
    private var mydataList = ArrayList<NearByV2Response.Data.NearestFreind>()
    private var adapter: FirstFragmentAdapter? = null

    companion object {

        fun newInstance(): OurMemorieFragment {
            return OurMemorieFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.our_memories_fragment, container, false)
        sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        ourMemoriePresenterImplementation =
            OurMemoriePresenterImplementation(this, activity!!)
        setupViews(view)
        setupRecyclerMyStoriesView()
        callGetMyStories(false)
        setupClickListener()
        return view
    }

    private fun setupClickListener() {
        addMemories!!.setOnClickListener {
            val pDialog = SweetAlertDialog(activity)
            pDialog.titleText = languageData!!.klAddStoryAlert
            pDialog.setCancelable(false)
            pDialog.setCancelButton(languageData!!.klCancel) {
                pDialog.dismiss()
            }
            pDialog.setConfirmButton(languageData!!.kllblAddStoryTitle) {
                pDialog.dismiss()
                val intent = Intent(activity, CameraPreview::class.java)
                intent.putExtra(ConstantLib.FROM_ACTIVITY, "HOMEACTIVITY")
                intent.putExtra(ConstantLib.PROFILE_IMAGE, sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE))
                startActivity(intent)
            }
            pDialog.show()
        }
    }


    private fun setupRecyclerMyStoriesView() {
        reyclerview!!.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        reyclerview!!.layoutManager = linearLayoutManager
        myStoriesAdapter = MemorieAdapter(myStoriesListData, object : StorieClickListener {
            override fun onStorieClick(storiesData: StoriesData) {
                val pDialog = SweetAlertDialog(activity)
                pDialog.titleText = languageData!!.klAddStoryAlert
                pDialog.setCancelable(false)
                pDialog.setCancelButton(languageData!!.klCancel) {
                    pDialog.dismiss()
                }
                pDialog.setConfirmButton(languageData!!.kllblAddStoryTitle) {
                    pDialog.dismiss()
                    val intent = Intent(activity, CameraPreview::class.java)
                    intent.putExtra(ConstantLib.FROM_ACTIVITY, "HOMEACTIVITY")
                    intent.putExtra(ConstantLib.PROFILE_IMAGE, storiesData.imageUrl)
                    startActivity(intent)
                }
                pDialog.show()
            }
        })
        reyclerview!!.adapter = myStoriesAdapter


        reyclerview!!.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                callGetMyStories(true)
            }

            override fun getTotalPageCount(): Int {
                return AMemoriesFragment.TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })


    }

    private fun callGetMyStories(requestDatFromServer: Boolean) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("is_dashboard", "1")
        input.addProperty("page_no", currentPage)
        ourMemoriePresenterImplementation!!.doGetMyStories(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )
    }


    override fun onMyStoriesInfiniteSuccess(responseData: GetMyStoriesResponse) {
        myStoriesAdapter!!.removeLoadingFooter()
        isLoading = false
        myStoriesListData.addAll(responseData.data)
        myStoriesAdapter!!.notifyDataSetChanged()

        if (currentPage != AMemoriesFragment.TOTAL_PAGES) {
            if (responseData.data.size == 0) {
                isLastPage = true
            } else {
                myStoriesAdapter!!.addLoadingFooter()
            }
        } else {
            isLastPage = true
        }
    }

    override fun onMyStoriesSuccess(responseData: GetMyStoriesResponse) {
        myStoriesListData.clear()
        myStoriesAdapter!!.notifyDataSetChanged()
        myStoriesListData.addAll(responseData.data)
        if (currentPage <= AMemoriesFragment.TOTAL_PAGES) {
            if (venueListData.size > 10){
                myStoriesAdapter!!.addLoadingFooter()
                isLastPage = false
            }else{
                isLastPage = true
            }

        } else {
            isLastPage = true
        }
        myStoriesAdapter!!.notifyDataSetChanged()
    }

    override fun onMyStoriesFailure(message: String) {
        Utility.showLogoutPopup(activity!!, message)
    }

    override fun onOnlineFriendSuccess(responseData: List<NearByV2Response.Data.NearestFreind>) {
        onlineFriendPageNo++
        mydataList.addAll(responseData)
        adapter!!.notifyDataSetChanged()
    }

    override fun onOnlineFriendInfiniteSuccess(responseData: List<NearByV2Response.Data.NearestFreind>) {
        onlineFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData)
        adapter?.notifyDataSetChanged()
    }

    override fun onOnlineFriendFailure(responseData: String) {
        if (mydataList.size > 0) {
            adapter?.setLoadingStatus(false)
            mydataList.removeAt(mydataList.size - 1)
            adapter?.notifyDataSetChanged()
        }
    }


    override fun validateError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun setupViews(view: View?) {
        reyclerview = view!!.findViewById<RecyclerView>(R.id.our_memorie_recycler)
        addMemories =view.findViewById<ConstraintLayout>(R.id.user_image)
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
    }

    override fun itemClickCallback(position: Int) {
        val intent = Intent(activity, AProfileDetails::class.java)
        intent.putExtra(ConstantLib.FRIEND_ID, mydataList[position].userid.toString())
        intent.putExtra(ConstantLib.PROFILE_IMAGE, mydataList[position].profile)
        intent.putExtra(ConstantLib.NAME, mydataList[position].name)
        intent.putExtra(ConstantLib.ADDRESS, mydataList[position].address)
        intent.putExtra(ConstantLib.REAL_FREIND_COUNT, mydataList[position].real_freind_count)
        intent.putExtra("from", "GroupFriendActivity")
        startActivity(intent)
    }

    override fun storieClicked(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onStop() {
        super.onStop()
        ourMemoriePresenterImplementation!!.onStop()
    }
}