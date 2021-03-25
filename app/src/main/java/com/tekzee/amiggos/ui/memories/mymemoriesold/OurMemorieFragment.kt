package com.tekzee.amiggos.ui.memories.mymemoriesold

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.memories.mymemoriesold.adapter.OurMemorieWithoutProductAdapter
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.MyMemoriesFragmentBinding
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import com.tekzee.amiggos.ui.storieviewnew.StorieViewNew

class OurMemorieFragment: BaseFragment() ,OurMemorieFragmentPresenter.MyMemoriePresenterMainView{


    private var binding: MyMemoriesFragmentBinding? =null
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

        binding = DataBindingUtil.inflate(inflater, R.layout.my_memories_fragment, container, false)
//        val view = inflater.inflate(R.layout.my_memories_fragment, container, false)
       
        return binding!!.root
    }


    companion object {
        private val ourMemorieFragment: OurMemorieFragment? = null


        fun newInstance(): OurMemorieFragment {

            if(ourMemorieFragment == null){
                return OurMemorieFragment()
            }
            return ourMemorieFragment

        }
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

    }

    override fun onResume() {
        super.onResume()
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
            override fun onMemorieClicked(
                storiesData: MemorieResponse.Data.Memories,
                adapterPosition: Int
            ) {
                if(storiesData.memory.isNotEmpty()){
                    val showMemoryList: ArrayList<MemorieResponse.Data.Memories> = ArrayList()
                    for(position in data.indices){
                        if(position>=adapterPosition){
                            if(data[position].memory.size>0){
                                showMemoryList.add(data[position])
                            }

                        }
                    }

                    val intent = Intent(activity, StorieViewNew::class.java)
                    intent.putExtra(ConstantLib.MEMORIE_DATA,storiesData)
                    intent.putExtra(ConstantLib.FROM,ConstantLib.OURMEMORIES)
                    intent.putExtra(ConstantLib.COMPLETE_MEMORY,showMemoryList)
                    sharedPreference!!.save(ConstantLib.TYPEFROM,ConstantLib.OURMEMORIES)
                    intent.putExtra(ConstantLib.BACKFROM,"")
                    intent.putExtra(ConstantLib.DELETED_POSITION, 0)
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
        responseData: List<MemorieResponse.Data.Memories>,
        dataResponse: MemorieResponse
    ) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData)
        setupErrorVisibility(dataResponse.message)
    }

    override fun onMyMemorieFailure(message: String) {
        setupErrorVisibility(message)
    }


    override fun validateError(message: String) {
        Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }


    fun setupErrorVisibility(message: String) {
        if (data.size == 0) {
            binding!!.errorLayout.visibility = View.VISIBLE
            binding!!.errortext.text = message
            reyclerview!!.visibility = View.GONE
        } else {
            reyclerview!!.visibility = View.VISIBLE
            binding!!.errortext.text = ""
            binding!!.errorLayout.visibility = View.GONE
        }
    }



}