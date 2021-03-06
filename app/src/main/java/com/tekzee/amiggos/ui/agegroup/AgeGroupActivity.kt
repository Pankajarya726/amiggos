//package com.tekzee.amiggos.ui.agegroup
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.databinding.DataBindingUtil
//import com.google.gson.JsonObject
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.databinding.AgeGroupActivityBinding
//import com.tekzee.amiggos.ui.agegroup.model.AgeGroupResponse
//import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
//import com.tekzee.amiggos.base.BaseActivity
//import com.tekzee.amiggos.util.SharedPreference
//import com.tekzee.amiggos.util.Utility
//import com.tekzee.amiggos.constant.ConstantLib
//
//class AgeGroupActivity : BaseActivity(), AgeGroupActivityPresenter.AgeGroupMainView {
//
//    lateinit var binding: AgeGroupActivityBinding
//    private var sharedPreference: SharedPreference? = null
//    private var ageGroupActivityPresenterImplementation: AgeGroupActivityPresenterImplementation? =
//        null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.age_group_activity)
//        sharedPreference = SharedPreference(this)
//        ageGroupActivityPresenterImplementation =
//            AgeGroupActivityPresenterImplementation(this, this)
//        setupClickListener()
//        setupDataView()
//    }
//
//    private fun setupDataView() {
//
//        binding.imgEighteen.setBackgroundResource(R.drawable.plus_18_disabled)
//        binding.imgEighteen.isEnabled = true
//        binding.imgTwentyOne.setBackgroundResource(R.drawable.plus_21_disabled)
//        binding.imgTwentyOne.isEnabled = true
//
//
////        if(sharedPreference!!.getValueString(ConstantLib.USER_AGE)!!.toInt() < 21){
////            binding.imgTwentyOne.setBackgroundResource(R.drawable.plus_unchecked_21)
////            binding.imgTwentyOne.isEnabled = false
////        }else{
////            binding.imgTwentyOne.setBackgroundResource(R.drawable.plus_21_disabled)
////            binding.imgTwentyOne.isEnabled = true
////            binding.imgEighteen.setBackgroundResource(R.drawable.plus_unchecked_18)
////            binding.imgEighteen.isEnabled = false
////        }
//    }
//
//    private fun setupClickListener() {
//        binding.imgEighteen.setOnClickListener {
//            binding.imgEighteen.setBackgroundResource(R.drawable.plus_18)
//            doCallAgeGroupApi("1")
//        }
//
//        binding.imgTwentyOne.setOnClickListener {
//            binding.imgTwentyOne.setBackgroundResource(R.drawable.plus_21)
//            doCallAgeGroupApi("2")
//        }
//    }
//
//    private fun doCallAgeGroupApi(data: String) {
//        val input: JsonObject = JsonObject()
//        input.addProperty("age_group", data)
//        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
//        ageGroupActivityPresenterImplementation!!.doCallAgeGroupApi(
//            input,
//            Utility.createHeaders(sharedPreference)
//        )
//    }
//
//
//    override fun validateError(message: String) {
//        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
//    }
//
//    override fun logoutUser() {
//        Utility.showLogoutPopup(applicationContext, lan!!.session_error)
//    }
//
//    override fun onAgeGroupApiSuccess(responseData: AgeGroupResponse) {
//        val intent = Intent(this, AHomeScreen::class.java)
//        startActivity(intent)
//        finishAffinity()
//    }
//}