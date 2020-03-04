package com.tekzee.amiggos.ui.memories.mymemories

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
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.amiggos.ui.memories.mymemories.adapter.OurMemorieWithoutProductAdapter
import com.tekzee.amiggos.ui.memories.mymemories.model.OurMemoriesWithoutProductsResponse
import com.tekzee.amiggos.ui.storieview.StorieViewActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxiclient.constant.ConstantLib

class MyMemorieFragment:BaseFragment() ,MyMemoriePresenter.MyMemoriePresenterMainView{

    private val data = ArrayList<StoriesData>()
    private var adapter: OurMemorieWithoutProductAdapter?=null
    private var reyclerview: RecyclerView?=null
    private var languageData: LanguageData? = null
    private var sharedPreference: SharedPreference? = null
    private var myMemorieImplementation: MyMemorieImplementation? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.my_memories_fragment, container, false)
        sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        myMemorieImplementation =
            MyMemorieImplementation(this, activity!!)
        setupViews(view)
        setupRecyclerMyMemorie()
        callGetMyMemories()
        return view
    }


    override fun onStop() {
        super.onStop()
        myMemorieImplementation!!.onStop()
    }

    private fun callGetMyMemories() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        myMemorieImplementation!!.callGetMyMemories(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }


    private fun setupRecyclerMyMemorie() {
        reyclerview!!.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(activity,3)
        reyclerview!!.layoutManager = layoutManager
        adapter = OurMemorieWithoutProductAdapter(data,object :MyMemorieClickListener{
            override fun onMemorieClicked(storiesData: StoriesData) {
                if(storiesData.content.isNotEmpty()){
                    val intent = Intent(activity, StorieViewActivity::class.java)
                    intent.putExtra(ConstantLib.CONTENT, storiesData)
                    intent.putExtra(ConstantLib.PROFILE_IMAGE, storiesData.imageUrl)
                    intent.putExtra(ConstantLib.USER_ID, storiesData.userid.toString())
                    intent.putExtra(ConstantLib.USER_NAME, storiesData.name)
                    context!!.startActivity(intent)
                }
            }

        })
        reyclerview!!.adapter = adapter
    }

    private fun setupViews(view: View?) {
        reyclerview = view!!.findViewById<RecyclerView>(R.id.mymemorierecyclerview)
     }
    
    
    
    override fun onMyMemorieSuccess(
        responseData: List<StoriesData>
    ) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData)
    }

    override fun onMyMemorieFailure(message: String) {
        Toast.makeText(activity,"Testing",Toast.LENGTH_LONG).show()
    }


    override fun validateError(message: String) {
        Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }
}