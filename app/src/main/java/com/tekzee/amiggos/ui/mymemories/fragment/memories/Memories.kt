//package com.tekzee.amiggos.ui.mymemories.fragment.memories
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.databinding.DataBindingUtil
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import cn.pedant.SweetAlert.SweetAlertDialog
//import com.google.gson.JsonObject
//import com.jakewharton.rxbinding2.widget.RxTextView
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.base.model.LanguageData
//import com.tekzee.amiggos.ui.cameranew.CameraActivity
//import com.tekzee.amiggos.databinding.MemoriesFragmentBinding
//import com.tekzee.amiggos.ui.home.model.StoriesData
//import com.tekzee.amiggos.ui.mymemories.fragment.memories.adapter.MemoriesAdapter
//import com.tekzee.amiggos.ui.mymemories.fragment.memories.model.MyMemoriesResponse
//import com.tekzee.amiggos.ui.onlinefriends.adapter.OnlineFriendAdapter
//import com.tekzee.amiggos.base.BaseFragment
//import com.tekzee.amiggos.util.SharedPreference
//import com.tekzee.amiggos.util.Utility
//import com.tekzee.amiggos.constant.ConstantLib
//import com.tekzee.amiggos.ui.storieviewnew.StorieViewNew
//import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//import java.util.concurrent.TimeUnit
//
//class Memories: BaseFragment(), MemoriesPresenter.MemoriesMainView, InfiniteScrollRecyclerView.RecyclerViewAdapterCallback ,OnlineFriendAdapter.HomeItemClick{
//
//
//
//    private val mLoadingData = StoriesData(loadingStatus = true)
//    lateinit var binding: MemoriesFragmentBinding
//    private var myView: View? = null
//    private var sharedPreference: SharedPreference? = null
//    private var languageData: LanguageData? = null
//    private var memoriesPresenterImplementation: MemoriesPresenterImplementation? = null
//    private var memoriesPageNo = 0
//    private var mydataList = ArrayList<StoriesData>()
//    private var adapter: MemoriesAdapter? = null
//
//    @SuppressLint("CheckResult")
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.memories_fragment,container,false)
//        myView = binding.root
//        sharedPreference = SharedPreference(requireContext())
//        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
//        memoriesPresenterImplementation = MemoriesPresenterImplementation(this,requireContext())
//        setupViewData()
//        RxTextView.textChanges(binding.edtSearch) .filter { it.length > 2 }.debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
//            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
//            memoriesPageNo = 0
//            if(it.isEmpty()){
//                callMemoriesApi(false, "")
//            }else{
//                callMemoriesApi(false, it.toString())
//            }
//        }
//        return myView
//    }
//
//    private fun setupViewData() {
//        binding.edtSearch.hint = languageData!!.klSearchTitle
//    }
//
//    override fun onResume() {
//        super.onResume()
//        memoriesPageNo = 0
//        callMemoriesApi(false, "")
//    }
//
//    private fun callMemoriesApi(requestDatFromServer: Boolean, searchvalue: String) {
//
//
//        val input: JsonObject = JsonObject()
//        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
//        input.addProperty("is_dashboard", "0")
//        input.addProperty("name", searchvalue)
//        input.addProperty("page_no", memoriesPageNo)
//        memoriesPresenterImplementation!!.docallMemoriesApi(
//            input,
//            Utility.createHeaders(sharedPreference),
//            requestDatFromServer
//        )
//
//    }
//
//    override fun validateError(message: String) {
//        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
//
//    }
//
//
//    override fun onMemoriesSuccess(responseData: MyMemoriesResponse?) {
//        memoriesPageNo++
//        mydataList = responseData!!.data as ArrayList<StoriesData>
//        setupRecyclerRealFriend()
//    }
//
//    private fun setupRecyclerRealFriend() {
//        val onlineFriendRecyclerview: RecyclerView = requireView().findViewById(R.id.real_friend_fragment)
//        val layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
//        onlineFriendRecyclerview.layoutManager = layoutManager
//        adapter = MemoriesAdapter(
//            mContext = requireActivity(),
//            mRecyclerView = onlineFriendRecyclerview,
//            mLayoutManager = layoutManager,
//            mRecyclerViewAdapterCallback = this,
//            mDataList = mydataList,
//            mItemClickCallback = this
//        )
//        onlineFriendRecyclerview.adapter = adapter
//        adapter?.setLoadingStatus(true)
//    }
//
//
//    override fun onMemoriesInfiniteSuccess(responseData: MyMemoriesResponse?) {
//        memoriesPageNo++
//        adapter?.setLoadingStatus(true)
//        mydataList.removeAt(mydataList.size - 1)
//        mydataList.addAll(responseData!!.data)
//        adapter?.notifyDataSetChanged()
//    }
//
//
//    override fun onMemoriesFailure(message: String) {
//        adapter?.setLoadingStatus(false)
//        if(mydataList.size>0)
//        mydataList.removeAt(mydataList.size - 1)
//        adapter?.notifyDataSetChanged()
//    }
//
//    override fun onLoadMoreData() {
//        mydataList.add(mLoadingData)
//        adapter?.notifyDataSetChanged()
//        callMemoriesApi(true, "")
//    }
//
//
//    override fun itemClickCallback(position: Int) {
//
////        val intent = Intent(activity, FriendProfile::class.java)
////        intent.putExtra(ConstantLib.FRIEND_ID,mydataList[position].userid.toString())
////        startActivity(intent)
//
//        if(mydataList[position].userid ==  sharedPreference!!.getValueInt(ConstantLib.USER_ID) && position == 0 ){
//
//            if(mydataList[position].content.isEmpty()){
//                val pDialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
//                pDialog.titleText = languageData!!.klAddStoryAlert
//                pDialog.setCancelable(false)
//                pDialog.setCancelButton(languageData!!.klCancel) {
//                    pDialog.dismiss()
//                }
//                pDialog.setConfirmButton(languageData!!.kllblAddStoryTitle) {
//                    pDialog.dismiss()
//                    val intent = Intent(activity, CameraActivity::class.java)
//                    intent.putExtra(ConstantLib.FROM_ACTIVITY,"MEMORIES")
//                    intent.putExtra(ConstantLib.PROFILE_IMAGE,mydataList[position].imageUrl)
//                    startActivity(intent)
//                }
//                pDialog.show()
//            }else{
//                val intent =Intent(activity, StorieViewNew::class.java)
//                intent.putExtra(ConstantLib.CONTENT, mydataList[position])
//                intent.putExtra(ConstantLib.PROFILE_IMAGE, mydataList[position].imageUrl)
//                intent.putExtra(ConstantLib.USER_ID, mydataList[position].userid.toString())
//                intent.putExtra(ConstantLib.USER_NAME, mydataList[position].name)
//                startActivity(intent)
//            }
//
//        }else{
//            val intent =Intent(activity, StorieViewNew::class.java)
//            intent.putExtra(ConstantLib.CONTENT, mydataList[position])
//            intent.putExtra(ConstantLib.PROFILE_IMAGE, mydataList[position].imageUrl)
//            intent.putExtra(ConstantLib.USER_ID, mydataList[position].userid.toString())
//            intent.putExtra(ConstantLib.USER_NAME, mydataList[position].name)
//            startActivity(intent)
//        }
//
//
//
//
//
//    }
//
//}