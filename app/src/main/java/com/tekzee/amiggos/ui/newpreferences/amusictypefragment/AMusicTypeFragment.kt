package com.tekzee.amiggos.ui.newpreferences.amusictypefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.MusicTypeFragmentBinding
import com.tekzee.amiggos.ui.newpreferences.amusictypefragment.adapter.MusicTypeAdapter
import com.tekzee.amiggos.ui.newpreferences.amusictypefragment.model.AMusicTypeResponse

import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib

class AMusicTypeFragment:BaseFragment(), AMusicTypePresenter.AMusicTypePresenterMainView {

    private var binding: MusicTypeFragmentBinding? =null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aMusicTypeImplementation: AMusicTypeImplementation?=null
    private var adapter: MusicTypeAdapter? =null
    private var data = ArrayList<AMusicTypeResponse.Data.MusicType>()

    companion object {

        fun newInstance(): AMusicTypeFragment {
            return AMusicTypeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.music_type_fragment, container, false)
        sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aMusicTypeImplementation = AMusicTypeImplementation(this,context!!)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        callMusicTypeApi()
    }


    private fun setupRecyclerView() {
        binding!!.musicTypeRecyclerview.setHasFixedSize(true)
        binding!!.musicTypeRecyclerview.layoutManager = GridLayoutManager(context, 3)
        adapter = MusicTypeAdapter(data)
        binding!!.musicTypeRecyclerview.adapter = adapter
    }

    private fun callMusicTypeApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        aMusicTypeImplementation!!.doCallMusicTypeApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    override fun onMusicTypeSuccess(responseData: AMusicTypeResponse?) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData!!.data.musicType)
        adapter!!.notifyDataSetChanged()
    }

    override fun onMusicTypeFailure(message: String) {
       Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    override fun validateError(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}