//package com.tekzee.amiggos.ui.memories.ourmemories
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import cn.pedant.SweetAlert.SweetAlertDialog
//import com.google.gson.JsonObject
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.base.model.LanguageData
//import com.tekzee.amiggos.ui.cameranew.CameraActivity
//import com.tekzee.amiggos.ui.memories.ourmemories.adapter.FeaturedBrandAdapter
//import com.tekzee.amiggos.ui.memories.ourmemories.adapter.MemorieAdapter
//import com.tekzee.amiggos.ui.memories.ourmemories.custom.CustomErrorItem
//import com.tekzee.amiggos.ui.memories.ourmemories.custom.CustomLoadingItem
//import com.tekzee.amiggos.ui.memories.ourmemories.model.FeaturedBrandProductResponse
//import com.tekzee.amiggos.util.SharedPreference
//import com.tekzee.amiggos.util.Utility
//import com.tekzee.amiggos.base.BaseFragment
//import com.tekzee.amiggos.constant.ConstantLib
//import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
//import com.tekzee.amiggos.ui.storieviewnew.StorieViewNew
//import ru.alexbykov.nopaginate.paginate.NoPaginate
//
//
//class OurMemorieFragment : BaseFragment(), OurMemoriePresenter.OurMemoriePresenterMainView{
//
//
//    private var noPaginate: NoPaginate? = null
//    private var ourMemorieData = ArrayList<MemorieResponse.Data.Memories>()
//    private var featuredProduct = ArrayList<MemorieResponse.Data.Memories>()
//    private lateinit var adapter: MemorieAdapter
//    private lateinit var adapterFeaturedBrands: FeaturedBrandAdapter
//    private var reyclerview: RecyclerView? = null
//    private var reyclerviewFeaturedBrand: RecyclerView? = null
//    private var addMemories: ConstraintLayout? =null
//    private var languageData: LanguageData? = null
//    private var sharedPreference: SharedPreference? = null
//    private var ourMemoriePresenterImplementation: OurMemoriePresenterImplementation? = null
//
//
//
//    private var ourMemoriePageNo=0
//
//    companion object {
//
//        fun newInstance(): OurMemorieFragment {
//            return OurMemorieFragment()
//        }
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//
//        val view = inflater.inflate(R.layout.our_memories_fragment, container, false)
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        sharedPreference = SharedPreference(requireContext())
//        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
//        ourMemoriePresenterImplementation =
//            OurMemoriePresenterImplementation(this, requireContext())
//        setupViews(view)
//        setupRecyclerOurMemorie()
//        setupFeaturedBrandRecyclerView()
//        setupClickListener()
//
//
//        callGetOurMemories()
//        callGetFeaturedProductForMemory()
//    }
//
//    private fun callGetFeaturedProductForMemory() {
//        val input: JsonObject = JsonObject()
//        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
//        input.addProperty("club_id", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
//        input.addProperty("page_no", 0)
//        ourMemoriePresenterImplementation!!.doCallFeaturedProductFromMemory(
//            input,
//            Utility.createHeaders(sharedPreference)
//        )
//    }
//
//    private fun setupPagination() {
//        noPaginate = NoPaginate.with(reyclerview!!)
//            .setOnLoadMoreListener {
//                ourMemoriePageNo++
//                callGetOurMemories()
//            }
//            .setLoadingTriggerThreshold(5) //0 by default
//            .setCustomErrorItem(CustomErrorItem())
//            .setCustomLoadingItem(CustomLoadingItem())
//            .build()
//    }
//
//    private fun setupFeaturedBrandRecyclerView() {
//        reyclerviewFeaturedBrand!!.setHasFixedSize(true)
//        val layoutManager = GridLayoutManager(activity,2)
//        reyclerviewFeaturedBrand!!.layoutManager = layoutManager
//        adapterFeaturedBrands = FeaturedBrandAdapter(featuredProduct,object: FeaturedBrandsClickListener{
//             override fun OnFeaturedBrandsClicked(featuredBrandsData: FeaturedBrandProductResponse.Data.FeaturedProduct) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//        })
//        reyclerviewFeaturedBrand!!.adapter = adapterFeaturedBrands
//    }
//
//    private fun setupClickListener() {
//
//
//        addMemories!!.setOnClickListener {
//            val pDialog = SweetAlertDialog(activity)
//            pDialog.titleText = languageData!!.klAddStoryAlert
//            pDialog.setCancelable(false)
//            pDialog.setCancelButton(languageData!!.klCancel) {
//                pDialog.dismiss()
//            }
//            pDialog.setConfirmButton(languageData!!.kllblAddStoryTitle) {
//                pDialog.dismiss()
//                val intent = Intent(activity, CameraActivity::class.java)
//                intent.putExtra(ConstantLib.FROM_ACTIVITY, "HOMEACTIVITY")
//                intent.putExtra(
//                    ConstantLib.PROFILE_IMAGE, sharedPreference!!.getValueString(
//                        ConstantLib.PROFILE_IMAGE))
//                startActivity(intent)
//            }
//            pDialog.show()
//        }
//    }
//
//
//    private fun setupRecyclerOurMemorie() {
//        reyclerview!!.setHasFixedSize(true)
//        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,
//            false)
//        reyclerview!!.layoutManager = layoutManager
//        adapter = MemorieAdapter(ourMemorieData,object: OurMemorieClickListener{
//            override fun OnMemorieClicked(memorieData: MemorieResponse.Data.Memories) {
//                if(memorieData.memory.isNotEmpty()){
////                    val intent =Intent(activity, StorieViewNew::class.java)
////                    intent.putExtra(ConstantLib.FROM,"MYMEMORIES")
////                    intent.putExtra(ConstantLib.OURSTORYID, ""/*memorieData.our_story_id.toString()*/)
////                    intent.putExtra(ConstantLib.CONTENT, memorieData)
////                    intent.putExtra(ConstantLib.PROFILE_IMAGE, memorieData.profile)
////                    intent.putExtra(ConstantLib.USER_ID, memorieData.venueId.toString())
////                    intent.putExtra(ConstantLib.USER_NAME, memorieData.name)
////                    context!!.startActivity(intent)
//
//                    val intent = Intent(requireContext(),StorieViewNew::class.java)
//                    intent.putExtra(ConstantLib.MEMORIE_DATA,memorieData)
//                    intent.putExtra(ConstantLib.FROM,ConstantLib.MEMORIES)
//                    sharedPreference!!.save(ConstantLib.FROM,ConstantLib.MEMORIES)
//                    context!!.startActivity(intent)
//                }
//            }
//
//        })
//        reyclerview!!.adapter = adapter
//     }
//
//    private fun callGetOurMemories() {
//
//        val input: JsonObject = JsonObject()
//        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
//        input.addProperty("page_no", ourMemoriePageNo)
//        input.addProperty("name", "")
//        ourMemoriePresenterImplementation!!.callGetOurMemories(
//            input,
//            Utility.createHeaders(sharedPreference)
//        )
//    }
//
//
//
//
//    override fun onOurMemorieSuccess(myStoryList: List<MemorieResponse.Data.Memories>) {
//        ourMemorieData.addAll(myStoryList)
//        adapter.notifyDataSetChanged()
//        setupPagination()
//
//    }
//
//    override fun onFeaturedBrandSuccess(data: List<MemorieResponse.Data.Memories>) {
//        featuredProduct.clear()
//        adapterFeaturedBrands.notifyDataSetChanged()
//        featuredProduct.addAll(data)
//        adapterFeaturedBrands.notifyDataSetChanged()
//    }
//
//    override fun onFeaturedBrandFailure(message: String) {
//       // Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
//    }
//
//
//
//    override fun onOurMemorieFailure(message: String) {
//        noPaginate!!.showLoading(false)
//        noPaginate!!.setNoMoreItems(true)
//       // Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
//    }
//
//
//    override fun validateError(message: String) {
//       // Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
//    }
//
//    private fun setupViews(view: View?) {
//        reyclerview = requireView().findViewById<RecyclerView>(R.id.reyclerview_memorie)
//        reyclerviewFeaturedBrand = requireView().findViewById<RecyclerView>(R.id.featuredbrand_recyclerview)
//        addMemories =requireView().findViewById<ConstraintLayout>(R.id.user_image)
//    }
//
//
//    override fun onStop() {
//        super.onStop()
//        ourMemoriePresenterImplementation!!.onStop()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
////        if(noPaginate!=null)
////        noPaginate!!.unbind()
//    }
//
//
//
//}