package com.saurabh.gitreposearchkotlin.model

/**
 * Created by kiris on 2/20/2018.
 */
class RepoItemResponse{
    private var name: String? = null
    private var description:String? = null
    private var subscribers_url:String? = null
    private var forks_count: Int = 0
    private var owner:OwnerResponse?=null

    fun getName():String?{
        return name
    }

    fun setName(name:String){
        this.name=name
    }

    fun getDescription():String?{
        return description
    }

    fun setDescription(description:String){
        this.description=description
    }

    fun getSubscribers_url():String?{
        return subscribers_url
    }

    fun setSubscriber_url(subscriber_url:String){
        this.subscribers_url=subscribers_url
    }

    fun getForks_count():Int?{
        return forks_count
    }

    fun setForks_count(forks_count:Int){
        this.forks_count=forks_count
    }

    fun getOwner():OwnerResponse?{
        return owner
    }

    fun getOwner(owner:OwnerResponse){
        this.owner=owner
    }
}