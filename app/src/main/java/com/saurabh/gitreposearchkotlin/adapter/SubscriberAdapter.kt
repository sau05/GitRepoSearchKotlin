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
import com.saurabh.gitreposearchkotlin.model.SubscriberResponse
import com.saurabh.gitreposearchkotlin.request.CustomImageRequest

/**
 * Created by kiris on 2/20/2018.
 */
class SubscriberAdapter  {

    private var imageLoader: ImageLoader?=null
    private var inflater:LayoutInflater?=null
    private var subscriberResponses:List<SubscriberResponse>?=null
    constructor(context:Context, subscriberResponseList:List<SubscriberResponse>){
        this.subscriberResponses=subscriberResponseList
        inflater= LayoutInflater.from(context)
        imageLoader=CustomImageRequest.getInstance(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = inflater!!.inflate(R.layout.sub_list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvName.setText(subscriberResponses!!.get(position).getLogin())
        holder.networkImageView.setImageUrl(subscriberResponses!!.get(position).getAvatar_url(), imageLoader)
    }

    override fun getItemCount(): Int {
        return subscriberResponses.size
    }

    class Holder : RecyclerView.ViewHolder {

        var tvName: TextView = null
        var networkImageView: NetworkImageView = null

        constructor(itemView: View) :
                super(itemView) {
            tvName = itemView.findViewById(R.id.subName) as TextView
            networkImageView = itemView.findViewById(R.id.subAvatar) as NetworkImageView
        }
    }
}