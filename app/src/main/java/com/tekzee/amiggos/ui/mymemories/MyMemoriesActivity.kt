//package com.tekzee.amiggos.ui.mymemories
//
//import android.os.Bundle
//import android.view.MenuItem
//import android.widget.Toast
//import androidx.appcompat.widget.Toolbar
//import androidx.databinding.DataBindingUtil
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentTransaction
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.databinding.ActivityMyMemoriesBinding
//import com.tekzee.amiggos.ui.mymemories.fragment.memories.Memories
//import com.tekzee.amiggos.ui.mymemories.fragment.ourmemories.OurMemories
//import com.tekzee.amiggos.base.BaseActivity
//import com.tekzee.amiggos.base.model.LanguageData
//import com.tekzee.amiggos.util.SharedPreference
//import com.tekzee.amiggos.constant.ConstantLib
//import kotlinx.android.synthetic.main.activity_realfriends.*
//
//class MyMemoriesActivity : BaseActivity() {
//
//    private lateinit var binding: ActivityMyMemoriesBinding
//    private var sharedPreference: SharedPreference? = null
//    private var languageData: LanguageData? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_memories)
//        sharedPreference = SharedPreference(this)
//        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
//        setupToolBar()
//        setupViewData()
//        LoadFirstFragment()
//
//
//
//    }
//
//    private fun LoadFirstFragment() {
//        LoadFragment(Memories())
//    }
//
//    private fun LoadSecondFragment() {
//        LoadFragment(OurMemories())
//    }
//
//    fun LoadFragment(fragment: Fragment){
//        val fm: FragmentManager = supportFragmentManager
//        val fragmentTransaction : FragmentTransaction = fm.beginTransaction()
//        fragmentTransaction.replace(R.id.container,fragment)
//        fragmentTransaction.commit()
//    }
//
//
//
//    private fun setupViewData() {
//
//
//        binding.radio2.text = languageData!!.klOURMEMORY
//        binding.radio1.text = languageData!!.klMemories
//
//
//        binding.radio1.isChecked = true
//
//        if(radio1.isChecked){
//            binding.radio1.setTextColor(resources.getColor(R.color.white))
//        }else{
//            binding.radio1.setTextColor(resources.getColor(R.color.colorPrimary))
//        }
//
//        if(radio2.isChecked){
//            binding.radio2.setTextColor(resources.getColor(R.color.white))
//        }else{
//            binding.radio2.setTextColor(resources.getColor(R.color.colorPrimary))
//        }
//
//
//        binding.radio1.setOnCheckedChangeListener{
//            compoundButton, b ->
//            if(b){
//                binding.radio1.setTextColor(resources.getColor(R.color.white))
//            }else{
//                binding.radio1.setTextColor(resources.getColor(R.color.colorPrimary))
//            }
//        }
//
//
//        binding.radio2.setOnCheckedChangeListener{
//                compoundButton, b ->
//            if(b){
//                binding.radio2.setTextColor(resources.getColor(R.color.white))
//            }else{
//                binding.radio2.setTextColor(resources.getColor(R.color.colorPrimary))
//            }
//        }
//
//
//        binding.radiogroup.setOnCheckedChangeListener{
//            radioGroup, checkedId ->
//
//            if(checkedId == R.id.radio1){
//                LoadFirstFragment()
//            }else{
//                LoadSecondFragment()
//            }
//        }
//    }
//
//
//    private fun setupToolBar() {
//        val toolbar: Toolbar = binding.toolbar
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//        binding.title.text = languageData!!.klMYMEMORY
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        if (item.itemId == android.R.id.home) {
//            onBackPressed()
//        }
//
//        return super.onOptionsItemSelected(item)
//    }
//
//
//    override fun validateError(message: String) {
//
//        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//    }
//}