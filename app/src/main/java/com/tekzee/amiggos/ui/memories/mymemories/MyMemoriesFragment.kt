package com.tekzee.amiggos.ui.memories.mymemories

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekzee.amiggos.R
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.MyMemoriesFragmentNewBinding
import com.tekzee.amiggos.ui.cameranew.CameraActivity
import com.tekzee.amiggos.ui.memories.mymemories.pagingfeaturedbrand.NewFeaturedBrandAdapter
import com.tekzee.amiggos.ui.memories.mymemories.pagingfeaturedbrand.NewMemorieAdapter
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import com.tekzee.amiggos.ui.storieviewnew.StorieViewNew
import com.tekzee.amiggos.util.SharedPreference
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MyMemoriesFragment : Fragment(), KodeinAware, MemorieClickListener,
    FeaturedBrandClickListener {

    private var featuredbrandAdapter: NewFeaturedBrandAdapter? =null
    private lateinit var memoryList:ArrayList<MemorieResponse.Data.Memories>
    private var memorieAdapter: NewMemorieAdapter?=null
    override val kodein: Kodein by closestKodein()
    val factory: MyMemorieViewModelFactory by instance<MyMemorieViewModelFactory>()

    val prefs: SharedPreference by instance<SharedPreference>()

    companion object {
        private val myMemoriesFragment: MyMemoriesFragment? = null


        fun newInstance(): MyMemoriesFragment {

            if(myMemoriesFragment == null){
                return MyMemoriesFragment()
            }
            return myMemoriesFragment

        }
    }

    private var binding: MyMemoriesFragmentNewBinding? = null
    private lateinit var viewModel: MyMemoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.my_memories_fragment_new, container, false)
        prefs.save(ConstantLib.FROM,"")
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(MyMemoriesViewModel::class.java)
        setupClickListener()


    }

    private fun setupClickListener() {
        binding!!.userImage.setOnClickListener{
            val intent = Intent(requireContext(), CameraActivity::class.java)
            intent.putExtra(ConstantLib.FROM_ACTIVITY, ConstantLib.MYMEMORY)
            intent.putExtra(ConstantLib.OURSTORYID, "")
            intent.putExtra(ConstantLib.USERNAME,prefs.getValueString(ConstantLib.NAME))
            startActivity(intent)
        }
    }

    private fun setupMemorieAdapter() {
        onStartedMemorie()
        observeMemorieLiveData()
    }

    private fun observeMemorieLiveData() {


        //observe live data emitted by view model
        viewModel.getMemorieProducts().observe(viewLifecycleOwner, Observer {
            memorieAdapter!!.submitList(it)
            onLoadedMemorie()
        })


        binding!!.reyclerviewMemorie.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding!!.reyclerviewMemorie.adapter = memorieAdapter
    }

    private fun setupFeaturedBrandNew() {
        onStarted()
        observeLiveData()
    }

    private fun observeLiveData() {
        featuredbrandAdapter = NewFeaturedBrandAdapter(this)
        //observe live data emitted by view model
        viewModel.getFeaturedBrandProducts().observe(viewLifecycleOwner, Observer {
            featuredbrandAdapter!!.submitList(it)
            onLoaded()
        })


        binding!!.featuredbrandRecyclerview.layoutManager = GridLayoutManager(
            activity,
            2
        )
        binding!!.featuredbrandRecyclerview.adapter = featuredbrandAdapter
    }


    override fun onItemClicked(itemData: MemorieResponse.Data.Memories, adapterPosition: Int) {

        if(itemData.memory.isNotEmpty()){
            val showMemoryList: ArrayList<MemorieResponse.Data.Memories> = ArrayList()
            for(position in memorieAdapter!!.currentList!!.toList().indices){
                if(position>=adapterPosition){
                    if(memorieAdapter!!.currentList!![position]!!.memory.size>0){
                        showMemoryList.add(memorieAdapter!!.currentList!!.toList()[position])
                    }

                }
            }

            val intent = Intent(requireContext(), StorieViewNew::class.java)
            intent.putExtra(ConstantLib.MEMORIE_DATA,itemData)
            intent.putExtra(ConstantLib.COMPLETE_MEMORY,showMemoryList)
            intent.putExtra(ConstantLib.FROM,ConstantLib.MEMORIES)
            prefs.save(ConstantLib.TYPEFROM,ConstantLib.MEMORIES)
            intent.putExtra(ConstantLib.BACKFROM,"")
            intent.putExtra(ConstantLib.DELETED_POSITION, 0)
            intent.putExtra(ConstantLib.POSITION, adapterPosition)
            startActivity(intent)
        }

    }

    fun onStarted() {
        binding!!.fprogressCircular.visibility = View.VISIBLE
        binding!!.featuredbrandRecyclerview.visibility = View.GONE
    }

    fun onLoaded() {
        binding!!.fprogressCircular.visibility = View.GONE
        binding!!.featuredbrandRecyclerview.visibility = View.VISIBLE
    }

    fun onStartedMemorie() {
        binding!!.progressCircular.visibility = View.VISIBLE
        binding!!.reyclerviewMemorie.visibility = View.GONE
    }

    fun onLoadedMemorie() {
        binding!!.progressCircular.visibility = View.GONE
        binding!!.reyclerviewMemorie.visibility = View.VISIBLE
    }

    override fun onItemClickedBrand(itemData: MemorieResponse.Data.Memories, adapterPosition: Int) {
        if(itemData.memory.isNotEmpty()){
            val showMemoryList: ArrayList<MemorieResponse.Data.Memories> = ArrayList()
            for(position in featuredbrandAdapter!!.currentList!!.toList().indices){
                if(position>=adapterPosition){
                    if(featuredbrandAdapter!!.currentList!![position]!!.memory.size>0){
                        showMemoryList.add(featuredbrandAdapter!!.currentList!!.toList()[position])
                    }
                }
            }
            val intent = Intent(requireContext(),StorieViewNew::class.java)
            intent.putExtra(ConstantLib.MEMORIE_DATA,itemData)
            intent.putExtra(ConstantLib.FROM,ConstantLib.MEMORIES)
            intent.putExtra(ConstantLib.COMPLETE_MEMORY,showMemoryList)
            prefs.save(ConstantLib.TYPEFROM,ConstantLib.MEMORIES)
            intent.putExtra(ConstantLib.DELETED_POSITION, 0)
            intent.putExtra(ConstantLib.BACKFROM,"")
            startActivity(intent)
        }

    }

    override fun onResume() {

        Log.e("Onresume is called","-----------------")
        memorieAdapter = NewMemorieAdapter(this)
        setupMemorieAdapter()
        setupFeaturedBrandNew()
        super.onResume()
    }

}
