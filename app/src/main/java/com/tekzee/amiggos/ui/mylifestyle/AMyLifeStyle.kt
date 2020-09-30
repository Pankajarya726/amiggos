package com.tekzee.amiggos.ui.mylifestyle

import android.content.Intent
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
import com.tekzee.amiggos.databinding.MyLifestyleFragmentBinding
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.mylifestyle.adapter.MyLifestyleAdapter
import com.tekzee.amiggos.ui.mylifestyle.model.MyLifestyleResponse
import com.tekzee.amiggos.ui.mylifestylesubcategory.AMyLifestyleSubcategory
import com.tekzee.amiggos.util.Utility


class AMyLifeStyle : BaseFragment(), MyLifestylePresenter.MyLifestylePresenterMainView,
     MyLifestyleClickListener {

    private lateinit var adapter: MyLifestyleAdapter
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var binding: MyLifestyleFragmentBinding? = null
    private var presenterImplementation: MyLifestylePresenterImplementation? = null

    companion object {

        fun newInstance(): AMyLifeStyle {
            return AMyLifeStyle()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.my_lifestyle_fragment, container, false)
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenterImplementation = MyLifestylePresenterImplementation(this, requireContext())
        setupLanguage()
        setupAdapter()

    }


    override fun onResume() {
        super.onResume()
        callMyLifestyle()
    }



    private fun setupLanguage() {

        binding!!.heading.text = languageData!!.chooseyourpreference

    }


    private fun callMyLifestyle() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        presenterImplementation!!.docallMyLifestyleApi(input, Utility.createHeaders(sharedPreference))
    }



    private fun setupAdapter() {
        adapter = MyLifestyleAdapter(this, ArrayList())
        binding!!.mylifestyleRecyclerview.layoutManager = GridLayoutManager(
            requireContext(),
            2
        )
        binding!!.mylifestyleRecyclerview.adapter = adapter
    }


    override fun onMyLifeStyleSuccess(responseData: MyLifestyleResponse) {
        val newlist = ArrayList<MyLifestyleResponse.Data.Lifestyle>()
        newlist.addAll(responseData.data.lifestyle)
        adapter.submitList(newlist)
    }

    override fun onMyLifestyleFailure(message: String) {
        Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
    }

    override fun validateError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenterImplementation!!.onStop()

    }

    override fun onItemClick(item: MyLifestyleResponse.Data.Lifestyle) {
       val intent = Intent(requireActivity(),AMyLifestyleSubcategory::class.java)
        intent.putExtra(ConstantLib.CATEGORY_ID,item.id.toString())
        intent.putExtra(ConstantLib.CATEGORY_NAME,item.name)
        requireActivity().startActivity(intent)
    }


}