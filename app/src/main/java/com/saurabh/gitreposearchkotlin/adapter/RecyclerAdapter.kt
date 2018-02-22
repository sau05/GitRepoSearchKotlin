package com.saurabh.gitreposearchkotlin.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.saurabh.gitreposearchkotlin.R
import com.saurabh.gitreposearchkotlin.listener.RecycleViewClickListener
import com.saurabh.gitreposearchkotlin.model.RepoItemResponse
import com.saurabh.gitreposearchkotlin.request.CustomImageRequest
import java.util.ArrayList

/**
 * Created by kiris on 2/20/2018.
 */
class RecyclerAdapter(context: Context, private val items: ArrayList<RepoItemResponse>) : RecyclerView.Adapter<RecyclerAdapter.SimpleViewHolder>() {

    private val inflater: LayoutInflater
    private var mClickListener: RecycleViewClickListener? = null
    private val imageLoader: ImageLoader

    init {
        inflater = LayoutInflater.from(context)
        imageLoader = CustomImageRequest.getInstance(context).getImageLoader()
    }

    fun setClickListener(clickListener: RecycleViewClickListener) {
        this.mClickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.tvName.setText(items[position].getName())
        holder.tvDesc.setText(items[position].getDescription())
        holder.tvForks.text = "Forks : " + items[position].getForks_count()
        holder.networkImageView.setImageUrl(items[position].getOwner()!!.getAvatar_url(), imageLoader)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var tvName: TextView
        var tvDesc: TextView
        var tvForks: TextView
        var networkImageView: NetworkImageView

        init {
            tvName = itemView.findViewById(R.id.tvName) as TextView
            tvDesc = itemView.findViewById(R.id.tvDescription) as TextView
            tvForks = itemView.findViewById(R.id.tvForks) as TextView
            networkImageView = itemView.findViewById(R.id.networkIV) as NetworkImageView
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) {
                mClickListener!!.onItemClick(view, adapterPosition)
            }
        }
    }
}
