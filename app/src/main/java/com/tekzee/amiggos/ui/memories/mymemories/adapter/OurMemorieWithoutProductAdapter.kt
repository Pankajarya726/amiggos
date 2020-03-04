package com.tekzee.amiggos.ui.memories.mymemories.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.amiggos.ui.memories.mymemories.MyMemorieClickListener
import kotlinx.android.synthetic.main.single_first_fragment.view.img_user_firstfragment


class OurMemorieWithoutProductAdapter(

    var mDataList: ArrayList<StoriesData>,
    var listener: MyMemorieClickListener
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
            Glide.with(itemView.context).load(mDataList[adapterPosition].content[0].url).placeholder(R.drawable.blackbg).into(itemView.img_user_firstfragment)
            itemView.img_user_firstfragment.setOnClickListener {
                listener.onMemorieClicked(mDataList[adapterPosition])
            }
        }

    }


}