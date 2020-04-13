package com.tekzee.amiggos.ui.turningup

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.TurningUpActivityBinding
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.amiggos.ui.turningup.adapter.TurningAdapter
import com.tekzee.amiggos.ui.turningup.interfaces.TurningUpClicked
import com.tekzee.amiggos.ui.turningup.model.TurningUpData
import com.tekzee.amiggos.ui.turningup.model.TurningUpResponse
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib

class TurningUpActivity : BaseActivity(), TurningupPresenter.TurningUpMainView {

    lateinit var binding : TurningUpActivityBinding
    private var adapter: TurningAdapter? = null
    private var data = ArrayList<TurningUpData>()
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var turningUpImplementation: TurningUpImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.turning_up_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        turningUpImplementation = TurningUpImplementation(this,this)
        setupToolBar()
        callTurningUpApi()
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        binding.turningupRecyclerview.setHasFixedSize(true)
        binding.turningupRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = TurningAdapter(data,sharedPreferences, object : TurningUpClicked {
            override fun onTuringUpClicked(position: Int, selectedData: TurningUpData) {
                val intent = Intent(applicationContext, FriendProfile::class.java)
                intent.putExtra("from","TurningupActivity")
                intent.putExtra(ConstantLib.FRIEND_ID,selectedData.userid.toString())
                startActivity(intent)
            }
        })
        binding.turningupRecyclerview.adapter = adapter
    }


    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klTurningupNavTitle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callTurningUpApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        turningUpImplementation!!.callTurningUpApi(input, Utility.createHeaders(sharedPreferences))
    }

    override fun validateError(message: String) {

    }

    override fun onTurningUpSuccess(responseData: TurningUpResponse) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData.data)
        adapter!!.notifyDataSetChanged()
    }

}