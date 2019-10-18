package com.tekzee.amiggos.ui.agegroup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.AgeGroupActivityBinding
import com.tekzee.amiggos.ui.agegroup.model.AgeGroupResponse
import com.tekzee.amiggos.ui.home.HomeActivity
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib

class AgeGroupActivity:BaseActivity(), AgeGroupActivityPresenter.AgeGroupMainView {


    lateinit var binding: AgeGroupActivityBinding
    private var sharedPreference: SharedPreference ? = null
    private var ageGroupActivityPresenterImplementation: AgeGroupActivityPresenterImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.age_group_activity)
        sharedPreference = SharedPreference(this)
        ageGroupActivityPresenterImplementation = AgeGroupActivityPresenterImplementation(this,this)
        setupClickListener()
        setupDataView()
    }

    private fun setupDataView() {


        if(sharedPreference!!.getValueString(ConstantLib.USER_AGE)!!.toInt() < 18 ){
            binding.imgEighteen.setBackgroundResource(R.drawable.plus_unchecked_18)
            binding.imgTwentyOne.setBackgroundResource(R.drawable.plus_unchecked_21)
            binding.imgEighteen.isEnabled = false
            binding.imgTwentyOne.isEnabled = false
        }else if(sharedPreference!!.getValueString(ConstantLib.USER_AGE)!!.toInt() in 19..20){
            binding.imgEighteen.setBackgroundResource(R.drawable.plus_18_disabled)
            binding.imgTwentyOne.setBackgroundResource(R.drawable.plus_unchecked_21)
            binding.imgEighteen.isEnabled = true
            binding.imgTwentyOne.isEnabled = false

        }else if(sharedPreference!!.getValueString(ConstantLib.USER_AGE)!!.toInt()>21){

            binding.imgEighteen.setBackgroundResource(R.drawable.plus_18_disabled)
            binding.imgTwentyOne.setBackgroundResource(R.drawable.plus_21_disabled)
            binding.imgEighteen.isEnabled = true
            binding.imgTwentyOne.isEnabled = true
        }
    }

    private fun setupClickListener() {
        binding.imgEighteen.setOnClickListener{
            binding.imgEighteen.setBackgroundResource(R.drawable.plus_18)
            doCallAgeGroupApi("1")
        }

        binding.imgTwentyOne.setOnClickListener{
            binding.imgEighteen.setBackgroundResource(R.drawable.plus_21)
            doCallAgeGroupApi("2")
        }
    }

    private fun doCallAgeGroupApi(data: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("age_group", data)
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        ageGroupActivityPresenterImplementation!!.doCallAgeGroupApi(input,Utility.createHeaders(sharedPreference))

    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun onAgeGroupApiSuccess(responseData: AgeGroupResponse) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}