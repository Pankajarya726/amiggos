package com.tekzee.amiggos.ui.memories.mymemoriesold.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.memories.mymemoriesold.OurMemorieFragmentClickListener
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import kotlinx.android.synthetic.main.ourmemoriewithoutproduct_layout.view.*
import kotlinx.android.synthetic.main.single_first_fragment.view.img_user_firstfragment


class OurMemorieWithoutProductAdapter(

    var mDataList: ArrayList<MemorieResponse.Data.Memories>,
    var listenerFragment: OurMemorieFragmentClickListener
)
    : RecyclerView.Adapter<OurMemorieWithoutProductAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ourmemoriewithoutproduct_layout, parent, false))
    }

    override fun getItemCount(): Int = mDataList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind() {
            Glide.with(itemView.context).load(mDataList[adapterPosition].thumb_image).placeholder(R.drawable.noimage).into(itemView.img_user_firstfragment)
            itemView.img_layout.setOnClickListener {
                listenerFragment.onMemorieClicked(mDataList[adapterPosition],adapterPosition)
            }
        }

    }


}