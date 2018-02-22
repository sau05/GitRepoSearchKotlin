package com.saurabh.gitreposearchkotlin.model

/**
 * Created by kiris on 2/20/2018.
 */
class OwnerResponse {

    private var avatar_url:String?=null

    fun getAvatar_url():String?{
        return avatar_url
    }

    fun setAvatar_url(avatar_url:String){
        this.avatar_url=avatar_url
    }
}