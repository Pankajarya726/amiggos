package com.tekzee.amiggos.ui.memories.mymemoriesold

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.memories.mymemoriesold.adapter.OurMemorieWithoutProductAdapter
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import com.tekzee.amiggos.ui.storieviewnew.StorieViewNew

class OurMemorieFragment:BaseFragment() ,OurMemorieFragmentPresenter.MyMemoriePresenterMainView{


    private val data = ArrayList<MemorieResponse.Data.Memories>()
    private var adapter: OurMemorieWithoutProductAdapter?=null
    private var reyclerview: RecyclerView?=null
    private var languageData: LanguageData? = null
    private var sharedPreference: SharedPreference? = null
    private var ourMemorieFragmentImplementation: OurMemorieFragmentImplementation? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.my_memories_fragment, container, false)
       
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        ourMemorieFragmentImplementation =
            OurMemorieFragmentImplementation(this, requireContext())
        setupViews(view)
        setupRecyclerMyMemorie()
        setupClickListener()
        callGetMyMemories()
    }


    private fun setupClickListener() {

    }


    override fun onStop() {
        super.onStop()
        ourMemorieFragmentImplementation!!.onStop()
    }

    private fun callGetMyMemories() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        ourMemorieFragmentImplementation!!.callGetOuryMemories(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }


    private fun setupRecyclerMyMemorie() {
        reyclerview!!.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(activity,3)
        reyclerview!!.layoutManager = layoutManager
        adapter = OurMemorieWithoutProductAdapter(data,object :OurMemorieFragmentClickListener{
            override fun onMemorieClicked(storiesData: MemorieResponse.Data.Memories) {
                if(storiesData.memory.isNotEmpty()){
//                    val intent = Intent(activity, StorieViewNew::class.java)
//                    intent.putExtra(ConstantLib.FROM,"OURMEMORIES")
//                    intent.putExtra(ConstantLib.OURSTORYID,"")
//                    intent.putExtra(ConstantLib.CONTENT, storiesData)
//                    intent.putExtra(ConstantLib.PROFILE_IMAGE, storiesData.profile)
//                    intent.putExtra(ConstantLib.USER_ID, storiesData.venueId)
//                    intent.putExtra(ConstantLib.USER_NAME, storiesData.name)
//                    context!!.startActivity(intent)

                    val intent = Intent(activity, StorieViewNew::class.java)
                    intent.putExtra(ConstantLib.MEMORIE_DATA,storiesData)
                    intent.putExtra(ConstantLib.FROM,ConstantLib.MEMORIES)
                    intent.putExtra(ConstantLib.FROM,ConstantLib.OURMEMORIES)
                    sharedPreference!!.save(ConstantLib.FROM,ConstantLib.OURMEMORIES)
                    context!!.startActivity(intent)
                }
            }

        })
        reyclerview!!.adapter = adapter
    }

    private fun setupViews(view: View?) {
        reyclerview = requireView().findViewById<RecyclerView>(R.id.mymemorierecyclerview)
     }
    
    
    
    override fun onMyMemorieSuccess(
        responseData: List<MemorieResponse.Data.Memories>
    ) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData)

    }

    override fun onMyMemorieFailure(message: String) {

    }


    override fun validateError(message: String) {
        Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }



}