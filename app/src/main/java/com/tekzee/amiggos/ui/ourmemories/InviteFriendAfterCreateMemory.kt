package com.tekzee.amiggos.ui.ourmemories

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.ActivityOurMemoriesBinding
import com.tekzee.amiggos.services.UploadWorkOurMemoryService
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.ourmemories.adapter.InviteFriendAdapter
import com.tekzee.amiggos.ui.ourmemories.model.InviteFriendResponse
import com.tekzee.amiggos.util.RxSearchObservable
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class InviteFriendAfterCreateMemory : BaseActivity(),
    InviteFriendPresenter.InviteFriendPresenterMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback,
    InviteFriendAdapter.InviteFriendClick {

    private lateinit var adapter: InviteFriendAdapter
    private lateinit var binding: ActivityOurMemoriesBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var selectedIds: String = ""
    private var inviteFriendImplementation: InviteFriendImplementation? = null
    private var mydataList = ArrayList<InviteFriendResponse.Data.RealFreind>()
    private val mLoadingData = InviteFriendResponse.Data.RealFreind(loadingStatus = true)
    private var onlineFriendPageNo = 0

    companion object {
        var selectUserIds: HashSet<Int> = HashSet()
        var ourMemoryId: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_our_memories)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        inviteFriendImplementation = InviteFriendImplementation(this, this)
        setupLanguage()
        setupclickListener()
        callOurMemoriesApi(false, "")
        ourMemoryId = intent.getStringExtra(ConstantLib.OURSTORYID)!!

    }


    private fun setupRecyclerView() {
        val onlineFriendRecyclerview: RecyclerView = findViewById(R.id.real_friend_fragment)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = InviteFriendAdapter(
            mContext = this,
            mRecyclerView = onlineFriendRecyclerview,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = mydataList,
            mItemClickCallback = this,
            languageData
        )
        onlineFriendRecyclerview.adapter = adapter
        adapter.setLoadingStatus(true)
    }

    private fun callOurMemoriesApi(requestDatFromServer: Boolean, searchvalue: String) {

        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("page_no", onlineFriendPageNo)
        input.addProperty("search", searchvalue)
        inviteFriendImplementation!!.doCallInviteFriendsApi(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )


    }


    @SuppressLint("CheckResult")
    private fun setupclickListener() {
        binding.imgClose.setOnClickListener {
            onBackPressed()
        }


        RxSearchObservable.fromView(binding.hiddenSearchWithRecycler.searchBarSearchView)
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter(Predicate { t ->
                t.isNotEmpty()
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<String>() { t ->
                onlineFriendPageNo = 0
                mydataList.clear()
                adapter!!.notifyDataSetChanged()
                callOurMemoriesApi(false, t.toString())
            })

        val closeButton: View? =
            binding.hiddenSearchWithRecycler.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeButton?.setOnClickListener {

            binding.hiddenSearchWithRecycler.searchBarSearchView.clearFocus()
            binding.hiddenSearchWithRecycler.searchBarSearchView.isIconified = false
            binding.hiddenSearchWithRecycler.clearSearchview()
            onlineFriendPageNo = 0
            mydataList.clear()
            adapter!!.notifyDataSetChanged()
            callOurMemoriesApi(false, "")
        }

        binding!!.btnInviteFriend.setOnClickListener {
            callUploadImageToMyMemories(toCommaSeparated()!!)
        }

    }


    private fun setupLanguage() {
        binding.txtTitle.text = languageData!!.invitefriend
        binding.txtSubtitle.text = languageData!!.invitefriendhint
        binding.btnInviteFriend.text = languageData!!.klDone
        Glide.with(applicationContext)
            .load(sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE))
            .into(binding.profileImage)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onOurMemoriesSuccess(responseData: InviteFriendResponse?) {
        onlineFriendPageNo++
        mydataList =
            responseData!!.data.realFreind as ArrayList<InviteFriendResponse.Data.RealFreind>
        setupRecyclerView()
    }



    override fun onOurMemoriesSuccessInfinite(responseData: InviteFriendResponse?) {
        onlineFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData!!.data.realFreind)
        adapter?.notifyDataSetChanged()
    }


    override fun onOurMemoriesFailure(message: String) {
        if (mydataList.size > 0) {
            adapter?.setLoadingStatus(false)
            mydataList.removeAt(mydataList.size - 1)
            adapter?.notifyDataSetChanged()
        }
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }


    private fun callUploadImageToMyMemories(commaSeperatedString: String) {
        if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
            val imageUri = intent.getStringExtra(ConstantLib.FILEURI)

            val data = Data.Builder().putString(ConstantLib.FILEURI, imageUri.toString())
                .putString(ConstantLib.FROM, "VIDEO")
                .putString(ConstantLib.SELECTEDFRIENDS, commaSeperatedString)
                .putString(ConstantLib.OURSTORYID,ourMemoryId)
                .putString(ConstantLib.TAGGED_ARRAY, intent.getStringExtra(ConstantLib.TAGGED_ARRAY)).build()

            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();


            val oneTimeWorkRequest =
                OneTimeWorkRequest.Builder(UploadWorkOurMemoryService::class.java).setInputData(
                    data
                ).setConstraints(constraints).addTag("Upload").build()
            WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
            val intent = Intent(applicationContext, AHomeScreen::class.java)
            startActivity(intent)
            selectUserIds.clear()
            finishAffinity()



        } else {
            val imageUri = intent.getStringExtra(ConstantLib.FILEURI)

            val data = Data.Builder().putString(ConstantLib.FILEURI, imageUri.toString())
                .putString(ConstantLib.FROM, "IMAGE")
                .putString(ConstantLib.SELECTEDFRIENDS, commaSeperatedString)
                .putString(ConstantLib.OURSTORYID,ourMemoryId)
                .putString(ConstantLib.TAGGED_ARRAY, intent.getStringExtra(ConstantLib.TAGGED_ARRAY)).build()

            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();


            val oneTimeWorkRequest =
                OneTimeWorkRequest.Builder(UploadWorkOurMemoryService::class.java).setInputData(
                    data
                ).setConstraints(constraints).addTag("Upload").build()
            WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
            val intent = Intent(applicationContext, AHomeScreen::class.java)
            startActivity(intent)
            selectUserIds.clear()
            finishAffinity()

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        selectUserIds.clear()
    }


    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter.notifyDataSetChanged()
        callOurMemoriesApi(true, "")
    }


    override fun itemClickCallback(
        position: Int,
        realFreind: InviteFriendResponse.Data.RealFreind,
        type: Int
    ) {
        if(type ==1){
            selectUserIds.remove(realFreind.userid)
        }else{
            selectUserIds.add(realFreind.userid)
        }
        adapter.notifyItemChanged(position)

    }


    fun toCommaSeparated(): String? {
        var result = ""
        if (selectUserIds.size > 0) {
            val sb = StringBuilder()
            for (s in selectUserIds) {
                sb.append(s).append(",")
            }
            result = sb.deleteCharAt(sb.length - 1).toString()
        }
        return result
    }
}
