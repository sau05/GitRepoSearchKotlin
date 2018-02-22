package com.saurabh.gitreposearchkotlin.model

import java.util.ArrayList

/**
 * Created by kiris on 2/20/2018.
 */
class ItemsResponse {

    private var items: ArrayList<RepoItemResponse>? = null

    fun getItems(): ArrayList<RepoItemResponse>? {
        return items
    }

    fun setItems(items: ArrayList<RepoItemResponse>) {
        this.items = items
    }
}